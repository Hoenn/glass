package me.glassware.handlers;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;

public class GameContactListener implements ContactListener
{
	private Array<Body> bodiesToRemove;
	public GameContactListener()
	{
		bodiesToRemove= new Array<Body>();
	}
	public void beginContact(Contact c)
	{
		Fixture fa = c.getFixtureA();
		Fixture fb = c.getFixtureB();	
		
		if(fa.getUserData()!=null&&fa.getUserData().equals("pickUp"))
		{
			bodiesToRemove.add(fa.getBody());
		}
		if(fb.getUserData()!=null&&fb.getUserData().equals("pickUp"))
		{
			bodiesToRemove.add(fb.getBody());
		}
		if((fa.getUserData()!=null&&fa.getUserData().equals("attackObject"))&&(fb.getUserData()!=null&&fb.getUserData().equals("Player")))
		{
			bodiesToRemove.add(fa.getBody());
		}
		if((fb.getUserData()!=null&&fb.getUserData().equals("attackObject"))&&(fa.getUserData()!=null&&fa.getUserData().equals("Player")))
		{
			bodiesToRemove.add(fb.getBody());
		}
		if((fa.getUserData()!=null&&fa.getUserData().equals("playerMelee"))&&(fb.getUserData()!=null&&fb.getUserData().equals("pickUp")))
		{
			bodiesToRemove.add(fa.getBody());
		}
		if((fb.getUserData()!=null&&fb.getUserData().equals("playerMelee"))&&(fa.getUserData()!=null&&fa.getUserData().equals("pickUp")))
		{
			bodiesToRemove.add(fb.getBody());
		}
	}
	//Called when two fixtures no longer collide
	public void endContact(Contact c)
	{
		Fixture fa = c.getFixtureA();
		Fixture fb = c.getFixtureB();
		if(fa==null||fb==null)
			return;
	}
	public Array<Body> getBodiesToRemove()
	{
		return bodiesToRemove;
	}

	public void preSolve(Contact c, Manifold m){}
	public void postSolve(Contact c, ContactImpulse ci){}
}
