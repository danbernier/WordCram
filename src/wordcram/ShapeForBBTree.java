package wordcram;

interface ShapeForBBTree {

	boolean contains(int x, int y, int width, int height);
	boolean intersects(int x, int y, int width, int height);
	
	int getLeft();
	int getTop();
	int getRight();
	int getBottom();
}
