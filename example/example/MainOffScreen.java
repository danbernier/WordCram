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

package example;

import java.util.Random;

import processing.core.PApplet;
import processing.core.PGraphics;
import wordcram.*;
import wordcram.text.TextSplitter;

public class MainOffScreen extends PApplet {

	Random r = new Random();
	int[] hues = new int[2];
		
	String[] fonts = 
//					 PFont.list();
//					 new String[] { "Impact", "Century Gothic", "Century Gothic Bold", "SansSerfi.plain", "Nina Bold", "Segoe UI", "LilyUPC Italic", "LilyUPC Bold", "LilyUPC"};
//					 new String[] { "Angelica", "Daisy Mae", "Grenouille", "LoveLetters" }; //"curly" }; //,
//					 new String[] { "curly" };
					 new String[] { "Blood Crow", "Blood Crow Condensed", "Blood Crow Expanded", "Blood Crow Shadow", "Blood Crow Shadow Condensed", "Ghoulish Fright AOE", "Insomnia", "MeltdownMF", "Misfits", "Nightmare AOE" };
	
	int numberOfWords;
	WordCram wordcram;
	PGraphics buffer;
	
	public void setup() {
		buffer = createGraphics(5000, 5000, JAVA2D);
		buffer.beginDraw();
		
		buffer.smooth();
		buffer.colorMode(HSB);
		initWordCram();
		
		textFont(createFont("Consolas", 15));
		fill(255);
		noStroke();
	}
	
	
	private void initWordCram() {
		Word[] words = loadWords();
		numberOfWords = words.length - 1;
		buffer.background(0);
		WordFonter fonter = Fonters.FonterFor(createFont(fonts[(int)random(fonts.length)], 1));
	
		wordcram = new WordCram(this, loadWords(), fonter, Sizers.byWeight(5, 70), Colorers.TwoHuesRandomSats(this), Anglers.MostlyHoriz, new SwirlWordPlacer(), new RandomWordNudger());
	}
	
	public void draw() {
		
		if (wordcram.hasMore()) {
			
			wordcram.drawNext();
			Word w = wordcram.currentWord();
						
			background(0);
//			fill(0, 200);
//			rect(0, 0, width, height);
			fill(255);
			text(w.word, 30, 100);
			text(nf(100*wordcram.currentWordIndex()/numberOfWords, 2) + " %", 30, 120);
			int rectMaxWidth = width - 60;
			int rectWidth = (int)map(wordcram.currentWordIndex(), 0, numberOfWords, 0, rectMaxWidth);
			rect(30, 140, rectWidth, 10);
			rect(30, 150, rectMaxWidth, 1);
		
		}
		else {
//			PGraphics flowers = flowers(buffer.width, buffer.height);		
//			buffer.blend(flowers, 0, 0, buffer.width, buffer.height, 0, 0, buffer.width, buffer.height, DARKEST);
			noLoop();
			saveImage();
		}
	}
	

	private PGraphics flowers(int gw, int gh) {
		PGraphics pg = createGraphics(gw, gh, JAVA2D);
		pg.beginDraw();
		pg.background(255);
		pg.noStroke();
		pg.smooth();
		
		int numFlowers = 6;
		for (int i = 0; i < numFlowers; i++) {
			flower(pg, numFlowers);
		}
		
		pg.fill(255, 100);
		pg.rect(0, 0, pg.width, pg.height);
		
		pg.textFont(createFont("Daisy Mae", gw*0.09f));
		pg.colorMode(HSB);
		pg.fill(240, 200, 240);
		pg.textAlign(CENTER);
		pg.text("Cthulhu! :)", pg.width/2, pg.height*0.65f);
		
		pg.endDraw();
//		pg.save("flowers.png");
		return pg;
	}
	
	
	
	public void mouseClicked() {
		initWordCram();
		loop();
	}
	
	public void keyPressed() {
		if (keyCode == ' ') {
			saveImage();
		}
	}
	
	private void saveImage() {
		buffer.save("wordcram.png");
	}
	
	private Word[] loadWords() {
		return new TextSplitter().split("so far i have not found the science");
		
		/*
		String[] lines = this.loadStrings("texts.txt");
		Word[] words = new Word[lines.length];
		
		for(int i = 0; i < lines.length; i++) {
			String[] bits = split(lines[i], "\t");
			words[i] = new Word(bits[0], Double.parseDouble(bits[1]));
		}
		
		return words;*/
	}


	private void flower(PGraphics pg, int numFlowers) {
	  int size = (int)random(30, pg.width / numFlowers+3);
	  int x = (int)random(pg.width-size);
	  int y = (int)random(pg.height-size);
	  
	  pg.fill(random(190, 240), random(100, 220), 255);
	  
	  float theta = random(TWO_PI);
	  for (int i = 0; i < 6; i++) {
	    float xd = cos(theta) * (size * 0.7f);
	    float yd = sin(theta) * (size * 0.7f);
	    pg.ellipse(x+xd, y+yd, size, size);
	    theta += TWO_PI / 3;
	    if (i == 2) theta += TWO_PI / 6;
	  }
	  
	  pg.ellipse(x, y, size, size);
	  pg.fill(255, 130);
	  pg.ellipse(x, y, size, size);
	}
}

