import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;

import javax.swing.event.MenuListener;

/**
 * <h1>Core_Graph</h1> 
 * The Core_Graph program is younger, impaired brother of well-known Paint. 
 * <p>
 * It allows you to draw rectangles, ellipses and polygons, everything on self-sculptured classes.
 * It also let you do cool things with them f.e. drag, scale, change color. And what is truly valid 
 * you can save them to a .txt file and open it from the same file. Furthermore coding of a saving is 
 * so simple that without bigger problems you can code new figures by your own.
 * <p>
 * To do: 
 * <p> - improve color management (it includes coding from char to rgb-something)
 * <p> - scaling polygons (maybe someday)
 * 
 * @author Gabriel Wechta
 * @version 1:2
 * @since 2019-05-09
 *
 */
public class Core_Graph extends JFrame { 
	
	Point startDrag, endDrag; //for drawing shadows of figures
	
	ArrayList<MyRectangle> r_shapes;
	ArrayList<MyEllipse> e_shapes;
	ArrayList<MyPolygon> p_shapes;
	
	ArrayList<Integer> coor_x; //integers for polygon
	ArrayList<Integer> coor_y;
	Point startPolygon; //for ending polygon
	GeneralPath path = null; //for drawing polygon
	private boolean drawingPolyline = false;
	
	JLabel MyFooter; //for setting footer in main constructor
	
	int shapeOrder = 0; //ta zmienna przechowuje porzadek w jakim obiekty byly rysowane
	
	enum State{ 
		CREAT_R,
		CREAT_E,
		CREAT_P,
		EDIT,
	}
	
	enum ShapeState {
		REC,
		CIRC,
	}
	
	public Color charToColor(char ch) {
	    Color color = Color.BLACK;
		switch(ch) {
		case 'R': color = Color.RED;
			break;
		case 'G': color = Color.GREEN;
			break;
		case 'B': color = Color.BLUE;
			break;
		case 'C': color = Color.BLACK; //to further development
			break;
		default: color = Color.BLACK;
			break;
		}
		return color;
	}
	
	public char colorToChar(Color color) {
	    char ch = 'C';
		if(color == Color.RED) {ch = 'R'; }
		else if (color == Color.GREEN) {ch = 'G'; }
		else if (color == Color.BLUE) {ch = 'B'; }
		else if (color == Color.BLACK) {ch = 'C'; }
		else {ch = 'C'; }

		return ch;
	}
	
	/**
	 * This function saves MyCanvas object to .txt file called "saves.txt".
	 * @see FileWriter
	 */
	public void save() {
		try {    
			FileWriter fw = new FileWriter("saves.txt");    
			
			for(int writingOrder = 0; writingOrder < shapeOrder; writingOrder++) { //obiekty w txt zapisywane sa w kolejnosci wedlug shapeOrder, order jest im przypisywany podczas tworzenia
				
				for(MyRectangle s: r_shapes) {
					if(s.getOrder() == writingOrder) {
						fw.write("r" + ";" + colorToChar(s.getColor()) + ";" + (int)s.x + ";" + (int)s.y + ";" + (int)s.width + ";" + (int)s.height);
						fw.write(System.getProperty("line.separator"));
					}
				}
				
				for(MyEllipse s: e_shapes) {
					if(s.getOrder() == writingOrder) {
						fw.write("e" + ";" + colorToChar(s.getColor()) + ";" + (int)s.x + ";" + (int)s.y + ";" + (int)s.width + ";" + (int)s.height);
						fw.write(System.getProperty("line.separator"));
					}
				}
				
				for(MyPolygon s: p_shapes) {
					if(s.getOrder() == writingOrder) {
						fw.write("p" + ";" + colorToChar(s.getColor()));
						for(int i = 0; i < s.npoints; i++) {
							fw.write(";" + s.xpoints[i]);
							fw.write(";" + s.ypoints[i]);
						}
						fw.write(System.getProperty("line.separator"));
					}
				}
			}
			fw.close();    
	    }
		catch(Exception e) { System.out.println(e); }  
	} 
	
