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
 * 		 {@link #forWebPage(String)}, {@link #forTextFile(String)}, {@link #forWords(Word[])}, etc.
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
		forWords(_words);
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
	 * This WordCram will be based on word frequencies in the text of the given web page.
	 * @return The WordCram, for further setup. 
	 */
	public WordCram forWebPage(String url) {
		return forWords(new WebPage(url, parent));
	}
	
	/**
	 * This WordCram will be based on word frequencies in the text of the given html file.
	 * @return The WordCram, for further setup. 
	 */
	public WordCram forHtmlFile(String path) {
		return forWords(new HtmlFile(path, parent));
	}
	
	/**
	 * This WordCram will be based on word frequencies in the text of the given html String.
	 * @return The WordCram, for further setup. 
	 */
	public WordCram forHtml(String html) {
		return forWords(new Html(html));
	}
	
	/**
	 * This WordCram will be based on word frequencies in the text of the given text file.
	 * @return The WordCram, for further setup. 
	 */
	public WordCram forTextFile(String path) {
		return forWords(new TextFile(path, parent));
	}
	
	/**
	 * This WordCram will be based on word frequencies in the text of the given text String.
	 * @return The WordCram, for further setup. 
	 */
	public WordCram forText(String text) {
		return forWords(new Text(text));
	}
	
	/**
	 * This WordCram will be based on word frequencies in the text of the given {@link TextSource}.
	 * @return The WordCram, for further setup.
	 */
	public WordCram forWords(TextSource textSource) {
		Word[] words = new TextSplitter().split(textSource.getText());
		return forWords(words);
	}
	
	/**
	 * This WordCram will be based on the given {@link Word} array.
	 * @return The WordCram, for further setup. 
	 */
	public WordCram forWords(Word[] _words) {
		// TODO move this to drawNext(), so accidentally using >1 textsource doesn't load extra stuff?
		words = new WordSorterAndScaler().sortAndScale(_words);
		return this;
	}
	
	//----------------------------------------------
	
	/**
	 * This WordCram will get a PFont for each fontName, via
	 * <a href="http://processing.org/reference/createFont_.html" target="blank">createFont</a>,
	 * and will render words in one of those PFonts. 
	 * @return The WordCram, for further setup. 
	 */
	public WordCram withFonts(String... fontNames) {
		PFont[] fonts = new PFont[fontNames.length];
		for (int i = 0; i < fontNames.length; i++) {
			fonts[i] = parent.createFont(fontNames[i], 1);
		}
		
		return withFonts(fonts);
	}
	
	/**
	 * This WordCram will render words in one of the given fonts. 
	 * @return The WordCram, for further setup.
	 */
	public WordCram withFonts(PFont... fonts) {
		return withFonter(Fonters.pickFrom(fonts));
	}
	public WordCram withFonter(WordFonter fonter) {
		this.fonter = fonter;
		return this;
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
	 */
	public Word currentWord() {
		return getWordCramEngine().currentWord();
	}
	
	/**
	 * This method, and {@link #currentWord()}, are probably just a bad
	 * idea waiting to be removed.  They're only here in case you want to display
	 * info about how the WordCram is progressing.  I wouldn't count on them
	 * being around for long -- if you really need them, please let me know.
	 */
	public int currentWordIndex() {
		return getWordCramEngine().currentWordIndex();
	}
	/* END OF methods JUST for off-screen drawing. */
}
