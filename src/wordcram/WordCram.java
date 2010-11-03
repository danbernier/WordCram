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

import processing.core.PApplet;
import processing.core.PFont;
import wordcram.text.*;

/**
 * WordCram is the main API for WordCram.  There are two phases to using a WordCram: 
 * constructing, and drawing.
 * 
 * <h2>Constructing a WordCram</h2>
 * 
 * <p>Constructing a WordCram is done with the fluent API -- first, construct a WordCram,
 * then call configuring methods on it.  Like this:
 * <pre>
 * WordCram wc = new WordCram(this)                            // Construct the WordCram...
 *   .fromWords(new WebPage("http://wordcram.wordpress.com"))  // Have it scrape the text from the WordCram blog...
 *   .withFonts("serif")                                       // and draw the words in a serif font... 
 *   .withColors(color(255,0,0), color(0,0,255));              // colored red and blue.
 * </pre>
 * 
 * <h2>Choose Your Words</h2>
 * 
 * <h3>Loading Your Text</h3>
 * 
 * You start by giving WordCram either some text to chew on,
 * or an array of Words you've weighted yourself.
 * <p>
 * WordCram can load text from a few different text sources:
 * <ul>
 * <li>{@link #fromWebPage(String)} will load a URL (or an HTML file from the filesystem), and scrape the text from the HTML</li>
 * <li>{@link #fromTextFile(String)} will load a file (from the filesystem or the network), and treat it as plaintext</li>
 * <li>{@link #fromHtmlString(String)} takes a String, assumes it's HTML, and scrapes out its text</li>
 * <li>{@link #fromTextString(String)} takes a String, and assumes it's plaintext</li>
 * <li>If you want WordCram to count words from some other source, you can pass your own {@link TextSource} 
 * 		to {@link #fromText(TextSource)}, and WordCram will get its text via {@link TextSource#getText()}</li>
 * </ul>
 * 
 * <h3>Counting Your Words</h3>
 * 
 * Once the text is loaded, you can control how WordCram counts up the words, too.
 * 
 * <h4>Case sensitivity</h4>
 * By default, WordCram will treat "HELLO", "hello", and "Hello" as three different words, but:
 * <ul>
 * <li>{@link #lowerCase()} will count them all as "hello",</li>
 * <li>{@link #upperCase()} will count them all as "HELLO", and</li>
 * <li>{@link #keepCase()} will count them separately, as they appear in the source text.</li>
 * </ul>
 * 
 * <h4>Custom stop words</h4>
 * <a href="../constant-values.html#wordcram.text.StopWords.ENGLISH">Common English words</a>
 * are removed from the text by default, but you can use your own list of stop words
 * with {@link #withStopWords(String)}.  Stop words are just a 
 * space-separated String, so if you just want to add a few, you can use 
 * <code>withStopWords(StopWords.ENGLISH + " your stop words")</code>. 
 * 
 * <h4>Numbers</h4>
 * By default, it excludes words that look
 * like numbers, but you can include them with {@link #includeNumbers()}, or go back to weeding them out with
 * {@link #excludeNumbers()}.
 * 
 * 
 * <h3>Your Own Weights</h3>
 * If you already have your words weighted, you can pass an array of words to {@link #fromWords(Word[])}.
 * 
 * 
 * 
 * <h2>Style Your Words</h2>
 * {TODO finish this}
 * 
 * 
 * <h2>Draw Your Words</h2>
 * {TODO finish this}
 * 
 * <p>After all that, actually rendering the WordCram is simple.  There are two ways:
 * <ul>
 * 	 <li>repeatedly call {@link #drawNext()} while the WordCram {@link #hasMore()} words
 *       to draw (probably once per Processing frame):
 *       <pre>
 *       void draw() {
 *           if (wordCram.hasMore()) {
 *               wordCram.drawNext();
 *           }
 *       }
 *       </pre>
 *   </li>
 *   <li>call {@link #drawAll()} once, and let it loop for you:
 *       <pre>
 *       void draw() {
 *           wordCram.drawAll();
 *       }
 *       </pre>
 *   </li>
 * </ul>
 * 
 * @author Dan Bernier
 */
public class WordCram {
	
	/*
	 * This class is really only two parts: the fluent builder API, and pass-through calls
	 * to the WordCramEngine, where all the work happens.  This separation keeps the classes 
	 * focused on only one thing, but still gives the user a pretty nice API.
	 */

