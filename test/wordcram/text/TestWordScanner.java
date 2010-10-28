package wordcram.text;

import org.junit.*;

public class TestWordScanner {

	private WordScanner scanner;

	@Before
	public void setup() {
		scanner = new WordScanner();
	}

	@Test
	public void testSplitLeftoverCases() {
		testSplit("a b c", "a b c", "basic test");
		testSplit("a b c", "A B C", "downcases");
		testSplit("a b c a", "A b C a",
				"preserves duplicate words (dumb test, I know)");
	}

	@Test
	public void removesWhiteSpace() {
		testSplit("a b c d", "a  b  c    d", "multiple spaces");
		testSplit("a b c", "   a b c    ", "leading and trailing spaces");
		testSplit("a b c", "a\tb \t c", "tabs");
		testSplit("a b c", "a\nb \n c", "new lines");
		testSplit("a b c", "\n\ta\n\tb\t\nc\n\t",
				"mixed, and leading, newlines & tabs");
	}

	@Test
	public void removesPunctuationOnTheEndsOfWords() {
		testSplit("a b c", "@a' #b? -c/", "removes punctuation");
		testSplit("i know i said", "\"I know,\" I said.",
				"removes punctuation from beginnigs & ends of words");
	}

	@Test
	public void removesMultiplePunctuationOnTheEndsOfWords() {
		testSplit("a b c", "@?a~' #@!&&$^b?@@ -@#$#@%c$#@//",
				"removes punctuation");
		testSplit("hey you there", "Hey--you there!!",
				"removes punctuation from beginnigs & ends of words");
	}

	@Test
	public void leavesPunctuationInsideWords() {
		testSplit("i'll say you're silly", "I'll say you're silly!",
				"leaves punctuation inside words");
	}

	@Test
	public void removesDashesBetweenWords() {
		testSplit("a b c", "a--b--c", "turns -- into a space");
	}

	@Test
	@Ignore("This could be an option later")
	public void removesNumbers() {
		testSplit("a b c", "a 0 b c 1 1948", "removes numbers");
	}

	private void testSplit(String expected, String src, String msg) {
		String[] expectedArray = expected.split(" ");
		String[] actual = scanner.scanIntoWords(src);
		
		for (int i = 0; i < expectedArray.length; i++) {
			Assert.assertEquals(msg, expectedArray[i], actual[i]);
		}
		Assert.assertEquals(msg, expectedArray.length, actual.length);
	}
}
