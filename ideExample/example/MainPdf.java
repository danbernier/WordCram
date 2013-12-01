package example;

import processing.core.PApplet;
import processing.core.PFont;
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
