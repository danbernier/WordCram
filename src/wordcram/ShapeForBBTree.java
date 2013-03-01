package wordcram;

public interface ShapeForBBTree {

	boolean contains(float x, float y, float width, float height);
	boolean intersects(float x, float y, float width, float height);
	
	int getLeft();
	int getTop();
	int getRight();
	int getBottom();
}
