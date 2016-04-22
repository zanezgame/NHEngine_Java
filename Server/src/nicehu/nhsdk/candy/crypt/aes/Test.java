package nicehu.nhsdk.candy.crypt.aes;


public class Test
{
	public static void main(String[] args)
		throws Exception
	{
		String str = "www.gowhere.so啊啊啊";
		System.out.println("原    文：" + str);

		String encryptData =CryptU.encrypt(str);
		System.out.println("加密后：" + encryptData);

		String decryptData = CryptU.decrypt(encryptData);
		System.out.println("解密后：" + decryptData);
	}
}
