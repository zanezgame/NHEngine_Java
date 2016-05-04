using System;
using System.Collections.Generic;
using System.Text;

namespace nicehu.common
{
    public class BigEndianUtil
    {
        private static byte[] ToFixedLength(byte[] a, int length)
        {
            if (a.Length > length)
                return null;

            if (a.Length == length)
                return a;

            var z = new byte[length];
            a.CopyTo(z, length - a.Length);
            return z;
        }

        public static UInt64 ToUInt64(byte[] data)
        {
            byte[] rawBytes = ToFixedLength(data, 8);
            if (rawBytes == null)
                return 0;

            if (BitConverter.IsLittleEndian)
                Array.Reverse(rawBytes);

            return BitConverter.ToUInt64(rawBytes, 0);
        }

        public static byte[] ToBytes(UInt64 num)
        {
            byte[] rawBytes = BitConverter.GetBytes(num);

            if (BitConverter.IsLittleEndian)
                Array.Reverse(rawBytes);

            return rawBytes;
        }

        public static Int64 ToInt64(byte[] data)
        {
            byte[] rawBytes = ToFixedLength(data, 8);
            if (rawBytes == null)
                return 0;

            if (BitConverter.IsLittleEndian)
                Array.Reverse(rawBytes);

            return BitConverter.ToInt64(rawBytes, 0);
        }

        public static byte[] ToBytes(Int64 num)
        {
            byte[] rawBytes = BitConverter.GetBytes(num);

            if (BitConverter.IsLittleEndian)
                Array.Reverse(rawBytes);

            return rawBytes;
        }

        public static uint ToUInt32(byte[] data)
        {
            byte[] rawBytes = ToFixedLength(data, 4);
            if (rawBytes == null)
                return 0;

            if (BitConverter.IsLittleEndian)
                Array.Reverse(rawBytes);

            return BitConverter.ToUInt32(rawBytes, 0);
        }

        public static byte[] ToBytes(uint num)
        {
            byte[] rawBytes = BitConverter.GetBytes(num);

            if (BitConverter.IsLittleEndian)
                Array.Reverse(rawBytes);

            return rawBytes;
        }

        public static int ToInt32(byte[] data)
        {
            byte[] rawBytes = ToFixedLength(data, 4);
            if (rawBytes == null)
                return 0;

            if (BitConverter.IsLittleEndian)
                Array.Reverse(rawBytes);

            return BitConverter.ToInt32(rawBytes, 0);
        }

        public static int ToInt32(byte[] data, int offset)
        {
            if (data.Length <= offset)
            {
                return 0;
            }

            if (BitConverter.IsLittleEndian)
            {
                byte[] rawBytes = new byte[4];
                int length = data.Length - offset > 4 ? 4 : data.Length - offset;
                Buffer.BlockCopy(data, offset, rawBytes, 0, length);
                Array.Reverse(rawBytes);
                return BitConverter.ToInt32(rawBytes, 0);
            }
            else
            {
                return BitConverter.ToInt32(data, offset);
            }
        }

        public static byte[] ToBytes(int num)
        {
            byte[] rawBytes = BitConverter.GetBytes(num);

            if (BitConverter.IsLittleEndian)
                Array.Reverse(rawBytes);

            return rawBytes;
        }

        public static UInt16 ToUInt16(byte[] data)
        {
            byte[] rawBytes = ToFixedLength(data, 2);
            if (rawBytes == null)
                return 0;

            if (BitConverter.IsLittleEndian)
                Array.Reverse(rawBytes);

            return BitConverter.ToUInt16(rawBytes, 0);
        }

        public static UInt16 ToUInt16(byte[] data, int offset)
        {
            if (data.Length <= offset)
            {
                return 0;
            }

            if (BitConverter.IsLittleEndian)
            {
                byte[] rawBytes = new byte[2];
                rawBytes[1] = data[offset];
                if (data.Length - offset > 1)
                {
                    rawBytes[0] = data[offset + 1];
                }
                return BitConverter.ToUInt16(rawBytes, 0);
            }
            else
            {
                return BitConverter.ToUInt16(data, offset);
            }
        }

        public static byte[] ToBytes(UInt16 num)
        {
            byte[] rawBytes = BitConverter.GetBytes(num);

            if (BitConverter.IsLittleEndian)
                Array.Reverse(rawBytes);

            return rawBytes;
        }

        public static Int16 ToInt16(byte[] data)
        {
            byte[] rawBytes = ToFixedLength(data, 2);
            if (rawBytes == null)
                return 0;

            if (BitConverter.IsLittleEndian)
                Array.Reverse(rawBytes);

            return BitConverter.ToInt16(rawBytes, 0);
        }

        public static byte[] ToBytes(Int16 num)
        {
            byte[] rawBytes = BitConverter.GetBytes(num);

            if (BitConverter.IsLittleEndian)
                Array.Reverse(rawBytes);

            return rawBytes;
        }

    }
}
