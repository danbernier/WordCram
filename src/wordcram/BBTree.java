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

class BBTree {
	public int x1;
	public int y1;
	public int x2;
	public int y2;
	public BBTree[] kids;

	BBTree(int _x1, int _y1, int _x2, int _y2) {
		x1 = _x1;
		y1 = _y1;
		x2 = _x2;
		y2 = _y2;
	}

	public boolean isLeaf() {
		return kids == null;
	}

	void swellLeaves(int extra) {
		if (isLeaf()) {
			x1 -= extra;
			x2 += extra;
			y1 -= extra;
			y2 += extra;
		} else {
			for (int i = 0; i < kids.length; i++) {
				kids[i].swellLeaves(extra);
			}
		}
	}
}
