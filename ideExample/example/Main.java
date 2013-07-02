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

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import processing.core.PApplet;
import processing.core.PFont;
import wordcram.Anglers;
import wordcram.Colorers;
import wordcram.ShapeBasedPlacer;
import wordcram.Sizers;
import wordcram.WeightShadeColorer;
import wordcram.Word;
import wordcram.WordCram;
import wordcram.text.FiniteTextSource;

public class Main extends PApplet {
	
	WordCram wordcram;
	
	public void setup() {
		size(1900, 1000); 
		smooth();
		colorMode(HSB);
		initWordCram();
	}
	
	
	//PGraphics pg;
	private void initWordCram() {
		background(255);
		ShapeBasedPlacer placer = ShapeBasedPlacer.fromFile("/home/bastian/Downloads/darknight.png", new Color(0,0,0));
//		ShapeBasedPlacer placer = ShapeBasedPlacer.fromTextGlyphs("BASTIAN", "sans");
		FiniteTextSource ts = FiniteTextSource.fromStrings(new String[]{"Marcus","Tina","Felix","Bastian"}, 400);
		WeightShadeColorer colorer = new WeightShadeColorer(this);
		colorer.setHighest(100);
		wordcram = new WordCram(this)
//					.fromTextFile(textFilePath())
//					.fromWebPage("http://en.wikipedia.org/wiki/Batman")
//					.fromWebPage("http://www.gesetze-im-internet.de/gg/BJNR000010949.html")
					.fromWords(ts.getWords())
					.withFonts(createFont("DriodSans",1))
					.withColorer(colorer)
//					.withColorer(Colorers.twoHuesRandomSatsOnWhite(this))
//					.withAngler(Anglers.mostlyHoriz())
					.withPlacer(placer)
					.withSizer(Sizers.byWeight(3, 90))
					.maxAttemptsToPlaceWord(3000)
//					.withWordPadding(1)
//					.maxNumberOfWordsToDraw(1000)
					.withNudger(placer)
					.minShapeSize(1)
					;
	}
	
	private void finishUp() {
		wordcram.writeToSVG("wordcram.svg");
		ShapeBasedPlacer placer = ShapeBasedPlacer.fromFile("/home/bastian/Downloads/darknight.png", new Color(0,0,0));
		DOMImplementation domImpl =
	            GenericDOMImplementation.getDOMImplementation();
	        Document document = domImpl.createDocument(null, "svg", null);

	        // Create an instance of the SVG Generator
	        SVGGraphics2D svgGenerator = new SVGGraphics2D(document);

	        // draw the chart in the SVG generator
	        svgGenerator.fill(placer.getArea());
	        
	        try {
	        OutputStream outputStream = new FileOutputStream("test.svg");
	        Writer out = new OutputStreamWriter(outputStream, "UTF-8");
	        svgGenerator.stream(out, true /* use css */);						
	        outputStream.flush();
	        outputStream.close();
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
		println("Done");
		noLoop();
	}
	
	public void draw() {
		//fill(55);
		//rect(0, 0, width, height);
		
		boolean allAtOnce = true;
		if (allAtOnce) {
//			wordcram.drawAll();
			wordcram.drawAllVerbose();
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
		/*
		Word word = wordcram.getWordAt(mouseX, mouseY);
		if (word != null) {
			System.out.println(round(mouseX) + "," + round(mouseY) + " -> " + word.word);
		}
		*/
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
		return "../ideExample/tao-te-ching.txt";
	}
	
	private Word[] alphabet() {
		Word[] w = new Word[26];
		for (int i = 0; i < w.length; i++) {
			w[i] = new Word(new String(new char[]{(char)(i+65)}), 26-i);
		}
		return w;
	}
}
