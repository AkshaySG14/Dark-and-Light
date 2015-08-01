package com.inoculates.dal.WorldHandlers;

// Created by akshaysubramaniam on 25/7/15.

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

public class BoundHandler {
    private ArrayList<Rectangle> bounds = new ArrayList<Rectangle>();
    private TiledMap map;
    private TiledMapTileLayer layer;

    public BoundHandler(TiledMap map) {
        this.map = map;
        layer = (TiledMapTileLayer) map.getLayers().get(0);
        getBounds();
    }

    // Create all bounds based on the information the tile map gives.
    private void getBounds() {
        for (MapObject object : map.getLayers().get("Bounds").getObjects())
            // If object is rectangle.
            if (object instanceof RectangleMapObject) {
                RectangleMapObject rectObject = (RectangleMapObject) object;
                Rectangle rect = rectObject.getRectangle();
                // Gets the middle of the tile the rectangle is in.
                float x = (int) (rect.getX() / layer.getTileWidth()) * layer.getTileWidth() + layer.getTileWidth() / 2;
                float y = (int) (rect.getY() / layer.getTileHeight()) * layer.getTileHeight() + layer.getTileHeight() / 2;
                Rectangle rectangle = new Rectangle(x, y, (int)
                        (rect.getWidth() / layer.getTileWidth()) * layer.getTileWidth() +
                        layer.getTileWidth() / 2, (int) (rect.getHeight() / layer.getTileHeight()) * layer.getTileHeight()
                        + layer.getTileHeight() / 2);
                bounds.add(rectangle);
            }
    }

    // Returns true if the x and y is inside a bound.
    public boolean isOutOfBounds(float x, float y) {
        for (Rectangle rect : bounds)
            if (rect.contains(x, y))
                return true;

        return false;
    }
}
