package wordcram.animation.svg;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.dom4j.Element;
import org.xml.sax.Attributes;

public class WordInfo implements Cloneable {
	private String id;
	private Map<String, String> style;
	private String path;
	private float opacity;
	
	public WordInfo (String id)
	{
		this.id = id;
		this.opacity = 1;
		this.style = new HashMap<String, String>();
	}
	
	public void addStyle(Attributes atts)
	{
		String style = atts.getValue("style");
		String[] keyValueSplit = style.replaceAll("\\s+","").split(";");
		for(String keyValue : keyValueSplit)
		{
			String[] split = keyValue.split(":");
			String key = split[0].toLowerCase();
			String value = split[1];
			
			addStyle(key, value);
		}	
	}

	public Element attachTo(Element element) {
		element.addAttribute("id", id);
		element.addAttribute("style", styleAsString());
		
		return element;
	}
	
	private String styleAsString() {
		StringBuffer style = new StringBuffer();
		for(Entry<String,String> entry : this.style.entrySet())
		{
			style.append(entry.getKey()+": "+entry.getValue()+";");
		}
		
		return style.toString();
	}

	public final String addStyle(String key, String value)
	{
		key = key.toLowerCase();
		return style.put(key, value);
	}
	
	public final int getStyleAsInteger(String style)
	{
		style = style.toLowerCase();
		String value = this.style.get(style);
		if(value == null)
			return -1;
		
		return Integer.parseInt(value);
	}
	

	public final void setOpacity(float opacity) {
		this.opacity = opacity;
	}
	
	public final float getOpacity()
	{
		return this.opacity;
	}
	
	public final String getStyleAsString(String style)
	{
		style = style.toLowerCase();
		String value = this.style.get(style);
		if(value == null)
			return "";
		
		return value;
	}
	
	public final float getStyleAsFloat(String style)
	{
		style = style.toLowerCase();
		String value = this.style.get(style);
		if(value == null)
			return -1;
		
		return Float.parseFloat(value);
	}
	
	public final double getStyleAsDouble(String style)
	{
		style = style.toLowerCase();
		String value = this.style.get(style);
		if(value == null)
			return -1;
		
		return Double.parseDouble(value);
	}


	public String getId() {
		return id;
	}
	
	public WordInfo copy()
	{
		return (WordInfo) clone();
	}
	

	public String getPath() {
		return path;
	}
	

	public void setPath(String path) {
		this.path = path;
	}
	
	@Override 
	public Object clone()
	{
		WordInfo info = new WordInfo(id);
		info.style.putAll(style);
		info.path = path;
		info.opacity = opacity;
		return info;
	}



	
}
