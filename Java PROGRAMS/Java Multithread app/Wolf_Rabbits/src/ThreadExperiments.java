
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.Panel;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

/*to do
 * (1) fix smart making animals
 * (2) showing gamematirx
 * (3) DEBUG 
 * (4) add k parameter
 */

/**
 * @author Gabi
 *
 */
class Rabbit implements Runnable {
	
	Point pos = new Point(0,0);
	String Name;
	ThreadExperiments GbRef; //where rabbit is doing his job
	int number;
	boolean isAlive = true;
	
	public Rabbit(String Name, ThreadExperiments Gb, Point startingPoint ,int number) {
		this.Name = Name;
		this.GbRef = Gb;
		this.pos = startingPoint;
		this.number = number;
		GbRef.GameMatrix[pos.x][pos.y] = 'R';
	}
	@Override
	public void run() {
		while (isAlive) {
			GbRef.doMoveRabbit(this);
			try {
				Thread.sleep(GbRef.time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
		}
		System.out.println(Name + " +++UMARL+++");
	}
}

class Wolf implements Runnable {
	Point pos = new Point(0,0);
	String Name;
	ThreadExperiments GbRef; //where wolf is doing his job
	boolean isReady = true;
	
	public Wolf (String Name, ThreadExperiments Gb, Point startingPoint) {
		this.Name = Name;
		this.GbRef = Gb;
		this.pos = startingPoint;
		GbRef.GameMatrix[pos.x][pos.y] = 'W';
	}
	
	@Override
	public void run() {
		while (GbRef.RabbitCount != 0) {
			if(GbRef.wolfFlag) {
				GbRef.doMoveWolf(this);	
			}		
			try {
				Thread.sleep(GbRef.time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		GbRef.panel.removeAll();
		GbRef.panel.add(new JLabel("Good Job Mr. Wolf! Only one question remains. Did yout really need to eat all of them?"));
		GbRef.panel.revalidate();
		GbRef.revalidate();
	}
}

public class ThreadExperiments extends JFrame {
		static int ROW;
	    static int COL;
	    int time;
		int cycleAfterDeathCount = 0; //variable responsible for wolf's rest
		int RabbitCount;
		
		boolean wolfFlag = true; //is wolf ready to move
		char[][] GameMatrix;
		JLabel[][] grid;
		JPanel panel = new JPanel(); //panel in main frame
		
		ArrayList<Rabbit> rabbitList = new ArrayList<Rabbit>();
		ArrayList<Thread> rabbtithreadList = new ArrayList<Thread>();
		
		Point wolfPosition;

		int[] FirstCheckingTab =  {1, 1, 1, 0,-1,-1,-1, 0, 0}; //all possibilities of moving N,NE,E... and last option to stay in the same place
		int[] SecondCheckingTab = {1, 0,-1,-1,-1, 0, 1, 1, 0}; 
		
		Random rand = new Random();
		
		public ThreadExperiments() {
			initUI();
		}
		
	    private void initUI() {
	    	
	    	setSize(1000, 1000);
	        setTitle("First threads first");
	        setLocationRelativeTo(null);
	        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	        
	        JButton onlyButton = new JButton("PLAY");
	        
	        onlyButton.addActionListener(event -> { //creating frame for gathering arguments for simulation
	        	
	        	JFrame popupFrame = new JFrame();
	        	popupFrame.setTitle("program argumetns");
	        	popupFrame.setSize(100, 200);
	        	popupFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	        	
	            JPanel mainPanel = new JPanel();
	            JLabel howManyRabbitsLabel = new JLabel();
	            JLabel xSizeLabel = new JLabel();
	            JLabel ySizeLabel = new JLabel();
	            JLabel kLabel = new JLabel();
	            JTextField howManyRabbitsTextField = new JTextField("");
	            JTextField xSizeTextField = new JTextField("");
	            JTextField ySizeTextField = new JTextField("");
	            JTextField kTextField = new JTextField("");
	            JButton sumbitButton = new JButton();

	            mainPanel.setLayout(new GridLayout(5, 2, 10, 20));
	            
	            howManyRabbitsLabel.setText("How many rabbits do you want?: ");
	            xSizeLabel.setText("X: ");
	            ySizeLabel.setText("Y: ");
	            kLabel.setText("k (time cycle in [0.5k , 1.5k](milisec): ");
	            sumbitButton.setText("Submit");
	            
	        	popupFrame.setVisible(true);
	        	onlyButton.setVisible(false);
	        	
	            popupFrame.add(mainPanel);
	            mainPanel.add(howManyRabbitsLabel);
	            mainPanel.add(howManyRabbitsTextField);
	            mainPanel.add(xSizeLabel);
	            mainPanel.add(xSizeTextField);
	            mainPanel.add(ySizeLabel);
	            mainPanel.add(ySizeTextField);
	            mainPanel.add(kLabel);
	            mainPanel.add(kTextField);
	            sumbitButton.addActionListener(second_event -> {
	            	
	            	int fluff = 0, xSize = 0, ySize = 0, k = 0 ;
	            	boolean isokay = true;
	            	
					try {
						fluff = Integer.parseInt(howManyRabbitsTextField.getText());
						xSize = Integer.parseInt(xSizeTextField.getText());
					    ySize = Integer.parseInt(ySizeTextField.getText());
						k = Integer.parseInt(kTextField.getText());
					} catch (NumberFormatException e1) {
						JOptionPane.showMessageDialog(this, "Something is empty, or incorrect! Please check again", "Incorrect Input", JOptionPane.ERROR_MESSAGE);
						isokay = false;
						e1.printStackTrace();
					}
	            	if(isokay) {
	            		if(xSize < 1 || ySize < 1 || fluff < 1){
		                    JOptionPane.showMessageDialog(this, "Bad data!", "Incorrect Input", JOptionPane.ERROR_MESSAGE);
		            	}
		            	else {
		            		System.out.println(xSize + " " + ySize + " " + fluff);
		            		RabbitCount = fluff;
		            		ROW = xSize;
		            		COL = ySize;
		            		time = ((rand.nextInt(10) + 5)/2)*k; 
		            		grid = new JLabel[ROW][COL];
		            		wolfPosition = new Point(ROW - 1, COL - 1);
		            		
		            		ArrayList<Point> rabbitCoordinates = whereRabbitsAre(fluff);
		            		
		            	    GameMatrix = new char[ROW][COL];
		            	    
		        	    	for(int i = 0; i < ROW; i++) { //Initializing GameMatrix with '0'
		        				for(int j = 0 ; j < COL; j++ ) { 	
		        				GameMatrix[i][j] = '0';
		        				}
		        	    	}
		        	 
		        	    	for(Point r_point: rabbitCoordinates) {  //Initializing GameMatrix with 'R' in all places where rabbits need to be. We do it because first thread print their movement before last rabbits manage to write themselves to GameMatrix
		        	    		GameMatrix[r_point.x][r_point.y] = 'R';
		        	    	}
		        	    	
		        	    	GameMatrix[wolfPosition.x][wolfPosition.y] = 'W'; //Setting wolf position
		        	    	
		            		popupFrame.setVisible(false);
		            		
		            		for(int i = 0; i < fluff; i++) {
		            			rabbitList.add(new Rabbit(i + " Ewa", this, rabbitCoordinates.get(i), i)); //Making rabbits
		            			rabbtithreadList.add(new Thread(rabbitList.get(i)));
		            			rabbtithreadList.get(i).start();
		            		}

		            		Wolf w = new Wolf("BigBy", this, wolfPosition);
		            		Thread wolfThread = new Thread(w);
		            		wolfThread.start();
		            	}
	            	}
	            	
	            	panel.setLayout(new GridLayout(ROW,COL));
     	
	            	 for (int i = 0; i < ROW; i++){
	            	        for (int j = 0; j < COL; j++){
	            	            grid[i][j] = new JLabel(String.valueOf(GameMatrix[i][j]));
	            	            grid[i][j].setBorder(new LineBorder(Color.BLACK));
	            	            grid[i][j].setOpaque(true);
	            	            panel.add(grid[i][j]);
	            	        }
	            	    }
	            	 
	            	add(panel);
	            	panel.setVisible(true);
	            	revalidate();
	            });
	            mainPanel.add(sumbitButton);
	            popupFrame.pack();
				
	        });
	        add(onlyButton);
	    }

		
		/**
		 * This method checks if movement to that place (row,col) is valid (inside matrix)
		 * @param row
		 * @param col
		 * @return true if animal can move there
		 */
		boolean isValid(int row, int col) { 
			if((row >= 0) && (row < ROW) && (col >= 0) && (col < COL)) return true;
			else return false;													
		}
		
		/**
		 * Simple distance method
		 * @param p1
		 * @param p2
		 * @return distance (double)
		 */
		static double distance(Point p1, Point p2) {
			return  Point2D.distance(p1.x, p1.y, p2.x, p2.y);
		}
		
		/**
		 * ArrayList<Point> type method randomly generating array with rabbit coordinates
		 * @param howMany
		 * @return ArrayList of points where rabbits need to be settle
		 */
		ArrayList<Point> whereRabbitsAre(int howMany){
			ArrayList<Point> A = new ArrayList<Point>();
			
			for(int j = 0; j < howMany; j++) { //making "howMany" rabbits coordinates
				int x = rand.nextInt(ROW) , y = rand.nextInt(COL);
				
				for(int i = 0; i < A.size(); i++) { //if they match with another rabbit -> make new coordinates
					 x = rand.nextInt(ROW);
					 y = rand.nextInt(COL);
					
					if((A.get(i).x == x && A.get(i).y == y ) || (x == ROW -1 && y == COL -1)) {
						
						i--;
						continue;
					}
				}
				A.add(new Point(x,y));	
			}
			return A;
		}
		
		/**
		 * Only method that Rabbit (runnable) does. It's responsible for moving rabbit on GameMatrix. 
		 * @param r
		 */
		synchronized void doMoveRabbit(Rabbit r) {
			cycleAfterDeathCount++;
			
			if(GameMatrix[r.pos.x][r.pos.y] == 'W') { //Happens when rabbit finds himself inside wolf
				r.isAlive = false;
				wolfFlag = false;
				cycleAfterDeathCount = 0;
				RabbitCount--;
				rabbitList.remove(r.number);
				for(Rabbit s : rabbitList) {
					if(s.number > r.number) s.number += -1;
				}
				return;
			}
			
			if(cycleAfterDeathCount >=6 ) { //checks if wolf is ready to stop resting
				wolfFlag = true;
				cycleAfterDeathCount = 0;
			}

			int index = rand.nextInt(8);
			double max = 0.0;
			int mem = 8;		 				//staying in the same place
			
			for(int i = 0 ; i < 8 ; i++) {
				index = (index+1)%8;
				int row = r.pos.x + FirstCheckingTab[index];
				int col = r.pos.y + SecondCheckingTab[index];
				
				if(isValid(row,col) && GameMatrix[row][col] == '0') {
					double d = distance(new Point(row,col), wolfPosition);
					
					if(d > max) { 
						max = d;
						mem = index;
					}
				}
			}
			GameMatrix[r.pos.x][r.pos.y] = '0';
			r.pos.x = r.pos.x + FirstCheckingTab[mem];
			r.pos.y = r.pos.y + SecondCheckingTab[mem];
			GameMatrix[r.pos.x][r.pos.y] = 'R';
			
			printGameMatrix();
		}
		
		synchronized void doMoveWolf(Wolf w) { //almost exactly as rabbit method. Look up
			w.isReady = true;
			int index = rand.nextInt(8);
			double min = 100000.0;
			int mem = 8;		 				//staying in the same place

			for(int i = 0 ; i < 8 ; i++) {
				index = (index+1)%8;
				int row = w.pos.x + FirstCheckingTab[index];
				int col = w.pos.y + SecondCheckingTab[index];
				
				if(isValid(row,col)) {
					for(Rabbit r : rabbitList) {
						double d = distance(new Point(row,col), r.pos);
						if(d < min) {
							min = d;
							mem = index;
						}
					}
				}
			}
			GameMatrix[w.pos.x][w.pos.y] = '0';
			w.pos.x = w.pos.x + FirstCheckingTab[mem];
			w.pos.y = w.pos.y + SecondCheckingTab[mem];
			wolfPosition.x = w.pos.x;
			wolfPosition.y = w.pos.y;
			GameMatrix[w.pos.x][w.pos.y] = 'W';
			
			printGameMatrix(); 
		}
		
		void printGameMatrix() {
			 panel.removeAll();
	       	 for (int i = 0; i < ROW; i++){
	 	        for (int j = 0; j < COL; j++){
	 	        	JLabel label = new JLabel(String.valueOf(GameMatrix[i][j]));
	 	        	label.setFont(new Font("Serif", Font.PLAIN, 100));
	 	        	label.setPreferredSize(new Dimension(50, 50));
	 	            grid[i][j] = label;
	 	            grid[i][j].setBorder(new LineBorder(Color.BLACK));
	 	            grid[i][j].setOpaque(true);
	 	           
	 	            panel.add(grid[i][j]);
	 	        }
	 	    }
			revalidate();
		}
		
			       
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				
				ThreadExperiments ex = new ThreadExperiments();
				ex.setVisible(true);
			}
		});
		
	}
}