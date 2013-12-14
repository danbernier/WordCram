package wordcram.animation.svg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLContentHandler extends DefaultHandler {

	private int fileIdx = 0;
	
	private HashMap<String, List<WordInfo>> wordInfoMap = new HashMap<String, List<WordInfo>>();
	private HashMap<String, String> pathMap = new HashMap<String, String>();
	
	private String lastId = "";
	private List<WordInfo> lastInfos ;

	private long startDocument;


	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		
	}

	@Override
	public void endDocument() throws SAXException {
		System.err.println("end...");
		for(String word : wordInfoMap.keySet())
		{
			List<WordInfo> infos = wordInfoMap.get(word);
			WordInfo info = infos.get(infos.size() - 1);
			while(infos.size() < (fileIdx))
			{
				WordInfo clone = info.copy();
				clone.setOpacity(0);
				infos.add(clone);
				lastInfos.add(clone);
			}
		}
		
		System.err.println((System.currentTimeMillis() - startDocument)+"ms");
		
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		
	}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
		
	}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
		
	}

	@Override
	public void processingInstruction(String target, String data) throws SAXException {
		
	}

	@Override
	public void setDocumentLocator(Locator locator) {
		
	}

	@Override
	public void skippedEntity(String name) throws SAXException {
		
	}

	@Override
	public void startDocument() throws SAXException {
		fileIdx++;
		startDocument = System.currentTimeMillis();
		lastId = "";
		if(lastInfos == null)
			lastInfos = new ArrayList<WordInfo>();
		else
			lastInfos.clear();
		
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		
		if(qName == "g")
		{
			lastId = atts.getValue("id");
			List<WordInfo> infos = wordInfoMap.get(lastId);
			
			if(infos == null)
			{
				infos = new ArrayList<WordInfo>();
				wordInfoMap.put(lastId, infos);
			}
			
			WordInfo info = new WordInfo(lastId);
			info.addStyle(atts);
			while(infos.size() < (fileIdx - 1))
			{
				WordInfo clone = info.copy();
				clone.setOpacity(0);
				infos.add(clone);
				lastInfos.add(clone);
			}
			
			lastInfos.add(info);
			infos.add(info);
		}
		else if(qName == "path")
		{
			String path = atts.getValue("d");
			if(!pathMap.containsKey(lastId))
			{
				pathMap.put(lastId, path);
			}
			
			for(WordInfo info : lastInfos)
			{
				info.setPath(path);
			}
			
			lastInfos.clear();
		}
		
	}

	@Override
	public void startPrefixMapping(String prefix, String uri) throws SAXException {
		
	}

	public final HashMap<String, List<WordInfo>> getWordInfoMap() {
		return wordInfoMap;
	}

	public final HashMap<String, String> getPathMap() {
		return pathMap;
	}
}
