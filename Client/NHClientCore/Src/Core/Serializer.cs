using System;
using System.Collections.Generic;
using System.Text;
using System.IO;
using nicehu.common;
using nicehu.net;
using ProtoBuf;
using nicehu.pb;


namespace nicehu.clientcore
{
    public class Serializer
    {
        private ClientCore clientCore;

        public Serializer(ClientCore clientCore)
        {
            this.clientCore = clientCore;
        }

        public bool Serialize<T>(ref byte[] buffer, ref int allLength, T instance, int msgId, int playerId)
        {
            MemoryStream stream = new MemoryStream();
            ProtoBuf.Serializer.Serialize<T>(stream, instance);

            BaseMsg baseMsg = new BaseMsg();
            baseMsg.id = msgId;
            baseMsg.playerId = playerId;
            baseMsg.msgData = stream.ToArray();


            MemoryStream baseStream = new MemoryStream(buffer);
            baseStream.Position = 6;//head
            ProtoBuf.Serializer.Serialize(baseStream, baseMsg);
            allLength = (int)baseStream.Position;
            int bodyLength = allLength - 6;
            if (bodyLength <= 0)
            {
                return false;
            }

            byte[] header = new byte[6];
            byte[] tmp = BigEndianUtil.ToBytes(NHNet.MSG_TYPE_DATA);
            Array.Copy(tmp, 0, header, 0, 2);
            byte[] tmp1 = BigEndianUtil.ToBytes(bodyLength);
            Array.Copy(tmp1, 0, header, 2,4);


            baseStream.Position = 0;
            BinaryWriter writer = new BinaryWriter(baseStream);
            writer.Write(header, 0, header.Length);

            return true;
        }

        public Message Deserialize(byte[] buffer, int offset, int size)
        {
            if (size <= 4)
            {
                return null;
            }
            //java  use big endian
            int curOffset = offset;
            int restSize = size;

            MemoryStream baseStream = new MemoryStream(buffer, offset, size);
            BaseMsg baseMsg = null;
            try
            {
                baseMsg = (BaseMsg)ProtoBuf.Serializer.NonGeneric.Deserialize(typeof(BaseMsg), baseStream);
            }
            catch (Exception ex)
            {
                LogU.Error(ex.StackTrace);
            }

            Message msg = new Message();
            msg.Id = baseMsg.id;
            msg.BaseMsg = baseMsg;

            return msg;
        }
        
    }
}
