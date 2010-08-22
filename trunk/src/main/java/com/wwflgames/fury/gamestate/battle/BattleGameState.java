package com.wwflgames.fury.gamestate.battle;

import com.google.common.collect.Lists;
import com.wwflgames.fury.Fury;
import com.wwflgames.fury.battle.Battle;
import com.wwflgames.fury.battle.BattleRound;
import com.wwflgames.fury.battle.BattleSystem;
import com.wwflgames.fury.card.Card;
import com.wwflgames.fury.card.Hand;
import com.wwflgames.fury.entity.SpriteSheetFactory;
import com.wwflgames.fury.gamestate.common.SpriteSheetRenderer;
import com.wwflgames.fury.main.AppState;
import com.wwflgames.fury.map.Direction;
import com.wwflgames.fury.map.DungeonMap;
import com.wwflgames.fury.mob.Mob;
import com.wwflgames.fury.mob.Stat;
import com.wwflgames.fury.monster.Monster;
import com.wwflgames.fury.player.Player;
import com.wwflgames.fury.util.Log;
import com.wwflgames.fury.util.TextUtils;
import com.wwflgames.slick.entity.*;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.wwflgames.fury.Fury.*;

public class BattleGameState extends BasicGameState {

    enum State {
        PLAYER_CHOOSE_CARD,
        PLAYER_CHOOSE_MONSTER,
        MONSTER_CHOSEN,
        BATTLE_OVER,
        PLAYER_LOST,
        SHOW_ITEMS_WON
    }

    private StateBasedGame game;
    private UnicodeFont font;
    private AppState appState;
    private SpriteSheetFactory spriteSheetFactory;
    private Battle battle;
    private BattleSystem battleSystem;
    private EntityManager entityManager;
    private int attackX;
    private int attackY;
    private State currentState;
    private BattleRound lastBattleRound;
    private List<Entity> cardsInPlay;
    private List<Entity> playerHandEntities = new ArrayList<Entity>();
    private Map<Mob, Entity> mobEntities;
    private Entity playerEntity;
    private boolean enterCalled = false;
    private Image victoryImage;
    private int expEarned;
    private Card selectedCard;
    private BattleTextRenderer playerBattleTextRenderer;
    private BattleTextRenderer monsterBattleTextRenderer;
    private UseItemPopup useItemPopup;

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


        refreshPlayerHandEntities();

        useItemPopup = new UseItemPopup("useItemPopup");
        Entity popupEntity = new KeyHandlingEntity("useItemEntity", useItemPopup)
                .setZIndex(10)
                .addComponent(useItemPopup);
        entityManager.addEntity(popupEntity);


        playerBattleTextRenderer = new BattleTextRenderer("player-battle-text", font);
        Entity playerBattleTextEntity = new Entity("player-battle-text-entity")
                .setPosition(new Vector2f(5, 32))
                .addComponent(playerBattleTextRenderer);
        entityManager.addEntity(playerBattleTextEntity);
        monsterBattleTextRenderer = new BattleTextRenderer("monster-battle-text", font);
        int x = (Fury.GAME_WIDTH / 2) + (dungeonWidth / 2) + 5;
        Entity monsterBattleTextEntity = new Entity("monster-battle-text-entity")
                .setPosition(new Vector2f(x, 32))
                .addComponent(monsterBattleTextRenderer);
        entityManager.addEntity(monsterBattleTextEntity);

        // Set up the battle
        //TODO: the "true" here is player initiative, it should be set somehow. For now,
        //we'll just always give the player initiative.
        battle = new Battle(player, monsters, true);
        battleSystem = new BattleSystem(battle);
        battleSystem.startBattle();

        currentState = State.PLAYER_CHOOSE_CARD;

