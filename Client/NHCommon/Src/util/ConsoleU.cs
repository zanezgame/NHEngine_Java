using System;
using System.Collections.Generic;
using System.Text;


namespace nicehu.common
{
	public class ConsoleU
	{
		public static void Debug(object msg)
		{
			Console.Out.WriteLine(DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss.fff") +"  DEBUG:"+ msg);
		}

		public static void Debug(string format, params Object[] args)
		{
			Console.Out.WriteLine(DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss.fff") + "  DEBUG:" + String.Format(format, args));
		}

		public static void Info(object msg)
		{
			Console.Out.WriteLine(DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss.fff") + "  INFO:" + msg);
		}

		public static void Info(string format, params Object[] args)
		{
			Console.Out.WriteLine(DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss.fff") + "  INFO:" + String.Format(format, args));

		}
		public static void Warn(object msg)
		{
			Console.Out.WriteLine(DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss.fff") + "  WARN:" + msg);
		}

		public static void Warn(string format, params Object[] args)
		{
			Console.Out.WriteLine(DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss.fff") + "  WARN:" + String.Format(format, args));
		}

		public static void Error(object msg)
		{
			Console.Out.WriteLine(DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss.fff") + " ERROR:" + msg);
		}

		public static void Error(string format, params Object[] args)
		{
			Console.Out.WriteLine(DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss.fff") + " ERROR:" + String.Format(format, args));
		}
	}
}
