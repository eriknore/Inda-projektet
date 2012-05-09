package Pong;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * 
 * @author Erik Norell & Daniel Aceituno
 * @version 2012-04-16
 */
public class Paddle {

	// yPosition, xPosition, 
	private int[] coordinates = {0,0}; // (x, y)
	private int[] dimensions = {0,0}; // (width, height)
	private int goal = 0, paddleSpeed = 4, rangeAI = 2;
	private Image paddleImage;
	// minimum output-angle from paddle in degrees (x and 180-x)
	private final int angle = 30;
	private boolean isHuman;

	/**
	 * Creates a paddle
	 * 
	 * @param yCoordinate
	 *            the Y-position of upper left corner at which the paddle is
	 *            drawn
	 * @throws SlickException 
	 */
	public Paddle(int xPosition, boolean humanPlayer) throws SlickException {
		paddleImage = new Image("data/paddles/paddle.png");
		dimensions[0] = paddleImage.getWidth();
		dimensions[1] = paddleImage.getHeight();
		coordinates[0] = xPosition;
		coordinates[1] = (Settings.getFrameHeight()-dimensions[1])/2;
		isHuman = humanPlayer;
		
		if(!isHuman && Settings.getDifficulty().equals("Easy"))
			paddleSpeed = 2;
	}

//	/**
//	 * Returns the y-coordinate
//	 * 
//	 * @return y-position
//	 */
//	public int getY() {
//		return yPosition;
//	}
//
//	public int getX() {
//		return xPosition;
//	}
	
	public int[] getCoordinates() {
		return coordinates;
	}
	
	/**
	 *  Moves the paddle up
	 */
	public void paddleUp() {
		if(coordinates[1] <= 0)
			coordinates[1] += paddleSpeed; // stops paddle from exiting screen
		coordinates[1] -= paddleSpeed;
	}

	/**
	 *  Moves the paddle down
	 */
	public void paddleDown() {
		if(coordinates[1] >= Settings.getFrameHeight()-dimensions[1])
			coordinates[1] -= paddleSpeed; // stops paddle from exiting screen
		coordinates[1] += paddleSpeed;
	}
	
	/**
	 * Returns the image of the paddle
	 * 
	 * @return Image of the paddle
	 */
	public Image getImage() {
		return paddleImage;
	}
	
	public int getAngle() {
		return angle;
	}
		
	public boolean isHuman() {
		return isHuman;
	}
	

	public void setGoal(int goal) {
		this.goal = goal;
	}
	
	public int getGoal() {
		return goal;
	}
	
	public void setRange(int range) {
		if(range < 2) {
			rangeAI = 2;
			return;
		}
		rangeAI = range;
	}
	
	public int getRange() {
		return rangeAI;
	}
	
	public void serve(Ball ball) {
		if(ball.isServingLeft()) {
			ball.serve(1);
		} else {
			ball.serve(-1);
		}
	}

	public void shrinkPaddle() throws SlickException{
		paddleImage = new Image("data/paddles/smallpaddle.png");
		dimensions[0] = paddleImage.getWidth();
		dimensions[1] = paddleImage.getHeight();
	}
	
	public void enlargePaddle() throws SlickException{
		paddleImage = new Image("data/paddles/largepaddle.png");
		dimensions[0] = paddleImage.getWidth();
		dimensions[1] = paddleImage.getHeight();
	}
	
	public void setPaddleSpeed(int newSpeed) {
		paddleSpeed = newSpeed;
	}
	
	public boolean isLeft() {
		return (coordinates[0] < Settings.getFrameWidth()/2);
	}
	
	public int getWidth() {
		return dimensions[0];
	}
	
	public int getHeight() {
		return dimensions[1];
	}
}