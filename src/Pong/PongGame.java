package Pong;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * @author Erik Norell & Daniel Aceituno
 * @version 2012-04-23
 */
public class PongGame extends StateBasedGame {
	
    public static final int MAINMENUSTATE = 0, GAMEPLAYSTATE = 1, HELPSTATE = 2,
    		ONEPLAYER = 3, TWOPLAYER = 4, DIFFICULTYMENU = 5;
    private static int width = 800, height = 600; // resolution
	
	public PongGame() {
		super("Pong");
		 
        this.addState(new MainMenuState(MAINMENUSTATE));
        this.addState(new GamePlayState(GAMEPLAYSTATE));
        this.addState(new HelpState(HELPSTATE));
        this.addState(new GamePlayState(ONEPLAYER));
        this.addState(new GamePlayState(TWOPLAYER));
        this.addState(new DifficultyMenuState(DIFFICULTYMENU));
        this.enterState(MAINMENUSTATE); // State to enter at runtime.
	}

	public static void main(String[] args) {
		try {
			AppGameContainer app = new AppGameContainer(new PongGame());
			app.setDisplayMode(width, height, false);
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
        this.getState(ONEPLAYER).init(gc, this);
        this.getState(TWOPLAYER).init(gc, this);
        this.getState(DIFFICULTYMENU).init(gc, this);
    }
	
	public static int getWidth() {
		return width;
	}
	
	public static int getHeight() {
		return height;
	}
}
