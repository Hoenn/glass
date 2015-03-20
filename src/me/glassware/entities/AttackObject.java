package me.glassware.entities;

import me.glassware.main.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;

public class AttackObject extends Entity
{
	private int damageValue;
	private ParticleEffect dust;
	public AttackObject(Body body, int dV)
	{
		super(body);
		damageValue=dV;
		TextureRegion frames = Game.atlas.findRegion("meatBall");
		setAnimation(frames.split(16,16)[0], 0f);
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
