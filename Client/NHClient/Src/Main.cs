using System;
using System.Threading;
using nicehu.net;
using nicehu.common;
using nicehu.clientcore;
using System.IO;
using nicehu.pb;

namespace nicehu.client
{
	class MainClass
	{
		public static void Main(string[] args)
		{
            Client.Init();
            Thread t = new Thread(new ThreadStart(Client.Update));
			t.Start();

			string cmd = string.Empty;
			while (true)
			{
				cmd = Console.ReadLine();
				if (cmd != null)
				{
                    Client.commands = cmd.Split(' ');
				}
                Client.HandleCmd();

            }
		}
	}
}