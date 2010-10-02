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

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.*;

import processing.core.*;
import wordcram.text.*;

/**
 * WordCram is both the main API for WordCram, and the place where all the rendering
 * happens.  There are two phases to using a WordCram: constructing, and drawing.
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
 * <p>After all that, actually rendering the WordCram is simple.  There are two typical ways:
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
	
	private PApplet parent;
	private PGraphics destination;
	
	private WordFonter fonter;
	private WordSizer sizer;
	private WordColorer colorer;
	private WordAngler angler;
	private WordPlacer placer;
	private WordNudger nudger;

	private BBTreeBuilder bbTreeBuilder;
	private FontRenderContext frc;
	
	private Word[] words;
	private int wordIndex;
	
	// PApplet parent is only for 2 things: to get its PGraphics g (aka destination), and 
	// for createGraphics, for drawing the words.  host should be used for nothing else.
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
		parent = _parent;
		destination = parent.g;
		fonter = _fonter;
		sizer = _sizer;
		colorer = _colorer;
		angler = _angler;
		placer = _wordPlacer;
		nudger = _wordNudger;
		
		bbTreeBuilder = new BBTreeBuilder();
		frc = new FontRenderContext(null, true, true);
		
		words = new WordSorterAndScaler().sortAndScale(_words);
		wordIndex = -1;
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
	 * @param _parent Your Processing sketch. You'll probably pass it as <code>this</code>.
	 */
	public WordCram(PApplet _parent) {
		parent = _parent;
		destination = parent.g;
	}
	
	// TODO need more overloads!
	
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
		// TODO move this to setDefaultsAndPrepareToDraw(), so accidentally using >1 textsource doesn't load extra stuff?
		words = new WordSorterAndScaler().sortAndScale(_words);
		wordIndex = -1;
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
	
	private void setDefaultsAndPrepareToDraw() {
		// TODO put some kind of "hey i already ran once, thx" flag in here
		if (fonter == null) fonter = Fonters.alwaysUse(parent.createFont("sans", 1));
		if (sizer == null) sizer = Sizers.byWeight(10, 70);
		if (colorer == null) colorer = Colorers.twoHuesRandomSats(parent);
		if (angler == null) angler = Anglers.mostlyHoriz();
		if (placer == null) placer = Placers.horizLine();
		if (nudger == null) nudger = new SpiralWordNudger();
		
		bbTreeBuilder = new BBTreeBuilder();
		frc = new FontRenderContext(null, true, true);
	}
	
	public boolean hasMore() {
		return wordIndex < words.length-1;
	}
	
	public void drawAll() {
		while(hasMore()) {
			drawNext();
		}
	}

	public void drawNext() {
		setDefaultsAndPrepareToDraw();
		
		Word word = words[++wordIndex];
		Shape wordShape = wordToShape(word);
		if (wordShape != null) {
			Rectangle2D rect = wordShape.getBounds2D();
			PVector wordLocation = placeWord(word, (int)rect.getWidth(), (int)rect.getHeight());
			
			if (wordLocation != null) {
				drawWordImage(word, wordShape, wordLocation);
			}
			else {
				//System.out.println("couldn't place: " + word.word + ", " + word.weight);
			}
		}
		else {
			wordIndex = words.length;
		}
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
		return hasMore() ? words[wordIndex] : null;
	}
	
	/**
	 * This method, and {@link #currentWord()}, are probably just a bad
	 * idea waiting to be removed.  They're only here in case you want to display
	 * info about how the WordCram is progressing.  I wouldn't count on them
	 * being around for long -- if you really need them, please let me know.
	 */
	public int currentWordIndex() {
		return wordIndex;
	}
	/* END OF methods JUST for off-screen drawing. */

	private Shape wordToShape(Word word) {
		
		float size = sizer.sizeFor(word, wordIndex, words.length);
		PFont pFont = fonter.fontFor(word);
		float rotation = angler.angleFor(word);		
		
		Font font = pFont.getFont().deriveFont(size);
		char[] chars = word.word.toCharArray();

		GlyphVector gv = font.layoutGlyphVector(frc, chars, 0, chars.length,
				Font.LAYOUT_LEFT_TO_RIGHT);

		Shape shape = gv.getOutline();

		if (rotation != 0.0) {
			shape = AffineTransform.getRotateInstance(rotation)
					.createTransformedShape(shape);
		}
		
		Rectangle2D rect = shape.getBounds2D();
		if (rect.getWidth() < 2 || rect.getHeight() < 2) { return null; }  // TODO extract config setting for minWordSize
		
		shape = AffineTransform.getTranslateInstance(-rect.getX(), -rect.getY()).createTransformedShape(shape);
		
		word.setBBTree(bbTreeBuilder.makeTree(shape, 3));  // TODO extract config setting for minBoundingBox, and add swelling option 

		return shape;
	}	
	
	private PImage shapeToImage(Shape shape, int color) {

		Rectangle wordRect = shape.getBounds();

		PGraphics wordImage = parent.createGraphics(wordRect.width-wordRect.x, wordRect.height-wordRect.y,
				PApplet.JAVA2D);
		wordImage.beginDraw();
		
			GeneralPath polyline = new GeneralPath(shape);
			Graphics2D g2 = (Graphics2D)wordImage.image.getGraphics();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setPaint(new Color(color, true));
			g2.fill(polyline);
		
		wordImage.endDraw();
		
		return wordImage;
	}

	private PVector placeWord(Word word, int wordImageWidth, int wordImageHeight) {
		// TODO does it make sense to COMBINE wordplacer & wordnudger, the way you (sort of) orig. had it?  i think it does...
		word.setDesiredLocation(placer.place(word, wordIndex, words.length, wordImageWidth, wordImageHeight, destination.width, destination.height));
				
		int maxAttempts = (int)((1.0-word.weight) * 600) + 100;
		Word lastCollidedWith = null;
		for (int attempt = 0; attempt < maxAttempts; attempt++) {

			word.nudge(nudger.nudgeFor(word, attempt));
			if (lastCollidedWith != null && word.overlaps(lastCollidedWith)) { continue; }
			
			PVector loc = word.getLocation();
			if (loc.x < 0 || loc.y < 0 || loc.x + wordImageWidth > destination.width || loc.y + wordImageHeight > destination.height) { continue; }
			
			boolean noOverlapFound = true;
			for (int i = 0; noOverlapFound && i < wordIndex && i < words.length; i++) {
				Word otherWord = words[i];
				if (word.overlaps(otherWord)) {
					noOverlapFound = false;
					lastCollidedWith = otherWord;
				}
			}
			
			if (noOverlapFound) {
				return word.getLocation();
			}
		}
		
		return null;
	}
	
	private void drawWordImage(Word word, Shape wordShape, PVector location) {
		
		boolean useJavaGeom = true;
		
		if (useJavaGeom) {

			GeneralPath polyline = new GeneralPath(wordShape);
			polyline.transform(AffineTransform.getTranslateInstance(location.x, location.y));
			
			boolean drawToParent = false;
			
			Graphics2D g2 = (Graphics2D)(drawToParent ? parent.getGraphics() : destination.image.getGraphics());
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setPaint(new Color(colorer.colorFor(word), true));
			g2.fill(polyline);
		
		}
		else {
			PImage wordImage = shapeToImage(wordShape, colorer.colorFor(word));
			destination.image(wordImage, location.x, location.y);
		}
		
//		destination.pushStyle();
//		destination.stroke(30, 255, 255, 50);
//		destination.noFill();
//		word.getBBTree().draw(destination);
//		destination.rect(location.x, location.y, wordImage.width, wordImage.height);
//		destination.popStyle();
		
		//destination.pushStyle();
		//destination.strokeWeight(PApplet.map(attempt, 0, 700, 1, 30));
		//destination.stroke(0, 255, 255, 50);
		//destination.line(origSpot.x, origSpot.y, location.x, location.y);
		//destination.popStyle();
	}
}