	private Word[] words;
	private TextSource textSource;
	private String stopWords = StopWords.ENGLISH;
	private boolean excludeNumbers = true;
	private enum TextCase { Lower, Upper, Keep };
	private TextCase textCase = TextCase.Keep;
	
	private WordCramEngine wordCramEngine;
	
	private PApplet parent;

	private WordFonter fonter;
	private WordSizer sizer;
	private WordColorer colorer;
	private WordAngler angler;
	private WordPlacer placer;
	private WordNudger nudger;
	
	/**
	 * This was the old way to build a WordCram: you have to specify <i>everything</i>.
	 * The new way, {@link #WordCram(PApplet)}, is much easier, but this will be left
	 * around for a while. 
	 * 
	 * @param _parent Your Processing sketch. You'll probably pass it as <code>this</code>.
	 * @param _words The array of words to put into the word cloud.
	 * @param _fonter says which font to use for each word.
	 * @param _sizer says which size to draw each word at.
	 * @param _colorer says which color to draw each word in.
	 * @param _angler says how to rotate each word.
	 * @param _wordPlacer says (approximately) where to place each word.
	 * @param _wordNudger says how to nudge a word, when it doesn't initially fit.
	 * @deprecated Since WordCram 0.3. Use {@link #WordCram(PApplet)} and the fluent builder methods instead.
	 */
	public WordCram(PApplet _parent, Word[] _words, WordFonter _fonter, WordSizer _sizer, WordColorer _colorer, WordAngler _angler, WordPlacer _wordPlacer, WordNudger _wordNudger) {
		this(_parent);
		withFonter(_fonter).withSizer(_sizer).withColorer(_colorer).withAngler(_angler).withPlacer(_wordPlacer).withNudger(_wordNudger);
		fromWords(_words);
	}

	/**
	 * This was the old way to build a WordCram: you have to specify <i>everything</i> 
	 * (except the WordNudger, which defaults to a {@link SpiralWordNudger}). 
	 * The new way, {@link #WordCram(PApplet)}, is much easier, but this will be left
	 * around for a while. 
	 * 
	 * @param _parent Your Processing sketch. You'll probably pass it as <code>this</code>.
	 * @param _words The array of words to put into the word cloud.
	 * @param _fonter says which font to use for each word.
	 * @param _sizer says which size to draw each word at.
	 * @param _colorer says which color to draw each word in.
	 * @param _angler says how to rotate each word.
	 * @param _wordPlacer says (approximately) where to place each word.
	 * @deprecated Since WordCram 0.3. Use {@link #WordCram(PApplet)} and the fluent builder methods instead.
	 */
	public WordCram(PApplet _parent, Word[] _words, WordFonter _fonter, WordSizer _sizer, WordColorer _colorer, WordAngler _angler, WordPlacer _wordPlacer) {
		this(_parent, _words, _fonter, _sizer, _colorer, _angler, _wordPlacer, new SpiralWordNudger());
	}
	
	/**
	 * Make a new WordCram.
	 * <p>
	 * When constructed this way, it's the starting point of the fluent API for building WordCrams.
	 * 
	 * @param parent Your Processing sketch. You'll probably pass it as <code>this</code>.
	 */
	public WordCram(PApplet parent) {
		this.parent = parent;
	}
	
	/**
	 * Tells WordCram which words to ignore when it counts up the words in your text.
	 * These words won't show up in the image.
	 * <p>
	 * Stop-words are always case-insensitive: if your source text contains "The plane, 
	 * the plane!", using "the" for a stop-word is enough to block both "the" and "The".
	 * <p>
	 * It doesn't matter whether this is called before or after the "for{text}" methods.
	 * <p>
	 * <b><i>Note:</i></b> Stop-words have no effect if you're passing in your own custom
	 * {@link Word} array, since WordCram won't do any text analysis on it (other than 
	 * sorting the words and scaling their weights). 
	 * 
	 * @param stopWords a space-delimited String of words to ignore when counting the words in your text.
	 * @return The WordCram, for further setup or drawing.
	 */
	public WordCram withStopWords(String stopWords) {
		this.stopWords = stopWords;
		return this;
	}
	
