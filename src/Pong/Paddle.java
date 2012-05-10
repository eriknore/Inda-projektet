package Pong;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * 
 * @author Erik Norell & Daniel Aceituno
 * @version 2012-05-10
 */
/**
 * @author Erik
 *
 */
public class Paddle {
 
	private int[] coordinates = {0,0}; // (x, y)
	private int[] dimensions = {0,0}; // (width, height)
	// goal is the y-coordinate the AI-paddle should aim for
	private int goal = 0, paddleSpeed = 4;
	// rangeAI is range from paddle center, used to determine how 
	// far from center the ball will hit the paddle if paddle is AI
	private int rangeAI = 2;
	private Image paddleImage;
	// minimum output-angle from paddle in degrees (0+angle and 180-angle)
	private final int angle = 30;
	private boolean isHuman;

	/**
	 * Creates a paddle
	 * @param xPosition where on x-axis the left border of a paddle is
	 * @param humanPlayer if player/paddle is human true, if AI false
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
	
	/**
	 * Returns the current coordinates of paddle in form of an int[] of size 2,
	 * where x-coordinate is [0] and y-coordinate [1]
	 * @return int[0] = x-coordinate, int[1] = y-coordinate
	 */
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
	
	/**
	 * Returns the preset maximum output angle of the ball after colliding
	 * with a paddle. On y-axis it's 0+angle and 180-angle
	 * @return The maximum output angle of ball
	 */
	public int getAngle() {
		return angle;
	}
		
	/**
	 * Returns true if player/paddle is controlled by user. Returns false 
	 * if controlled by AI
	 * @return true if player/paddle is human, if AI false
	 */
	public boolean isHuman() {
		return isHuman;
	}
	

	/**
	 * @param goal
	 */
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

	public void shrinkPaddle() throws SlickException {
		paddleImage = new Image("data/paddles/smallpaddle.png");
		dimensions[0] = paddleImage.getWidth();
		dimensions[1] = paddleImage.getHeight();
	}
	
	public void enlargePaddle() throws SlickException {
		paddleImage = new Image("data/paddles/largepaddle.png");
		dimensions[0] = paddleImage.getWidth();
		dimensions[1] = paddleImage.getHeight();
	}
	
	public void resetPaddle() throws SlickException {
		paddleImage = new Image("data/paddles/paddle.png");
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