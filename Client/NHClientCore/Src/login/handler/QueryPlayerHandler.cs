using System;
using System.Net;
using System.Net.Sockets;
using System.Collections.Generic;
using nicehu.net;
using nicehu.common;
using nicehu.pb;
using System.Threading;


namespace nicehu.clientcore
{
    class QueryPlayerHandler
    {
        private ClientCore clientCore;

        public QueryPlayerHandler(ClientCore ClientCore)
        {
            this.clientCore = ClientCore;
        }

        public bool QueryPlayerReq()
        {

            QueryPlayerReq request = new QueryPlayerReq();
            return clientCore.Transmitter.SendToGame(request, (int)EGMI.EGMI_QUERY_PLAYER_REQ);
        }

        public void OnQueryPlayerRes(Message message)
        {
			QueryPlayerRes response = (QueryPlayerRes)message.GetPb(typeof(QueryPlayerRes)); 

			nicehu.clientcore.Player player = new nicehu.clientcore.Player();
			if (response.player != null)
			{
				player.FromProto(response.player);
                LogU.Debug("receive QueryPlaeyr Success");
			}

            clientCore.onQueryPlayer(
                response.result, 
                player
                );
        }
    }
}
