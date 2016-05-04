using System;
using System.Collections.Generic;
using System.Text;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using nicehu.common;

namespace nicehu.net
{
    public delegate void OnConnected(Connection connection, SocketError result);
    public delegate void OnReceived(Connection connection, byte[] buffer, int offset, int size);
    public delegate void OnSend(Connection connection, Object userData);
    public delegate void OnDisconnected(Connection connection, SocketError error);

    public class Connection
    {
        public OnConnected OnConnected;
        public OnReceived OnReceived;
        public OnSend OnSend;
        public OnDisconnected OnDisconnected;
        
        public IPEndPoint ipPort;
        public NetworkHandler netHandler;
        public FrameDecoder frameDecoder;
        public BufferMgr bufferMgr;
        public EventArgsMgr EventArgsMgr;
        public Socket socket;
        public int status;

        private bool hasInit = false;//Connect是否已经初始化过
        public int evenCount;//当前总EventArg数量
        public Object eventLock = new Object();
        public Object statusLock = new Object();

        public Connection( IPEndPoint ipPort)
        {
            this.ipPort = ipPort;
            this.netHandler = new NetworkHandler();
            this.frameDecoder = new LengthFieldBasedFrameDecoder(NHNet._MAX_COMPRESS_MESSAGE_SIZE, 2, 4, 0, 0);
            this.bufferMgr = new BufferMgr();
            this.EventArgsMgr = new EventArgsMgr(this);
            this.evenCount = 0;
            hasInit = true;

            this.socket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
            this.socket.NoDelay = true;
            this.status = NHNet.CON_CLOSED;
        }


        public void ReInit()
        {
            try
            {
                socket.Close();

                this.netHandler = new NetworkHandler();
                this.frameDecoder = new LengthFieldBasedFrameDecoder(NHNet._MAX_COMPRESS_MESSAGE_SIZE, 2, 4, 0, 0);
                this.bufferMgr = new BufferMgr();
                lock (eventLock)
                {
                    this.EventArgsMgr = new EventArgsMgr(this);
                    evenCount = 0;
                }

                this.socket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
                this.socket.NoDelay = true;
                this.status = NHNet.CON_CLOSED;
            }
            catch (Exception ex)
            {
                LogU.Warn("reInitConection exception {0}", ex.ToString());
            }
        }
        
        public void Connect()
        {
            lock (statusLock)
            {
                status = NHNet.CON_CONNECTING;
            }
            SocketAsyncEventArgs eventArg = EventArgsMgr.GetEventArg(false);
            if (eventArg == null)
            {
                LogU.Error("Connect----Can't eventArg when connecting");
                return;
            }
            //无论连接成功还是失败,都会返回true,自动调用ea的Completed时间,不过e中状态不同.当网络问题,导致超时的时候,会返回false,不会自动调用Completed事件.ps: 在createaccount协议中有失败自动重连两次的逻辑
            bool willRaiseEvent = socket.ConnectAsync(eventArg);
            if (!willRaiseEvent)
            {
                ProcessConnect(eventArg, false);
            }
        }
        public void Disconnect(bool isClearData)
        {
            try
            {
                hasInit = false;
                socket.Shutdown(SocketShutdown.Both);
                socket.Disconnect(true);
            }
            catch (Exception e)
            {
                socket.Close();
                LogU.Error("Disconnect exception details {0}", e.Message);
            }

            lock (statusLock)
            {
                status = NHNet.CON_CLOSED;
            }
        }

        public bool Send(byte[] buffer, int offset, int count)
        {
            SocketAsyncEventArgs eventArg = EventArgsMgr.GetEventArg(false);
            if (eventArg == null)
            {
                LogU.Error("There is no SocketAsyncEventArgs available");
                return false;
            }
            if (bufferMgr.SetBuffer(eventArg, buffer, offset, count) == false)
            {
                LogU.Error("There is no memory available in buffermanager");
                return false;
            }

            bool willRaiseEvent = socket.SendAsync(eventArg);
            if (!willRaiseEvent)
            {
                ProcessSend(eventArg, false);
            }
            return true;
        }

        //handle every EventArgs
        public void Update()
        {
            Queue<SocketAsyncEventArgs> asyncEvnts = new Queue<SocketAsyncEventArgs>();
            lock (eventLock)
            {
                asyncEvnts = EventArgsMgr.appendEventArgs;
                EventArgsMgr.appendEventArgs = new Queue<SocketAsyncEventArgs>();
            }

            while (asyncEvnts.Count > 0)
            {
                HandleAsyncEvent(asyncEvnts.Dequeue(), false);
            }
        }

