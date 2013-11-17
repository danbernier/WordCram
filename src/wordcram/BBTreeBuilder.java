package wordcram;

/*
 Copyright 2010 Daniel Bernier

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

import java.awt.Shape;
import java.awt.geom.Rectangle2D;

public class BBTreeBuilder {
    public BBTree makeTree(Shape shape, int swelling) {
        Rectangle2D bounds = shape.getBounds2D();
        int minBoxSize = 1;
        int x = (int) bounds.getX();
        int y = (int) bounds.getY();
        int width = (int) bounds.getWidth();
        int height = (int) bounds.getHeight();

        BBTree tree = makeTree(shape, minBoxSize, x, y, width, height);
         tree.swell(swelling);
        return tree;
    }

    private BBTree makeTree(Shape shape, int minBoxSize, int x, int y,
            int width, int height) {

        if (shape.contains(x, y, width, height)) {
            return new BBTree(x, y, width, height);
        }
        else if (shape.intersects(x, y, width, height)) {
            BBTree tree = new BBTree(x, y, width, height);

            boolean tooSmallToContinue = width <= minBoxSize;
            if (!tooSmallToContinue) {
                int halfHeight = height/2;
                int halfWidth = width/2;

                // upper left
                BBTree t0 = makeTree(shape, minBoxSize, x, y, halfWidth, halfHeight);
                // upper right
                BBTree t1 = makeTree(shape, minBoxSize, x+halfWidth, y, halfWidth, halfHeight);
                // lower left
                BBTree t2 = makeTree(shape, minBoxSize, x, y+halfHeight, halfWidth, halfHeight);
                // lower right
                BBTree t3 = makeTree(shape, minBoxSize, x+halfWidth, y+halfHeight, halfWidth, halfHeight);

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
