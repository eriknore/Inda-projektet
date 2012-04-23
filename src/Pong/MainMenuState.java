package Pong;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import org.newdawn.slick.state.transition.BlobbyTransition;
import org.newdawn.slick.state.transition.CombinedTransition;
import org.newdawn.slick.state.transition.CrossStateTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.state.transition.HorizontalSplitTransition;
import org.newdawn.slick.state.transition.SelectTransition;
import org.newdawn.slick.state.transition.Transition;
/**
 * A stateclass representing the main menu.
 * @author Erik Norell & Daniel Aceituno
 * @version 2012-04-17
 */
public class MainMenuState extends BasicGameState {

	private int stateID = -1;
    private static int width = 800, height = 600; // resolution
    
	private Image menuBackground, pvpButton, pvaiButton, scoreButton, optionsButton, quitButton, difficultyMenu;

	private int firstMenuButtonX = 80; 
	private int firstMenuButtonY = 50;
	private MouseOverArea pvpButtonArea, pvaiButtonArea, scoreButtonArea, optionsButtonArea, quitButtonArea;
	private boolean choosingDifficulty; 

	public MainMenuState(int stateID) {
		this.stateID = stateID; 
	}


	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		menuBackground = new Image("data/backgrounds/menu.png");
		pvpButton = new Image("data/menu/playervsplayer.png");
		pvaiButton = new Image("data/menu/playervsai.png");
		quitButton = new Image("data/menu/quitgame.png");
		difficultyMenu = new Image("data/menu/difficultychoose.png");

		pvpButtonArea = new MouseOverArea(gc, pvpButton, firstMenuButtonX, firstMenuButtonY);
		pvaiButtonArea = new MouseOverArea(gc, pvpButton, firstMenuButtonX, firstMenuButtonY + 80);
		quitButtonArea = new MouseOverArea(gc, pvpButton, firstMenuButtonX, firstMenuButtonY + 160);
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		menuBackground.draw(0, 0);
		pvpButton.draw(firstMenuButtonX, firstMenuButtonY);
		pvaiButton.draw(firstMenuButtonX, firstMenuButtonY + 80);
		quitButton.draw(firstMenuButtonX, firstMenuButtonY + 160);
		g.drawString("Press 'H' for help", width-200, height-30);
		buttonVisualEffect();
		
		if(choosingDifficulty){
			difficultyMenu();
		}
	}

	private void difficultyMenu() {
		difficultyMenu.draw(width/2, height/4);
		
	}


	/**
	 * Temporarly decreases the button's scale if
	 * the mouse is on the button.
	 */
	private void buttonVisualEffect(){
		MouseOverArea[] areas = {pvpButtonArea, pvaiButtonArea, quitButtonArea}; 
		Image[] images = {pvpButton, pvaiButton, quitButton};
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
		
		if(input.isKeyDown(Input.KEY_H)) 
			sbg.enterState(PongGame.HELPSTATE, t, t);
		
		if(pvpButtonArea.isMouseOver()){
			if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON))
				sbg.enterState(PongGame.GAMEPLAYSTATE, t, t); 
		}

		if(pvaiButtonArea.isMouseOver()){ //Inte implementerat än
			if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)){
				choosingDifficulty = true;
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
