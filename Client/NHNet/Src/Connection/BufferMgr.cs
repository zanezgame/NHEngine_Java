using System;
using System.Collections.Generic;
using System.Text;
using System.Net.Sockets;

namespace nicehu.net
{
    public class BufferMgr
    {
        int totalBytesSize;//字节数组最大空间
        int oneBufferSize;//一个Event需要分配的空间

        byte[] bigBufferData;//为N个Event提供空间的大字节数组
        int currentIndex;//当前字节数组已使用到的最大位置
        Stack<int> m_freeIndexPool;//被回收的空间的位置(可重复利用)
        
       

        public BufferMgr()
        {
            totalBytesSize = NHNet.EVENTARGS_MAX_COUNT * NHNet.EVENTARGS_BUFFER_SIZE;
            oneBufferSize = NHNet.EVENTARGS_BUFFER_SIZE;

            bigBufferData = new byte[totalBytesSize];
            currentIndex = 0;
            m_freeIndexPool = new Stack<int>();
        }

        // allocate buffer space
        public bool SetBuffer(SocketAsyncEventArgs args)
        {
            if (m_freeIndexPool.Count > 0)
            {
                args.SetBuffer(bigBufferData, m_freeIndexPool.Pop(), oneBufferSize);
            }
            else
            {
                if (currentIndex+ oneBufferSize> totalBytesSize)
                {
                    return false;
                }
                args.SetBuffer(bigBufferData, currentIndex, oneBufferSize);
                currentIndex += oneBufferSize;
            }
            return true;
        }

        //allocate buffer space and set data
        public bool SetBuffer(SocketAsyncEventArgs args, byte[] buffer, int offset, int count)
        {
            if (m_freeIndexPool.Count > 0)
            {
                int _offset = m_freeIndexPool.Pop();
                Buffer.BlockCopy(buffer, offset, bigBufferData, _offset, count);
                args.SetBuffer(bigBufferData, _offset, count);
            }
            else
            {
                if (currentIndex + oneBufferSize > totalBytesSize)
                {
                    return false;
                }
                Buffer.BlockCopy(buffer, offset, bigBufferData, currentIndex, count);
                args.SetBuffer(bigBufferData, currentIndex, count);
                currentIndex += oneBufferSize;
            }
            return true;
        }

        public void FreeBuffer(SocketAsyncEventArgs args)
        {
            m_freeIndexPool.Push(args.Offset);
            args.SetBuffer(null, 0, 0);
        }


    }
}
