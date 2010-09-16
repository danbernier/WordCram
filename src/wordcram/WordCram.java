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

import processing.core.*;
import wordcram.text.WordSorterAndScaler;

public class WordCram {
	
	/*
	 * Other ideas, things to wordcram:
	 * - beatles lyrics: http://www.beatleslyricsarchive.com/albums.php
	 * - us constitution: http://www.usconstitution.net/const.txt
	 * - baby names by decade: http://www.census.gov/genealogy/names/names_files.html
	 */

    // Inspirations:
    // http://www.wordle.net/ and http://blog.wordle.net
    // http://www.tagxedo.com/gallery.html -- arbitrary shapes, cool
    // http://worditout.com/ -- lame, but looks like javascript
    // http://code.google.com/p/wordookie/
    // http://tagul.com/ -- similar, but w/ arbitrary shapes (how?), and clickable words (meh)
	//
    // some people on P5 forums wanted a wordle library: http://processing.org/discourse/yabb2/YaBB.pl?num=1237544659
	
	/*
	 * TODO don't forget about (?) drawing to an off-screen buffer, rather than assuming it'll always be a PApplet. (really?)
	 * TODO palette support?  Colorers based on http://colorschemedesigner.com's Mono(hue), Complement(hue), Triad(hue, offsetAngle), etc
	 * TODO render transparently, so you can overlay it, or use it as a mask
	 * TODO pick some really nice default fonts & colors for demos: check http://www.colourlovers.com/palette/1281472/Hybrid?widths=1
	 * TODO try the other perf. improvements in the paper & book
	 * TODO scale the window (somehow) to fit words in, when they run off-screen 
	 * TODO try changing the color scheme AFTER rendering it -- should work fine
	 * TODO provide a way to print out the config: font, hues, etc
	 * TODO new DeliciousTagsTextLoader(username), new RssTextLoader(url), new HtmlTextLoader(url), new TwitterStreamTextLoader(twitterer)
	 * TODO WordCram wc = new WordCramBuilder().rotateWith(myRotater).colorWith(#ff0000, #00ff00).buildIt();
	 *      ... pass in WordFooers, and have sensible overrides (with varargs where appropriate) to build WordCrams more easily.
	 *      ... WordCramBuilder will have sensible defaults for what's not specified.
	 * TODO detect when words will be drawn TOO small, and stop rendering.
	 * 
	 * notes on building a P5 library: http://code.google.com/p/processing/wiki/LibraryBasics
	 */
	
	/*
	 * thought: to decide whether to rotate a word or not, grab the screen's width:height ratio.
	 * if it's wide & short, longer words will be horiz
	 * if it's tall & narrow, longer words will be vert
	 * short words will be either
	 * double aspect = width / height;
	 * double lenPercentile = maxWordLen / thisWordLen;
	 * ...not sure of the math here, work it out
	 */
	
	private PApplet parent;
	private PGraphics destination;
	
	private WordFonter fonter;
	private WordSizer sizer;
	private WordColorer colorer;
	private WordAngler angler;
	private WordPlacer placer;
	private WordNudger nudger;
	
	private Word[] words;
	private BBTreeBuilder bbTreeBuilder;
	private int wordIndex;
	
	// PApplet parent is only for 2 things: to get its PGraphics g (aka destination), and 
	// for createGraphics, for drawing the words.  host should be used for nothing else.
	public WordCram(PApplet _parent, Word[] _words, WordFonter _fonter, WordSizer _sizer, WordColorer _colorer, WordAngler _angler, WordPlacer _wordPlacer, WordNudger _wordNudger) {
		parent = _parent;
		destination = parent.g;
		fonter = _fonter;
		sizer = _sizer;
		colorer = _colorer;
		angler = _angler;
		placer = _wordPlacer;
		nudger = _wordNudger;
		words = new WordSorterAndScaler().sortAndScale(_words);
		wordIndex = -1;
		bbTreeBuilder = new BBTreeBuilder();
	}

	public WordCram(PApplet _parent, Word[] _words, WordFonter _fonter, WordSizer _sizer, WordColorer _colorer, WordAngler _angler, WordPlacer _wordPlacer) {
		this(_parent, _words, _fonter, _sizer, _colorer, _angler, _wordPlacer, new SpiralWordNudger());
	}
	
