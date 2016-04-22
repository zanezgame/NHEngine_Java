package nicehu.server.manageserver.config.text;

import java.util.HashMap;
import java.util.List;

import org.dom4j.Element;

import nicehu.nhsdk.candy.struct.Pair;
import nicehu.nhsdk.candy.xml.XmlU;
import nicehu.server.manageserver.config.core.ConfigPath;

public class TextConfigMgr
{
	public static TextConfigMgr instance = new TextConfigMgr();
	public TextConfig cfg = new TextConfig();

	public void reload()
	{
		Element root = XmlU.getXmlRootFromFilePath(ConfigPath.file_TextConfig);
		TextConfig cfg = new TextConfig();
		List<Element> serverTexts = root.element("ServerText").elements("String");
		for (Element tempE : serverTexts)
		{
			Pair<String, String> serverTextTemp = loadServerTextFromXml(tempE);
			if (serverTextTemp != null)
			{
				cfg.getTexts().put(serverTextTemp.getFirst(), serverTextTemp.getSecond());
			}
		}
		this.cfg = cfg;
	}

	private static Pair<String, String> loadServerTextFromXml(Element element)
	{
		HashMap<String, String> kv = XmlU.getAttribute(element);
		Pair<String, String> serverText = new Pair<String, String>(kv.get("name"), element.getText());
		return serverText;
	}

}
