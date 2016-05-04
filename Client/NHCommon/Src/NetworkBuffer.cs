using System;
using System.Collections.Generic;
using System.Text;
using System.IO;

namespace nicehu.common
{
    public class NetworkBuffer
    {
        MemoryStream ms;
        BinaryReader br;
        BinaryWriter bw;
        
        int writeOffset;
        int readOffset;
        bool isBigEndian;


        public int ReadOffset
        {
            get { return readOffset; }
            set
            {
                if (value > ms.Length || value > writeOffset)
                {
                    throw new IndexOutOfRangeException();                  
                }
                else
                {
                    readOffset = value;
                }
            }
        }
        

        public int WriteOffset
        {
            get { return writeOffset; }
            set
            {
                if (value > ms.Capacity || value < readOffset)
                {
                    throw new IndexOutOfRangeException();                    
                }
                else
                {
                    writeOffset = value;
                }
            }
        }

        public int Capacity
        {
            get { return ms.Capacity; }
        }

        public int ReadableBytes
        {
            get { return writeOffset - readOffset; }
        }

        public bool Readable
        {
            get { return readOffset < writeOffset; }
        }

        public int WritableBytes
        {
            get { return ms.Capacity - writeOffset; }
        }

        public void ResetReadOffset()
        {
            readOffset = 0;
        }

        public void ResetWriteOffset()
        {
            readOffset = 0;
            writeOffset = 0;
        }

        public NetworkBuffer(int capacity, bool isBigEndian)
        {
            ms = new MemoryStream(capacity);
            br = new BinaryReader(ms);
            bw = new BinaryWriter(ms);
            this.isBigEndian = isBigEndian;
        }

        public NetworkBuffer(byte[] buffer, int offset, int count, bool isBigEndian)
        {
            ms = new MemoryStream(buffer, offset, count);
            br = new BinaryReader(ms);
            bw = new BinaryWriter(ms);
            this.isBigEndian = isBigEndian;
        }

        public void DiscardReadBytes()
        {
            int _length = writeOffset - readOffset;
            if (readOffset > 0)
            {
                if (_length > 0)
                {
                    byte[] _buffer = Readbytes(readOffset, _length);
                    ms.Position = 0;
                    bw.Write(_buffer, 0, _length);
                    writeOffset = _length;
                    readOffset = 0;
                }
                else
                {
                    ResetWriteOffset();
                }
            }
        }

        public void SkipBytes(int length)
        {
            if ((readOffset + length) > writeOffset)
            {
                throw new IndexOutOfRangeException();
            }
            else
            {
                readOffset += length;
            }
        }

        public bool EnsureWritableBytes(int writableBytes)
        {
            if (writableBytes > (ms.Capacity - writeOffset))
            {
                return false;
            }
            return true;
        }

        public byte ReadByte(int offset)
        {
            if (offset + 1 > writeOffset)
            {
                throw new IndexOutOfRangeException();
            }
            ms.Position = offset;
            readOffset = offset + 1;
            return br.ReadByte();
        }

        public byte ReadByte()
        {
            return ReadByte(readOffset);
        }

        public Int16 ReadInt16(int offset)
        {
            if (offset + 2 > writeOffset)
            {
                throw new IndexOutOfRangeException();
            }
            ms.Position = offset;
            readOffset = offset + 2;
            if (isBigEndian && BitConverter.IsLittleEndian)
            {
                return BigEndianUtil.ToInt16(br.ReadBytes(2));
            }
            else
            {
                return br.ReadInt16();
            }
        }

        public Int16 ReadInt16()
        {
            return ReadInt16(readOffset);
        }


        public UInt16 ReadUInt16(int offset)
        {
            if (offset + 2 > writeOffset)
            {
                throw new IndexOutOfRangeException();
            }
            ms.Position = offset;
            readOffset = offset + 2;
            if (BitConverter.IsLittleEndian && isBigEndian)
            {
                return BigEndianUtil.ToUInt16(br.ReadBytes(2));
            }
            else
            {
                return br.ReadUInt16();
            }
        }
        public UInt16 ReadUInt16()
        {
            return ReadUInt16(readOffset);
        }

        public Int32 ReadInt32(int offset)
        {
            if (offset + 4 > writeOffset)
            {
                throw new IndexOutOfRangeException();
            }
            ms.Position = offset;
            readOffset = offset + 4;
            if (BitConverter.IsLittleEndian && isBigEndian)
            {
                return BigEndianUtil.ToInt32(br.ReadBytes(4));
            }
            return br.ReadInt32();
        }
        public Int32 ReadInt32()
        {
            return ReadInt32(readOffset);
        }

        public UInt32 ReadUInt32(int offset)
        {
            if (offset + 4 > writeOffset)
            {
                throw new IndexOutOfRangeException();
            }
            ms.Position = offset;
            readOffset = offset + 4;
            if (BitConverter.IsLittleEndian && isBigEndian)
            {
                return BigEndianUtil.ToUInt32(br.ReadBytes(4));
            }

            return br.ReadUInt32();
        }

        public UInt32 ReadUInt32()
        {
            return ReadUInt32(readOffset);
        }

