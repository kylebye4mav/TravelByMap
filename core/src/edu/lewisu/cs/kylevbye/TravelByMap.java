package edu.lewisu.cs.kylevbye;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TravelByMap extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	OrthographicCamera cam;
	int WIDTH, HEIGHT;
	
	@Override
	public void create () {
		
		//	720p Resolution
		Gdx.graphics.setWindowedMode(1280, 720);
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();
		
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		cam = new OrthographicCamera(WIDTH, HEIGHT);
		cam.translate(WIDTH/2, HEIGHT/2);
		cam.update();
		batch.setProjectionMatrix(cam.combined);
		
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		processInput();
		
		cam.update();
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
	
	///
	///	Helper Methods
	///
	
	private void processInput() {
		
		//
		//	Zoom (RF)
		//
		
		//	Check 'R', zoom in if needed.
		if(Gdx.input.isKeyPressed(Keys.R)) {
			cam.zoom -= .01f;
		}
		
		//	Check 'F', zoom out if needed.
		if(Gdx.input.isKeyPressed(Keys.F)) {
			cam.zoom += .01f;
		}
		
		//
		//	Rotate (QE)
		//
		if (Gdx.input.isKeyPressed(Keys.Q)) {
			cam.rotate(1f);
		}
		
		//
		//	Process Movement (WASD, Shift + WASD)
		//
		
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
			
			//	Check 'W', translate up if needed.
			if(Gdx.input.isKeyPressed(Keys.W)) {
				cam.translate(0, 7f);
			}
			
			//	Check 'A', translate left if needed.
			if(Gdx.input.isKeyPressed(Keys.A)) {
				cam.translate(-7f, 0);
			}
			
			//	Check 'S', translate down if needed.
			if(Gdx.input.isKeyPressed(Keys.S)) {
				cam.translate(0, -7f);
			}
			
			//	Check 'D', translate right if needed.
			if(Gdx.input.isKeyPressed(Keys.D)) {
				cam.translate(7f, 0);
			}
			
		}
		else {
			
			//	Check 'W', translate up if needed.
			if(Gdx.input.isKeyPressed(Keys.W)) {
				cam.translate(0, 1f);
			}
			
			//	Check 'A', translate left if needed.
			if(Gdx.input.isKeyPressed(Keys.A)) {
				cam.translate(-1f, 0);
			}
			
			//	Check 'S', translate down if needed.
			if(Gdx.input.isKeyPressed(Keys.S)) {
				cam.translate(0, -1f);
			}
			
			//	Check 'D', translate right if needed.
			if(Gdx.input.isKeyPressed(Keys.D)) {
				cam.translate(1f, 0);
			}
			
		}
		
	}
	
}
