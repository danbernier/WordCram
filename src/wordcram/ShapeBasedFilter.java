package wordcram;

import java.awt.Shape;
import java.awt.geom.Area;

public class ShapeBasedFilter implements WordPlaceFilter {

	Area area;
	
	public ShapeBasedFilter(Shape shape) {
		this.area = new Area(shape);
	}

	public boolean canFit(Word word) {
		return area.contains(
				(Float) word.getProperty("x"), 
				(Float) word.getProperty("y"), 
				word.getRenderedWidth(), 
				word.getRenderedHeight()
				);
	}

}