        public byte[] Readbytes(int offset, int count)
        {
            if (offset + count > writeOffset)
            {
                throw new IndexOutOfRangeException();
            }

            ms.Position = offset;
            readOffset = offset + count;
            return br.ReadBytes(count);
        }
        public byte[] Readbytes(int count)
        {
            return Readbytes(readOffset, count);
        }

        public byte GetByte(int offset)
        {
            if (offset + 1 > writeOffset)
            {
                throw new IndexOutOfRangeException();
            }
            ms.Position = offset;
            return br.ReadByte();
        }
        public byte GetByte()
        {
            return GetByte(readOffset);
        }

        public Int16 GetInt16(int offset)
        {
            if (offset + 2 > writeOffset)
            {
                throw new IndexOutOfRangeException();
            }
            ms.Position = offset;
            if (BitConverter.IsLittleEndian && isBigEndian)
            {
                return BigEndianUtil.ToInt16(br.ReadBytes(2));
            }
            return br.ReadInt16();
        }
        public Int16 GetInt16()
        {
            return GetInt16(readOffset);
        }

        public UInt16 GetUInt16(int offset)
        {
            if (offset + 2 > writeOffset)
            {
                throw new IndexOutOfRangeException();
            }
            ms.Position = offset;
            if (BitConverter.IsLittleEndian && isBigEndian)
            {
                return BigEndianUtil.ToUInt16(br.ReadBytes(2));
            }
            return br.ReadUInt16();
        }

        public UInt16 GetUInt16()
        {
            return GetUInt16(readOffset);
        }

        public Int32 GetInt32(int offset)
        {
            if (offset + 4 > writeOffset)
            {
                throw new IndexOutOfRangeException();
            }
            ms.Position = offset;
            if (BitConverter.IsLittleEndian && isBigEndian)
            {
                return BigEndianUtil.ToInt32(br.ReadBytes(4));
            }
            return br.ReadInt32();
        }
        public Int32 GetInt32()
        {
            return GetInt32(readOffset);
        }

        public UInt32 GetUInt32(int offset)
        {
            if (offset + 4 > writeOffset)
            {
                throw new IndexOutOfRangeException();
            }
            ms.Position = offset;
            if (BitConverter.IsLittleEndian && isBigEndian)
            {
                return BigEndianUtil.ToUInt32(br.ReadBytes(4));
            }
            return br.ReadUInt32();
        }
        public UInt32 GetUInt32()
        {
            return GetUInt32(readOffset);
        }

        public void Write(int offset, byte value)
        {
            if (offset + 1 > ms.Capacity)
            {
                throw new IndexOutOfRangeException();
            }
            ms.Position = offset;
            writeOffset = offset + 1;
            bw.Write(value);
        }
        public void Write(byte value)
        {
            Write(writeOffset, value);
        }

        public void Write(int offset, Int16 value)
        {
            if (offset + 2 > ms.Capacity)
            {
                throw new IndexOutOfRangeException();
            }
            ms.Position = offset;
            writeOffset = offset + 2;
            if (BitConverter.IsLittleEndian && isBigEndian)
            {
                bw.Write(BigEndianUtil.ToBytes(value));
            }
            else
            {
                bw.Write(value);
            }
        }
        public void Write(Int16 value)
        {
            Write(writeOffset, value);
        }

        public void Write(int offset, UInt16 value)
        {
            if (offset + 2 > ms.Capacity)
            {
                throw new IndexOutOfRangeException();
            }
            ms.Position = offset;
            writeOffset = offset + 2;
            if (BitConverter.IsLittleEndian && isBigEndian)
            {
                bw.Write(BigEndianUtil.ToBytes(value));
            }
            else
            {
                bw.Write(value);
            }
        }
        public void Write(UInt16 value)
        {
            Write(writeOffset, value);
        }

        public void Write(int offset, Int32 value)
        {
            if (offset + 4 > ms.Capacity)
            {
                throw new IndexOutOfRangeException();
            }
            ms.Position = offset;
            writeOffset = offset + 4;
            if (BitConverter.IsLittleEndian && isBigEndian)
            {
                bw.Write(BigEndianUtil.ToBytes(value));
            }
            else
            {
                bw.Write(value);
            }
        }
        public void Write(Int32 value)
        {
            Write(writeOffset, value);
        }

        public void Write(int offset, UInt32 value)
        {
            if (offset + 4 > ms.Capacity)
            {
                throw new IndexOutOfRangeException();
            }
            ms.Position = offset;
            writeOffset = offset + 4;
            if (BitConverter.IsLittleEndian && isBigEndian)
            {
                bw.Write(BigEndianUtil.ToBytes(value));
            }
            else
            {
                bw.Write(value);
            }
        }
        public void Write(UInt32 value)
        {
            Write(writeOffset, value);
        }

        public void Write(int offset, byte[] value, int index, int count)
        {
            if (offset + count > ms.Capacity)
            {
                throw new IndexOutOfRangeException();
            }
            ms.Position = offset;
            writeOffset = offset + count;
            bw.Write(value, index, count);
        }
        public void Write(byte[] value, int index, int count)
        {
            Write(writeOffset, value, index, count);
        }

        public byte[] ToArray()
        {
            return ms.ToArray();
        }
    }
}
