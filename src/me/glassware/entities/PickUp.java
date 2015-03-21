package me.glassware.entities;

import static me.glassware.handlers.B2DVars.PPM;
import me.glassware.handlers.B2DVars;
import me.glassware.main.Game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class PickUp extends Entity
{
	private String name;

	public PickUp(World world, String name, float x, float y)
	{
		//super(body);
		this.name=name;
		
		TextureRegion frames = Game.atlas.findRegion(name);
		setAnimation(frames.split(18,18)[0], 0f);
		
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		bdef.position.set(x/PPM, y/PPM);
		
		shape.setAsBox(9/PPM, 9/PPM);
		fdef.shape=shape;
		fdef.isSensor=true;
		fdef.filter.categoryBits = B2DVars.BIT_PICKUP;
		fdef.filter.maskBits=B2DVars.BIT_PLAYER;
		
		//Referencing protected body from entity
		body = world.createBody(bdef);
		body.createFixture(fdef).setUserData("pickUp");
		body.setUserData(this);
		
		shape.dispose();
	}
	public String getName()
	{
		return name;
	}
	public void moveUp(){}
	public void moveLeft(){}
	public void moveDown(){}
	public void moveRight(){}
}
