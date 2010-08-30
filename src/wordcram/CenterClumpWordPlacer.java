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

public class CenterClumpWordPlacer implements WordPlacer {

	private java.util.Random r = new java.util.Random();
	private float stdev = 0.4f;
	
	@Override
	public PVector place(Word word, int wordIndex, int wordsCount, int gsize, PGraphics p) {		
		return new PVector(getOneUnder(p.width-gsize), getOneUnder(p.height-gsize));
	}
	
	private int getOneUnder(float upperLimit) {
		return PApplet.round(PApplet.map((float)r.nextGaussian() * stdev, -2, 2, 0, upperLimit));
	}
}

