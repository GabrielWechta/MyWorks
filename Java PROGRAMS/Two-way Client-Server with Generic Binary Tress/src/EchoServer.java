
import java.net.*;
import java.util.ArrayList;
import java.io.*;

/** Two way communication implementation of client-server app (server).
 * @author gabriel
 * 
 */
public class EchoServer {
	
	enum TreeKind {
		INTEGER,
		DOUBLE,
		STRING,
	}
	
	private static TreeKind treekind = TreeKind.INTEGER;

    public static void main(String[] args) throws IOException {
            	
        try {
            ServerSocket serverSocket = new ServerSocket(2500);
            Socket clientSocket = serverSocket.accept();     
            
            PrintWriter out =  new PrintWriter(clientSocket.getOutputStream(), true);                   
            BufferedReader in = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()));
     
            BinaryTree<Integer> i_tree = new BinaryTree<Integer>();
            BinaryTree<Double> d_tree = new BinaryTree<Double>();
            BinaryTree<String> s_tree = new BinaryTree<String>();
            
            String receiveMessage;
            
            while (true) {
            	if((receiveMessage = in.readLine()) != null) {
            		
            		switch(receiveMessage.charAt(0)) {
            		case 'i': treekind = TreeKind.INTEGER;
            			break;
            		case 'd': treekind = TreeKind.DOUBLE;
        				break;
            		case 's': treekind = TreeKind.STRING;
        				break;
        				
            		case 'I': {
	            			if(treekind == TreeKind.INTEGER) {
	            				String cutReceiveMessage = String.valueOf(receiveMessage.subSequence(1, receiveMessage.length()));
	                        	try {
									int argument = Integer.valueOf(cutReceiveMessage);
									i_tree.insert(argument);
									out.println(i_tree.draw());
								} catch (NumberFormatException e) {
									out.println("Wrong argument type, it should be integer");
								}
	                        	
	                        	out.flush();
	            			}
	            			if(treekind == TreeKind.DOUBLE) {
	            				String cutReceiveMessage = String.valueOf(receiveMessage.subSequence(1, receiveMessage.length()));
	                        	try {
									double argument = Double.valueOf(cutReceiveMessage);
									d_tree.insert(argument);
									out.println(d_tree.draw());
								} catch (NumberFormatException e) {
									out.println("Wrong argument type, it should be double");
								}
	                        	
	                        	out.flush();
	            			}
	            			if(treekind == TreeKind.STRING) {
	            				String cutReceiveMessage = String.valueOf(receiveMessage.subSequence(1, receiveMessage.length()));
	                        	try {
									String argument = String.valueOf(cutReceiveMessage);
									s_tree.insert(argument);
									out.println(s_tree.draw());
								} catch (Exception e) {
									out.println("Wrong argument type, it should be string");
								}
	                        		                        	
	                        	out.flush();
	            			}
            			}
        				break;
        				
	            	case 'D': {
	            			if(treekind == TreeKind.INTEGER) {
	            				String cutReceiveMessage = String.valueOf(receiveMessage.subSequence(1, receiveMessage.length()));
	                        	try {
									int argument = Integer.valueOf(cutReceiveMessage);
									i_tree.delete(argument);
									out.println(i_tree.draw());
								} catch (NumberFormatException e) {
									out.println("Wrong argument type, it should be integer");
								}
	                        	
	                        	out.flush();
	            			}
	            			if(treekind == TreeKind.DOUBLE) {
	            				String cutReceiveMessage = String.valueOf(receiveMessage.subSequence(1, receiveMessage.length()));
	                        	try {
									double argument = Double.valueOf(cutReceiveMessage);
									d_tree.delete(argument);
									out.println(d_tree.draw());
								} catch (NumberFormatException e) {
									out.println("Wrong argument type, it should be double");
								}
	                        	
	                        	out.flush();
	            			}
	            			if(treekind == TreeKind.STRING) {
	            				String cutReceiveMessage = String.valueOf(receiveMessage.subSequence(1, receiveMessage.length()));
	                        	try {
									String argument = cutReceiveMessage;
									s_tree.delete(argument);
									out.println(s_tree.draw());
								} catch (Exception e) {
									out.println("Wrong argument type, it should be string");
								}
	                        	
	                        	out.flush();
	            			}
	        			}
	    				break;
	            	case 'S': {
	            			if(treekind == TreeKind.INTEGER) {
	            				String cutReceiveMessage = String.valueOf(receiveMessage.subSequence(1, receiveMessage.length()));
	                        	try {
									int argument = Integer.valueOf(cutReceiveMessage);
									out.println(i_tree.search(argument));
									i_tree.draw();
								} catch (NumberFormatException e) {
									out.println("Wrong argument type, it should be integer");
								}
	                        	
	                        	out.flush();
	            			}
	            			if(treekind == TreeKind.DOUBLE) {
	            				String cutReceiveMessage = String.valueOf(receiveMessage.subSequence(1, receiveMessage.length()));
	                        	try {
									double argument = Double.valueOf(cutReceiveMessage);
									out.println(d_tree.search(argument));
									d_tree.draw();
								} catch (NumberFormatException e) {
									out.println("Wrong argument type, it should be double");
								}
	                        	
	                        	out.flush();
	            			}
	            			if(treekind == TreeKind.STRING) {
	            				String cutReceiveMessage = String.valueOf(receiveMessage.subSequence(1, receiveMessage.length()));
	                        	try {
									String argument = cutReceiveMessage;
									out.println(s_tree.search(argument));
									s_tree.draw();
								} catch (Exception e) {
									out.println("Wrong argument type, it should be string");
								}
	                        	
	                        	out.flush();
	            			}
	        			}
	    				break;
	            	case 'P': {
		        			if(treekind == TreeKind.INTEGER) {
		                    	out.println(i_tree.draw());
		                    	out.flush();
		        			}
		        			if(treekind == TreeKind.DOUBLE) {
		                    	out.println(d_tree.draw());
		        				out.flush();
		        			}
		        			if(treekind == TreeKind.STRING) {
		                    	out.println(s_tree.draw());
		        				out.flush();
		        			}
		    			}
						break;
					default: {
							System.out.println("Unhandled input from client.");
						}
						break;
	    			}
            	}
                out.flush();
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                + 2500 + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}