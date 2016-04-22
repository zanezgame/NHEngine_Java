package nicehu.nhsdk.candy.crypt.aes;

import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import nicehu.nhsdk.candy.log.LogU;

public class CryptU
{

	// 密钥算法
	public static final String KEY_ALGORITHM = "AES";
	public static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
	private static final String keyStr = "1234567890123456";
	private static Key key = null;
	private static Cipher cipher = null;
	static
	{
		try
		{
			key = new SecretKeySpec(keyStr.getBytes("utf-8"), KEY_ALGORITHM);
			cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		}
		catch (Exception e)
		{
			LogU.error(e);
		}
	}

	public static String encrypt(String data)
	{
		try
		{
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes("utf-8")));
		}
		catch (Exception e)
		{
			LogU.error(e);
			return "";
		}
	}

	public static String decrypt(String data)
	{
		try
		{
			cipher.init(Cipher.DECRYPT_MODE, key);
			return new String(cipher.doFinal(Base64.getDecoder().decode(data)), "utf-8");
		}
		catch (Exception e)
		{
			LogU.error(e);
			return "";
		}
	}
}
