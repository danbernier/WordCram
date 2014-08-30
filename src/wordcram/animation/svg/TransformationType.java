package wordcram.animation.svg;

public enum TransformationType {
	SCALE, ROTATE, TRANSLATE;
	
	public String toString()
	{
		return this.name().toLowerCase();
	}
}
