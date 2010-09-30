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
import wordcram.text.*;

public class Main extends PApplet {
	
	WordCram wordcram;
	
	public void setup() {
		size(1200, 700); //(int)random(300, 800)); //1200, 675); //1600, 900);
		smooth();
		colorMode(HSB);
		initWordCram();
		//frameRate(1);
	}
	
	private PFont randomFont() {
		String[] fonts = PFont.list();
		return createFont(fonts[(int)random(fonts.length)], 1);
	}
	
	private void initWordCram() {
		background(55);
		
		wordcram = new WordCram(this)
					//.forWebPage("http://cnn.com")
					//.forWords(alphabet())
					//.forWords(loadWords())
					.forTextFile(textFilePath())
					//.forHtmlFile("webPage.html")
					//.forHtml("<html><b>Hippolyta Hall</b> and <i>The Kindly Ones</i> for Congress!</html>")
					//.forText("Cthulhu for President! Why choose the lesser of two evils?")
					.withFonts(randomFont())
					.withColors(color(0, 0, 175))
					.withAngler(Anglers.random());
	}
	
	public void draw() {
		//fill(55);
		//rect(0, 0, width, height);
		
		boolean allAtOnce = false;
		if (allAtOnce) {
			wordcram.drawAll();
			println("Done");
			noLoop();
		}
		else {
			int wordsPerFrame = 1;
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
