package mainGame;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

public class Waves extends GameMode {
	private int currentLevelNum = 0;
	protected int maxTick = 2000,currentTick = 0;
	private Random r = new Random();
	private ArrayList<ID> currentEnemy;
	private String[] side = { "left", "right", "top", "bottom" };
	private Level currentLevel = null;
	private ArrayList<Integer> currentEnemySpawns;
	private static Image img;
	private int levelPopTimer = 0;
	private LevelText t;
	private ID lastEnemy = null;
	private ID lastBoss = (Math.random()*1 == 0 ? ID.EnemyBoss:ID.EnemyRocketBoss);

    public Waves(Game g) {
        super(g);
    }

    //Links the ID of an enemy to actual creation.
	//This allows the gameMode to override the generic Level Spawning Scheme. IE if a boss doesn't care where a player is. 
	@Override
	public GameObject getEnemyFromID(ID enemy,Point spawnLoc){
		switch(enemy){
		case EnemyBasic:  return new EnemyBasic(spawnLoc.getX(), spawnLoc.getY(), 9, 9, ID.EnemyBasic, game.getHandler());
		case EnemySmart: return new EnemySmart(spawnLoc.getX(), spawnLoc.getY(), -5, ID.EnemySmart, game.getHandler());
		case EnemySweep: return new EnemySweep(spawnLoc.getX(), spawnLoc.getY(), 9, 2, ID.EnemySweep, game.getHandler());
		case EnemyShooter: return new EnemyShooter(spawnLoc.getX(),spawnLoc.getY(), 100, 100, -20 + (int)(Math.random()*5), ID.EnemyShooter, game.getHandler());
		case EnemyBurst: return new EnemyBurst(-200, 200, 15, 15, 200, side[r.nextInt(4)], ID.EnemyBurst, game.getHandler());
		//case BossEye: return new EnemyBoss(ID.EnemyBoss, handler);
		case EnemyBoss: return new EnemyBoss(ID.EnemyBoss, game.getHandler(),currentLevelNum/10,game.getHUD());
		case EnemyRocketBoss: return new EnemyRocketBoss(100,100,ID.EnemyRocketBoss,game.getPlayer(), game.getHandler(),game.getHUD(), this,currentLevelNum/10);
		case EnemyFast: return new EnemyFast(spawnLoc.getX(), spawnLoc.getY(), ID.EnemySmart, game.getHandler());
		case EnemyShooterMover: return new EnemyShooterMover(spawnLoc.getX(),spawnLoc.getY(), 100, 100, -20 + (int)(Math.random()*5), ID.EnemyShooterMover, game.getHandler());
		case EnemyShooterSharp: return new EnemyShooterSharp(spawnLoc.getX(),spawnLoc.getY(), 200, 200, -20 + (int)(Math.random()*5), ID.EnemyShooter, game.getHandler());
		default: 
			System.err.println("Enemy not found");
			return new EnemyBasic(spawnLoc.getX(),spawnLoc.getY(), 9, 9, ID.EnemyBasic, game.getHandler());
		}
	}
	
	/**
	 * Generates a random enemy ID
	 * @return ID (for entities)
	 */
	private ID randomEnemy(){	
		int r = (int)(Math.random()*5); //0-6 can be generated
		ID returnID = null;
		System.out.println("Enemy type of level " + this.currentLevelNum + " is " + r);
		switch(r){ //pick what enemy the random integer represents
			case 0: returnID = ID.EnemySmart; break;
			case 1: returnID = ID.EnemyBasic; break;
			case 2: returnID = ID.EnemyShooter; break;
			case 3: returnID = ID.EnemyBurst; break;
			case 4: returnID = ID.EnemyFast; break;
			default: returnID = randomEnemy(); break;
		}
		System.out.println(returnID + "| " + this.lastEnemy);
		if(returnID == this.lastEnemy){
			returnID = this.randomEnemy();
		}
		this.lastEnemy = returnID;
		return returnID;
	}
	
