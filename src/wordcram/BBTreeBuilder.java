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

public class BBTreeBuilder {
    public BBTree makeTree(ShapeForBBTree shape, int swelling) {
        int left = shape.getLeft();
        int top = shape.getTop();
        int right = shape.getRight();
        int bottom = shape.getBottom();

        int minBoxSize = 1;
        return makeTree(shape, minBoxSize, left, top, right, bottom).swell(swelling);
    }

    private BBTree makeTree(ShapeForBBTree shape, int minBoxSize, int left, int top,
                            int right, int bottom) {

        int width = right - left;
        int height = bottom - top;

        if (shape.contains(left, top, width, height)) {
            return new BBTree(left, top, right, bottom);
        }
        else if (shape.intersects(left, top, width, height)) {
            BBTree tree = new BBTree(left, top, right, bottom);

            boolean tooSmallToContinue = width <= minBoxSize;
            if (!tooSmallToContinue) {
                int centerX = avg(left, right);
                int centerY = avg(top, bottom);

                // upper left
                BBTree t0 = makeTree(shape, minBoxSize, left, top, centerX, centerY);
                // upper right
                BBTree t1 = makeTree(shape, minBoxSize, centerX, top, right, centerY);
                // lower left
                BBTree t2 = makeTree(shape, minBoxSize, left, centerY, centerX, bottom);
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
