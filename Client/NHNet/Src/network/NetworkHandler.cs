using System;
using System.Collections.Generic;
using System.Text;
using System.IO;
using nicehu.common;

namespace nicehu.net
{
    public class NetworkHandler
    {

        public void OnReceived(Connection connection, byte[] buffer, int offset, int count)
        {
            UInt16 MSG_TYPE = BigEndianUtil.ToUInt16(buffer, offset);
            Int32 msgLength = BigEndianUtil.ToInt32(buffer, offset + 2);
           if (MSG_TYPE == NHNet.MSG_TYPE_DATA)
            {
                connection.OnReceived(connection, buffer, offset + 6, count - 6);
            }
            else if (MSG_TYPE == NHNet.MSG_TYPE_DATA_COPMRESS)
            {
                //never use
                byte[] uncompressData = new byte[NHNet._MAX_UNCOMPRESS_MESSAGE_SIZE];
                int uncompressDataCount = -1;
                try
                {
                    //uncompressDataCount = null;//TODO uncompress
                }
                catch (OverflowException ex)
                {
                    LogU.Debug("uncompress exception {0}",ex.Message);
                }
                connection.OnReceived(connection, uncompressData, 0, uncompressDataCount);
            }
        }
    }
}
