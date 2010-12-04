package example;

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

import java.util.*;

import processing.core.*;
import wordcram.*;
import wordcram.text.*;

public class Main extends PApplet {
	
	WordCram wordcram;
	
	public void setup() {

		// destination.image.getGraphics():
		// P2D -> sun.awt.image.ToolkitImage, JAVA2D -> java.awt.image.BufferedImage.

		// parent.getGraphics():
		// P2D -> sun.java2d.SunGraphics2D, JAVA2D -> same thing.

		// P2D can't draw to destination.image.getGraphics(). Interesting.

		size(900, 700); // (int)random(300, 800)); //1200, 675); //1600, 900);
		smooth();
		colorMode(HSB);
		initWordCram();
		//frameRate(1);
	}
	
	private PFont randomFont() {
		String[] fonts = PFont.list();
		String noGoodFontNames = "Dingbats|Standard Symbols L";
		String blockFontNames = "OpenSymbol|Mallige Bold|Mallige Normal|Lohit Bengali|Lohit Punjabi|Webdings";
		Set<String> noGoodFonts = new HashSet<String>(Arrays.asList((noGoodFontNames+"|"+blockFontNames).split("|")));
		String fontName;
		do {
			fontName = fonts[(int)random(fonts.length)];
		} while (fontName == null || noGoodFonts.contains(fontName));
		System.out.println(fontName);
		return createFont(fontName, 1);
		//return createFont("Molengo", 1);
	}
	
	private void initWordCram() {
		background(30);

		wordcram = new WordCram(this)
					.fromTextFile(textFilePath())					
					//.upperCase()
					//.excludeNumbers()
					.withFonts(randomFont())
					.withColorer(Colorers.twoHuesRandomSats(this))
					.withAngler(Anglers.random()) //Anglers.horiz())
					//.withPlacer(Placers.horizLine())
					.withPlacer(new WordPlacer() {
						private java.util.Random r = new java.util.Random();

						public PVector place(Word word, int wordIndex,
								int wordsCount, int wordImageWidth,
								int wordImageHeight, int fieldWidth, int fieldHeight) {

							float x = (float) (r.nextGaussian() * (1 - word.weight)) / 2;
							float y = (float) (r.nextGaussian() * (1 - word.weight)) / 2;
							//x *= Math.abs(x);
							y *= Math.abs(y);
							x = PApplet.map(x, -2, 2, 0, fieldWidth - wordImageWidth);
							y = PApplet.map(y, -2, 2, 0, fieldHeight - wordImageHeight);
							
							y = PApplet.map((float)wordIndex, wordsCount, 0, 0, fieldHeight - wordImageHeight);
							y = PApplet.map((float)word.weight, 0, 1, 0, fieldHeight - wordImageHeight);
							
							x = PApplet.map((float)word.weight, 1, 0, 0, fieldWidth - wordImageWidth);
							//y = (float)(r.nextGaussian() * (1-word.weight)) / 2;
							//y *= Math.abs(y);
							//y = PApplet.map(y, -2, 2, 0, fieldHeight - wordImageHeight);
							
							int firstLetter = word.word.toCharArray()[0];
							x = PApplet.map(firstLetter, 97, 122, 0, fieldWidth - wordImageWidth);
							
							return new PVector(x, y);
						}
					})
					.withPlacer(Placers.centerClump())
					.withSizer(Sizers.byWeight(5, 130))
					
					//.withNudger(new PlottingWordNudger(this, new SpiralWordNudger()))
					//.withNudger(new RandomWordNudger())
					
					;
		
		//wordcram.printWhenSkippingWords();
	}
	
	public void draw() {
		//fill(55);
		//rect(0, 0, width, height);
		
		boolean allAtOnce = true;
		if (allAtOnce) {
			wordcram.drawAll();
			println("Done");
			save("wordcram.png");
			noLoop();
		}
		else {
			int wordsPerFrame = 1;
			while (wordcram.hasMore() && wordsPerFrame-- > 0) {
				wordcram.drawNext();
			}
			if (!wordcram.hasMore()) {
				println("Done");
				save("wordcram.png");
				noLoop();
			}
		}
	}
	
	public void mouseMoved() {
		Word word = wordcram.getWordAt(mouseX, mouseY);
		if (word != null) {
			System.out.println(round(mouseX) + "," + round(mouseY) + " -> " + word.word);
		}
	}
		
	public void mouseClicked() {
		initWordCram();
		loop();
	}
	
	public void keyPressed() {
		if (keyCode == ' ') {
			saveFrame("wordcram-##.png");
		}
	}
	
	private Word[] loadWords() {
		String[] text = loadStrings(textFilePath());
		return new TextSplitter().split(text);
	}
	
	private String textFilePath() {
		boolean linux = true;
		String projDir = linux ? "/home/dan/projects/" : "c:/dan/";
		String path = projDir + "eclipse/wordcram/trunk/ideExample/tao-te-ching.txt";
		return path;		
	}
	
	private Word[] alphabet() {
		Word[] w = new Word[26];
		for (int i = 0; i < w.length; i++) {
			w[i] = new Word(new String(new char[]{(char)(i+65)}), 26-i);
		}
		return w;
	}
}
