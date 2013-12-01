package wordcram;

import junit.framework.Assert;

import org.junit.Test;

public class ABBTree {

    @Test
    public void testContainsPointWithASingleTreeNode() {
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
    public void testOverlapsWithASingleTreeNode() {
		BBTree a = makeTree(0, 0, 30, 30);
		BBTree b = makeTree(0, 0, 5, 5);

		assertOverlap(a, b);

		b.setLocation(40, 40);
		assertNoOverlap(a, b);

		b.setLocation(29, 29);
		assertOverlap(a, b);

		a.setLocation(32, 0);
		assertOverlap(a, b);

		a.setLocation(32, 33);
		assertOverlap(a, b);

		a.setLocation(32, 34);
		assertNoOverlap(a, b);
    }

    /*
      Tests with a pattern kind of like this:
      --------
      |***   |
      |***   |
      |   ***|
      |   ***|
      --------
      (Though they're squares.)
     */
    @Test
    public void testOverlapsWithNestedTreeNodes() {
		BBTree root      = makeTree(10, 10, 20, 20);
		BBTree upLeft    = makeTree(10, 10, 15, 15);
		BBTree downRight = makeTree(15, 15, 20, 20);
		root.addKids(upLeft, downRight);

		BBTree mobile = makeTree(0, 0, 4, 4);

		assertNoOverlap(root, mobile);

		mobile.setLocation(8, 8);
		assertOverlap(root, mobile);

		mobile.setLocation(16, 8);
		assertNoOverlap(root, mobile);

		mobile.setLocation(8, 16);
		assertNoOverlap(root, mobile);

		mobile.setLocation(13, 13);
		assertOverlap(root, mobile);

		mobile.setLocation(16, 16);
		assertOverlap(root, mobile);

		mobile.setLocation(22, 22);
		assertNoOverlap(root, mobile);
    }

    BBTree makeTree(int left, int top, int right, int bottom) {
		return new BBTree(left, top, right, bottom);
    }

    private void assertOverlap(BBTree a, BBTree b) {
		Assert.assertEquals(true, a.overlaps(b));
		Assert.assertEquals(true, b.overlaps(a));
    }

    private void assertNoOverlap(BBTree a, BBTree b) {
		Assert.assertEquals(false, a.overlaps(b));
		Assert.assertEquals(false, b.overlaps(a));
    }
}
