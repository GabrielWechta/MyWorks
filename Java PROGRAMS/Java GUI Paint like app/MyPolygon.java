import java.awt.Color;
import java.awt.Polygon;

/**
 * @author Gabi
 * @since 2019-05-09
 */
public class MyPolygon extends Polygon {
	private Color color = new Color(0,0,0);
	private int order;
	
	MyPolygon(int[] xpoints, int[] ypoints, int npoints){
		super(xpoints, ypoints, npoints);
	}
	
	MyPolygon(int[] xpoints, int[] ypoints, int npoints, Color color){
		super(xpoints, ypoints, npoints);
		this.color = color;
	}
	
	private int centerX() {
		int centerPoint_x = 0;
		for(int i = 0; i < npoints; i++) {
			centerPoint_x += xpoints[i];
		}
		return centerPoint_x/npoints;
	}
	
	private int centerY() {
		int centerPoint_y = 0;
		for(int i = 0; i < npoints; i++) {
			centerPoint_y += ypoints[i];
		}
		return centerPoint_y/npoints;
	}
	
	public void extend(float amount) {
		int centerPoint_x = centerX();
		int centerPoint_y = centerY();
		
		for(int i = 0; i < npoints; i++) {
			if(xpoints[i] > centerPoint_x) {
				if(ypoints[i] > centerPoint_y) {
					xpoints[i] += amount;
					ypoints[i] += amount;
				}
				else {
					xpoints[i] += amount;
					ypoints[i] -= amount;
				}
			}
			else {
				if(ypoints[i] > centerPoint_y) {
					xpoints[i] -= amount;
					ypoints[i] += amount;
				}
				else {
					xpoints[i] -= amount;
					ypoints[i] -= amount;
				}
			}
		}
		
	}
	public void changeColor(Color cs) {
		this.color = cs;
	}
	public Color getColor() {
		return this.color;
	}
	
    public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
	
}
