package mainGame;


import java.awt.*;

/**
 * A type of enemy in the game
 * 
 * @author Brandon Loehle 5/30/16
 *
 */

public class EnemyFast extends GameObject {
	public EnemyFast(Point.Double point, Handler handler) {
		super(point.x, point.y, 32, 64, handler);
		velX = 1 - 2*Math.random();
		velY = -12;
	}

    public void tick() {
		this.x += velX;
		this.y += velY;

		if (this.y <= 0) {
			velY = Math.abs(velY);
		}
        if (this.y >= getHandler().getGameDimension().getHeight() - 40) {
            velY = -Math.abs(velY);
        }
		if (this.x <= 0) {
			velX = Math.abs(velX);
		}
		if (this.x >= getHandler().getGameDimension().getWidth() - 16) {
		    velX = -Math.abs(velX);
        }

		//handler.addObject(new Trail(x, y, ID.Trail, Color.cyan, 16, 16, 0.025, this.handler));
	}

	@Override
    public void render(Graphics g) {
        if (velY > 0) {
            g.drawImage(getHandler().getTheme().get(this), (int)x, (int)y+64,(int)width,(int)-height, null);
        }
        else {
            g.drawImage(getHandler().getTheme().get(this), (int)x, (int)y,(int)width,(int)height, null);
        }
    }
 }
