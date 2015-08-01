package com.inoculates.dal.WorldHandlers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.inoculates.dal.InputProcessor.PlayerInput;
import com.inoculates.dal.Sprites.BlackScreen;
import com.inoculates.dal.Sprites.Enemy;
import com.inoculates.dal.Sprites.Player;
import com.inoculates.dal.Sprites.TerrainObject;
import com.inoculates.dal.UI.UI;

import java.util.ArrayList;

// Created by akshaysubramaniam on 25/6/15.

public class GameScreen implements Screen {
    // The game itself.
    private Game game;
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
    // Handler that stores the location of all bounds.
    public BoundHandler bHandler;
    // Shader for the level.
    private ShaderProgram currentMapShader;
    // Screen for losing and winning.
    private BlackScreen screen;
    // Player's spherical sprite.
    public Player player;
    // List of UI.
    public ArrayList<UI> UIs = new ArrayList<UI>();
    // List of enemies.
    public ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    // List of terrain objects.
    public ArrayList<TerrainObject> tObjects = new ArrayList<TerrainObject>();

    // The atlases that are used to create the texture frames.
    public ArrayList<TextureAtlas> spriteAtlases = new ArrayList<TextureAtlas>();

    public GameScreen(Game game) {
        this.game = game;
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
        world.setContactListener(new PhysicsContactListener());
        // Creates the sprite renderer.
        batch = new SpriteBatch();
        // Creates the textures of the game.
        loadObjects();
        // Creates the lights in the game.
        lHandler = new LightHandler(this, world, map);
        // Creates the bounds in the game.
        bHandler = new BoundHandler(map);
        // Creates the various objects in the game.
        Creator creator = new Creator(map, this, world);
        creator.createInstances();
        // Creates the black screen.
        screen = new BlackScreen(this, spriteAtlases.get(1));
    }

    public void restart() {
        // Recreates the various objects in the game. First empties out the arraylists, and then recreates the two
        // handlers.
        UIs = new ArrayList<UI>();
        tObjects = new ArrayList<TerrainObject>();
        enemies = new ArrayList<Enemy>();

        world = new World(new Vector2(0, 0), true);
        world.setContactListener(new PhysicsContactListener());
        lHandler = new LightHandler(this, world, map);
        bHandler = new BoundHandler(map);
        // Recreates the objects and unfreezes the game.
        Creator creator = new Creator(map, this, world);
        creator.createInstances();
        screen.setDisplaying(false);
        input.unFreeze();
    }

    // Destroys this instance of the game, effectively ending it.
    public void end() {
        Gdx.app.exit();
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

        // Renders all objects and then updates the camera.
        batch.begin();
        drawSprites();
        batch.end();

        // Renders the various lights.
        lHandler.light();

        // Ensures that the player is visible at all times whilst everything else is shrouded in darkness. Note that the
        // black screen draws over EVERYTHING.
        batch.begin();
        drawUI();
        player.draw(batch);
        screen.draw(batch);
        batch.end();

        updateCamera();
    }

    // Draws all sprites in the game, some by iterating through loops.
    private void drawSprites() {
        for (Enemy enemy : enemies)
            enemy.draw(batch);
        for (TerrainObject tObject : tObjects)
            tObject.draw(batch);
    }

    // Draws all the UI in the game, by iterating through loops.
    private void drawUI() {
        for (UI ui : UIs)
            ui.draw(batch);
    }

    private void updateCamera() {
        // Sets the camera to the position of the player.
        camera.position.set(player.getX(), player.getY(), 0);
        // If camera is beyond the left bounds, sets the camera just to the right of the left bounds.
        if (camera.position.x - camera.viewportWidth / 2 < 0)
            camera.position.x = camera.viewportWidth / 2;
        // If camera is beyond the right bounds, sets the camera just to the left of the right bounds.
        if (camera.position.x + camera.viewportWidth / 2 > 1280)
            camera.position.x = 1280 - camera.viewportWidth / 2;
        // If camera is beyond the bottom bounds, sets the camera just to the top of the bottom bounds.
        if (camera.position.y - camera.viewportHeight / 2 < 0)
            camera.position.y = camera.viewportHeight / 2;
        // If camera is beyond the top bounds, sets the camera just to the bottom of the top bounds.
        if (camera.position.y + camera.viewportHeight / 2 > 1280)
            camera.position.y = 1280 - camera.viewportHeight / 2;
        // Updates camera of its new position.
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
        manager.load("UI/UI.pack", TextureAtlas.class);
        manager.finishLoading();

        spriteAtlases.add((TextureAtlas) manager.get("Sprites/Sprites.pack"));
        spriteAtlases.add((TextureAtlas) manager.get("UI/UI.pack"));
    }

    // Gets the normal coordinates of the projected value. This is useful for obtaining the position of the mouse on
    // the screen.
    public Vector2 unproject(Vector2 value) {
        // Gets the unprojection from the camera.
        Vector3 v = camera.unproject(new Vector3(value, 0));
        // Returns the x and y of the unprojection.
        return new Vector2(v.x, v.y);
    }

    // Lose Screen. Freezes the game and informs the black screen to display a you have lost screen.
    public void loseScreen() {
        screen.setDisplaying(true);
        screen.setLost(true);
        input.freeze();
    }

    // Win Screen. Freezes the game and informs the black screen to display a you have won screen.
    public void winScreen() {
        screen.setDisplaying(true);
        screen.setLost(false);
        input.freeze();
    }

    // Unprojects the user mouse coordinates so that it is the same as the game screen and then launches the check click
    // method.
    public void checkClick(int x, int y) {
        Vector3 unprojection = camera.unproject(new Vector3(x, y, 1));
        screen.checkClick(unprojection.x, unprojection.y);
    }

    // Unprojects the user mouse coordinates so that it is the same as the game screen and then launches the check hover
    // method.
    public void checkHover(int x, int y) {
        Vector3 unprojection = camera.unproject(new Vector3(x, y, 1));
        screen.checkHover(unprojection.x, unprojection.y);
    }

    public TiledMap getMap() {
        return map;
    }
}
