using System;
using System.Collections.Generic;
using System.Text;
using System.IO;

namespace nicehu.common
{
	public class LogU
	{
		public static bool enable = true;

        public static void Debug(object msg)
		{
			if (enable) ConsoleU.Debug(msg);
		}
		public static void Debug(string format, params Object[] args)
		{
			if (enable) ConsoleU.Debug(format, args);
		}
		public static void Info(object msg)
		{
			if (enable) ConsoleU.Info(msg);
		}
		public static void Info(string format, params Object[] args)
		{
			if (enable) ConsoleU.Info(format, args);
		}
		public static void Warn(object msg)
		{
			if (enable) ConsoleU.Warn(msg);
		}
		public static void Warn(string format, params Object[] args)
		{
			if (enable) ConsoleU.Warn(format, args);
		}
		public static void Error(object msg)
		{
			if (enable) ConsoleU.Error(msg);
		}
		public static void Error(string format, params Object[] args)
		{
			if (enable) ConsoleU.Error(format, args);
		}

	}
}
