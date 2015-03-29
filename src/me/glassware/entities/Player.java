package me.glassware.entities;

import static me.glassware.handlers.B2DVars.PPM;
import me.glassware.handlers.B2DVars;
import me.glassware.main.Game;
import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Player extends Entity
{
	private int maxHealth;
	private int currentHealth;
	private Sound hurtSound;
	private Array<Item> inventory;
	private ParticleEffect healingEffect;
	private int tileSight;
	private float visionDistance;
	private PointLight pointLight;
	private ConeLight coneLight;
	public Player(World world)
	{

		super.player=this;
		inventory = new Array<Item>();
		hurtSound= Game.manager.get("res/sounds/magic154.ogg");
		maxHealth=100;
		currentHealth=-15;
		
		tileSight=5;
		visionDistance=0;
		healingEffect= new ParticleEffect();
		healingEffect.load(Gdx.files.internal("res/particles/healing.p"), Game.atlas);
		particleManager.addParticle(healingEffect);
		
		facingDirection=0f;
		
		for(int i=0;i<40;i++)
			addItem(new Item("potion"));
		
		TextureRegion frames= Game.atlas.findRegion("wizardSprite");
		setAnimation(frames.split(20,20)[0], 1/4f);
		
		BodyDef bdef = new BodyDef();
		FixtureDef fdef= new FixtureDef();
		CircleShape shape = new CircleShape();
		
		bdef.position.set(30/PPM, 30/PPM);
		bdef.type = BodyType.DynamicBody;
		bdef.gravityScale=0;
		body=world.createBody(bdef);
		body.setLinearDamping(10f);
		
		shape.setRadius(8/PPM);
		fdef.shape = shape;	
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_GROUND|B2DVars.BIT_PICKUP;
		body.createFixture(fdef).setUserData("Player");
		body.setUserData(this);
		shape.dispose();
		
		//Starting position
		faceDown();
		
		
	}
	public void enablePointLight(RayHandler rh, Color color)
	{
		pointLight = new PointLight(rh,  1000, color, visionDistance, body.getPosition().x, body.getPosition().y);
		pointLight.setSoftnessLength(0f);//Makes shadows look better
		pointLight.attachToBody(body); //Light follows player
		pointLight.setContactFilter( B2DVars.BIT_PLAYER, (short)(0), B2DVars.BIT_GROUND); //Light has the Mask and category bits of the Player 
	}
	public void enableConeLight(RayHandler rh, Color color)
	{
		coneLight = new ConeLight(rh, 1000, color, visionDistance, body.getPosition().x, body.getPosition().x, facingDirection, 45);
		coneLight.setSoftnessLength(0f);
		coneLight.attachToBody(body,0, 0, facingDirection);
		coneLight.setContactFilter(B2DVars.BIT_PLAYER, (short)(0), B2DVars.BIT_GROUND);
	}
	public float getVisionDistance()
	{
		return visionDistance;
	}
	public void setVisionDistance(float tileSize)
	{
		visionDistance=((tileSize*tileSight)/PPM)*2;
	}
	public int getTileSightDistance()
	{
		return tileSight;
	}
	public void setTileSightDistance(int ts)
	{
		tileSight=ts;
	}
	public void setMaxHealth(int h)
	{
		maxHealth=h;
	}
	public int getHealth()
	{
		return currentHealth;
	}
	public void healDamage(int healing)
	{
		
		if(currentHealth==maxHealth)
			System.out.println("full health");
		else 
		{
			if(currentHealth+healing>maxHealth)
				currentHealth=maxHealth;
			else
				currentHealth+=healing;
			particleManager.playParticle(healingEffect);
		}
	}
	
	public void takeDamage(int dmg)
	{
		if(hurtSound!=null)
			hurtSound.play(.15f);
		currentHealth-=dmg;
		//TODO if dead
	}
	public void addItem(Item i)
	{
		inventory.add(i);
	}
	public void useItemAt(int i)
	{
		if(i<inventory.size)
		{
			inventory.get(i).useItem();
			inventory.removeIndex(i);
		}
	}
	public Array<Item> getInventory()
	{
		return inventory;
	}
	public void moveUp()
	{
		body.applyLinearImpulse(0	, .30f, getBody().getLocalCenter().x, getBody().getLocalCenter().y, true); 
	}
	public void moveLeft()
	{
		body.applyLinearImpulse(-.30f, 0f, getBody().getLocalCenter().x, getBody().getLocalCenter().y, true);
	}
	public void moveDown()
	{
		body.applyLinearImpulse(0	, -.30f, getBody().getLocalCenter().x, getBody().getLocalCenter().y, true);
	}
	public void moveRight()
	{
		body.applyLinearImpulse(0.30f	, 0, getBody().getLocalCenter().x, getBody().getLocalCenter().y, true);

	}
	public void faceUp()   
	{
		setDirection(180f); 
		body.setTransform(body.getPosition(), MathUtils.degRad*(90f));
	}
	public void faceLeft() 
	{ 
		setDirection(-90f);
		body.setTransform(body.getPosition(), MathUtils.degRad*(-180f));			
	}
	public void faceDown() 
	{
		setDirection(0);
		body.setTransform(body.getPosition(), MathUtils.degRad*(-90f));
	}
	public void faceRight()
	{ 
		setDirection(90f);
		body.setTransform(body.getPosition(), 0);
	}
}

