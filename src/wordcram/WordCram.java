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
import processing.core.PGraphics;
import wordcram.text.*;

/**
 * The WordCram class is the main API for WordCram.  There are three steps to making a WordCram:
 * <ol>
 * <li>weight your words
 * <li>style your words
 * <li>draw your WordCram
 * </ol>
 * You start with a <code>new WordCram(this)</code>, and then...
 * 
 * <h2>Step One: Weight Your Words</h2>
 * 
 * You start by giving WordCram either some text to chew on,
 * or an array of Words you've weighted yourself.
 * 
 * <h3>Let WordCram Weight Your Words</h3>
 * <p>
 * WordCram can weight your words by the number of times they appear in a text document.  
 * It can load text from a few different sources:
 * <ul>
 * <li>{@link #fromWebPage(String)} will load a URL (or an HTML file from the filesystem), and scrape the text from the HTML</li>
 * <li>{@link #fromTextFile(String)} will load a file (from the filesystem or the network), and treat it as plaintext</li>
 * <li>{@link #fromHtmlString(String)} takes a String, assumes it's HTML, and scrapes out its text</li>
 * <li>{@link #fromTextString(String)} takes a String, and assumes it's plaintext</li>
 * <li>If you need some other way to load your text, 
 * 	    pass your own TextSource to {@link #fromText(TextSource)}, 
 * 	    and WordCram will get its text via {@link TextSource#getText()}.</li>
 * </ul>
 * 
 * Once the text is loaded, you can control how WordCram counts up the words.
 * <p>
 * <b>Case sensitivity:</b>  If your text contains "hello", "HELLO", and "Hello",
 * <ul><li>{@link #lowerCase()} will count them all as "hello"</li>
 * 	   <li>{@link #upperCase()} will count them all as "HELLO"</li>
 *     <li>{@link #keepCase()}, the default, will count them separately, as three different words</li></ul>
 * <p>
 * <b>Numbers:</b>
 * If your text contains words like "42" or "3.14159", 
 * you can remove them with {@link #excludeNumbers()} (the default),
 * or include them with {@link #includeNumbers()}.
 * <p>
 * <b>Stop words:</b> <a href="../constant-values.html#wordcram.text.StopWords.ENGLISH">Common English words</a>
 * are removed from the text by default, but you can use your own list of stop words
 * with {@link #withStopWords(String)}.
 * 
 * 
 * <h3>Weight Your Own Words</h3>
 * If you have some other way to weight your words, you can pass them to {@link #fromWords(Word[])}.
 * 
 * 
 * 
 * <h2>Step Two: Style Your Words</h2>
 * 
 * There are six questions you have to answer when drawing a word on the WordCram:
 * 
 * <h3>How big should it be?</h3>
 * A word can be 
 * {@link #sizedByWeight(int, int)},
 * {@link #sizedByRank(int, int)}, or
 * {@link #withSizer(WordSizer)}
 * 
 * <h3>How should it be angled?</h3>
 * It can be 
 * {@link #angledAt(float...)},
 * {@link #angledBetween(float, float)}, or
 * {@link #withAngler(WordAngler)}
 * 
 * <h3>What font should it be in?</h3>
 * {@link #withFont(String)}
 * {@link #withFonts(String...)}
 * {@link #withFonter(WordFonter)}
 * 
 * <h3>How should it be colored?</h3>
 * {@link #withColors(int...)}
 * {@link #withColorer(WordColorer)}
 * 
 * <h3>Where on the image should it go?</h3>
 * {@link #withPlacer(WordPlacer)}
 * 
 * <h3>If it doesn't fit at first, how should I nudge it?</h3>
 * {@link #withNudger(WordNudger)} 
 * 
 * <h2>Step Three: Draw Your WordCram</h2>
 * 
 * <p>After all that, actually rendering the WordCram is simple.
 * 
 * You can repeatedly call {@link #drawNext()} while the WordCram {@link #hasMore()} words
 * to draw (probably once per Processing frame):
 * <pre>
 * void draw() {
 *     if (wordCram.hasMore()) {
 *         wordCram.drawNext();
 *     }
 * }
 * </pre>
 * Or you can call {@link #drawAll()} once, and let it loop for you:
 * <pre>
 * void draw() {
 *     wordCram.drawAll();
 * }
 * </pre>
 * 
 * <p>
 * If you're having trouble getting your words to show up, you might
 * want to {@link #getSkippedWords()}.  Knowing which words were
 * skipped, and why, can help you size and place your words better.
 *  
 * @author Dan Bernier
 */
public class WordCram {
	
	/*
	 * This class is really only two parts: the fluent builder API, and pass-through calls
	 * to the WordCramEngine, where all the work happens.  This separation keeps the classes 
	 * focused on only one thing, but still gives the user a pretty nice API.
	 */
	
	public static final String SKIPPED_BECAUSE = "skippedBecause";
	public static final int TOO_MANY_WORDS = 301;  // TODO 0.4: find a better "name" for that
	public static final int TOO_SMALL = 302;
	public static final int NO_ROOM = 303;

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
	
