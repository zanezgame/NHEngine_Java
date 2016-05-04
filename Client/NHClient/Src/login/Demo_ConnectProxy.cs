using System;
using System.Collections.Generic;
using System.Threading;
using nicehu.clientcore;
using nicehu.common;
using nicehu.pb;


namespace nicehu.client
{
    class Demo_ConnectProxy
    {
        public static void Handle(string[] commands)
        {
            Client.clientCore.ConnectProxy(Client.proxyIp, Client.proxyPort);

            LogU.Debug("connect ProxyServer  " + Client.proxyIp + "  " + Client.proxyPort);
        }
    }
}
