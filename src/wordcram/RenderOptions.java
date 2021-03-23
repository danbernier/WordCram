package wordcram;

class RenderOptions {
    int maxAttemptsToPlaceWord = -1; // default: based on Word weight
    int maxNumberOfWordsToDraw = -1; // default: unlimited
    int minShapeSize = 7;
    int wordPadding = 1;
    double minFontSize = 7;
    double maxFontSize = 100;

    boolean rightToLeft = false;
}
