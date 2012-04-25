package Pong;

import java.util.Random;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


/**
 * @author Erik Norell & Daniel Aceituno 
 * @version 2012-04-16
 */
public class Ball {

	private int xPosition, yPosition, diameter;
	private double deltaX, deltaY, ballSpeed;
	private Image ballImage;
	private boolean isServingLeft = false, isServingRight = false;

	public Ball() throws SlickException {
		ballImage = new Image("data/ball/default.png");
		diameter = ballImage.getWidth();
		ballSpeed = 8;

		// simulate a coinflip to decide which player to serve
		Random rand = new Random();
		int coinFlip = 1+rand.nextInt(99);
		if(coinFlip > 50) {
			isServingLeft = true;
		} else {
			isServingRight = true;
		}

	}

	/**
	 * 
	 * @return The ball's position on the x-axis
	 */
	public int getXPosition(){
		return xPosition;
	}

	/**
	 * 
	 * @return The ball's position on the y-axis
	 */
	public int getYPosition(){
		return yPosition;
	}

	/**
	 * @return Image of the ball
	 */
	public Image getImage() {
		return ballImage;
	}

	/**
	 * @param paddleLeft
	 * @param paddleRight
	 * @param topWall
	 * @param bottomWall
	 * @return
	 */
	public void moveBall(Paddle left, Paddle right) {
		if(isServingLeft) {
			xPosition = left.getX() + left.getImage().getWidth();
			yPosition = left.getY() + (left.getImage().getHeight()-diameter)/2;
			return;
		}
		if(isServingRight) {
			xPosition = right.getX() - diameter;
			yPosition = right.getY() +(right.getImage().getHeight()-diameter)/2;
			return;
		}
		checkWalls();
		checkPaddles(left, right);
		yPosition += deltaY;
		xPosition += deltaX;
		return;
	}

	private void checkWalls() {
		if(yPosition <= 0) {
			yPosition = 1;
			deltaY = -deltaY;
			if(deltaY == 0)
				deltaY = 1;
		}
		if(yPosition + diameter >= Settings.getFrameHeight()) {
			yPosition = Settings.getFrameHeight() - diameter -1;
			deltaY = -deltaY;
			if(deltaY == 0)
				deltaY = -1;
		}
	}

	private void checkPaddles(Paddle left, Paddle right) {
		int edgeOfPaddle = left.getX() + left.getImage().getWidth();
		// if ball is to the left of left paddle in relation to X-position...
		if(xPosition + deltaX <= edgeOfPaddle && xPosition + diameter + deltaX >= left.getX()) {
			int leftY = left.getY();
			// ... then check if it is hitting the paddle or not
			if(yPosition <= leftY + left.getImage().getHeight() && yPosition + diameter >= leftY) {
				xPosition = edgeOfPaddle;
				double angle = getAngleFromPaddle(left);
				deltaY = ballSpeed*Math.sin(angle);
				deltaX = ballSpeed*Math.cos(angle);
			}
		}
		edgeOfPaddle = right.getX();
		// if ball is to the right of right paddle in relation to X-position...
		if(xPosition + diameter + deltaX >= edgeOfPaddle && xPosition + deltaX <= edgeOfPaddle + right.getImage().getWidth()) {
			int rightY = right.getY();
			// ... then check if it is hitting the paddle or not
			if(yPosition <= rightY + right.getImage().getHeight() && yPosition + diameter >= rightY) {
				xPosition = edgeOfPaddle - diameter;
				double angle = getAngleFromPaddle(right);
				deltaY = ballSpeed*Math.sin(angle);
				deltaX = -ballSpeed*Math.cos(angle);
			}
		}
	}
	
	private double getAngleFromPaddle(Paddle paddle) {
		int paddleY = paddle.getY();
		// ball relative to paddle, value is where on paddle the center of ball is
		double ballValue = (yPosition-(paddleY-diameter)); 	
		// the angle at which the ball is returned with angle in relation to Y-axel (default angle is 30)
		double returnAngle = (90-paddle.getAngle()); // i.e. 60 degrees from X-axel and up to (Y-axel+30)
		// half the paddle-height including ball-radius at both ends of paddle
		double halfPaddleHeight = (paddle.getImage().getHeight() + diameter)/2; // default: 120/2
		// set a ratio between angle and paddle-size
		double angleHeightRatio = returnAngle/halfPaddleHeight; // default: 60 degrees/60 pixels
		double angle;
		if(yPosition+diameter/2 < paddleY+halfPaddleHeight) { // above paddle-center
			// newYSpeed = 60 degrees - ([max.ballValue=60, min=1] * [60 degrees/60 pixels])
			// 1<= newYSpeed <= 60
			angle = -Math.toRadians(returnAngle-(ballValue*angleHeightRatio)); // minus because up is -Y
		} else if(yPosition+diameter/2 > paddleY + halfPaddleHeight) { // below paddle-center
			// newYSpeed = -60 degrees - ([max.ballValue=120, min=61] * [60 degrees/60 pixels])
			// 1<= newYSpeed <= 60
			angle = Math.toRadians((-returnAngle)+(ballValue*angleHeightRatio));
		} else { // dead on center
			angle = 0; // straight back
		}
		return angle;
	}

	public boolean checkOutOfBounds() {
		if(xPosition - 50 > Settings.getFrameWidth()) {
			isServingRight = true;
			deltaY = 0;
			return true;
		}
		if(xPosition + 50 < 0) {
			isServingLeft = true;
			deltaY = 0;
			return true;
		}
		return false;
	}
	
	public boolean isServingLeft() {
		return isServingLeft;
	}
	
	public boolean isServingRight() {
		return isServingRight;
	}
	
	public double getDeltaY() {
		return deltaY;
	}
	
	public double getDeltaX() {
		return deltaX;
	}
	
	public double getBallSpeed() {
		return ballSpeed;
	}
	
	public void serve(int direction) {
		deltaX = direction*ballSpeed;
		isServingLeft = false;
		isServingRight = false;
	}

}