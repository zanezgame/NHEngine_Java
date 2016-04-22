package nicehu.nhsdk.candy.test.json;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import nicehu.nhsdk.candy.json.JsonU;
import nicehu.nhsdk.candy.util.CandyU;

public class Test1
{
	private static final Logger logger = LoggerFactory.getLogger(Test1.class);

	public static void main(String[] args)
	{
		User user = new User();
		user.age = 8999;
		user.a = 888;
		String str = JsonU.getJsonStr(user);
		User rest = JsonU.getJavaObj(User.class, str);
		CandyU.printNow();

	}

}
