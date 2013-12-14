package wordcram.animation.svg;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import wordcram.animation.IAnimator;

public class SVGAnimator implements IAnimator {

	private static FilenameFilter filter = new FilenameFilter() {

		@Override
		public boolean accept(File arg0, String arg1) {
			return arg1.endsWith("svg");
		}

	};

	private List<File> files = new ArrayList<File>();

	private float transition = 4f;
	private float waitingTime = 2f;

	private HashMap<String, List<WordInfo>> wordInfoMap;

	private HashMap<String, String> pathMap;

	private XMLContentHandler handler;

	public SVGAnimator() {

	}

	@Override
	public IAnimator setTransition(float f) {
		this.transition = (f <= 0) ? 0.5f : f;
		return this;
	}
	
	@Override
	public IAnimator setDelayBetweenFiles(float seconds) {
		this.waitingTime = (seconds < 0) ? 0 : seconds;
		return this;
	}

	@Override
	public IAnimator addFile(File file) throws IOException {
		files.add(file);
		return this;
	}

	@Override
	public IAnimator addFolder(File folder) throws IOException {
		if (folder == null || !folder.exists() || !folder.isDirectory())
			throw new IOException("The folder " + ((folder == null) ? "null" : folder.getAbsolutePath()) + " does not exist or is not a directory");

		for (File f : folder.listFiles()) {
			if (!filter.accept(f.getParentFile(), f.getName()))
				continue;

			files.add(f);
		}
		return this;
	}

	@Override
	public void toFile(File svgFile) throws IOException {
		
		Document doc = DocumentHelper.createDocument();
		doc.addDocType("svg", "-//W3C//DTD SVG 1.0//EN", "http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd");

		Element root = doc.addElement("svg");
		root.addNamespace("", "http://www.w3.org/2000/svg");
		root.addNamespace("xlink", "http://www.w3.org/1999/xlink");
		
		
		readFiles();
		

		wordInfoMap = handler.getWordInfoMap();
		pathMap = handler.getPathMap();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(svgFile));
		float opacityTransition = 0.5f;
		for (String word : wordInfoMap.keySet()) {
			List<WordInfo> infos = wordInfoMap.get(word);
			String pathStr = pathMap.get(word);
			Element path = null;
			Element group = null;
			Element transformation;
			int wordInfoIdx = 0;
			WordInfo previous = null;
			for (WordInfo info : infos) {

				if (group == null) {
					group =  root.addElement("g",  "http://www.w3.org/2000/svg");
					info.attachTo(group);
					path = group.addElement("path");
					path.addAttribute("d", pathStr);
					previous = info;
					continue;
				}

				float begin = (wordInfoIdx * (this.transition+this.waitingTime)) + this.waitingTime;
				

				//Add Special Animation so that every words that need to be masked from the
				//first animation are masked;
				if(wordInfoIdx == 0)
				{
					transformation = doAnimate(path, "opacity", 0, 0);
					transformation.addAttribute("from", previous.getOpacity() + "");
					transformation.addAttribute("to", previous.getOpacity() + "");
				}
				
				if (previous.getOpacity() != info.getOpacity()) {

					transformation = doAnimate(path, "opacity", begin, opacityTransition);
					
					transformation.addAttribute("from", previous.getOpacity() + "");
					transformation.addAttribute("to", info.getOpacity() + "");
				}

				if (!previous.getPath().equals(info.getPath())) {
					transformation = doAnimate(path, "d", begin, transition);
					transformation.addAttribute("to", info.getPath());
				}

				if (!previous.getStyleAsString("fill").equals(info.getStyleAsString("fill"))) {
					transformation = doAnimateColor(path, "fill", begin, transition);
					transformation.addAttribute("to", info.getStyleAsString("fill"));
				}

				

				previous = info;
				wordInfoIdx++;

			}
		}

		writer.write("<"+doc.asXML().substring(1).replaceAll("<", "\n<"));
		writer.close();
	}


	/**
	 * @param path
	 * @param begin
	 * @return
	 */
	private Element doAnimate(Element path, String attributeName, float begin, float duration) {
		Element transformation;
		transformation = path.addElement("animate");
		transformation.addAttribute("begin", begin + "s");
		if(duration > 0)
			transformation.addAttribute("dur", duration + "s");
		else
			transformation.addAttribute("dur", "0.01s");
		
		transformation.addAttribute("attributeName", attributeName);
		transformation.addAttribute("fill", "freeze");

		return transformation;
	}

	/**
	 * @param path
	 * @param begin
	 * @return
	 */
	private Element doAnimateColor(Element path, String attributeName, float begin, float duration) {
		Element transformation;
		transformation = path.addElement("animateColor");
		transformation.addAttribute("begin", begin + "s");
		transformation.addAttribute("dur", duration + "s");
		transformation.addAttribute("attributeName", attributeName);
		transformation.addAttribute("fill", "freeze");

		return transformation;
	}

	private void readFiles()  {

		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(false);
		SAXParser parser = null;
		
		try {
			parser = factory.newSAXParser();
		}
		catch (Exception e1) {
			e1.printStackTrace();
			return;
		}
		
		handler = new XMLContentHandler();

		for (File f : files) {
			try {
				parser.parse(f, handler);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
