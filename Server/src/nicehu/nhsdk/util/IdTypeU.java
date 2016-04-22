package nicehu.nhsdk.util;

import nicehu.pb.NHDefine;

public class IdTypeU
{
	public static NHDefine.EIdType getIdType(int id)
	{
		if (id < 1000)
		{
			return NHDefine.EIdType.EIT_Special;
		}
		else if (id > 1000)
		{
			return NHDefine.EIdType.EIT_Item;
		}
		else
		{
			return NHDefine.EIdType.EIT_Unknow;
		}
	}

}
