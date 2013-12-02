package wordcram;

import java.awt.Shape;
import java.awt.geom.Area;

import processing.core.PVector;

public class ShapeBasedFilter implements WordPlaceFilter {

	Area area;
	
	public ShapeBasedFilter(Shape shape) {
		this.area = new Area(shape);
	}

	public boolean canFit(Word word) {
		PVector place = word.getTargetPlace();
		return area.contains(
				place.x, 
				place.y, 
				word.getRenderedWidth(), 
				word.getRenderedHeight()
				);
	}

}
