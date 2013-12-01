package wordcram;

import java.awt.Shape;
import java.util.HashMap;

import processing.core.PFont;
import processing.core.PVector;

/**
 * A weighted word, for rendering in the word cloud image.
 * <p>
 * Each Word object has a {@link #word} String, and its associated {@link #weight}, and it's constructed
 * with these two things.
 *
 *
 * <h3>Hand-crafting Your Words</h3>
 *
 * If you're creating your own <code>Word[]</code> to pass
 * to the WordCram (rather than using something like {@link WordCram#fromWebPage(String)}),
 * you can specify how a Word should be rendered: set a Word's font, size, etc:
 *
 * <pre>
 * Word w = new Word("texty", 20);
 * w.setFont(createFont("myFontName", 1));
 * w.setAngle(radians(45));
 * </pre>
 *
 * Any values set on a Word will override the corresponding component ({@link WordColorer},
 * {@link WordAngler}, etc) - it won't even be called for that word.
 *
 * <h3>Word Properties</h3>
 * A word can also have properties. If you're creating custom components,
 * you might want to send other information along with the word, for the components to use:
 *
 * <pre>
 * Word strawberry = new Word("strawberry", 10);
 * strawberry.setProperty("isFruit", true);
 *
 * Word pea = new Word("pea", 10);
 * pea.setProperty("isFruit", false);
 *
 * new WordCram(this)
 *   .fromWords(new Word[] { strawberry, pea })
 *   .withColorer(new WordColorer() {
 *      public int colorFor(Word w) {
 *        if (w.getProperty("isFruit") == true) {
 *          return color(255, 0, 0);
 *        }
 *        else {
 *          return color(0, 200, 0);
 *        }
 *      }
 *    });
 * </pre>
 *
 * @author Dan Bernier
 */
public class Word implements Comparable<Word> {

    public String word;
    public float weight;

    private Float presetSize;
    private Float presetAngle;
    private PFont presetFont;
    private Integer presetColor;
    private PVector presetTargetPlace;

    // These are null until they're rendered, and can be wiped out for a re-render.
    private Float renderedSize;
    private Float renderedAngle;
    private PFont renderedFont;
    private Integer renderedColor;
    private PVector targetPlace;
    private PVector renderedPlace;
    private WordSkipReason skippedBecause;

    private HashMap<String,Object> properties = new HashMap<String,Object>();

    public Word(String word, float weight) {
        this.word = word;
        this.weight = weight;
    }

    /**
     * Set the size this Word should be rendered at - WordCram won't even call the WordSizer.
     * @return the Word, for more configuration
     */
    public Word setSize(float size) {
        this.presetSize = size;
        return this;
    }

    /**
     * Set the angle this Word should be rendered at - WordCram won't even call the WordAngler.
     * @return the Word, for more configuration
     */
    public Word setAngle(float angle) {
        this.presetAngle = angle;
        return this;
    }

    /**
     * Set the font this Word should be rendered in - WordCram won't call the WordFonter.
     * @return the Word, for more configuration
     */
    public Word setFont(PFont font) {  // TODO provide a string overload? Will need the PApplet...
        this.presetFont = font;
        return this;
    }

    /**
     * Set the color this Word should be rendered in - WordCram won't call the WordColorer.
     * @return the Word, for more configuration
     */
    public Word setColor(int color) {
        this.presetColor = color;
        return this;
    }

    /**
     * Set the place this Word should be rendered at - WordCram won't call the WordPlacer.
     * @return the Word, for more configuration
     */
    public Word setPlace(PVector place) {
        this.presetTargetPlace = place.get();
        return this;
    }

    /**
     * Set the place this Word should be rendered at - WordCram won't call the WordPlacer.
     * @return the Word, for more configuration
     */
    public Word setPlace(float x, float y) {
        this.presetTargetPlace = new PVector(x, y);
        return this;
    }

    /*
     * These methods are called by EngineWord: they return (for instance)
     * either the color the user set via setColor(), or the value returned
     * by the WordColorer. They're package-local, so they can't be called by the sketch.
     */

    Float getSize(WordSizer sizer, int rank, int wordCount) {
        renderedSize = presetSize != null ? presetSize : sizer.sizeFor(this, rank, wordCount);
        return renderedSize;
    }

    Float getAngle(WordAngler angler) {
        renderedAngle = presetAngle != null ? presetAngle : angler.angleFor(this);
        return renderedAngle;
    }

    PFont getFont(WordFonter fonter) {
        renderedFont = presetFont != null ? presetFont : fonter.fontFor(this);
        return renderedFont;
    }

    Integer getColor(WordColorer colorer) {
        renderedColor = presetColor != null ? presetColor : colorer.colorFor(this);
        return renderedColor;
    }

    PVector getTargetPlace(WordPlacer placer, int rank, int count, int wordImageWidth, int wordImageHeight, int fieldWidth, int fieldHeight) {
        targetPlace = presetTargetPlace != null ? presetTargetPlace : placer.place(this, rank, count, wordImageWidth, wordImageHeight, fieldWidth, fieldHeight);
        return targetPlace;
    }

    void setRenderedPlace(PVector place) {
        renderedPlace = place.get();
    }

