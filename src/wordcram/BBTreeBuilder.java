package wordcram;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;

public class BBTreeBuilder {
    public BBTree makeTree(Shape shape, int swelling) {
        Rectangle2D bounds = shape.getBounds2D();
        int minBoxSize = 1;
        int x = (int) bounds.getX();
        int y = (int) bounds.getY();
        int right = x + (int) bounds.getWidth();
        int bottom = y + (int) bounds.getHeight();

        BBTree tree = makeTree(shape, minBoxSize, x, y, right, bottom);
        tree.swell(swelling);
        return tree;
    }

    private BBTree makeTree(Shape shape, int minBoxSize, int x, int y,
            int right, int bottom) {

        int width = right - x;
        int height = bottom - y;

        if (shape.contains(x, y, width, height)) {
            return new BBTree(x, y, right, bottom);
        }
        else if (shape.intersects(x, y, width, height)) {
            BBTree tree = new BBTree(x, y, right, bottom);

            boolean tooSmallToContinue = width <= minBoxSize;
            if (!tooSmallToContinue) {
                int centerX = avg(x, right);
                int centerY = avg(y, bottom);

                // upper left
                BBTree t0 = makeTree(shape, minBoxSize, x, y, centerX, centerY);
                // upper right
                BBTree t1 = makeTree(shape, minBoxSize, centerX, y, right, centerY);
                // lower left
                BBTree t2 = makeTree(shape, minBoxSize, x, centerY, centerX, bottom);
                // lower right
                BBTree t3 = makeTree(shape, minBoxSize, centerX, centerY, right, bottom);

                tree.addKids(t0, t1, t2, t3);
            }

            return tree;
        }
        else {  // neither contains nor intersects
            return null;
        }
    }

    private int avg(int a, int b) {
        // reminder: x >> 1 == x / 2
        // avg = (a+b)/2 = (a/2)+(b/2) = (a>>1)+(b>>1)
        return (a + b) >> 1;
    }
}
