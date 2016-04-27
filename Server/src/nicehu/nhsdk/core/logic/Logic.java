package nicehu.nhsdk.core.logic;

import nicehu.nhsdk.core.handler.HandlerMgr;

public interface Logic
{
	void addProto(HandlerMgr controller);

	void addHandler(HandlerMgr controller);
}
