package nicehu.nhsdk.candy.xml;

import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlU
{
	private static final Logger logger = LoggerFactory.getLogger(XmlU.class);

	public static Element getXmlRootFromFilePath(String filepath)
	{
		SAXReader reader = new SAXReader();
		Document document = null;

		try
		{
			document = reader.read(filepath);
		}
		catch (DocumentException e)
		{
			logger.error("{}", ExceptionUtils.getStackTrace(e));
		}
		if (document == null)
		{
			return null;
		}

		Element root = document.getRootElement();
		return root;
	}

	public static Element getXmlRootFromContent(String content)
	{
		Document document = null;
		try
		{
			document = DocumentHelper.parseText(content);
		}
		catch (DocumentException e)
		{
			logger.error("{}", ExceptionUtils.getStackTrace(e));
		}
		if (document == null)
		{
			return null;
		}

		Element root = document.getRootElement();
		return root;
	}

	public static HashMap<String, String> getAttribute(Element element)
	{
		HashMap<String, String> attributes = new HashMap<String, String>();
		Iterator<Attribute> it = element.attributeIterator();
		while (it.hasNext())
		{
			Attribute attribute = it.next();
			attributes.put(attribute.getName(), attribute.getValue());
		}
		return attributes;
	}

}
