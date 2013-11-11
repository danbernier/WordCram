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

import java.util.Arrays;

class WordSorterAndScaler {

    public Word[] sortAndScale(Word[] rawWords) {
    	if (rawWords.length == 0) {
    		return rawWords;
    	}

        Word[] words = copy(rawWords);
        Arrays.sort(words);
        float maxWeight = words[0].weight;

        for (Word word : words) {
            word.weight = word.weight / maxWeight;
        }

        return words;
    }

    private Word[] copy(Word[] rawWords) {

        // was Arrays.copyOf(rawWords, rawWords.length); - removed for Java 1.5 compatibility.

        Word[] copy = new Word[rawWords.length];
        for(int i = 0; i < copy.length; i++) {
            copy[i] = rawWords[i];
        }
        return copy;
    }
}
