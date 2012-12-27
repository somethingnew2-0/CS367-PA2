
/** 
 * An &lt;tt&gt;Image&lt;/tt&gt; object contains the name of the file containing the image 
 * and a positive integer indicating the length of time (duration) the image 
 * should be displayed in hundredths of a seconds (i.e., 1/100 is 1 
 * hundredth; 100 hundredths = 1).
 * 
 * &lt;b&gt;Do not modify this file in any way!&lt;/b&gt;
 */
public class Image {
	private String fileName;  // name of file containing the image
	private int duration;     // duration in hundredths of a seconds

	/**
	 * Creates a new &lt;tt&gt;Image&lt;/tt&gt; object with specified file name.  
	 * The durations is initially 0.
	 * @param fileName the name of the file containing the image.
	 */
	public Image(String fileName) {
		this.fileName = fileName;
		this.duration = 0;
	}
	
	/**
	 * Returns the file name associated with this image.
	 * @return the file associated with this image
	 */
	public String getFile() { 
		return fileName; 
	}
	
	/**
	 * Returns the duration for this image (in hundreths of seconds).
	 * @return the duration for this image (in hundreths of seconds)
	 */
	public int getDuration() { 
		return duration; 
	}
	
	/**
	 * Sets the duration for this image to the one given.
	 * @param duration the new duration (in hundreths of seconds) for this photo
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	/**
	 * Returns the string representation of this image in the form:
	 * &lt;p&gt;&lt;tt&gt;&lt;i&gt;file_name&lt;/i&gt; [&lt;i&gt;duration&lt;/i&gt;]&lt;/tt&gt;&lt;/p&gt;
	 * where the &lt;i&gt;duration&lt;/i&gt; is given in hundredths of seconds
	 * @return The string representation of this photo.
	 */
	public String toString() {
		return fileName + " [" + duration + "]";
	}
}