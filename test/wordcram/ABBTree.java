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

import junit.framework.Assert;

import org.junit.Test;

public class ABBTree {

    @Test
    public void testContainsPoint() {
	// TODO Could this be done better w/ a theory?
	// (Or whatever those JUnit things are called.)
	BBTree testTree = makeTree(20, 20, 100, 100);

	Assert.assertTrue(testTree.containsPoint(22, 22));
	Assert.assertFalse(testTree.containsPoint(22, 18));
	Assert.assertFalse(testTree.containsPoint(18, 22));

	Assert.assertTrue(testTree.containsPoint(98, 98));
	Assert.assertFalse(testTree.containsPoint(98, 102));
	Assert.assertFalse(testTree.containsPoint(102, 98));

	Assert.assertTrue(testTree.containsPoint(22, 98));
	Assert.assertFalse(testTree.containsPoint(22, 102));
	Assert.assertFalse(testTree.containsPoint(18, 98));

	Assert.assertTrue(testTree.containsPoint(98, 22));
	Assert.assertFalse(testTree.containsPoint(98, 18));
	Assert.assertFalse(testTree.containsPoint(102, 22));
   
	for (int i = 21; i < 100; i++) {
	    for (int j = 21; j < 100; j++) {
		Assert.assertTrue(testTree.containsPoint(i, j));
	    }
	}
    }

    @Test
    public void testOverlaps() {
	BBTree a = makeTree(0, 0, 30, 30);
	BBTree b = makeTree(0, 0, 5, 5);

	Assert.assertTrue(a.overlaps(b));
	Assert.assertTrue(b.overlaps(a));

	b.setLocation(40, 40);
	Assert.assertFalse(a.overlaps(b));
	Assert.assertFalse(b.overlaps(a));

	b.setLocation(29, 29);
	Assert.assertTrue(a.overlaps(b));
	Assert.assertTrue(b.overlaps(a));

	a.setLocation(32, 0);
	Assert.assertTrue(a.overlaps(b));
	Assert.assertTrue(b.overlaps(a));

	a.setLocation(32, 33);
	Assert.assertTrue(a.overlaps(b));
	Assert.assertTrue(b.overlaps(a));

	a.setLocation(32, 34);
	Assert.assertFalse(a.overlaps(b));
	Assert.assertFalse(b.overlaps(a));
    }

    BBTree makeTree(int x, int y, int right, int bottom) {
	return new BBTree(x, y, right, bottom);
    }
}