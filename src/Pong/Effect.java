package Pong;

import java.util.Random;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Effects can be obtained by hitting them with
 * the ball, they give either positive or negative effects
 * to the player.
 * Effects appear randomly in the middle and dissapears
 * when obtained by a player.
 * 
 * @author Erik Norell & Daniel Aceituno 
 * @version 2012-04-28
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
		int randomEffect = rand.nextInt(6); //Four pictures

		switch (randomEffect){
		case 0: effectImage = new Image("data/effects/largerballeffect.png");
		effectType = "largerpaddle";
		break;
		case 1: effectImage = new Image("data/effects/smallerballeffect.png");
		effectType = "smallerpaddle";
		break;
		case 2: effectImage = new Image("data/effects/smallerballeffect.png");
		effectType = "smallerball";
		break;
		case 3: effectImage = new Image("data/effects/largerballeffect.png");
		effectType = "largerball";
		break;
		case 4: effectImage = new Image("data/effects/smallerballeffect.png");
		effectType = "fasterball";
		break;
		case 5: effectImage = new Image("data/effects/largerballeffect.png");
		effectType = "slowerball";
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
	public Ball fasterBall(Ball ball) throws SlickException{
		ball.setBallSpeed(ball.getBallSpeed()*1.25);
		return ball;
	}

	/**
	 * A positive effect, decreases the ball speed
	 * @throws SlickException 
	 */
	public Ball slowerBall(Ball ball) throws SlickException{
		// could approach 0 in infinity, which is pretty unlikely
		ball.setBallSpeed(ball.getBallSpeed()*0.75);
		return ball;
	}
}
