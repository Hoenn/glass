package me.glassware.handlers;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class GameContactListener implements ContactListener
{
	private boolean playerOnGround;
	//Called when two fixtures begin to collide
	public void beginContact(Contact c)
	{
		Fixture fa = c.getFixtureA();
		Fixture fb = c.getFixtureB();

		if(fa.getUserData()!=null && fa.getUserData().equals("foot"))
		{
			playerOnGround=true;
		}
		if(fb.getUserData()!=null && fb.getUserData().equals("foot"))
		{
			playerOnGround=true;
		}
	}
	//Called when two fixtures no longer collide
	public void endContact(Contact c)
	{
		Fixture fa = c.getFixtureA();
		Fixture fb = c.getFixtureB();
		if(fa.getUserData()!=null && fa.getUserData().equals("foot"))
		{
			playerOnGround=false;
		}
		if(fb.getUserData()!=null && fb.getUserData().equals("foot"))
		{
			playerOnGround=false;
		}
	}
	public boolean isPlayerOnGround()
	{
		return playerOnGround;
	}
	//Collision Detetction ->
	//preSolve
	//Collision Handling
	//postSolve
	public void preSolve(Contact c, Manifold m){}
	public void postSolve(Contact c, ContactImpulse ci){}
}
