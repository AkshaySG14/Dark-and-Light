package com.inoculates.dal.WorldHandlers;

import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

// Created by akshaysubramaniam on 30/6/15.
public class LightHandler {
    private GameScreen screen;
    private World world;
    private TiledMap map;
    private TiledMapTileLayer layer;
    public RayHandler rHandler;

    public LightHandler(GameScreen screen, World world, TiledMap map) {
        this.screen = screen;
        this.world = world;
        this.map = map;

        // Gets one layer of the map for layer purposes.
        layer = (TiledMapTileLayer) map.getLayers().get("Objects");
        // Creates the ray handler, which draws and updates all lights.
        rHandler = new RayHandler(world);
    }

    public void createLights() {
        // Gets every object in the spawns layer.
        for (MapObject object : map.getLayers().get("Lights").getObjects())
            // If object is rectangle.
            if (object instanceof RectangleMapObject) {
                RectangleMapObject rectObject = (RectangleMapObject) object;
                Rectangle rect = rectObject.getRectangle();
                // Gets the middle of the tile the rectangle is in.
                float x = (int) (rect.getX() / layer.getTileWidth()) * layer.getTileWidth() + layer.getTileWidth() / 2;
                float y = (int) (rect.getY() / layer.getTileHeight()) * layer.getTileHeight() + layer.getTileHeight() / 2;

                // If player spawn, creates player.
                if (object.getProperties().containsKey("pl")) {
                    Color color = getColor(object);
                    PointLight light = new PointLight(rHandler, 150, color, 100, x, y);
                    light.setSoftnessLength(100);
                }
            }
    }

    private Color getColor(MapObject object) {
        if (object.getProperties().containsKey("red"))
            return Color.RED;
        if (object.getProperties().containsKey("green"))
            return Color.GREEN;
        if (object.getProperties().containsKey("blue"))
            return Color.BLUE;
        if (object.getProperties().containsKey("yellow"))
            return Color.YELLOW;
        return null;
    }

    public void light() {
        rHandler.setCombinedMatrix(screen.camera.combined);
        rHandler.updateAndRender();
    }

    public void dispose() {
        rHandler.dispose();
    }
}
