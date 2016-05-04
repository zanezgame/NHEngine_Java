using System;
using System.Collections.Generic;
using System.Text;


namespace nicehu.common
{
    public class MD5Util
    {
        public static string GetMD5String(string strPwd)
        {
            System.Security.Cryptography.MD5 md5 = new System.Security.Cryptography.MD5CryptoServiceProvider();
            byte[] data = System.Text.Encoding.Default.GetBytes(strPwd);
            byte[] md5data = md5.ComputeHash(data);
            md5.Clear();
            string str = "";
            for (int i = 0; i < md5data.Length; i++)
            {
                str += md5data[i].ToString("x").PadLeft(2, '0');
            }
            return str;
        }
    }
}
