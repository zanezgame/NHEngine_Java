using System;
using System.Collections.Generic;
using System.Threading;
using nicehu.clientcore;
using nicehu.common;
using nicehu.pb;


namespace nicehu.client
{
    class Demo_ServerDisconnect
    {
        public static void OnReceive(String result, int playerId)
        {
            LogU.Debug("ServerDisconnect :{0}", result);
            LogU.Debug("PlayerId = {0}", playerId);
        }
    }
}
