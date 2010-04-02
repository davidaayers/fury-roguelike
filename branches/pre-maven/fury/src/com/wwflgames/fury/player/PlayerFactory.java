package com.wwflgames.fury.player;

import com.google.inject.ImplementedBy;

@ImplementedBy(PlayerFactoryImpl.class)
public interface PlayerFactory {
    Player createForProfession(Profession profession);
}
