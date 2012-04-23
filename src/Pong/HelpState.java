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

public class HelpState extends BasicGameState {

	private int stateID = -1;
	private Image helpmenubackground;
    private static int width = 800, height = 600; // resolution
	
	public HelpState(int stateID) {
		this.stateID = stateID;
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		helpmenubackground = new Image("data/backgrounds/helpmenubackground.png");

	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		helpmenubackground.draw(0,0);
		int rightLineX = 540;
		int leftLineX = 50;
		int y = 50;
		
		g.drawString("Right Player controls: ", rightLineX, y);
		g.drawString("blaaablaaa", rightLineX, y+50);
		g.drawString("Left Player controls: ", leftLineX, y);
		g.drawString("blaaaablaaa", leftLineX, y+50);
		
		g.drawString("Press 'H' to return", width-200, height-30); //returnar inte till menyn dock, skippa?
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		Input input = gc.getInput();
		Transition t = new FadeOutTransition();
		
		if(input.isKeyDown(Input.KEY_ESCAPE)) 
			sbg.enterState(PongGame.GAMEPLAYSTATE, t, t);

		if(input.isKeyDown(Input.KEY_H)) 
			sbg.enterState(PongGame.GAMEPLAYSTATE, t, t);
		
	}

	@Override
	public int getID() {
		return stateID;
	}

}
