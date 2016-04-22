package nicehu.nhsdk.candy.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;




import nicehu.nhsdk.candy.json.JsonU;
import nicehu.nhsdk.candy.log.LogU;
import nicehu.nhsdk.candy.object.Empty;
import nicehu.nhsdk.candy.util.CloseU;

public class HttpU
{
	private static Logger logger = LogU.getLogger(HttpU.class);

	public static String method = "GET";

	public static JSONObject send(String url, Map<String, String> params)
	{
		String resultStr = null;
		if (HttpU.method.equals("GET"))
		{
			resultStr = HttpU.sendGet(url + "/" + HttpU.genParams(params));
		}
		else
		{
			resultStr = HttpU.sendPost(url, params);
		}
		if (Empty.is(resultStr))
		{
			JSONObject result = new JSONObject();
			JsonU.updateErr(result, "send  Http   Faild!!!");
			return result;
		}
		return JSONObject.fromObject(resultStr);
	}

	public static String sendGet(String url)
	{
		return HttpU.sendGet(url, false);
	}

	public static String sendGet(String url, boolean openLog)
	{
		CloseableHttpClient httpclient = HttpClients.custom().build();
		CloseableHttpResponse response = null;
		String resultStr = null;
		try
		{
			HttpGet httpget = new HttpGet(url);
			httpget.setConfig(RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build());

			response = httpclient.execute(httpget);
			resultStr = EntityUtils.toString(response.getEntity(), "UTF-8");

			logger.info("send    Request:  " + url);
			logger.info(response.getStatusLine().toString());
			logger.info("receive Response: " + resultStr);
		}
		catch (Exception e)
		{
			if (openLog)
			{
				logger.error(ExceptionUtils.getStackTrace(e));
			}
			else
			{
				logger.error("send  Http  Get  Faild!!!!!!!!!!!  url" + url);
			}
		}
		finally
		{
			CloseU.close(response);
			CloseU.close(httpclient);
		}
		return resultStr;
	}

	public static String sendPost(String uri, Map<String, String> params)
	{
		return HttpU.sendPost(uri, params, false);
	}

	public static String sendPost(String uri, Map<String, String> params, boolean openLog)
	{
		List<NameValuePair> paramsPair = HttpU.getParamsPair(params);

		CloseableHttpClient httpclient = HttpClients.custom().build();
		CloseableHttpResponse response = null;
		String resultStr = null;
		try
		{
			HttpPost httpPost = new HttpPost(uri);
			httpPost.setConfig(RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build());
			httpPost.setEntity(new UrlEncodedFormEntity(paramsPair));

			response = httpclient.execute(httpPost);
			resultStr = EntityUtils.toString(response.getEntity(), "UTF-8");

			logger.info(response.getStatusLine().toString());
			logger.info(resultStr);
		}
		catch (Exception e)
		{
			if (openLog)
			{
				logger.error(ExceptionUtils.getStackTrace(e));
			}
			else
			{
				logger.error("send  Http  Get  Faild!!!!!!!!!!!!!!!  uri" + uri);
			}
		}
		finally
		{
			CloseU.close(response);
			CloseU.close(httpclient);
		}
		return resultStr;
	}

	private static List<NameValuePair> getParamsPair(Map<String, String> map)
	{
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> entry : map.entrySet())
		{
			params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		return params;
	}

	public static String genParams(Map<String, String> map)
	{
		StringBuilder params = new StringBuilder();

		for (String name : map.keySet())
		{
			try
			{
				if (params.length() != 0)
				{
					params.append(",");
				}

				params.append(name).append("=").append(URLEncoder.encode(map.get(name).toString(), "utf-8"));
			}
			catch (UnsupportedEncodingException e)
			{
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return params.toString();
	}
}
