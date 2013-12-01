package wordcram.text;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;

class Html2Text {
	public String text(String html, String cssSelector) {
		Document doc = Jsoup.parse(html);

		if (cssSelector == null) {
			return doc.text();
		}
		else {
			return doc.select(cssSelector).text();
		}
	}
}
