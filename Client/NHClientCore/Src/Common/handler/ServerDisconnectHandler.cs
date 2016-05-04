using System;
using System.Collections.Generic;
using nicehu.pb;
using nicehu.common;
using System.Text;

namespace nicehu.clientcore
{
	public class ServerDisconnectHandler
	{
		private ClientCore clientCore;

		public ServerDisconnectHandler(ClientCore clientCore)
		{
			this.clientCore = clientCore;
		}

		public void OnServerDisconnect(Message message)
		{
            ServerDisconnect response = (ServerDisconnect)message.GetPb(typeof(ServerDisconnect));

            clientCore.onServerDisconnect(response.result, response.playerId);
		}
	}
}
