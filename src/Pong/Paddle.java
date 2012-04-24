package Pong;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * 
 * @author Erik Norell & Daniel Aceituno
 * @version 2012-04-16
 */
public class Paddle {

	private int yPosition, xPosition, frameHeight, AIdelay = 0, goal = 300;
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
	public void paddleUp() {
		if(yPosition <= 0)
			yPosition += paddleSpeed; // stops paddle from exiting screen
		yPosition -= paddleSpeed;
	}

	/**
	 *  Moves the paddle down
	 */
	public void paddleDown() {
		if(yPosition >= frameHeight-paddleImage.getHeight())
			yPosition -= paddleSpeed; // stops paddle from exiting screen
		yPosition += paddleSpeed;
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
	
//	public void getAIMovement(Ball ball, float timeDelta, Paddle otherPaddle) {
//		int ballPosition = ball.getYPosition();
//		int paddleCenter = yPosition + paddleImage.getHeight()/2;
//		int middle = PongGame.getWidth()/2;
//		if(yPosition < middle) {
//			if(isHuman && ball.isServingLeft()) {
//				if(AIdelay > 20) {
//					ball.serveLeft(this);
//					AIdelay = 0;
//					return;
//				}
//				yPosition -= timeDelta*paddleSpeed;
//				AIdelay++;
//				return;
//			}
//		} else if(yPosition > middle) {
//			if(isHuman && ball.isServingRight()) {
//				if(AIdelay > 20) {
//					ball.serveRight(this);
//					AIdelay = 0;
//					return;
//				}
//				yPosition -= timeDelta*paddleSpeed;
//				AIdelay++;
//				return;
//			}
//		}
//		if(ballPosition + 5 > paddleCenter - 30)
//			yPosition += timeDelta*paddleSpeed;
//		if(ballPosition - 5 < paddleCenter - 30)
//			yPosition -= timeDelta*paddleSpeed;
//		
//		if(ball.getDeltaX() > 0)
//			goal = calculateBall(ball);
//		if(yPosition > goal) {
//			yPosition -= timeDelta*paddleSpeed;
//		} else if(yPosition < goal) {
//			yPosition += timeDelta*paddleSpeed;
//		}
//	}
	
	public boolean isHuman() {
		return isHuman;
	}
	
	private int calculateBall(Ball ball) {
		double ballY = ball.getYPosition();
		double ballDeltaY = ball.getDeltaY();
		double ballX = ball.getXPosition();
		double ballDeltaX = ball.getDeltaX();
		double totalDistance;
		if(ballDeltaY == 0) {
			totalDistance = ballX*ballDeltaX;
		} else {
			totalDistance = ballX*ballDeltaX/ballDeltaY;
		}
		double distanceToWall = frameHeight-ballY+ball.getImage().getWidth();
		totalDistance =- distanceToWall;
		int height = PongGame.getHeight();
		while(totalDistance > height) {
			totalDistance -= height;
		//	oddsOrEven++;
		}
		Double toReturn = totalDistance;
		return toReturn.intValue();
	}
	
	public void setGoal(int goal) {
		this.goal = goal;
	}
	
	public int getGoal() {
		return goal;
	}
}