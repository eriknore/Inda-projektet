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
 * pvp = player vs player, refactor?
 * @author dace
 * @version 2012-04-17
 */
public class MainMenuState extends BasicGameState {

	private int stateID = -1;

	private Image menuBackground, pvpButton, pvaiButton, scoreButton, optionsButton, quitButton;


	private int firstMenuButtonX = 80; //pvpButton
	private int firstMenuButtonY = 50;

	private MouseOverArea pvpButtonArea, pvaiButtonArea, scoreButtonArea, optionsButtonArea, quitButtonArea; 

	public MainMenuState(int stateID) {
		this.stateID = stateID; 
	}


	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		//bättre sätt?, Imagearray??
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
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		Input input = gc.getInput();

		if(pvpButtonArea.isMouseOver()){
			//TODO effect, visuell mouseover
			if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)){
				Transition t = new FadeOutTransition(); 
				sbg.enterState(PongGame.GAMEPLAYSTATE, t, t); //Stänger detta menuState?
			}
		}
		if(quitButtonArea.isMouseOver()){
			//TODO effekt, visuell mouseover
			if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON))
				gc.exit();
		}

		//TODO Alla andra areas	
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
