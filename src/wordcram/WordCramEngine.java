package wordcram;

interface WordCramEngine {
	// definite keepers:
	void drawAll();
	Word getWordAt(float x, float y);
	Word[] getSkippedWords();
	
	// not so sure:
	void drawNext();
	boolean hasMore();
	float getProgress();
}
