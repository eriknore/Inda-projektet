package Pong;

import org.newdawn.slick.Image;

/**
 * @author Erik Norell & Daniel Aceituno 
 * @version 2012-04-16
 */
public class Ball {
	
	private int xPosition, yPosition, speedX, speedY;
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
	public int getXPosition(){
		return xPosition;
	}
	
	/**
	 * Sets a new position to the ball on the x-axis
	 */
	public void moveX(int deltaX){
		xPosition += deltaX;
	}
	
	/**
	 * 
	 * @return The ball's position on the y-axis
	 */
	public int getYPosition(){
		return yPosition;
	}
	
	/**
	 * Sets a new position to the ball on the y-axis
	 */
	public void moveY(int deltaY){
		yPosition += deltaY;
		
	}
	
	/**
	 * @return Image of the ball
	 */
	public Image getImage() {
		return ballImage;
	}
	
}