package me.glassware.states;

import static me.glassware.handlers.B2DVars.PPM;
import me.glassware.entities.PickUp;
import me.glassware.entities.Player;
import me.glassware.handlers.B2DVars;
import me.glassware.handlers.GameContactListener;
import me.glassware.handlers.GameInput;
import me.glassware.handlers.GameStateManager;
import me.glassware.main.Game;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
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
public class Menu extends GameState
{	
	private World world;
	private Box2DDebugRenderer b2dr;
	private boolean debug=false;
	
	private OrthographicCamera b2dCam;
		
	private TiledMap tileMap;
	private OrthogonalTiledMapRenderer tmr;
	private float tileSize;
	
	private GameContactListener contacts;
	
	private Player player;
	private Array<PickUp> pickUps;
	public Menu(GameStateManager gsm)
	{
		super(gsm);
		
		//Set up Box2D
		world = new World(new Vector2(0f, 0f), true);
		contacts= new GameContactListener();
		world.setContactListener(contacts);		
		b2dr= new Box2DDebugRenderer();
		
		//Create Player
		createPlayer();
		
		//Create Tiles
		createTiles();
		
		//create pick ups
		createPickUps();
			
		//set up b2d camera
		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false, Game.V_WIDTH/PPM, Game.V_HEIGHT/PPM);		
		
	}
	public void handleInput()
	{
		if(GameInput.isDown(GameInput.BUTTON_W))
		{
			player.getBody().applyLinearImpulse(0	, .30f, player.getBody().getLocalCenter().x, player.getBody().getLocalCenter().y, true); //Tighter controls, immediately modifies VELOCITY
		}
		if(GameInput.isDown(GameInput.BUTTON_A))
		{
			player.getBody().applyLinearImpulse(-.30f, 0f, player.getBody().getLocalCenter().x, player.getBody().getLocalCenter().y, true);
		}
		if(GameInput.isDown(GameInput.BUTTON_S))
		{
			player.getBody().applyLinearImpulse(0	, -.30f, player.getBody().getLocalCenter().x, player.getBody().getLocalCenter().y, true);
		}
		if(GameInput.isDown(GameInput.BUTTON_D))
		{
			player.getBody().applyLinearImpulse(.30f	, 0f, player.getBody().getLocalCenter().x, player.getBody().getLocalCenter().y, true);
		}
		if(GameInput.isDown(GameInput.BUTTON_UP))
		{
			
		}
		if(GameInput.isDown(GameInput.BUTTON_RIGHT))
		{
			
		}
		if(GameInput.isDown(GameInput.BUTTON_DOWN))
		{
			
		}
		if(GameInput.isDown(GameInput.BUTTON_LEFT))
		{

		}
		if(GameInput.isPressed(GameInput.BUTTON_Z))
		{
			player.getBody().setLinearVelocity(0f, 0f);
		}		
	}
	
	public void update(float dt)
	{
		handleInput();
		
		world.step(dt, 6, 2);
		player.update(dt);
		
		Array<Body> bodies=contacts.getBodiesToRemove();
		for(Body b: bodies)
		{
			pickUps.removeValue((PickUp)b.getUserData(), true);
			world.destroyBody(b);
			//TODO Player to collect the object
		}
		bodies.clear();
		
		for(PickUp p: pickUps)
			p.update(dt);
	}
	
	public void render()
	{
		//clear screen
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//draw tile map
		tmr.setView(cam);
		tmr.render();
		
		//draw pickups
		for(PickUp p: pickUps)
			p.render(sb);
		
		//draw player
		sb.setProjectionMatrix(cam.combined);
		player.render(sb);

		
		
		//draw Box2dworld
		if(debug)
			b2dr.render(world, b2dCam.combined);
		
	}
	private void createPlayer()
	{
		BodyDef bdef = new BodyDef();
		FixtureDef fdef= new FixtureDef();
		PolygonShape shape = new PolygonShape();
		
		bdef.position.set(160/PPM, 200/PPM);
		bdef.type = BodyType.DynamicBody;
		Body body= world.createBody(bdef);
		
		shape.setAsBox(8/PPM, 8/PPM);
		fdef.shape = shape;
		fdef.restitution = .5f;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_GROUND|B2DVars.BIT_PICKUP;
		body.setLinearDamping(10f);
		body.createFixture(fdef).setUserData("Player");
		
		
		//create foot sensor
		shape.setAsBox(2/PPM, 2/PPM, new Vector2(0, -5/PPM), 0);
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_GROUND;
		fdef.isSensor=true;
		body.createFixture(fdef).setUserData("foot");
		
		//Create Player
		player = new Player(body);
		body.setUserData("player");
		
		//body.getUserData() -> returns player object
		//player.getBody -> returns body
	}
	private void createTiles()
	{
		//load tile map
		tileMap = new TmxMapLoader().load("res/maps/test.tmx");
		tmr = new OrthogonalTiledMapRenderer(tileMap);
		
		tileSize = (int) tileMap.getProperties().get("tilewidth");
		
		TiledMapTileLayer layer;
		layer=(TiledMapTileLayer) tileMap.getLayers().get("Walls");
		createLayer(layer);
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
				v[0] = new Vector2(-tileSize/2/PPM, -tileSize/2/PPM); //Bottom Left
				v[1] = new Vector2(-tileSize/2/PPM, tileSize/2/PPM); //Top Left
				v[2]= new Vector2(tileSize/2/PPM, tileSize/2/PPM); //Top Right
				v[3]= new Vector2(tileSize/2/PPM, -tileSize/2/PPM); //Bottom Right
				
				v[4] = new Vector2(-tileSize/2/PPM, -tileSize/2/PPM);//Completes the chain
				cs.createChain(v);
				
				fdef.friction=0;
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
		
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		
		float x=0, y=0;
		
		for(MapObject mo: layer.getObjects())
		{
			if(mo instanceof RectangleMapObject)
			{
				Rectangle r = ((RectangleMapObject) mo).getRectangle();
				x = r.x;
				y = r.y;
			}
			bdef.position.set(x/PPM, y/PPM);
			
			shape.setAsBox(9/PPM, 9/PPM);
			fdef.shape=shape;
			fdef.isSensor=true;
			fdef.filter.categoryBits = B2DVars.BIT_PICKUP;
			fdef.filter.maskBits=B2DVars.BIT_PLAYER;
			Body body = world.createBody(bdef);
			//TODO Set userData to a random value from list
			// of pickups so that they can be created as 
			// item objects and added to player inventory
			body.createFixture(fdef).setUserData("pickUp");
			
			PickUp p = new PickUp(body);
			
			pickUps.add(p);
			
			body.setUserData(p);
			
			
		}
	}
	public void dispose(){}
}
