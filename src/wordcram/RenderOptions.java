package wordcram;

class RenderOptions {
	boolean useCustomCanvas = false;
	int maxAttemptsForPlacement = -1; // default: based on Word weight
	int maxNumberOfWordsToDraw = -1; // default: unlimited
	
	/* 
	 * TODO Resolve the apparent duplication between registerSkippedWords and printWhenSkippingWords.
	 * If, when you skip a word, you give it a "skippedBecause" property (set to either "TooSmall" or "CouldntFit"),
	 * and when you successfully placed one, gave it a "placedAt" property (set to the PVector),
	 * then getSkippedWords() could just look for words with a skippedBecause property,
	 * and you could throw away BOTH of these booleans.
	 * 
	 * Note: the maxNumberOfWordsToDraw gives us another reason to skip words: they came
	 * too late in the game.
	 */
	boolean registerSkippedWords = false;
	boolean printWhenSkippingWords = false;
	
	// TODO add (& use) remaining RenderOptions
	// int minShapeSize; // default: 7
	// int boundingBoxSwell; // default: 0
	// int minBoundingBox; // default: 7
}
