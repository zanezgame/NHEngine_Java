using System;
using System.Collections.Generic;

using System.Text;
using nicehu.common;
using nicehu.pb;


namespace nicehu.clientcore
{

    public delegate void OnMessageReceived(Message msg);
    public class HandlerMgr
    {
        Dictionary<int, OnMessageReceived> handleFunctions = new Dictionary<int, OnMessageReceived>();


        public bool handle(Message msg)
        {
            OnMessageReceived handleFunction;
            if (handleFunctions.TryGetValue(msg.Id, out handleFunction))
            {
                handleFunction(msg);
                return true;
            }
            else
            {
                LogU.Error("don't support message id {0}", msg.Id);
                return false;
            }
        }

        public void addHandler(int msgId, OnMessageReceived handleFunction)
        {
            handleFunctions.Add(msgId, handleFunction);
        }


    }
}
