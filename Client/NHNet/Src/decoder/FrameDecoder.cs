using System;
using System.Collections.Generic;
using System.Text;
using nicehu.common;

namespace nicehu.net
{
    public abstract class  FrameDecoder
    {
        protected NetworkBuffer networkBuffer;
        protected int msgInitOffset;//解码数据部分实际起始偏移(已越过header部分)
        protected int msgCount;//数据部分实际长度
        public void OnReceived(Connection connection, byte[] buffer, int offset, int count)
        {
            if (networkBuffer == null)
            {
                networkBuffer = new NetworkBuffer(NHNet._MAX_COMPRESS_MESSAGE_SIZE, true);
            }

            networkBuffer.Write(buffer, offset, count);

            try
            {
                CallDecode(connection);
            }
            catch (Exception e)
            {
                LogU.Debug(e.Message);
                LogU.Debug(e.StackTrace);

                //通知connection，连接异常
                if (null != connection.OnReceived)
                {
                    connection.OnReceived(connection, null, 0, -1);
                }
            }
        }

        public abstract bool Decode(Connection connection);

        public void CallDecode(Connection connection)
        {
            while (networkBuffer.Readable)
            {
                int oldReaderOffset = networkBuffer.ReadOffset;
                //try to decode
                bool result = Decode(connection);
                if (result == false)
                {
                    if (oldReaderOffset == networkBuffer.ReadOffset)
                    {
                        // wait to receive more data
                        break;
                    }
                    else
                    {
                        // Previous data has been discarded.
                        // Probably it is reading on.
                        //never be here -- wind
                        continue;
                    }
                }
                else if (msgCount == 0)
                {
                    throw new InvalidOperationException(
                            "decode() method must read at least one byte " +
                            "if it returned a frame ");
                }
                try
                {
                    if (connection.netHandler!= null)
                    {
                        connection.netHandler.OnReceived(connection, networkBuffer.ToArray(), msgInitOffset, msgCount);
                    }
                    else
                    {
                        connection.OnReceived(connection, networkBuffer.ToArray(), msgInitOffset, msgCount);
                    }
                }
                finally
                {
                    networkBuffer.ReadOffset = msgInitOffset + msgCount;
                    networkBuffer.DiscardReadBytes();
                }
            }
        }
    }
}
