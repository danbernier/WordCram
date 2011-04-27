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

import processing.core.*;
import processing.pdf.PGraphicsPDF;
import wordcram.*;

public class MainPdf extends PApplet {
	
	public void setup() {
		size(700, 800, PDF, "/home/dan/Desktop/wordcram.pdf");
	}
	
	public void draw() {
		//beginRecord(PDF, "wordcram.pdf");
		
		//((PGraphicsPDF)this.g).addFonts("/home/dan/.fonts");
		//println("fonts we can use with PDF:");
		//println(PGraphicsPDF.listFonts());
		
		smooth();
		colorMode(HSB);
		background(30);
		
		new WordCram(this)
		  .fromTextFile(textFilePath())
		  .withFont("Sawasdee")
		  .withColors(color(255))
		  .angledAt(radians(-20))
		  .withPlacer(Placers.wave())
		  .withSizer(Sizers.byWeight(5, 70))
		  .withWordPadding(3)
		  .maxNumberOfWordsToDraw(300)
		  //.printSkippedWords()
		  .drawAll();
		
		//endRecord();
		exit();
	}
		
	private String textFilePath() {
		boolean linux = true;
		String projDir = linux ? "/home/dan/projects/" : "c:/dan/";
		String path = projDir + "eclipse/wordcram/trunk/ideExample/tao-te-ching.txt";
		return path;		
	}
	
	private PFont randomFont() {
		String[] fonts = PGraphicsPDF.listFonts();
		String fontName = fonts[(int)random(fonts.length)];
		return createFont(fontName, 1); 
	}
}
