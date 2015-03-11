package me.glassware.entities;

import me.glassware.main.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;

public class Player extends Entity
{
	private int totalHealth;
	private int currentHealth;
	private Sound hurtSound;
	private String hurtSoundPath;
	private Array<Item> inventory;
	
	public Player(Body body)
	{
		super(body);
		
		inventory = new Array<Item>();
		hurtSoundPath= "res/sounds/magic154.ogg";
		hurtSound= Gdx.audio.newSound(Gdx.files.internal(hurtSoundPath));
		totalHealth=100;
		
		TextureRegion frames= Game.atlas.findRegion("wizardSprite");
		setAnimation(frames.split(20,20)[0], 1/4f);
		
	}
	public void setTotalHealth(int h)
	{
		totalHealth=h;
	}
	public int getHealth()
	{
		return currentHealth;
	}
	public void takeDamage(int dmg)
	{
		if(hurtSound!=null)
			hurtSound.play();
		currentHealth-=dmg;
		//TODO if dead
	}
	public void addItem(Item i)
	{
		inventory.add(i);
	}
	public void useItemAt(int i)
	{
		if(i<inventory.size)
		{
			inventory.get(i).useItem();
			inventory.removeIndex(i);
		}
	}
	public Array<Item> getInventory()
	{
		return inventory;
	}
}

