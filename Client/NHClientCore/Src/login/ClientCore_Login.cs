using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using nicehu.pb;
using System.Net;


namespace nicehu.clientcore
{
    public partial class ClientCore
    {
        //AuthServer
        private CreateHandler createHandler;
        private LoginHandler loginHandler;

        //GameServer
        private AuthTokenHandler authTokenHandler;
        private QueryPlayerHandler queryPlayerHandler;
        private ServerDisconnectHandler serverDisconnectHandler;

        
        public OnCreate onCreate;
        public OnLogin onLogin;
        public OnAuthToken onAuthToken;
        public OnQueryPlayer onQueryPlayer;
        public OnServerDisconnect onServerDisconnect;

        public void Init_Login()
        {
            
            createHandler = new CreateHandler(this);
            loginHandler = new LoginHandler(this);

            queryPlayerHandler = new QueryPlayerHandler(this);
            authTokenHandler = new AuthTokenHandler(this);
            serverDisconnectHandler = new ServerDisconnectHandler(this);

            this.handlerMgr.addHandler((int)EGMI.EGMI_AUTH_TOKEN_RES, authTokenHandler.OnAuthTokeRes);
            this.handlerMgr.addHandler((int)EGMI.EGMI_QUERY_PLAYER_RES, queryPlayerHandler.OnQueryPlayerRes);
            this.handlerMgr.addHandler((int)EGMI.EGMI_SERVER_DISCONNECT, serverDisconnectHandler.OnServerDisconnect);
        }

        public bool create(string ip, int port, string account, string pass, nicehu.pb.DeviceInfo deviceInfo)
        {
            IPAddress ipAddress = IpU.GetIPV4Address(ip);
            if (ipAddress == null)
            {
                return false;
            }
            createHandler.Create(new IPEndPoint(ipAddress, port),  account, pass, deviceInfo);
            return true;
        }
        public bool login(string ip, int port, String account, String pass, nicehu.pb.DeviceInfo deviceInfo)
        {
            string guid = Guid.NewGuid().ToString();

            IPAddress host = IpU.GetIPV4Address(ip);
            if (host == null)
            {
                return false;
            }

            loginHandler.Login(new IPEndPoint(host, port), account, pass, guid, deviceInfo);
            return true;
        }

        public bool authToken()
        {
            return authTokenHandler.AuthTokenReq(accountId, token);
        }
        
        public bool queryPlayer()
        {
            return queryPlayerHandler.QueryPlayerReq();
        }
    }
}
