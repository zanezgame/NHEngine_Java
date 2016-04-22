package nicehu.nhsdk.core.type;

public class ProtoType
{
	// Protocol Type 0xxxxx0000
	public static final int MANAGE = 0x00010000;
	public static final int AUTH = 0x00020000;
	public static final int GATE = 0x00030000;
	public static final int DATA = 0x00040000;
	public static final int GAME = 0x00050000;
	public static final int CENTER = 0x00060000;
	
	public static final int SERVER_MANAGE = 0x10010000;
	public static final int SERVER_AUTH = 0x10020000;
	public static final int SERVER_GATE = 0x10030000;
	public static final int SERVER_DATA = 0x10040000;
	public static final int SERVER_GAME = 0x10050000;
	public static final int SERVER_CENTER = 0x10060000;

	public static int getHttpType(Integer id)
	{
		int value = Integer.parseInt(""+id, 16);
		return 0xffff0000 & value;
	}

	public static int getType(Integer id)
	{
		return 0xffff0000 & id;
	}

	public static int getId(Integer id)
	{
		return 0x0000ffff & id;
	}
}
