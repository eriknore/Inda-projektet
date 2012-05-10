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
 * @version 2012-05-10
 */
public class HelpState extends BasicGameState {

	private int stateID = -1;
	private Image helpmenubackground, powerUp, powerDown;
	
	public HelpState(int stateID) {
		this.stateID = stateID;
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		helpmenubackground = new Image("data/backgrounds/helpmenubackground.png");
		powerUp = new Image("data/effects/powerup.png");
		powerDown = new Image("data/effects/powerdown.png");
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		helpmenubackground.draw(0,0);
		int rightLineX = 540;
		int leftLineX = 50;
		int y = 50;
		
		powerUp.draw(leftLineX, y+300);
		g.drawString("[Random Power-Up]\nIncreases:\n -Ball size\n -Paddle size\n -Paddle speed\nDecreases:\n -Ball speed", leftLineX + 30, y + 300);
		
		powerDown.draw(leftLineX + 300, y + 300);
		g.drawString("[Random Power-Down]\nIncreases:\n -Ball speed\nDecreases:\n -Ball size\n -Paddle size\n -Paddle speed", leftLineX + 330, y + 300);
		
		g.drawString("Right Player controls: ", rightLineX, y);
		g.drawString("Move up: Up Arrow", rightLineX, y+50);
		g.drawString("Move down: Down Arrow", rightLineX, y+100);
		g.drawString("Serve: Left Arrow", rightLineX, y+150);
		g.drawString("Left Player controls: ", leftLineX, y);
		g.drawString("Move up: W", leftLineX, y+50);
		g.drawString("Move down: S", leftLineX, y+100);
		g.drawString("Serve: D", leftLineX, y+150);
		g.drawString("Press 'H' to return, 'Esc' to return to main menu", 300, Settings.getFrameHeight()-30); 
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
	
	/**
	 * Returns the state ID.
	 * @return Returns the state ID
	 */
	@Override
	public int getID() {
		return stateID;
	}
}