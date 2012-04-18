package Pong;

import java.util.Map;
import java.util.Random;

import org.newdawn.slick.Image;


/**
 * @author Erik Norell & Daniel Aceituno 
 * @version 2012-04-16
 */
public class Ball {

	private int xPosition, yPosition, diameter;
	private double speedX, speedY;
	private Image ballImage;
	private boolean isServingLeft = false, isServingRight = false, isOutOfBounds = false;

	public Ball(int xCoordinate, int yCoordinate, Image imageOfBall) {
		xPosition = xCoordinate;
		yPosition = yCoordinate;
		ballImage = imageOfBall;
		diameter = ballImage.getWidth();
		speedX = 0;
		speedY = 0;

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

	/**
	 * @param paddleLeft
	 * @param paddleRight
	 * @param topWall
	 * @param bottomWall
	 * @return
	 */
	public void moveBall(Map<String, Integer> state, float delta) {
		isOutOfBounds = false;
		if(isServingLeft) {
			// kolla serveLeft() för att förstå vad dessa värden innebär, ska refaktorisera
			serveLeft(state.get("pLeftX") + state.get("pWidth"), state.get("pLeftY") + (state.get("pHeight")-diameter)/2);
			return;
		}
		if(isServingRight) {
			// kolla serveRight() för att förstå vad dessa värden innebär, ska refaktorisera
			serveRight(state.get("pRightX") - ballImage.getWidth(), state.get("pRightY") + (state.get("pHeight")-diameter)/2);
			return;
		}
		checkWalls(state.get("frameHeight"));
		// kolla CheckRight()/Left() för att förstå vad dessa värden innebär, ska refaktorisera
		checkRight(state.get("pRightX") + state.get("pWidth"), state.get("pRightX"), state.get("pRightY") + state.get("pHeight"), state.get("pRightY"), state.get("angle"));
		checkLeft(state.get("pLeftX") + state.get("pWidth"), state.get("pLeftX"), state.get("pLeftY") + state.get("pHeight"), state.get("pLeftY"), state.get("angle"));
		checkOutOfBounds(state.get("frameWidth"));
		yPosition += speedY*delta;
		xPosition += speedX*delta;
		return;
	}

	public void servedLeft() {
		isServingLeft = false;
	}

	public void servedRight() {
		isServingRight = false;
	}

	private void serveLeft(int boardX, int boardY) {
		speedX = 2;
		speedY = 0;
		xPosition = boardX;
		yPosition = boardY;
	}

	private void serveRight(int boardX, int boardY) {
		speedX = -2;
		speedY = 0;
		xPosition = boardX;
		yPosition = boardY;
	}

	private void checkWalls(int frameHeight) {
		if(yPosition < 0) {
			yPosition = 0 + 1;
			speedY = -speedY;
		}
		if(yPosition + diameter > frameHeight) {
			yPosition = frameHeight - diameter -1;
			speedY = -speedY;
		}
	}

	private void checkLeft(int rightBorder, int leftBorder, int bottomBorder, int topBorder, int angle) {
		if(xPosition + speedX < rightBorder && xPosition + speedX > leftBorder) {
			if(yPosition <= bottomBorder && yPosition + diameter >= topBorder) {
				xPosition = rightBorder;

				// ball relative to paddle, value is where on paddle the center of ball is
				double ballValue = (yPosition-(topBorder-diameter)); 	
				// the angle at which the ball is returned with y + angle (default angle is 30)
				double returnAngle = (90-angle); // i.e. 60 degrees
				// half the paddle-height including ball-radius at both ends of paddle
				double halfPaddleHeight = (bottomBorder-topBorder+diameter)/2; // default: 120/2
				// set a ratio between angle and paddle-size
				double angleHeightRatio = returnAngle/halfPaddleHeight; // default: 60 degrees/60 pixels
				double newYSpeed;
				if(yPosition+diameter < topBorder+halfPaddleHeight) { // above paddle-center
					// newYSpeed = 60 degrees - ([max.ballValue=60, min=1] * [60 degrees/60 pixels])
					// 1<= newYSpeed <= 60
					newYSpeed = returnAngle-(ballValue*angleHeightRatio);
					speedY = -Math.sin(Math.toRadians(newYSpeed)); // minus because up is -Y
				} else if(yPosition > bottomBorder-halfPaddleHeight) { // below paddle-center
					// newYSpeed = -60 degrees - ([max.ballValue=120, min=61] * [60 degrees/60 pixels])
					// 1<= newYSpeed <= 60
					newYSpeed = (-returnAngle)+(ballValue*angleHeightRatio);
					speedY = Math.sin(Math.toRadians(newYSpeed)); // no minus because up is +Y
				} else { // dead on center
					speedY = -speedY; // straight back
				}
				speedX = -speedX;
			}
		}
	}

	private void checkRight(int rightBorder, int leftBorder, int bottomBorder, int topBorder, int angle) {
		if(xPosition + diameter + speedX > leftBorder && xPosition + diameter + speedX < rightBorder) {
			if(yPosition <= bottomBorder && yPosition + diameter >= topBorder) {
				xPosition = leftBorder - diameter;

				// ball relative to paddle, value is where on paddle the center of ball is
				double ballValue = (yPosition-(topBorder-diameter)); 	
				// the angle at which the ball is returned with y + angle (default angle is 30)
				double returnAngle = (90-angle); // i.e. 60 degrees
				// half the paddle-height including ball-radius at both ends of paddle
				double halfPaddleHeight = (bottomBorder-topBorder+diameter)/2; // default: 120/2
				// set a ratio between angle and paddle-size
				double angleHeightRatio = returnAngle/halfPaddleHeight; // default: 60 degrees/60 pixels
				double newYSpeed;
				if(yPosition+diameter < topBorder+halfPaddleHeight) { // above paddle-center
					// newYSpeed = 60 degrees - ([max.ballValue=60, min=1] * [60 degrees/60 pixels])
					// 1<= newYSpeed <= 60
					newYSpeed = returnAngle-(ballValue*angleHeightRatio);
					speedY = -Math.sin(Math.toRadians(newYSpeed)); // minus because up is -Y
				} else if(yPosition > bottomBorder-halfPaddleHeight) { // below paddle-center
					// newYSpeed = -60 degrees - ([max.ballValue=120, min=61] * [60 degrees/60 pixels])
					// 1<= newYSpeed <= 60
					newYSpeed = (-returnAngle)+(ballValue*angleHeightRatio);
					speedY = Math.sin(Math.toRadians(newYSpeed)); // no minus because up is +Y
				} else { // dead on center
					speedY = -speedY; // straight back
				}
				speedX = -speedX;
			}
		}
	}

	private void checkOutOfBounds(int frameWidth) {
		if(xPosition - 50 > frameWidth) {
			//TODO point for left player
			isServingRight = true;
			isOutOfBounds = true;
			return;
		}
		if(xPosition + 50 < 0) {
			//TODO point for right player
			isServingLeft = true;
			isOutOfBounds = true;
			return;
		}
	}
	//lagt till dessa nedan (kolla playerScore i GamePlayState)
	//lagt till en instansvariabel isOutOfBounds, som ändras i checkOutOfBounds och moveBall
	//vet inte om det är snyggaste lösningen?? försökte hålla det så Ball'igt som möjligt
	//radera detta när du har läst :)
	public boolean getIsServingRight(){
		return isServingRight;
	}
	public boolean getIsServingLeft(){
		return isServingLeft;
	}
	public boolean wasOutOfBounds(){
		if(isOutOfBounds){
			return isOutOfBounds;
		}
		return false;
	}
}