package me.glassware.entities;

import me.glassware.handlers.Animation;
import me.glassware.handlers.B2DVars;
import me.glassware.handlers.ParticleManager;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class Entity
{
	protected Body body;
	protected Animation animation;
	protected float width;
	protected float height;	
	
	protected float facingDirection;
	
	protected ParticleManager particleManager;
	
	public static Player player;
	
	public enum FacingDirection{LEFT, UP, RIGHT, DOWN;}
	
	public Entity()
	{
		//this.body=body;
		animation=new Animation();
		particleManager=new ParticleManager(this);
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
		particleManager.update(dt);
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
	
		particleManager.render(sb);

		sb.end();
	}
	public void setDirection(float dir)
	{
		facingDirection=dir;
	}
	public float getDirection()
	{
		return facingDirection;
	}
	public Body getBody()
	{
		return body;
	}
	public Vector2 getPosition()
	{
		return body.getPosition();
	}
	public Vector2 getPixelPosition()
	{
		return new Vector2(body.getPosition().x*B2DVars.PPM, body.getPosition().y*B2DVars.PPM);
	}
	public float getWidth()
	{
		return width;
	}
	public float getHeight()
	{
		return height;
	}

	public abstract void moveUp();
	public abstract void moveDown();
	public abstract void moveLeft();
	public abstract void moveRight();
}
