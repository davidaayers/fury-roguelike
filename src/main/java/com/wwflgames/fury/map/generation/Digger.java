package com.wwflgames.fury.map.generation;

import com.wwflgames.fury.map.DungeonMap;

public interface Digger {
    Feature dig(DungeonMap map, JoinPoint point) throws DigException;
}
