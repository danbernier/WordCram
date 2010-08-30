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
import wordcram.*;

public class MainFading extends PApplet {

	String[] fonts = // PFont.list();
					 new String[] { "Impact", "Century Gothic", "Century Gothic Bold", "SansSerfi.plain", "Nina Bold", "Segoe UI", "LilyUPC Italic", "LilyUPC Bold", "LilyUPC"};
	
	WordCram wordcram;
	
	public void setup() {
		setSize(800, 600);
		smooth();
		colorMode(HSB);
		background(0);
		initWordCram();
	}
	
	
	private void initWordCram() {
		WordFonter fonter = Fonters.FonterFor(createFont(fonts[(int)random(fonts.length)], 1));
	
		wordcram = new WordCram(this, loadWords(), fonter, Sizers.byWeight(5, 70), Colorers.TwoHuesRandomSats(this), Anglers.MostlyHoriz, new CenterClumpWordPlacer(), new RandomWordNudger());
	}
	
	public void draw() {
		fill(0, 20);
		rect(0,0,width,height);
		
		if (wordcram.hasMore()) {
			wordcram.drawNext();
		}
		else {
			noLoop();
		}
	}
	
	public void mouseClicked() {
		background(0);
		initWordCram();
		loop();
	}
	
	public void keyPressed() {
		if (keyCode == ' ') {
			saveFrame("wordcram-##.png");
		}
	}
	

	private Word[] loadWords() {

		Word[] t = new Word[26];
		for (int i = 0; i < t.length; i++) {
			t[i] = new Word(new String(new char[]{(char)(i+65)}), (float)(26-i)/26f);
		}
		return t;
		
		/*
		String[] lines = this.loadStrings("texts.txt");
		Word[] words = new Word[lines.length];
		
		for(int i = 0; i < lines.length; i++) {
			String[] bits = split(lines[i], "\t");
			words[i] = new Word(bits[0], Double.parseDouble(bits[1]));
		}
		
		return words;
		*/
	}
}
