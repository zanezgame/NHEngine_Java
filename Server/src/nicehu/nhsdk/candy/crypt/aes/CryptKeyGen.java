package nicehu.nhsdk.candy.crypt.aes;

import java.util.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import nicehu.nhsdk.candy.log.LogU;

public class CryptKeyGen
{
	public static String genkey()
	{
		try
		{
			KeyGenerator kg = KeyGenerator.getInstance(CryptU.KEY_ALGORITHM);
			kg.init(128); // 初始化密钥生成器:AES要求密钥长度为128,192,256位
			SecretKey secretKey = kg.generateKey();
			System.out.println(new String(secretKey.getEncoded()));
			String key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
			System.out.println("gen key:" + key);
			return key;
		}
		catch (Exception e)
		{
			LogU.error(e);
			return "";
		}
	}

	public static void main(String[] args)
	{
		genkey();
	}
}
