package com.wwflgames.fury.gamestate;

import com.wwflgames.fury.Fury;
import com.wwflgames.fury.battle.*;
import com.wwflgames.fury.entity.*;
import com.wwflgames.fury.main.AppState;
import com.wwflgames.fury.map.Direction;
import com.wwflgames.fury.map.DungeonMap;
import com.wwflgames.fury.mob.Mob;
import com.wwflgames.fury.monster.Monster;
import com.wwflgames.fury.player.Player;
import com.wwflgames.fury.util.Log;
import com.wwflgames.fury.util.TextUtil;
import org.newdawn.slick.Color;
import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.awt.Font;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.wwflgames.fury.Fury.*;

public class BattleGameState extends BasicGameState {
    enum State {
        PLAYER_CHOOSE_MONSTER,
        MONSTER_CHOSEN,
        ANIMATION_PLAY,
        ANIMATION_DONE,
        BATTLE_OVER,
        SHOW_ITEMS_WON
    }

    enum ReplayState {
        CREATE_PLAYER_CARD,
        SHOW_PLAYER_DAMAGE,
        CREATE_MONSTER_CARD,
        SHOW_MONSTER_DAMAGE,
        WAIT
    }

    private GameContainer container;
    private StateBasedGame game;
    private UnicodeFont font;
    private AppState appState;
    private SpriteSheetFactory spriteSheetFactory;
    private Battle battle;
    private NewBattleSystem battleSystem;
    private EntityManager entityManager;
    private int attackX;
    private int attackY;
    private State currentState;
    private ReplayState replayState;
    private BattleRound lastBattleRound;
    private StateBag stateBag;
    private List<ItemLogMessage> playerEffects;
    private List<ItemLogMessage> monsterEffects;
    private List<Entity> cardsInPlay;
    private java.util.Map<Mob, Entity> mobEntities;
    private Entity playerEntity;
    private List<Mob> monstersToShowCardsFor = new ArrayList<Mob>();
    private Mob currentMonster;
    private int monsterCardOffset;
    private boolean enterCalled = false;
    private Image victoryImage;
    private int expEarned;


    public BattleGameState(AppState appState, SpriteSheetFactory spriteSheetFactory) {
        this.appState = appState;
        this.spriteSheetFactory = spriteSheetFactory;
    }

    @Override
    public int getID() {
        return BATTLE_STATE;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {

        this.container = container;
        this.game = game;

        Font jFont = new Font("Verdana", Font.PLAIN, 12);
        font = new UnicodeFont(jFont);
        font.getEffects().add(new ColorEffect(java.awt.Color.white));
        font.addAsciiGlyphs();
        font.loadGlyphs();

        entityManager = new EntityManager(container, game);

        victoryImage = new Image("victory.png");

    }

    // called when this state is entered. Here's where we'll setup our battle 
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        Log.debug("BattleGameState-> entered.");

        // create a new state bag
        stateBag = new StateBag();

        // damage stacks
        playerEffects = new ArrayList<ItemLogMessage>();
        monsterEffects = new ArrayList<ItemLogMessage>();

        // cards in play
        cardsInPlay = new ArrayList<Entity>();

        mobEntities = new HashMap<Mob, Entity>();

        // grab the player
        Player player = appState.getPlayer();
        DungeonMap dungeonMap = appState.getMap();

        int playerMapX = player.getMapX();
        int playerMapY = player.getMapY();

        int mapOffsetX = playerMapX - 1;
        int mapOffsetY = playerMapY - 1;

        int scale = 4;
        int dungeonWidth = Fury.TILE_WIDTH * scale * 3;
        int drawX = Fury.GAME_WIDTH / 2 - dungeonWidth / 2;

        Entity mapEntity = new Entity("dungeonMap")
                .setPosition(new Vector2f(drawX, 32))
                .setScale(scale)
                .addComponent(new BattleMapRenderer("mapRender", appState.getMap(), mapOffsetX, mapOffsetY));

        entityManager.addEntity(mapEntity);

        List<Monster> monsters = findMonsters(dungeonMap, playerMapX, playerMapY);

        // finally, create entities for all of the monsters found, and the player, so
        // they can be rendered
        for (Monster monster : monsters) {
            Log.debug("monster x = " + monster.getMapX() + " , y = " + monster.getMapY());
            SpriteSheet monsterSpriteSheet = spriteSheetFactory.spriteSheetForName(monster.getSpriteSheet());
            MobRenderer sprite = new MobRenderer(monster, monsterSpriteSheet);
            sprite.useSprite(1, 2);
            Entity mobEntity = createMobEntity(mapOffsetX, mapOffsetY, monster, sprite, drawX);
            entityManager.addEntity(mobEntity);
            mobEntities.put(monster, mobEntity);
        }

        SpriteSheet heroSpriteSheet = spriteSheetFactory.spriteSheetForName(player.getProfession().getSpriteSheet());
        MobRenderer heroSprite = new MobRenderer(player, heroSpriteSheet);

        heroSprite.useSprite(1, 2);

        playerEntity = createMobEntity(mapOffsetX, mapOffsetY, player, heroSprite, drawX);
        entityManager.addEntity(playerEntity);


        // Set up the battle
        //TODO: the "true" here is player initiative, it should be set somehow. For now,
        //we'll just always give the player initiative.
        battle = new Battle(player, monsters, true);
        battleSystem = new NewBattleSystem(battle);
        battleSystem.startBattle();

        currentState = State.PLAYER_CHOOSE_MONSTER;

        enterCalled = true;

        Log.debug("BattleGameState-> complete.");
    }

