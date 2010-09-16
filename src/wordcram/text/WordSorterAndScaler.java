package wordcram.text;

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

import java.util.*;

import wordcram.Word;

public class WordSorterAndScaler {

	public Word[] sortAndScale(Word[] rawWords) {
		
		Word[] words = Arrays.copyOf(rawWords, rawWords.length);
		Arrays.sort(words);
		double maxWeight = words[0].weight;
		
		for (Word word : words) {
			word.weight = word.weight / maxWeight;
		}
		
		return words;
	}
}
