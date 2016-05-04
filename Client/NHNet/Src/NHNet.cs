using System;
using System.Collections.Generic;
using System.Text;

namespace nicehu.net
{
    public class NHNet
    {
        public static int CON_CLOSED = 0;
        public static int CON_CONNECTING = 1;
        public static int CON_CONNECTED = 2;

        public static int EVENTARGS_BUFFER_SIZE = 1024;
        public static int EVENTARGS_MAX_COUNT = 256;

        public static int _MAX_COMPRESS_MESSAGE_SIZE = 1048576; //1024*1024
        public static int _MAX_UNCOMPRESS_MESSAGE_SIZE = 3145728;// 1024*1024*3 = 3MB
        
        public static UInt16 MSG_TYPE_DATA = 1;
        public static UInt16 MSG_TYPE_DATA_COPMRESS = 2;


        
    }

}