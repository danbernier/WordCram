package wordcram;

import processing.core.*;
import wordcram.text.*;
import java.util.ArrayList;

/**
 * The main API for WordCram.
 *
 * <p>There are three steps to making a WordCram:
 * <ol>
 * <li>weight your words
 * <li>style your words
 * <li>draw your WordCram
 * </ol>
 * You start with a <code>new WordCram(this)</code>, and then...
 *
 * <h2>Step One: Weight Your Words</h2>
 *
 * Give WordCram some text to chew on, or an array of Words you've
 * weighted yourself.
 *
 * <h3>Let WordCram Weight Your Words</h3>
 *
 * <p>WordCram weights your words by the number of times they appear
 * in a document.  It can load the document a few ways:
 *
 * <ul>
 * <li>{@link #fromWebPage(String)} and {@link #fromHtmlFile(String)} load the HTML and scrape out the words</li>
 * <li>{@link #fromHtmlString(String...)} takes a String (or String[]), assumes it's HTML, and scrapes out its text</li>
 * <li>{@link #fromTextFile(String)} loads a file (from the filesystem or the network), and counts the words</li>
 * <li>{@link #fromTextString(String...)} takes a String (or String[]), and counts the words</li>
 * </ul>
 *
 * <p>If you need some other way to load your text, pass your own
 * TextSource to {@link #fromText(TextSource)}, and WordCram get its
 * text via {@link TextSource#getText()}.
 *
 * <p>Once the text is loaded, you can control how WordCram counts up the words.
 *
 * <p><b>Case sensitivity:</b> If your text contains "hello", "HELLO",
 * and "Hello",
 *
 * <ul><li>{@link #lowerCase()} will count them all as "hello"</li>
 *     <li>{@link #upperCase()} will count them all as "HELLO"</li>
 *     <li>{@link #keepCase()}, the default, will count them separately, as three different words</li></ul>
 *
 * <p><b>Numbers:</b> If your text contains words like "42" or
 * "3.14159", you can remove them with {@link #excludeNumbers()} (the
 * default), or include them with {@link #includeNumbers()}.
 *
 * <p><b>Stop words:</b> WordCram uses <a
 * href="https://github.com/jdf/cue.language">cue.language</a> to remove common words from the text by default, but you can
 * add your own stop words with {@link #withStopWords(String)}.
 *
 *
 * <h3>Weight Your Own Words</h3>
 *
 * <p>If you have some other way to weight your words, you can pass
 * them to {@link #fromWords(Word[])}, and in that case, you can use
 * {@link Word#setColor(int)}, {@link Word#setFont(PFont)}, {@link
 * Word#setAngle(float)}, and/or {@link Word#setPlace(PVector)} to
 * control how any (or all) of your Words are drawn.
 *
 *
 *
 * <h2>Step Two: Style Your Words</h2>
 *
 * There are six questions you have to answer when drawing a word on the WordCram:
 *
 * <h3>How big should it be?</h3>
 * A word can be
 * {@link #sizedByWeight(int, int)},
 * {@link #sizedByRank(int, int)}, or
 * {@link #withSizer(WordSizer)}
 *
 * <h3>How should it be angled?</h3>
 * It can be
 * {@link #angledAt(float...)},
 * {@link #angledBetween(float, float)}, or
 * {@link #withAngler(WordAngler)}
 *
 * <h3>What font should it be in?</h3> You can render words {@link
 * #withFont(String)} or {@link #withFonts(String...)} (those both can
 * also take PFonts), or {@link #withFonter(WordFonter)}
 *
 * <h3>How should it be colored?</h3>
 * {@link #withColor(int)},
 * {@link #withColors(int...)}, or
 * {@link #withColorer(WordColorer)}
 *
 * <h3>Where on the image should it go?</h3>
 * {@link #withPlacer(WordPlacer)}
 *
 * <h3>If it doesn't fit at first, how should I nudge it?</h3>
 * {@link #withNudger(WordNudger)}
 *
 * <h2>Step Three: Draw Your WordCram</h2>
 *
 * <p>After all that, actually rendering the WordCram is simple.
 *
 * You can repeatedly call {@link #drawNext()} while the WordCram
 * {@link #hasMore()} words to draw (probably once per Processing
 * frame):
 *
 * <pre>
 * void draw() {
 *     if (wordCram.hasMore()) {
 *         wordCram.drawNext();
 *     }
 * }
 * </pre>
 *
 * Or you can call {@link #drawAll()} once, and let it loop for you:
 *
 * <pre>
 * void draw() {
 *     wordCram.drawAll();
 * }
 * </pre>
 *
 * <h2>Step Three-and-a-Half: How Did It Go?</h2>
 *
 * <p>If you're having trouble getting your words to show up, you
 * might want to {@link #getSkippedWords()}.  Knowing which words were
 * skipped, and why (see {@link Word#wasSkippedBecause()}), can help
 * you size and place your words better.
 *
 * <p>You can also {@link #getWords()} to see the whole list, and
 * {@link #getWordAt(float,float)} to see which word covers a given pixel.
 *
 * @author Dan Bernier
 */
