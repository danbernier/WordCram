package wordcram.renderer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import wordcram.EngineWord;
import wordcram.WordColorer;

public class SVGDocumentRenderer implements Renderer {

	String fileName;
	SVGGraphics2D svgGenerator;
	WordColorer colorer;

	public SVGDocumentRenderer(String fileName) {
		this.fileName = fileName;
		DOMImplementation domImpl = GenericDOMImplementation
				.getDOMImplementation();
		Document document = domImpl.createDocument(null, "svg", null);
		svgGenerator = new SVGGraphics2D(document);
	}

	public void close() {
		OutputStream os;
		try {
			os = new FileOutputStream(fileName);
			Writer w = new OutputStreamWriter(os, "iso-8859-1");
			svgGenerator.stream(w, true);
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public int getWidth() {
		return 1000;
	}

	public int getHeight() {
		return 1000;
	}

	public void setColorer(WordColorer colorer) {
		this.colorer = colorer;
	}

	public void drawEngineWord(EngineWord eWord) {
		GeneralPath path2d = new GeneralPath(eWord.getShape());
		Graphics2D g2 = svgGenerator;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setPaint(new Color(eWord.getWord().getColor(colorer), true));
		g2.fill(path2d);
	}

}
