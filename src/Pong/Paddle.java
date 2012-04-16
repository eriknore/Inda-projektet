package Pong;

import org.newdawn.slick.Image;

/**
 * 
 * @author Erik Norell & Daniel Aceituno
 * @version 2012-04-16
 */
public class Paddle {

	private int yPosition, frameHeight;
	private Image paddleImage;

	/**
	 * Creates a paddle
	 * 
	 * @param yCoordinate
	 *            the Y-position of upper left corner at which the paddle is
	 *            drawn
	 */
	public Paddle(int yCoordinate, int frameHeight, Image imageOfPaddle) {
		yPosition = yCoordinate;
		this.frameHeight = frameHeight;
		paddleImage = imageOfPaddle;
		
	}

	/**
	 * Returns the y-coordinate
	 * 
	 * @return y-position
	 */
	public int getY() {
		return yPosition;
	}

	/**
	 *  Moves the paddle up
	 */
	public void paddleUp(int delta) {
		if(yPosition <= 0)
			yPosition += delta; // stops paddle from exiting screen
		yPosition -= delta;
	}

	/**
	 *  Moves the paddle down
	 */
	public void paddleDown(int delta) {
		if(yPosition >= frameHeight-paddleImage.getHeight())
			yPosition -= delta; // stops paddle from exiting screen
		yPosition += delta;
	}
	
	/**
	 * Returns the image of the paddle
	 * 
	 * @return Image of the paddle
	 */
	public Image getImage() {
		return paddleImage;
	}
}