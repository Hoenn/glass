package me.glassware.screens;

import static me.glassware.handlers.B2DVars.PPM;
import me.glassware.entities.AttackObject;
import me.glassware.entities.Entity;
import me.glassware.entities.Item;
import me.glassware.entities.PickUp;
import me.glassware.entities.Player;
import me.glassware.handlers.B2DVars;
import me.glassware.handlers.GameContactListener;
import me.glassware.handlers.GameInput;
import me.glassware.handlers.GameScreenManager;
import me.glassware.handlers.LevelCreator;
import me.glassware.main.Game;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class TestLevel extends GameScreen
{
		
	private TiledMap levelMap;
	private OrthogonalTiledMapRenderer tmr;
	private float tileSize;
	private float tileSizeB2D;
	
	private GameContactListener contacts;
	private Box2DDebugRenderer b2dr;
	private boolean debug;
	
	
	private RayHandler rayHandler;
	
	private Music levelSong;
	
	private Array<AttackObject> attackObjects;
	private Array<PickUp> pickUps;
	
	private Player player;
	private final Vector2 spawnPoint= new Vector2(30, 30);
	public TestLevel(GameScreenManager gsm)
	{
		super(gsm);
		Game.currentScreen=this;

		
		//Set up Box2D
		contacts= new GameContactListener();
		Game.world.setContactListener(contacts);		
		b2dr= new Box2DDebugRenderer();		
		debug=true;
		bodyList=new Array<Body>();
		//Set up b2d lights
		rayHandler=new RayHandler(Game.world);
		rayHandler.setShadows(false);
		rayHandler.setCulling(true);
		createAMap();

		//Play Music
		levelSong= Game.manager.get("res/songs/Oblivion_main.ogg");
		levelSong.setVolume(.5f); //0.0 - 1.0f
		levelSong.setLooping(true);
		levelSong.play();
						
		attackObjects= new Array<AttackObject>();
		pickUps = new Array<PickUp>();
		
		this.player = Entity.player;
		player.resetBody(Game.world);
		player.setPixelPosition(spawnPoint);
		
		
	}
	@Override
	public void update(float dt)
	{
		
		handleInput();		

		Game.world.step(Game.STEP, 6, 2);
		
		player.update(dt);
		
		rayHandler.update();
		
		Array<Body> bodies=contacts.getBodiesToRemove();
		for(Body b: bodies)
		{
			if(b.getUserData() instanceof PickUp)
			{
				Item i = new Item (((PickUp)b.getUserData()).getName());
				player.addItem(i);
			
				pickUps.removeValue((PickUp)b.getUserData(), true);	
				Game.world.destroyBody(b);
			}
			if(b.getUserData() instanceof AttackObject)
			{
				AttackObject a = (AttackObject)b.getUserData();
				player.takeDamage(a.getDamageValue());
				a.destroy();
				attackObjects.removeValue(a, true);
				Game.world.destroyBody(b);
			}
		}
		bodies.clear();
		
		//for(PickUp p: pickUps)
		//	p.update(dt);
		for(AttackObject ao: attackObjects)
			ao.update(dt);
			
	}
	@Override
	public void render()
	{
		//clear screen
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);		

		//Move tileMap view to cam position and render
		tmr.setView(cam);
		tmr.render();
				
		//Smooth move camera to player
		cam.position.lerp(new Vector3(player.getPixelPosition(), 0f), .5f);
		cam.update();
		
		//Must be set to draw sb properly
		sb.setProjectionMatrix(cam.combined);	
		
		//draw pickups
		//for(PickUp p: pickUps)
		//	p.render(sb);
		//draw attackObjects
		for(AttackObject ao: attackObjects)
			ao.render(sb);
		
		//draw light source on B2D camera
		rayHandler.setCombinedMatrix(b2dCam.combined);
		//Keep b2dCam with player body
		b2dCam.position.lerp(new Vector3(player.getPosition(), 0f), .5f);
		b2dCam.update();
		rayHandler.render();
		
		//draw player
		player.render(sb);

		//draw Box2dworld
		if(debug)
		{
			b2dr.render(Game.world, b2dCam.combined);
		}		
	}
	private void createAMap()
	{
		levelMap = new TiledMap();
		tmr = new OrthogonalTiledMapRenderer(levelMap);
		
		TextureRegion floorTexture = new TextureRegion(new Texture(Gdx.files.internal("res/maps/Dirt_Floor.png")));
		TextureRegion wallTexture = new TextureRegion(new Texture(Gdx.files.internal("res/maps/Dirt_Wall.png")));

		//TODO: This method DOES NOT NEED PADDING for the floor image.
		
		TiledMapTileLayer floorLayer = new TiledMapTileLayer(20, 20, 20, 20);
		for(int x=0; x<floorLayer.getWidth();x++)
		{
			for(int y=0;y<floorLayer.getHeight();y++)
			{
				Cell cell = new Cell();
				cell.setTile(new StaticTiledMapTile(floorTexture));
				//cell.getTile().setOffsetX(-.5f);
				//cell.getTile().setOffsetY(-.5f);
				floorLayer.setCell(x, y, cell);
			}
		}
		LevelCreator.addBorderToLayer(floorLayer, wallTexture);
		floorLayer.setName("Floor");
		levelMap.getLayers().add(floorLayer);
		
	
		
		TiledMapTileLayer wallLayer = new TiledMapTileLayer(20, 20, 20 ,20);
		for(int x=1; x<wallLayer.getWidth()-1;x++)
		{
			for(int y=1;y<wallLayer.getHeight()-1;y++)
			{
				if(MathUtils.random.nextInt(4)==1)
				{
					Cell cell = new Cell();
					cell.setTile(new StaticTiledMapTile(wallTexture));
					wallLayer.setCell(x, y, cell);
				}
			}
		}
		
		LevelCreator.removeExtraTiles(wallLayer);
		wallLayer.setName("Walls");
		levelMap.getLayers().add(wallLayer);
		tileSize=20;
		tileSizeB2D=tileSize/2/PPM;
		
		LevelCreator.createBoundries(levelMap, Game.world);
		LevelCreator.createSolidWalls(wallLayer, Game.world);
		
	}

	@Override
	public void handleInput()
	{
		{
			if(GameInput.isDown(GameInput.BUTTON_W))
			{
				player.moveUp();
			}
			if(GameInput.isDown(GameInput.BUTTON_A))
			{
				player.moveLeft();
			}
			if(GameInput.isDown(GameInput.BUTTON_S))
			{
				player.moveDown();
			}
			if(GameInput.isDown(GameInput.BUTTON_D))
			{
				player.moveRight();
			}
			if(GameInput.isPressed(GameInput.BUTTON_UP))
			{
				player.faceUp();
			}
			if(GameInput.isPressed(GameInput.BUTTON_LEFT))
			{
				player.faceLeft();
			}
			if(GameInput.isPressed(GameInput.BUTTON_DOWN))
			{
				player.faceDown();
			}
			if(GameInput.isPressed(GameInput.BUTTON_RIGHT))
			{
				player.faceRight();
			}
			if(GameInput.isPressed(GameInput.BUTTON_SPACE))
			{
				player.getLightComponent().toggleConeLight();
				player.swingSword();
			}
			if(GameInput.isPressed(GameInput.BUTTON_Z))
			{
				player.useItemAt(0);

				System.out.println(player.getHealth());
				System.out.println(player.getPixelPosition().x +" ,"+ player.getPixelPosition().y);
			}	
			if(GameInput.isPressed(GameInput.BUTTON_X))
			{
				debug=!debug;
				levelSong.stop();
			}
			if(GameInput.isPressed(GameInput.BUTTON_C))
			{
				player.getLightComponent().togglePointLight();
				player.swingFist();
			}
			if(GameInput.isPressed(GameInput.BUTTON_R))
			{
				rayHandler.setShadows(false);
			}
			if(GameInput.isPressed(GameInput.BUTTON_F))
			{
				rayHandler.setShadows(true);
			}
			if(GameInput.isPressed(GameInput.BUTTON_NUM_1))
			{
				zoomIn();
			}
			if(GameInput.isPressed(GameInput.BUTTON_NUM_2))
			{
				zoomOut();
			}
			if(GameInput.isPressed(GameInput.BUTTON_NUM_3))
			{
				gsm.setScreen(gsm.MENU, false);
			}
			if(GameInput.isPressed(GameInput.BUTTON_ESCAPE))
			{
				gsm.pauseScreen(gsm.TESTLEVEL);
			}
		}
		
	}
	@Override
	public void pause()
	{
		levelSong.pause();
	}
	@Override
	public void resume() 
	{
		if(!levelSong.isPlaying())
		{
			levelSong.play();
		}
	}
	@Override
	public void dispose()
	{
		tmr.dispose();
		levelSong.dispose();
		Game.clearWorld();

	}


}
