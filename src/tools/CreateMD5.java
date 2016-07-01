package tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CreateMD5 {
	/*静态方法，便于作为工具类*/
	
	/* md5加密
	 * @param plainText 字符串
	 * @param length    生成length长度的随机字符串
	 * @return randomChars length长度的随机字符串
	 */
	public static String getMd5(String plainText,int length) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			if (length == 32){
				//32位加密
				return buf.toString();
			}else if(length == 16){
				// 16位的加密
				return buf.toString().substring(8, 24);
			}
			//默认返回32位的加密
			return buf.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}

	}
}
