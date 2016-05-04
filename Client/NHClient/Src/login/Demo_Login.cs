using System;
using System.Collections.Generic;
using System.Threading;
using nicehu.clientcore;
using nicehu.common;
using nicehu.pb;


namespace nicehu.client
{
    class Demo_Login
    {

        public static void Handle(string[] commands)
        {
            nicehu.pb.DeviceInfo deviceInfo = new nicehu.pb.DeviceInfo();
            deviceInfo.oSType = "IOS";
            deviceInfo.oSVersion = "9.0";
            deviceInfo.udid = "default";
            deviceInfo.deviceName = "iphone6s";

            Client.clientCore.login(Client.ip, Client.port, commands[1], commands[2],  deviceInfo);

            LogU.Debug("login  " + commands[1] + "  " + commands[2]);

        }
        public static void OnReceive(int result, int accountId, String token, int lastAreaId, List<nicehu.pb.Area> areas )
        {

            if (result == (int)EGEC.EGEC_CORE_SUCCESS)
            {
                Client.proxyIp = areas[0].ip;
                Client.proxyPort = areas[0].port;
                LogU.Debug("Login Success ! ProxyServer address: {0}:{1}", Client.proxyIp, Client.proxyPort);
            }
            else
            {
                LogU.Debug("Loign error:" + result.ToString("X"));
            }
        }
    }
}
