package Pong;

import java.util.Random;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


/**
 * @author Erik Norell & Daniel Aceituno 
 * @version 2012-04-16
 */
public class Ball {

	private float ballSpeed, deltaX, deltaY;
	private float[] coordinates = {0,0}; // (x, y)
	private int radius;
	private Image ballImage;
	private boolean isServingLeft = false, isServingRight = false;

	public Ball() throws SlickException {
		ballImage = new Image("data/ball/default.png");
		radius = ballImage.getWidth()/2;
		ballSpeed = 8;

		// simulate a coinflip to decide which player to serve
		Random rand = new Random();
		if(rand.nextBoolean()) {
			isServingLeft = true;
		} else {
			isServingRight = true;
		}

	}

	/**
	 * 
	 * @return The ball's position on the x-axis
	 */
	public float[] getCoordinates(){
		return coordinates;
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
			coordinates[0] = left.getCoordinates()[0] + left.getWidth();
			coordinates[1] = left.getCoordinates()[1] + (left.getHeight()-2*radius)/2;
			return;
		}
		if(isServingRight) {
			coordinates[0] = right.getCoordinates()[0] - 2*radius;
			coordinates[1] = right.getCoordinates()[1] +(right.getHeight()-2*radius)/2;
			return;
		}
		checkWalls();
		checkPaddles(left, right);
		coordinates[1] += deltaY;
		coordinates[0] += deltaX;
		return;
	}

	private void checkWalls() {
		if(coordinates[1] <= 0) {
			coordinates[1] = 1;
			deltaY = -deltaY;
		}
		
		if(coordinates[1] + 2*radius >= Settings.getFrameHeight()) {
			coordinates[1] = Settings.getFrameHeight() - 2*radius -1;
			deltaY = -deltaY;
		}
	}

	private void checkPaddles(Paddle left, Paddle right) {
		
		int edgeOfPaddle = left.getCoordinates()[0] + left.getWidth();
		// if ball is to the left of left paddle in relation to X-position...
		if(coordinates[0] + deltaX <= edgeOfPaddle && coordinates[0] + 2*radius + deltaX >= left.getCoordinates()[0]) {
			int leftY = left.getCoordinates()[1];
			// ... then check if it is hitting the paddle or not
			if(coordinates[1] <= leftY + left.getHeight() && coordinates[1] + 2*radius >= leftY) {
				coordinates[0] = edgeOfPaddle;
				float angle = getAngleFromPaddle(left);
				deltaY = (float) (ballSpeed*Math.sin(angle));
				deltaX = (float) (ballSpeed*Math.cos(angle));
			}
		}
		edgeOfPaddle = right.getCoordinates()[0];
		// if ball is to the right of right paddle in relation to X-position...
		if(coordinates[0] + 2*radius + deltaX >= edgeOfPaddle && coordinates[0] + deltaX <= edgeOfPaddle + right.getWidth()) {
			int rightY = right.getCoordinates()[1];
			// ... then check if it is hitting the paddle or not
			if(coordinates[1] <= rightY + right.getHeight() && coordinates[1] + 2*radius >= rightY) {
				coordinates[0] = edgeOfPaddle - 2*radius;
				float angle = getAngleFromPaddle(right);
				deltaY = (float) (ballSpeed*Math.sin(angle));
				deltaX = (float) (-ballSpeed*Math.cos(angle));
			}
		}
	}
	
	private float getAngleFromPaddle(Paddle paddle) {
		int paddleY = paddle.getCoordinates()[1];
		
		// ball relative to paddle, value is where on paddle the center of ball is
		float ballValue = (coordinates[1]-(paddleY-2*radius)); 	
		// the angle at which the ball is returned with angle in relation to Y-axel (default angle is 30)
		float returnAngle = (90-paddle.getAngle()); // i.e. 60 degrees from X-axel and up to (Y-axel+30)
		// half the paddle-height including ball-radius at both ends of paddle
		float halfPaddleHeight = (paddle.getHeight() + 2*radius)/2; // default: 120/2
		// set a ratio between angle and paddle-size
		float angleHeightRatio = returnAngle/halfPaddleHeight; // default: 60 degrees/60 pixels
		Double angle;
		if(coordinates[1]+radius < paddleY+halfPaddleHeight) { // above paddle-center
			// newYSpeed = 60 degrees - ([max.ballValue=60, min=1] * [60 degrees/60 pixels])
			// 1<= newYSpeed <= 60
			angle = -Math.toRadians(returnAngle-(ballValue*angleHeightRatio)); // minus because up is -Y
		} else if(coordinates[1]+radius > paddleY + halfPaddleHeight) { // below paddle-center
			// newYSpeed = -60 degrees - ([max.ballValue=120, min=61] * [60 degrees/60 pixels])
			// 1<= newYSpeed <= 60
			angle = Math.toRadians((-returnAngle)+(ballValue*angleHeightRatio));
		} else { // dead on center
			angle = 0.0; // straight back
		}
		return angle.floatValue();
	}

	public boolean checkOutOfBounds(Paddle left, Paddle right) {
		
		if(coordinates[0] - 50 > Settings.getFrameWidth()) {
			isServingRight = true;
			deltaY = 0;
			coordinates[0] = right.getCoordinates()[0] - 2*radius;
			coordinates[1] = Settings.getFrameHeight()/2 - radius;
			return true;
		}
		if(coordinates[0] + 50 < 0) {
			isServingLeft = true;
			deltaY = 0;
			coordinates[0] = left.getCoordinates()[0] + left.getWidth();
			coordinates[1] = Settings.getFrameHeight()/2 - radius;
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
	
	public float getDeltaY() {
		return deltaY;
	}
	
	public float getDeltaX() {
		return deltaX;
	}
	
	public float getBallSpeed() {
		return ballSpeed;
	}
	
	public void serve(int direction) {
		deltaX = direction*ballSpeed;
		isServingLeft = false;
		isServingRight = false;
	}
	
	public int getRadius(){
		return radius;
	}
	
	public void shrinkBall(Ball ball) throws SlickException{
		ballImage = new Image("data/ball/smallball.png");
		radius = ballImage.getWidth()/2;
	}
	
	public void enlargeBall(Ball ball) throws SlickException{
		ballImage = new Image("data/ball/largeball.png");
		radius = ballImage.getWidth()/2;
	}
	
	public void resetBall() throws SlickException{
		ballImage = new Image("data/ball/default.png");
		radius = ballImage.getWidth()/2;
		ballSpeed = 8;
	}
	
	public void setBallSpeed(float newSpeed) {
		ballSpeed = newSpeed;
	}
	
}