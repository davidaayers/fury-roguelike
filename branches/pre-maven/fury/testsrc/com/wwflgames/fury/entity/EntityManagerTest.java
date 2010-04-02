package com.wwflgames.fury.entity;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.*;

public class EntityManagerTest {
    private EntityManager entityManager;

    @Before
    public void setUp() throws Exception {
        entityManager = new EntityManager(null,null);
    }

    @Test
    public void testAddEntityAddsToAddedListAndThenToEntityListWhenUpdateCalled() throws Exception {
        Entity fake = new Entity("foo");
        entityManager.addEntity(fake);
        assertEquals(1,entityManager.entitiesToAdd.size());
        entityManager.update(1);
        assertTrue(entityManager.entitiesToAdd.isEmpty());
        assertEquals(1,entityManager.entities.size());
    }

    @Test
    public void testRemoveEntityAddsToRemoveListAndThenRemovesWhenUpdateCalled() throws Exception {
        Entity fake = new Entity("foo");
        entityManager.addEntity(fake);
        assertEquals(1,entityManager.entitiesToAdd.size());
        entityManager.update(1);
        assertTrue(entityManager.entitiesToAdd.isEmpty());
        assertEquals(1,entityManager.entities.size());
        entityManager.removeEntity(fake);
        assertEquals(1,entityManager.entitiesToRemove.size());
        entityManager.update(1);
        assertTrue(entityManager.entities.isEmpty());
    }

    @Test
    public void testRemoveEntityByIdBehavesTheSameAsRemoveEntity() throws Exception {
        Entity fake = new Entity("foo");
        entityManager.addEntity(fake);
        assertEquals(1,entityManager.entitiesToAdd.size());
        entityManager.update(1);
        assertTrue(entityManager.entitiesToAdd.isEmpty());
        assertEquals(1,entityManager.entities.size());
        entityManager.removeEntityById(fake.getId());
        assertEquals(1,entityManager.entitiesToRemove.size());
        entityManager.update(1);
        assertTrue(entityManager.entities.isEmpty());
    }

    @Test
    public void testUpdateSortsTwoEntitiesByZOrder() throws Exception {
        Entity fake1 = new Entity("foo1").setZIndex(5);
        Entity fake2 = new Entity("foo2").setZIndex(1);
        entityManager.addEntity(fake1);
        entityManager.addEntity(fake2);
        entityManager.update(1);

        assertEquals(fake2,entityManager.entities.get(0));
        assertEquals(fake1,entityManager.entities.get(1));
    }

}
