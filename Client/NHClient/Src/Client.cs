using System;
using System.Threading;
using nicehu.clientcore;
using nicehu.common;
using System.Collections.Generic;
using nicehu.pb;


namespace nicehu.client
{
    public class Client
    {
        //public static string ip = "127.0.0.1";
        public static string ip = "45.78.61.104";
        public static int port = 2000;
        public static string proxyIp;
        public static int proxyPort;
        public static string accountName;
        public static string lastCmd = "";
        public static string[] commands;
        public static ClientCore clientCore = new ClientCore();

        public static nicehu.clientcore.Player playerLocal;

        public static void Init()
        {
            LoginModule.init(clientCore);
        }

        public static void Handle()
        {
            LoginModule.Handle( commands);

        }

        public static void Update()
        {
            while (true)
            {
                clientCore.Update();
                Thread.Sleep(10);
            }
        }

        public static void HandleCmd()
        {
            if (commands != null && commands[0].Equals("reset"))
            {
                lastCmd = "";
                LogU.Debug(" Reset SUCCESS");
                return;
            }
            if (commands != null && commands[0].Equals("nh"))
            {
                string easyCmd = GenEasyCmd();
                commands = easyCmd.Split(' ');
            }
            if (commands == null || commands.Length == 0)
            {
                return;
            }
            lastCmd = commands[0];
            Handle();
            commands = null;

        }


        public static string GenEasyCmd()
        {
            switch(lastCmd)
            {
                case null:
                case "":
                case "query":
                    {
                        Random myRand = new Random();
                        int x = myRand.Next();
                        accountName = "nh" + x.ToString("x");
                        return "create " + accountName + " a";
                    }
                case "create":
                    {
                        return "login " + accountName + " a";
                    }
                case "login":
                    {
                        return "connect";
                    }
                case "connect":
                    {
                        return "auth";
                    }
                case "auth":
                    {
                        return "query";
                    }
                default:
                    {
                        return "";
                    }
            }
        }

    }
}

