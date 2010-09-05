package wordcram;

import processing.core.PVector;

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

public class Word implements Comparable<Word> {
	public String word;
	public double weight;
	
	private PVector desiredLocation;
	private PVector currentLocation;
	private BBTree bbTree;
		
	public Word(String word, double weight) {
		this.word = word;
		this.weight = weight;
	}

	@Override
	public int compareTo(Word w) {
		if (w.weight != weight) {
			return (int)(w.weight - weight);
		}
		return w.word.compareTo(word);
	}
	
	public void setBBTree(BBTree _bbTree) {
		bbTree = _bbTree;
	}

	public boolean overlaps(Word other) {
		return bbTree.overlaps(other.bbTree);
	}
	
	public void setDesiredLocation(PVector loc) {
		desiredLocation = new PVector(loc.x, loc.y);
		currentLocation = new PVector(loc.x, loc.y);
	}
	public void nudge(PVector nudge) {
		currentLocation = PVector.add(desiredLocation, nudge);
		bbTree.setLocation(currentLocation.get());
	}
	public PVector getLocation() {
		return currentLocation.get();
	}
}
