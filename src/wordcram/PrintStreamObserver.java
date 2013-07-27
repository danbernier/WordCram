package wordcram;

import java.io.PrintStream;

public class PrintStreamObserver implements Observer {

	private PrintStream stream = System.out;

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
		stream.println("Word \"" + word.word + "\" placed at "
				+ word.getRenderedPlace());
	}

	public void wordSkipped(Word word) {
		stream.println("Word \"" + word.word + "\" was skipped because "
				+ word.wasSkippedBecause());
	}

	public void endDraw(Word[] words) {
	    	stream.println("Total Words: " + words.length);
	    	int overNumber = 0;
	    	int tooSmall = 0;
	    	int noSpace = 0;
	    	int placed = 0;
	    	for (Word w: words) {
		    	if (w.wasSkipped()) {
		    		if (w.wasSkippedBecause() == WordSkipReason.NO_SPACE) {
		    			noSpace++;
		    		} else if (w.wasSkippedBecause() == WordSkipReason.SHAPE_WAS_TOO_SMALL) {
		    			tooSmall++;
		    		} else if (w.wasSkippedBecause() == WordSkipReason.WAS_OVER_MAX_NUMBER_OF_WORDS) {
		    			overNumber++;
		    		} 
		    	} else {
		    			placed++;
		    	}
	    	}
	    	stream.println("Placed: " + placed);
	    	stream.println("Skipped because no Space: " + noSpace);
	    	stream.println("Skipped because too Small: " + tooSmall);
	    	stream.println("Skipped because max Number reached: " + overNumber);
	}

	public void wordsCounted(Word[] words) {
		stream.println("Counted " + words.length + " words.");
	}
}
