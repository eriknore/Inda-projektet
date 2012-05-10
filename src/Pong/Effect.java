package Pong;

import java.util.Random;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;

/**
 * Effects can be obtained by hitting them with
 * the ball, they give either positive or negative effects
 * to the player.
 * Effects appear randomly in the middle and disappears
 * when obtained by a player.
 * 
 * @author Erik Norell & Daniel Aceituno 
 * @version 2012-05-10
 */
public class Effect {

	private Image effectImage;
	private String effectType;

	public Effect() throws SlickException {
		setEffectImage();
	}

	/**
	 * Sets a random image to the effect.
	 * (4 possible)
	 * @throws SlickException 
	 */
	public void setEffectImage() throws SlickException{
		Random rand = new Random();
		int randomEffect = rand.nextInt(8); //Four pictures

		switch (randomEffect){
		case 0: effectImage = new Image("data/effects/powerup.png");
		effectType = "Larger Paddle";
		break;
		case 1: effectImage = new Image("data/effects/powerdown.png");
		effectType = "Smaller Paddle";
		break;
		case 2: effectImage = new Image("data/effects/powerdown.png");
		effectType = "Smaller Ball";
		break;
		case 3: effectImage = new Image("data/effects/powerup.png");
		effectType = "Larger Ball";
		break;
		case 4: effectImage = new Image("data/effects/powerdown.png");
		effectType = "Faster Ball";
		break;
		case 5: effectImage = new Image("data/effects/powerup.png");
		effectType = "Slower Ball";
		break;
		case 6: effectImage = new Image("data/effects/powerup.png");
		effectType = "Faster Paddle";
		break;
		case 7: effectImage = new Image("data/effects/powerdown.png");
		effectType = "Slower Paddle";
		break;
		}
	}

	/**
	 * Returns the image of the effect.
	 */
	public Image getImage(){
		return effectImage;
	}

	/**
	 * Returns the type of the effect.
	 */
	public String getEffectType(){
		return effectType;
	}

	/**
	 * A negative effect, decreases the ball size
	 * @throws SlickException 
	 */
	public Ball smallerBall(Ball ball) throws SlickException{
		ball.shrinkBall(ball);
		return ball;
	}

	/**
	 * A positive effect, increases the ball size
	 * @return 
	 * @throws SlickException 
	 */
	public Ball largerBall(Ball ball) throws SlickException{
		ball.enlargeBall(ball);
		return ball;
	}

	/**
	 * A negative effect, decreases the player's
	 * paddle size.
	 * @param paddle 
	 * @return 
	 * @throws SlickException 
	 */
	public Paddle smallerPaddle(Paddle paddle) throws SlickException{
		paddle.shrinkPaddle();
		return paddle;
	}

	/**
	 * A positive effect, increases the player's
	 * paddle size.
	 * @return 
	 * @throws SlickException 
	 */
	public Paddle largerPaddle(Paddle paddle) throws SlickException{
		paddle.enlargePaddle();
		return paddle;
	}

	public int getWidth(){
		return effectImage.getWidth();
	}

	public int getHeight(){
		return effectImage.getHeight();
	}

	/**
	 * A negative effect, increases the ball speed
	 * @throws SlickException 
	 */
	private Ball fasterBall(Ball ball) throws SlickException{
		ball.setBallSpeed((float) (ball.getBallSpeed()*1.25));
		return ball;
	}

	/**
	 * A positive effect, decreases the ball speed
	 * @throws SlickException 
	 */
	private Ball slowerBall(Ball ball) throws SlickException{
		// could approach 0 in infinity, but that's pretty unlikely
		ball.setBallSpeed((float) (ball.getBallSpeed()*0.75));
		return ball;
	}
	
	/**
	 * A negative effect, increases the speed of the paddle
	 * @throws SlickException 
	 */
	private Paddle fasterPaddle(Paddle paddle) throws SlickException{
		paddle.setPaddleSpeed((float) (paddle.getPaddleSpeed()*1.25));
		return paddle;
	}

	/**
	 * A positive effect, decreases the ball speed
	 * @throws SlickException 
	 */
	private Paddle slowerPaddle(Paddle paddle) throws SlickException{
		// could approach 0 in infinity, but that's pretty unlikely
		paddle.setPaddleSpeed((float) (paddle.getPaddleSpeed()*0.75));
		return paddle;
	}
	
	/**
	 * Checks if a player has obtained
	 * the effect.
	 * @throws SlickException 
	 */
	public boolean checkEffectCollision(Ball ball, Paddle paddleLeft, Paddle paddleRight) throws SlickException{
		//Create 2 shapes identical to the effectImage and ballImage
		float[] coordinates = ball.getCoordinates();
		int ballRadius = ball.getRadius();
		Shape ballShape = new Circle(coordinates[0]-ballRadius, coordinates[1]-ballRadius, ballRadius);
		int effectX = Settings.getFrameWidth()/2 - effectImage.getWidth()/2;
		int effectY = Settings.getFrameHeight()/2 - effectImage.getHeight()/2;
		Shape effectShape = new Circle(effectX, effectY, effectImage.getWidth()/2);
		if(effectShape.intersects(ballShape)){
			givePlayerEffect(ball, paddleLeft, paddleRight);
			return true;
			}
		return false;
	}
	
	/**
	 * Gives the effect to the player
	 * that obtained it.
	 * @throws SlickException 
	 */
	private void givePlayerEffect(Ball ball, Paddle paddleLeft, Paddle paddleRight) throws SlickException {
		Paddle paddleToChange;
		if(ball.getDeltaX() > 0) {
			paddleToChange = paddleLeft;
		} else {
			paddleToChange = paddleRight;
		}
		if(effectType.equals("Larger Paddle"))
			paddleToChange = largerPaddle(paddleToChange);
		if(effectType.equals("Smaller Paddle"))
			paddleToChange = smallerPaddle(paddleToChange);
		if(effectType.equals("Smaller Ball"))
			ball = smallerBall(ball);
		if(effectType.equals("Larger Ball"))
			ball = largerBall(ball);
		if(effectType.equals("Slower Ball"))
			ball = slowerBall(ball);
		if(effectType.equals("Faster Ball"))
			ball = fasterBall(ball);
		if(effectType.equals("Slower Paddle"))
			paddleToChange = slowerPaddle(paddleToChange);
		if(effectType.equals("Faster Paddle"))
			paddleToChange = fasterPaddle(paddleToChange);
	}
}
