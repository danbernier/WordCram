package wordcram;

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

import java.util.Random;

import processing.core.*;

public class Placers {

	public static WordPlacer horizLine() {
		final Random r = new Random();

		return new WordPlacer() {
			public PVector place(Word word, int wordIndex, int wordsCount,
					int gsize, PGraphics applet) {
				int centerHorizLine = (int) ((applet.height-gsize) * 0.5);
				int centerVertLine = (int) ((applet.width-gsize) * 0.5);

				float xOff = (float) r.nextGaussian() * ((applet.width-gsize) * 0.2f);
				float yOff = (float) r.nextGaussian() * 50;

				return new PVector(centerVertLine + xOff, centerHorizLine + yOff);
			}
		};
	}

	public static WordPlacer centerClump() {
		final Random r = new Random();
		final float stdev = 0.4f;

		return new WordPlacer() {

			public PVector place(Word word, int wordIndex, int wordsCount,
					int gsize, PGraphics p) {
				return new PVector(getOneUnder(p.width - gsize),
						getOneUnder(p.height - gsize));
			}

			private int getOneUnder(float upperLimit) {
				return PApplet.round(PApplet.map((float) r.nextGaussian()
						* stdev, -2, 2, 0, upperLimit));
			}
		};
	}

	public static WordPlacer swirl() {
		return new SwirlWordPlacer();
	}

	public static WordPlacer upperLeft() {
		return new UpperLeftWordPlacer();
	}

	public static WordPlacer wave() {
		return new WaveWordPlacer();
	}
}
