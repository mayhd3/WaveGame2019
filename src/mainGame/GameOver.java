package mainGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

/**
 * The game over screen
 * 
 * @author Brandon Loehle 5/30/16
 *
 */

public class GameOver extends GameState {

	private int timer;
	private Color retryColor;
	private String text;
	private Image img;
	private Waves game;
	public GameOver(Waves waves) {
	    game = waves;
		timer = 90;
		this.retryColor = Color.white;
		//the background image is the same as the menu background S
		img = getImage("/images/Background.png");
	}
	public void tick() {
		game.getHandler().clearPlayer();
		flash();
	}

	public void render(Graphics g) {
		//render the background image
		g.drawImage(img, 0, 0, (int)game.getHandler().getGameDimension().getWidth(), (int)game.getHandler().getGameDimension().getHeight(), null);
		//Set up the font
		Font font = new Font("Amoebic", 1, 100);
		Font font2 = new Font("Amoebic", 1, 60);
		//Game Over Font
		g.setFont(font);
		g.setColor(Color.white);
		text = "Game Over";
		g.drawString(text, (int)game.getHandler().getGameDimension().getWidth() / 2 - getTextWidth(font, text) / 2, (int)game.getHandler().getGameDimension().getHeight() / 2 - 150);
		//The level the player died on
		g.setFont(font2);
		g.setColor(Color.white);
		text = "Level: " + game.getHUD().getLevel();
		g.drawString(text, 100, 500);
		//Get the high score of the PLAYER
		g.setFont(font2);
		g.setColor(Color.white);
		text = "Your Score: " + game.getHUD().getScore();
		g.drawString(text, (int)game.getHandler().getGameDimension().getWidth() / 2 - getTextWidth(font2, text) / 2, 500);

		//This is the high score from the text file
		try {
			BufferedReader reader = new BufferedReader(new FileReader("src/HighScores.txt"));
			String trueHighScore =reader.readLine();
			//draw the high score text string
			g.setFont(font2);
			text = "High Score:" + trueHighScore;
			g.drawString(text, 1400, 500);
			g.setColor(Color.white);
		}
		catch (IOException e) {
			System.out.println(e);
			System.exit(1);
		}

		//g.drawString(text, Game.WIDTH / 2 - getTextWidth(font2, text) / 2, Game.HEIGHT / 2 + 50);
		//Text flashing
		g.setColor(this.retryColor);
		g.setFont(font2);
		text = "Click anywhere to play again";
		g.drawString(text, (int)game.getHandler().getGameDimension().getWidth() / 2 - getTextWidth(font2, text) / 2, (int)game.getHandler().getGameDimension().getHeight() / 2 + 150);
	}

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        game.getHandler().object.clear();
        game.getHUD().health = 100;
        game.getHUD().setScore(0);
        game.getHUD().setLevel(1);
        game.setState(game.getMenu());
    }


    //This really isn't "flashing" so much as it's changing the color of the text to black then white
	public void flash() {
		timer--;
		if (timer == 45) {
			this.retryColor = Color.black;
		} else if (timer == 0) {
			this.retryColor = Color.white;
			timer = 90;
		}
	}

	/**
	 * Function for getting the pixel width of text
	 * 
	 * @param font
	 *            the Font of the test
	 * @param text
	 *            the String of text
	 * @return width in pixels of text
	 */
	public int getTextWidth(Font font, String text) {
		AffineTransform at = new AffineTransform();
		FontRenderContext frc = new FontRenderContext(at, true, true);
		int textWidth = (int) (font.getStringBounds(text, frc).getWidth());
		return textWidth;
	}
	/**
	 * Function for getting the path of the image background
	 */
	public Image getImage(String path) {
		Image image = null;
		try {
			URL imageURL = Client.class.getResource(path);
			image = Toolkit.getDefaultToolkit().getImage(imageURL);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return image;
	}
}
