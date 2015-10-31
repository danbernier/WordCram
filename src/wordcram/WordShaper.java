package wordcram;

import java.awt.Font; // awt: font+word=shape
import java.awt.Shape; // awt: font+word=shape
import java.awt.font.FontRenderContext; // awt: font+word=shape
import java.awt.font.GlyphVector; // awt: font+word=shape
import java.awt.geom.AffineTransform; // awt: move and rotate shape
import java.awt.geom.Rectangle2D; // awt: move shape

import processing.core.PFont;

public class WordShaper {
    private FontRenderContext frc = new FontRenderContext(null, true, true);


    private boolean rightToLeft;
    public WordShaper(boolean rightToLeft) {
        this.rightToLeft = rightToLeft;
    }
    public WordShaper() {
        this(false);
    }


    public Shape getShapeFor(String word, Font font, float fontSize, float angle) {
        Shape shape = makeShape(word, sizeFont(font, fontSize));
        return moveToOrigin(rotate(shape, angle));
    }

    public Shape getShapeFor(String word, Font font) {
        return getShapeFor(word, font, font.getSize2D(), 0);
    }

    public Shape getShapeFor(String word, PFont pFont, float fontSize, float angle) {
        return getShapeFor(word, (Font)pFont.getNative(), fontSize, angle);
    }

    public Shape getShapeFor(String word, PFont pFont) {
        return getShapeFor(word, (Font)pFont.getNative());
    }



    private Font sizeFont(Font unsizedFont, float fontSize) {
        if (fontSize == unsizedFont.getSize2D()) {
            return unsizedFont;
        }
    	return unsizedFont.deriveFont(fontSize);
    }

    private Shape makeShape(String word, Font font) {
        char[] chars = word.toCharArray();

        // TODO hmm: this doesn't render newlines.  Hrm.  If your word text is "foo\nbar", you get "foobar".
        GlyphVector gv = font.layoutGlyphVector(frc, chars, 0, chars.length,
                this.rightToLeft ? Font.LAYOUT_RIGHT_TO_LEFT : Font.LAYOUT_LEFT_TO_RIGHT);

        return gv.getOutline();
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
