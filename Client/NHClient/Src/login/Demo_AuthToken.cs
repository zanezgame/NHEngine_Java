using System;
using System.Collections.Generic;
using System.Threading;
using nicehu.clientcore;
using nicehu.common;
using nicehu.pb;


namespace nicehu.client
{
    class Demo_AuthToken
    {
        public static void Handle(string[] commands)
        {
            Client.clientCore.authToken();

            LogU.Debug("authToken  ");
        }
        public static void OnReceive(int result)
        {
            if (result == (int)EGEC.EGEC_CORE_SUCCESS)
            {
                LogU.Debug("AuthToken SUCCESS!!! Keep Connection!");
            }
            else
            {
                LogU.Debug("Auth token failed !!! :{0}", result.ToString("X"));
            }
        }
    }
}
