package wordcram;

import java.awt.Shape;
import java.awt.geom.Area;

public class ShapeBasedFilter implements SketchFilter {

	Area area;
	float minY, minX, maxY, maxX;
	
	public ShapeBasedFilter(Shape shape) {
		this.area = new Area(shape);
	}

	@Override
	public boolean filter(Word word) {
		return area.contains(
				(float) word.getProperty("x"), 
				(float) word.getProperty("y"), 
				new Float((int) word.getProperty("width")), 
				new Float((int) word.getProperty("height")));
	}

}
