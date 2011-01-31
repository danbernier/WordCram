package wordcram;

class RenderOptions {
	boolean useCustomCanvas = false;
	int maxAttemptsForPlacement = -1;
	
	/* 
	 * TODO Resolve the apparent duplication between registerSkippedWords and printWhenSkippingWords.
	 * If, when you skip a word, you give it a "skippedBecause" property (set to either "TooSmall" or "CouldntFit"),
	 * and when you successfully placed one, gave it a "placedAt" property (set to the PVector),
	 * then getSkippedWords() could just look for words with a skippedBecause property,
	 * and you could throw away BOTH of these booleans.
	 */
	boolean registerSkippedWords = false;
	boolean printWhenSkippingWords = false;
	
	// TODO add (& use) remaining RenderOptions
	// int minShapeSize;
	// int maxNumberOfWordsToDraw;
	// int boundingBoxSwell;
}
