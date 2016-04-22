package nicehu.nhsdk.candy.data;

import java.util.Map;

import org.slf4j.Logger;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import nicehu.nhsdk.candy.collect.CollectionU;
import nicehu.nhsdk.candy.http.HttpUE;
import nicehu.nhsdk.candy.log.LogU;
import nicehu.nhsdk.candy.object.Empty;

public class MessageU
{
	private static final Logger logger = LogU.getLogger(MessageU.class);



	public static Message genHttpMessage(ChannelHandlerContext ctx,Object me)
	{
		Message msg = new Message();

		String jsonStr = "";
		try
		{
			JSONObject jsonObj = null;
			HttpRequest request = (HttpRequest)me;
			String paramStr = HttpUE.getParamStr(request);
			if (Empty.is(paramStr))
			{
				logger.error("Wrong param Format!!!!!! : " + paramStr);
				return null;
			}

			if (request.getMethod().equals(HttpMethod.GET))
			{
				jsonObj = JSONObject.fromObject(paramStr);
			}
			else
			{
				Map<String, String> paramsMap = CollectionU.splitToMap(paramStr, "&", "=");
				jsonStr = paramsMap.get("jsonStr");
				if (Empty.is(jsonStr))
				{
					return null;
				}
				jsonObj = JSONObject.fromObject(jsonStr);
			}
			msg.setId(jsonObj.getInt("pid"));
			msg.setPlayerId(jsonObj.optInt("uid"));
			msg.setJsonObj(jsonObj);
		}
		catch (JSONException e)
		{
			logger.error("Wrong Json Format!!!!!! : " + jsonStr);
			return null;

		}
		catch (Exception e)
		{
			LogU.error(e);
			return null;
		}

		return msg;
	}
}
