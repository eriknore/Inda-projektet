package Pong;

import java.util.Map;
import java.util.Random;

import org.newdawn.slick.Image;

/**
 * @author Erik Norell & Daniel Aceituno 
 * @version 2012-04-16
 */
public class Ball {
	
	private int xPosition, yPosition, speedX, speedY, diameter;
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
			speedX = 1;
			xPosition = state.get("pLeftX") + state.get("pWidth");
			yPosition = state.get("pLeftY") + (state.get("pHeight")-diameter)/2;
			return;
		}
		if(isServingRight) {
			speedX = -1;
			xPosition = state.get("pRightX") - ballImage.getWidth();
			yPosition = state.get("pRightY") + (state.get("pHeight")-diameter)/2;
			return;
		}
		if(yPosition < 0) {
			yPosition = 0;
			speedY = -speedY;
		}
		if(yPosition + diameter > state.get("frameHeight")) {
			yPosition = state.get("frameHeight") - diameter;
			speedY = -speedY;
		}
		if(xPosition + diameter + speedX > state.get("pRightX") && xPosition + diameter + speedX < state.get("pRightX") + state.get("pWidth")) {
			if(yPosition <= (state.get("pRightY") + state.get("pHeight")) && yPosition + diameter >= state.get("pRightY")) {
				xPosition = state.get("pRightX") - diameter;
				speedX = -speedX;	
			}
		}
		if(xPosition + speedX < state.get("pLeftX") + state.get("pWidth") && xPosition + speedX > state.get("pLeftX")) {
			if(yPosition <= (state.get("pLeftY") + state.get("pHeight")) && yPosition + diameter >= state.get("pLeftY")) {
				xPosition = state.get("pLeftX") + state.get("pWidth");
				speedX = -speedX;			
			}
		}
		if(xPosition - 50 > state.get("frameWidth")) {
			//TODO point for left player
			isServingRight = true;
			return;
		}
		if(xPosition + 50 < 0) {
			//TODO point for right player
			isServingLeft = true;
			return;
		}
		yPosition += speedY*2*delta;
		xPosition += speedX*2*delta;
		return;
	}
	
	public void servedLeft() {
		isServingLeft = false;
	}
	
	public void servedRight() {
		isServingRight = false;
	}
}