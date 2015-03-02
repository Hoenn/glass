package me.glassware.entities;

import me.glassware.main.Game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class PickUp extends B2DSprite
{
	private String name;
	public PickUp(Body body, String name)
	{
		super(body);
		this.name=name;
		Texture tex = Game.res.getTexture("sword");
		//Texture tex = Game.res.getTexture(name);
		TextureRegion[] sprites = TextureRegion.split(tex, 18, 18)[0];
		setAnimation(sprites, 0f);
	}
	public String getName()
	{
		return name;
	}
}
