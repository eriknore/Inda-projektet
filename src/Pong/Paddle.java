package Pong;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * 
 * @author Erik Norell & Daniel Aceituno
 * @version 2012-04-16
 */
public class Paddle {

	private int yPosition, xPosition, frameHeight, AIdelay = 0;
	private Image paddleImage;
	// minimum output-angle from paddle in degrees (x and 180-x)
	private final int angle = 30, paddleSpeed = 3;
	private boolean isHuman;

	/**
	 * Creates a paddle
	 * 
	 * @param yCoordinate
	 *            the Y-position of upper left corner at which the paddle is
	 *            drawn
	 * @throws SlickException 
	 */
	public Paddle(int frameHeight, int xPosition, boolean humanPlayer) throws SlickException {
		this.frameHeight = frameHeight;
		paddleImage = new Image("data/paddles/paddle.png");
		yPosition = (frameHeight-paddleImage.getHeight())/2;
		this.xPosition = xPosition;
		isHuman = humanPlayer;
		
	}

	/**
	 * Returns the y-coordinate
	 * 
	 * @return y-position
	 */
	public int getY() {
		return yPosition;
	}

	public int getX() {
		return xPosition;
	}
	
	/**
	 *  Moves the paddle up
	 */
	public void paddleUp(float timeDelta) {
		float delta = timeDelta*paddleSpeed;
		if(yPosition <= 0)
			yPosition += delta; // stops paddle from exiting screen
		yPosition -= delta;
	}

	/**
	 *  Moves the paddle down
	 */
	public void paddleDown(float timeDelta) {
		float delta = timeDelta*paddleSpeed;
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
	
	public int getAngle() {
		return angle;
	}
	
	public void getAIMovement(Ball ball, float timeDelta) {
		int ballPosition = ball.getYPosition();
		int paddleCenter = yPosition + paddleImage.getHeight()/2;
		if(ball.isServingLeft()) {
			if(AIdelay > 40) {
				ball.servedLeft();
				AIdelay = 0;
				return;
			}
			yPosition -= timeDelta*paddleSpeed;
			AIdelay++;
		} else if(ballPosition - 5 > paddleCenter) {
			yPosition += timeDelta*paddleSpeed;
		} else if(ballPosition + 5 < paddleCenter){
			yPosition -= timeDelta*paddleSpeed;
		}
	}
	
	public boolean isHuman() {
		return isHuman;
	}
}