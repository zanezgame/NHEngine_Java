using System;
using System.Collections.Generic;
using nicehu.common;
using nicehu.net;
using nicehu.pb;


namespace nicehu.clientcore
{
	public class Transmitter
	{
		byte[] buffer;
        Connection connection;
        ClientCore clientCore;

        public ClientCore ClientCore
        {
            get { return clientCore; }
            set { clientCore = value; }
        }
		
		public Connection Connection {
			get {return connection;}
			set {connection = value;}
		}

		public Transmitter(ClientCore ClientCore)
		{
            this.ClientCore = ClientCore;
			buffer = new byte[NHNet._MAX_COMPRESS_MESSAGE_SIZE];
		}
		
		public bool SendToGame<T>(T message, int msgId)
		{
			if (connection == null)
			{
				return false;
			}

            int netStatus = connection.getStatus();
            //无论什么原因导致连接断开,只要需要发送数据,并且已经初始化过,那么就尝试重连
            if (netStatus == NHNet.CON_CLOSED)
            {
                if (connection.HasInit())
                {
                    clientCore.reConnect();
                    LogU.Debug("Transmitter: NHNet.CON_CLOSED ,has try reConnect,this msg can not send");
                    return false;
                }
            }
            if (netStatus == NHNet.CON_CONNECTING)
            {
                LogU.Debug("Transmitter:  NHNet.CON_CONNECTING ,can not send data");
                return false;
            }

            int length = 0;
            bool result = ClientCore.Serializer.Serialize<T>(ref buffer, ref length, message, msgId,ClientCore.accountId);
            if (result)
            {
                result = connection.Send(buffer, 0, length);
            }
            else
            {
                LogU.Error("Failed to serialize the message {0}", msgId);
            }

            return result;
		}
		
        public bool SendToAuth<T>(Connection connection, T message, int msgId)
		{
			if (connection == null)
			{
				return false;
			}
			int length = 0;
            bool result = ClientCore.Serializer.Serialize<T>(ref buffer, ref length, message, msgId, ClientCore.accountId);
            if (result)
            {
                result = connection.Send(buffer, 0, length);
            }
            else
            {
                LogU.Error("Failed to serialize the message {0}", msgId);
            }

            return result;
		}
	}
}

