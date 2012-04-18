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
	private boolean isServingLeft = false, isServingRight = false;
	
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
				int ballValue = (yPosition-topBorder)-(diameter/2); // center of ball relative to paddle
				double paddleValue; // will give each pixel of paddle an angle value
				int paddleHeight = bottomBorder-topBorder;
				if(yPosition < topBorder+(paddleHeight/2) && yPosition + diameter > topBorder) {
					paddleValue = (90-angle)/(ballValue+diameter);
					speedY = -Math.sin(Math.toRadians(paddleValue));
				} else if(ballValue > (paddleHeight)/2) {
					paddleValue = ((90-angle))/(-ballValue+paddleHeight);
					speedY = Math.sin(Math.toRadians(paddleValue));
				} else {
					speedY = -speedY;
					return;
				}
				speedX = -speedX;			
			}
		}
	}
	
	private void checkRight(int rightBorder, int leftBorder, int bottomBorder, int topBorder, int angle) {
		if(xPosition + diameter + speedX > leftBorder && xPosition + diameter + speedX < rightBorder) {
			if(yPosition <= bottomBorder && yPosition + diameter >= topBorder) {
				xPosition = leftBorder - diameter;
				int ballValue = (yPosition-topBorder+diameter)-(diameter/2); // center of ball relative to paddle
				double paddleValue; // will give each pixel of paddle an angle value
				int paddleHeight = bottomBorder-topBorder;
				if(yPosition < topBorder+(paddleHeight/2) && yPosition + diameter > topBorder) {
					paddleValue = (90-angle)/(ballValue);
					speedY = -Math.sin(Math.toRadians(paddleValue));
				} else if(yPosition < bottomBorder+(paddleHeight/2) && yPosition + diameter > bottomBorder) {
					paddleValue = ((90-angle))/(-ballValue+paddleHeight);
					speedY = Math.sin(Math.toRadians(paddleValue));
				} else {
					speedY = -speedY;
					return;
				}
				speedX = -speedX;
			}
		}
	}
	
	private void checkOutOfBounds(int frameWidth) {
		if(xPosition - 50 > frameWidth) {
			//TODO point for left player
			isServingRight = true;
			return;
		}
		if(xPosition + 50 < 0) {
			//TODO point for right player
			isServingLeft = true;
			return;
		}
	}
}