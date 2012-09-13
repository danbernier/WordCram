package example;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import processing.core.*;

public class MainJFrame {
	static public void main(String args[]) {
		// This method works!
		//PApplet.main (new String[] { "--present", "--bgcolor=#000000", "--stop-color=#cccccc", "example.Main"});
		
        // But now this method.
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        PApplet sketch = new example.Main();
        frame.add(sketch, BorderLayout.CENTER);
        sketch.init();
        frame.pack();
        frame.show();
	}
}
