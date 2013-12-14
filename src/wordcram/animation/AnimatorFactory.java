package wordcram.animation;

import wordcram.animation.svg.SVGAnimator;

public enum AnimatorFactory {
	INSTANCE;
	
	public static enum Animators{
		SVGAnimator
	}
	
	public static IAnimator createAnimator(Animators name)
	{
		switch(name)
		{
			case SVGAnimator:
				return new SVGAnimator();
				
			default:
				return new SVGAnimator();
		}
	}
}	
