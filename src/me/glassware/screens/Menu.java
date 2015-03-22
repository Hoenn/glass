package me.glassware.screens;

import static me.glassware.handlers.B2DVars.PPM;
import me.glassware.entities.AttackObject;
import me.glassware.entities.Item;
import me.glassware.entities.PickUp;
import me.glassware.entities.Player;
import me.glassware.handlers.B2DVars;
import me.glassware.handlers.GameContactListener;
import me.glassware.handlers.GameInput;
import me.glassware.handlers.GameScreenManager;
import me.glassware.main.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
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
	
	private OrthographicCamera b2dCam;
		
	private TiledMap tileMap;
	private OrthogonalTiledMapRenderer tmr;
	private float tileSize;
	private float tileSizeB2D;
		
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
		
		//Play Music
		menuSong= Game.manager.get("res/songs/testmusic.ogg");
		menuSong.setVolume(.5f); //0.0 - 1.0f
		menuSong.setLooping(true);
		menuSong.play();
		
		//Create objects
		player=new Player(world);
		attackObjects= new Array<AttackObject>();
		createTiles();
		createPickUps();
			
		//set up b2d camera
		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false, Game.V_WIDTH/PPM, Game.V_HEIGHT/PPM);		
		
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
		if(GameInput.isPressed(GameInput.BUTTON_Z))
		{
			player.useItemAt(0);

			System.out.println(player.getHealth());
			System.out.println(player.getPosition().x*PPM+" ,"+ player.getPosition().y*PPM);
		}	
		if(GameInput.isPressed(GameInput.BUTTON_X))
		{
			debug=!debug;
			menuSong.stop();
		}
		if(GameInput.isDown(GameInput.BUTTON_C))
		{
			//for(int i =0; i<30;i++)
			//	createFallingBall();
			AttackObject ao = new AttackObject(world, 15, "statictrap",MathUtils.random(30,(int) (tileMap.getProperties().get("width"))*tileSize)-tileSize
					, MathUtils.random(30,(int) (tileMap.getProperties().get("height"))*tileSize)-tileSize, true);
			attackObjects.add(ao);
		}
		if(GameInput.isPressed(GameInput.BUTTON_ESC))
		{
			gsm.setScreen(gsm.PAUSE, true);
		}
	}
	public void update(float dt)
	{
		handleInput();
		
		world.step(Game.STEP, 6, 2);
		player.update(dt);
		
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
		
		for(PickUp p: pickUps)
			p.update(dt);
		for(AttackObject ao: attackObjects)
			ao.update(dt);
	}
	
	public void render()
	{
		//clear screen
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);		
		
		//draw tile map
		tmr.setView(cam);
		tmr.render();
		
		sb.setProjectionMatrix(cam.combined);	
		

		//Follow player cam
		cam.position.set(player.getPosition().x*PPM, player.getPosition().y*PPM, 0f);
		cam.update();
		
		//draw pickups
		for(PickUp p: pickUps)
			p.render(sb);
		
		for(AttackObject ao: attackObjects)
			ao.render(sb);
		
		//draw player
		player.render(sb);
		
		//draw Box2dworld
		if(debug)
		{
			b2dCam.position.set(player.getPosition().x, player.getPosition().y, 10f);
			b2dCam.update();
			b2dr.render(world, b2dCam.combined);
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
	private void createFallingBall()
	{
		//This needs to be cleaned up
		AttackObject ao = new AttackObject(world, 5, "fallingball", MathUtils.random(30,(int) (tileMap.getProperties().get("width"))*tileSize)
											, ((int)(tileMap.getProperties().get("height"))*tileSize)-tileSize, true);
		attackObjects.add(ao);
	}
	private void createBoundries()
	{
		float width=(float)((int)tileMap.getProperties().get("width")*tileSize);
		float height =(float)((int)tileMap.getProperties().get("height")*tileSize);
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
		
		TiledMapTileLayer layer;
		layer=(TiledMapTileLayer) tileMap.getLayers().get("Walls");
		
		createLayer(layer);
		createBoundries();
	}
	private void createLayer(TiledMapTileLayer layer)
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
