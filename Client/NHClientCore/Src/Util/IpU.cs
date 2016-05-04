using System;
using System.Collections.Generic;
using System.Text;
using System.Threading;
using System.Net;
using System.Net.Sockets;
using nicehu.net;
using nicehu.common;
using nicehu.pb;


namespace nicehu.clientcore
{
    public partial class IpU
    {
       
        public static IPAddress GetIPV4Address(string hostname)
        {
            IPAddress[] hosts = null;
            try
            {
                hosts = Dns.GetHostAddresses(hostname);
            }
            catch (Exception ex)
            {
                LogU.Error("GetHostAddresses " + ex.Message);
                return null;
            }
            if (hosts == null || hosts.Length == 0)
            {
                return null;
            }

            IPAddress host = null;
            foreach (IPAddress address in hosts)
            {
                if (address.AddressFamily == AddressFamily.InterNetwork)
                {
                    host = address;
                    break;
                }
            }

            return host;
        }
        
    }
}

