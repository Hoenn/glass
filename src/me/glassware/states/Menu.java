package me.glassware.states;

import static me.glassware.handlers.B2DVars.PPM;
import me.glassware.handlers.B2DVars;
import me.glassware.handlers.GameContactListener;
import me.glassware.handlers.GameInput;
import me.glassware.handlers.GameStateManager;
import me.glassware.main.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
//This is a commit test
public class Menu extends GameState
{	
	private World world;
	private Box2DDebugRenderer b2dr;
	
	private OrthographicCamera b2dCam;
	
	private Body playerBody;
	
	private GameContactListener contacts;
	public Menu(GameStateManager gsm)
	{
		super(gsm);
		
		world = new World(new Vector2(0f, 0f), true);
		contacts= new GameContactListener();
		world.setContactListener(contacts);
		
		b2dr= new Box2DDebugRenderer();
		
		BodyDef bdef = new BodyDef();
		Body body;
		
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		
		
		//Create Left Wall
		bdef.position.set(0/PPM, 0/PPM);
		bdef.type= BodyType.StaticBody;
		body = world.createBody(bdef);
		
		shape.setAsBox(1/PPM, Game.V_HEIGHT/PPM);
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_GROUND;
		fdef.filter.maskBits = B2DVars.BIT_PLAYER;
		body.createFixture(fdef).setUserData("Left Wall");
				
		
		//Create Right Wall
		bdef.position.set(Game.V_WIDTH/PPM, 0/PPM);
		bdef.type= BodyType.StaticBody;
		body = world.createBody(bdef);
		
		shape.setAsBox(1/PPM, Game.V_HEIGHT/PPM);
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_GROUND;
		fdef.filter.maskBits = B2DVars.BIT_PLAYER;
		body.createFixture(fdef).setUserData("Right Wall");
		
		//Create Top Wall
		bdef.position.set(Game.V_WIDTH/PPM, Game.V_HEIGHT/PPM);
		bdef.type= BodyType.StaticBody;
		body = world.createBody(bdef);
		
		shape.setAsBox(Game.V_WIDTH/PPM, 1/PPM);
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_GROUND;
		fdef.filter.maskBits = B2DVars.BIT_PLAYER;
		body.createFixture(fdef).setUserData("Top Wall");
		
		
		//Create Bottom Wall
		bdef.position.set(0/PPM, 0/PPM);
		bdef.type= BodyType.StaticBody;
		body = world.createBody(bdef);
		
		shape.setAsBox(Game.V_WIDTH/PPM, 1/PPM);
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_GROUND;
		fdef.filter.maskBits = B2DVars.BIT_PLAYER;
		body.createFixture(fdef).setUserData("Bottom Wall");
		
		
		//Player
		bdef.position.set(160/PPM, 200/PPM);
		bdef.type = BodyType.DynamicBody;
		playerBody = world.createBody(bdef);
		
		shape.setAsBox(5/PPM, 5/PPM);
		fdef.shape = shape;
		fdef.restitution = .5f;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_GROUND;
		playerBody.createFixture(fdef).setUserData("Player");
		
		
		//create foot sensor
		shape.setAsBox(2/PPM, 2/PPM, new Vector2(0, -5/PPM), 0);
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_GROUND;
		fdef.isSensor=true;
		playerBody.createFixture(fdef).setUserData("foot");
		
		//set up b2d camera
		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false, Game.V_WIDTH/PPM, Game.V_HEIGHT/PPM);
		

		
	}
	public void handleInput()
	{
		if(GameInput.isDown(GameInput.BUTTON_W))
		{
			playerBody.applyLinearImpulse(0	, .15f, playerBody.getLocalCenter().x, playerBody.getLocalCenter().y, true); //Tighter controls, immediately modifies VELOCITY
		}
		if(GameInput.isDown(GameInput.BUTTON_A))
		{
			playerBody.applyLinearImpulse(-.15f, 0f, playerBody.getLocalCenter().x, playerBody.getLocalCenter().y, true);
		}
		if(GameInput.isDown(GameInput.BUTTON_S))
		{
			playerBody.applyLinearImpulse(0	, -.15f, playerBody.getLocalCenter().x, playerBody.getLocalCenter().y, true);
		}
		if(GameInput.isDown(GameInput.BUTTON_D))
		{
			playerBody.applyLinearImpulse(.15f	, 0f, playerBody.getLocalCenter().x, playerBody.getLocalCenter().y, true);
		}
		if(GameInput.isPressed(GameInput.BUTTON_Z))
		{
			playerBody.setTransform(new Vector2(120/PPM, 200/PPM), 0);
			playerBody.setLinearVelocity(0f, 0f);
		}

			
	}
	
	public void update(float dt)
	{
		handleInput();
		
		world.step(dt, 6, 2);
	}
	
	public void render()
	{
		//clear screen
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//draw world
		b2dr.render(world, b2dCam.combined);
	}
	
	public void dispose(){}
}
