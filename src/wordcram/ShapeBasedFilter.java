package wordcram;

import java.awt.Shape;
import java.awt.geom.Area;

public class ShapeBasedFilter implements SketchFilter {

	Area area;
	float minY, minX, maxY, maxX;
	
	public ShapeBasedFilter(Shape shape) {
		this.area = new Area(shape);
	}

	public boolean filter(Word word) {
		return area.contains(
				(Float) word.getProperty("x"), 
				(Float) word.getProperty("y"), 
				new Float((Integer) word.getProperty("width")), 
				new Float((Integer) word.getProperty("height")));
	}

}
