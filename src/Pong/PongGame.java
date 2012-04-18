package Pong;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * @author Erik Norell & Daniel Aceituno
 * @version 2012-04-16
 */
public class PongGame extends StateBasedGame {
	
    public static final int MAINMENUSTATE = 0;
    public static final int GAMEPLAYSTATE = 1;
    private static int width = 800, height = 600; // resolution
	
	public PongGame() {
		super("Pong");
		 
        this.addState(new MainMenuState(MAINMENUSTATE));
        this.addState(new GamePlayState(GAMEPLAYSTATE));
        this.enterState(MAINMENUSTATE);
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
	 * 
	 */
	@Override
    public void initStatesList(GameContainer gc) throws SlickException {
 
        this.getState(MAINMENUSTATE).init(gc, this);
        this.getState(GAMEPLAYSTATE).init(gc, this);
    }
}