public class WordCram {

    /*
     * This class is really only two parts: the fluent builder API, and pass-through calls
     * to the WordCramEngine, where all the work happens.  This separation keeps the classes
     * focused on only one thing, but still gives the user a pretty nice API.
     */
    private Word[] words;
    private ArrayList<TextSource> textSources = new ArrayList<TextSource>();
    private String extraStopWords = "";
    private boolean excludeNumbers = true;
    private enum TextCase { Lower, Upper, Keep };
    private TextCase textCase = TextCase.Keep;

    private WordCramEngine wordCramEngine;

    private PApplet parent;

    private WordFonter fonter;
    private WordSizer sizer;
    private WordColorer colorer;
    private WordAngler angler;
    private WordPlacer placer;
    private WordNudger nudger;

    private WordRenderer renderer;
    private RenderOptions renderOptions = new RenderOptions();
    private Observer observer;

    /**
     * Make a new WordCram.
     * <p>
     * It's the starting point of the fluent API for building WordCrams.
     *
     * @param parent Your Processing sketch. Pass it as <code>this</code>.
     */
    public WordCram(PApplet parent) {
        this.parent = parent;
        this.renderer = new ProcessingWordRenderer(parent.g);
        this.observer = new SketchCallbackObserver(parent);
    }

    /**
     * Tells WordCram which words to ignore when it counts up the words in your text.
     * These words won't show up in the image.
     * <p>
     * Stop-words are always case-insensitive: if your source text contains "The plane,
     * the plane!", using "the" for a stop-word is enough to block both "the" and "The".
     * <p>
     * It doesn't matter whether this is called before or after the "for{text}" methods.
     * <p>
     * <b><i>Note:</i></b> Stop-words have no effect if you're passing in your own custom
     * {@link Word} array, since WordCram won't do any text analysis on it (other than
     * sorting the words and scaling their weights).
     *
     * @param extraStopWords a space-delimited String of words to ignore when counting the words in your text.
     * @return The WordCram, for further setup or drawing.
     */
    public WordCram withStopWords(String extraStopWords) {
        this.extraStopWords = extraStopWords;
        return this;
    }

    /**
     * Exclude numbers from the text in the WordCram.  They're excluded by default.
     * <p>
     * Words that are all numbers, like 1, 3.14159, 42, or 1492, will be excluded.
     * Words that have some letters and some numbers like 1A, U2, or funnyguy194 will be included.
     *
     * @see #includeNumbers()
     * @return The WordCram, for further setup or drawing.
     */
    public WordCram excludeNumbers() {
        this.excludeNumbers = true;
        return this;
    }

    /**
     * Include numbers from the text in the WordCram.  They're excluded by default.
     *
     * @see #excludeNumbers()
     * @return The WordCram, for further setup or drawing.
     */
    public WordCram includeNumbers() {
        this.excludeNumbers = false;
        return this;
    }

    /**
     * Make the WordCram change all words to lower-case.
     * Stop-words are unaffected; they're always case-insensitive.
     * The default is to keep words as they appear in the text.
     *
     * @return The WordCram, for further setup or drawing.
     */
    public WordCram lowerCase() {
        this.textCase = TextCase.Lower;
        return this;
    }

    /**
     * Make the WordCram change all words to upper-case.
     * Stop-words are unaffected; they're always case-insensitive.
     * The default is to keep words as they appear in the text.
     *
     * @return The WordCram, for further setup or drawing.
     */
    public WordCram upperCase() {
        this.textCase = TextCase.Upper;
        return this;
    }

