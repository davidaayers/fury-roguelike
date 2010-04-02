package com.wwflgames.fury.guice;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;
import com.wwflgames.fury.entity.SpriteSheetProvider;
import com.wwflgames.fury.monster.MonsterFactoryImpl;
import com.wwflgames.fury.player.ProfessionFactoryImpl;


public class FuryModule implements Module {

    @Override
    public void configure(Binder binder) {
        // set up our SpriteSheetProvider multibinder
        Multibinder<SpriteSheetProvider> ssBinder = Multibinder.newSetBinder(binder, SpriteSheetProvider.class);
        ssBinder.addBinding().to(ProfessionFactoryImpl.class);
        ssBinder.addBinding().to(MonsterFactoryImpl.class);
    }
}
