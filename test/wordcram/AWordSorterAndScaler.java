package wordcram;

import junit.framework.Assert;

import org.junit.Test;

public class AWordSorterAndScaler {

	private float epsilon = 0.00001f;

	@Test
	public void testSortingAndScalingWithNormalWeights() {
		Word[] tinyWords = new Word[] {
				new Word("tiny  ", 1),
				new Word("little", 2),
				new Word("things", 3)
		};

		Word[] weighted = new WordSorterAndScaler().sortAndScale(tinyWords);

		Assert.assertEquals("Didn't get out 3 Words", 3, weighted.length);

		Assert.assertEquals(3/3f, weighted[0].weight, epsilon);
		Assert.assertEquals(2/3f, weighted[1].weight, epsilon);
		Assert.assertEquals(1/3f, weighted[2].weight, epsilon);

		Assert.assertEquals("things", weighted[0].word);
		Assert.assertEquals("little", weighted[1].word);
		Assert.assertEquals("tiny  ", weighted[2].word);
	}

	@Test
	public void testSortingAndScalingWithTinyWeights() {
		Word[] tinyWords = new Word[] {
				new Word("critter", 0.000001f),
				new Word("crayter", 0.000002f),
				new Word("croyter", 0.000003f)
		};

		Word[] weighted = new WordSorterAndScaler().sortAndScale(tinyWords);

		Assert.assertEquals("Didn't get out 3 Words", 3, weighted.length);

		Assert.assertEquals(3/3f, weighted[0].weight, epsilon);
		Assert.assertEquals(2/3f, weighted[1].weight, epsilon);
		Assert.assertEquals(1/3f, weighted[2].weight, epsilon);

		Assert.assertEquals("croyter", weighted[0].word);
		Assert.assertEquals("crayter", weighted[1].word);
		Assert.assertEquals("critter", weighted[2].word);
	}

	@Test
	public void testEmptyArrayOfWordsReturnsSameEmptyArray() {
		Word[] words = new Word[0];
		Word[] sorted = new WordSorterAndScaler().sortAndScale(words);

		Assert.assertEquals(words, sorted);
	}
}
