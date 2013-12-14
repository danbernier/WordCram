package wordcram.animation;

import static org.junit.Assert.fail;

import java.io.File;

import org.junit.Test;

import wordcram.animation.AnimatorFactory;
import wordcram.animation.IAnimator;
import wordcram.animation.AnimatorFactory.Animators;
import wordcram.animation.svg.SVGAnimator;

public class TestSVGAnimator {

	@Test
	public void test() {
		File folder = new File("svgs");
		
		IAnimator animator = AnimatorFactory.createAnimator(Animators.SVGAnimator);
		
		try {
			animator.addFolder(folder)
			.setTransition(1f)
			.setDelayBetweenFiles(2f)
			.toFile(new File("animatedSVG.svg"));
		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}
