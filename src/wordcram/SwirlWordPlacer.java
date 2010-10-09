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

import processing.core.PApplet;
import processing.core.PVector;

public class SwirlWordPlacer implements WordPlacer {

	private java.util.Random r = new java.util.Random();
	
	@Override
	public PVector place(Word word, int wordIndex, int wordsCount, int wordImageWidth, int wordImageHeight, int fieldWidth, int fieldHeight) {

		float theta = PApplet.map(wordIndex, 0, wordsCount, 0, 6 * PApplet.TWO_PI);
		
		// TODO applet.width/2 -> max = triggy, based on sin/cos, so if height is short, it stays onscreen 
		float radius = PApplet.map(wordIndex, 0, wordsCount, 0, fieldWidth/2);
//		float radius = PApplet.map((float)word.weight, 0, 1.0f, 0, applet.width/2);
//		radius = radius * (float)word.weight * 100;
		
		float cx = fieldWidth/2;
		float cy = fieldHeight/2;
		
		float x = PApplet.cos(theta) * radius;
		float y = PApplet.sin(theta) * radius;
		
		x += cx;
		y += cy;
		
		//x += r.nextGaussian() * 20;
		//y += r.nextGaussian() * 20;
		
		return new PVector(x, y);		
	}

}
