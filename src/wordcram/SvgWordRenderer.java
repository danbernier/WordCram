package wordcram;

/*
Copyright 2013 Daniel Bernier

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
import java.awt.Shape;
import java.awt.geom.PathIterator;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

class SvgWordRenderer implements WordRenderer {
	private PrintWriter out;
	private int width;
	private int height;

	SvgWordRenderer(String filename, int width, int height) throws FileNotFoundException {
		this.out = new PrintWriter(filename);
		this.width = width;
		this.height = height;

		pl("<?xml version=\"1.0\"?>");
		pl("<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.0//EN\" \"http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd\">");
		// TODO add wordcram metadata
		pl("<svg width=\"" + width + "\" height=\"" + height + "\">");
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public void drawWord(EngineWord word, Color color) {
		// TODO add word metadata
		p("<g style=\"fill:" + getColor(color) + "; stroke-width:0px\">");
		renderShape(word.getShape());
		pl("</g>");
	}

	public void finish() {
		pl("</svg>");
		out.flush();
		out.close();
	}

	private String getColor(Color color) {
		// rgb(30, 200, 90)
		// #ff0023
		// TODO use hex format, for smaller file sizes

		return "rgb(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ")";
	}


	private void renderShape(Shape shape) {

		PathIterator pathIter = shape.getPathIterator(null, 1.0);
		p("<path d=\"");

		// TODO try using the fuller style w/ bezier curves
		// Since we passed 1.0 (flatness) to getPathIterator, pathIter
		// will only contain: SEG_MOVETO, SEG_LINETO, and SEG_CLOSE

		float[] coords = new float[6];

		while (!pathIter.isDone ()) {
			int type = pathIter.currentSegment(coords);

			switch(type) {
				case PathIterator.SEG_MOVETO:
					p("M" + coords[0] + " " + coords[1]);

					break;
				case PathIterator.SEG_LINETO:
					p("L" + coords[0] + " " + coords[1]);
					break;

				case PathIterator.SEG_CLOSE:
					pl("Z");
					break;
			}


			pathIter.next();
		}

		pl("\"/>");
	}

	private void pl(String s) {
		out.println(s);
	}

	private void p(String s) {
		out.print(s);
	}
}