	/**
	 * Exclude numbers from the text in the WordCram.  They're excluded by default.
	 * <p>
	 * Words that are all numbers, like 1, 3.14159, 42, or 1492, will be excluded.
	 * Words that have some letters and some numbers like 1A, U2, or funnyguy194 will be included.
	 *
	 * @see #includeNumbers()
	 * @return The WordCram, for further setup or drawing.
	 */
	public WordCram excludeNumbers() {
		this.excludeNumbers = true;
		return this;
	}
	
	/**
	 * Include numbers from the text in the WordCram.  They're excluded by default.
	 *
	 * @see #excludeNumbers()
	 * @return The WordCram, for further setup or drawing.
	 */
	public WordCram includeNumbers() {
		this.excludeNumbers = false;
		return this;
	}
	
	/**
	 * Make the WordCram change all words to lower-case.  
	 * Stop-words are unaffected; they're always case-insensitive.
	 * The default is to keep words as they appear in the text.
	 * 
	 * @return The WordCram, for further setup or drawing.
	 */
	public WordCram lowerCase() {
		this.textCase = TextCase.Lower;
		return this;
	}
	
	/**
	 * Make the WordCram change all words to upper-case.  
	 * Stop-words are unaffected; they're always case-insensitive.
	 * The default is to keep words as they appear in the text.
	 * 
	 * @return The WordCram, for further setup or drawing.
	 */
	public WordCram upperCase() {
		this.textCase = TextCase.Upper;
		return this;
	}
	
	/**
	 * Make the WordCram leave all words cased as they appear in the text.  
	 * Stop-words are unaffected; they're always case-insensitive.
	 * This is the default.
	 * 
	 * @return The WordCram, for further setup or drawing.
	 */
	public WordCram keepCase() {
		this.textCase = TextCase.Keep;
		return this;
	}
	
	/**
	 * Make a WordCram from the text on a web page.
	 * Just before the WordCram is drawn, it'll load the web page's HTML, scrape out the text, 
	 * and count and sort the words.
	 * 
	 * @param webPageAddress the URL of the web page to load 
	 * @return The WordCram, for further setup or drawing.
	 */
	public WordCram fromWebPage(String webPageAddress) {
		return fromText(new WebPage(webPageAddress, parent));
	}
	
	// TODO from an inputstream!  or reader, anyway
	
	/**
	 * Makes a WordCram from a String of HTML.
	 * Just before the WordCram is drawn, it'll scrape out the text from the HTML, and count and sort the words.
	 * 
	 * @param html the String of HTML
	 * @return The WordCram, for further setup or drawing. 
	 */
	public WordCram fromHtmlString(String html) {
		return fromText(new Html(html));
	}
	
	/**
	 * Makes a WordCram from a text file, either on the filesystem or the network.
	 * Just before the WordCram is drawn, it'll load the file, and count and sort its words.
	 * 
	 * @param textFilePathOrUrl the path of the text file
	 * @return The WordCram, for further setup or drawing. 
	 */
	public WordCram fromTextFile(String textFilePathOrUrl) {
		return fromText(new TextFile(textFilePathOrUrl, parent));
	}
	
	/**
	 * Makes a WordCram from a String of text.
	 * 
	 * @param text the String of text to get the words from
	 * @return The WordCram, for further setup or drawing. 
	 */
	public WordCram fromTextString(String text) {
		return fromText(new Text(text));
	}
	
	/**
	 * Makes a WordCram from any TextSource.
	 * <p>
	 * It only caches the TextSource -- it won't load the text from it until {@link #drawAll()}
	 * or {@link #drawNext()} is called.
	 * 
	 * @param textSource the TextSource to get the text from.
	 * @return The WordCram, for further setup or drawing.
	 */
	public WordCram fromText(TextSource textSource) {
		this.textSource = textSource;
		return this;
	}
	
	/**
	 * Makes a WordCram from your own custom Word array.
	 * The Words can be ordered and weighted arbitrarily -- WordCram will
	 * sort them by weight, and then divide their weights by the weight of the
	 * heaviest Word, so the heaviest Word will end up with a weight of 1.0.
	 * <p>
	 * Note: WordCram will do no text analysis on the words; stop-words will
	 * have no effect, etc. These words are supposed to be ready to go.
	 * 
	 * @return The WordCram, for further setup or drawing. 
	 */
	public WordCram fromWords(Word[] words) {
		this.words = words;
		return this;
	}
	
	//----------------------------------------------
	