    /**
     * Make the WordCram leave all words cased as they appear in the text.
     * Stop-words are unaffected; they're always case-insensitive.
     * This is the default.
     *
     * @return The WordCram, for further setup or drawing.
     */
    public WordCram keepCase() {
        this.textCase = TextCase.Keep;
        return this;
    }

    /**
     * Make a WordCram from the text on a web page.
     * Just before the WordCram is drawn, it'll load the web page's HTML, scrape out the text,
     * and count and sort the words.
     *
     * @param webPageAddress the URL of the web page to load
     * @return The WordCram, for further setup or drawing.
     */
    public WordCram fromWebPage(String webPageAddress) {
        return fromWebPage(webPageAddress, null);
    }

    /**
     * Make a WordCram from the text in any elements on a web page that match the
     * <tt>cssSelector</tt>.
     * Just before the WordCram is drawn, it'll load the web page's HTML, scrape
     * out the text, and count and sort the words.
     *
     * HTML parsing is handled by Jsoup, so see
     * <a href="http://jsoup.org/cookbook/extracting-data/selector-syntax">the
     * Jsoup selector documentation</a> if you're having trouble writing your
     * selector.
     *
     * @param webPageAddress the URL of the web page to load
     * @param cssSelector a CSS selector to filter the HTML by, before extracting
     * text
     * @return The WordCram, for further setup or drawing.
     */
    public WordCram fromWebPage(String webPageAddress, String cssSelector) {
        return fromText(new WebPage(webPageAddress, cssSelector, parent));
    }

    /**
     * Make a WordCram from the text in a HTML file.
     * Just before the WordCram is drawn, it'll load the file's HTML, scrape out the text,
     * and count and sort the words.
     *
     * @param htmlFilePath the path of the html file to load
     * @return The WordCram, for further setup or drawing.
     */
    public WordCram fromHtmlFile(String htmlFilePath) {
        return fromHtmlFile(htmlFilePath, null);
    }

    /**
     * Make a WordCram from the text in any elements on a web page that match the
     * <tt>cssSelector</tt>.
     * Just before the WordCram is drawn, it'll load the file's HTML, scrape out the text,
     * and count and sort the words.
     *
     * HTML parsing is handled by Jsoup, so see
     * <a href="http://jsoup.org/cookbook/extracting-data/selector-syntax">the
     * Jsoup selector documentation</a> if you're having trouble writing your
     * selector.
     *
     * @param htmlFilePath the path of the html file to load
     * @param cssSelector a CSS selector to filter the HTML by, before extracting
     * text
     * @return The WordCram, for further setup or drawing.
     */
    public WordCram fromHtmlFile(String htmlFilePath, String cssSelector) {
        return fromText(new WebPage(htmlFilePath, cssSelector, parent));
    }

    // TODO from an inputstream!  or reader, anyway

    /**
     * Makes a WordCram from a String of HTML.  Just before the
     * WordCram is drawn, it'll scrape out the text from the HTML,
     * and count and sort the words. It takes one String, or any
     * number of Strings, or an array of Strings, so you can
     * easily use it with <a
     * href="http://processing.org/reference/loadStrings_.html"
     * target="blank">loadStrings()</a>.
     *
     * @deprecated because its signature is annoying, and makes it hard to
     * pass a CSS Selector. If you love this method, and want it to stick around,
     * let me know: <a href="http://github.com/danbernier/WordCram/issues">open
     * a github issue</a>, send me a
     * <a href="http://twitter.com/wordcram">tweet</a>,
     * or say hello at wordcram at gmail.
     * Otherwise, it'll be deleted in a future release, probably 0.6.
     *
     * @param html the String(s) of HTML
     * @return The WordCram, for further setup or drawing.
     */
    @Deprecated
    public WordCram fromHtmlString(String... html) {
        return fromText(new Html(PApplet.join(html, "")));
    }

    /**
     * Makes a WordCram from a text file, either on the filesystem
     * or the network.  Just before the WordCram is drawn, it'll
     * load the file, and count and sort its words.
     *
     * @param textFilePathOrUrl the path of the text file
     * @return The WordCram, for further setup or drawing.
     */
    public WordCram fromTextFile(String textFilePathOrUrl) {
        return fromText(new TextFile(textFilePathOrUrl, parent));
    }

