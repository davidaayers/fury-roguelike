package com.wwflgames.fury.gamestate;

import com.wwflgames.fury.Fury;
import com.wwflgames.fury.battle.*;
import com.wwflgames.fury.card.Card;
import com.wwflgames.fury.card.Hand;
import com.wwflgames.fury.entity.*;
import com.wwflgames.fury.main.AppState;
import com.wwflgames.fury.map.Direction;
import com.wwflgames.fury.map.DungeonMap;
import com.wwflgames.fury.mob.Mob;
import com.wwflgames.fury.monster.Monster;
import com.wwflgames.fury.player.Player;
import com.wwflgames.fury.util.Log;
import com.wwflgames.fury.util.TextUtils;
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
import java.util.*;

import static com.wwflgames.fury.Fury.*;

public class BattleGameState extends BasicGameState {

    enum State {
        PLAYER_CHOOSE_CARD,
        PLAYER_CHOOSE_MONSTER,
        MONSTER_CHOSEN,
        BATTLE_OVER,
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
    private List<ItemLogMessage> playerEffects;
    private List<ItemLogMessage> monsterEffects;
    private List<Entity> cardsInPlay;
    private List<Entity> playerHandEntities = new ArrayList<Entity>();
    private Map<Mob, Entity> mobEntities;
    private Entity playerEntity;
    private boolean enterCalled = false;
    private Image victoryImage;
    private int expEarned;
    private Card selectedCard;

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


        refreshPlayerHandEntities();


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

        int drawX = 400 - (widthPerCard * hand.getMaxHandSize() / 2);
        int drawY = 450;
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
        if ( selectedCard.isTargetable() ) {
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

        if (currentState == State.PLAYER_CHOOSE_CARD) {
            g.setColor(Color.green);
            String text = "Battle Round " + battleSystem.getRound() + ", choose card to play";
            TextUtils.centerText(container, g, text, 416);
        }

        if (currentState == State.PLAYER_CHOOSE_MONSTER) {
            g.setColor(Color.green);
            String text = "Using " + selectedCard.getName() + ", choose Monster to attack";
            TextUtils.centerText(container, g, text, 416);
        }

        g.setColor(Color.white);

        /////////////////////////////////////////////////////////////////////
        // Not sure how to do the word stuff yet -- entities? or just draw em
        /////////////////////////////////////////////////////////////////////

        int scale = 4;
        int TILE_WIDTH = Fury.TILE_WIDTH * scale;
        int midPoint = Fury.GAME_WIDTH / 2;
        int x = midPoint - ((TILE_WIDTH * 3) / 2);

        // render the player's stuff
        drawBattleText(5, playerEffects);

        // render the monster's stuff
        drawBattleText((x + TILE_WIDTH * 3) + 5, monsterEffects);

        entityManager.render(g);

        if (currentState == State.SHOW_ITEMS_WON) {
            g.drawImage(victoryImage, midPoint - victoryImage.getWidth() / 2, 60);
            int textY = victoryImage.getHeight() + 60 + 10;
            TextUtils.centerText(container, g, "You find the following item(s):", textY);
            textY += 128 + 65;
            TextUtils.centerText(container, g, "You earned " + expEarned + " experience!", textY);
            textY += 20;
            TextUtils.centerText(container, g, "Press any key to continue", textY);
        }
    }

    private void drawBattleText(int effectX, List<ItemLogMessage> effects) {
        if (effects.isEmpty()) {
            return;
        }
        int effectY = 32;
        int max = effects.size() > 27 ? 27 : effects.size();
        for (int idx = 0; idx < max; idx++) {
            ItemLogMessage effectStr = effects.get(idx);
            font.drawString(effectX, effectY, effectStr.getString(), effectStr.getColor());
            effectY += 14;
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

                if ( !selectedCard.isTargetable() ) {
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
                greyOutItemMessages();

                Log.debug("SHOW_PLAYER_DAMAGE");
                // add the player's effects to the damage stack
                List<BattleResult> battleResults = lastBattleRound.getResultsFor(appState.getPlayer());
                for (BattleResult battleResult : battleResults) {
                    addDescToEffects(playerEffects, battleResult);
                }

                Log.debug("SHOW_MONSTER_DAMAGE");
                for (Mob enemy : battle.getEnemies()) {
                    List<BattleResult> monsterResults = lastBattleRound.getResultsFor(enemy);
                    for (BattleResult battleResult : monsterResults) {
                        addDescToEffects(monsterEffects, battleResult);
                    }
                }

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

                if (battle.allEnemiesDead()) {
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
        List<String> parts = TextUtils.maybeSplitString(string, 160,font);
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
    public void mouseMoved(int oldX, int oldY, int newX, int newY) {
        entityManager.mouseMoved(oldX, oldY, newX, newY);
    }

    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {

        // see if any entities are clickable
        boolean handled = entityManager.mouseClicked(button, x, y, clickCount);
        if (handled) {
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
