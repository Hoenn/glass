package me.glassware.entities;

import me.glassware.main.Game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class AttackObject extends Entity
{
	private int damageValue;
	public AttackObject(Body body, int dV)
	{
		super(body);
		damageValue=dV;
		//TextureRegion frames = Game.atlas.findRegion(name);
		//setAnimation(frames.split(18,18)[0], 0f);
	}
	public int getDamageValue()
	{
		return damageValue;
	}

}
