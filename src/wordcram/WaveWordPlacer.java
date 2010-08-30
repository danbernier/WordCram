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

package wordcram;

import processing.core.*;

public class WaveWordPlacer implements WordPlacer, PConstants {

	private java.util.Random r = new java.util.Random();
	
	@Override
	public PVector place(Word word, int wordIndex, int wordsCount, int gsize, PGraphics p) {
		return new PVector(getX(p, wordIndex, wordsCount), 
							getY(p, wordIndex, wordsCount));
	}
	
	private int getX(PGraphics p, int wordIndex, int wordsCount) {
		return PApplet.round(
					PApplet.map(wordIndex, 0, wordsCount, 0, p.width) 
					+ (float)(r.nextGaussian() * 20)
				);		
	}
	
	private int getY(PGraphics p, int wordIndex, int wordsCount) {

		float sinOffset = PApplet.map((float)Math.sin(PApplet.map(wordIndex, 0, wordsCount, PI, -PI)),
				0, 1, 0, p.height/3);
//		sinOffset = 0.0f;

		return PApplet.round(
					PApplet.map(wordIndex, 0, wordsCount, 0, p.height) 
					+ (float)(r.nextGaussian() * 20) 
					+ sinOffset
				);		
	}

}

