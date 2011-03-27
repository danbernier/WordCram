package wordcram;

import java.util.regex.Pattern;

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

class WordScanner {
	
	public String[] scanIntoWords(String text) {
		String[] tokens = splitIntoTokens(text);

		for (int i = 0; i < tokens.length; i++) {
			String token = tokens[i];
			token = removePunctuationFromStringBeginning(token);
			token = removePunctuationFromStringEnd(token);
			token = removePunctuationFromBeginningOfWords(token);
			token = removePunctuationFromEndOfWords(token);
			tokens[i] = token;
		}

		return tokens;
	}

	private Pattern punctuationAtEndOfWords = Pattern.compile("[^\\p{javaLetter}]+\\s+");
	private Pattern punctuationAtBeginningOfWords = Pattern.compile("\\s+[^\\p{javaLetter}]+");
	private Pattern punctuationAtStringEnd = Pattern.compile("[^\\p{javaLetter}]+$");
	private Pattern punctuationAtStringBeginning = Pattern.compile("^[^\\p{javaLetter}]+");
	
	private String removePunctuationFromEndOfWords(String token) {
		return punctuationAtEndOfWords.matcher(token).replaceAll(" ");
	}

	private String removePunctuationFromBeginningOfWords(String token) {
		return punctuationAtBeginningOfWords.matcher(token).replaceAll(" ");
	}

	private String removePunctuationFromStringEnd(String token) {
		return punctuationAtStringEnd.matcher(token).replaceAll("");
	}

	private String removePunctuationFromStringBeginning(String token) {
		return punctuationAtStringBeginning.matcher(token).replaceAll("");
	}

	private String[] splitIntoTokens(String text) {
		return text.trim().split("(\\s+|--)");
	}
}
