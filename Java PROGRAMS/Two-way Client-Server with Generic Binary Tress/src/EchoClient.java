
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.*;
import java.net.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/** Two way communication implementation of client-server app (client).
 * @author gabriel
 * 
 */
public class EchoClient extends JFrame{
	
	public EchoClient() {
		InitUI();
	}
	
	private void InitUI() {
		setSize(500,700);
		setTitle("Client");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		String hostName = "localhost";
		
		JPanel mainPanel = new JPanel();
		JPanel firstPanel = new JPanel();
		JPanel secondPanel = new JPanel();
		JPanel thirdPanel = new JPanel();
		JPanel fourthPanel = new JPanel();
		
		firstPanel.setLayout(new BorderLayout());
		secondPanel.setLayout(new BorderLayout());
		thirdPanel.setLayout(new BorderLayout());
		fourthPanel.setLayout(new BorderLayout());
		
		JButton insertButton, searchButton, deleteButton, drawButton, integerButton, doubleButton, stringButton; 
		JTextField inputTextField;
		JLabel outputLabel;

        integerButton = new JButton("integer");
        doubleButton = new JButton("double");
        stringButton = new JButton("string");
        
        inputTextField = new JTextField();
        
        insertButton = new JButton("insert");
        searchButton = new JButton("search");
        deleteButton = new JButton("delete");
        drawButton = new JButton("draw");
        
        outputLabel = new JLabel("");
        
        mainPanel.setLayout(new GridLayout(4,1));
        firstPanel.add(integerButton, BorderLayout.LINE_START);
        firstPanel.add(stringButton, BorderLayout.CENTER);
        firstPanel.add(doubleButton, BorderLayout.LINE_END);
        secondPanel.add(inputTextField);
        thirdPanel.add(searchButton, BorderLayout.LINE_START);
        thirdPanel.add(insertButton, BorderLayout.CENTER);
        thirdPanel.add(deleteButton, BorderLayout.LINE_END);
        thirdPanel.add(drawButton, BorderLayout.SOUTH);
        fourthPanel.add(outputLabel);
        
        mainPanel.add(firstPanel);
        mainPanel.add(secondPanel);
        mainPanel.add(thirdPanel);
        mainPanel.add(fourthPanel);
		
	        try {
	            Socket echoSocket = new Socket(hostName, 2500);
	            PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
	            BufferedReader in = new BufferedReader( new InputStreamReader(echoSocket.getInputStream()));
	            
	            integerButton.addActionListener(event -> {
	            	out.println("i");
	            	out.flush();
	            });
	            
	            doubleButton.addActionListener(event -> {
	            	out.println("d");
	            	out.flush();
	            });
	            
	            stringButton.addActionListener(event -> {
	            	out.println("s");
	            	out.flush();
	            });
	            
	            insertButton.addActionListener(event -> {
	            	out.println("I"+inputTextField.getText());
	            	out.flush();
	            	try {
						outputLabel.setText(in.readLine());
						revalidate();
					} catch (IOException e) {
						e.printStackTrace();
					}
	            });
	            
	            searchButton.addActionListener(event -> {
	            	out.println("S"+inputTextField.getText());
	            	out.flush();
	            	try {
						outputLabel.setText(in.readLine());
						revalidate();
					} catch (IOException e) {
						e.printStackTrace();
					}
	            });
	            
	            deleteButton.addActionListener(event -> {
	            	out.println("D"+inputTextField.getText());
	            	out.flush();
	            	try {
						outputLabel.setText(in.readLine());
						revalidate();
					} catch (IOException e) {
						e.printStackTrace();
					}
	            });
	            
	            drawButton.addActionListener(event -> {
	            	out.println("P"+inputTextField.getText());
	            	out.flush();
	            	try {
						outputLabel.setText(in.readLine());
						revalidate();
					} catch (IOException e) {
						e.printStackTrace();
					}
	            });

	         
	        } catch (UnknownHostException e) {
	            System.err.println("Don't know about host " + hostName);
	            System.exit(1);
	        } catch (IOException e) {
	            System.err.println("Couldn't get I/O for the connection to " +
	                hostName);
	            System.exit(1);
	        } 
	        add(mainPanel);
	}
    public static void main(String[] args) throws IOException {
    	java.awt.EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				
				EchoClient ex = new EchoClient();
				ex.setVisible(true);
			}
		});
    }
}
