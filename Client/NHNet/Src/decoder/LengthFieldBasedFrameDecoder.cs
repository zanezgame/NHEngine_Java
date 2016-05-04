using System;
using System.Collections.Generic;
using System.Text;
using System.IO;
using nicehu.common;

namespace nicehu.net
{
    public class LengthFieldBasedFrameDecoder : FrameDecoder
    {
        private int maxFrameLength;//支持的最大数据长度
        private int lengthField_Offset;//数据起始位置偏移(offset前面的数据不参与解码)  type
        private int lengthField_Length;//存储数据长度的的字段大小  lenth
        private int lengthField_HeadSize;// totalhead
        private int lengthAdjustment;//解码时额外的校正适配长度(一般为0,即不需要校正适配)
        private int initialBytesToStrip;//数据部分需要跳过的长度(一般为0,即不跳过任何数据部分内容)

        //lengthFieldLength must be 1/2/4
        //maxFrameLength  must > lengthFieldOffset + lengthFieldLength..or  there is no space for data
        public LengthFieldBasedFrameDecoder(int maxFrameLength, int lengthField_Offset, int lengthField_Length,
            int lengthAdjustment, int initialBytesToStrip)
        {
            this.maxFrameLength = maxFrameLength;
            this.lengthField_Offset = lengthField_Offset;
            this.lengthField_Length = lengthField_Length;
            this.lengthField_HeadSize = lengthField_Offset + lengthField_Length;
            this.lengthAdjustment = lengthAdjustment;
            this.initialBytesToStrip = initialBytesToStrip;
        }

        public override bool Decode(Connection connection)
        {
            //size< headsize
            if (networkBuffer.ReadableBytes < lengthField_HeadSize)
            {
                return false;
            }
            //skip type
            int actualLengthFieldOffset = networkBuffer.ReadOffset + lengthField_Offset;
            //data size
            int frameLength;
            switch (lengthField_Length)
            {
                case 1:
                    frameLength = networkBuffer.GetByte(actualLengthFieldOffset);
                    break;
                case 2:
                    frameLength = networkBuffer.GetUInt16(actualLengthFieldOffset);
                    break;
                case 4:
                    frameLength = networkBuffer.GetInt32(actualLengthFieldOffset);
                    break;
                default:
                    throw new CorruptedFrameException("should not reach here");
            }

            if (frameLength < 0)
            {
                networkBuffer.SkipBytes(lengthField_HeadSize);
                throw new CorruptedFrameException(
                        "negative pre-adjustment length field: " + frameLength);
            }
            //totalsize= headsize+bodysize
            frameLength += lengthAdjustment + lengthField_HeadSize;
            if (frameLength < lengthField_HeadSize)
            {
                networkBuffer.SkipBytes(lengthField_HeadSize);
                throw new CorruptedFrameException(
                        "Adjusted frame length (" + frameLength + ") is less " +
                        "than lengthFieldEndOffset: " + lengthField_HeadSize);
            }
            //数据超长
            if (frameLength > maxFrameLength)
            {
                networkBuffer.SkipBytes(networkBuffer.ReadableBytes);
                throw new CorruptedFrameException(String.Format("too long frame, frame length:{0}", frameLength));
            }
            
            //need receive more data
            if (frameLength > networkBuffer.ReadableBytes)
            {
                return false;
            }
            if (frameLength < initialBytesToStrip)
            {
                networkBuffer.SkipBytes(frameLength);
                throw new CorruptedFrameException(
                        "Adjusted frame length (" + frameLength + ") is less " +
                        "than initialBytesToStrip: " + initialBytesToStrip);
            }
            networkBuffer.SkipBytes(initialBytesToStrip);

            //msg_offset
            msgInitOffset = networkBuffer.ReadOffset;
            //msg_size
            msgCount = frameLength - initialBytesToStrip;

            //can decode
            return true;
        }

    }
}
