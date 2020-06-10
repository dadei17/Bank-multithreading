// Cracker.java
/*
 Generates SHA hashes of short strings in parallel.
*/

import java.security.*;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class Cracker {
	// Array of chars used to produce strings
	public static final char[] CHARS = "abcdefghijklmnopqrstuvwxyz0123456789.,-!".toCharArray();
	public static ArrayList<String> arr; 
	private static String pass;
	private static String hashed;
	public static CountDownLatch latch; 
	/*
	 Given a byte[] array, produces a hex String,
	 such as "234a6f". with 2 chars for each byte in the array. 
	 (provided code)
	*/
	public static String hexToString(byte[] bytes) {
		StringBuffer buff = new StringBuffer();
		for (int i=0; i<bytes.length; i++) {
			int val = bytes[i];
			val = val & 0xff;  // remove higher bits, sign
			if (val<16) buff.append('0'); // leading 0
			buff.append(Integer.toString(val, 16));  
		}
		return buff.toString();
	} 
	
	/*
	 Given a string of hex byte values such as "24a26f", creates
	 a byte[] array of those values, one byte value -128..127
	 for each 2 chars.
	 (provided code)
	*/
	public static byte[] hexToArray(String hex) {
		byte[] result = new byte[hex.length()/2];
		for (int i=0; i<hex.length(); i+=2) {
			result[i/2] = (byte) Integer.parseInt(hex.substring(i, i+2), 16);
		}
		return result;
	}
	
	public static String hashing(String st) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA"); 
		md.update(st.getBytes());
		String res = hexToString(md.digest());
		return res;
	}
	
	static int resultIsFound = 0;
	
	public static void allConcat(int len, String hashedStr, String res) throws NoSuchAlgorithmException {
		if(resultIsFound == 1) return;	
		if(res.length() > len) return;
		if(res.length() == len) {
			if(hashedStr.equals(hashing(res))) { 
				System.out.println(res);
				pass = res;
				resultIsFound = 1;
			} 
		} 
		for(int i=0; i<arr.size(); i++) {
			String st = arr.get(i);
			allConcat(len, hashedStr, res + st); 
		}
	}
	
	public static void findPassword(String targ, int len, int num) throws NoSuchAlgorithmException, InterruptedException {
		latch = new CountDownLatch(num); 
		for(int i=1; i<=num; i++) {
			String st = "";
			int startIndx = (i-1) * CHARS.length / num;
			int endIndx = i * CHARS.length / num;
			for(int j=startIndx; j<endIndx; j++) {
				st += CHARS[j];
			}
			CrackerThread t = new CrackerThread(st, len);
			t.start();
		}
		latch.await();
		allConcat(len,targ,""); 
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException, InterruptedException{ 
		if(args.length == 0) {
			System.out.println("Args: target length [workers]");
			System.exit(1); 
		}
		if (args.length < 2) {
			hashed = hashing(args[0]); 
			System.out.println(hashed);
			return;
		}
		
		// args: targ len [num]
		String targ = args[0]; 
		int len = Integer.parseInt(args[1]);
		int num = 1;
		if (args.length>2) {
			num = Integer.parseInt(args[2]);
		}
		// a! 34800e15707fae815d7c90d49de44aca97e2d759
		// xyz 66b27417d37e024c46526c2f6d358a754fc552f3
		
		// YOUR CODE HERE
		arr = new ArrayList<String>();
		findPassword(targ,len,num);
		resultIsFound = 0;
	}
	
	public String getPassword() {
		return pass;
	}
	
	public String getHashed() {
		return hashed;
	}
} 

class CrackerThread extends Thread{
	public int limitedLength;
	public String str; 
	public CrackerThread(String a, int b) {
		limitedLength = b;
		str = a;
	}
	
	public void run() {
		for(int i =1; i<=Math.min(str.length(), limitedLength); i++) {
			permutations(str, "", i);
		}
		Cracker.latch.countDown();
	}
	
	public void permutations(String s, String res, int len) {
		if(res.length() == len) {
			Cracker.arr.add(res); 
			return;
		}
		for(int i =0; i<s.length(); i++) {
			char ch = s.charAt(i);
			permutations(s, res + ch, len);
		}
	}
}
