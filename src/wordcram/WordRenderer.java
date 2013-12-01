package wordcram;

import java.awt.Color;

interface WordRenderer {
	int getWidth();
	int getHeight();
	void drawWord(EngineWord word, Color color);
	void finish();
}