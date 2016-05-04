using System;
using System.Collections.Generic;
using System.Text;

namespace nicehu.net
{
    public class CorruptedFrameException : Exception
    {
        public CorruptedFrameException(string msg):base(msg)
        {
        }
    }
}
