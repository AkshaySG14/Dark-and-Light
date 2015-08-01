package com.inoculates.dal.WorldHandlers;

// Created by akshaysubramaniam on 27/6/15.

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.inoculates.dal.Sprites.*;

// This is the class that creates the various objects in the game.
public class Creator {
    private TiledMap map;
    private TiledMapTileLayer layer;
    private GameScreen screen;
    private World world;
    private Array<MapObject> linkers = new Array<MapObject>();

    public Creator(TiledMap map, GameScreen screen, World world) {
        this.map = map;
        this.screen = screen;
        this.world = world;
        layer = (TiledMapTileLayer) map.getLayers().get(0);
    }

    // Creates all objects in the game.
    public void createInstances() {
        // Creates all blocks.
        createFromTiles();
        // Creates all terrain objects.
        createTerrainObjects();
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
                    screen.player = new Player(screen, world, screen.spriteAtlases.get(0), x, y);
                // If enemy spawn, creates corresponding enemy. Uses the string values in the spawn rectangle to
                // get the type of Lighter and the angle.
                if (object.getProperties().containsKey("rlstatic"))
                    screen.enemies.add(new RedLighterStatic(screen, world, x, y, screen.spriteAtlases.get(0),
                            Integer.parseInt(rectObject.getProperties().get("rlstatic").toString()),
                            Float.parseFloat(rectObject.getProperties().get("angle").toString())));
                if (object.getProperties().containsKey("rldynamic"))
                    screen.enemies.add(new RedLighterDynamic(screen, world, x, y, screen.spriteAtlases.get(0),
                            Float.parseFloat(rectObject.getProperties().get("angle").toString())));
                if (object.getProperties().containsKey("yl"))
                    screen.enemies.add(new YellowLighter(screen, world, screen.spriteAtlases.get(0), x, y));
                if (object.getProperties().containsKey("ol"))
                    screen.enemies.add(new OrangeLighter(screen, world, screen.spriteAtlases.get(0), x, y,
                            Float.parseFloat(rectObject.getProperties().get("angle").toString())));
            }
    }

    // Creates blocks and lights from the tiles themselves.
    private void createFromTiles() {
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
                if (cell != null && cell.getTile().getProperties().containsKey("block"))
                    // Creates a physical block at that point. Note the lack of a sprite.
                    block = new Block(world, x * objectLayer.getTileWidth() + objectLayer.getTileWidth() / 2,
                            y * objectLayer.getTileHeight() + objectLayer.getTileHeight() / 2);
            }
    }

    private void createTerrainObjects() {
        // Gets all linkers before beginning anything else.
        getLinkers();
        // Gets every object in the spawns layer.
        for (MapObject object : map.getLayers().get("Spawns").getObjects())
            // If object is rectangle.
            if (object instanceof RectangleMapObject) {
                RectangleMapObject rectObject = (RectangleMapObject) object;
                Rectangle rect = rectObject.getRectangle();
                // Gets the middle of the tile the rectangle is in.
                float x = (int) (rect.getX() / layer.getTileWidth()) * layer.getTileWidth() + layer.getTileWidth() / 2;
                float y = (int) (rect.getY() / layer.getTileHeight()) * layer.getTileHeight() + layer.getTileHeight() / 2;

                // If terrain object, creates corresponding terrain object. Uses the string values in the spawn
                // rectangle to get the linked terrain object.
                if (object.getProperties().containsKey("lbox")) {
                    int value = Integer.parseInt(rectObject.getProperties().get("gate").toString());
                    screen.tObjects.add(new LightBox(screen, world, screen.spriteAtlases.get(0),
                            getLinkerObject("gate", value), x, y));
                }
            }
    }

    // Puts all the linker objects into one array to allow for easy access.
    private void getLinkers() {
        for (MapObject object : map.getLayers().get("Spawns").getObjects())
            if (object.getProperties().containsKey("linker"))
                linkers.add(object);
    }

    // This method returns the linked terrain object based on the name and the value given.
    private TerrainObject getLinkerObject(String name, int value) {
        // Gets every object in the spawns layer.
        for (MapObject object : linkers)
            // If object is rectangle.
            if (object instanceof RectangleMapObject) {
                RectangleMapObject rectObject = (RectangleMapObject) object;
                Rectangle rect = rectObject.getRectangle();
                // Gets the middle of the tile the rectangle is in.
                float x = (int) (rect.getX() / layer.getTileWidth()) * layer.getTileWidth() + layer.getTileWidth() / 2;
                float y = (int) (rect.getY() / layer.getTileHeight()) * layer.getTileHeight() + layer.getTileHeight() / 2;

                // If the object has the same property as the name given and it contains the same value, creates the
                // new object and returns it simutaneously.
                if (object.getProperties().containsKey(name) &&
                        Integer.parseInt(rectObject.getProperties().get(name).toString()) == value)
                    return createObject(name, x, y, Float.parseFloat(rectObject.getProperties().get("angle").toString()));
            }
        // For compiling purposes. This method should never return null.
        return null;
    }

    // This method is responsible for the actual creation of the linker object.
    private TerrainObject createObject(String name, float x, float y, float angle) {
        TerrainObject tObject = null;
        // If it is the gate object, creates and returns a gate.
        if (name.equals("gate"))
            tObject = new Gate(screen, world, screen.spriteAtlases.get(0), x, y, angle);
        // Adds the object to the rendering list.
        screen.tObjects.add(tObject);
        return tObject;
    }
}