    /**
     * Get the size the Word was rendered at: either the value passed to setSize(), or the value returned from the WordSizer.
     * @return the rendered size
     */
    public float getRenderedSize() {
        return renderedSize;
    }

    /**
     * Get the angle the Word was rendered at: either the value passed to setAngle(), or the value returned from the WordAngler.
     * @return the rendered angle
     */
    public float getRenderedAngle() {
        return renderedAngle;
    }

    /**
     * Get the font the Word was rendered in: either the value passed to setFont(), or the value returned from the WordFonter.
     * @return the rendered font
     */
    public PFont getRenderedFont() {
        return renderedFont;
    }

    /**
     * Get the color the Word was rendered in: either the value passed to setColor(), or the value returned from the WordColorer.
     * @return the rendered color
     */
    public int getRenderedColor() {
        return renderedColor;
    }

    /**
     * Get the place the Word was supposed to be rendered at: either the value passed to setPlace(),
     * or the value returned from the WordPlacer.
     */
    public PVector getTargetPlace() {
        return targetPlace;
    }

    /**
     * Get the final place the Word was rendered at, or null if it couldn't be placed.
     * It returns the original target location (which is either the value passed to setPlace(),
     * or the value returned from the WordPlacer), plus the nudge vector returned by the WordNudger.
     * @return If word was placed, it's the (x,y) coordinates of the word's final location; else it's null.
     */
    public PVector getRenderedPlace() {
        return renderedPlace;
    }

    /**
     * Indicates whether the Word was placed successfully. It's the same as calling word.getRenderedPlace() != null.
     * If this returns false, it's either because a) WordCram didn't get to this Word yet,
     * or b) it was skipped for some reason (see {@link #wasSkipped()} and {@link #wasSkippedBecause()}).
     * @return true only if the word was placed.
     */
    public boolean wasPlaced() {
        return renderedPlace != null;
    }

    /**
     * Indicates whether the Word was skipped.
     * @see Word#wasSkippedBecause()
     * @return true if the word was skipped
     */
    public boolean wasSkipped() {
        return wasSkippedBecause() != null;
    }

    /**
     * Tells you why this Word was skipped.
     *
     * If the word was skipped, this will return a {@link WordSkipReason}
     * explaining why.
     * 
     * If the word was successfully placed, or WordCram hasn't
     * gotten to this word yet, this will return null.
     *
     * @return the {@link WordSkipReason} why the word was skipped, or null if
     * it wasn't skipped.
     */
    public WordSkipReason wasSkippedBecause() {
        return skippedBecause;
    }

    void wasSkippedBecause(WordSkipReason reason) {
        skippedBecause = reason;
    }

    /**
     * Get a property value from this Word, for a WordColorer, a WordPlacer, etc.
     * @param propertyName
     * @return the value of the property, or <code>null</code>, if it's not there.
     */
    public Object getProperty(String propertyName) {
        return properties.get(propertyName);
    }

    /**
     * Set a property on this Word, to be used by a WordColorer, a WordPlacer, etc, down the line.
     * @param propertyName
     * @param propertyValue
     * @return the Word, for more configuration
     */
    public Word setProperty(String propertyName, Object propertyValue) {
        properties.put(propertyName, propertyValue);
        return this;
    }

    /**
     * Displays the word, and its weight (in parentheses).
     * <code>new Word("hello", 1.3).toString()</code> will return "hello (0.3)".
     */
    @Override
    public String toString() {
        String status = "";
        if (wasPlaced()) {
            status = renderedPlace.x + "," + renderedPlace.y;
        }
        else if (wasSkipped()) {
            status = skippedBecause.toString();
        }
        if (status.length() != 0) {
            status = " [" + status + "]";
        }
        return word + " (" + weight + ")" + status;
    }

    /**
     * Compares Words based on weight only. Words with equal weight are arbitrarily sorted.
     */
    public int compareTo(Word w) {
        if (w.weight < weight) {
            return -1;
        }
        else if (w.weight > weight) {
            return 1;
        }
        else return 0;
    }
    
    // Note: these are only so we can delegate to EngineWord for getShape().
    private EngineWord engineWord;
    void setEngineWord(EngineWord engineWord) {
    	this.engineWord = engineWord;
    }
    

    // These are down here, because it comes from the EngineWord, *not* from the Fonter, Angler, etc.
    /**
     * Gets the width, in pixels, of the java.awt.Shape that will be rendered for 
     * this Word, based on the Font, Angle, and Size for this Word.
     * If that all hasn't been figured out yet, then this returns 0.
     * @return The width in pixels of this Word's Shape, or 0, if it hasn't 
     * been rendered yet.
     * @see #getRenderedHeight()
     */
    public float getRenderedWidth() {
    	if (engineWord == null) return 0.0f;
    	return (float)engineWord.getShape().getBounds2D().getWidth();
    }

    /**
     * Gets the height, in pixels, of the java.awt.Shape that will be rendered for 
     * this Word, based on the Font, Angle, and Size for this Word.
     * If that all hasn't been figured out yet, then this returns 0.
     * @return The height in pixels of this Word's Shape, or 0, if it hasn't 
     * been rendered yet.
     * @see #getRenderedWidth()
     */
    public float getRenderedHeight() {
    	if (engineWord == null) return 0.0f;
    	return (float)engineWord.getShape().getBounds2D().getHeight();
    }
}
