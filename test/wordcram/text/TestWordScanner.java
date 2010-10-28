package wordcram.text;

import org.junit.*;

public class TestWordScanner {

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

	private void testSplit(String expected, String src) {
		String[] expectedArray = expected.split(" ");
		String[] actual = scanner.scanIntoWords(src);

		for (int i = 0; i < expectedArray.length; i++) {
			Assert.assertEquals(expectedArray[i], actual[i]);
		}
		Assert.assertEquals(expectedArray.length, actual.length);
	}
}
