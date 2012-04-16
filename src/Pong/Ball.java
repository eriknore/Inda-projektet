package Pong;

import org.newdawn.slick.Image;

/**
 * @author Erik Norell & Daniel Aceituno 
 * @version 2012-04-16
 */
public class Ball {
	
	private int xPosition, yPosition;
	private Image ballImage;
	
	public Ball(int xCoordinate, int yCoordinate, Image imageOfBall) {
		xPosition = xCoordinate;
		yPosition = yCoordinate;
		ballImage = imageOfBall;
	}
	
	/**
	 * 
	 * @return The ball's position on the x-axis
	 */
	public int getNewXPosition(){
		return xPosition;
	}
	
	/**
	 * Sets a new position to the ball on the x-axis
	 */
	public void setNewXposition(int xCoordinate){
		xPosition = xCoordinate;
	}
	
	/**
	 * 
	 * @return The ball's position on the y-axis
	 */
	public int getNewYPosition(){
		return yPosition;
	}
	
	/**
	 * Sets a new position to the ball on the y-axis
	 */
	public void setNewYposition(int yCoordinate){
		yPosition = yCoordinate;
		
	}
	
	/**
	 * @return Image of the ball
	 */
	public Image getImage() {
		return ballImage;
	}
	
}