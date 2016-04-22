package nicehu.nhsdk.candy.cmd;

import java.util.HashMap;

import org.slf4j.Logger;

import nicehu.nhsdk.candy.cmd.core.Cmd;
import nicehu.nhsdk.candy.cmd.core.CmdProc;
import nicehu.nhsdk.candy.log.LogU;

public abstract class CmdProcMgr
{
	private static final Logger logger = LogU.getLogger(CmdProcMgr.class);

	private HashMap<String, CmdProc> cmds = new HashMap<String, CmdProc>();

	public abstract void init();

	// public String Proc(Cmd cmd)
	// {
	// CmdProc cmdProc = this.getProc(cmd);
	// if (cmdProc != null)
	// {
	// try
	// {
	// String result = cmdProc.proc(cmd);
	// logger.info("return Cmd Result: " + result.toString());
	// return result;
	// }
	// catch (Exception e)
	// {
	// LogU.error(e);
	// return "Cmd Proc Failed!";
	// }
	// }
	// else
	// {
	// logger.error(" Cmd Not Find! Cmd: " + cmd.getName());
	// return "Cmd Not Find!";
	// }
	// }

	public void addProc(Object name, CmdProc cmdProc)
	{
		this.cmds.put(name.toString(), cmdProc);
	}

	public CmdProc getProc(String name)
	{
		return this.cmds.get(name);
	}

	public CmdProc getProc(Cmd cmd)
	{
		return this.cmds.get(cmd.getName());
	}
}
