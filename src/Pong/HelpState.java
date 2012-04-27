package Pong;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.state.transition.Transition;

/**
 * A stateclass representing the help window.
 * Used when the player presses the 'H' button
 * during the gameplay.
 * @author Erik Norell & Daniel Aceituno
 * @version 2012-04-23
 */
public class HelpState extends BasicGameState {

	private int stateID = -1;
	private Image helpmenubackground, largeBallEffect, smallBallEffect, largePaddleEffect, smallPaddleEffect;
    private static int width = 800, height = 600; // resolution
	
	public HelpState(int stateID) {
		this.stateID = stateID;
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		helpmenubackground = new Image("data/backgrounds/helpmenubackground.png");
		largeBallEffect = new Image("data/effects/largerballeffect.png");
		smallBallEffect = new Image("data/effects/smallerballeffect.png");
		smallPaddleEffect = new Image("data/effects/smallerpaddleeffect.png");
		largePaddleEffect = new Image("data/effects/largerpaddleeffect.png");
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		helpmenubackground.draw(0,0);
		int rightLineX = 540;
		int leftLineX = 50;
		int y = 50;
		
//		largeBallEffect.draw(leftLineX, y+300);
//		g.drawString(" - Happy effect", leftLineX+ 30, y+300);
//		
//		smallBallEffect.draw(leftLineX, y + 340);
//		g.drawString(" - Sad effect", leftLineX + 30, y + 340);
//		
//		largePaddleEffect.draw(leftLineX, y + 400);
//		g.drawString(" - Happy paddle!", leftLineX + 30 , y+ 400);
//		
//		smallPaddleEffect.draw(leftLineX+200, y + 400);
//		g.drawString("- Sad Paddle!", leftLineX + 230, y + 400);
		
		
		g.drawString("Right Player controls: ", rightLineX, y);
		g.drawString("Move up: Up Arrow", rightLineX, y+50);
		g.drawString("Move down: Down Arrow", rightLineX, y+100);
		g.drawString("Serve: Left Arrow", rightLineX, y+150);
//		g.drawString("Use Weapon: Left Arrow", rightLineX, y+200);
		g.drawString("Left Player controls: ", leftLineX, y);
		g.drawString("Move up: W", leftLineX, y+50);
		g.drawString("Move down: S", leftLineX, y+100);
		g.drawString("Serve: D", leftLineX, y+150);
//		g.drawString("Use Weapon: D", leftLineX, y+200);
		
		g.drawString("Press 'H' to return, 'Esc' to return to main menu", 300, height-30); 
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		Input input = gc.getInput();
		Transition t = new FadeOutTransition();
		
		if(input.isKeyDown(Input.KEY_ESCAPE)) 
			sbg.enterState(PongGame.MAINMENUSTATE, t, t);

		if(input.isKeyDown(Input.KEY_H)) 
			sbg.enterState(PongGame.GAMEPLAYSTATE, t, t);
		
	}

	@Override
	public int getID() {
		return stateID;
	}

}
