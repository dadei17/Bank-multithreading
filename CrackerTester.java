import java.security.NoSuchAlgorithmException;

import junit.framework.TestCase;

public class CrackerTester extends TestCase{
	
	public void test1() throws NoSuchAlgorithmException, InterruptedException{
		String password = "code"; 
		Cracker c = new Cracker();
		String hashCode = "e6fb06210fafc02fd7479ddbed2d042cc3a5155e";
		String args[] = {hashCode,Integer.toString(password.length()),"20"};
		Cracker.main(args);
		assertEquals(c.getPassword(), password);
		Cracker.hexToArray("ab");
	}
	
	public void test2() throws NoSuchAlgorithmException, InterruptedException {
		String args[] = {"sun"};
		Cracker c = new Cracker();
		String hashCode = "22fa6121da96f43a106e413e65d4f9089c53824c";
		Cracker.main(args);
		assertEquals(c.getHashed(),hashCode); 
	}
	
	public void test3() throws NoSuchAlgorithmException, InterruptedException{
		String password = "moon";
		Cracker c = new Cracker();  
		String hashCode = "46dcd4dd65b63d106b8cfb4aad906b23716cc613";
		String args1[] = {hashCode,Integer.toString(password.length()),"40"};
		Cracker.main(args1);
		assertEquals(c.getPassword(), password);
		String args2[] = {password};
		Cracker.main(args2);
		assertEquals(c.getHashed(),hashCode);
	}
	
	public void test4() throws NoSuchAlgorithmException, InterruptedException{
		String password = "cake";
		Cracker c = new Cracker();  
		String hashCode = "5ddc2ae17d5b13b4e5b3215177685116565f5058";
		String args1[] = {hashCode,Integer.toString(password.length()),"20"};
		Cracker.main(args1);
		assertEquals(c.getPassword(), password);
	}
	
	public void test4moreThreads() throws NoSuchAlgorithmException, InterruptedException {
		String password = "cake"; 
		Cracker c = new Cracker();
		String hashCode = "5ddc2ae17d5b13b4e5b3215177685116565f5058";
		String args[] = {hashCode,Integer.toString(password.length()),"40"};
		Cracker.main(args);
		assertEquals(c.getPassword(), password);
	}
	
	public void test4lessThreads() throws NoSuchAlgorithmException, InterruptedException {
		String password = "cake"; 
		Cracker c = new Cracker();
		String hashCode = "5ddc2ae17d5b13b4e5b3215177685116565f5058";
		String args[] = {hashCode,Integer.toString(password.length()),"10"};
		Cracker.main(args);
		assertEquals(c.getPassword(), password);
	}
	
}
