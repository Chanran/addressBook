package tools;

public class CreateRandom {
	/*产生随机字符串(英文字母,数字)
	 * @author blue
	 * @param length 要产生随机字符串的长度,默认为32位
	 * @return randomChars length长度的随机字符串
	 */
	public static String getRandomChars(){
		int length = 32;
		char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
		String randomChars = "";
		for (int i = 0; i < length; i++){
			randomChars += chars[(int)(Math.random()*(chars.length-1))];
		}
		return new String(randomChars);
		
	}
	public static String getRandomChars(int length){
		char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
		String randomChars = "";
		for (int i = 0; i < length; i++){
			randomChars += chars[(int)(Math.random()*(chars.length-1))];
		}
		return new String(randomChars);
	}
}