	/**
	 * Generates a random enemy ID
	 * @return ID (for entities)
	 */
	private ID randomEnemyHard(){	
		int r = (int)(Math.random()*3);
		ID returnID = null;
		System.out.println("Hard Enemy type of level " + this.currentLevelNum + " is " + r);
		switch(r){ //pick what enemy the random integer represents
			case 0: returnID = ID.EnemyShooterMover;break;
			case 1: returnID = ID.EnemySweep; break;
			case 2: returnID = ID.EnemyShooterSharp; break;
			default: returnID = randomEnemyHard(); break;
		}
		System.out.println(returnID + "| " + this.lastEnemy);
		if(returnID == this.lastEnemy){
			returnID = this.randomEnemyHard();
		}
		this.lastEnemy = returnID;
		return returnID;
	}
	/**
	 * Ticks Level classes generated.
	 * Generates levels when they are completed. 
	 */
	@Override
	public void tick() {
		currentTick++;
		this.levelPopTimer++;
		//after 3 seconds, the handler would remove the level text object "t".
		if(this.levelPopTimer>=100){
            game.getHandler().removeObject(t);
		}
		if(currentLevel==null || currentLevel.running()==false){
			this.currentLevelNum = this.currentLevelNum + 1;
			game.getHUD().setLevel(this.currentLevelNum);
            game.getHandler().clearEnemies();
			this.levelPopTimer = 0;
			t = new LevelText(
                    game.getHandler().getGameDimension().getWidth() / 2 - 675,
                    game.getHandler().getGameDimension().getHeight() / 2 - 200,
                "Level " + this.currentLevelNum + (this.currentLevelNum%5 == 0 ? ": Boss Level!!!":""),
                ID.Levels1to10Text,
                game.getHandler()
            );
            game.getHandler().addObject(t);
			
			double tempx = (Math.random()*(game.getHandler().getGameDimension().getWidth()-300))+150;
			double tempy = (Math.random()*(game.getHandler().getGameDimension().getHeight()-300))+150;
			switch ((int)(Math.random()*5)){
			case 0: game.getHandler().addObject(new PickupSize(tempx,tempy));break;
			case 1: game.getHandler().addObject(new PickupHealth(tempx,tempy));break;
			case 2: game.getHandler().addObject(new PickupLife(tempx,tempy));break;
			case 3: game.getHandler().addObject(new PickupScore(tempx,tempy));break;
			case 4: game.getHandler().addObject(new PickupFreeze(tempx,tempy));break;
			}
			if(this.currentLevelNum%5 == 0){
				ArrayList<Integer>bossLimit = new ArrayList<Integer>();
				bossLimit.add(1);
				System.out.println("New Boss Level");
				currentLevel = new Level(this.game, 0,randomBoss(), bossLimit, -1 , false, false);
			} else{
				if ((currentLevelNum%5)-1 == 0 && currentLevelNum > 1) {game.setGameState(game.getUpgradeScreen());
				    game.setPaused(true);
				}
				System.out.println("New Normal Level");
				this.createNewEnemyLists();
				System.out.println(this.currentEnemy.size());
				System.out.println(this.currentEnemySpawns.size());
				currentLevel = new Level( this.game,0, this.currentEnemy,this.currentEnemySpawns,60*(20),false,false);
			}
			
		}
		currentLevel.tick();
		
	}
	/**
	 * Creates a new list of enemies for the next level to spawn.
	 * Sets the new list as a global variable for the game to access later. 
	 * (This can be later changed to return a list and can be passed into the level class | removing the global variable)
	 * Problem - Java Tuples cannot return both an arraylist of enemies, and the # of times they spawn. 
	 */
	private void createNewEnemyLists() {
		ArrayList<ID>newEnemy = new ArrayList<ID>();
		ArrayList<Integer>newSpawn = new ArrayList<Integer>();
		int curr = this.currentLevelNum/5;
		do{
			curr--;ID e = this.randomEnemy();
			if (curr >= 1) {//potential for a harder enemy to spawn
			if (curr >= 3 || Math.random() > .5) {
				 e = this.randomEnemyHard();
				 curr--;
			}
			}
			
			newEnemy.add(e);
			int s = (e.getDifficuty() + (int)(Math.random()*((e.getDifficuty()*0.1))));
			if(e.getDifficuty()==1)
				s = 1;
			newSpawn.add(s);
			System.out.println("----" + e + "-----" + s);
		}while(curr>= 0);
		this.currentEnemy = newEnemy;
		this.currentEnemySpawns = newSpawn;
		
	}
	/**
	 * 
	 * @return Returns an array of enemy bosses to be generated. 
	 * As of right now, enemy bosses are hard coded to only spawn once during a level. 
	 * See tick above.
	 */
	private ArrayList<ID> randomBoss() {
		ArrayList<ID>bossReturn = new ArrayList<ID>();
		if(this.lastBoss==ID.EnemyRocketBoss){
			System.out.println("Enemy Boss");
			bossReturn.add(ID.EnemyBoss);
			this.lastBoss = ID.EnemyBoss;
		}else{
			System.out.println("Enemy Rocket Boss");
			this.lastBoss = ID.EnemyRocketBoss;
			bossReturn.add(ID.EnemyRocketBoss);
		}
		return bossReturn;
	}
	/**
	 * Renders any static images for the level.
	 * IE Background. 
	 */
	@Override
	public void render(Graphics g) {
		g.drawImage(img, 0, 0, (int)game.getHandler().getGameDimension().getWidth(), (int)game.getHandler().getGameDimension().getHeight(), null);
	}

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    /**
	 * @param hardReset - if false only enemies are wiped. If true gamemode is completely reset. 
	 */
	@Override
	public void resetMode(boolean hardReset) {
		this.currentTick = 0;
		this.currentEnemy = null;
		this.currentLevel =  null;
        game.getHandler().clearEnemies();
		if(hardReset) {
			this.currentLevelNum = 0;
			game.getPlayer().playerWidth = 32;
			game.getPlayer().playerHeight = 32;
			game.getHUD().setExtraLives(0);
			game.getHUD().resetHealth();
		}
	}
	@Override
	public void resetMode() {
		resetMode(true);
	}

    public static void updateSprite(Themes theme) {
        // Set sprite based on current theme
        try {
            switch (theme) {
                case Space:
                    img = ImageIO.read(new File("src/images/space2.jpg"));
                    break;
                case Underwater:
                    img = ImageIO.read(new File("src/images/Water.jpg"));
                    break;
            }
        } catch (IOException e) {
            System.err.println("Error reading sprite file for Waves (game background)");
        }
    }

}