	/**
	 * This function reads MyCanvas from .txt file called "saves.txt".
	 * 
	 */
	public void read() {
        String fileName = "saves.txt";
        String line = null;
        
        r_shapes.clear(); //arraye sa czysczone aby namalowaly zupelnie czysty canvas przed domalowaniem obiektow z pliku
        e_shapes.clear();
        p_shapes.clear();
        repaint();

        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
            	String[] arguments_from_line = line.split(";"); //przerobienie line na tablice stringow aby mozna bylo uzywac kolejnych pol do konwercji na int
            	
            	switch (arguments_from_line[0].charAt(0)) {
            	
            	case 'r' : MyRectangle r_tmp = new MyRectangle(Integer.valueOf(arguments_from_line[2]), Integer.valueOf(arguments_from_line[3]), Integer.valueOf(arguments_from_line[4]), Integer.valueOf(arguments_from_line[5]), charToColor(arguments_from_line[1].charAt(0)));
            		r_tmp.setOrder(shapeOrder);
            		r_shapes.add(r_tmp);
            		break;
            		
            	case 'e' : MyEllipse e_tmp = new MyEllipse(Integer.valueOf(arguments_from_line[2]), Integer.valueOf(arguments_from_line[3]), Integer.valueOf(arguments_from_line[4]), Integer.valueOf(arguments_from_line[5]), charToColor(arguments_from_line[1].charAt(0)));
        		e_tmp.setOrder(shapeOrder);
        		e_shapes.add(e_tmp);
        		break;
            		
            	case 'p' : //przypadek MyPolygon wyglada tak dziko aby czytelnie moc go zapisac do pliku
	            {
	            	int[] xpoints = new int[arguments_from_line.length/2 - 1];
	            	int[] ypoints = new int[arguments_from_line.length/2 - 1];
	            	
	            	for(int i = 2, array_index = 0; i < arguments_from_line.length; i++) {
	            		if(i%2 == 0) { //pierwsza wspolrzedna to x 
	            			xpoints[array_index] = Integer.valueOf(arguments_from_line[i]);
	            		}
	            		else { //druga to y
	            			ypoints[array_index] = Integer.valueOf(arguments_from_line[i]);
	            			array_index++;
	            		}
	            	}
	            	MyPolygon p_tmp = new MyPolygon(xpoints, ypoints, (arguments_from_line.length-1)/2, charToColor(arguments_from_line[1].charAt(0)));
	            	p_tmp.setOrder(shapeOrder);
	            	p_shapes.add(p_tmp);
	            	break;
	            }
            	default : System.out.println("Co� posz�o nie tak z kodowaniem obiekt�w do pliku");
            		break;
            	}
            	shapeOrder++;
            }   
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println("Error reading file '"  + fileName + "'");                  
        }
		repaint();
	}   
	
	/** Very smart method generating object from MyRecntagle class (extending Rectangle2D.Float) based on P1(x1,y1) and P2(x2,y2) which are opposite points in Rectangle. 
	 * 
	 * @param x1 first coordinate
	 * @param y1 second coordinate
	 * @param x2 third coordinate
	 * @param y2 fourth coordinate
	 * @return MyRectangle 
	 * @see Rectangle2D.Float
	 */
	public MyRectangle makeRectangle(int x1, int y1, int x2, int y2) {
        return new MyRectangle(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
    }
	
	/** Incredibly smart method generating object from MyEllipse class (extending Ellispe2D.Float) based on P1(x1,y1) and P2(x2,y2) which are opposite points in rectangle surrounding Ellipse.
	 * 
	 * 
	 * @param x1 first coordinate
	 * @param y1 second coordinate
	 * @param x2 third coordinate
	 * @param y2 fourth coordinate
	 * @return MyEllipse 
	 * @see Ellipse2D.Float
	 */
	public MyEllipse makeEllipse(int x1, int y1, int x2, int y2) {
        return new MyEllipse(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
    }
	
	class MyCanvas extends JComponent {

		class MovingAdapter extends MouseAdapter {
			
			private int x;
	        private int y;
	        
	        @Override
	        public void mouseClicked(MouseEvent e) {
	        	if(state == State.EDIT) {
	        		x = e.getX();
		            y = e.getY();
		            doChangeColor(e);
	        	}
	        	if(state == State.CREAT_P) {
	        		if(!drawingPolyline) {
	        		}
	        		doMakePolygon(e);
	        	}
	        }
	        
	        @Override
	        public void mousePressed(MouseEvent e) {
	            if(state == State.EDIT) {
	            x = e.getX();
		        y = e.getY();
	            repaint();
	            }
	            if(state == State.CREAT_R || state == State.CREAT_E) {
	            startDrag = new Point(e.getX(), e.getY());
	            endDrag = startDrag;
	            repaint();
	            }
	        }
	        
	        @Override
	        public void mouseDragged(MouseEvent e) {
	        	if(state == State.EDIT) {
	            doMove(e);
	        	}
	        	if(state == State.CREAT_R || state == State.CREAT_E) {
	        	endDrag = new Point(e.getX(), e.getY());
	        	repaint();
	        	}
	        }
	        
	        @Override
	        public void mouseReleased(MouseEvent e) {
	        	if(state == State.EDIT) {
	        		MyFooter.setText("");
	        		repaint();
	        	}
	        	else {
	        		if(state == State.CREAT_R) {
		        		MyRectangle rec_tmp = makeRectangle(startDrag.x, startDrag.y, e.getX(), e.getY());
		        		rec_tmp.setOrder(shapeOrder);
		        		shapeOrder++;
			        	r_shapes.add(rec_tmp);
		        	}
		        	if(state == State.CREAT_E) {
		        		MyEllipse ell_tmp = makeEllipse(startDrag.x, startDrag.y, e.getX(), e.getY());
		        		ell_tmp.setOrder(shapeOrder);
		        		shapeOrder++;
		        		e_shapes.add(ell_tmp);
		        	}
		        	startDrag = null;
		        	endDrag = null;
		        	repaint();
	        	}	
	        }
	        
	        @Override
	        public void mouseWheelMoved(MouseWheelEvent e) {
	        	if(state == State.EDIT) {
	        		doScale(e);
	        	}
	        }
	        
	        /**Medium smartness method responsible for making Polygons based on mouse click, it uses GeneralPath class. Method puts new Polygon into global array called "p_shapes".
	         * @param e
	         * @see GeneralPath, mouseClicked, MouseEvent
	         */
	        private void doMakePolygon(MouseEvent e) {
	        	Point p = e.getPoint();
	        	
                if (!drawingPolyline) {
	        		startPolygon = p;
                    path = new GeneralPath();
                    path.moveTo(p.x, p.y);
                    drawingPolyline = true;
                    
                    coor_x.add(p.x);
                    coor_y.add(p.y);
                }
                else {
                    path.lineTo(p.x, p.y);

                    coor_x.add(p.x);
                    coor_y.add(p.y);
                    
                    if(Math.abs(p.x - startPolygon.x) < 15 && Math.abs(p.y - startPolygon.y) < 15) {
                    	drawingPolyline = false;
                    	path.closePath();
                    	path.reset();
                    	
                    	coor_x.add(startPolygon.x);
                        coor_y.add(startPolygon.y);
                        
                        int[] xpoints = new int[coor_x.size()];
                        int[] ypoints = new int[coor_y.size()];
                        
                        for(int i = 0; i < coor_x.size(); i++)
                        {
                        	xpoints[i] = coor_x.get(i);
                        	ypoints[i] = coor_y.get(i);
                        }
                        
                        coor_x.clear();
                        coor_y.clear();
                        
                    	MyPolygon polygon = new MyPolygon(xpoints, ypoints, xpoints.length);
                    	polygon.setOrder(shapeOrder);
                    	shapeOrder++;
                    	p_shapes.add(polygon);
                    }
                }
                repaint();
        	}
	        
	        /**Method responsible for moving objects here and there on MyCanvas (extending JComponent).
	         * 
	         * @param e
	         * @see MyCanvas, JComponent
	         */
	        private void doMove(MouseEvent e) {
	            
	            int dx = e.getX() - x;
	            int dy = e.getY() - y;
	            
	            String wichAreMoving = new String();

	            for(MyRectangle s : r_shapes) {
		            if (s.isHit(x, y)) {
		                s.addX(dx);
		                s.addY(dy);
		                repaint();
		                wichAreMoving += Integer.toString(s.getOrder());
		                wichAreMoving += ", ";
		            }
	            }
	            
	            for(MyEllipse s : e_shapes) {
		            if (s.isHit(x, y)) {
		                s.addX(dx);
		                s.addY(dy);
		                repaint();
		                wichAreMoving += Integer.toString(s.getOrder());
		                wichAreMoving += ", ";
		            }
	            }
	            
	            for(MyPolygon s : p_shapes) {
	            	if(s.contains(x, y)) {
	            		s.translate(dx, dy);
	            		repaint();
	            		wichAreMoving += Integer.toString(s.getOrder());
	            		wichAreMoving += ", ";
	            	}
	            }
	            
	            
	            MyFooter.setText(" x = " + e.getX() + "   y = " + e.getY() + "   You are moving: " + wichAreMoving); //this line makes footer appear when something is being moved
	            
	            x += dx;
	            y += dy;            
	        }
	        
	        /**Method responsible for changing shape's color.
	         * @param e
	         * @see MyRectangle, MyEllipse
	         */
	        private void doChangeColor(MouseEvent e) {
	        	x = e.getX();
	        	y = e.getY();
	        	
	        	for(MyRectangle s : r_shapes) {
		            if (s.isHit(x, y)) {
		                s.changeColor(color);
		                repaint();
		            }
	            }
	        	
	        	for(MyEllipse s : e_shapes) {
		            if (s.isHit(x, y)) {
		                s.changeColor(color);
		                repaint();
		            }
	            }
	        	
	        	for(MyPolygon s : p_shapes) {
	        		if(s.contains(x,y)) {
	        			s.changeColor(color);
	        			repaint();
	        		}
	        	}
	        }
	        
	        /**Very advanced method messing with shape's width and height parameters.
	         * @param e
	         * @see MyRectangle, MyEllipse
	         */
	        private void doScale(MouseWheelEvent e) {
	        	x = e.getX();
	        	y = e.getY();
	        	if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
		        	float amount = e.getWheelRotation() * 5f;
		        	
		            for(MyRectangle s : r_shapes) {
			            if (s.isHit(x, y)) {       
			                s.addHeight(amount);
			                s.addWidth(amount);
			                repaint();
			            }
		            }
		            
		            for(MyEllipse s : e_shapes) {
			            if (s.isHit(x, y)) {       
			                s.addHeight(amount);
			                s.addWidth(amount);
			                repaint();
			            }
		            }
		            
		            for(MyPolygon s : p_shapes) {
		            	if(s.contains(x,y)) {
		            		s.extend(amount);
		            		repaint();
		            	}
		            }
		        }
	        }
		}
		
		@Override
	    public void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        
	        doDrawing(g);        
	    }
		
		private void doDrawing(Graphics g) {
	        
	        Graphics2D g2d = (Graphics2D) g;

	       for(int drawingOrder = 0; drawingOrder < shapeOrder; drawingOrder++) {
	    	   for(MyRectangle s: r_shapes) { 		//RECTANGLES DRAWING
	    		   if(s.getOrder() == drawingOrder) {
			        	g2d.setPaint(s.getColor());
			        	g2d.fill(s);
	    		   }
		        }
		        
		        for(MyEllipse s: e_shapes) { 		//ELLIPSE DRAWING
		        	if(s.getOrder() == drawingOrder) {
			        	g2d.setPaint(s.getColor());
			        	g2d.fill(s);
	    		   }
		        }
		        
		        for(MyPolygon s: p_shapes) {		//POLYLINE DRAWING
		        	if(s.getOrder() == drawingOrder) {
			        	g2d.setPaint(s.getColor());
			        	g2d.fill(s);
	    		   }        	
		        }
	       }
		        
		   if (startDrag != null && endDrag != null) { //this piece is responsible for animating shadow of an object. What's important it's never saved to an array, it only animates it.  
		        g2d.setPaint(Color.LIGHT_GRAY);
		        Shape s;
		        if(state == State.CREAT_R) {
		        	s = makeRectangle(startDrag.x, startDrag.y, endDrag.x, endDrag.y);
		        }
		        else {
		        	s = makeEllipse(startDrag.x, startDrag.y, endDrag.x, endDrag.y);
		        }
		           g2d.fill(s);
		        }
		        
		   if(path != null) {
		        g2d.setPaint(Color.LIGHT_GRAY);
		        g2d.draw(path);
		   }        
	    }
		
		/**
		 * Class Constructor preparing whole canvas for playing with Core_Graph.
		 */
		public MyCanvas() {
			MovingAdapter ma = new MovingAdapter(); 
			addMouseMotionListener(ma);
	        addMouseListener(ma);
	        addMouseWheelListener(ma);
	        
	        setSize(new Dimension(300, 300));
	        
	        r_shapes = new ArrayList<>();
	        e_shapes = new ArrayList<>();
	        p_shapes = new ArrayList<>();
	        coor_x = new ArrayList<>();
	        coor_y = new ArrayList<>();
		}
	}
	
	private static State state = State.CREAT_P;
	private static Color color = Color.BLACK;
	
	public Core_Graph() {
		initUI();
	}
	
	private void initUI() {
		
		JMenuBar menuBar;
		JMenu fileMenu, editMenu, creatingMenu, infoMenu, colorChangeMenu;
		JMenuItem  readMenuItem, saveMenuItem, editingMenuItem, exitingMenuItem, toGreenMenuItem, toBlueMenuItem, toRedMenuItem, toBlackMenuItem,
			creatingRectangleMenuItem, creatingEllipseMenuItem, creatingPolylineMenuItem;
		
		add(new MyCanvas());
		
		setTitle("First things first");
		MyFooter = new JLabel();
		MyFooter.setBackground(new Color(140,140,140));
		add(MyFooter,BorderLayout.SOUTH);
		setSize(720, 720);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		menuBar = new JMenuBar();
		fileMenu = new JMenu("Plik");
		editMenu = new JMenu("Edycja");
		infoMenu = new JMenu("Info");
		colorChangeMenu = new JMenu("Zmien Kolor");
		creatingMenu = new JMenu("Tworzenie");
		readMenuItem = new JMenuItem("Otworz z pliku");
		saveMenuItem = new JMenuItem("Zapisz");
		editingMenuItem = new JMenuItem("Edytownie");
		exitingMenuItem = new JMenuItem("Zakoncz");
		toGreenMenuItem = new JMenuItem("Zielony");
		toBlueMenuItem = new JMenuItem("Niebieski");
		toRedMenuItem = new JMenuItem("Czerwony");
		toBlackMenuItem = new JMenuItem("Czarny");
		creatingRectangleMenuItem = new JMenuItem("Prostokat");
		creatingEllipseMenuItem = new JMenuItem("Okrag");
		creatingPolylineMenuItem = new JMenuItem("Wielokat");
		
		readMenuItem.addActionListener(event -> read());
		fileMenu.add(readMenuItem);
		saveMenuItem.addActionListener(event -> save());
		fileMenu.add(saveMenuItem);
		menuBar.add(fileMenu);
		creatingRectangleMenuItem.addActionListener(event -> state = State.CREAT_R);
		creatingMenu.add(creatingRectangleMenuItem);
		creatingEllipseMenuItem.addActionListener(event -> state = State.CREAT_E);
		creatingMenu.add(creatingEllipseMenuItem);
		creatingPolylineMenuItem.addActionListener(event -> state = State.CREAT_P);
		creatingMenu.add(creatingPolylineMenuItem);
		editMenu.add(creatingMenu);
		editingMenuItem.addActionListener(event -> state = State.EDIT);
		editMenu.add(editingMenuItem);
		editMenu.addSeparator();
		exitingMenuItem.addActionListener(event -> System.exit(0));
		editMenu.add(exitingMenuItem);
		menuBar.add(editMenu);
		toGreenMenuItem.addActionListener(event -> {color = Color.GREEN; state = State.EDIT;} );
		colorChangeMenu.add(toGreenMenuItem);
		toBlueMenuItem.addActionListener(event -> {color = Color.BLUE; state = State.EDIT;});
		colorChangeMenu.add(toBlueMenuItem);
		toRedMenuItem.addActionListener(event -> {color = Color.RED; state = State.EDIT;});
		colorChangeMenu.add(toRedMenuItem);
		toBlackMenuItem.addActionListener(event -> {color = Color.BLACK; state = State.EDIT;});
		colorChangeMenu.add(toBlackMenuItem);
		menuBar.add(colorChangeMenu);
		infoMenu.setToolTipText("Tworca: Gabriel Wechta i giganci na ktorych barkach stoi i kurczowo trzyma sie ich szyi :: ver 1.2");
		menuBar.add(infoMenu);
		setJMenuBar(menuBar);
	
	}
	
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				
				Core_Graph ex = new Core_Graph();
				ex.setVisible(true);
				
			}
		});
		
	}
}
