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
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;

import processing.core.PGraphics;
import processing.core.PGraphicsJava2D;

class ProcessingWordRenderer implements WordRenderer {
	PGraphics destination;

	ProcessingWordRenderer(PGraphics destination) {
		this.destination = destination;
	}

	public int getWidth() {
		return destination.width;
	}

	public int getHeight() {
		return destination.height;
	}

	public void drawWord(EngineWord word, Color color) {
        GeneralPath path2d = new GeneralPath(word.getShape());

//        Graphics2D g2 = (Graphics2D)destination.image.getGraphics();
        Graphics2D g2 = ((PGraphicsJava2D)destination).g2;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(color);
        g2.fill(path2d);
	}

	public void finish() {}
}