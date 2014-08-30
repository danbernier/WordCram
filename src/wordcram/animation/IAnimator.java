package wordcram.animation;

import java.io.File;
import java.io.IOException;

public interface IAnimator {

	/**
	 * Set how long a motion from one file to another file takes
	 * @param seconds 
	 * @return
	 */
	public IAnimator setTransition(float seconds);
	
	/**
	 * Set how long to stop between a motion and another motion
	 * @param seconds
	 * @return
	 */
	public IAnimator setDelayBetweenFiles(float seconds);
	
	/**
	 * Add a file to be parsed for the final motion
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public IAnimator addFile(File file) throws IOException;
	
	/**
	 * Add all files from specified folder to be parsed for the final motion
	 * @param folder
	 * @return
	 * @throws IOException
	 */
	public IAnimator addFolder(File folder) throws IOException;
	
	/**
	 * Generate the final animation
	 * @param outputFile
	 * @throws IOException
	 */
	public void toFile(File outputFile) throws IOException;
}
