package mainGame;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
/**
 * This class is meant to be a generic level that classes implementing gamemode can use to generate and throw away levels of different parameters.
 * 
  * @author Joe Passanante 11/28/17
 */
public class Level extends GameState {
	private boolean levelRunning = true;
	private int maxTick = 0;
	private ArrayList<ID> enemyList;
	private ArrayList<Integer> spawnLimits;
	private ArrayList<Integer> spawnTicks;
	private int currentTick = 0;
	private boolean bossDead = false;
	int dif = 0;
	int x;
    private LevelText t;
    private int levelPopTimer = 0;
    Waves game;
	/**
	 * 
	 * @param g - The game class that the gamemode is apart of.
	 * @param dif - The level of difficulty (This can be left at 0)
	 * @param enemyList - The list of Enemy ID's that the level can spawn
	 * @param maxSpawn - The corresponding spawn rates of the enemyList (index for index, must be equal in size) 
	 * @param maxTick - The time the level takes to complete, if boss level leave at -1
	 * @param spawnPowerUp - True/False for spawning PowerUps(Not Implemented)
	 * @param upgrades - True/False if when the level is completed the player can choose a upgrade (Not Implemented)
	 */
	public Level(Waves g,int dif, ArrayList<ID> enemyList, ArrayList<Integer>maxSpawn,int maxTick,boolean spawnPowerUp, boolean upgrades, int currentLevelNum){
	    game = g;
		this.enemyList = enemyList;
		this.spawnLimits = maxSpawn;
		this.maxTick = maxTick;
		this.spawnTicks = new ArrayList<Integer>();
		for(int i = 0; i<enemyList.size();i++){
			spawnTicks.add(0);
		}

        t = new LevelText(
                game.getHandler().getGameDimension().getWidth() / 2 - 675,
                game.getHandler().getGameDimension().getHeight() / 2 - 200,
                "Level " + currentLevelNum + (currentLevelNum%5 == 0 ? ": Boss Level!!!":""),
                ID.Levels1to10Text,
                game.getHandler()
        );
		if(currentLevelNum > 1) {
            game.getHandler().addObject(t);
        }
    }
	/**
	 * Tick spawns new enemies depending on their spawn limit and current tick.
	 * Takes the max level tick, and for every enemy divides it by the # of enemies.
	 * It then spawns that enemy every X ticks depending on the volume of spawns. 
	 * This ensures that the enemies are evenly spawned throughout the level. 
	 */
	public void tick(){
        game.getHandler().tick();// handler must always be ticked in order to draw all entities.

        currentTick++;
        this.levelPopTimer++;
        //after 3 seconds, the handler would remove the level text object "t".
        if(this.levelPopTimer>=100){
            game.getHandler().removeObject(t);
        }

		if (game.getHUD() != null && maxTick >= 0) {
		    game.getHUD().levelProgress = (int) (((double)currentTick/(double)maxTick)*100);
		}
		if(currentTick>=maxTick && maxTick>=0) this.levelRunning = false;
		if(running()==false) {
		    game.setState(game.getCurrentLevel());
		    return;
        }
		this.currentTick++;
	
		for(int i = 0; i<enemyList.size();i++){ //run through all the enemies we can spawn
			//check if we should even be spawning them(max spawn?)
			if(this.spawnTicks.get(i)==0){
				//check if its the right tick we should be checking?
				if(this.spawnLimits.get(i)>0){
					//if good time, spawn 1 enemy, subtract from max spawn and reset tick counter.
					game.getHandler().addObject(game.getEnemyFromID(this.enemyList.get(i), getSpawnLoc()));
					this.spawnLimits.set(i, this.spawnLimits.get(i)-1);
					this.spawnTicks.set(i,(this.maxTick-this.currentTick)/(this.spawnLimits.get(i)+1));
				}
			}else{
				this.spawnTicks.set(i, this.spawnTicks.get(i)-1);
			}
		}
		if(this.maxTick<=-1 && this.currentTick>=25){
			this.bossDead = this.checkIfBossDead();
			if(this.bossDead){
				System.err.println("Boss is dead");
				//spawn upgrade screen.
				//check if upgrade has been picked
				this.levelRunning = false; //end the level
			}
		}
		
	}
	private Point getSpawnLoc(){
		int x = (int)((Math.random()*(game.getHandler().getGameDimension().getWidth()*1.2))-game.getHandler().getGameDimension().getWidth()*0.1); //20% increase for a 10% margin.
		int y = (int)((Math.random()*(game.getHandler().getGameDimension().getHeight()*1.2))-game.getHandler().getGameDimension().getHeight()*0.1);
		if(Math.sqrt(Math.pow((game.getPlayer().getX()-x), 2) + Math.pow((game.getPlayer().getY()-y), 2))<=game.getPlayer().playerWidth*5){ //don't spawn within 5X of player size
			return getSpawnLoc(); //try another point
		}
		if(x>=game.getHandler().getGameDimension().getWidth()-50 || y>=game.getHandler().getGameDimension().getHeight()-50 || y < 50 || x < 50){
			return getSpawnLoc(); //try another point
		}
		return new Point(x,y);
	}
	/**
	 * render anything that is specific to this level(not static content for the gamemode itself. 
	 * @param g Passed by parent (IE gamemode).
	 */
	public void render(Graphics g){
		
	}
	private boolean checkIfBossDead(){
		boolean isDead = true;
		for(GameObject b: game.getHandler().object){
			for(ID id: this.enemyList){
				if (b.id==id) isDead = false;
			}
		}
		return isDead;
	}
	public boolean running(){
		return this.levelRunning;
	}
}
