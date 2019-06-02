import java.awt.Color;
import java.awt.geom.Ellipse2D;

/**
 * @author Gabi
 * @since 2019-05-09
 */
public class MyEllipse extends Ellipse2D.Float { 
	
		private Color color = new Color(0,0,0);
		private int order;
		
		public MyEllipse(float x, float y, float width, float height) {
            
            setFrame(x, y, width, height);
        }
        
        public MyEllipse(float x, float y, float width, float height, Color color) {
            
            setFrame(x, y, width, height);
            this.color = color; 
        }

        public boolean isHit(float x, float y) {
            
            return getBounds2D().contains(x, y);
        }

        public void addX(float x) {
            
            this.x += x;
        }

        public void addY(float y) {
            
            this.y += y;
        }

        public void addWidth(float w) {
            
            this.width += w;
        }

        public void addHeight(float h) {
            
            this.height += h;
        }
        
        public Color getColor() {
        	return this.color;
        }
        
        public void changeColor(Color cs) {
        	this.color = cs;
        }
        
        public int getOrder() {
			return order;
		}

		public void setOrder(int order) {
			this.order = order;
		}  
    }