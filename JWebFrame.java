import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class JWebFrame extends JFrame {
	private static DefaultTableModel model;
	public JTable table;
	private JPanel panel;
	private JButton single, concur, stop;
	private JTextField text;
	private JLabel running, completed, elapsed;
	private JProgressBar progress;
	private int line = 0;
	public int runningThreads = 0;
	public int completedThreads = 0;
	public long timeElapsed = 0;
	public Semaphore sem;
	private Thread th;
	private ArrayList<WebWorker> arr;
	private static Lock l = new ReentrantLock();
	
	
	public JWebFrame(){
		super("WebLoader");
		setSize(new Dimension(600,500));
		panel = new JPanel();
		model = new DefaultTableModel(new String[] { "url", "status"}, 0);
		table = new JTable(model);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		JScrollPane scrollpane = new JScrollPane(table);
		scrollpane.setPreferredSize(new Dimension(600,300));
		panel.add(scrollpane);  
		
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		single = new JButton("Single Thread Fetch");
		panel.add(single);
		concur = new JButton("Concurrent Fetch");
		panel.add(concur);
		
		text = new JTextField();
		text.setMaximumSize(new Dimension(50, 50));
		panel.add(text);
		
		running = new JLabel("Running:");
		panel.add(running);
        completed = new JLabel("Completed:");
        panel.add(completed);
        elapsed = new JLabel("Elapsed:"); 
        panel.add(elapsed);
		
        progress = new JProgressBar();
        panel.add(progress);
        
		stop = new JButton("Stop");
		stop.setEnabled(false);
     	panel.add(stop);
     	
     	add(panel); 
     	setLocationByPlatform(true);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		allActionLists();
	}
	
	private void allActionLists() {
		single.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				th = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							runThreads(1);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				});
				th.start();
				single.setEnabled(false);
				concur.setEnabled(false);
				stop.setEnabled(true);
			}
		});
		concur.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final int numOfThreads = Integer.parseInt(text.getText()); 
				th = new Thread(new Runnable() {
					
					@Override
					public void run() {
						try {
							runThreads(numOfThreads);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					} 
				});
				th.start();
				single.setEnabled(false);
				concur.setEnabled(false);
				stop.setEnabled(true); 
			} 
		});
		 
		stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for(int i =0; i<arr.size(); i++) {
					arr.get(i).interrupt();
				}
				th.stop(); 
				single.setEnabled(true);
				concur.setEnabled(true);
				stop.setEnabled(false);
			}
		});
	}
	
	static void printSites(String file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String eachUrl;
		while (true) {
			eachUrl = reader.readLine();
			if(eachUrl == null) break;
            model.addRow(new String[]{eachUrl, ""});
        }
	} 
	
	public static void main(String args[]) throws IOException {
		JWebFrame j = new JWebFrame();
		printSites("links.txt"); 
	}

	public void setLabels() {
		 running.setText("Running:" + runningThreads);
         completed.setText("Complete:" + completedThreads);
         elapsed.setText("Elapsed:" + timeElapsed);
	}
	
	public void runThreads(int number) throws InterruptedException { 
		sem = new Semaphore(number);
		arr = new ArrayList<WebWorker>();
		while(line < model.getRowCount()) {
			sem.acquire();
			WebWorker worker = new WebWorker((String)model.getValueAt(line, 0), line, this);
			arr.add(worker);
			l.lock();
			runningThreads++;
			l.unlock();
			worker.start(); 
			line++; 
		}
		single.setEnabled(false);
		concur.setEnabled(false);
		stop.setEnabled(true);
	}
	
}
