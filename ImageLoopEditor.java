///////////////////////////////////////////////////////////////////////////////
// Title:            ImageLoopEditor
// Files:            ImageLoopEditor.java, LinkedLoop.java,
//						LinkedLoopIterator.java, EmptyLoopException.java
// Semester:         Fall 2011
//
// Author:           Peter Collins pmcollins2@wisc.edu
// CS Login:         pcollins
// Lecturer's Name:  Beck, Hasti
// Lab Section:      NA
//
///////////////////////////////////////////////////////////////////////////////

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

/**
 * An application meant for managing images for a looping slideshow.
 * 
 * <p>
 * Bugs: none known
 * 
 * @author Peter Collins
 */
public class ImageLoopEditor {
	/**
	 * A private instance of our image linked loop which stays throughout the
	 * scope of the application.
	 */
	private static LinkedLoop<Image> imageLoop;

	/**
	 * Main method of the image loop editor. Controls the reading of the any
	 * input files, add new entries to the loop, and the managing of the loop
	 * via keyboard inputs or file inputs.
	 * 
	 * @param args
	 *            command line arguments, can only have one optional the input
	 *            file of the auto inputed keyboard commands to run.
	 */
	public static void main(String[] args) {
		// more than one arguments aren't allowed
		if (args.length > 1) {
			System.out.println("invalid command-line arguments");
			System.exit(1);
		}

		// command inputs either file or keyboard
		Scanner in = null;

		// if the optional argument was given open the file
		if (args.length == 1) {
			try {
				File inFile = new File(args[0]);
				if (!inFile.exists() || !inFile.canRead()) {
					System.out.println("problem with input file");
					System.exit(1);
				}

				in = new Scanner(inFile);
			} catch (FileNotFoundException e) {
				System.out.println("problem with input file");
				System.exit(1);
			}

		} else {
			// assuming 'in' has been declared as a Scanner
			in = new Scanner(System.in);
		}

		// instantiate the linked loop of images
		imageLoop = new LinkedLoop<Image>();

		// for reading console input
		boolean done = false;
		// regex for determining valid string inputs
		Pattern validInput = Pattern.compile("^[\\w\\.\\-]+$");
		while (!done) {
			// prompt for input
			System.out.print("Enter command (? for help)> ");
			String line = in.nextLine();
			// print the command if it was file input
			if (args.length == 1) {
				System.out.println(line);
			}

			// only do something if the user enters at least one character
			if (line.length() > 0) {
				char choice = line.charAt(0); // strip off option character
				String remainder = ""; // used to hold the remainder of line
				if (line.length() > 1) {
					// trim off any leading or trailing spaces
					remainder = line.substring(1).trim();
				}

				switch (choice) {
				case '?':
					// output help
					System.out
							.println("s (save)      l (load)       d (display)");
					System.out.println("f (forward)   b (backward)   j (jump)");
					System.out
							.println("r (remove)    a (add after)  i (insert before)");
					System.out.println("c (contains)  u (update)     x (exit)");
					break;
				case 's':
					// validate input string
					if (!validInput.matcher(remainder).matches()) {
						System.out.println("invalid command");
						break;
					}

					// check for empty loop
					if (imageLoop.size() == 0) {
						System.out.println("no images to save");
						break;
					}

					try {
						// write to given file name
						File outFile = new File(remainder);
						// warn if it exists
						if (outFile.exists()) {
							System.out
									.println("warning: file already exists, will be overwritten");
						}
						// stop if we can't write to the file
						if (outFile.exists() && !outFile.canWrite()) {
							System.out.println("unable to save");
							break;
						}

						// open the file to write to
						PrintStream ps = new PrintStream(outFile);

						// iterate through the images and write
						for (Image image : imageLoop) {
							ps.println(image.getFile() + " "
									+ image.getDuration());
						}

						// write to the file and save
						ps.close();
					} catch (FileNotFoundException e) {
						// catch if we can't write
						System.out.println("unable to save");
					}
					break;
				case 'l':
					// validate input string
					if (!validInput.matcher(remainder).matches()) {
						System.out.println("invalid command");
						break;
					}

					try {
						// read from give file name
						File inFile = new File(remainder);
						// validate it exists and is readible
						if (!inFile.exists() || !inFile.canRead()) {
							System.out.println("unable to load");
							break;
						}

						// remember our original size (or position in the loop)
						int origPos = imageLoop.size();

						// open the file for read
						Scanner load = new Scanner(inFile);

						// create loop variables for lines in the file, and the
						// temporary image
						// object we will construct
						String imageLine;
						String[] tokens;
						Image image;

						// start at the "end" of loop to append to
						imageLoop.previous();

						// iterate through the lines of the file
						while (load.hasNext()) {
							imageLine = load.nextLine(); // get the next line in
															// the file
							tokens = imageLine.split("\\s+"); // split the file
																// by whitespace
							image = new Image(tokens[0]); // the first token
															// string is the
															// filename
							image.setDuration(Integer.parseInt(tokens[1])); // the
																			// second
																			// token
																			// is
																			// duration

							imageLoop.next(); // go to the next image in the
												// loop since we want to append
							imageLoop.add(image); // add the newly constructed
													// image to the loop
						}

						// move back to the first image in the input file, over
						// the original items
						for (int i = 0; i <= origPos; i++) {
							imageLoop.next();
						}

						// close the file
						load.close();
					} catch (FileNotFoundException e) {
						// catch if we can't read the file
						System.out.println("unable to load");
					}
					break;
				case 'd':
					// if the loop is empty report it
					if (imageLoop.size() == 0) {
						System.out.println("no images");
					} else {
						// iterate through all images and display them
						for (Image image : imageLoop) {
							System.out.println(image.getFile() + " ["
									+ image.getDuration() + "]");
						}
					}
					break;
				case 'f':
					// if the loop is empty report it
					if (imageLoop.size() == 0) {
						System.out.println("no images");
					} else {
						// move forward one image
						imageLoop.next();
						// display the context of that image
						displayCurrentContext(imageLoop);
					}
					break;
				case 'b':
					// if the loop is empty report it
					if (imageLoop.size() == 0) {
						System.out.println("no images");
					} else {
						// move backward one image
						imageLoop.previous();
						// display the context of that image
						displayCurrentContext(imageLoop);
					}
					break;
				case 'j':
					// initialize the number of images to jump
					int numberToJump = 0;
					try {
						// parse to input
						numberToJump = Integer.parseInt(remainder);
						// report if there was no input as invalid
						if (remainder.isEmpty()) {
							System.out.println("invalid command");
							break;
						}
					} catch (NumberFormatException e) {
						// report if there was an error parsing the int
						System.out.println("invalid command");
						break;
					}

					// if the loop is empty report it
					if (imageLoop.size() == 0) {
						System.out.println("no images");
					} else {
						if (numberToJump >= 0) {
							// if the number to jump is positive go forward that
							// many images
							for (int i = 0; i < numberToJump; i++) {
								imageLoop.next();
							}
						} else {
							// if the number to jump is negative go backward
							// that many images
							for (int i = 0; i < -numberToJump; i++) {
								imageLoop.previous();
							}
						}
						// display the current context of jumped to image
						displayCurrentContext(imageLoop);
					}
					break;
				case 'r':
					try {
						// remove the current image
						imageLoop.removeCurrent();
						// display the current context of the next image
						displayCurrentContext(imageLoop);
					} catch (EmptyLoopException e) {
						// report if the loop was empty
						System.out.println("no images");
					}
					break;
				case 'a':
					// validate input string
					if (!validInput.matcher(remainder).matches()) {
						System.out.println("invalid command");
						break;
					}

					if (imageLoop.size() == 0) {
						// add new image if there aren't any
						imageLoop.add(new Image(remainder));
					} else {
						// move to next image to append
						imageLoop.next();
						// add new image to loop
						imageLoop.add(new Image(remainder));
					}
					// display the appended images' context
					displayCurrentContext(imageLoop);
					break;
				case 'i':
					// validate input string
					if (!validInput.matcher(remainder).matches()) {
						System.out.println("invalid command");
						break;
					}

					// prepend or add new image if there aren't any
					imageLoop.add(new Image(remainder));
					// display inserted images' context
					displayCurrentContext(imageLoop);
					break;
				case 'c':
					// validate input string
					if (!validInput.matcher(remainder).matches()) {
						System.out.println("invalid command");
						break;
					}

					try {
						// get current image loop size
						int totalImages = imageLoop.size();
						if (totalImages == 0) {
							// report if the loop is empty
							System.out.println("no images");
						} else {
							// iterate through the loop looking for next file
							// containing the input
							for (int i = 0; i < totalImages; i++) {
								imageLoop.next();
								if (imageLoop.getCurrent().getFile()
										.contains(remainder)) {
									// found image file containing search
									// display it's context
									displayCurrentContext(imageLoop);
									break;
								}
								if (i == totalImages - 1) {
									// Last image and still not found, report it
									System.out.println("not found");
								}
							}
						}
					} catch (EmptyLoopException e) {
						// report if the loop is empty
						System.out.println("no images");
					}
					break;
				case 'u':
					// the duration to set
					int duration = 0;
					try {
						// parse the duration as int
						duration = Integer.parseInt(remainder);
						// verify the value isn't empty and is positive
						if (remainder.isEmpty() || duration < 0) {
							System.out.println("invalid command");
							break;
						}
					} catch (NumberFormatException e) {
						// catch if the input is a valid int
						System.out.println("invalid command");
						break;
					}

					try {
						// set the duration of the current image
						imageLoop.getCurrent().setDuration(duration);
						// display the current image context then
						displayCurrentContext(imageLoop);
					} catch (EmptyLoopException e) {
						// report if there are any images in the loop
						System.out.println("no images");
					}
					break;
				case 'x':
					done = true;
					System.out.println("exit");
					break;

				default: // ignore any unknown commands
					break;

				} // end switch
			} // end if
		} // end while
		in.close(); // close the scanner when done
	} // end main

