package wordcram;

import java.awt.Color; // awt: JUST for the interface (this IS an interface)

interface WordRenderer {
	int getWidth();
	int getHeight();
	void drawWord(EngineWord word, Color color);
	void finish();
}
