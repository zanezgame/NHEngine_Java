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
    
    public delegate void OnCreate(int result);
    public delegate void OnLogin(int result, int accountId, String token, int lastAreaId, List<nicehu.pb.Area> areas);
    
    public delegate void OnAuthToken(int result);
    public delegate void OnQueryPlayer(int result, nicehu.clientcore.Player player);
    public delegate void OnServerDisconnect(String result, int playerId);
    public partial class ClientCore
    {
        public Connection connection;
        public ProxyConnection proxyConnection;
        public Transmitter transmitter;
        public Serializer serializer;
        public HandlerMgr handlerMgr;


        public int accountId;
        public string token;
        public string proxyIp;
        public int proxyPort;


        public ClientCore()
        {
            proxyConnection = new ProxyConnection(this);
            transmitter = new Transmitter(this);
            serializer = new Serializer(this);
            handlerMgr = new HandlerMgr();

            Init_Login();
        }

        public void Update()
        {
            if (connection != null)
            {
                connection.Update();
            }
        }

        public bool ConnectProxy(string ip, int port)
        {
            return proxyConnection.ConnectProxy(ip, port);
        }

        public bool reConnect()
        {
            if (connection == null)
            {
                LogU.Debug("reconnect found connection is null");
                return false;
            }

            if (!connection.HasInit())
            {
                return false;
            }
            
            connection.ReInit();
            connection.Connect();

            return true;
        }


        public bool DisconnectProxy()
        {
            if (connection == null)
            {
                return false;
            }
            connection.Disconnect(true);

            return true;
        }


        public Serializer Serializer
        {
            get { return serializer; }
            set { serializer = value; }
        }

        public HandlerMgr HandlerMgr
        {
            get { return handlerMgr; }
            set { handlerMgr = value; }
        }

        public Transmitter Transmitter
        {
            get { return transmitter; }
            set { transmitter = value; }
        }

        public ProxyConnection ProxyConnection
        {
            get { return proxyConnection; }
            set { proxyConnection = value; }
        }

        public Connection Connection
        {
            get { return connection; }
            set { connection = value; }
        }


        public string Token
        {
            get { return token; }
            set { token = value; }
        }

        public int AccountId
        {
            get { return accountId; }
            set { accountId = value; }
        }

    }
}

