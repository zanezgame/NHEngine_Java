using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Sockets;
using System.Text;
using nicehu.common;

namespace nicehu.net
{
    public class EventArgsMgr
    {
        private Connection con;

        public Queue<SocketAsyncEventArgs> freeEventArgs = new Queue<SocketAsyncEventArgs>();              //free EventArgs
        public Queue<SocketAsyncEventArgs> appendEventArgs = new Queue<SocketAsyncEventArgs>();            //eventArgs need to be handle

        public EventArgsMgr(Connection  con)
        {
            this.con = con;
        }

        //get a AsyncEvent
        public  SocketAsyncEventArgs GetEventArg( bool needBuffer)
        {
            lock (con.eventLock)
            {
                if (freeEventArgs.Count > 0)
                {
                    SocketAsyncEventArgs eventArg = freeEventArgs.Dequeue();
                    if (needBuffer && eventArg.Buffer == null)
                    {
                        con.bufferMgr.SetBuffer(eventArg);
                    }
                    else if (needBuffer == false && eventArg.Buffer != null)
                    {
                        con.bufferMgr.FreeBuffer(eventArg);
                    }
                    return eventArg;
                }
                else if (con.evenCount < NHNet.EVENTARGS_MAX_COUNT)
                {
                    con.evenCount++;
                    SocketAsyncEventArgs eventArg = new SocketAsyncEventArgs();
                    eventArg.RemoteEndPoint = con.ipPort;
                    eventArg.Completed += new EventHandler<SocketAsyncEventArgs>(SocketEventArg_Completed);
                    if (needBuffer)
                    {
                        con.bufferMgr.SetBuffer(eventArg);
                    }
                    return eventArg;
                }
                return null;
            }
        }

        // 连接  Connect,Send,Receive等Connect操作的回调方法
        public void SocketEventArg_Completed(object sender, SocketAsyncEventArgs e)
        {
            con.HandleAsyncEvent(e, true);
        }

        //free a AsyncEvent
        public  void PushFreeEventArg(Connection con, SocketAsyncEventArgs eventArg, bool clearBuffer)
        {
            lock (con.eventLock)
            {
                if (clearBuffer)
                {
                    if (eventArg.Buffer != null)
                    {
                       con.bufferMgr.FreeBuffer(eventArg);
                    }
                    else
                    {
                        eventArg.SetBuffer(null, 0, 0);
                    }
                }
                freeEventArgs.Enqueue(eventArg);
            }
        }

        //add need to handle EventArg
        public  void PushAppendEventArg(SocketAsyncEventArgs eventArg)
        {
            lock (con.eventLock)
            {
                appendEventArgs.Enqueue(eventArg);
            }
        }

       

    }
}
