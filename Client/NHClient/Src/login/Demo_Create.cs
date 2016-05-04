using System;
using System.Collections.Generic;
using System.Threading;
using nicehu.clientcore;
using nicehu.common;
using nicehu.pb;


namespace nicehu.client
{
    class Demo_Create
    {

        public static void Handle(string[] commands)
        {
            nicehu.pb.DeviceInfo deviceInfo = new nicehu.pb.DeviceInfo();
            deviceInfo.oSType = "IOS";
            deviceInfo.oSType = "9.3.0";
            deviceInfo.udid = "default";
            deviceInfo.deviceName = "iphone7";
            Client.clientCore.create(Client.ip, Client.port, commands[1], commands[2],  deviceInfo);
            LogU.Debug("create  "+commands[1] +"  " + commands[2]);
        }
        public static void OnReceive(int result)
        {
            if (result == (int)EGEC.EGEC_CORE_SUCCESS)
            {
                LogU.Debug("CreateAccount SUCCESS !");
            }
            else
            {
                LogU.Debug("CreateAccount error:" + result.ToString("X"));
            }
        }
    }
}
