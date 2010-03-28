package com.wwflgames.fury.player;

import com.google.inject.ImplementedBy;

import java.util.List;

@ImplementedBy(ProfessionFactoryImpl.class)
public interface ProfessionFactory {
    List<Profession> getAllProfessions();
}
