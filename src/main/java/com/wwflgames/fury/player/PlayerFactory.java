package com.wwflgames.fury.player;

import com.wwflgames.fury.player.profession.Profession;

public interface PlayerFactory {
    Player createForProfession(Profession profession);
}
