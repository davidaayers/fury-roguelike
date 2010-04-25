package com.wwflgames.fury.player;


import java.util.List;

public interface ProfessionFactory {
    List<Profession> getAllProfessions();
    Profession getProfessionByName(String name);
}
