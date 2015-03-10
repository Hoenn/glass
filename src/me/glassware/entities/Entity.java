package me.glassware.entities;

import me.glassware.handlers.Animation;
import me.glassware.handlers.B2DVars;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Entity
{

	protected Body body;
	protected Animation animation;
	protected float width;
	protected float height;	
	
	protected float facingDirection;


	
	public Entity(Body body)
	{
		this.body=body;
		animation=new Animation();
	}
	public void setAnimation(TextureRegion[] reg, float delay)//Default one animation Entity
	{
		animation.setFrames(reg, delay);

		width=reg[0].getRegionWidth();
		height=reg[0].getRegionHeight();
	}

	public void update(float dt)
	{	
		animation.update(dt);

	}
	public void render(SpriteBatch sb)
	{
		sb.begin();
		sb.draw(animation.getFrame(), //The Image
				body.getPosition().x*B2DVars.PPM-width/2,  //Position X
				body.getPosition().y*B2DVars.PPM-height/2, //Position Y
				width/2, height/2, //Origin of rotation
				width, height, //width and height
				1f, 1f, //X scale and Y Scale
				facingDirection); //Direction in degrees

		sb.end();
	}
	public void setDirection(float dir)
	{
		facingDirection=dir;
	}
	public Body getBody()
	{
		return body;
	}
	public Vector2 getPosition()
	{
		return body.getPosition();
	}
	public float getWidth()
	{
		return width;
	}
	public float getHeight()
	{
		return height;
	}

}
