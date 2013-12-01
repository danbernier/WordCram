package wordcram.text;

public class Html implements TextSource {

    private String src;

    public Html(String htmlSrc) {
        src = htmlSrc;
    }

    public String getText() {
        return new Html2Text().text(src, null);
    }
}
