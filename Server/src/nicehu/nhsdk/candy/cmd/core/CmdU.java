package nicehu.nhsdk.candy.cmd.core;

import java.util.Map;

import net.sf.json.JSONObject;

import nicehu.nhsdk.candy.data.Message;
import nicehu.nhsdk.candy.str.ParseU;

public class CmdU
{

	public static Cmd genCmd(Map<String, String> paramsMap)
	{
		Cmd cmd = new Cmd();
		cmd.attrs = paramsMap;
		cmd.name = paramsMap.get("cmdName");
		return cmd;
	}

	public static Cmd genCmd(Message message)
	{
		Cmd cmd = new Cmd();
		//cmd.attrs = message.attrs;
		cmd.name = cmd.attrs.get("cmdName");
		return cmd;
	}

	@SuppressWarnings("unchecked")
	public static Cmd genCmd(JSONObject json)
	{
		Cmd cmd = new Cmd();
		json.keySet().stream().forEach(x -> {
			cmd.attrs.put(ParseU.pStr(x), ParseU.pStr(json.get(x)));
		});
		cmd.name = cmd.attrs.get("cmdName");
		return cmd;
	}
}
