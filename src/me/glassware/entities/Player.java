package me.glassware.entities;

import me.glassware.handlers.ParticleManager;
import me.glassware.main.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;

public class Player extends Entity
{
	private int maxHealth;
	private int currentHealth;
	private Sound hurtSound;
	private String hurtSoundPath;
	private Array<Item> inventory;
	private ParticleEffect healingEffect;
	
	public Player(Body body)
	{
		super(body);
		super.player=this;
		inventory = new Array<Item>();
		hurtSound= Game.manager.get("res/sounds/magic154.ogg");
		maxHealth=100;
		currentHealth=-15;
		
		healingEffect= new ParticleEffect();
		healingEffect.load(Gdx.files.internal("res/particles/healing.p"), Gdx.files.internal("res/particles"));
		particleManager.addParticle(healingEffect);
		

		for(int i=0;i<40;i++)
			addItem(new Item("potion"));
		
		TextureRegion frames= Game.atlas.findRegion("wizardSprite");
		setAnimation(frames.split(20,20)[0], 1/4f);
		
	}
	public void setMaxHealth(int h)
	{
		maxHealth=h;
	}
	public int getHealth()
	{
		return currentHealth;
	}
	public void healDamage(int healing)
	{
		
		if(currentHealth==maxHealth)
			System.out.println("full health");
		else 
		{
			if(currentHealth+healing>maxHealth)
				currentHealth=maxHealth;
			else
				currentHealth+=healing;
			particleManager.playParticle(healingEffect);
		}
	}
	
	public void takeDamage(int dmg)
	{
		if(hurtSound!=null)
			hurtSound.play(.15f);
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
	public void moveUp()
	{
		getBody().applyLinearImpulse(0	, .30f, getBody().getLocalCenter().x, getBody().getLocalCenter().y, true); 
	}
	public void moveLeft()
	{
		getBody().applyLinearImpulse(-.30f, 0f, getBody().getLocalCenter().x, getBody().getLocalCenter().y, true);
	}
	public void moveDown()
	{
		getBody().applyLinearImpulse(0	, -.30f, getBody().getLocalCenter().x, getBody().getLocalCenter().y, true);
	}
	public void moveRight()
	{
		getBody().applyLinearImpulse(0.30f	, 0, getBody().getLocalCenter().x, getBody().getLocalCenter().y, true);

	}
}

