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

import processing.core.PApplet;
import processing.core.PFont;
import wordcram.*;
import wordcram.text.TextSplitter;

public class Main extends PApplet {
	
	String[] fonts = PFont.list();
					 //new String[] { "Molengo", "Liberation Sans" }; 
					 // "Impact", "Century Gothic", "Century Gothic Bold", "SansSerfi.plain", "Nina Bold", "Segoe UI", "LilyUPC Italic", "LilyUPC Bold", "LilyUPC"};
	
	WordCram wordcram;
	
	public void setup() {
		size(1200, 600); //1200, 675); //1600, 900);
		smooth();
		colorMode(HSB);
		initWordCram();
	}
	
	private PFont randomFont() {
		return createFont(fonts[(int)random(fonts.length)], 1);
	}
	
	private void initWordCram() {
		background(55);
		
		/*
		WordColorer redBlue = new WordColorer() {
			public int colorFor(Word w) {
				if (w.word.length() % 2 == 0) {
					return color(0, 255, 255);
				}
				return color(150, 255, 255);
			}
		};
		*/
		
		//WordColorer colorer = Colorers.twoHuesRandomSats(this);
		WordColorer colorer = Colorers.pickFrom(color(0, 200, 255), color(30, 200, 255), color(200, 200, 255));
		//colorer = Colorers.twoHuesRandomSats(this);
		colorer = Colorers.pickFrom(color(0, 0, 175));
		
		wordcram = new WordCram(this, loadWords(), 
				Fonters.alwaysUse(randomFont()),
				Sizers.byWeight(5, 100),
				colorer, 
				Anglers.mostlyHoriz(),
				Placers.horizLine(),
				//new PlottingWordNudger(this, 
						new SpiralWordNudger()
				//)
		);
	}
	
	public void draw() {
		//fill(55, 20);
		//rect(0, 0, width, height);
		
		boolean allAtOnce = false;
		if (allAtOnce) {
			wordcram.draw();
			println("Done");
			noLoop();
		}
		else {
			int wordsPerFrame = 10;
			while (wordcram.hasMore() && wordsPerFrame-- > 0) {
				wordcram.drawNext();
			}
			if (!wordcram.hasMore()) {
				println("Done");
				noLoop();
			}
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
		boolean loadFile = true;
		if (!loadFile) {
			Word[] w = new Word[26];
			for (int i = 0; i < w.length; i++) {
				w[i] = new Word(new String(new char[]{(char)(i+65)}), (float)(26-i)/26f);
			}
			return w;
		}
		else {
			boolean linux = true;
			String projDir = linux ? "/home/dan/projects/" : "c:/dan/";
			String path = projDir + "eclipse/wordcram/trunk/ideExample/tao-te-ching.txt";
			return new TextSplitter().split(loadStrings(path));
		}
	}
}
