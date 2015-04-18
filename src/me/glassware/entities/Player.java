package me.glassware.entities;

import static me.glassware.handlers.B2DVars.PPM;
import me.glassware.handlers.B2DVars;
import me.glassware.main.Game;
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
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Player extends Entity
{
	private int maxHealth;
	private int currentHealth;
	private Sound hurtSound;
	private float invincTime=0;
	private float invincDuration=1f; //In seconds
	public enum HealthState{Default, Invincible}
	private HealthState HEALTH_STATE;
	
	private Array<Item> inventory;
	
	private ParticleEffect healingEffect;
	
	private int tileSight;
	private float visionDistance;
	
	private LightComponent lightComponent;
	
	private Activity ACTIVITY;
	
	public enum State{Idle, Attacking}
	private State STATE;
	private float attackTime=0; //Accumulator
	private float attackDuration=.5f;//In Seconds
	private float fistRange=20;
	private FixtureDef fist_fDef;
	private float swordRange=40;
	private FixtureDef sword_fDef;
	private FixtureDef dagger_fDef;
	private FixtureDef spear_fDef;
	
	//@Param sight is about how many tiles the player can see with 
	//attached light enabled
	public Player(World world, float tileSize, int sight)
	{
		super.player=this;
		
		STATE=State.Idle;
		HEALTH_STATE=HealthState.Default;
		
		inventory = new Array<Item>();
		hurtSound= Game.manager.get("res/sounds/magic154.ogg");
		maxHealth=100;
		currentHealth=maxHealth;

		tileSight=sight;
		setVisionDistance(tileSize);
		
		lightComponent = new LightComponent();
		
		healingEffect= new ParticleEffect();
		healingEffect.load(Gdx.files.internal("res/particles/healing.p"), Game.atlas);
		particleManager.addParticle(healingEffect);
		
		facingDirectionInDegree=0f;
		
		for(int i=0;i<40;i++)
			addItem(new Item("potion"));
		
		TextureRegion frames= Game.atlas.findRegion("wizardSprite");
		setAnimation(frames.split(20,20)[0], 1/4f);
		
				
		//Fist attack
		fist_fDef= new FixtureDef();		
		fist_fDef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fist_fDef.filter.maskBits = B2DVars.BIT_NPC|B2DVars.BIT_PICKUP;
		ChainShape fistShape = new ChainShape();
		fistShape.createLoop(getConeVertices(fistRange, 80, 16));
		fist_fDef.shape=fistShape;
		fist_fDef.isSensor=true;
		
		//Sword Attack
		sword_fDef= new FixtureDef();
		sword_fDef.filter.categoryBits = B2DVars.BIT_WEAPON;
		sword_fDef.filter.maskBits = B2DVars.BIT_NPC;
		ChainShape swordShape = new ChainShape();
		swordShape.createLoop(getConeVertices(swordRange, 90, 15));
		sword_fDef.shape=swordShape;
		sword_fDef.isSensor=true;
			
		//Initialize player body;
		resetBody(world);
			
	}
	@Override
	public void update(float dt)
	{

		if(STATE==State.Attacking&&attackTime<attackDuration)
		{
			attackTime+=dt;
		}
		else
		{
			attackTime=0;
			removeAttack();
			STATE=State.Idle;
		}

		if(HEALTH_STATE==HealthState.Invincible&&invincTime<invincDuration)
		{
			invincTime+=dt;
		}
		else
		{
			invincTime=0;
			HEALTH_STATE=HealthState.Default;
		}
		super.update(dt);
	}
	public void resetBody(World world)
	{
		//Player body 
		BodyDef bdef = new BodyDef();
		FixtureDef fdef= new FixtureDef();
		CircleShape shape = new CircleShape();
		
		bdef.position.set(30/PPM, 30/PPM);
		bdef.type = BodyType.DynamicBody;
		bdef.gravityScale=0;
		body=world.createBody(bdef);
		body.setLinearDamping(15f);
		
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
	private Vector2[] getConeVertices(float radius, float arcInDegree, int angularIncrementInDegree) 
	{

		int angularIncrements=(int) (arcInDegree/angularIncrementInDegree);

        Vector2[] coneVertices = new Vector2[angularIncrements+2];
		coneVertices[0] = new Vector2(0, 0);
		
		float angle=0;
		float arcInRad=arcInDegree*MathUtils.degRad;
		float adjust=arcInRad/2;
		
		for(int i=0;i<angularIncrements+1;i++)
		{
			angle= (i*(arcInRad/angularIncrements))-adjust;
			coneVertices[i+1]= new Vector2((radius*MathUtils.cos(angle))/PPM, (radius*MathUtils.sin(angle))/PPM);
		}
		return coneVertices;
	}
	public void activatePointLight(RayHandler rh, Color color)
	{
		lightComponent.createPointLight(this.body, rh, color, visionDistance);
	}
	public void activateConeLight(RayHandler rh, Color color)
	{
		lightComponent.createConeLight(this.body, rh, color, visionDistance, facingDirectionInDegree);
	}
	public LightComponent getLightComponent()
	{
		return lightComponent;
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
		if(HEALTH_STATE==HealthState.Default)
		{
			if(hurtSound!=null)
				hurtSound.play(.15f);
			currentHealth-=dmg;
			
			HEALTH_STATE=HealthState.Invincible;
		}
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
	public void swingFist()
	{
		if(STATE==State.Idle)
		{
			STATE=State.Attacking;
			body.createFixture(fist_fDef).setUserData("playerFist");
		}
	}
	public void swingSword()
	{
		if(STATE==State.Idle)
		{
			STATE=State.Attacking;
			body.createFixture(sword_fDef).setUserData("swordFist");
		}
	}
	public void removeAttack()
	{
		if(body.getFixtureList().size>1)
		{
			body.destroyFixture(body.getFixtureList().removeIndex(1));
		}
	}
	public void setPixelPosition(Vector2 targetPos)
	{
		body.setTransform(targetPos.x/PPM, targetPos.y/PPM, 0f);
	}
	public void setPixelPosition(float x, float y)
	{
		body.setTransform(x/PPM, y/PPM, 0f);
	}
	public void moveUp()
	{
		body.applyLinearImpulse(0	, .40f, getBody().getLocalCenter().x, getBody().getLocalCenter().y, true);		
	}
	public void moveLeft()
	{
		body.applyLinearImpulse(-.40f, 0f, getBody().getLocalCenter().x, getBody().getLocalCenter().y, true);
	}
	public void moveDown()
	{
		body.applyLinearImpulse(0	, -.40f, getBody().getLocalCenter().x, getBody().getLocalCenter().y, true);
	}
	public void moveRight()
	{
		body.applyLinearImpulse(0.40f	, 0, getBody().getLocalCenter().x, getBody().getLocalCenter().y, true);
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
