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
		if (word.wasPlaced()) {
			stream.println("Word \"" + word.word + "\" placed at " + word.getRenderedPlace());
		}
		if (word.wasSkipped()) {
			stream.println("Word \"" + word.word + "\" was skipped because " + word.wasSkippedBecause());
		}
	}

	
	public void endDraw() {
		stream.println("Drawing Finished.");
	}

}
