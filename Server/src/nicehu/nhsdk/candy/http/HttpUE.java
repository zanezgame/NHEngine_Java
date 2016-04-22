package nicehu.nhsdk.candy.http;

import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUE
{
	private static Logger logger = LoggerFactory.getLogger(HttpUE.class);

	public static String getParamStr(HttpRequest request)
	{
		String result = "";
		try
		{
			if (request.getMethod().equals(HttpMethod.GET))
			{

				String uri = request.getUri();
				uri = java.net.URLDecoder.decode(uri, "UTF-8");
				logger.info("Receive Get URI Decoder: {}", uri);

				result = uri.substring(1);
				if (result == null || result.equals("") || result.equals("favicon.ico"))
				{
					return null;
				}
			}
			else
			{
				String dataListStr = null;
				List<InterfaceHttpData> dataList = null;
				HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), request, CharsetUtil.UTF_8);
				dataList = decoder.getBodyHttpDatas();
				// [jsonStr={"token":"","uid":"","pid":"20001","data":{"socialType":"1","socialId":"sR735c82e9","deviceId":"dR735c82e9","socialNickName":"珍惜","osType":"ios","osVersion":"7.0.2","deviceName":"iphone 6"}}]
				if (dataList == null || dataList.size() == 0)
				{
					return null;
				}
				dataListStr = dataList.toString();
				logger.info("Receive Post Decoder: {}", dataListStr);
				result = dataListStr.substring(1, dataListStr.length() - 1);
			}
		}
		catch (Exception e)
		{
			logger.error(ExceptionUtils.getStackTrace(e));
			return null;
		}
		return result;
	}
}
