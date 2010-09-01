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

public class WordPlacement {
	private Word word;
	private BBTree bbTree;

	// TODO move the BBTreeBuilder into here? We'll need to pass the Word, and
	// the bgColor, which would be weird...
	public WordPlacement(Word _word, BBTree _bbTree) {
		word = _word;
		bbTree = _bbTree;
	}

	public Word getWord() {
		return word;
	}

	public boolean overlaps(WordPlacement other) {
		bbTree.setLocation(word.getLocation());
		other.bbTree.setLocation(other.getWord().getLocation());
		return bbTree.overlaps(other.bbTree);
	}
}
