using System;
using System.Net;
using System.Net.Sockets;
using System.Collections.Generic;
using nicehu.net;
using nicehu.common;
using nicehu.pb;


namespace nicehu.clientcore
{
    public class LoginHandler
    {
        private ClientCore clientCore;

        private string guid = "";
        private string account;
        private string pass = "";
        private nicehu.pb.DeviceInfo deviceInfo = new nicehu.pb.DeviceInfo();


        public LoginHandler(ClientCore ClientCore)
        {
            this.clientCore = ClientCore;
        }


        public void Login(IPEndPoint ipPort, String account, String pass, String guid,  nicehu.pb.DeviceInfo deviceInfo)
        {
            if (clientCore.Connection != null && clientCore.Connection.socket.Connected)
            {
                clientCore.Connection.Disconnect(true);
            }
            this.account = account;
            this.pass = pass;
            this.guid = guid;
            this.deviceInfo = deviceInfo;
            if (deviceInfo == null)
            {
                LogU.Error("DeviceInfo is null");
            }
            clientCore.Connection = new Connection(ipPort);
            clientCore.Connection.OnConnected = OnMyConnected;
            clientCore.Connection.OnReceived = OnMyReceived;
            clientCore.Connection.Connect();
        }

        void OnMyConnected(Connection connection, System.Net.Sockets.SocketError result)
        {
            if (result == System.Net.Sockets.SocketError.Success)
            {
                LoginReq request = new LoginReq();
                request.account = account;
                request.pass = pass;
                request.randomSeed = guid;
                request.deviceInfo = deviceInfo;

                //if (MessageStatistic.isOpen) loginReq.seq = MessageStatistic.curTimeMS();
                if (clientCore.Transmitter.SendToAuth(connection, request, (int)EGMI.EGMI_LOGIN_REQ) == false)
                {
                    if (clientCore != null && clientCore.onLogin != null)
                    {
                        clientCore.onLogin((int)EGEC.EGEC_CORE_NET_ERROR, 1, "", -1, null);
                    }
                }
            }
            else
            {
                if (clientCore != null && clientCore.onLogin != null)
                {
                    clientCore.onLogin((int)EGEC.EGEC_CORE_NET_ERROR, 1, "", -1, null);
                }
            }
        }
        void OnMyReceived(Connection connection, byte[] buffer, int offset, int size)
        {
            Message msg = clientCore.Serializer.Deserialize(buffer, offset, size);
            if (msg.Id != (int)EGMI.EGMI_LOGIN_RES)
            {
                LogU.Error("Unhandled message:{0}", msg.Id);
                return;
            }
            if (clientCore == null || clientCore.onLogin == null)
            {
                return;
            }

            LoginRes response = (LoginRes)msg.GetPb(typeof(LoginRes));
            if (response == null)
            {
                clientCore.onLogin((int)EGEC.EGEC_CORE_DESERIALIZE_ERROR, 1, "", -1, null);
            }
            else
            {
                if (response.result == (int)EGEC.EGEC_CORE_SUCCESS)
                {
                    clientCore.Token = guid + response.token;
                    clientCore.AccountId = response.accountId;
                }

                clientCore.onLogin( response.result, response.accountId, response.token, response.lastAreaId, response.areas);
            }
            connection.Disconnect(true);
        }

    }
}