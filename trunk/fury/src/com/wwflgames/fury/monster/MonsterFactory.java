package com.wwflgames.fury.monster;

import com.google.inject.ImplementedBy;

@ImplementedBy(MonsterFactoryImpl.class)
public interface MonsterFactory {
    Monster createMonster(int points);
}
