package Pong;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * This is the main class, basically it just initiates
 * all game-states and starts game.
 * @author Erik Norell & Daniel Aceituno
 * @version 2012-04-23
 */
public class PongGame extends StateBasedGame {
	
    public static final int MAINMENUSTATE = 0, GAMEPLAYSTATE = 1, HELPSTATE = 2, DIFFICULTYMENU = 3;
	
	public PongGame() {
		super("Pong");
		 
        this.addState(new MainMenuState(MAINMENUSTATE));
        this.addState(new GamePlayState(GAMEPLAYSTATE));
        this.addState(new HelpState(HELPSTATE));
        this.addState(new DifficultyMenuState(DIFFICULTYMENU));
        this.enterState(MAINMENUSTATE); // State to enter at runtime.
	}

	public static void main(String[] args) {
		try {
			AppGameContainer app = new AppGameContainer(new PongGame());
			app.setDisplayMode(Settings.getFrameWidth(), Settings.getFrameHeight(), false);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Initiates the game's states and adds
	 * them to the same GameContainer.
	 */
	@Override
    public void initStatesList(GameContainer gc) throws SlickException {
 
        this.getState(MAINMENUSTATE).init(gc, this);
        this.getState(GAMEPLAYSTATE).init(gc, this);
        this.getState(HELPSTATE).init(gc, this);
        this.getState(DIFFICULTYMENU).init(gc, this);
    }
}
