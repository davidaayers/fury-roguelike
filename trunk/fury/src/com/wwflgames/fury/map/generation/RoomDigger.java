package com.wwflgames.fury.map.generation;

import com.wwflgames.fury.map.DungeonMap;
import com.wwflgames.fury.map.TileType;
import com.wwflgames.fury.util.Log;
import com.wwflgames.fury.util.Rand;

import java.awt.*;

import static com.wwflgames.fury.map.Direction.E;
import static com.wwflgames.fury.map.Direction.N;

public class RoomDigger extends AbstractDigger {

    private int minWidth;
    private int maxWidth;
    private int minHeight;
    private int maxHeight;

    public RoomDigger(int minWidth, int minHeight, int maxWidth, int maxHeight) {
        this.minWidth = minWidth;
        this.minHeight = minHeight;
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
    }

    @Override
    public Feature dig(DungeonMap map, JoinPoint joinPoint) throws DigException {
        return doDigging(map, joinPoint);
    }

    private Feature doDigging(DungeonMap map, JoinPoint joinPoint) throws DigException {

        int startX = 0;
        int endX = 0;
        int startY = 0;
        int endY = 0;

        // choose a random width and height
        Dimension d = determineDimension();
        int width = d.width;
        int height = d.height;

        Log.debug("Trying to build a room " + width + " x " + height);

        switch (joinPoint.getDirection()) {
            case N:
            case S:
                int halfWidth = width / 2;
                startX = joinPoint.getX() - Rand.between(1, width - halfWidth + 1);
                endX = startX + width;

                if (joinPoint.getDirection() == N) {
                    startY = joinPoint.getY() - height + 1;
                    endY = joinPoint.getY() + 1;
                } else {
                    startY = joinPoint.getY();
                    endY = joinPoint.getY() + height;
                }

                break;

            case E:
            case W:
                int halfHeight = height / 2;
                startY = joinPoint.getY() - Rand.between(1, height - halfHeight + 1);
                endY = startY + height;

                if (joinPoint.getDirection() == E) {
                    startX = joinPoint.getX();
                    endX = joinPoint.getX() + width;
                } else {
                    startX = joinPoint.getX() - width + 1;
                    endX = joinPoint.getX() + 1;
                }

                break;
        }

        Feature feature = maybeDigFeature(map, startX, endX, startY, endY);

        addJoinPoints(3, feature, map);


        // finally, draw the join point
        map.getTileAt(joinPoint.getX(), joinPoint.getY()).setType(TileType.JOIN);

        // set the join point as connected
        joinPoint.setConnected(true);

        // add the join point to the list of join points for this room
        feature.addJoinPoint(joinPoint);
        return feature;
    }

    protected Dimension determineDimension() {
        return new Dimension(Rand.between(minWidth, maxWidth), Rand.between(minHeight, maxHeight));
    }

}