    /**
     * Makes a WordCram from a String of text. It takes one
     * String, or any number of Strings, or an array of Strings,
     * so you can easily use it with <a
     * href="http://processing.org/reference/loadStrings_.html"
     * target="blank">loadStrings()</a>.
     *
     * @param text the String of text to get the words from
     * @return The WordCram, for further setup or drawing.
     */
    //example fromTextString(loadStrings("my.txt"))
    //example fromTextString("one", "two", "three")
    //example fromTextString("Hello there!")
    public WordCram fromTextString(String... text) {
        return fromText(new Text(PApplet.join(text, " ")));
    }

    /**
     * Makes a WordCram from any TextSource.
     *
     * <p> It only caches the TextSource - it won't load the text
     * from it until {@link #drawAll()} or {@link #drawNext()} is
     * called.
     *
     * @param textSource the TextSource to get the text from.
     * @return The WordCram, for further setup or drawing.
     */
    public WordCram fromText(TextSource textSource) {
        this.textSources.add(textSource);
        return this;
    }

    /**
     * Makes a WordCram from your own custom Word array.  The
     * Words can be ordered and weighted arbitrarily - WordCram
     * will sort them by weight, and then divide their weights by
     * the weight of the heaviest Word, so the heaviest Word will
     * end up with a weight of 1.0.
     *
         * <p>Note: WordCram won't do any text analysis on the words;
     * stop-words will have no effect, etc. These words are
     * supposed to be ready to go.
     *
     * @return The WordCram, for further setup or drawing.
     */
    public WordCram fromWords(Word[] words) {
        this.words = words;
        return this;
    }

    //----------------------------------------------

    /**
     * This WordCram will get a
     * <a href="http://processing.org/reference/PFont.html" target="blank">PFont</a>
     * for each fontName, via
     * <a href="http://processing.org/reference/createFont_.html" target="blank">createFont</a>,
     * and will render words in one of those PFonts.
     *
     * @return The WordCram, for further setup or drawing.
     */
    public WordCram withFonts(String... fontNames) {
        PFont[] fonts = new PFont[fontNames.length];
        for (int i = 0; i < fontNames.length; i++) {
            fonts[i] = parent.createFont(fontNames[i], 1);
        }

        return withFonts(fonts);
    }

    /**
     * Make the WordCram render all words in the font that matches
     * the given name, via Processing's
     * <a href="http://processing.org/reference/createFont_.html" target="blank">createFont</a>.
     *
     * @param fontName the font name to pass to createFont.
     * @return The WordCram, for further setup or drawing.
     */
    public WordCram withFont(String fontName) {
        PFont font = parent.createFont(fontName, 1);
        return withFont(font);
    }

    /**
     * This WordCram will render words in one of the given
     * <a href="http://processing.org/reference/PFont.html" target="blank">PFonts</a>.
     *
     * @return The WordCram, for further setup or drawing.
     */
    public WordCram withFonts(PFont... fonts) {
        return withFonter(Fonters.pickFrom(fonts));
    }

    /**
     * Make the WordCram render all words in the given
     * <a href="http://processing.org/reference/PFont.html" target="blank">PFont</a>.
     *
     * @param font the PFont to render the words in.
     * @return The WordCram, for further setup or drawing.
     */
    public WordCram withFont(PFont font) {
        return withFonter(Fonters.pickFrom(font));
    }

    /**
     * Use the given WordFonter to pick fonts for each word.
     * You can make your own, or use a pre-fab one from {@link Fonters}.
     *
     * @see WordFonter
         * @see Fonters
     * @param fonter the WordFonter to use.
     * @return The WordCram, for further setup or drawing.
     */
    /*=
     * Here is a bit of a play-ground for now, to see how
     * this might work. See docgen.rb.
     * example withFonter({your WordFonter})
     * example withFonter(Fonters.alwaysUse("Comic Sans"))
     * example withFonter(new WordFonter() { ... (how to doc-gen this?)
     =*/
    public WordCram withFonter(WordFonter fonter) {
        this.fonter = fonter;
        return this;
    }

    /**
     * Make the WordCram size words by their weight, where the
     * "heaviest" word will be sized at <code>maxSize</code>.
     *
         * <p>Specifically, it makes the WordCram use {@link
         * Sizers#byWeight(int, int)}.
     *
     * @param minSize the size to draw a Word of weight 0
     * @param maxSize the size to draw a Word of weight 1
     * @return The WordCram, for further setup or drawing.
     */
    /*=example sizedByWeight(int minSize, int maxSize)=*/
    public WordCram sizedByWeight(int minSize, int maxSize) {
        return withSizer(Sizers.byWeight(minSize, maxSize));
    }

