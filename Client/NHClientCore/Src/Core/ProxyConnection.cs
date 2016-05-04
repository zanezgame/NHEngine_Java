using System;
using System.Collections.Generic;
using System.Text;
using System.Threading;
using System.Net;
using System.Net.Sockets;
using nicehu.net;
using nicehu.common;
using nicehu.pb;


namespace nicehu.clientcore
{
    public  class ProxyConnection
    {
        public ClientCore clientCore;

        public ProxyConnection(ClientCore clientCore)
        {
            this.clientCore = clientCore;
        }

        public bool ConnectProxy(string ip, int port)
        {
            if (clientCore.connection != null && clientCore.Connection.socket.Connected)
            {
                clientCore.connection.Disconnect(false);
            }

            clientCore.proxyIp = ip;
            clientCore.proxyPort = port;

            IPAddress host = IpU.GetIPV4Address(ip);
            if (host == null)
            {
                return false;
            }
            
            clientCore.connection =new Connection(new IPEndPoint(host, port));
            clientCore.connection.OnConnected = OnMyConnected;
            clientCore.connection.OnDisconnected = OnMyDisconnected;
            clientCore.connection.OnReceived = OnMyReceived;
            clientCore.connection.Connect();

            return true;
        }

        public void OnMyConnected(Connection connection, SocketError result)
        {
            LogU.Debug("Connect ProxyServer: {0}", result);
            if (result == SocketError.Success)
            {
                clientCore.transmitter.Connection = connection;
            }
            else
            {
                LogU.Debug("Connect ProxyServer Faild !!! :{0}", result.ToString("X"));
               
            }
        }

        public void OnMyReceived(Connection connection, byte[] buffer, int offset, int size)
        {
            if (size == -1)
            {
                LogU.Debug(" frameDecoder Decode failed then reConnect");
                connection.Disconnect(true);
                clientCore.reConnect();
                return;
            }
            
            
            Message msg = clientCore.Serializer.Deserialize(buffer, offset, size);
            clientCore.handlerMgr.handle(msg);
        }

        public void OnMyDisconnected(Connection connection, SocketError error)
        {
            LogU.Debug("ProxyServer Connect Disconnect :{0}", error.ToString("X"));
        }

    }
}

