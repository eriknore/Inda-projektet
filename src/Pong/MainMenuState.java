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
 * @author Erik Norell & Daniel Aceituno
 * @version 2012-04-17
 */
public class MainMenuState extends BasicGameState {

	private int stateID = -1;

	private Image menuBackground, pvpButton, pvaiButton, scoreButton, optionsButton, quitButton;

	private int firstMenuButtonX = 80; 
	private int firstMenuButtonY = 50;

	private MouseOverArea pvpButtonArea, pvaiButtonArea, scoreButtonArea, optionsButtonArea, quitButtonArea; 

	public MainMenuState(int stateID) {
		this.stateID = stateID; 
	}


	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		menuBackground = new Image("data/backgrounds/menu.png");
		pvpButton = new Image("data/menu/playervsplayer.png");
		pvaiButton = new Image("data/menu/playervsai.png");
		scoreButton = new Image("data/menu/highscore.png");
		optionsButton = new Image("data/menu/options.png");
		quitButton = new Image("data/menu/quitgame.png");

		pvpButtonArea = new MouseOverArea(gc, pvpButton, firstMenuButtonX, firstMenuButtonY);
		pvaiButtonArea = new MouseOverArea(gc, pvpButton, firstMenuButtonX, firstMenuButtonY + 80);
		scoreButtonArea = new MouseOverArea(gc, pvpButton, firstMenuButtonX, firstMenuButtonY + 160);
		optionsButtonArea = new MouseOverArea(gc, pvpButton, firstMenuButtonX, firstMenuButtonY + 240);
		quitButtonArea = new MouseOverArea(gc, pvpButton, firstMenuButtonX, firstMenuButtonY + 320);
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		menuBackground.draw(0, 0);
		pvpButton.draw(firstMenuButtonX, firstMenuButtonY);
		pvaiButton.draw(firstMenuButtonX, firstMenuButtonY + 80);
		scoreButton.draw(firstMenuButtonX, firstMenuButtonY + 160);
		optionsButton.draw(firstMenuButtonX, firstMenuButtonY + 240);
		quitButton.draw(firstMenuButtonX, firstMenuButtonY + 320);
		buttonVisualEffect();
	}

	/**
	 * Temporarly decreases the button's scale if
	 * the mouse is on the button.
	 */
	private void buttonVisualEffect(){
		MouseOverArea[] areas = {pvpButtonArea, pvaiButtonArea, scoreButtonArea, optionsButtonArea, quitButtonArea}; 
		Image[] images = {pvpButton, pvaiButton, scoreButton, optionsButton, quitButton};
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
			if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON))
				sbg.enterState(PongGame.GAMEPLAYSTATE, t, t); 
		}

		if(pvaiButtonArea.isMouseOver()){ //Gå över till gameplaystate eller twoplayerstate?
			if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)){
				sbg.enterState(PongGame.GAMEPLAYSTATE, t, t); 
			}
		}
		
		if(scoreButtonArea.isMouseOver()){
			//highscoreState
		}
		if(optionsButtonArea.isMouseOver()){
			//optionsState
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