    /**
     * Make the WordCram size words by their rank.  The first
     * word will be sized at <code>maxSize</code>.
     *
         * <p>Specifically, it makes the WordCram use {@link
         * Sizers#byRank(int, int)}.
     *
     * @param minSize the size to draw the last Word
     * @param maxSize the size to draw the first Word
     * @return The WordCram, for further setup or drawing.
     */
    /*=example sizedByRank(int minSize, int maxSize)=*/
    public WordCram sizedByRank(int minSize, int maxSize) {
        return withSizer(Sizers.byRank(minSize, maxSize));
    }

    /**
     * Use the given WordSizer to pick fonts for each word.
     * You can make your own, or use a pre-fab one from {@link Sizers}.
     *
         * @see WordSizer
     * @see Sizers
     * @param sizer the WordSizer to use.
     * @return The WordCram, for further setup or drawing.
     */
    public WordCram withSizer(WordSizer sizer) {
        this.sizer = sizer;
        return this;
    }

    /**
     * Render words by randomly choosing from the given colors.
     * Uses {@link Colorers#pickFrom(int...)}.
     *
     * <p> Note: if you want all your words to be, say, red,
     * <i>don't</i> do this:
     *
     * <pre>
     * ...withColors(255, 0, 0)...  // Not what you want!
     * </pre>
         *
     * You'll just see a blank WordCram.  Since <a
     * href="http://processing.org/reference/color_datatype.html"
     * target="blank">Processing stores colors as integers</a>,
     * WordCram will see each integer as a different color, and
     * it'll color about 1/3 of your words with the color
     * represented by the integer 255, and the other 2/3 with the
     * color represented by the integer 0.  The punchline is,
     * Processing stores opacity (or alpha) in the highest bits
     * (the ones used for storing really big numbers, from
     * 2<sup>24</sup> to 2<sup>32</sup>), so your colors 0 and 255
     * have, effectively, 0 opacity -- they're completely
     * transparent.  Oops.
     *
     * <p> Use this instead, and you'll get what you're after:
     *
     * <pre>
     * ...withColors(color(255, 0, 0))...  // Much better!
     * </pre>
     *
     * @param colors the colors to randomly choose from.
     * @return The WordCram, for further setup or drawing.
     */
    public WordCram withColors(int... colors) {
        return withColorer(Colorers.pickFrom(colors));
    }

    /**
     * Renders all words in the given color.
     * @see #withColors(int...)
     * @param color the color for each word.
     * @return The WordCram, for further setup or drawing.
     */
    public WordCram withColor(int color) {
        return withColors(color);
    }

    /**
     * Use the given WordColorer to pick colors for each word.
     * You can make your own, or use a pre-fab one from {@link Colorers}.
     *
     * @see WordColorer
     * @see Colorers
     * @param colorer the WordColorer to use.
     * @return The WordCram, for further setup or drawing.
     */
    public WordCram withColorer(WordColorer colorer) {
        this.colorer = colorer;
        return this;
    }

    // TODO need more overloads!

    /**
     * Make the WordCram rotate each word at one of the given angles.
     * @param anglesInRadians The list of possible rotation angles, in radians
     * @return The WordCram, for further setup or drawing.
     */
    public WordCram angledAt(float... anglesInRadians) {
        return withAngler(Anglers.pickFrom(anglesInRadians));
    }

    /**
     * Make the WordCram rotate words randomly, between the min and max angles.
     * @param minAngleInRadians The minimum rotation angle, in radians
     * @param maxAngleInRadians The maximum rotation angle, in radians
     * @return The WordCram, for further setup or drawing.
     */
    public WordCram angledBetween(float minAngleInRadians, float maxAngleInRadians) {
        return withAngler(Anglers.randomBetween(minAngleInRadians, maxAngleInRadians));
    }

    /**
     * Use the given WordAngler to pick angles for each word.
     * You can make your own, or use a pre-fab one from {@link Anglers}.
     *
     * @see WordAngler
     * @see Anglers
     * @param angler the WordAngler to use.
     * @return The WordCram, for further setup or drawing.
     */
    public WordCram withAngler(WordAngler angler) {
        this.angler = angler;
        return this;
    }

