package me.glassware.entities;

import static me.glassware.handlers.B2DVars.PPM;
import me.glassware.handlers.B2DVars;
import me.glassware.main.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class AttackObject extends Entity
{
	public enum TrapType{FallingBall, StaticTrap};
	private TrapType type;
	private int damageValue;
	private ParticleEffect particle;
	public AttackObject(World world, TrapType t, int dV, float xPos, float yPos, boolean spinning)
	{
		type=t;
		damageValue=dV;	

		switch(type)
		{
			case FallingBall: createFallingBall(world, dV, xPos, yPos, spinning);
								break;
			case StaticTrap:  createStaticTrap(world, dV, xPos, yPos);
								break;	
		}

		particleManager.addParticle(particle);
		particleManager.playParticle(particle);
	}
	@Override
	public void update(float dt)
	{
		super.update(dt);
		facingDirectionInDegree=body.getAngle()*MathUtils.radDeg;

	}
	private void createStaticTrap(World world, int dV, float x, float y)
	{
		TextureRegion frames = Game.atlas.findRegion("footTrap");
		setAnimation(frames.split(18,18)[0], 0f);
		particle= new ParticleEffect();
		particle.load(Gdx.files.internal("res/particles/trap.p"), Game.atlas);
		
		BodyDef bdef = new BodyDef();
		FixtureDef fdef= new FixtureDef();
		PolygonShape shape = new PolygonShape();
		
		bdef.position.set(x/PPM, y/PPM);
		bdef.type= BodyType.DynamicBody;
		bdef.gravityScale=0f;
		
		body = world.createBody(bdef);
		shape.setAsBox(5/PPM, 5/PPM);
		fdef.shape=shape;
		fdef.shape=shape;
		fdef.restitution=.5f;
		fdef.friction=.5f;
		fdef.filter.categoryBits=B2DVars.BIT_OBJECT;
		fdef.filter.maskBits=B2DVars.BIT_GROUND|B2DVars.BIT_PLAYER;

		body.createFixture(fdef).setUserData("attackObject");
		body.setUserData(this);
		
		Game.currentScreen.getBodyList().add(body);
		shape.dispose();
	}
	private void createFallingBall(World world, int dV, float x, float y, boolean spinning)		
	{
		TextureRegion frames = Game.atlas.findRegion("meatBall");
		setAnimation(frames.split(16,16)[0], 0f);
		particle= new ParticleEffect();
		particle.load(Gdx.files.internal("res/particles/dust.p"),Game.atlas);
		
		BodyDef bdef = new BodyDef();
		FixtureDef fdef= new FixtureDef();
		CircleShape shape = new CircleShape();
		
		bdef.position.set(x/PPM, y/PPM);
		bdef.type= BodyType.DynamicBody;
		bdef.gravityScale=1;
		if(spinning)
			bdef.angularVelocity=(MathUtils.random(5, 20)*MathUtils.randomSign());
		
		body = world.createBody(bdef);
		shape.setRadius(8/PPM);
		fdef.shape=shape;
		fdef.restitution=.5f;
		fdef.friction=.5f;
		fdef.filter.categoryBits=B2DVars.BIT_OBJECT;
		fdef.filter.maskBits=B2DVars.BIT_GROUND|B2DVars.BIT_PLAYER;
		
		body.setLinearVelocity(0f, -.5f);
		body.createFixture(fdef).setUserData("attackObject");
		body.setUserData(this);
		Game.currentScreen.getBodyList().add(body);
		shape.dispose();
	}
	public int getDamageValue()
	{
		return damageValue;
	}
	public void destroy()
	{
		//delete my shit 
	}
	public void moveUp(){}
	public void moveLeft(){}
	public void moveDown(){}
	public void moveRight(){}
}
