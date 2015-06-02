package me.glassware.entities;

import me.glassware.handlers.B2DVars;
import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;

public class LightComponent
{
	private static final int maxRaysCast=500;
	private PointLight pointLight;
	private ConeLight coneLight;
	public LightComponent()
	{
		pointLight=null;
		coneLight=null;
	}
	public void createPointLight(Body b, RayHandler rh, Color color, float distance)
	{
		pointLight = new PointLight(rh,  maxRaysCast, color, distance, b.getPosition().x, b.getPosition().y);
		pointLight.setSoftnessLength(0f);//Makes shadows look better
		pointLight.attachToBody(b); //Light follows player
		pointLight.setContactFilter( B2DVars.BIT_PLAYER, (short)(0), B2DVars.BIT_OBJECT); //Light has the Mask and category bits of the Player 
	}
	public void createConeLight(Body b, RayHandler rh, Color color, float distance, float facingDirection)
	{
		coneLight = new ConeLight(rh, maxRaysCast, color, distance, b.getPosition().x, b.getPosition().x, facingDirection, 45);
		coneLight.setSoftnessLength(0f);
		coneLight.attachToBody(b, 0, 0, facingDirection);
		coneLight.setContactFilter(B2DVars.BIT_PLAYER, (short)(0), B2DVars.BIT_GROUND);
	}
	public boolean isPointLightActive()
	{
		return pointLight.isActive();
	}
	public void setPointLightActive(boolean b)
	{
		pointLight.setActive(b);
	}
	public void togglePointLight()
	{
		if(pointLight.isActive())
			pointLight.setActive(false);
		else
			pointLight.setActive(true);
	}
	public boolean isConeLightActive()
	{
		return coneLight.isActive();
	}
	public void setConeLightActive(boolean b)
	{
		coneLight.setActive(b);
	}
	public void toggleConeLight()
	{
		if(coneLight.isActive())
			coneLight.setActive(false);
		else
			coneLight.setActive(true);
	}
	public void setPointLightDistance(float newDistance)
	{
		pointLight.setDistance(newDistance);
	}
	public void setConeLightDistance(float newDistance)
	{
		coneLight.setDistance(newDistance);
	}
}
