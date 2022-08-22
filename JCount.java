// JCount.java

/*
 Basic GUI/Threading exercise.
*/

import javax.swing.*;

import java.awt.Dimension;
import java.awt.event.*;

public class JCount extends JPanel {
	private JTextField text;
	private JLabel label;
	private JButton start;
	private JButton stop;
	private jCountThread thread;
	
	public JCount() {
		// Set the JCount to use Box layout
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		text = new JTextField();
		add(text);
		label = new JLabel("0"); 
		add(label);
		start = new JButton("Start");
		add(start);
		stop = new JButton("Stop");
		add(stop);
		add(Box.createRigidArea(new Dimension(0,40)));
		start.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) { 
				if(thread != null) {
					thread.stop();
				}
				thread = new jCountThread(label, text);
				thread.start();
			}
		});
		
		stop.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				thread.stop();
			}
		});
	} 
	
	static public void main(String[] args)  {
		// Creates a frame with 4 JCounts in it.
		// (provided)
		JFrame frame = new JFrame("The Count");
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		
		frame.add(new JCount());
		frame.add(new JCount());
		frame.add(new JCount());
		frame.add(new JCount());
		
		frame.setLocationByPlatform(true);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}

class jCountThread extends Thread{
	private JLabel label;
	private int finish;
	private int starter;
	public jCountThread(JLabel l, JTextField t) {
		label = l;
		starter = 0;
		finish = 0;
		try {
			finish = Integer.parseInt(t.getText());
		}catch (NumberFormatException ignore) {}
	}
	
	public void run() {
		while(finish > starter) {
			starter++;
			if(starter % 10000 == 0) {
				label.setText(Integer.toString(starter));
				try {
					sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(finish == starter) label.setText(Integer.toString(starter));
		}
	}
}

