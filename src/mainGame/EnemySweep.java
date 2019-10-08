package mainGame;

import java.awt.*;

import java.util.Random;


/**
 * A type of enemy in the game
 * 
 * @author Brandon Loehle 5/30/16
 *
 */

public class EnemySweep extends GameObject {
	private Color[] colors= {Color.red, Color.blue, Color.green, Color.cyan, Color.magenta, Color.orange, Color.yellow, Color.pink};
	private Random index = new Random();

	private Color random = colors[index.nextInt(8)];
	
	public EnemySweep(Point.Double point, Handler handler) {
		super(point.x, point.y, 16, 16, handler);
		this.velX =  9;
		this.velY = 2;
		if (Math.random() > .5) {
			this.velX*=-1;
		}
		if (Math.random() > .5) {
			this.velY*=-1;
		}
		
	}

	public void tick() {
		this.x += velX;
		this.y += velY;

		// if (this.y <= 0 || this.y >= Game.HEIGHT - 43) velY *= -1;
		if (this.x <= 0 || this.x >= getHandler().getGameDimension().getWidth() - 16)
			velX *= -1;
		//check for removal once bottom of screen is hit. 
		if (this.y <= 0 || this.y >= getHandler().getGameDimension().getWidth() - 43){
            getHandler().remove(this);
			return;
		}
		
		//handler.addObject(new Trail(x, y, ID.Trail, Color.cyan, 16, 16, 0.025, this.handler));
        getHandler().add(new Trail(x, y, random, (int)width, (int)height, 0.025, getHandler()));
	}

	public void render(Graphics g) {
		g.setColor(random);
		Rectangle bounds = new Rectangle();
		g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
	}
}
