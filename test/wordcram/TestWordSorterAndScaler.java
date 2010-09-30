package wordcram;

import junit.framework.Assert;

import org.junit.Test;

import wordcram.Word;

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

public class TestWordSorterAndScaler {
	
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
				new Word("critter", 0.000001),
				new Word("crayter", 0.000002),
				new Word("croyter", 0.000003)
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
}