	/**
	 * Displays the current image context to stdout.
	 */
	private static void displayCurrentContext(LinkedLoop<Image> linkedLoop) {
		Image image;
		try {
			if (imageLoop.size() == 1) {
				// display the only image if it's the only one
				image = imageLoop.getCurrent();
				System.out.println("--> " + image.getFile() + " ["
						+ image.getDuration() + "] <--");
			} else if (imageLoop.size() == 2) {
				// display the two images: current first, next second, if there
				// are only two
				image = imageLoop.getCurrent();
				System.out.println("--> " + image.getFile() + " ["
						+ image.getDuration() + "] <--");
				imageLoop.next();
				image = imageLoop.getCurrent();
				System.out.println("    " + image.getFile() + " ["
						+ image.getDuration() + "]");
				imageLoop.previous(); // return to current image
			} else if (imageLoop.size() >= 3) {
				// display three images: previous first, current second, next
				// third, if there are three or more
				imageLoop.previous(); // go to previous to display
				image = imageLoop.getCurrent();
				System.out.println("    " + image.getFile() + " ["
						+ image.getDuration() + "]");
				imageLoop.next();
				image = imageLoop.getCurrent();
				System.out.println("--> " + image.getFile() + " ["
						+ image.getDuration() + "] <--");
				imageLoop.next();
				image = imageLoop.getCurrent();
				System.out.println("    " + image.getFile() + " ["
						+ image.getDuration() + "]");
				imageLoop.previous(); // return to current image
			}
		} catch (EmptyLoopException e) {
			// report if the loop is empty
			System.out.println("no images");
		}
	}
}