package wordcram;

import processing.core.PGraphics;

public class SimulatedAnnealingEngine extends AbstractWordCramEngine implements WordCramEngine {

	SimulatedAnnealingEngine(PGraphics destination, Word[] words, WordFonter fonter, WordSizer sizer,
			WordColorer colorer, WordAngler angler, WordPlacer placer, WordNudger nudger, WordShaper shaper,
			BBTreeBuilder bbTreeBuilder, RenderOptions renderOptions)
	{
		super(destination, words, fonter, sizer, colorer, angler, placer, nudger, shaper, bbTreeBuilder, renderOptions);
	}

	//====================================================================
	// definite keepers:
	
	public void drawAll() {
		
	}	
	
	//====================================================================
	// not so sure:
	
	public void drawNext() {
		// TODO Auto-generated method stub
	}

	public boolean hasMore() {
		// TODO Auto-generated method stub
		return false;
	}

	public float getProgress() {
		// TODO Auto-generated method stub
		return 0;
	}
}
