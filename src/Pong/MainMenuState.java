package Pong;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeOutTransition;

import org.newdawn.slick.state.transition.Transition;
/**
 * A stateclass representing the main menu.
 * Used when the game starts.
 * @author Erik Norell & Daniel Aceituno
 * @version 2012-04-23
 */
public class MainMenuState extends BasicGameState {

	private int stateID = -1;
	private Image menuBackground, pvpButton, pvpcButton, quitButton;
	private int firstMenuButtonX = 80; // [Player vs Player] button
	private int firstMenuButtonY = 50;
	private MouseOverArea pvpButtonArea, pvpcButtonArea, quitButtonArea; 

	public MainMenuState(int stateID) {
		this.stateID = stateID; 
	}


	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		menuBackground = new Image("data/backgrounds/menu.png");
		pvpButton = new Image("data/menu/playervsplayer.png"); 
		pvpcButton = new Image("data/menu/playervsai.png");
		quitButton = new Image("data/menu/quitgame.png");
		pvpButtonArea = new MouseOverArea(gc, pvpButton, firstMenuButtonX, firstMenuButtonY);
		pvpcButtonArea = new MouseOverArea(gc, pvpcButton, firstMenuButtonX, firstMenuButtonY + 80);
		quitButtonArea = new MouseOverArea(gc, quitButton, firstMenuButtonX, firstMenuButtonY + 160);
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		menuBackground.draw(0, 0);
		pvpButton.draw(firstMenuButtonX, firstMenuButtonY);
		pvpcButton.draw(firstMenuButtonX, firstMenuButtonY + 80);
		quitButton.draw(firstMenuButtonX, firstMenuButtonY + 160);
		buttonVisualEffect();
	}

	/**
	 * Temporarly decreases the button's scale if
	 * the mouse is on the button.
	 */
	private void buttonVisualEffect(){
		MouseOverArea[] areas = {pvpButtonArea, pvpcButtonArea, quitButtonArea}; 
		Image[] images = {pvpButton, pvpcButton, quitButton};
		for(int i = 0 ; i < areas.length ; i++){
			if(areas[i].isMouseOver()){
				images[i].draw(firstMenuButtonX, firstMenuButtonY + (i*80), 0.99f);
			}
		}
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		Input input = gc.getInput();
		Transition t = new FadeOutTransition();

		if(input.isKeyDown(Input.KEY_ESCAPE)) 
			sbg.enterState(PongGame.GAMEPLAYSTATE, t, t);

		if(pvpButtonArea.isMouseOver()){
			if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
				Settings.setIsLeftHuman(true);
				Settings.setIsRightHuman(true);
				sbg.enterState(PongGame.GAMEPLAYSTATE, t, t);
			}
		}

		if(pvpcButtonArea.isMouseOver()){ 
			if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)){
				Settings.setIsLeftHuman(false);
				Settings.setIsRightHuman(false);
				sbg.enterState(PongGame.DIFFICULTYMENU, t, t);
			}
		}

		if(quitButtonArea.isMouseOver()){
			if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON))
				gc.exit();
		}
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
