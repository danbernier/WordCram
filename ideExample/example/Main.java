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

public class Main extends PApplet {
	
	WordCram wordcram;
	
	public void setup() {

		// destination.image.getGraphics():
		// P2D -> sun.awt.image.ToolkitImage, JAVA2D -> java.awt.image.BufferedImage.

		// parent.getGraphics():
		// P2D -> sun.java2d.SunGraphics2D, JAVA2D -> same thing.

		// P2D can't draw to destination.image.getGraphics(). Interesting.

		size(700, 400); // (int)random(300, 800)); //1200, 675); //1600, 900);
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
	
	//PGraphics pg;
	private void initWordCram() {
		background(30);
		
		//pg = createGraphics(800, 600, JAVA2D);
		//pg.beginDraw();

		wordcram = new WordCram(this)
					//.withCustomCanvas(pg)
					.fromTextFile(textFilePath())
					//.fromWords(alphabet())
					//.upperCase()
					//.excludeNumbers()
					.withFonts(randomFont())
					.withColorer(Colorers.twoHuesRandomSats(this))
					.withAngler(Anglers.mostlyHoriz())
					.withPlacer(Placers.horizLine())
					//.withPlacer(Placers.centerClump())
					.withSizer(Sizers.byWeight(5, 70))
					//.minShapeSize(0)
					//.withMaxAttemptsForPlacement(10)
					
					//.withNudger(new PlottingWordNudger(this, new SpiralWordNudger()))
					//.withNudger(new RandomWordNudger())
					
					;
		
		wordcram.printWhenSkippingWords();
		//wordcram.maxNumberOfWordsToDraw(500);
	}
	
	private void finishUp() {
		//pg.endDraw();
		//image(pg, 0, 0);
		
		//println(wordcram.getSkippedWords());
		
		println("Done");
		save("wordcram.png");
		noLoop();
	}
	
	public void draw() {
		//fill(55);
		//rect(0, 0, width, height);
		
		boolean allAtOnce = false;
		if (allAtOnce) {
			wordcram.drawAll();
			finishUp();
		}
		else {
			int wordsPerFrame = 1;
			while (wordcram.hasMore() && wordsPerFrame-- > 0) {
				wordcram.drawNext();
			}
			
			if (!wordcram.hasMore()) {
				finishUp();
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