        enterCalled = true;
    }

    private void refreshPlayerHandEntities() {
        // first, remove any entities that may already be on the manager
        for (Entity entity : playerHandEntities) {
            entityManager.removeEntity(entity);
        }

        playerHandEntities.clear();

        Hand hand = appState.getPlayer().getHand();
        int widthPerCard = 135;

        int drawX = (Fury.GAME_WIDTH / 2) - (widthPerCard * hand.getMaxHandSize() / 2);
        int drawY = 465;
        for (Card card : hand.getHand()) {
            CardRenderer renderer = new CardRenderer(card, font, new ClickNotifier<Card>() {
                @Override
                public void clickHappened(Card data) {
                    cardSelected(data);
                }
            });
            ClickableEntity cardEntity = new ClickableEntity("cardEntity" + card.getName(), renderer);
            cardEntity.setPosition(new Vector2f(drawX, drawY));
            cardEntity.addComponent(renderer);
            entityManager.addEntity(cardEntity);
            playerHandEntities.add(cardEntity);
            drawX += widthPerCard;
        }

    }

    private void cardSelected(Card data) {

        // should only be called if the player is selecting a card
        selectedCard = data;

        // remove the player's hand from the entity manager so more clicks
        // can't happen
        for (Entity entity : playerHandEntities) {
            entityManager.removeEntity(entity);
        }

        playerHandEntities.clear();

        // if the selected card is targetable, allow the player to choose, otherwise
        // don't
        if (selectedCard.isTargetable()) {
            currentState = State.PLAYER_CHOOSE_MONSTER;
        } else {
            currentState = State.MONSTER_CHOSEN;
        }
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
        TextUtils.centerText(container, g, "Battle Screen", 0);

        int y = 425;

        if (currentState == State.PLAYER_CHOOSE_CARD || currentState == State.PLAYER_CHOOSE_MONSTER) {
            String text = "'U'se item - 'F'lee combat";
            g.setColor(Color.cyan);
            TextUtils.centerText(container, g, text, y);
        }

        y += 20;

        if (currentState == State.PLAYER_CHOOSE_CARD) {
            g.setColor(Color.green);
            String text = "Battle Round " + battleSystem.getRound() + ", choose card to play";
            TextUtils.centerText(container, g, text, y);
        }

        if (currentState == State.PLAYER_CHOOSE_MONSTER) {
            g.setColor(Color.green);
            String text = "Using " + selectedCard.getName() + ", choose Monster to attack";
            TextUtils.centerText(container, g, text, y);
        }

        g.setColor(Color.white);

        /////////////////////////////////////////////////////////////////////
        // Not sure how to do the word stuff yet -- entities? or just draw em
        /////////////////////////////////////////////////////////////////////
        int midPoint = Fury.GAME_WIDTH / 2;

        entityManager.render(g);

        if (currentState == State.SHOW_ITEMS_WON) {
            g.drawImage(victoryImage, midPoint - victoryImage.getWidth() / 2, 60);
            int textY = victoryImage.getHeight() + 60 + 10;
            TextUtils.centerText(container, g, "You find the following item(s):", textY);
            textY += 128 + 65;
            TextUtils.centerText(container, g, "You earned " + expEarned + " experience!", textY);
            textY += 20;
            TextUtils.centerText(container, g, "Press any key to continue", textY);
        } else if (currentState == State.PLAYER_LOST) {
            int textY = 120;
            TextUtils.centerText(container, g, "You died!", textY);
            textY += 20;
            TextUtils.centerText(container, g, "Press any key to continue", textY);
        }
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {

        if (!enterCalled) {
            return;
        }

        entityManager.update(delta);

        switch (currentState) {

            case MONSTER_CHOSEN:
                Log.debug("MONSTER_CHOSEN");

                Player player = appState.getPlayer();
                DungeonMap dungeonMap = appState.getMap();

                if (!selectedCard.isTargetable()) {
                    lastBattleRound = battleSystem.performBattleRound(selectedCard);
                } else {
                    int monsterX = player.getMapX() + attackX;
                    int monsterY = player.getMapY() + attackY;
                    Monster monster1 = null;
                    if (dungeonMap.inBounds(monsterX, monsterY)) {
                        monster1 = (Monster) dungeonMap.getTileAt(monsterX, monsterY).getMob();
                    }
                    lastBattleRound = battleSystem.performBattleRound(selectedCard, monster1);
                }

                // clear out all of the cards in play
                clearCardsInPlay();

                Log.debug("SHOW_PLAYER_DAMAGE");
                // add the player's effects to the damage stack
                playerBattleTextRenderer.addBattleRoundResults(lastBattleRound, newArrayList((Mob) player));
                monsterBattleTextRenderer.addBattleRoundResults(lastBattleRound, Lists.<Mob>newArrayList(battle.getEnemies()));


                List<Mob> deadMobs = new ArrayList<Mob>();
                // remove any monsters that were killed during combat
                for (Mob monster : mobEntities.keySet()) {
                    if (monster.isDead()) {
                        deadMobs.add(monster);
                    }
                }

                Log.debug("dead mobs is " + deadMobs);

                for (Mob monster : deadMobs) {
                    Entity mobEntity = mobEntities.remove(monster);
                    entityManager.removeEntity(mobEntity);
                    // remove it from the dungeonMap too
                    appState.getMap().removeMob(monster);
                    // tell the monster to perform it's death activities
                    ((Monster) monster).died();
                }

                Integer health = player.getStatValue(Stat.HEALTH);
                Log.debug("Health is " + health);
                if (health <= 0) {
                    appState.setGameOver(true);
                    appState.setPlayerWon(false);
                    currentState = State.PLAYER_LOST;
                    break;
                } else if (battle.allEnemiesDead()) {
                    currentState = State.BATTLE_OVER;
                    break;
                }

                refreshPlayerHandEntities();
                currentState = State.PLAYER_CHOOSE_CARD;

                break;

            case BATTLE_OVER:

                generateItemsWon();
                computeExperienceEarned();

                currentState = State.SHOW_ITEMS_WON;

                break;

            case PLAYER_LOST:
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

    private void clearCardsInPlay() {
        // remove them all from the entity manager so they stop
        // rendering
        for (Entity entity : cardsInPlay) {
            entityManager.removeEntity(entity);
        }

        cardsInPlay.clear();
    }

    @Override
    public void keyPressed(int key, char c) {

        if (entityManager.keyPressed(key, c)) {
            return;
        }

        if (currentState == State.SHOW_ITEMS_WON || currentState == State.PLAYER_LOST) {
            transitionToNextScreen();

        }

        // look for "Flee" and "Use"
        if (currentState == State.PLAYER_CHOOSE_CARD || currentState == State.PLAYER_CHOOSE_MONSTER) {
            if (key == Input.KEY_F) {
                fleeCombat();
                return;
            }
            if (key == Input.KEY_U) {
                Log.debug("use pressed");
                useItem();
                return;
            }
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

    private void useItem() {
        //TODO: Need a way to show results for item usage that dont involve
        // create fake rounds
        BattleRound round = new BattleRound(-1);
        useItemPopup.showForPlayer(appState.getPlayer(), round);
    }

    private void fleeCombat() {
        for (Mob mob : battle.getAllBattleParticipants()) {
            mob.removeAllStatusEffects();
        }
        game.enterState(DUNGEON_GAME_STATE);
    }

    private void transitionToNextScreen() {
        // if we have won the game, go to the game won screen
        // otherwise, back to the dungeon!
        if (appState.isGameOver()) {
            game.enterState(GAME_OVER_STATE);
        } else {
            game.enterState(DUNGEON_GAME_STATE);
        }
    }

    @Override
    public void mouseMoved(int oldX, int oldY, int newX, int newY) {
        entityManager.mouseMoved(oldX, oldY, newX, newY);
    }

    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {

        // see if any entities are clickable
        if (entityManager.mouseClicked(button, x, y, clickCount)) {
            return;
        }

        if (currentState == State.PLAYER_CHOOSE_CARD) {
            // don't do anything else
            return;
        }

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
}
