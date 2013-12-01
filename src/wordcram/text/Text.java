package wordcram.text;

public class Text implements TextSource {

    private String text;

    public Text(String _text) {
        text = _text;
    }

    public String getText() {
        return text;
    }
}
