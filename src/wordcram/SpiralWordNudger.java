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

public class SpiralWordNudger implements WordNudger {

	private float a;
	private float b;
	
	// Who knows? this seems to be good, but it seems to depend on the font -- bigger fonts need a bigger thetaIncrement.
	private float thetaIncrement = (float)(Math.PI * 0.03);

	public SpiralWordNudger() {
		this(1, 1);
	}

	public SpiralWordNudger(float _a, float _b) {
		a = _a;
		b = _b;
	}

	@Override
	public PVector nudgeFor(Word w, int attempt) {
		b = PApplet.map(attempt, 0, 600, 1, 10);
		float theta = thetaIncrement * attempt;
		float rad = a + b * theta;
		float x = PApplet.cos(theta) * rad;
		float y = PApplet.sin(theta) * rad;
		return new PVector(x, y);
	}
}