    /**
     * Use the given WordPlacer to pick locations for each word.
     * You can make your own, or use a pre-fab one from {@link Placers}.
     *
     * @see WordPlacer
     * @see Placers
     * @see PlottingWordPlacer
     * @param placer the WordPlacer to use.
     * @return The WordCram, for further setup or drawing.
     */
    public WordCram withPlacer(WordPlacer placer) {
        this.placer = placer;
        return this;
    }

    /**
     * Use the given WordNudger to pick angles for each word.
     * You can make your own, or use a pre-fab one.
     *
     * @see WordNudger
     * @see SpiralWordNudger
     * @see RandomWordNudger
     * @see PlottingWordNudger
     * @param nudger the WordNudger to use.
     * @return The WordCram, for further setup or drawing.
     */
    public WordCram withNudger(WordNudger nudger) {
        this.nudger = nudger;
        return this;
    }

    /**
     * How many attempts should be used to place a word.  Higher
     * values ensure that more words get placed, but will make
     * algorithm slower.
     * @param maxAttempts
     * @return The WordCram, for further setup or drawing.
     */
    public WordCram maxAttemptsToPlaceWord(int maxAttempts) {
        renderOptions.maxAttemptsToPlaceWord = maxAttempts;
        return this;
    }

    /**
     * The maximum number of Words WordCram should try to draw.
     * This might be useful if you have a whole bunch of words,
     * and need an artificial way to cut down the list (for
     * speed).  By default, it's unlimited.
     * @param maxWords can be any value from 0 to Integer.MAX_VALUE. Values < 0 are treated as unlimited.
     * @return The WordCram, for further setup or drawing.
     */
    public WordCram maxNumberOfWordsToDraw(int maxWords) {
        renderOptions.maxNumberOfWordsToDraw = maxWords;
        return this;
    }

    /**
     * The smallest-sized Shape the WordCram should try to draw.
     * By default, it's 7.
     * @param minShapeSize the size of the smallest Shape.
     * @return The WordCram, for further setup or drawing.
     */
    public WordCram minShapeSize(int minShapeSize) {
        renderOptions.minShapeSize = minShapeSize;
        return this;
    }

    /**
     * Use a custom canvas instead of the applet's default one.
     * This may be needed if rendering in background or in other
     * dimensions than the applet size is needed.
     * @deprecated for more consistent naming. Use {@link #toCanvas(PGraphics canvas)} instead.
     * @param canvas the canvas to draw to
     * @return The WordCram, for further setup or drawing.
     */
    public WordCram withCustomCanvas(PGraphics canvas) {
        return toCanvas(canvas);
    }

    /**
     * Use a custom canvas instead of the applet's default one.
     * This may be needed if rendering in background or in other
     * dimensions than the applet size is needed.
     * @param canvas the canvas to draw to
     * @return The WordCram, for further setup or drawing.
     */
    public WordCram toCanvas(PGraphics canvas) {
        this.renderer = new ProcessingWordRenderer(canvas);
        return this;
    }

    public WordCram toSvg(String filename, int width, int height) throws java.io.FileNotFoundException {
        this.renderer = new SvgWordRenderer(parent.sketchPath(filename), width, height);
        return this;
    }


    /**
     * Add padding around each word, so they stand out from each other more.
     * If you call this multiple times, the last value will be used.
     *
     * WordCram uses a tree of java.awt.Rectangle objects to detect whether two words overlap.
     * What this method actually does is call <code>Rectangle.grow(padding)</code> on the
     * leaves of that tree.
     *
     * @param padding The number of pixels to grow each rectangle by. Defaults to zero.
     * @return The WordCram, for further setup or drawing.
     */
    public WordCram withWordPadding(int padding) {
        renderOptions.wordPadding = padding;
        return this;
    }

    /**
     * Render a heatmap of the locations where your WordPlacer
     * places words. This is pretty accurate: it renders all your words
     * according to your sizer, fonter, angler, and placer, without
     * nudging them, to an in-memory buffer. Then it splits your sketch
     * into 10x10 pixel squares, and counts how many words overlap each
     * square, and renders a heatmap: black for 0 words, green for 1,
     * and red for more than 8. Rendering too many words at the same
     * spot will make your WordCram run slower, and skip more words,
     * so learning where your hotspots are can be helpful.
     *
     * This is very experimental, and could be changed or removed in a
     * future release.
     */
    public void testPlacer() {
      initComponents();
      WordShaper shaper = new WordShaper(renderOptions.rightToLeft);
      PlacerHeatMap heatMap = new PlacerHeatMap(words, fonter, sizer, angler, placer, nudger, shaper);
      heatMap.draw(parent);
    }

