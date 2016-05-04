using nicehu.common;
using nicehu.pb;
using System;
using System.Collections.Generic;
using System.IO;
using System.Text;

namespace nicehu.clientcore
{
    public class Message
    {
        private int id;
        private BaseMsg baseMsg;

        public Object GetPb(Type messageLite)
        {
            MemoryStream stream = new MemoryStream(baseMsg.msgData, 0, baseMsg.msgData.Length);
            Object subPb = null;
            try
            {
                subPb = ProtoBuf.Serializer.NonGeneric.Deserialize(messageLite, stream);
            }
            catch (Exception ex)
            {
                LogU.Error(ex.StackTrace);
            }
            return subPb;
        }

        public int Id
        {
            get { return id; }
            set { id = value; }
        }


        public BaseMsg BaseMsg
        {
            get { return baseMsg; }
            set { baseMsg = value; }
        }

    }
}
