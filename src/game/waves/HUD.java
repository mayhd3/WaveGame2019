package game.waves;

import game.Animatable;
import game.GameObject;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

/**
 * The main Heads Up Display of the game
 * 
 * @author Brandon Loehle 5/30/16
 *
 */

public class HUD implements Animatable {
	public double health = 100;
	private double healthMax = 100;
	private double greenValue = 255;

	private int score = 0;
	private int level = 0;

	private boolean regen = false;
	private String regenString = "Disabled";
	private int timer = 60;
	private int healthBarWidth = 400;
	private int healthBarModifier = 2;
	private boolean doubleHealth = false;
	private String ability = "";
	private int abilityUses;

	private Color scoreColor = Color.white;

	private int extraLives = 0;
	public int levelProgress;

	//calls background and each of the damage resistance shields
	private Image img;
	private Image HUDshield1;
	private Image HUDshield2;
	private Image HUDshield3;
	private Image HUDshield4;
	private Image HUDshield5;

	Waves game;
	public HUD(Waves waves) {
	    game = waves;

        HUDshield1 = game.getHandler().getTheme().get("shield1");
        HUDshield2 = game.getHandler().getTheme().get("shield2");
        HUDshield3 = game.getHandler().getTheme().get("shield3");
        HUDshield4 = game.getHandler().getTheme().get("shield4");
        HUDshield5 = game.getHandler().getTheme().get("shield5");
    }

	//game uses tick method to check amount of health player has and update health bar display. Also updates score using this method
	public void tick() {
		health = GameObject.clamp(health, 0, health);
		greenValue = GameObject.clamp(greenValue, 0, 255);
		greenValue = health * healthBarModifier;
		greenValue = GameObject.clamp(greenValue, 0, 255);
		score++;

		if (regen) {// regenerates health if that ability has been unlocked
			timer--;
			if (timer == 0 && health < 100) {
				health += 1;
				timer = 60;
			}
		}
	}

	public void render(Graphics g) {
		Font font = new Font("Amoebic", 1, 30);
		
		g.drawImage(img, 0, 0, (int)game.getHandler().getGameDimension().getWidth(), (int)game.getHandler().getGameDimension().getHeight(), null);

		g.setColor(Color.GRAY);
		g.fillRect(15, 1000, healthBarWidth, 64);
		g.setColor(new Color(75, (int) greenValue, 0));
		g.fillRect((int) 15, (int) 1000, (int) health * 4, 64);
		g.setColor(scoreColor);
		g.drawRect(15, 1000, healthBarWidth, 64);

		g.setFont(font);

		regenString = regen ? "Enabled" : "Disabled";

		//displays elements of the HUD
		g.drawString("Score: " + score, 15, 25);
		g.drawString("Level: " + level, 15, 75);
		g.drawString("Extra Lives: " + extraLives, 15, 125);
		g.drawString("Level Progress: " + levelProgress + "%", 15, 175);
		g.drawString("Health: " + (int)health + "/" + (int)healthMax, 15, 1050);
		g.drawString("Player Size: " + String.format("%.2f",game.getPlayer().getWidth()), 15, 225);
		g.drawString("Regeneration: " + regenString, 15, 275);
		g.drawString("High Score: " + game.getHandler().getHighScore(), 1500, 25);
		
		//this switch statement updates the damage resistance sprite on the HUD
		Image shieldImg; 
		switch ((int)((2 -game.getPlayer().getDamage())*100)){
			case 0: shieldImg =  HUDshield1;break;
			case 25: shieldImg =  HUDshield2;break;
			case 50: shieldImg =  HUDshield3;break;
			case 75: shieldImg =  HUDshield4;break;
			default: shieldImg = HUDshield5;break;
		}
			//prints damage resistance next to HUD along with sprite
			g.drawImage(shieldImg, healthBarWidth+40, 1010, 40, 40, null);
			g.drawString("" + (2 -game.getPlayer().getDamage()),healthBarWidth+100, 1040);

		
		
		//this is a tough one, can't figure out what it does
		if(game.getHandler().getHighScore() < score){
            game.getHandler().setHighScore(score);
		}
		

		//if the player has an ability, display that to the screen
		if (ability.equals("freezeTime")) {//comment
			g.drawString("Time Freezes: " + abilityUses, (int)game.getHandler().getGameDimension().getWidth() - 300, 64);
		} else if (ability.equals("clearScreen")) {
			g.drawString("Screen Clears: " + abilityUses, (int)game.getHandler().getGameDimension().getWidth() - 300, 64);
		} else if (ability.equals("levelSkip")) {
			g.drawString("Level Skips: " + abilityUses, (int)game.getHandler().getGameDimension().getWidth() - 300, 64);
		}
	}
	
	//series of methods which activate and set a number of different things within the game such as score and amount of lives

	public void setAbility(String ability) {
		this.ability = ability;
	}

	public int getAbilityUses() {
		return this.abilityUses;
	}

	public void setAbilityUses(int abilityUses) {
		this.abilityUses = abilityUses;
	}

	public void updateScoreColor(Color color) {
		this.scoreColor = color;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getScore() {
		return score;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public void setRegen() {
		regen = true;
	}

	public void resetRegen() {
		regen = false;
	}

	public void setExtraLives(int lives) {
		this.extraLives = lives;
	}

	public int getExtraLives() {
		return this.extraLives;
	}

	public void healthIncrease() {
		doubleHealth = true;
		healthMax +=20;
		this.health = healthMax;
		healthBarModifier = 1;
		healthBarWidth = (int) (healthMax*4);
	}

	public void resetHealth() {
		doubleHealth = false;
		healthMax = 100;
		this.health = healthMax;
		healthBarModifier = 2;
		healthBarWidth = 400;
	}

	public void restoreHealth() {
		this.health = healthMax;
	}

    public boolean getRegen() {
        return regen;
    }
}