        public void HandleAsyncEvent(SocketAsyncEventArgs e, Boolean firstStep)
        {
            switch (e.LastOperation)
            {
                case SocketAsyncOperation.Connect:
                    //LogU.Debug("Receive SocketAsyncOperation.Connect");
                    ProcessConnect(e, firstStep);
                    break;
                case SocketAsyncOperation.Receive:
                    //LogU.Debug("Receive SocketAsyncOperation.Receive  {0}", e.BytesTransferred);
                    ProcessReceive(e, firstStep);
                    break;
                case SocketAsyncOperation.Send:
                    //LogU.Debug("Receive SocketAsyncOperation.Send");
                    ProcessSend(e, firstStep);
                    break;
                default:
                    LogU.Error("Invalid operation completed");
                    throw new Exception("Invalid operation completed");
            }
        }

       

        public void ProcessConnect(SocketAsyncEventArgs e, bool firstStep)
        {
            if (firstStep)
            {
                //add to handle list
                EventArgsMgr.PushAppendEventArg(e);

                //if success,receive data
                if (e.SocketError == SocketError.Success)
                {
                    SocketAsyncEventArgs eventArg = EventArgsMgr.GetEventArg(true);
                    if (eventArg == null)
                    {
                        return;
                    }
                    bool willRaiseEvent = socket.ReceiveAsync(eventArg);
                    if (!willRaiseEvent)
                    {
                        ProcessReceive(eventArg, false);
                    }
                }

                return;
            }

            if (e.SocketError == SocketError.Success)
            {
                if (OnConnected != null)
                {
                    OnConnected(this, e.SocketError);
                }
                EventArgsMgr.PushFreeEventArg(this, e, true);
                lock (statusLock)
                {
                    status = NHNet.CON_CONNECTED;
                }
            }
            else if (e.SocketError == SocketError.IsConnected)
            {
                if (socket.Connected == true)
                {
                    if (OnConnected != null)
                    {
                        OnConnected(this, e.SocketError);
                    }
                    EventArgsMgr.PushFreeEventArg(this, e, true);
                    lock (statusLock)
                    {
                        status = NHNet.CON_CONNECTED;
                    }
                }
                else
                {
                    try
                    {
                        lock (statusLock)
                        {
                            status = NHNet.CON_CLOSED;
                        }
                        socket.Shutdown(SocketShutdown.Both);
                        socket.Disconnect(true);
                    }
                    catch (Exception ex)
                    {
                        LogU.Warn("ProcessConnect shutdown or disconnect found socket stat is confilct exception {0}", ex.ToString());
                    }
                    if (OnConnected != null)
                    {
                        OnConnected(this, SocketError.SocketError);
                    }
                    EventArgsMgr.PushFreeEventArg(this, e, true);
                    return;
                }
            }
            else
            {
                LogU.Error("Failed to connect to {0}, Error Code:{1}", ipPort.ToString(), e.SocketError);
                if (OnConnected != null)
                {
                    OnConnected(this, e.SocketError);
                }
                EventArgsMgr.PushFreeEventArg(this, e, true);

                lock (statusLock)
                {
                    status = NHNet.CON_CLOSED;
                }
            }
        }

        public void ProcessReceive(SocketAsyncEventArgs e, bool firstStep)
        {
            if (firstStep)
            {
                SocketError socketError = e.SocketError;
                int bytesTransferred = e.BytesTransferred;
                EventArgsMgr.PushAppendEventArg(e);
                if (socketError == SocketError.Success && bytesTransferred > 0)
                {
                    SocketAsyncEventArgs eventArg = EventArgsMgr.GetEventArg(true);
                    if (eventArg == null)
                    {
                        try
                        {
                            lock (statusLock)
                            {
                                status = NHNet.CON_CLOSED;
                            }
                            socket.Shutdown(SocketShutdown.Both);
                            socket.Disconnect(true);
                        }
                        catch (Exception)
                        {
                        }
                        return;
                    }
                    else
                    {
                        bool willRaiseEvent = socket.ReceiveAsync(eventArg);
                        if (!willRaiseEvent)
                        {
                            ProcessReceive(eventArg, false);
                        }
                    }
                }

                return;
            }
            if (e.SocketError == SocketError.Success && e.BytesTransferred > 0)
            {
                // Data has now been sent and received from the server.
                if (OnReceived != null)
                {
                    try
                    {
                        frameDecoder.OnReceived(this, e.Buffer, e.Offset, e.BytesTransferred);
                    }
                    catch (Exception ex)
                    {
                        LogU.Error("ProcessReceive exception {0}", ex.Message);
                        LogU.Error(ex.ToString());
                    }
                }
                EventArgsMgr.PushFreeEventArg(this, e, false);
            }
        }

        public void ProcessSend(SocketAsyncEventArgs e, bool firstStep)
        {
            if (firstStep)
            {
                EventArgsMgr.PushAppendEventArg(e);
                return;
            }
            if (e.SocketError == SocketError.Success)
            {
                if (OnSend != null)
                {
                    OnSend(this, e.UserToken);
                }
            }
            EventArgsMgr.PushFreeEventArg(this, e, true);
        }

        public int getStatus()
        {
            int tmp = 0;
            lock (statusLock)
            {
                tmp = status;
            }
            return tmp;
        }

        public bool HasInit()
        {
            return hasInit;
        }

    }
}