	private PGraphics destination = null;
	private RenderOptions renderOptions = new RenderOptions();
	
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
	 * These words won't show up in the image.  {@link StopWords} provides some common sets
	 * of stop-words.
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
	 * @see StopWords
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
	 * @param minSize the size to draw a Word of weight 0
	 * @param maxSize the size to draw a Word of weight 1
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
	 * Make the WordCram rotate each word at one of the given angles. 
	 * @param anglesInRadians The list of possible rotation angles, in radians
	 * @return The WordCram, for further setup or drawing.
	 */
	public WordCram angledAt(float... anglesInRadians) {
		return withAngler(Anglers.pickFrom(anglesInRadians));
	}
	
	/**
	 * Make the WordCram rotate words randomly, between the min and max angles. 
	 * @param minAngleInRadians The minimum rotation angle, in radians
	 * @param maxAngleInRadians The maximum rotation angle, in radians
	 * @return The WordCram, for further setup or drawing.
	 */
	public WordCram angledBetween(float minAngleInRadians, float maxAngleInRadians) {
		return withAngler(Anglers.randomBetween(minAngleInRadians, maxAngleInRadians));
	}

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
	
	/**
	 * How many attempts should be used to place a word.
	 * Higher values ensure that more words get placed, but will make algorithm slower.
	 * @author FEZ (Felix Kratzer)
	 * @param maxAttempts
	 * @return The WordCram, for further setup or drawing.
	 */
	public WordCram withMaxAttemptsForPlacement(int maxAttempts) {
		renderOptions.maxAttemptsForPlacement = maxAttempts;
		return this;
	}
	
	/**
	 * The maximum number of Words WordCram should try to draw.
	 * This might be useful if you have a whole bunch of words, and need
	 * an artificial way to cut down the list (for speed).
	 * By default, it's unlimited.
	 * @param maxWords can be any value from 0 to Integer.MAX_VALUE. Values < 0 are treated as unlimited.
	 * @return The WordCram, for further setup or drawing.
	 */
	public WordCram maxNumberOfWordsToDraw(int maxWords) {
		renderOptions.maxNumberOfWordsToDraw = maxWords;
		return this;
	}
	
	/**
	 * The smallest sized Shape the WordCram should try to draw.
	 * By default, it's 7.
	 * @param maxWords can be any value from 0 to Integer.MAX_VALUE. Values < 0 are treated as unlimited.
	 * @return The WordCram, for further setup or drawing.
	 */
	public WordCram minShapeSize(int minShapeSize) {
		renderOptions.minShapeSize = minShapeSize;
		return this;
	}
	
	/**
	 * Use a custom canvas instead of the applet's default one.
	 * This may be needed if rendering in background or in other dimensions than the
	 * applet size is needed.
	 * @author FEZ (Felix Kratzer)
	 * @param canvas
	 * @return The WordCram, for further setup or drawing.
	 */
	public WordCram withCustomCanvas(PGraphics canvas) {
		this.destination = canvas;
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
			
			PGraphics canvas = destination == null? parent.g : destination; 
			wordCramEngine = new WordCramEngine(canvas, words, fonter, sizer, colorer, angler, placer, nudger, new WordShaper(), new BBTreeBuilder(), renderOptions);
		}
		
		return wordCramEngine;
	}
	

	/**
	 * If you're drawing the words one-at-a-time using {@link #drawNext()},
	 * this will tell you whether the WordCram has any words left to draw.
	 * @return true if the WordCram has any words left to draw; false otherwise.
	 * @see #drawNext()
	 */
	public boolean hasMore() {
		return getWordCramEngine().hasMore();
	}

	/**
	 * If the WordCram has any more words to draw, draw the next one.
	 * @see #hasMore()
	 * @see #drawAll()
	 */
	public void drawNext() {
		getWordCramEngine().drawNext();
	}
	
	/**
	 * Just like it sounds: draw all the words.  Once the WordCram has everything set,
	 * call this and wait just a bit.
	 * @see #drawNext()
	 */
	public void drawAll() {
		getWordCramEngine().drawAll();
	}
	
	/** 
	 * Get the Words that WordCram is drawing. This can be useful if
	 * you want to inspect exactly how the words were weighted, or see
	 * how they were colored, fonted, sized, angled, or placed, or why
	 * they were skipped.
	 */
	public Word[] getWords() {
		Word[] wordsCopy = new Word[words.length];
		System.arraycopy(words, 0, wordsCopy, 0, words.length);
		return wordsCopy;
	}
	
	/**
	 * Get the Word at the given (x,y) coordinates.
	 * <p>
	 * This can be called while the WordCram is rendering, or after it's done.
	 * If a Word is too small to render, or hasn't been placed yet, it will 
	 * never be returned by this method.
	 * 
	 * @param x the X coordinate
	 * @param y the Y coordinate
	 * @return the Word that covers those coordinates, or null if there isn't one 
	 */
	public Word getWordAt(float x, float y) {
		return getWordCramEngine().getWordAt(x, y);
	}
	
	/**
	 * Returns an array of words that could not be placed.
	 * @author FEZ (Felix Kratzer)
	 * @return An array of words
	 */
	public Word[] getSkippedWords() {
		return getWordCramEngine().getSkippedWords();
	}
	
	/**
	 * How far through the words are we? Useful for when drawing to a custom PGraphics.
	 * @return The current point of progress through the list, as a float between 0 and 1. 
	 */
	public float getProgress() {
		return getWordCramEngine().getProgress();
	}
}
