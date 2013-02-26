package wordcram;

import java.util.ArrayList;

import processing.core.PGraphics;

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

public class BBTree {

    private int left;
    private int top;
    private int right;
    private int bottom;
    private BBTree[] kids;

    private BBTree parent;
    private int rootX;
    private int rootY;

    private int swelling = 0;

    BBTree(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    void addKids(BBTree... _kids) {
        ArrayList<BBTree> kidList = new ArrayList<BBTree>();
        for (BBTree kid : _kids) {
            if (kid != null) {
                kidList.add(kid);
		kid.parent = this;
            }
        }

        kids = kidList.toArray(new BBTree[0]);
    }

    public void setLocation(int left, int top) {
    	rootX = left;
    	rootY = top;
    }

    private BBTree getRoot() {
	   return parent == null ? this : parent.getRoot();
    }

    public boolean overlaps(BBTree otherTree) {
        if (rectCollide(this, otherTree)) {
            if (this.isLeaf() && otherTree.isLeaf()) {
                return true;
            }
            else if (this.isLeaf()) {  // Then otherTree isn't a leaf.
                for (BBTree otherKid : otherTree.kids) {
                    if (this.overlaps(otherKid)) {
                        return true;
                    }
                }
            }
            else {
                for (BBTree myKid : this.kids) {
                    if (otherTree.overlaps(myKid)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean rectCollide(BBTree aTree, BBTree bTree) {
        int[] a = aTree.getPoints();
        int[] b = bTree.getPoints();

        return a[3] > b[1] && a[1] < b[3] && a[2] > b[0] && a[0] < b[2];
    }

    private int[] getPoints() {
	BBTree root = getRoot();
        return new int[] {
                root.rootX - swelling + left,
                root.rootY - swelling + top,
                root.rootX + swelling + right,
                root.rootY + swelling + bottom
        };
    }

    boolean containsPoint(float x, float y) {
        BBTree root = getRoot();
        return root.rootX + this.left < x &&
            root.rootX + this.right > x &&
            root.rootY + this.top < y &&
            root.rootY + this.bottom > y;
    }

    boolean isLeaf() {
        return kids == null;
    }

    void swell(int extra) {
        swelling += extra;
        if (!isLeaf()) {
            for (int i = 0; i < kids.length; i++) {
                kids[i].swell(extra);
            }
        }
    }

    public void draw(PGraphics g) {
        g.pushStyle();
        g.noFill();

        g.stroke(30, 255, 255, 50);
        drawLeaves(g);

        g.popStyle();
    }

    private void drawLeaves(PGraphics g) {
        if (this.isLeaf()) {
            drawBounds(g, getPoints());
        } else {
            for (int i = 0; i < kids.length; i++) {
                kids[i].drawLeaves(g);
            }
        }
    }

    private void drawBounds(PGraphics g, int[] rect) {
        g.rect(rect[0], rect[1], rect[2], rect[3]);
    }
}
