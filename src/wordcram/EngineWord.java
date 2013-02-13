package wordcram;

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

import java.awt.Shape;
import java.awt.geom.AffineTransform;

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
        currentLocation = desiredLocation.get();
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
        return currentLocation.get();
    }

    boolean wasPlaced() {
        return word.wasPlaced();
    }

    boolean wasSkipped() {
        return word.wasSkipped();
    }
}
