import java.awt.geom.Rectangle2D;

import java.awt.*;
import java.awt.color.ColorSpace;
/**
 * @author Gabi
 * @since 2019-05-09
 */
public class MyRectangle extends Rectangle2D.Float {
	
		private Color color = new Color(0,0,0);
		private int order;
		
        public MyRectangle(float x, float y, float width, float height) {
            
            setRect(x, y, width, height);
        }
        
        public MyRectangle(float x, float y, float width, float height, Color color) {
            
            setRect(x, y, width, height);
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