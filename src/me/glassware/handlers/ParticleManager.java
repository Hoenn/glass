package me.glassware.handlers;

import java.util.ArrayList;

import me.glassware.entities.Entity;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import me.glassware.handlers.B2DVars;

public class ParticleManager
{
	private ArrayList<ParticleEffect> particles;
	private Entity entity;
	public ParticleManager(Entity e)
	{
		entity=e;
		particles = new ArrayList<ParticleEffect>();
	}
	public void update(float dt)
	{
		for(ParticleEffect p: particles)
		{
			if(!p.isComplete())
				p.update(dt);
				p.setPosition(entity.getPosition().x*B2DVars.PPM, entity.getPosition().y*B2DVars.PPM);
		}
	}
	public void render(SpriteBatch sb)
	{
		for(ParticleEffect p: particles)
		{
			if(!p.isComplete())
				p.draw(sb);			
		}
	}
	public void addParticle(ParticleEffect e)//Adds a particle without starting it
	{
		particles.add(e);
	}
	public void playParticle(ParticleEffect e)//Plays a particle if it exists, or adds it then plays it.
	{
		if(particles.contains(e))
		{
			e.start();
		}
		else
		{
			addParticle(e);
			e.start();
		}
	}
	
}
