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
 * A stateclass representing the difficulty menu.
 * Used when the user chooses to play vs PC.
 * @author Erik Norell & Daniel Aceituno
 * @version 2012-04-23
 */
public class DifficultyMenuState extends BasicGameState {
	
	private int stateID = -1;
	private Image difficultyMenu, menuBackground, easy, medium, hard, cancel;
	private int firstMenuButtonX = 215; // [Easy] button
	private int firstMenuButtonY = 220;
	private MouseOverArea easyArea, mediumArea, hardArea, cancelArea; 
	
	
	public DifficultyMenuState(int stateID) {
		this.stateID = stateID;
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		menuBackground = new Image("data/backgrounds/menu.png");
		difficultyMenu = new Image("data/menu/difficultychoose.png");
		easy = new Image("data/menu/easy.png");
		medium = new Image("data/menu/medium.png");
		hard = new Image("data/menu/hard.png");
		cancel = new Image("data/menu/cancel.png");
		easyArea = new MouseOverArea(gc, easy, firstMenuButtonX, firstMenuButtonY);
		mediumArea = new MouseOverArea(gc, medium, firstMenuButtonX + 110, firstMenuButtonY);
		hardArea = new MouseOverArea(gc, hard, firstMenuButtonX + 220, firstMenuButtonY);
		cancelArea = new MouseOverArea(gc, cancel, firstMenuButtonX + 95, firstMenuButtonY + 110);
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		Input input = gc.getInput();
		Transition t = new FadeOutTransition();
		
		if(easyArea.isMouseOver()){
			if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
				Settings.setDifficulty("Easy");
				sbg.enterState(PongGame.GAMEPLAYSTATE, t, t);
			}
		}
		
		if(mediumArea.isMouseOver()){
			if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
				Settings.setDifficulty("Medium");
				sbg.enterState(PongGame.GAMEPLAYSTATE, t, t);
			}
		}
		
		if(hardArea.isMouseOver()){
			if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
				Settings.setDifficulty("Hard");
				sbg.enterState(PongGame.GAMEPLAYSTATE, t, t);
			}
		}
		
		if(cancelArea.isMouseOver()) {
			if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON))
			sbg.enterState(PongGame.MAINMENUSTATE, t, t);
		}
		
		if(input.isKeyDown(Input.KEY_ESCAPE)) 
			sbg.enterState(PongGame.MAINMENUSTATE, t, t);
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		menuBackground.draw(0, 0);
		difficultyMenu.draw(Settings.getFrameWidth()/4, Settings.getFrameHeight()/4);
		easy.draw(firstMenuButtonX, firstMenuButtonY);
		medium.draw(firstMenuButtonX + 110, firstMenuButtonY);
		hard.draw(firstMenuButtonX + 220, firstMenuButtonY);
		cancel.draw(firstMenuButtonX + 95, firstMenuButtonY + 110);
		buttonVisualEffect();	
	}
	
	/**
	 * Temporarly decreases the button's scale if
	 * the mouse is on the button.
	 */
	private void buttonVisualEffect(){
		MouseOverArea[] areas = {easyArea, mediumArea, hardArea}; 
		Image[] images = {easy, medium, hard};
		for(int i = 0 ; i < areas.length ; i++){
			if(areas[i].isMouseOver()){
				images[i].draw(firstMenuButtonX + (i*110), firstMenuButtonY, 0.99f);
			}
		}
		// And the cancelbutton has special coordinates
		if(cancelArea.isMouseOver())
			cancel.draw(firstMenuButtonX + 95, firstMenuButtonY + 110, 0.99f);
	}


	@Override
	public int getID() {
		return stateID;
	}
}
