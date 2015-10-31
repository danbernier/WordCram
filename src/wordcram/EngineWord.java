package wordcram;

import java.awt.Shape;  // awt: holds it; from the Word
import java.awt.geom.AffineTransform;  // awt: for translating shapes

import processing.core.PVector;

class EngineWord {
    Word word;
    int rank;

    private Shape shape;
    private BBTreeBuilder bbTreeBuilder;
    private BBTree bbTree;

    private PVector desiredLocation;
    private PVector currentLocation;

    EngineWord(Word word, int rank, int wordCount, BBTreeBuilder bbTreeBuilder) {
        this.word = word;
        this.rank = rank;
        this.bbTreeBuilder = bbTreeBuilder;
        word.setEngineWord(this);
    }

    void setShape(Shape shape, int swelling) {
        this.shape = shape;
        this.bbTree = bbTreeBuilder.makeTree(shape, swelling);
    }

    Shape getShape() {
        return shape;
    }

    boolean overlaps(EngineWord other) {
        return bbTree.overlaps(other.bbTree);
    }


    boolean containsPoint(float x, float y) {
        return bbTree.containsPoint(x, y);
    }

    void setDesiredLocation(WordPlacer placer, int count, int wordImageWidth, int wordImageHeight, int fieldWidth, int fieldHeight) {
        desiredLocation = word.getTargetPlace(placer, rank, count, wordImageWidth, wordImageHeight, fieldWidth, fieldHeight);
        currentLocation = desiredLocation.copy();
    }

    void nudge(PVector nudge) {
        currentLocation = PVector.add(desiredLocation, nudge);
        bbTree.setLocation((int)currentLocation.x, (int)currentLocation.y);
    }

    void finalizeLocation() {
        AffineTransform transform = AffineTransform.getTranslateInstance(
                currentLocation.x, currentLocation.y);
        shape = transform.createTransformedShape(shape);
        bbTree.setLocation((int)currentLocation.x, (int)currentLocation.y);
        word.setRenderedPlace(currentLocation);
    }

    PVector getCurrentLocation() {
        return currentLocation.copy();
    }

    boolean wasPlaced() {
        return word.wasPlaced();
    }

    boolean wasSkipped() {
        return word.wasSkipped();
    }
}
