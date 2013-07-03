package wordcram;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;


public class SVGDocumentRenderer extends AbstractGraphics2DRenderer {

	String fileName;
	
	public SVGDocumentRenderer(String fileName) {
		this.fileName = fileName;
		DOMImplementation domImpl = GenericDOMImplementation
				.getDOMImplementation();
		Document document = domImpl.createDocument(null, "svg", null);
		graphics = new SVGGraphics2D(document);
	}

	public void close() {
		OutputStream os;
		try {
			os = new FileOutputStream(fileName);
			Writer w = new OutputStreamWriter(os, "iso-8859-1");
			((SVGGraphics2D) graphics).stream(w, true);
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

}
