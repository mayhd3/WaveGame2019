package game.pickup;

import game.GameEntity;
import game.GameLevel;
import game.Player;

import javax.sound.sampled.Clip;

public class Pickup extends GameEntity.Bouncing {
    public Pickup(GameLevel level) {
        this(level,0);
    }

    public Pickup(GameLevel level, int h) {
        super(level.spawnPoint(), 30, 30, level);
        setHealth(h);
    }

    private boolean clipped;
    @Override
    public void render(Clip c, int i) {
        if(clipped) {
            super.render(c, i);
            clipped = false;
        }
    }

    @Override
    public void collide(Player player) {
        getLevel().getEntities().remove(this);
        player.getInactive().add(0, this);
        clipped = true;
    }

    @Override
    public void tick() {
        if(getHealth() < 0) {
            getLevel().getEntities().remove(this);
        }
    }

    public void affect(Player player) {
        setHealth(getHealth() - 1);
        if(getHealth() < 0) {
            player.getActive().remove(this);
        }
    }

    public static class Active extends Pickup {
        public Active(GameLevel level) {
            super(level);
        }

        public void collide(Player player) {
            super.collide(player);
            player.getActive().add(0, player.getInactive().get(0));
        }
    }
}
