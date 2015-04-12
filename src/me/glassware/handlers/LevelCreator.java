package me.glassware.handlers;

import static me.glassware.handlers.B2DVars.PPM;
import me.glassware.main.Game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public class LevelCreator
{
	public static float tileSize;
	public static float tileSizeB2D;
	public static void createBoundries(TiledMap tileMap, World world)
	{
		TiledMapTileLayer temp = (TiledMapTileLayer) tileMap.getLayers().get("Walls");
		float width= temp.getWidth()*tileSize;
		float height =temp.getHeight()*tileSize;
		float bodyWidth=width/2/PPM;
		float bodyHeight=height/2/PPM;

		
		BodyDef bdef= new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		
		bdef.type=BodyType.StaticBody;
		fdef.filter.categoryBits=B2DVars.BIT_GROUND;
		fdef.filter.maskBits=B2DVars.BIT_PLAYER;
		fdef.isSensor=false;
		fdef.friction=.5f;
		
		//LEFT BOUNDRY	
		shape.setAsBox(tileSizeB2D, height/2/PPM);
		bdef.position.set(tileSizeB2D, height/2/PPM); //LEFT
		fdef.shape=shape;
		Body leftWall=world.createBody(bdef);
		leftWall.createFixture(fdef);
		Game.currentScreen.getBodyList().add(leftWall);
		
		//BOTTOM BOUNDRY
		shape.setAsBox(bodyWidth, tileSizeB2D);
		bdef.position.set(bodyWidth, tileSizeB2D);
		fdef.shape=shape;
		Body bottomWall=world.createBody(bdef);
		bottomWall.createFixture(fdef);
		Game.currentScreen.getBodyList().add(bottomWall);		
		
		//RIGHT BOUNDRY		
		shape.setAsBox(tileSizeB2D, bodyHeight);
		bdef.position.set(width/PPM-tileSizeB2D, bodyHeight); //
		fdef.shape=shape;
		Body rightWall=world.createBody(bdef);
		rightWall.createFixture(fdef);
		Game.currentScreen.getBodyList().add(rightWall);		
		
		//TOP BOUNDRY
		shape.setAsBox(bodyWidth, tileSizeB2D);
		bdef.position.set(bodyWidth, height/PPM-tileSizeB2D);
		fdef.shape=shape;
		Body topWall=world.createBody(bdef);
		topWall.createFixture(fdef);
		Game.currentScreen.getBodyList().add(topWall);	

		
		shape.dispose();
	}
	public static void createSolidWalls(TiledMapTileLayer layer, World world)
	{
		BodyDef bdef= new BodyDef();
		FixtureDef fdef= new FixtureDef();
		
		for(int row =0; row<layer.getHeight();row++)
		{
			for(int col =0; col<layer.getWidth();col++)
			{
				//get cell
				Cell cell = layer.getCell(col,row);
				
				//check if cell exist
				if(cell==null)continue;
				if(cell.getTile()==null)continue;
				
				//create a body and fixture from cell
				bdef.type = BodyType.StaticBody;
				bdef.position.set((col+0.5f)*tileSize/PPM, (row+0.5f)*tileSize/PPM);
				
				ChainShape cs = new ChainShape();
				Vector2[] v = new Vector2[5];
				v[0] = new Vector2(-tileSizeB2D, -tileSizeB2D); //Bottom Left
				v[1] = new Vector2(-tileSize/2/PPM, tileSizeB2D); //Top Left
				v[2]= new Vector2(tileSizeB2D, tileSizeB2D); //Top Right
				v[3]= new Vector2(tileSizeB2D, -tileSizeB2D); //Bottom Right
				
				v[4] = new Vector2(-tileSizeB2D, -tileSizeB2D);//Completes the chain
				cs.createChain(v);
				
				fdef.friction=.3f;
				fdef.isSensor=false;
				fdef.shape=cs;
				fdef.filter.categoryBits=B2DVars.BIT_GROUND;
				fdef.filter.maskBits=B2DVars.BIT_PLAYER;
				Body temp = world.createBody(bdef);
				temp.createFixture(fdef);
				Game.currentScreen.getBodyList().add(temp);
				
			}
		}
		
	}
	public static void addBorderToLayer(TiledMapTileLayer layer, TextureRegion texture)
	{
		Cell cell = new Cell();
		for(int x=0;x<layer.getWidth();x++)
		{
			cell.setTile(new StaticTiledMapTile(texture));
			layer.setCell(x, 0, cell);
		}
		for(int y=1;y<layer.getHeight();y++)
		{
			cell.setTile(new StaticTiledMapTile(texture));
			layer.setCell(0, y, cell);
		}
		for(int x=1;x<layer.getWidth();x++)
		{
			cell.setTile(new StaticTiledMapTile(texture));
			layer.setCell(x, layer.getHeight()-1, cell);
		}
		for(int y=1;y<layer.getHeight();y++)
		{
			cell.setTile(new StaticTiledMapTile(texture));
			layer.setCell(layer.getWidth()-1, y, cell);
		}
	}
	public static void removeExtraTiles(TiledMapTileLayer layer)
	{
		
		for(int x=1;x<layer.getWidth()-1;x++)
		{
			for(int y=1;y<layer.getHeight()-1;y++)
			{
				Cell neighbor1 = layer.getCell(x-1, y);
				Cell neighbor2 = layer.getCell(x-1, y-1);
				Cell neighbor3 = layer.getCell(x-1, y+1);
				Cell neighbor4 = layer.getCell(x+1, y);
				Cell neighbor5 = layer.getCell(x+1, y-1);
				Cell neighbor6 = layer.getCell(x+1, y+1);
				Cell neighbor7 = layer.getCell(x, y+1);
				Cell neighbor8 = layer.getCell(x, y-1);
				if(neighbor1!=null&&neighbor2!=null&&neighbor3!=null&&neighbor4!=null&&neighbor5!=null&&neighbor6!=null&&neighbor7!=null&&neighbor8!=null)
					layer.setCell(x, y, null);
			}
		}
		
		
	}
}
