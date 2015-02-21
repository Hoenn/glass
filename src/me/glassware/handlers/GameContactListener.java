package me.glassware.handlers;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class GameContactListener implements ContactListener
{
	//Called when two fixtures begin to collide
	public void beginContact(Contact c)
	{
		Fixture fa = c.getFixtureA();
		Fixture fb = c.getFixtureB();
		
		System.out.println(fa.getUserData()+", "+ fb.getUserData());
		
	}
	//Called when two fixtures no longer collide
	public void endContact(Contact c)
	{
	}
	//Collision Detetction ->
	//preSolve
	//Collision Handling
	//postSolve
	public void preSolve(Contact c, Manifold m){}
	public void postSolve(Contact c, ContactImpulse ci){}
}