	public boolean hasMore() {
		return wordIndex < words.length-1;
	}
	
	/* methods JUST for off-screen drawing. */
	/* Replace these w/ a callback functor to drawNext()? */
	public Word currentWord() {
		return hasMore() ? words[wordIndex] : null;
	}
	public int currentWordIndex() {
		return wordIndex;
	}
	/* END OF methods JUST for off-screen drawing. */	
	
	private PImage renderWordToBuffer(Word word) {

		float fontSize = sizer.sizeFor(word, wordIndex, words.length);
		PFont font = fonter.fontFor(word);
		float rotation = angler.angleFor(word);
		int color = colorer.colorFor(word);
		
		destination.textFont(font, fontSize); // just for textWidth, etc
		
		int wordImageSize = PApplet.round(PApplet.max(destination.textWidth(word.word), destination.textAscent() + destination.textDescent()));
		wordImageSize = PApplet.ceil(wordImageSize * 1.5f);
		if (wordImageSize < 2) { return null; }

	    int bgColor = destination.color(255, 0);
		
	    PGraphics wordImage = parent.createGraphics(wordImageSize, wordImageSize, PApplet.JAVA2D);
		wordImage.beginDraw();
			wordImage.textFont(font, fontSize);
			wordImage.background(bgColor);
			wordImage.colorMode(PApplet.HSB);
			wordImage.fill(color);
			wordImage.noStroke();
			wordImage.textAlign(PApplet.CENTER, PApplet.CENTER);
			
			wordImage.pushMatrix();
				int halfWordImageSize = PApplet.round(wordImageSize / 2);
				wordImage.translate(halfWordImageSize, halfWordImageSize);
				wordImage.rotate(rotation);
				wordImage.translate(-halfWordImageSize, -halfWordImageSize);
				wordImage.text(word.word, 0, 0, wordImageSize, wordImageSize);
			wordImage.popMatrix();
		wordImage.endDraw();
		
		word.setBBTree(bbTreeBuilder.makeTree(wordImage, bgColor, 3));
		
		return wordImage;
	}
	
	private void placeWord(PImage wordImage, Word word) {
		int wordImageSize = wordImage.width;

		// TODO does it make sense to COMBINE wordplacer & wordnudger, the way you (sort of) orig. had it?  i think it does...
		word.setDesiredLocation(placer.place(word, wordIndex, words.length, wordImageSize, destination));
		PVector origSpot = word.getLocation();
				
		int maxAttempts = (int)((1.0-word.weight) * 600) + 100;
		Word lastCollidedWith = null;
		for (int attempt = 0; attempt < maxAttempts; attempt++) {

			word.nudge(nudger.nudgeFor(word, attempt));
			if (lastCollidedWith != null && word.overlaps(lastCollidedWith)) { continue; }
			
			boolean noOverlapFound = true;
			for (int i = 0; noOverlapFound && i < wordIndex && i < words.length; i++) {
				Word otherWord = words[i];
				if (word.overlaps(otherWord)) {
					noOverlapFound = false;
					lastCollidedWith = otherWord;
				}
			}
			
			if (noOverlapFound) {
				//System.out.println("finished early: " + attempt + "/" + maxAttempts + " (" + ((float)100*attempt/maxAttempts) + ")");
				PVector location = word.getLocation();
				destination.image(wordImage, location.x, location.y);
				//destination.pushStyle();
				//destination.strokeWeight(PApplet.map(attempt, 0, 700, 1, 30));
				//destination.stroke(0, 255, 255, 50);
				//destination.line(origSpot.x, origSpot.y, location.x, location.y);
				//destination.popStyle();
				return;
			}
		}
		
		//System.out.println("couldn't place: " + word.word + ", " + word.weight);
	}

	public void drawNext() {
		Word word = words[++wordIndex];
		PImage wordImage = renderWordToBuffer(word);
		if (wordImage != null) {
			placeWord(wordImage, word);
		}
	}
	
	public void draw() {
		while(hasMore()) {
			drawNext();
		}
	}
}
