package game.enemy;

import game.GameLevel;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * @author Brandon Loehle 5/30/16
 * @author David Nguyen 12/13/17
 * #author Aaron Paterson 10/24/19
 */

public class EnemyBoss extends Enemy.Bouncing {
    private int currentTick;

    public EnemyBoss(GameLevel level) {
        super(new Point2D.Double(level.getDimension().getWidth() / 2, -100), 96, 96, level);
        setHealth(1000); //full health is 1000
    }

    public void tick() {
        super.tick();

        if (currentTick == 0) {
            setVelX(0);
            setVelY(2);
        } else if (currentTick == 80) {
            setVelY(0);
            setVelX(8);
        } else if (currentTick > 80) {
            if (getLevel().getRandom().random() < .2) {
                getLevel().getEntities().add(new EnemyBossBullet(new Point.Double(getPosX() + 48, getPosY() + 80), getLevel()));
                setHealth(getHealth() - 10);
            }
            if (getLevel().getNumber() > 10 && getLevel().getRandom().random() < 1.0 / 60) {
                getLevel().getEntities().add(new EnemyBossBomb(new Point2D.Double(getPosX() + 48, getPosY() + 96), getLevel(), getLevel().getNumber()));
                setHealth(getHealth() - 10);
            }
        }

        // handler.addObject(new Trail(x, y, ID.Trail, Color.red, 96, 96, 0.025,
        // this.handler));
        currentTick += 1;
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        g.drawLine(0, 138, (int) getLevel().getDimension().getWidth(), 138);
        super.render(g, super.getBounds());

        // HEALTH BAR
        g.setColor(Color.GRAY);
        g.fillRect((int) getLevel().getDimension().getWidth() / 2 - 500, (int) getLevel().getDimension().getHeight() - 150, 1000, 50);
        g.setColor(Color.RED);
        g.fillRect((int) getLevel().getDimension().getWidth() / 2 - 500, (int) getLevel().getDimension().getHeight() - 150, (int) getHealth(), 50);
        g.setColor(Color.WHITE);
        g.drawRect((int) getLevel().getDimension().getWidth() / 2 - 500, (int) getLevel().getDimension().getHeight() - 150, 1000, 50);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(0, 0, getLevel().getDimension().width, 200);
    }
}
