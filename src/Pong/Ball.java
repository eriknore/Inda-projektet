package Pong;

import java.util.Random;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


/**
 * @author Erik Norell & Daniel Aceituno 
 * @version 2012-05-09
 */
public class Ball {

	private float ballSpeed, deltaX, deltaY;
	private float[] coordinates = {0,0}; // (x, y)
	private int radius;
	private Image ballImage;
	private boolean isServingLeft = false, isServingRight = false;

	/**
	 * Creates a new ball and sets who get to start serving 
	 * (simulating a coinflip)
	 * @throws SlickException
	 */
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
	 * Returns the ball's coordinates
	 * @return The ball's coordinates in form [x, y]
	 */
	public float[] getCoordinates(){
		return coordinates;
	}

	/**
	 * Returns the image of the ball
	 * @return Image of the ball
	 */
	public Image getImage() {
		return ballImage;
	}

	/**
	 * Moves the ball one frame, checking for if someone is serving,
	 * wall-collisions and paddle-collisions.
	 * @param left Left Paddle
	 * @param right Right Paddle
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

	/**
	 * Checks if the ball collides with a wall and adjusts
	 * direction accordingly
	 */
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

	/**
	 * Checks if the pall collides with a paddle and adjusts
	 * deltaX and deltaY accordingly if so.
	 * @param left Left Paddle
	 * @param right Right Paddle
	 */
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
	
	/**
	 * Calculates the angle at which the ball bounces of a paddle, 
	 * the further from center the greater the angle (default is 30-120 degrees,
	 *  paddle as reference)
	 * @param paddle The paddle which the ball collides with
	 * @return The angle at which the ball travels after colliding with a paddle
	 */
	private float getAngleFromPaddle(Paddle paddle) {
		int paddleY = paddle.getCoordinates()[1];
		
		// ball relative to paddle, value is where on paddle the center of ball is
		float ballValue = (coordinates[1]-(paddleY-2*radius)); 	
		// the angle at which the ball is returned with angle in relation to Y-axis (default angle is 30)
		float returnAngle = (90-paddle.getAngle()); // i.e. 60 degrees from X-axis and up to (Y-axis+30)
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

	/**
	 * Checks if ball is behind each paddle and out of screen.  If so
	 * returns true and resets ball coordinates and serving-variable, otherwise false.
	 * @param left Left paddle
	 * @param right Right paddle
	 * @return True if ball is out-of-screen, false otherwise
	 */
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
	
	/**
	 * Returns true if left paddle is serving, otherwise false
	 * @return True if left paddle is serving
	 */
	public boolean isServingLeft() {
		return isServingLeft;
	}
	
	/**
	 * Returns true if right paddle is serving, otherwise false
	 * @return True if right paddle is serving
	 */
	public boolean isServingRight() {
		return isServingRight;
	}
	
	/**
	 * Returns the rate of movement and direction in 
	 * y-direction of the ball
	 * @return deltaY - the movement per frame in y-direction
	 */
	public float getDeltaY() {
		return deltaY;
	}
	
	/**
	 * Returns the rate of movement and direction in 
	 * x-direction of the ball
	 * @return deltaY - the movement per frame in x-direction
	 */
	public float getDeltaX() {
		return deltaX;
	}
	
	/**
	 * Returns the speed of the ball
	 * @return The speed of the ball
	 */
	public float getBallSpeed() {
		return ballSpeed;
	}
	
	/**
	 * Serves the ball by setting the speed (in x-direction, 
	 * y-direction is 0) to positive or negative. Also resets
	 * indication that left or right paddle is serving
	 * @param direction
	 */
	public void serve(int direction) {
		deltaX = direction*ballSpeed;
		isServingLeft = false;
		isServingRight = false;
	}
	
	/**
	 * Returns the radius of the ball
	 * @return The radius of the ball
	 */
	public int getRadius(){
		return radius;
	}
	
	/**
	 * Shrinks the ball, used as a power-down
	 * @param ball The ball
	 * @throws SlickException
	 */
	public void shrinkBall(Ball ball) throws SlickException{
		ballImage = new Image("data/ball/smallball.png");
		radius = ballImage.getWidth()/2;
	}
	
	/**
	 * Enlarges the ball, used as a power-up
	 * @param ball The ball
	 * @throws SlickException
	 */
	public void enlargeBall(Ball ball) throws SlickException{
		ballImage = new Image("data/ball/largeball.png");
		radius = ballImage.getWidth()/2;
	}
	
	
	/**
	 * Resets the image and speed to default
	 * @throws SlickException
	 */
	public void resetBall() throws SlickException{
		ballImage = new Image("data/ball/default.png");
		radius = ballImage.getWidth()/2;
		ballSpeed = 8;
	}
	
	/**
	 * Sets a new speed for the ball
	 * @param newSpeed The new speed of the ball
	 */
	public void setBallSpeed(float newSpeed) {
		ballSpeed = newSpeed;
	}
	
}