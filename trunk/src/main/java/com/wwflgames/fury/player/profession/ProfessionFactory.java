package com.wwflgames.fury.player.profession;


import com.wwflgames.fury.player.profession.Profession;

import java.util.List;

public interface ProfessionFactory {
    List<Profession> getAllProfessions();
    Profession getProfessionByName(String name);
}
