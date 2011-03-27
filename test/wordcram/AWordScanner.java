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

import org.junit.*;

public class AWordScanner {

	private WordScanner scanner;

	@Before
	public void setup() {
		scanner = new WordScanner();
	}

	@Test
	public void smokeTest() {
		testSplit("a b c", "a b c");
	}

	@Test
	public void preservesDuplicateWords() {
		testSplit("a b c a", "a b c a");
	}

	@Test
	public void preservesCase() {
		testSplit("A B C a b c", "A B C a b c");
	}

	@Test
	public void removesMultipleSpaces() {
		testSplit("a b c d", "a  b  c    d");
	}
	
	@Test
	public void removesLeadingAndTrailingSpaces() {
		testSplit("a b c", "   a b c    ");
	}
	
	@Test
	public void removesTabs() {
		testSplit("a b c", "a\tb \t c");
	}
	
	@Test
	public void removesNewLines() {
		testSplit("a b c", "a\nb \n c");
	}
	
	@Test
	public void removesMixOfLeadingAndTrailingNewLinesAndTabs() {
		testSplit("a b c", "\n\ta\n\tb\t\nc\n\t");
	}

	@Test
	public void removesPunctuationFromTheEndsOfWords() {
		testSplit("a b c", "@a' #b? -c/");
		testSplit("I know I said", "\"I know,\" I said.");
	}

	@Test
	public void removesMultiplePunctuationFromTheEndsOfWords() {
		testSplit("a b c", "@?a~' #@!&&$^b?@@ -@#$#@%c$#@//");
	}
	
	@Test
	public void removesPunctuationFromTheBeginningsAndEndsOfWords() {
		testSplit("Hey you there", "Hey--you there!!");
	}

	@Test
	public void leavesPunctuationInsideWords() {
		testSplit("I'll say you're silly", "I'll say you're silly!");
	}

	@Test
	public void removesDashesBetweenWords() {
		testSplit("a b c", "a--b--c");
	}
	
	@Test
	public void handlesABunchOfUnicodeCharacters() {
		testSplit("Ā ā Ă ă Ć ć Ĉ ĉ Ċ ċ Č č Ď ď Đ đ Ē ē Ĕ ĕ Ė ė Ę ę Ě ě", 
				  "Ā ā Ă ă Ć ć Ĉ ĉ Ċ ċ Č č Ď ď Đ đ Ē ē Ĕ ĕ Ė ė Ę ę Ě ě");
	}

	@Test
	public void handlesUnicodesInWords() {
		testSplit("l'évolution des espèces vivantes ont révolutionné la biologie", 
				  "l'évolution des espèces vivantes ont révolutionné la biologie");
		testSplit("Großcousin Käfern Frühjahr standesgemäßen Vögeln",
				  "Großcousin  *Käfern  Frühjahr,  'standesgemäßen'!  Vögeln.");
		testSplit("a partir d'avantpassats comuns mitjançant la selecció natural",
				  "a partir d'avantpassats comuns mitjançant la selecció natural.");
	}

	private void testSplit(String expected, String src) {
		String[] expectedArray = expected.split(" ");
		String[] actual = scanner.scanIntoWords(src);

		for (int i = 0; i < expectedArray.length; i++) {
			Assert.assertEquals(expectedArray[i], actual[i]);
		}
		Assert.assertEquals(expectedArray.length, actual.length);
	}
}
