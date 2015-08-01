package com.inoculates.dal.WorldHandlers;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.inoculates.dal.Sprites.Player;
import com.inoculates.dal.Sprites.YellowLighter;

// Created by akshaysubramaniam on 18/7/15.

public class PhysicsContactListener implements ContactListener {

    public void beginContact(Contact contact) {
        // If there is contact between the player and a yellow lighter, if the yellow lighter is not returning, makes the
        // yellow lighter follow the player.
        if (contact.getFixtureA().getBody().getUserData() instanceof YellowLighter &&
                contact.getFixtureB().getBody().getUserData() instanceof Player) {
            YellowLighter lighter = (YellowLighter) contact.getFixtureA().getBody().getUserData();
            if (!lighter.getReturning())
                lighter.follow();
        }
        if (contact.getFixtureA().getBody().getUserData() instanceof Player &&
        contact.getFixtureB().getBody().getUserData() instanceof YellowLighter) {
            YellowLighter lighter = (YellowLighter) contact.getFixtureB().getBody().getUserData();
            if (!lighter.getReturning())
                lighter.follow();
        }
    }

    public void endContact(Contact contact) {

    }

    public void preSolve(Contact contact, Manifold fold) {

    }

    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
