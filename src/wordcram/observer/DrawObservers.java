package wordcram.observer;

import java.io.PrintStream;
import java.util.Observable;
import java.util.Observer;


public class DrawObservers  {

	public static class PrintStreamObserver implements Observer {
		
		PrintStream stream;
		
		public PrintStreamObserver(PrintStream stream) {
			this.stream = stream;
		}

		public void update(Observable o, Object arg) {
			stream.println(arg);
		}
		
	}
	
	

}
