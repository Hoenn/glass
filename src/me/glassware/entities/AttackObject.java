package me.glassware.entities;

import static me.glassware.handlers.B2DVars.PPM;

import java.lang.reflect.Array;
import java.util.Arrays;

import me.glassware.handlers.B2DVars;
import me.glassware.main.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class AttackObject extends Entity
{
	private int damageValue;
	private String[] trapChoices = {"fallingball"};
	private ParticleEffect dust;
	public AttackObject(World world, int dV, String type, boolean spinning)
	{
		damageValue=dV;
		TextureRegion frames = Game.atlas.findRegion("meatBall");
		setAnimation(frames.split(16,16)[0], 0f);

		
		switch(type)
		{
			case "fallingball": createFallingBall(world, dV, spinning);
								break;
			default:			throw new IllegalArgumentException("String "+type+" does not match any available attack object constructors \n "
																	+ "available choices are "+ Arrays.toString(trapChoices));
			
		}
		dust= new ParticleEffect();
		dust.load(Gdx.files.internal("res/particles/dust.p"),Game.atlas);
		particleManager.addParticle(dust);
		particleManager.playParticle(dust);

	}
	@Override
	public void update(float dt)
	{
		super.update(dt);
		facingDirection=body.getAngle()*MathUtils.radDeg;

	}
	private void createFallingBall(World world, int dV, boolean spinning)		
	{
		BodyDef bdef = new BodyDef();
		FixtureDef fdef= new FixtureDef();
		CircleShape shape = new CircleShape();
		
		bdef.position.set((30+Game.random.nextInt(340))/PPM, 371/PPM);
		bdef.type= BodyType.DynamicBody;
		bdef.gravityScale=1;//Gives falling balls gravity of 4.8
		if(spinning)
			bdef.angularVelocity=(Game.random.nextInt(100)+5)-Game.random.nextInt(110); //Reduces stuck balls
		super.body = world.createBody(bdef);
		shape.setRadius(8/PPM);
		fdef.shape=shape;
		fdef.restitution=.5f;
		fdef.friction=.5f;
		fdef.filter.categoryBits=B2DVars.BIT_OBJECT;
		fdef.filter.maskBits=B2DVars.BIT_GROUND|B2DVars.BIT_PLAYER;
		
		body.setLinearVelocity(0f, -.5f);
		body.createFixture(fdef).setUserData("attackObject");
		body.setUserData(this);
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
