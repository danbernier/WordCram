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

import processing.core.PApplet;

public class Colorers {

	public static WordColorer TwoHuesRandomSats(final PApplet host) {

		final float[] hues = new float[] { host.random(256), host.random(256) };

		return new WordColorer() {
			public int colorFor(Word w) {

				float hue = hues[(int)host.random(hues.length)];
				float sat = host.random(256);
				float val = host.random(100, 256);

				return host.color(hue, sat, val);
			}
		};
	}
}