	/**
	 * This WordCram will get a 
	 * <a href="http://processing.org/reference/PFont.html" target="blank">PFont</a>
	 * for each fontName, via
	 * <a href="http://processing.org/reference/createFont_.html" target="blank">createFont</a>,
	 * and will render words in one of those PFonts.
	 * 
	 * @return The WordCram, for further setup or drawing.
	 */
	public WordCram withFonts(String... fontNames) {
		PFont[] fonts = new PFont[fontNames.length];
		for (int i = 0; i < fontNames.length; i++) {
			fonts[i] = parent.createFont(fontNames[i], 1);
		}
		
		return withFonts(fonts);
	}
	
	/**
	 * Make the WordCram render all words in the font that matches
	 * the given name, via Processing's 
	 * <a href="http://processing.org/reference/createFont_.html" target="blank">createFont</a>.
	 * 
	 * @param fontName the font name to pass to createFont.
	 * @return The WordCram, for further setup or drawing.
	 */
	public WordCram withFont(String fontName) {
		PFont font = parent.createFont(fontName, 1);
		return withFont(font);
	}
	
	/**
	 * This WordCram will render words in one of the given 
	 * <a href="http://processing.org/reference/PFont.html" target="blank">PFonts</a>.
	 * 
	 * @return The WordCram, for further setup or drawing.
	 */
	public WordCram withFonts(PFont... fonts) {
		return withFonter(Fonters.pickFrom(fonts));
	}
	
	/**
	 * Make the WordCram render all words in the given
	 * <a href="http://processing.org/reference/PFont.html" target="blank">PFont</a>.
	 * 
	 * @param font the PFont to render the words in.
	 * @return The WordCram, for further setup or drawing.
	 */
	public WordCram withFont(PFont font) {
		return withFonter(Fonters.pickFrom(font));
	}
	
	/**
	 * Use the given WordFonter to pick fonts for each word.
	 * You'll probably only use this if you're making a custom WordFonter.
	 * 
	 * @param fonter the WordFonter to use.
	 * @return The WordCram, for further setup or drawing.
	 */
	public WordCram withFonter(WordFonter fonter) {
		this.fonter = fonter;
		return this;
	}
	
	/**
	 * Make the WordCram size words by their weight, where the "heaviest"
	 * word will be sized at <code>maxSize</code>.
	 * <p>
	 * Specifically, it makes the WordCram use {@link Sizers#byWeight(int, int)}.
	 * 
	 * @param minSize the size to draw a Word with weight = 0
	 * @param maxSize the size to draw a Word with weight = 1
	 * @return The WordCram, for further setup or drawing.
	 */
	public WordCram sizedByWeight(int minSize, int maxSize) {
		return withSizer(Sizers.byWeight(minSize, maxSize));
	}
	
	/**
	 * Make the WordCram size words by their rank.  The first
	 * word will be sized at <code>maxSize</code>.
	 * <p>
	 * Specifically, it makes the WordCram use {@link Sizers#byRank(int, int)}.
	 * 
	 * @param minSize the size to draw the last Word
	 * @param maxSize the size to draw the first Word
	 * @return The WordCram, for further setup or drawing.
	 */
	public WordCram sizedByRank(int minSize, int maxSize) {
		return withSizer(Sizers.byRank(minSize, maxSize));
	}

	/**
	 * Use the given WordSizer to pick fonts for each word.
	 * You'll probably only use this if you're making a custom WordSizer.
	 * 
	 * @param sizer the WordSizer to use.
	 * @return The WordCram, for further setup or drawing.
	 */
	public WordCram withSizer(WordSizer sizer) {
		this.sizer = sizer;
		return this;
	}
	
	/**
	 * Render words by randomly choosing from the given
	 * colors.  Uses {@link Colorers#pickFrom(int...)}.
	 * <p>
	 * Note: if you want all your words to be, say, red, <i>don't</i> do this:
	 * <pre>
	 * ...withColors(255, 0, 0)...  // No no!
	 * </pre>
	 * You'll just see a blank WordCram.  Since 
	 * <a href="http://processing.org/reference/color_datatype.html" target="blank">Processing 
	 * stores colors as integers</a>, WordCram will see each integer as a different
	 * color, and it'll color about 1/3 of your words with the color represented by 
	 * the integer 255, and the other 2/3 with the color represented by the integer
	 * 0.  The punchline is, Processing stores opacity (or alpha) in the highest 
	 * bits (the ones used for storing really big numbers, from 2<sup>24</sup> to 
	 * 2<sup>32</sup>), so your colors 0 and 255 have, effectively, 0 opacity -- they're
	 * completely transparent.  Oops.
	 * <p>
	 * Use this instead, and you'll get what you're after:
	 * <pre>
	 * ...withColors(color(255, 0, 0))...  // Much better!
	 * </pre>
	 * 
	 * @param colors the colors to randomly choose from.
	 * @return The WordCram, for further setup or drawing.
	 */
	public WordCram withColors(int... colors) {
		return withColorer(Colorers.pickFrom(colors));
	}

