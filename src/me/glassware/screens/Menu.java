package me.glassware.screens;

import static me.glassware.handlers.B2DVars.PPM;
import me.glassware.entities.AttackObject;
import me.glassware.entities.AttackObject.TrapType;
import me.glassware.entities.Entity;
import me.glassware.entities.Item;
import me.glassware.entities.PickUp;
import me.glassware.entities.Player;
import me.glassware.handlers.B2DVars;
import me.glassware.handlers.GameContactListener;
import me.glassware.handlers.GameInput;
import me.glassware.handlers.GameScreenManager;
import me.glassware.main.Game;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
//This is a commit test
public class Menu extends GameScreen
{	
	private World world;
	private Box2DDebugRenderer b2dr;
	private boolean debug;
		
	private RayHandler rayHandler;
		
	private TiledMap tileMap;
	private OrthogonalTiledMapRenderer tmr;
	private float tileSize;
	private float tileSizeB2D;
	
	private TiledMap myMap;
	
	private float maxHeightInBounds;
	private float minHeightInBounds;
	private float maxWidthInBounds;
	private float minWidthInBounds;
		
	private Music menuSong;
	
	private GameContactListener contacts;
	
	private Player player;
	private Array<PickUp> pickUps;
	private Array<AttackObject> attackObjects;
	public Menu(GameScreenManager gsm)
	{
		super(gsm);
		
		//Set up Box2D
		world = new World(new Vector2(0f, -4.8f), true);
		contacts= new GameContactListener();
		world.setContactListener(contacts);		
		b2dr= new Box2DDebugRenderer();		
		debug=false;
		//Set up b2d lights
		rayHandler=new RayHandler(world);
		rayHandler.setShadows(true);
		rayHandler.setCulling(true);

		//Play Music
		menuSong= Game.manager.get("res/songs/testmusic.ogg");
		menuSong.setVolume(.5f); //0.0 - 1.0f
		menuSong.setLooping(true);
		menuSong.play();
		
		//Create objects
		//createTiles();
		createAMap();
		player=new Player(world, tileSize, 7);
		player.createPointLight(rayHandler, Color.CYAN);
		player.createConeLight(rayHandler, Color.PURPLE);

		attackObjects= new Array<AttackObject>();
		//createPickUps();
		
	}
	public void update(float dt)
	{
		handleInput();

		world.step(Game.STEP, 6, 2);
		
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
				world.destroyBody(b);
			}
			if(b.getUserData() instanceof AttackObject)
			{
				AttackObject a = (AttackObject)b.getUserData();
				player.takeDamage(a.getDamageValue());
				a.destroy();
				attackObjects.removeValue(a, true);
				world.destroyBody(b);
			}
		}
		bodies.clear();
		
		//for(PickUp p: pickUps)
		//	p.update(dt);
		for(AttackObject ao: attackObjects)
			ao.update(dt);
	}
	
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
			b2dr.render(world, b2dCam.combined);
		}			
	}
	private void createFallingBall()
	{
		AttackObject ao = new AttackObject(world,  TrapType.FallingBall, 5, 
				MathUtils.random(minWidthInBounds, maxWidthInBounds) , maxHeightInBounds, true);
		attackObjects.add(ao);
	}

	private void createAMap()
	{
		myMap = new TiledMap();
		tmr = new OrthogonalTiledMapRenderer(myMap);
		
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
		addBorderToLayer(floorLayer, wallTexture);
		floorLayer.setName("Floor");
		myMap.getLayers().add(floorLayer);
		
	
		
		TiledMapTileLayer wallLayer = new TiledMapTileLayer(20, 20, 20 ,20);
		for(int x=1; x<wallLayer.getWidth()-1;x++)
		{
			for(int y=1;y<wallLayer.getHeight()-1;y++)
			{
				if(MathUtils.random.nextInt(5)==3)
				{
					Cell cell = new Cell();
					cell.setTile(new StaticTiledMapTile(wallTexture));
					wallLayer.setCell(x, y, cell);
				}
			}
		}
		
		removeExtraTiles(wallLayer);
		wallLayer.setName("Walls");
		myMap.getLayers().add(wallLayer);
		tileSize=20;
		tileSizeB2D=tileSize/2/PPM;
		
		createBoundries();
		createSolidWalls(wallLayer);
		
	}
	private void addBorderToLayer(TiledMapTileLayer layer, TextureRegion texture)
	{
		Cell cell = new Cell();
		for(int x=0;x<layer.getWidth();x++)
		{
			cell.setTile(new StaticTiledMapTile(texture));
			layer.setCell(x, 0, cell);
		}
		for(int y=1;y<layer.getHeight();y++)
		{
			cell.setTile(new StaticTiledMapTile(texture));
			layer.setCell(0, y, cell);
		}
		for(int x=1;x<layer.getWidth();x++)
		{
			cell.setTile(new StaticTiledMapTile(texture));
			layer.setCell(x, layer.getHeight()-1, cell);
		}
		for(int y=1;y<layer.getHeight();y++)
		{
			cell.setTile(new StaticTiledMapTile(texture));
			layer.setCell(layer.getWidth()-1, y, cell);
		}
	}
	private void removeExtraTiles(TiledMapTileLayer layer)
	{
		
		for(int x=1;x<layer.getWidth()-1;x++)
		{
			for(int y=1;y<layer.getHeight()-1;y++)
			{
				Cell neighbor1 = layer.getCell(x-1, y);
				Cell neighbor2 = layer.getCell(x-1, y-1);
				Cell neighbor3 = layer.getCell(x-1, y+1);
				Cell neighbor4 = layer.getCell(x+1, y);
				Cell neighbor5 = layer.getCell(x+1, y-1);
				Cell neighbor6 = layer.getCell(x+1, y+1);
				Cell neighbor7 = layer.getCell(x, y+1);
				Cell neighbor8 = layer.getCell(x, y-1);
				if(neighbor1!=null&&neighbor2!=null&&neighbor3!=null&&neighbor4!=null&&neighbor5!=null&&neighbor6!=null&&neighbor7!=null&&neighbor8!=null)
					layer.setCell(x, y, null);
			}
		}
		
		
	}
	private void createBoundries()
	{
		TiledMapTileLayer temp = (TiledMapTileLayer) myMap.getLayers().get("Walls");
		float width= temp.getWidth()*tileSize;
		float height =temp.getHeight()*tileSize;
		float bodyWidth=width/2/PPM;
		float bodyHeight=height/2/PPM;

		
		BodyDef bdef= new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		
		bdef.type=BodyType.StaticBody;
		fdef.filter.categoryBits=B2DVars.BIT_GROUND;
		fdef.filter.maskBits=B2DVars.BIT_PLAYER;
		fdef.isSensor=false;
		fdef.friction=.5f;
		
		//LEFT BOUNDRY
		shape.setAsBox(tileSizeB2D, height/2/PPM);
		bdef.position.set(tileSizeB2D, height/2/PPM); //LEFT
		fdef.shape=shape;
		world.createBody(bdef).createFixture(fdef);
		
		//BOTTOM BOUNDRY
		shape.setAsBox(bodyWidth, tileSizeB2D);
		bdef.position.set(bodyWidth, tileSizeB2D);
		fdef.shape=shape;
		world.createBody(bdef).createFixture(fdef);
		
		//RIGHT BOUNDRY		
		shape.setAsBox(tileSizeB2D, bodyHeight);
		bdef.position.set(width/PPM-tileSizeB2D, bodyHeight); //
		fdef.shape=shape;
		world.createBody(bdef).createFixture(fdef);
		
		//TOP BOUNDRY
		shape.setAsBox(bodyWidth, tileSizeB2D);
		bdef.position.set(bodyWidth, height/PPM-tileSizeB2D);
		fdef.shape=shape;
		world.createBody(bdef).createFixture(fdef);
		
		shape.dispose();
	}
	private void createTiles()
	{
		//load tile map
		tileMap = new TmxMapLoader().load("res/maps/test.tmx");
		tmr = new OrthogonalTiledMapRenderer(tileMap);

		tileSize = (int) tileMap.getProperties().get("tilewidth");
		tileSizeB2D=tileSize/2/PPM;
		
		//Creates Walls with collision
		TiledMapTileLayer layer;
		layer=(TiledMapTileLayer) tileMap.getLayers().get("Walls");		
		createSolidWalls(layer);
		
		//Create Boundry walls
		createBoundries();
		
		//Initialize Tile Relevant variables
		maxWidthInBounds=((int)(tileMap.getProperties().get("width")))*(tileSize)-tileSize;
		minWidthInBounds=tileSize;
		maxHeightInBounds=((int)(tileMap.getProperties().get("height")))*(tileSize)-tileSize;
		minHeightInBounds=tileSize;
	}
	private void createSolidWalls(TiledMapTileLayer layer)
	{
		BodyDef bdef= new BodyDef();
		FixtureDef fdef= new FixtureDef();
		
		for(int row =0; row<layer.getHeight();row++)
		{
			for(int col =0; col<layer.getWidth();col++)
			{
				//get cell
				Cell cell = layer.getCell(col,row);
				
				//check if cell exist
				if(cell==null)continue;
				if(cell.getTile()==null)continue;
				
				//create a body and fixture from cell
				bdef.type = BodyType.StaticBody;
				bdef.position.set((col+0.5f)*tileSize/PPM, (row+0.5f)*tileSize/PPM);
				
				ChainShape cs = new ChainShape();
				Vector2[] v = new Vector2[5];
				v[0] = new Vector2(-tileSizeB2D, -tileSizeB2D); //Bottom Left
				v[1] = new Vector2(-tileSize/2/PPM, tileSizeB2D); //Top Left
				v[2]= new Vector2(tileSizeB2D, tileSizeB2D); //Top Right
				v[3]= new Vector2(tileSizeB2D, -tileSizeB2D); //Bottom Right
				
				v[4] = new Vector2(-tileSizeB2D, -tileSizeB2D);//Completes the chain
				cs.createChain(v);
				
				fdef.friction=.3f;
				fdef.isSensor=false;
				fdef.shape=cs;
				fdef.filter.categoryBits=B2DVars.BIT_GROUND;
				fdef.filter.maskBits=B2DVars.BIT_PLAYER;
				
				world.createBody(bdef).createFixture(fdef);
				
			}
		}
		
	}
	private void createPickUps()
	{
		pickUps = new Array<PickUp>();
		
		MapLayer layer = tileMap.getLayers().get("PickUps");
	
		float x=0, y=0;	
		for(MapObject mo: layer.getObjects())
		{
			
			Rectangle r = ((RectangleMapObject) mo).getRectangle();
			x = r.x;
			y = r.y;

			//String temp = Game.itemList.get(Game.random.nextInt(Game.itemList.size));
			String temp="potion";
			PickUp p = new PickUp(world, temp, x , y);		
			pickUps.add(p);		
		}
		
	}
	public void handleInput()
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
			player.toggleConeLight();
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
			menuSong.stop();
		}
		if(GameInput.isPressed(GameInput.BUTTON_C))
		{
			player.togglePointLight();
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
		if(GameInput.isPressed(GameInput.BUTTON_ESCAPE))
		{
			gsm.setScreen(gsm.PAUSE, true);
		}
	}
	public void pause()
	{		
		menuSong.pause();
	}
	
	public void resume()
	{
		if(!menuSong.isPlaying())
				menuSong.play();
	}
	public void dispose()
	{
		tmr.dispose();
		world.dispose();		
	}
	public void hide(){}
	public void render(float arg0){}
	public void resize(int arg0, int arg1){}
	public void show(){}
}
