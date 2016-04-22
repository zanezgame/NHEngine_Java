package nicehu.nhsdk.candy.test.json;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Test
{
	private static final Logger logger = LoggerFactory.getLogger(Test.class);

	public static void main(String[] args)
	{
//		try
//		{
//			User user = new User();
//			user.setName("小民");
//			user.setEmail("xiaomin@sina.com");
//			user.setAge1(20);
//
//			/**
//			 * ObjectMapper是JSON操作的核心，Jackson的所有JSON操作都是在ObjectMapper中实现。
//			 * ObjectMapper有多个JSON序列化的方法，可以把JSON字符串保存File、OutputStream等不同的介质中。 writeValue(File arg0, Object
//			 * arg1)把arg1转成json序列，并保存到arg0文件中。 writeValue(OutputStream arg0, Object arg1)把arg1转成json序列，并保存到arg0输出流中。
//			 * writeValueAsBytes(Object arg0)把arg0转成json序列，并把结果输出成字节数组。 writeValueAsString(Object
//			 * arg0)把arg0转成json序列，并把结果输出成字符串。
//			 */
//			ObjectMapper mapper = new ObjectMapper();
//
//			// User类转JSON
//			// 输出结果：{"name":"小民","age":20,"birthday":844099200000,"email":"xiaomin@sina.com"}
//			String json = mapper.writeValueAsString(user);
//			System.out.println(json);
//
//			User back = mapper.readValue(json, User.class);
//			System.out.println(back.toString());
//		}
//		catch (Exception e)
//		{
//			logger.error(ExceptionUtils.getStackTrace(e));
//		}

	}

}
