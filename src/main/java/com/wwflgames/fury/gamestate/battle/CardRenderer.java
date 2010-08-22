package com.wwflgames.fury.gamestate.battle;

import com.wwflgames.fury.card.Card;
import com.wwflgames.fury.card.applier.Applier;
import com.wwflgames.fury.util.Log;
import com.wwflgames.fury.util.TextUtils;
import com.wwflgames.slick.entity.ClickNotifier;
import com.wwflgames.slick.entity.EntityMouseHandler;
import com.wwflgames.slick.entity.Renderer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Vector2f;

import java.util.List;

public class CardRenderer extends Renderer implements EntityMouseHandler {
    private Color cardBgColor;
    private Card card;
    private UnicodeFont font;
    private boolean mouseOver;
    private ClickNotifier<Card> cardClickNotifier;

    public CardRenderer(Card card, UnicodeFont font, ClickNotifier<Card> cardClickNotifier) {
        super(card.getName());
        this.cardClickNotifier = cardClickNotifier;
        this.cardBgColor = Color.gray;
        this.card = card;
        this.font = font;
    }

    @Override
    public void render(Graphics g) {
        if (!owner.isVisible()) {
            return;
        }

        Vector2f pos = owner.getPosition();

        int width = 32 * 4;
        int height = 32 * 4;

        g.setColor(cardBgColor);
        g.fillRoundRect(pos.x, pos.y, width, height, 15);
        if ( mouseOver ) {
            g.setColor(Color.red);
        } else {
            g.setColor(Color.white);
        }
        g.drawRoundRect(pos.x, pos.y, width, height, 15);

        renderCardText();
    }

    protected void renderCardText() {

        // draw card name;
        int y = 4;
        List<String> cardName = TextUtils.maybeSplitString(card.getName(),32*4,font);
        for ( String str: cardName ) {
            drawString(str, y, Color.white);
            y+=14;
        }

        // draw effects against
        if ( card.getUsedAgainstAppliers() != null ) {
            for ( Applier applier : card.getUsedAgainstAppliers() ) {
                for ( String str : TextUtils.maybeSplitString(applier.description(),32*4,font)) {
                    drawString(str, y, Color.red);
                    y += 14;
                }
            }

        }

        if ( card.getUsedByAppliers() != null ) {
            for ( Applier applier : card.getUsedByAppliers() ) {
                for ( String str : TextUtils.maybeSplitString(applier.description(),32*4,font)) {
                    drawString(str, y, Color.green);
                    y += 14;
                }
            }
        }
    }

    private void drawString(String text, int y, Color fontColor) {
        Vector2f pos = owner.getPosition();
        int width = 32 * 4;
        int strWidth = font.getWidth(text);
        font.drawString(pos.x + (width / 2) - (strWidth / 2), pos.y + y, text, fontColor);

    }

    @Override
    public void update(int delta) {

    }

    @Override
    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
        // see if mouse is over us
        mouseOver = isPositionOverEntity(newx,newy);

    }

    @Override
    public boolean mouseClicked(int button, int x, int y, int clickCount) {
        if ( isPositionOverEntity(x, y) ) {
            Log.debug("Mouse click on " + owner.getId());
            cardClickNotifier.clickHappened(card);
            return true;
        }
        return false;
    }

    private boolean isPositionOverEntity(int x, int y) {
        float ownerX = owner.getPosition().getX();
        float ownerY = owner.getPosition().getY();
        if ( x >= ownerX && x <= ownerX + 32*4 &&
                y >= ownerY && y <= ownerY + 32*4 ) {
            return true;
        }
        return false;
    }
}
