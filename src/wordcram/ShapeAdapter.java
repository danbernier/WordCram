package wordcram;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;

public class ShapeAdapter implements ShapeForBBTree {
	
	private Shape shape;
	private int left;
	private int top;
	private int right;
	private int bottom;

	public ShapeAdapter(Shape shape) {
		this.shape = shape;
		Rectangle2D bounds = shape.getBounds2D();
		
		this.left = (int) bounds.getX();
        this.top = (int) bounds.getY();
        this.right = left + (int) bounds.getWidth();
        this.bottom = top + (int) bounds.getHeight();
	}

	public boolean contains(int x, int y, int width, int height) {
		return shape.contains(x, y, width, height);
	}

	public boolean intersects(int x, int y, int width, int height) {
		return shape.intersects(x, y, width, height);
	}

	public int getLeft() {
		return left;
	}

	public int getTop() {
		return top;
	}

	public int getRight() {
		return right;
	}

	public int getBottom() {
		return bottom;
	}

}
