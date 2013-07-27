package wordcram;

import java.io.PrintStream;

public class PrintStreamObserver implements Observer {

	private PrintStream stream = System.out;

	private int overNumber;
	private int tooSmall;
	private int noSpace;
	private int placed;

	public PrintStreamObserver() {
	}

	public PrintStreamObserver(PrintStream stream) {
		this();
		this.stream = stream;
	}

	public void beginDraw() {
		stream.println("Beginning to Draw.");
	}

	public void wordDrawn(Word word) {
		placed++;
		stream.println("Word \"" + word.word + "\" placed at "
				+ word.getRenderedPlace());
	}

	public void wordSkipped(Word word) {
		if (word.wasSkippedBecause() == WordSkipReason.NO_SPACE) {
			noSpace++;
		} else if (word.wasSkippedBecause() == WordSkipReason.SHAPE_WAS_TOO_SMALL) {
			tooSmall++;
		} else if (word.wasSkippedBecause() == WordSkipReason.WAS_OVER_MAX_NUMBER_OF_WORDS) {
			overNumber++;
		}
		stream.println("Word \"" + word.word + "\" was skipped because "
				+ word.wasSkippedBecause());
	}

	public void endDraw() {
			stream.println("Total Words: " + (placed + noSpace + tooSmall + overNumber));
	    	stream.println("Placed: " + placed);
	    	stream.println("Skipped because no Space: " + noSpace);
	    	stream.println("Skipped because too Small: " + tooSmall);
	    	stream.println("Skipped because max Number reached: " + overNumber);
	}

	public void wordsCounted(Word[] words) {
		stream.println("Counted " + words.length + " words.");
	}
}
