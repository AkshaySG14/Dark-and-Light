package com.inoculates.dal.Sprites;

// Created by akshaysubramaniam on 13/7/15.

import box2dLight.ConeLight;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;
import com.inoculates.dal.WorldHandlers.GameScreen;

public class Character extends Sprite {
    protected GameScreen screen;
    protected World world;
    protected Head head;
    protected ConeLight fLight;
    protected TiledMapTileLayer layer;

    protected boolean lighting = false, lit = false;

    public Character(GameScreen screen, World world) {
        this.screen = screen;
        this.world = world;
        layer = (TiledMapTileLayer) screen.getMap().getLayers().get("Objects");
    }

    // Switches on/off the light and the color of the head correspondingly.
    public void switchLight() {
        // Resets the position of the flashlight so it draws at the correct position immediately.
        fLight.setDirection(head.getRotation() + 90);
        fLight.setPosition(head.getX() + head.getWidth() / 2, head.getY() + head.getHeight() / 2);

        // Switches on/off the flashlight.
        lighting = !lighting;
        // Changes the color of the head.
        head.changeColor(lighting);
        // Sets the light to active, so that it is rendered.
        fLight.setActive(lighting);
    }

    // Checks to see whether the target is in the cone of light.
    protected boolean checkLightCollision(float tX, float tY) {
        // Distance of the light.
        float lightDistance = fLight.getDistance() / 2.5f;
        // Returns if no flashlight was lit.
        if (!lighting)
            return false;

        // Creates a line based on the angle the character is facing. If the target is too close to this line, which
        // is determined by how far along the line the program has iterated to, the method will return true.
        for (float i = 0; i < lightDistance; i++) {
            // Gets the position of the line in the current iteration.
            float fX = (float) (fLight.getX() + Math.cos((head.getRotation() + 90) * MathUtils.degreesToRadians) * i);
            float fY = (float) (fLight.getY() + Math.sin((head.getRotation() + 90) * MathUtils.degreesToRadians) * i);
            // Gets distance.
            float distance = (float) (Math.sqrt(Math.pow((tX - fX), 2) + Math.pow((tY - fY), 2)));
            // Minimum distance determined by i, or how far it has iterated. Farther out means more leniency with
            // distance. This roughly emulates the cone flashlight model. Also checks to see if anything is blocking
            // the light.
            if (distance < 25 / lightDistance * i && pathingClear(fX, fY))
                return true;
        }
        return false;
    }

    // This is the pathing method that checks whether the target is beyond a light-blocking threshold, like a box.
    // sX and sY are the light points where the character's light has overlapped with the target.
    protected boolean pathingClear(float sX, float sY) {
        // Angle between the light point and the flash light point.
        double angle = Math.atan2(fLight.getY() - sY, fLight.getX() - sX);
        // Distance between the light point and the flash light point.
        float distance = (float) (Math.sqrt(Math.pow((fLight.getX() - sX), 2) + Math.pow((fLight.getY() - sY), 2)));
        // Creates a line between the two points and iterates through it.
        for (float i = 0; i <= distance; i++) {
            float fX = (float) (sX + Math.cos(angle) * i);
            float fY = (float) (sY + Math.sin(angle) * i);
            // If area is blocked, that means the light does not pierce through the threshold, therefore return false.
            if (isCellBlocked(fX, fY))
                return false;
        }

        return true;
    }

    // Checks whether the cell at the x and y point is blocked or not.
    protected boolean isCellBlocked(float x, float y) {
        // Gets the cell by dividing the x position of the character by the tile width (16) and casting to an int. The
        // same is done for the y position. This will essentially acquire the cell position, which will then be checked
        // for its properties.
        TiledMapTileLayer.Cell cell = layer.getCell((int) (x / layer.getTileWidth()), (int) (y / layer.getTileHeight()));
        // Checks if one of the cell properties is blocked.
        return cell != null && cell.getTile() != null && (cell.getTile().getProperties().containsKey("block") ||
                cell.getTile().getProperties().containsKey("lbox"));
    }

    // Checks whether the cell at the x and y point is the end tile.
    protected boolean isCellEnd(float x, float y) {
        // Gets the cell by dividing the x position of the character by the tile width (16) and casting to an int. The
        // same is done for the y position. This will essentially acquire the cell position, which will then be checked
        // for its properties.
        TiledMapTileLayer.Cell cell = layer.getCell((int) (x / layer.getTileWidth()), (int) (y / layer.getTileHeight()));
        // Checks if one of the cell properties is blocked.
        return cell != null && cell.getTile() != null && (cell.getTile().getProperties().containsKey("end"));
    }

    // Gets the distance using the basic formula.
    protected float getDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
}
