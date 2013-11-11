package wordcram;

import java.awt.Shape;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import processing.core.PApplet;
import processing.core.PImage;

public class Simpsus extends PApplet {
	
	List<Word> skipped, drawn;

	public Simpsus() {
		skipped = new ArrayList<Word>();
		drawn = new ArrayList<Word>();
	}

	public void setup() {
		size(1600, 1200);
		background(color(11, 0, 0));
	}

	public void draw() {
		drawMetallica();
	}

	public void wordSkipped(Word word) {
		skipped.add(word);
	}

	public void wordDrawn(Word word) {
		drawn.add(word);
	}
	
	public void endDraw() {
		System.out.println("done!!");
		System.out.println("Drew: " + drawn.size());
		System.out.println("Skipped: " + skipped.size());
		Map<WordSkipReason,Integer> reasons = new HashMap<WordSkipReason, Integer>();
		for (Word word: skipped) {
			if (!reasons.containsKey(word.wasSkippedBecause())) {
				reasons.put(word.wasSkippedBecause(), 1);
			} else {
				reasons.put(word.wasSkippedBecause(), reasons.get(word.wasSkippedBecause()) + 1);
			}
		}
		for (Entry<WordSkipReason,Integer> entry:reasons.entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue());
		}
	}

	public void drawMetallica() {
		try {
			PImage image = loadImage("/home/bastian/Dropbox/Camera Uploads/crams/ideas/Metallica.png");
			Shape imageShape = new ImageShaper().shape(image, color(0, 0, 0));
			ShapeBasedPlacer placer = new ShapeBasedPlacer(imageShape);
			background(color(11, 0, 0));
			new WordCram(this).toSvg("/tmp/out.svg", width, height)
					.fromWebPage("http://en.wikipedia.org/wiki/Metallica")
					.minShapeSize(3).withPlacer(placer).withNudger(placer)
					.withFont("Futura-CondensedExtraBold")
					.withColor(color(219, 201, 0))
					.sizedByWeight(6, 120).drawAll();
		} catch (java.io.FileNotFoundException x) {
			println(x);
		}
		noLoop();
	}

}
