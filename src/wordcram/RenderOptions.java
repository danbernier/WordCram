package wordcram;

class RenderOptions {
	int maxAttemptsForPlacement = -1; // default: based on Word weight
	int maxNumberOfWordsToDraw = -1; // default: unlimited
	int minShapeSize = 7;
	boolean printWhenSkippingWords = false;
		
	// TODO add (& use) remaining RenderOptions
	// int boundingBoxSwell; // default: 0
	// int minBoundingBox; // default: 2
}
