// Bank.java

/*
 Creates a bunch of accounts and uses threads
 to post transactions to the accounts concurrently.
*/

import java.io.*;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

public class Bank {
	public static final int ACCOUNTS = 20;	 // number of accounts
	public static ArrayList<Account> accs;
	public static ArrayBlockingQueue<Transaction> trans;
	public static CountDownLatch latch; 
	
	/*
	 Reads transaction data (from/to/amt) from a file for processing.
	 (provided code)
	 */  
	public void readFile(String file) { 
			try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			// Use stream tokenizer to get successive words from file
			StreamTokenizer tokenizer = new StreamTokenizer(reader);
			
			while (true) {
				int read = tokenizer.nextToken();
				if (read == StreamTokenizer.TT_EOF) break;  // detect EOF
				int from = (int)tokenizer.nval;
				
				tokenizer.nextToken();
				int to = (int)tokenizer.nval;
				 
				tokenizer.nextToken();
				int amount = (int)tokenizer.nval;
				
				// Use the from/to/amount
				// YOUR CODE HERE
				Transaction t = new Transaction(from, to, amount);
				trans.put(t);
				
			}
		}
		catch (Exception e) { System.exit(1);}
	}

	/*
	 Processes one file of transaction data
	 -fork off workers
	 -read file into the buffer
	 -wait for the workers to finish
	*/
	public static void processFile(String file, int numWorkers) throws InterruptedException {
		Bank b = new Bank();
		latch = new CountDownLatch(numWorkers);
		for(int i=0; i<numWorkers; i++) { 
			BankThread t = new BankThread();
			t.start();
		}
		b.readFile(file);
		for(int i=0; i<numWorkers; i++) {
			trans.put(new Transaction(-1, 0, 0));
		}
		latch.await();
	}  

	
	
	/*
	 Looks at commandline args and calls Bank processing.
	*/
	public static void main(String[] args) throws InterruptedException {
		// deal with command-lines args
		
		if (args.length == 0) {
			System.exit(1);
		}
		
		String file = args[0];
		
		int numWorkers = 1;
		if (args.length >= 2) {
			numWorkers = Integer.parseInt(args[1]);
		}
		// YOUR CODE HERE  
		accs = new ArrayList<Account>(ACCOUNTS);
		trans = new ArrayBlockingQueue<Transaction>(100);
		Bank b = new Bank();
		
		for(int i =0; i<ACCOUNTS; i++) {
			accs.add(new Account(b, i, 1000));
		}
	
		processFile(file,numWorkers);
	
		for(int i=0; i<ACCOUNTS; i++) {
			System.out.println(accs.get(i).toString());
		}
	}
}
 


class BankThread extends Thread{
	public void run (){ 
		while(true) {
			try {
				Transaction t = Bank.trans.take();
				if(t.from == -1) break;
				Account from = Bank.accs.get(t.from);
				from.fromTrans(t.amount); 
				Account to = Bank.accs.get(t.to);
				to.toTrans(t.amount);  
			} catch (InterruptedException e) {}
		}
		Bank.latch.countDown();
   }		
}
	