	/**
	 * Use the given WordColorer to pick colors for each word.
	 * 
	 * @param colorer the WordColorer to use.
	 * @return The WordCram, for further setup or drawing.
	 */
	public WordCram withColorer(WordColorer colorer) {
		this.colorer = colorer;
		return this;
	}

	// TODO need more overloads!

	/**
	 * Use the given WordAngler to pick angles for each word.
	 * 
	 * @param angler the WordAngler to use.
	 * @return The WordCram, for further setup or drawing.
	 */
	public WordCram withAngler(WordAngler angler) {
		this.angler = angler;
		return this;
	}

	/**
	 * Use the given WordPlacer to pick locations for each word.
	 * 
	 * @param placer the WordPlacer to use.
	 * @return The WordCram, for further setup or drawing.
	 */
	public WordCram withPlacer(WordPlacer placer) {
		this.placer = placer;
		return this;
	}

	/**
	 * Use the given WordNudger to pick angles for each word.
	 * 
	 * @param nudger the WordNudger to use.
	 * @return The WordCram, for further setup or drawing.
	 */
	public WordCram withNudger(WordNudger nudger) {
		this.nudger = nudger;
		return this;
	}
	
	
	private WordCramEngine getWordCramEngine() {
		if (wordCramEngine == null) {

			if (words == null && textSource != null) {
				String text = textSource.getText();
				
				text = textCase == TextCase.Lower ? text.toLowerCase()
				     : textCase == TextCase.Upper ? text.toUpperCase()
				     : text;
				
				String[] wordStrings = new WordScanner().scanIntoWords(text);
				words = new WordCounter(stopWords).shouldExcludeNumbers(excludeNumbers).count(wordStrings);
			}			
			words = new WordSorterAndScaler().sortAndScale(words);
			

			if (fonter == null) fonter = Fonters.alwaysUse(parent.createFont("sans", 1));
			if (sizer == null) sizer = Sizers.byWeight(5, 70);
			if (colorer == null) colorer = Colorers.twoHuesRandomSats(parent);
			if (angler == null) angler = Anglers.mostlyHoriz();
			if (placer == null) placer = Placers.horizLine();
			if (nudger == null) nudger = new SpiralWordNudger();
						
			wordCramEngine = new WordCramEngine(parent, words, fonter, sizer, colorer, angler, placer, nudger);
		}
		
		return wordCramEngine;
	}
	

	// TODO javadoc these 3
	public boolean hasMore() {
		return getWordCramEngine().hasMore();
	}

	public void drawNext() {
		getWordCramEngine().drawNext();
	}
	
	public void drawAll() {
		getWordCramEngine().drawAll();
	}
	
	/* methods JUST for off-screen drawing. */
	/* Replace these w/ a callback functor to drawNext()? */
	
	/**
	 * This method, and {@link #currentWordIndex()}, are probably just a bad
	 * idea waiting to be removed.  They're only here in case you want to display
	 * info about how the WordCram is progressing.  I wouldn't count on them
	 * being around for long -- if you really need them, please let me know.
	 * 
	 * @deprecated Will be removed in the 0.4 release, at the latest. 
	 */
	public Word currentWord() {
		return getWordCramEngine().currentWord();
	}
	
	/**
	 * This method, and {@link #currentWord()}, are probably just a bad
	 * idea waiting to be removed.  They're only here in case you want to display
	 * info about how the WordCram is progressing.  I wouldn't count on them
	 * being around for long -- if you really need them, please let me know.
	 * 
	 * @deprecated Will be removed in the 0.4 release, at the latest. 
	 */
	public int currentWordIndex() {
		return getWordCramEngine().currentWordIndex();
	}
	/* END OF methods JUST for off-screen drawing. */
}
