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

import java.awt.Font;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import processing.core.PFont;

public class WordShaper {
    private FontRenderContext frc = new FontRenderContext(null, true, true);

    public Shape getShapeFor(String word, PFont font, float fontSize, float angle, int minShapeSize) {

        Shape shape = makeShape(word, font, fontSize);

        if (isTooSmall(shape, minShapeSize)) {
            return null;
        }

        return moveToOrigin(rotate(shape, angle));
    }

    private Shape makeShape(String word, PFont pFont, float fontSize) {
        Font font = pFont.getFont().deriveFont(fontSize);

        char[] chars = word.toCharArray();

        // TODO hmm: this doesn't render newlines.  Hrm.  If your word text is "foo\nbar", you get "foobar".
        GlyphVector gv = font.layoutGlyphVector(frc, chars, 0, chars.length,
                Font.LAYOUT_LEFT_TO_RIGHT);

        return gv.getOutline();
    }

    private boolean isTooSmall(Shape shape, int minShapeSize) {
        Rectangle2D r = shape.getBounds2D();

        // Most words will be wider than tall, so this basically boils down to height.
        // For the odd word like "I", we check width, too.
        return r.getHeight() < minShapeSize || r.getWidth() < minShapeSize;
    }

    private Shape rotate(Shape shape, float rotation) {
        if (rotation == 0) {
            return shape;
        }

        return AffineTransform.getRotateInstance(rotation).createTransformedShape(shape);
    }

    private Shape moveToOrigin(Shape shape) {
        Rectangle2D rect = shape.getBounds2D();

        if (rect.getX() == 0 && rect.getY() == 0) {
            return shape;
        }

        return AffineTransform.getTranslateInstance(-rect.getX(), -rect.getY()).createTransformedShape(shape);
    }
}