    private WordCramEngine getWordCramEngine() {
        if (wordCramEngine == null) {
            initComponents();
            WordShaper shaper = new WordShaper(renderOptions.rightToLeft);
            wordCramEngine = new WordCramEngine(renderer, words, fonter, sizer, colorer, angler, placer, nudger, shaper, new BBTreeBuilder(), renderOptions, observer);
        }
        return wordCramEngine;
    }

    private void initComponents() {

      if (words == null && !textSources.isEmpty()) {
        String text = joinTextSources();

        text = textCase == TextCase.Lower ? text.toLowerCase()
          : textCase == TextCase.Upper ? text.toUpperCase()
          : text;

        words = new WordCounter().withExtraStopWords(extraStopWords).shouldExcludeNumbers(excludeNumbers).count(text, renderOptions);
        observer.wordsCounted(words);
        if (words.length == 0) {
          warnScripterAboutEmptyWordArray();
        }
      }
      words = new WordSorterAndScaler().sortAndScale(words);

      if (fonter == null) fonter = Fonters.alwaysUse(parent.createFont("sans", 1));
      if (sizer == null) sizer = Sizers.byWeight(5, 70);
      if (colorer == null) colorer = Colorers.alwaysUse(parent.color(0));
      if (angler == null) angler = Anglers.mostlyHoriz();
      if (placer == null) placer = Placers.horizLine();
      if (nudger == null) nudger = new SpiralWordNudger();
    }

    private String joinTextSources() {
        StringBuffer buffer = new StringBuffer();
        for (TextSource textSource : textSources) {
            buffer.append(textSource.getText());
            buffer.append("\n");
        }
        return buffer.toString();
    }

    private void warnScripterAboutEmptyWordArray() {
    	System.out.println();
    	System.out.println("cue.language can't find any non-stop words in your text. This could be because your file encoding is wrong, or because all your words are single characters, among other things.");
    	System.out.println("Since cue.language can't find any words in your text, WordCram won't display any, but your Processing sketch will continue as normal.");
    	System.out.println("See https://github.com/danbernier/WordCram/issues/8 for more information.");
    }


    /**
     * If you're drawing the words one-at-a-time using {@link
     * #drawNext()}, this will tell you whether the WordCram has
     * any words left to draw.
     * @return true if the WordCram has any words left to draw; false otherwise.
     * @see #drawNext()
     */
    public boolean hasMore() {
        return getWordCramEngine().hasMore();
    }

    /**
     * If the WordCram has any more words to draw, draw the next
     * one.
     * @see #hasMore()
     * @see #drawAll()
     */
    public void drawNext() {
        getWordCramEngine().drawNext();
    }

    /**
     * Just like it sounds: draw all the words.  Once the WordCram
     * has everything set, call this and wait just a bit.
     * @see #drawNext()
     */
    public void drawAll() {
        getWordCramEngine().drawAll();
    }
    

    /**
     * Get the Words that WordCram is drawing. This can be useful
     * if you want to inspect exactly how the words were weighted,
     * or see how they were colored, fonted, sized, angled, or
     * placed, or why they were skipped.
     */
    public Word[] getWords() {
        Word[] wordsCopy = new Word[words.length];
        System.arraycopy(words, 0, wordsCopy, 0, words.length);
        return wordsCopy;
    }

    /**
     * Get the Word at the given (x,y) coordinates.
     *
     * <p>This can be called while the WordCram is rendering, or
     * after it's done.  If a Word is too small to render, or
     * hasn't been placed yet, it will never be returned by this
     * method.
     *
     * @param x the X coordinate
     * @param y the Y coordinate
     * @return the Word that covers those coordinates, or null if there isn't one
     */
    public Word getWordAt(float x, float y) {
        return getWordCramEngine().getWordAt(x, y);
    }

    /**
     * Returns an array of words that could not be placed.
     * @return An array of the skipped words
     */
    public Word[] getSkippedWords() {
        return getWordCramEngine().getSkippedWords();
    }

    /**
     * How far through the words are we? Useful for when drawing
     * to a custom PGraphics.
     * @return The current point of progress through the list, as a float between 0 and 1.
     */
    public float getProgress() {
        return getWordCramEngine().getProgress();
    }
    
    public WordCram withObserver(Observer observer) {
    	this.observer = observer;
    	return this;
    }
}
