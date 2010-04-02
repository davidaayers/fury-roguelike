package com.wwflgames.fury.monster;

public interface MonsterFactory {
    Monster createMonster(int points);

    Monster createBossMonster(int points);
}
