using System;
using System.Collections.Generic;
using System.Threading;
using nicehu.clientcore;
using nicehu.common;
using nicehu.pb;


namespace nicehu.client
{
    class Demo_QueryPlayer
    {

        public static void Handle( string[] commands)
        {
            Client.clientCore.queryPlayer();

            LogU.Debug("queryPlayer  ");
        }
       
        public static void OnReceive(int result, nicehu.clientcore.Player player)
        {
            if (result == (int)EGEC.EGEC_CORE_SUCCESS)
            {
               Client.playerLocal = player;
                LogU.Debug("QueryPlayer SUCCESS ");
                LogU.Debug(" player.PlayerId = " + player.PlayerId);
                LogU.Debug(" player.name = " + player.Name);
                LogU.Debug(" player.level = " + player.LevelAttrib.Level);
                LogU.Debug(" player.exp = " + player.LevelAttrib.Exp);
                LogU.Debug(" player.Money = " + player.Money);
                LogU.Debug(" player.Diamond = " + player.Diamond);

                foreach (nicehu.clientcore.Item item in player.Items)
                {
                    LogU.Debug(" Item: id: {0}, count: {1} ", item.Id, item.Count);
                }
                LogU.Debug("playerId :{0}", player.PlayerId);
                LogU.Debug("player.loginTime :{0}", player.LoginTime);
            }
            else
            {
                LogU.Debug(" result = " + result );
            }
        }
    }
}
