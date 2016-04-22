package nicehu.server.manageserver.config.text;

import java.util.concurrent.ConcurrentHashMap;

public class TextConfig
{
	private ConcurrentHashMap<String, String> texts = new ConcurrentHashMap<>();
	
	public String getText(String key)
	{
		return texts.get(key);
	}

	public ConcurrentHashMap<String, String> getTexts()
	{
		return texts;
	}

	public void setTexts(ConcurrentHashMap<String, String> texts)
	{
		this.texts = texts;
	}

}
