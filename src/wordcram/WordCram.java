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
 * <p>Constructing a WordCram is done with the fluent API -- first, construct a WordCram,
 * then call configuring methods on it.  Like this:
 * <pre>
 * WordCram wc = new WordCram(this)                           // Construct the WordCram...
 *   .forWords(new WebPage("http://wordcram.wordpress.com"))  // set its words... 
 *   .withFonts("serif")                                      // set its font...
 *   .withColors(color(255,0,0), color(0,0,255));             // set its colors.
 * </pre>
 * 
 * <p>Creating a WordCram comes down to two parts:
 * <ul>
 * 	 <li>Give it your text or word list.  All these methods start with "for...": 
 * 		 {@link #fromWebPage(String)}, {@link #fromTextFile(String)}, {@link #fromWords(Word[])}, etc.
 *   </li>
 *   <li>Tell it how to display your words.  All these methods start with "with...": 
 *       {@link #withFonts(PFont...)}, {@link #withColors(int...)}, etc.
 *   </li>
 * </ul>
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
	
	private WordCramEngine wordCramEngine;
	
	private PApplet parent;

	private WordFonter fonter;
	private WordSizer sizer;
	private WordColorer colorer;
	private WordAngler angler;
	private WordPlacer placer;
	private WordNudger nudger;
	
	private Word[] words;
	
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
	 * @deprecated Since WordCram 0.3. Use {@link #WordCram(PApplet)} and the builder fluent API instead.
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
	 * @deprecated Since WordCram 0.3. Use {@link #WordCram(PApplet)} and the builder fluent API instead.
	 */
	public WordCram(PApplet _parent, Word[] _words, WordFonter _fonter, WordSizer _sizer, WordColorer _colorer, WordAngler _angler, WordPlacer _wordPlacer) {
		this(_parent, _words, _fonter, _sizer, _colorer, _angler, _wordPlacer, new SpiralWordNudger());
	}
	
	/**
	 * Make a new WordCram.
	 * <p>
	 * When constructed this way, it's the starting point of the fluent API for building WordCrams.
	 * @param parent Your Processing sketch. You'll probably pass it as <code>this</code>.
	 */
	public WordCram(PApplet parent) {
		this.parent = parent;
	}
	
	/**
	 * Make a WordCram from the text on a web page.
	 * Loads the web page's HTML, scrapes out the text, and counts and sorts the words.
	 * @param webPageAddress the URL of the web page to load 
	 * @return The WordCram, for further setup or drawing.
	 */
	public WordCram fromWebPage(String webPageAddress) {
		return fromText(new WebPage(webPageAddress, parent));
	}
	
	/**
	 * Makes a WordCram from the text in a saved .html file.
	 * Loads the file's HTML, scrapes out the text, and counts and sorts the words.
	 * @param path the path of the HTML file
	 * @return The WordCram, for further setup or drawing.
	 */
	public WordCram fromHtmlFile(String path) {
		return fromText(new HtmlFile(path, parent));
	}
	
	/**
	 * Makes a WordCram from a String of HTML.
	 * Scrapes out the text from the HTML, and counts and sorts the words.
	 * @param html the String of HTML
	 * @return The WordCram, for further setup or drawing. 
	 */
	public WordCram fromHtml(String html) {
		return fromText(new Html(html));
	}
	
	/**
	 * Makes a WordCram from a text file.  Loads the file, and counts and sorts its words. 
	 * @param path the path of the text file
	 * @return The WordCram, for further setup or drawing. 
	 */
	public WordCram fromTextFile(String path) {
		return fromText(new TextFile(path, parent));
	}
	
	/**
	 * Makes a WordCram from a String of text.
	 * @param text the String of text to get the words from
	 * @return The WordCram, for further setup or drawing. 
	 */
	public WordCram fromText(String text) {
		return fromText(new Text(text));
	}
	
	/**
	 * Makes a WordCram from any {@link TextSource}.  Call's the textSource's getText()
	 * method, so if that means network or filesystem access, it'll happen when you call this. 
	 * @param textSource the TextSource to get the text from.
	 * @return The WordCram, for further setup or drawing.
	 */
	public WordCram fromText(TextSource textSource) {
		String text = textSource.getText();
		String[] words = new WordScanner().scanIntoWords(text);
		Word[] weightedWords = new WordCounter().count(words);
		return fromWords(weightedWords);
	}
	
	/**
	 * Makes a WordCram from your own custom {@link Word} array.
	 * The Words can be ordered and weighted arbitrarily -- WordCram will
	 * sort them by weight, and then divide their weights by the weight of the
	 * heaviest Word, so the heaviest Word will end up with a weight of 1.0.
	 * @return The WordCram, for further setup or drawing. 
	 */
	public WordCram fromWords(Word[] _words) {
		// TODO move this to drawNext(), so accidentally using >1 textsource doesn't load extra stuff?
		words = new WordSorterAndScaler().sortAndScale(_words);
		return this;
	}
	
	//----------------------------------------------
	
	/**
	 * This WordCram will get a PFont for each fontName, via
	 * <a href="http://processing.org/reference/createFont_.html" target="blank">createFont</a>,
	 * and will render words in one of those PFonts. 
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
	 * @param fontName the font name to pass to createFont.
	 * @return The WordCram, for further setup or drawing.
	 */
	public WordCram withFont(String fontName) {
		PFont font = parent.createFont(fontName, 1);
		return withFont(font);
	}
	
	/**
	 * This WordCram will render words in one of the given PFonts.
	 * @return The WordCram, for further setup or drawing.
	 */
	public WordCram withFonts(PFont... fonts) {
		return withFonter(Fonters.pickFrom(fonts));
	}
	
	/**
	 * Make the WordCram render all words in the given PFont.
	 * @param font the PFont to render the words in.
	 * @return The WordCram, for further setup or drawing.
	 */
	public WordCram withFont(PFont font) {
		return withFonter(Fonters.pickFrom(font));
	}
	
	/**
	 * Use the given WordFonter to pick fonts for each word.
	 * You'll probably only use this if you're making a custom WordFonter.
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
	 * To be specific, the font size for each word will be calculated with:
	 * <pre>PApplet.lerp(minSize, maxSize, (float)word.weight)</pre>
	 * word.weight 
	 * @param minSize
	 * @param maxSize
	 * @return
	 */
	public WordCram sizedByWeight(int minSize, int maxSize) {
		return withSizer(Sizers.byWeight(minSize, maxSize));
	}
	
	public WordCram sizedByRank(int minSize, int maxSize) {
		return withSizer(Sizers.byRank(minSize, maxSize));
	}
	
	public WordCram withSizer(WordSizer sizer) {
		this.sizer = sizer;
		return this;
	}
	
	public WordCram withColors(int... colors) {
		return withColorer(Colorers.pickFrom(colors));
	}
	public WordCram withColorer(WordColorer colorer) {
		this.colorer = colorer;
		return this;
	}

	// TODO need more overloads!
	
	public WordCram withAngler(WordAngler angler) {
		this.angler = angler;
		return this;
	}
	
	public WordCram withPlacer(WordPlacer placer) {
		this.placer = placer;
		return this;
	}
	
	public WordCram withNudger(WordNudger nudger) {
		this.nudger = nudger;
		return this;
	}
	
	
	private WordCramEngine getWordCramEngine() {
		if (wordCramEngine == null) {

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
	

	
	public boolean hasMore() {
		return getWordCramEngine().hasMore();
	}
	
	public void drawAll() {
		getWordCramEngine().drawAll();
	}

	public void drawNext() {
		getWordCramEngine().drawNext();
	}
	
	/* methods JUST for off-screen drawing. */
	/* Replace these w/ a callback functor to drawNext()? */
	
	/**
	 * This method, and {@link #currentWordIndex()}, are probably just a bad
	 * idea waiting to be removed.  They're only here in case you want to display
	 * info about how the WordCram is progressing.  I wouldn't count on them
	 * being around for long -- if you really need them, please let me know.
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
	 * @deprecated Will be removed in the 0.4 release, at the latest. 
	 */
	public int currentWordIndex() {
		return getWordCramEngine().currentWordIndex();
	}
	/* END OF methods JUST for off-screen drawing. */
}
