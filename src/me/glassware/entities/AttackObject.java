package me.glassware.entities;

import me.glassware.main.Game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;

public class AttackObject extends Entity
{
	private int damageValue;
	public AttackObject(Body body, int dV)
	{
		super(body);
		damageValue=dV;
		TextureRegion frames = Game.atlas.findRegion("meatBall");
		setAnimation(frames.split(16,16)[0], 0f);
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

}
