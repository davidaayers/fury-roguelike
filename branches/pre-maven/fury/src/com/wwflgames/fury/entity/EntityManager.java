package com.wwflgames.fury.entity;

import com.wwflgames.fury.util.Log;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EntityManager {

    private GameContainer container;
    private StateBasedGame game;

    List<Entity> entities = new ArrayList<Entity>();
    List<Entity> entitiesToRemove = new ArrayList<Entity>();
    List<Entity> entitiesToAdd = new ArrayList<Entity>();

    private Comparator<Entity> entityComparator;

    public EntityManager(GameContainer container, StateBasedGame game) {
        this.container = container;
        this.game = game;
        entityComparator = newComparator();
    }

    public void addEntity(Entity entity) {
        entitiesToAdd.add(entity);
        // set the container and game on the entity
        entity.setContainer(container)
                .setGame(game);
    }

    public Entity removeEntity(Entity entity) {
        //entities.remove(entity);
        entitiesToRemove.add(entity);
        return entity;
    }

    public Entity removeEntityById(String id) {
        Entity e = findEntityById(id);
        if (e != null) {
            return removeEntity(e);
        }
        return null;
    }

    public void removeAllEntities() {
        entitiesToRemove.addAll(entities);
    }

    public Entity findEntityById(String id) {
        for (Entity entity : entities) {
            if (entity.getId().equals(id)) {
                return entity;
            }
        }
        return null;
    }

    public void update(int delta) {

        // see if any entities have flagged themselves for removal
        for (Entity entity : entities) {
            if (entity.shouldRemove()) {
                removeEntity(entity);
            }
        }


        // process our removed entities first
        if (!entitiesToRemove.isEmpty()) {
            for (Entity removed : entitiesToRemove) {
                entities.remove(removed);
            }
            entitiesToRemove.clear();
        }

        // add any new entries
        if (!entitiesToAdd.isEmpty()) {
            for (Entity added : entitiesToAdd) {
                entities.add(added);
            }
            entitiesToAdd.clear();
        }

        // resort the entity list by z-order, lowest to highest
        Collections.sort(entities, entityComparator);

        for (Entity entity : entities) {
            entity.update(delta);
        }
    }

    private Comparator<Entity> newComparator() {
        return new Comparator<Entity>() {
            @Override
            public int compare(Entity o1, Entity o2) {
                return o1.getZIndex().compareTo(o2.getZIndex());
            }
        };
    }

    public void render(Graphics gr) {
        for (Entity entity : entities) {
            entity.render(gr);
        }
    }

    // removes all entities
    public void clear() {
        entities.clear();
    }

    public void printDebug() {
        // print a list of all of our entities
        Log.debug("Total of " + entities.size() + " entities");
        for (Entity entity : entities) {
            Log.debug("Entity: " + entity.getId());
        }
    }

}
