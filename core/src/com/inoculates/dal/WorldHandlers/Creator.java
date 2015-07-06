package com.inoculates.dal.WorldHandlers;

// Created by akshaysubramaniam on 27/6/15.

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.inoculates.dal.Sprites.Block;
import com.inoculates.dal.Sprites.Player;
import com.inoculates.dal.Sprites.RedLighterStatic;

import java.util.Iterator;

// This is the class that creates the various objects in the game.
public class Creator {
    private TiledMap map;
    private TiledMapTileLayer layer;
    private GameScreen screen;
    private World world;

    public Creator(TiledMap map, GameScreen screen, World world) {
        this.map = map;
        this.screen = screen;
        this.world = world;
        layer = (TiledMapTileLayer) map.getLayers().get(0);
    }

    // Creates all objects in the game.
    public void createInstances() {
        // Creates all blocks.
        createBlocks();
        // Base sprite that can be transformed into any object needed.
        Sprite sprite;
        // Gets every object in the spawns layer.
        for (MapObject object : map.getLayers().get("Spawns").getObjects())
            // If object is rectangle.
            if (object instanceof RectangleMapObject) {
                RectangleMapObject rectObject = (RectangleMapObject) object;
                Rectangle rect = rectObject.getRectangle();
                // Gets the middle of the tile the rectangle is in.
                float x = (int) (rect.getX() / layer.getTileWidth()) * layer.getTileWidth() + layer.getTileWidth() / 2;
                float y = (int) (rect.getY() / layer.getTileHeight()) * layer.getTileHeight() + layer.getTileHeight() / 2;

                // If player spawn, creates player.
                if (object.getProperties().containsKey("playerspawn"))
                    screen.player = new Player(screen, world, x, y, screen.spriteAtlases.get(0));
                // If enemy spawn, creates corresponding enemy. Uses the string values in the spawn rectangle to
                // get the type of RedLigther and the angle.
                if (object.getProperties().containsKey("rlstatic"))
                    screen.enemies.add(new RedLighterStatic(screen, world, x, y, screen.spriteAtlases.get(0),
                            Integer.parseInt(rectObject.getProperties().get("rlstatic").toString()),
                            Float.parseFloat(rectObject.getProperties().get("angle").toString())));
            }
    }

    private void createBlocks() {
        // Physical block body that needs to be created.
        Block block;
        // Gets the object layer.
        TiledMapTileLayer objectLayer = (TiledMapTileLayer) map.getLayers().get("Objects");
        // Iterates through every cell in the map.
        for (int x = 0; x < objectLayer.getWidth(); x ++)
            for (int y = 0; y < objectLayer.getHeight(); y ++) {
                // Gets the cell at this particular position.
                TiledMapTileLayer.Cell cell = objectLayer.getCell(x, y);
                // Sees if the tile of the cell is equal to the block tile.
                if (cell != null && cell.getTile().getProperties().containsKey("block")) {
                    // Creates a physical block at that point. Note the lack of a sprite.
                    block = new Block(world, x * objectLayer.getTileWidth() + objectLayer.getTileWidth() / 2,
                            y * objectLayer.getTileHeight() + objectLayer.getTileHeight() / 2);
                }
            }
    }
}
