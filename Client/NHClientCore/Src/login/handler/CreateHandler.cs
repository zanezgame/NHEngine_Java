using System;
using System.Net;
using System.Net.Sockets;
using nicehu.net;
using nicehu.common;
using System.Collections.Generic;
using nicehu.pb;


namespace nicehu.clientcore
{
    public class CreateHandler
    {
        private ClientCore clientCore;

        private string account;
        private string pass;
        private nicehu.pb.DeviceInfo deviceInfo = new nicehu.pb.DeviceInfo();

        public CreateHandler(ClientCore ClientCore)
        {
            this.clientCore = ClientCore;
        }

        public void Create(IPEndPoint ipPort, string account, string pass, nicehu.pb.DeviceInfo deviceInfo)
        {
            if (clientCore.Connection != null && clientCore.Connection.socket.Connected)
            {
                clientCore.Connection.Disconnect(true);
            }
            this.account = account;
            this.pass = pass;
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


        void OnMyConnected(Connection connection, SocketError result)
        {
            if (result == SocketError.Success)
            {
                CreateAccountReq request = new CreateAccountReq();
                request.account = account;
                request.pass = pass;
                request.deviceInfo = deviceInfo;
                

                if (clientCore.Transmitter.SendToAuth(connection, request, (int)EGMI.EGMI_CREATE_ACCOUNT_REQ) == false)
                {
                    if (clientCore.onCreate != null)
                    {
                        clientCore.onCreate((int)EGEC.EGEC_CORE_NET_ERROR);
                    }
                }
            }
            else
            {
                if ( clientCore.onCreate != null)
                {
                    clientCore.onCreate((int)EGEC.EGEC_CORE_NET_ERROR);
                }
            }
        }
        void OnMyReceived(Connection connection, byte[] buffer, int offset, int size)
        {
            Message msg = clientCore.Serializer.Deserialize(buffer, offset, size);
            if (msg.Id != (int)EGMI.EGMI_CREATE_ACCOUNT_RES)
            {
                LogU.Error("Unhandled message:{0}", msg.Id);
                return;
            }
            if (clientCore == null || clientCore.onCreate == null)
            {
                return;
            }

            CreateAccountRes response = (CreateAccountRes)msg.GetPb(typeof(CreateAccountRes));
            if (response == null)
            {
                clientCore.onCreate((int)EGEC.EGEC_CORE_DESERIALIZE_ERROR);
            }
            else
            {
                clientCore.onCreate(response.result);
            }
            connection.Disconnect(true);
        }

    }
}
