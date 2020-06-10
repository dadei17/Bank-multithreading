import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Semaphore;

import javax.swing.*;

public class WebWorker extends Thread {
	private String urlString;
	private String status;
	private int threadLine;
	private JWebFrame frame;
	private long time;
	private static Semaphore s = new Semaphore(1);
	
	public WebWorker(String url, int line, JWebFrame webFrame) {
		urlString = url;
		threadLine = line;
		frame = webFrame;
	}
	
	public void run() {
		try {
			s.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		frame.setLabels();
		s.release();  
		download();
		frame.sem.release();
		try {
			s.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		frame.runningThreads--;
		frame.table.setValueAt(status, threadLine, 1);
		frame.completedThreads++;
		frame.timeElapsed += time;
		 
		frame.setLabels();
		s.release();
	}

// This is the core web/download i/o code...  
 	private void download() {
		InputStream input = null;
		StringBuilder contents = null;
		try {
			URL url = new URL(urlString);
			URLConnection connection = url.openConnection();
		
			// Set connect() to throw an IOException
			// if connection does not succeed in this many msecs.
			connection.setConnectTimeout(5000);
			
			connection.connect();
			input = connection.getInputStream();

			BufferedReader reader  = new BufferedReader(new InputStreamReader(input));
		
			char[] array = new char[1000];
			int len; 
			int bytes = 0;
			contents = new StringBuilder(1000);
			long start = System.currentTimeMillis();
			while ((len = reader.read(array, 0, array.length)) > 0) {
				contents.append(array, 0, len);
				Thread.sleep(100);
				bytes += len; 
			}
			// Successful download if we get here
			long end = System.currentTimeMillis();  
			SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");  
		    Date date = new Date();  
		    time = end-start;
			status = timeFormat.format(date) + " " + time + "ms " + bytes + "bytes";
		} 
		// Otherwise control jumps to a catch... 
		catch(MalformedURLException ignored) {
			status = "err";
		} 
		catch(InterruptedException exception) {
			status = "interrupt";
		}
		catch(IOException ignored) {
			status = "err";  
		}
		// "finally" clause, to close the input stream
		// in any case
		finally {
			try{ 
				if (input != null) input.close();
			}
			catch(IOException ignored) {}
		}
 	}
	
}
