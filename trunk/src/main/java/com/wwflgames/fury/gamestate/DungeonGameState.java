package com.wwflgames.fury.gamestate;

import com.wwflgames.fury.Fury;
import com.wwflgames.fury.entity.*;
import com.wwflgames.fury.main.AppState;
import com.wwflgames.fury.map.*;
import com.wwflgames.fury.mob.Mob;
import com.wwflgames.fury.monster.Monster;
import com.wwflgames.fury.player.Player;
import com.wwflgames.fury.player.PlayerController;
import com.wwflgames.fury.util.Log;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class DungeonGameState extends BasicGameState {
    private GameContainer gameContainer;
    private StateBasedGame stateBasedGame;
    private EntityManager entityManager;
    private AppState appState;
    private SpriteSheetFactory spriteSheetFactory;
    private PlayerController playerController;
    private Entity miniMap;

    public DungeonGameState(AppState appState, SpriteSheetFactory spriteSheetFactory) {
        this.appState = appState;
        this.spriteSheetFactory = spriteSheetFactory;
    }


    @Override
    public int getID() {
        return Fury.DUNGEON_GAME_STATE;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        this.gameContainer = gameContainer;
        this.stateBasedGame = stateBasedGame;
    }

    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        entityManager = new EntityManager(gameContainer, stateBasedGame);
        showNewMap();
    }

    private void showNewMap() throws SlickException {
        entityManager.removeAllEntities();

        DungeonMap map = appState.getMap();

        playerController = new PlayerController(appState.getPlayer(), map);

        // create a dungeonMap entity
        Entity mapEntity = new Entity("dungeonMap")
                .setPosition(new Vector2f(0, 0))
                .setScale(1)
                .addComponent(new DungeonMapRenderer("mapRender", map, playerController));

        entityManager.addEntity(mapEntity);

        // player entity
        Entity player = new Entity("player")
                .setPosition(new Vector2f(0, 0))
                .setScale(1)
                .addComponent(new SpriteSheetRenderer("playerRender",
                        spriteSheetFactory.spriteSheetForName(appState.getPlayer().getProfession().getSpriteSheet()))
                        .useSprite(1, 2))
                .addComponent(new MobMapPositionAction("mapPosition", appState.getPlayer(), playerController));

        entityManager.addEntity(player);

        // grab all of the monsters on the dungeonMap
        for (Monster monster : map.getMonsterList()) {
            Entity monsterEntity = new Entity("monster" + monster.name())
                    .setPosition(new Vector2f(0, 0))
                    .setScale(1)
                    .addComponent(new MonsterRenderer("monsterRenderer",
                            spriteSheetFactory.spriteSheetForName(monster.getSpriteSheet()), monster)
                            .useSprite(1, 2))
                    .addComponent(new MobMapPositionAction("mapPosition", monster, playerController));

            entityManager.addEntity(monsterEntity);
        }

        // add the fog of war
        Entity fogOfWar = new Entity("fogOfWar")
                .setPosition(new Vector2f(0, 0))
                .setScale(1)
                .addComponent(new MapFogOfWarRenderer("forRenderer", map, playerController));

        entityManager.addEntity(fogOfWar);

        // add the mini-map
        float mmScale = 400f / (float) (map.getWidth() * 32);
        float mmHeight = map.getHeight() * 32 * mmScale;
        miniMap = new Entity("miniMap")
                .setScale(mmScale)
                .setPosition(new Vector2f(200, 300 - (mmHeight / 2)))
                .setVisible(false)
                .addComponent(new MiniDungeonMapRenderer("mapRender", map, playerController));

        entityManager.addEntity(miniMap);
    }

    @Override
    public void leave(GameContainer container, StateBasedGame game) throws SlickException {
        Log.debug("DungeonGameState-> leaving state");
        entityManager.clear();
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics)
            throws SlickException {

        if (entityManager != null) {
            entityManager.render(graphics);
        }

    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException {

        if (entityManager != null) {
            entityManager.update(delta);
        }

    }

    public void keyPressed(int key, char c) {
        Log.debug("key = " + key);

        if (key == 32) {
            // go back to dungeon screen
            stateBasedGame.enterState(Fury.MANAGE_DECK_STATE);
        }

        if (key == 50) {
            miniMap.setVisible(!miniMap.isVisible());
        }

        Direction d = Direction.forKey(key);
        if (d != null) {
            //TODO: meh i dont like the slick exception that has propogated to here :(
            try {
                tryMoveAndMaybeAttack(d.getDx(), d.getDy());
            } catch (SlickException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {
        if ( button == Input.MOUSE_LEFT_BUTTON && clickCount == 1) {
            Log.debug("Mouse clicked at " + x + "," + y);
            Rectangle r = playerController.getPlayerRectangle();
            int dx = 0;
            int dy = 0;
            if ( x > r.getMaxX() ) {
                dx = 1;
            }
            if ( x < r.getMinX() ) {
                dx = -1;
            }
            if ( y > r.getMaxY() ) {
                dy = 1;
            }
            if ( y < r.getMinY() ) {
                dy = -1;
            }
            try {
                tryMoveAndMaybeAttack(dx,dy);
            } catch (SlickException e) {
                e.printStackTrace();
            }
        }
    }

    private void tryMoveAndMaybeAttack(int dx, int dy) throws SlickException {
        Player player = appState.getPlayer();
        Tile tile = player.getCurrentMapTile();
        int currX = tile.getX();
        int currY = tile.getY();
        int newX = currX + dx;
        int newY = currY + dy;
        DungeonMap dungeonMap = appState.getMap();

        Log.debug("Inspecting " + newX + "," + newY);

        // first, see if moving to newX,newY would cause combat
        if (!dungeonMap.inBounds(newX, newY)) {
            Log.debug("Out of bounds");
            return;
        }
        Tile newTile = dungeonMap.getTileAt(newX, newY);
        Mob enemy = newTile.getMob();
        Log.debug("Enemy present, enemy was " + enemy);
        if (enemy != null) {
            Log.debug("about to initiate combat");
            initiateCombat(player);
        } else if (newTile.getType() == TileType.STAIR && !newTile.getStairs().areLocked()) {
            changeLevel(newTile.getStairs());
        } else if (dungeonMap.inBounds(newX, newY) && dungeonMap.isWalkable(newX, newY)) {
            playerController.movePlayerTo(newX, newY);
        } else {
            Log.debug("Hit a wall!");
        }
    }

    private void changeLevel(Stairs stairs) throws SlickException {

        DungeonMap oldMap = appState.getMap();
        DungeonMap newMap = stairs.mapAtOtherEndFrom(oldMap);

        appState.getDungeon().setCurrentLevel(newMap);

        Tile newMapTile = stairs.tileAtOtherEndFrom(oldMap);
        oldMap.removeMob(appState.getPlayer());
        newMap.addMob(appState.getPlayer(), newMapTile.getX(), newMapTile.getY());

        showNewMap();
    }


    private void initiateCombat(Mob initiator) {
        if (initiator instanceof Player) {
            appState.setPlayerInitiative(true);
        } else {
            appState.setPlayerInitiative(false);
        }
        stateBasedGame.enterState(Fury.BATTLE_STATE);
    }

}
