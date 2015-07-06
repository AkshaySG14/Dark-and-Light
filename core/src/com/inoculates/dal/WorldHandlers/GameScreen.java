package com.inoculates.dal.WorldHandlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.inoculates.dal.InputProcessor.PlayerInput;
import com.inoculates.dal.Sprites.Block;
import com.inoculates.dal.Sprites.Enemy;
import com.inoculates.dal.Sprites.Player;

import java.util.ArrayList;

// Created by akshaysubramaniam on 25/6/15.

public class GameScreen implements Screen {
    // The Box2d classes for the world that is responsible for the handling of the events that occur inside the game.
    // It is also needed for rendering.
    private World world;
    // Map that contains the terrain.
    private TiledMap map;
    // These are the layers of the tiledmap.
    private int[] fog = new int[] {0}, objects = new int[] {1}, foreground = new int[] {2}, background = new int[] {3};
    // Interpreter for the game, that handles movement.
    private PlayerInput input = new PlayerInput(this);
    // Renderer of the tiled map that serves as the background.
    private OrthogonalTiledMapRenderer renderer;
    // Camera that determines which part of the world the game is rendering.
    public OrthographicCamera camera;
    // Batch that draws extra materials like fonts.
    private SpriteBatch batch;
    // Handler that creates and draws all the lights.
    public LightHandler lHandler;
    // Shader for the level.
    private ShaderProgram currentMapShader;
    // Player's spherical sprite.
    public Player player;
    // List of enemies.
    public Array<Enemy> enemies = new Array<Enemy>();

    // The atlases that are used to create the texture frames.
    public ArrayList<TextureAtlas> spriteAtlases = new ArrayList<TextureAtlas>();

    public GameScreen() {
    }

    // Sets the input processor and creates all objects.
    @Override
    public void show () {
        // Input processor for the game.
        Gdx.input.setInputProcessor(input);
        // Creates the tiled map.
        map = new TmxMapLoader().load("TileMaps/Level1.tmx");
        // Creates renderer and camera.
        renderer = new OrthogonalTiledMapRenderer(map);
        camera = new OrthographicCamera();
        // Creates the the physical world, setting the gravity to zero in the process.
        world = new World(new Vector2(0, 0), true);
        // Creates the sprite renderer.
        batch = new SpriteBatch();
        // Creates the textures of the game.
        loadObjects();
        // Creates the lights in the game.
        lHandler = new LightHandler(this, world, map);
        lHandler.createLights();
        // Creates the various objects in the game.
        Creator creator = new Creator(map, this, world);
        creator.createInstances();
    }

    // Renders all objects, and updates them.
    @Override
    public void render (float delta) {
        // Updates the PHYSICAL aspects of the game at 60 times per second.
        world.step(1f/60f, 6, 2);

        // Updates the VISUAL aspects of the game.
        // Clears canvas and then renders the art over it.
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        // Sets the shader of the batch.
        batch.setShader(currentMapShader);



        // Renders the various layers of the tiled map.
        renderer.setView(camera);
        renderer.render(fog);
        renderer.render(objects);
        renderer.render(foreground);
        renderer.render(background);

        // Renders the various lights.
        lHandler.light();

        // Renders all objects and then updates the camera.
        batch.begin();
        drawSprites();
        updateCamera();
        batch.end();
    }

    // Draws all sprites in the game, some by iterating through loops.
    private void drawSprites() {
        player.draw(batch);
        for (Enemy enemy : enemies)
            enemy.draw(batch);
    }

    private void updateCamera() {
        // Sets the camera to the position of the player, and updates it.
        camera.position.set(player.getX(), player.getY(), 0);
        camera.update();
    }

    @Override
    public void hide () {
    }

    // This resizes the camera and window of the game.
    @Override
    public void resize (int width, int height) {
        camera.viewportWidth = 200;
        camera.viewportHeight = 150;
    }

    @Override
    public void pause () {
    }

    @Override
    public void resume () {
    }

    @Override
    public void dispose () {
        player.getTexture().dispose();
        lHandler.dispose();
    }

    // Loads all the textures the game requires.
    private void loadObjects() {
        AssetManager manager = new AssetManager();
        manager.load("Sprites/Sprites.pack", TextureAtlas.class);
        manager.finishLoading();

        spriteAtlases.add((TextureAtlas) manager.get("Sprites/Sprites.pack"));
    }

    // Gets the normal coordinates of the projected value. This is useful for obtaining the position of the mouse on
    // the screen.
    public Vector2 unproject(Vector2 value) {
        // Gets the unprojection from the camera.
        Vector3 v = camera.unproject(new Vector3(value, 0));
        // Returns the x and y of the unprojection.
        return new Vector2(v.x, v.y);
    }
}
