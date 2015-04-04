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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
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
	
	private float meleeRange=20;
	private FixtureDef melee_fDef;
	private float swordRange=40;
	private FixtureDef sword_fDef;
	private FixtureDef dagger_fDef;
	private FixtureDef spear_fDef;
	
	private BodyDef bdef;
	private FixtureDef fdef;
	private CircleShape shape;
	
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
		
		
		//For temp cone fixtures
		
		//Melee attack
		melee_fDef= new FixtureDef();		
		melee_fDef.filter.categoryBits = B2DVars.BIT_PLAYER;
		melee_fDef.filter.maskBits = B2DVars.BIT_NPC|B2DVars.BIT_PICKUP;
		PolygonShape melee = new PolygonShape();
		melee.set(getConeVertices(meleeRange));
		melee_fDef.shape=melee;
		melee_fDef.isSensor=true;
		
		//Sword Attack
		sword_fDef= new FixtureDef();
		sword_fDef.filter.categoryBits = B2DVars.BIT_WEAPON;
		sword_fDef.filter.maskBits = B2DVars.BIT_NPC;
		PolygonShape sword = new PolygonShape();
		sword.set(getConeVertices(swordRange));
		sword_fDef.shape=sword;
		sword_fDef.isSensor=true;
		
		//Player body 
		bdef = new BodyDef();
		fdef= new FixtureDef();
		shape = new CircleShape();
		
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

	private Vector2[] getConeVertices(float range)
	{
        Vector2[] coneVertices = new Vector2[8];

		coneVertices[0] = new Vector2(0, 0f  );
		float angle=0;
		float adjust=(MathUtils.PI/4);
		for(int i=0;i<7;i++)
		{
			angle= ((i/6f)*90f*MathUtils.degRad)-adjust;
			coneVertices[i+1]= new Vector2((range*MathUtils.cos(angle))/PPM, (range*MathUtils.sin(angle))/PPM);
		}
		return coneVertices;
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
	public boolean isPointLightActive()
	{
		return pointLight.isActive();
	}
	public void setPointLightActive(boolean b)
	{
		pointLight.setActive(b);
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

		if(currentHealth-dmg<0)
			currentHealth=0;
		//die();
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
	public void toggleMelee()
	{
		body.createFixture(melee_fDef).setUserData("playerMelee");		
	}
	public void toggleSword()
	{
		body.createFixture(sword_fDef).setUserData("swordMelee");
	}
	public void removeMelee()
	{
		if(body.getFixtureList().size>1)
		{
			body.destroyFixture(body.getFixtureList().removeIndex(1));
		}
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

