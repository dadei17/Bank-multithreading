import junit.framework.TestCase;

public class BankTester  extends TestCase{
	
	public void test1() throws Exception { 
		Bank.main(new String[] {"small.txt"});
		for(int i = 0; i < Bank.ACCOUNTS; i++) {
			if(i%2 == 0) {
				assertEquals(Bank.accs.get(i).getBalance(), 1001);
			}else {
				assertEquals(Bank.accs.get(i).getBalance(), 999);
			}
			
		}
		Bank.main(new String[] {"small.txt", "10"});
		for(int i = 0; i < Bank.ACCOUNTS; i++) {
			if(i%2 == 0) {
				assertEquals(Bank.accs.get(i).getBalance(), 1001);
			}else {
				assertEquals(Bank.accs.get(i).getBalance(), 999);
			}
		}
		Transaction t = new Transaction(1, 2, 100);
		System.out.println(t.toString());
	}
	
	public void test2() throws Exception { 
		Bank.main(new String[] {"5k.txt", "1"});
		for(int i = 0; i < Bank.ACCOUNTS; i++) {
			assertEquals(Bank.accs.get(i).getBalance(), 1000);
		}
		Bank.main(new String[] {"5k.txt", "2"});
		for(int i = 0; i < Bank.ACCOUNTS; i++) {
			assertEquals(Bank.accs.get(i).getBalance(), 1000);
		}
		Bank.main(new String[] {"5k.txt", "5"});
		for(int i = 0; i < Bank.ACCOUNTS; i++) {
			assertEquals(Bank.accs.get(i).getBalance(), 1000);
		}
	}
	
	public void test3() throws Exception { 
		Bank.main(new String[] {"100k.txt", "5"});
		for(int i = 0; i < Bank.ACCOUNTS; i++) {
			assertEquals(Bank.accs.get(i).getBalance(), 1000);
		}
		Bank.main(new String[] {"100k.txt", "15"});
		for(int i = 0; i < Bank.ACCOUNTS; i++) {
			assertEquals(Bank.accs.get(i).getBalance(), 1000);
		}
		Bank.main(new String[] {"100k.txt", "20"});
		for(int i = 0; i < Bank.ACCOUNTS; i++) {
			assertEquals(Bank.accs.get(i).getBalance(), 1000);
		}
	}
}