    @Override
    public void leave(GameContainer container, StateBasedGame game) throws SlickException {
        Log.debug("BattleGameState-> leaving state");
        entityManager.clear();
        enterCalled = false;
    }


    private Entity createMobEntity(int mapOffsetX, int mapOffsetY, Mob mob, SpriteSheetRenderer sprite, int drawX) {
        MobLocationAction mobLocationAction = new MobLocationAction(mob.name() + "location")
                .setMapOffset(mapOffsetX, mapOffsetY)
                .setScreenOffset(drawX, 32)
                .setMob(mob);

        return new Entity(mob.name() + "entity")
                .setScale(4)
                .addComponent(sprite)
                .addComponent(mobLocationAction);
    }

    // package private for testing
    List<Monster> findMonsters(DungeonMap dungeonMap, int playerX, int playerY) {
        List<Monster> monsters = new ArrayList<Monster>();
        // look at all of the 8 squares around the player
        // and see if there are monsters there
        for (int y = -1; y < 2; y++) {
            for (int x = -1; x < 2; x++) {
                int mx = playerX + x;
                int my = playerY + y;
                if (dungeonMap.inBounds(mx, my)) {
                    Mob mob = dungeonMap.getTileAt(playerX + x, playerY + y).getMob();
                    if (mob != null && mob instanceof Monster) {
                        monsters.add((Monster) mob);
                    }
                }
            }
        }
        return monsters;
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {

        if (!enterCalled) {
            return;
        }

        g.setColor(Color.white);
        TextUtil.centerText(container, g, "Battle Screen", 0);

        if (currentState == State.PLAYER_CHOOSE_MONSTER) {
            g.setColor(Color.green);
            String text = "Battle Round " + battleSystem.getRound() + ", choose Monster to attack";
            TextUtil.centerText(container, g, text, 416);
        }

        g.setColor(Color.white);

        /////////////////////////////////////////////////////////////////////
        // Not sure how to do the word stuff yet -- entities? or just draw em
        /////////////////////////////////////////////////////////////////////

        int scale = 4;
        int TILE_WIDTH = Fury.TILE_WIDTH * scale;
        int midPoint = Fury.GAME_WIDTH / 2;
        int x = midPoint - ((TILE_WIDTH * 3) / 2);
        int y = 32;

        String player = appState.getPlayer().name();
        int width = font.getWidth(player);
        font.drawString(x / 2 - width / 2, y, player, Color.white);

        // render the player's stuff
        drawBattleText(5, playerEffects);

        // render the monster's stuff
        drawBattleText((x + TILE_WIDTH * 3) + 5, monsterEffects);

        if (currentMonster != null) {
            String monster = currentMonster.name();
            width = font.getWidth(monster);
            int mw = Fury.GAME_WIDTH - (x + TILE_WIDTH * 3);
            int mx = (x + TILE_WIDTH * 3) + mw / 2 - width / 2;
            font.drawString(mx, y, monster, Color.white);
        }

        entityManager.render(g);

        if (currentState == State.SHOW_ITEMS_WON) {
            g.drawImage(victoryImage, midPoint - victoryImage.getWidth() / 2, 60);
            int textY = victoryImage.getHeight() + 60 + 10;
            TextUtil.centerText(container, g, "You find the following item(s):", textY);
            textY += 128 + 65;
            TextUtil.centerText(container, g, "You earned " + expEarned + " experience!", textY);
            textY += 20;
            TextUtil.centerText(container, g, "Press any key to continue", textY);
        }
    }

    private void drawBattleText(int effectX, List<ItemLogMessage> effects) {
        if (effects.isEmpty()) {
            return;
        }
        int effectY = Fury.TILE_HEIGHT + Fury.TILE_HEIGHT * 4 + 42;
        int max = effects.size() > 25 ? 25 : effects.size();
        for (int idx = 0; idx < max; idx++) {
            ItemLogMessage effectStr = effects.get(idx);
            font.drawString(effectX, effectY, effectStr.getString(), effectStr.getColor());
            effectY += 14;
        }
    }

    private List<String> maybeSplitString(String effectStr, int maxWidth) {

        String[] parts = effectStr.split(" ");
        List<String> splitString = new ArrayList<String>();
        int width = 0;
        String current = "";
        for (String part : parts) {
            width += font.getWidth(part + " ");
            if (width < maxWidth) {
                current = current + part + " ";
            } else {
                splitString.add(current);
                width = 0;
                current = part + " ";
            }
        }
        splitString.add(current);

        return splitString;
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {

        if (!enterCalled) {
            return;
        }

        entityManager.update(delta);

        switch (currentState) {
            case MONSTER_CHOSEN:
                handleMonsterChosen();
                monstersToShowCardsFor.clear();
                monstersToShowCardsFor.addAll(battle.getEnemies());
                entityManager.printDebug();
                break;
            case ANIMATION_PLAY:
                handleAnimation(delta);
                break;
            case ANIMATION_DONE:
                Log.debug("ANIMATION_DONE");

                List<Mob> deadMobs = new ArrayList<Mob>();
                // remove any monsters that were killed during combat
                for (Mob monster : mobEntities.keySet()) {
                    if (monster.isDead()) {
                        deadMobs.add(monster);
                    }
                }

                for (Mob monster : deadMobs) {
                    Entity mobEntity = mobEntities.remove(monster);
                    entityManager.removeEntity(mobEntity);
                    // remove it from the dungeonMap too
                    appState.getMap().removeMob(monster);
                    // tell the monster to perform it's death activities
                    ((Monster) monster).died();
                }

                if (battle.allEnemiesDead()) {
                    currentState = State.BATTLE_OVER;
                    break;
                }

                // reset monster card offset
                monsterCardOffset = 0;


                currentState = State.PLAYER_CHOOSE_MONSTER;
                break;

            case BATTLE_OVER:

                generateItemsWon();
                computeExperienceEarned();

                currentState = State.SHOW_ITEMS_WON;

                break;

            case SHOW_ITEMS_WON:

                // do nothing - we are waiting here for the player
                // to press a key
                break;

        }
    }

    private void generateItemsWon() {
    }

    private void computeExperienceEarned() {
        expEarned = 0;
        for (Monster monster : battle.getOriginalEnemies()) {
            expEarned += monster.computeExp();
        }
        // add the exp to the player
        appState.getPlayer().addExp(expEarned);
    }


    private void handleMonsterChosen() {
        Log.debug("MONSTER_CHOSEN");

        Player player = appState.getPlayer();
        DungeonMap dungeonMap = appState.getMap();
        int monsterX = player.getMapX() + attackX;
        int monsterY = player.getMapY() + attackY;
        Monster monster = null;
        if (dungeonMap.inBounds(monsterX, monsterY)) {
            monster = (Monster) dungeonMap.getTileAt(monsterX, monsterY).getMob();
        }
        if (monster != null) {
            //TODO: NOT RIGHT, NEEDS A CARD INSTEAD OF NULL
            lastBattleRound = battleSystem.performBattleRound(null, monster);
            replayState = ReplayState.CREATE_PLAYER_CARD;
            // clear out all of the cards in play
            clearCardsInPlay();
            greyOutItemMessages();
            currentState = State.ANIMATION_PLAY;
        } else {
            Log.debug("Monster was null or dungeonMap was out of bounds, resetting state");
            currentState = State.PLAYER_CHOOSE_MONSTER;
        }
    }

    private void greyOutItemMessages() {
        for (ItemLogMessage str : playerEffects) {
            str.setColor(Color.darkGray);
        }
        for (ItemLogMessage str : monsterEffects) {
            str.setColor(Color.darkGray);
        }
    }

    private void clearCardsInPlay() {
        // remove them all from the entity manager so they stop
        // rendering
        for (Entity entity : cardsInPlay) {
            entityManager.removeEntity(entity);
        }

        cardsInPlay.clear();
    }

    private void handleAnimation(int delta) {
        Player player = appState.getPlayer();

        switch (replayState) {

            case CREATE_PLAYER_CARD:
                Log.debug("CREATE_PLAYER_CARD");
                final Entity playerCard = new Entity("playerCard");
                ActionFinishedNotifier notifier = new ActionFinishedNotifier() {
                    @Override
                    public void actionComplete() {
                        playerCard.removeComponentById("moveTo");
                        changeReplayState(ReplayState.SHOW_PLAYER_DAMAGE);
                    }
                };
                //createCard(playerCard, playerEntity, lastBattleRound.getItemUsedBy(player), 42, 64, notifier);
                entityManager.addEntity(playerCard);
                cardsInPlay.add(playerCard);
                changeReplayState(ReplayState.WAIT);
                break;

            case SHOW_PLAYER_DAMAGE:
                Log.debug("SHOW_PLAYER_DAMAGE");
                // add the player's effects to the damage stack
                //ItemUsage result = lastBattleRound.getItemUsageResultFor(appState.getPlayer());
                List<BattleResult> battleResults = lastBattleRound.getResultsFor(player);
                for (BattleResult battleResult : battleResults) {
                    addDescToEffects(playerEffects, battleResult);
                }
//                addStringToEffects(0, playerEffects,
//                        createItemUsedString(player,lastBattleRound.getItemUsedBy(player)), Color.white);

                changeReplayState(ReplayState.CREATE_MONSTER_CARD);
                break;

            case CREATE_MONSTER_CARD:
                Log.debug("SHOW_MONSTER_CARD");

                if (monstersToShowCardsFor.isEmpty()) {
                    // done showing cards
                    currentState = State.ANIMATION_DONE;
                    break;
                }

                // grab the next monster to show
                currentMonster = monstersToShowCardsFor.remove(0);
                Log.debug("Just removed " + currentMonster + " ther are " + monstersToShowCardsFor.size() + " left");
                Entity monsterEntity = mobEntities.get(currentMonster);
                final Entity monsterCard = new Entity("playerCard");
                ActionFinishedNotifier monsterNotifier = new ActionFinishedNotifier() {
                    @Override
                    public void actionComplete() {
                        monsterCard.removeComponentById("moveTo");
                        changeReplayState(ReplayState.SHOW_MONSTER_DAMAGE);
                    }
                };
//                createCard(monsterCard, monsterEntity,
//                        lastBattleRound.getItemUsedBy(currentMonster), 600 + monsterCardOffset, 64, monsterNotifier);
                entityManager.addEntity(monsterCard);
                cardsInPlay.add(monsterCard);
                changeReplayState(ReplayState.WAIT);
                monsterCardOffset += 10;
                break;

            case SHOW_MONSTER_DAMAGE:
                Log.debug("SHOW_MONSTER_DAMAGE");

                List<BattleResult> monsterResults = lastBattleRound.getResultsFor(currentMonster);
                for (BattleResult battleResult : monsterResults) {
                    addDescToEffects(monsterEffects, battleResult);
                }
//                addStringToEffects(0, monsterEffects,
//                        createItemUsedString(currentMonster,lastBattleRound.getItemUsedBy(currentMonster)), Color.white);

                changeReplayState(ReplayState.CREATE_MONSTER_CARD);
                break;

            case WAIT:
                // do nothing
                break;
        }
    }

//    private String createItemUsedString(Mob mob, Item item) {
//        return mob.name() + " uses " + item.name();
//    }


    private String createDescString(BattleResult effectResult) {
        String s0 = effectResult.getEffectedMob().possessiveName();
        String s1 = effectResult.getEffectedMob().name();
        return MessageFormat.format(effectResult.getDesc(), s0, s1);
    }


    private void addDescToEffects(List<ItemLogMessage> effects, BattleResult result) {
        String desc = createDescString(result);
        Color color = determineColor(result);
        addStringToEffects(0, effects, desc, color);
    }

    private void addStringToEffects(int pos, List<ItemLogMessage> effects, String string, Color color) {
        List<String> parts = maybeSplitString(string, 160);
        Collections.reverse(parts);
        for (String part : parts) {
            effects.add(pos, new ItemLogMessage(part, color));
        }
    }


    private Color determineColor(BattleResult effectResult) {
        ResultType effectType = effectResult.getResultType();
        if (effectType == ResultType.DAMAGE) {
            return Color.red;
        }
        if (effectType == ResultType.BUFF) {
            return Color.green;
        }
        if (effectType == ResultType.DEATH) {
            return Color.yellow;
        }
        return Color.white;
    }

    private void changeReplayState(ReplayState newState) {
        stateBag.clearAll();
        replayState = newState;
    }

//    private Entity createCard(Entity cardEntity, Entity mobEntity, Item item, float x, float y, ActionFinishedNotifier notifier) {
//        ItemRenderer card = new ItemRenderer(item, font);
//
//        MoveToAction action = new MoveToAction("moveTo", x, y, .5f, notifier);
//        Vector2f mobPos = mobEntity.getPosition();
//        cardEntity.addComponent(card)
//                .addComponent(action)
//                .setPosition(new Vector2f(mobPos.x, mobPos.y));
//        return cardEntity;
//    }

    @Override
    public void keyPressed(int key, char c) {

        if (currentState == State.SHOW_ITEMS_WON) {
            transitionToNextScreen();

        }

        if (currentState != State.PLAYER_CHOOSE_MONSTER) {
            Log.debug("Key pressed when it's not time to press keys!");
            return;
        }

        Log.debug("key = " + key);

        Direction d = Direction.forKey(key);

        if (d != null) {
            attackX = d.getDx();
            attackY = d.getDy();
            currentState = State.MONSTER_CHOSEN;
        }
    }

    private void transitionToNextScreen() {
        // if we have won the game, go to the game won screen
        // otherwise, back to the dungeon!
        if (appState.isGameOver()) {
            game.enterState(GAME_WON_STATE);
        } else {
            game.enterState(DUNGEON_GAME_STATE);
        }
    }

    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {

        if (currentState == State.SHOW_ITEMS_WON) {
            transitionToNextScreen();
        }

        if (button == Input.MOUSE_LEFT_BUTTON && clickCount == 1) {
            Log.debug("Mouse clicked at " + x + "," + y);
            Vector2f playerDrawLoc = playerEntity.getPosition();
            float drawX = playerDrawLoc.getX();
            float drawY = playerDrawLoc.getY();
            float width = TILE_WIDTH * 4;
            float height = TILE_HEIGHT * 4;
            Rectangle r = new Rectangle(drawX, drawY, width, height);
            int dx = 0;
            int dy = 0;
            if (x > r.getMaxX()) {
                dx = 1;
            }
            if (x < r.getMinX()) {
                dx = -1;
            }
            if (y > r.getMaxY()) {
                dy = 1;
            }
            if (y < r.getMinY()) {
                dy = -1;
            }

            if (dx != 0 || dy != 0) {
                attackX = dx;
                attackY = dy;
                currentState = State.MONSTER_CHOSEN;
            }

        }
    }

    private class ItemLogMessage {
        private String string;
        private Color color;

        private ItemLogMessage(String string, Color color) {
            this.string = string;
            this.color = color;
        }

        public String getString() {
            return string;
        }

        public Color getColor() {
            return color;
        }

        public void setColor(Color color) {
            this.color = color;
        }
    }

}
