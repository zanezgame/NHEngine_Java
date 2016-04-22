package nicehu.nhsdk.util;

import nicehu.server.manageserver.config.text.TextConfigMgr;

public class TextConfigU
{

	public static String get(String name)
	{
		return TextConfigMgr.instance.cfg.getText(name);
	}
}
