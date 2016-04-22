package nicehu.nhsdk.candy.json;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import nicehu.nhsdk.candy.object.Empty;
import nicehu.nhsdk.util.TextConfigU;

public class JsonU
{
	private static final Logger logger = LoggerFactory.getLogger(JsonU.class);
	public static ObjectMapper mapper = new ObjectMapper();
	static
	{
		mapper.setSerializationInclusion(Include.NON_NULL);
	}

	// @JsonIgnore

	public static String getJsonStr(Object obj)
	{

		if (Empty.is(obj))
		{
			return null;
		}
		String result = null;
		try
		{
			result = mapper.writeValueAsString(obj);
			// logger.debug("____________use Java Obj gen Json Str, Json Str: " + result);
		}
		catch (Exception e)
		{
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return result;
	}

	public static <T> T getJavaObj(Class<T> calss, String jsonStr)
	{
		if (Empty.is(jsonStr))
		{
			return null;
		}
		T result = null;
		try
		{
			long now = System.currentTimeMillis();
			result = (T)mapper.readValue(jsonStr, calss);
			// logger.debug("CostTime: " + (System.currentTimeMillis() - now) +
			// "____________use Json Str gen Java Obj, Json Str: " + jsonStr);
		}
		catch (Exception e)
		{
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return result;
	}

	public static JSONObject gen()
	{
		JSONObject obj = new JSONObject();
		obj.put("code", "0");
		obj.put("errMsg", "");
		obj.put("data", new JSONArray());
		return obj;
	}

	public static JSONObject gen(int pid)
	{
		JSONObject obj = new JSONObject();
		obj.put("pid", pid);
		obj.put("code", "0");
		obj.put("errMsg", "");
		obj.put("data", new JSONArray());
		return obj;
	}

	public static JSONObject gen(int pid, int code, String errMsg)
	{
		JSONObject obj = new JSONObject();
		obj.put("pid", pid);
		obj.put("code", code);
		obj.put("errMsg", errMsg);
		obj.put("data", new JSONArray());
		return obj;
	}

	public static JSONObject updateErr(JSONObject result, String errMsg)
	{
		result.put("code", "1");
		result.put("errMsg", errMsg);
		return result;
	}

	public static JSONObject updateErr(JSONObject result, int code, String errMsg)
	{
		result.put("code", code);
		result.put("errMsg", errMsg);
		return result;
	}

	public static boolean isErr(JSONObject result)
	{
		return !"0".equals(result.getString("code"));
	}

	public static boolean check(JSONObject obj, String... args)
	{
		for (String arg : args)
		{
			if (!obj.containsKey(arg))
			{
				return false;
			}
		}
		return true;
	}
}
