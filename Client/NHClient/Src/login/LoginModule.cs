using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace nicehu.client
{
    public class LoginModule
    {
        public static void init(nicehu.clientcore.ClientCore ClientCore)
        {
            ClientCore.onCreate = Demo_Create.OnReceive;
            ClientCore.onLogin = Demo_Login.OnReceive;
            ClientCore.onAuthToken = Demo_AuthToken.OnReceive;
            ClientCore.onQueryPlayer = Demo_QueryPlayer.OnReceive;
            ClientCore.onServerDisconnect = Demo_ServerDisconnect.OnReceive;

        }

        public static void Handle(string[] commands)
        {
            if ("create" == commands[0])
            {
                Demo_Create.Handle( commands);
            }
            else if ("login" == commands[0])
            {
                Demo_Login.Handle(commands);
            }
            else if("connect" == commands[0])
            {
                Demo_ConnectProxy.Handle(commands);
            }
            else if ("auth" == commands[0])
            {
                Demo_AuthToken.Handle( commands);
            }else if ("query" == commands[0])
            {
                Demo_QueryPlayer.Handle(commands);
            }



               
        }
    }
}
