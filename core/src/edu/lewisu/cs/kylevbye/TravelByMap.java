package edu.lewisu.cs.kylevbye;

import edu.lewisu.cs.kylevbye.util.*;

import java.io.File;
import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TravelByMap extends ApplicationAdapter {
	
	//	Backs one directory, then goes into assets/config.txt
	//	Default for running on command line
	String fileImageConfig = new File(System.getProperty("user.dir")).getParent() + "\\assets\\config.txt";
	
	String[] fileImageNames;
	SpriteBatch batch;
	Texture img;
	OrthographicCamera cam;
	ArrayList<Image> images;
	int WIDTH, HEIGHT;
	int tries = 0;
	
	@Override
	public void create () {
		
		//	720p Resolution
		Gdx.graphics.setWindowedMode(1280, 720);
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();
		images = new ArrayList<Image>();
		batch = new SpriteBatch();
		
		// Init all images
		TextFileReader fileReader = new TextFileReader(fileImageConfig);
		if (!fileReader.readFile()) {
			
			//	If running through eclipse, change config location.
			//	Will terminate program after 3 failed attempts.
			if (tries < 3) {
				fileImageConfig = new File(System.getProperty("user.dir")).getParent() + "\\core\\assets\\config.txt";
				++tries;
				create();
				return;
			}
			else {
				System.err.println("Error reading " + fileImageConfig); 
				System.exit(1);
			}
		}
		ArrayList<String> fileLines = new ArrayList<String>(); 
		for (String data : fileReader.getData()) {
			if (!(data.charAt(0) == '#')) fileLines.add(data);
		}
		
		for (String fileLine : fileLines) {
			String[] fileLineParts = fileLine.split("\t");
			
			//	TextureRegion
			Texture tex = new Texture(fileLineParts[0]);
			TextureRegion texR = new TextureRegion(tex);
			
			//	Coordinates x,y
			int imgX = Integer.parseInt(fileLineParts[1]);
			int imgY = Integer.parseInt(fileLineParts[2]);
			
			//	Origin in x,y and angle
			int imgOrgX = 0;
			int imgOrgY = 0;
			int imgAngle = 0;
			
			//	Width and Height
			int imgWidth = tex.getWidth();
			int imgHeight = tex.getHeight();
			
			//	minZoom
			float imgMinZoom = Float.parseFloat(fileLineParts[3]);
			
			//	Scale x,y
			float scaleX = Float.parseFloat(fileLineParts[4]);
			float scaleY = Float.parseFloat(fileLineParts[5]);
			
			//	Add image
			images.add(new Image(
					texR, imgX, imgY, imgWidth, imgHeight, 
					imgOrgX, imgOrgY, imgAngle, imgMinZoom,
					scaleX, scaleY
					));
			
		}
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
		updateCam();
		batch.begin();
		
		for (Image i : images) {
			if (cam.zoom <= i.getMinZoom() || i.getMinZoom() == 0.0f) {
				batch.draw(
						i.getTexture(), i.getX(), i.getY(), i.getOrgX(), i.getOrgY(), 
						i.getWidth(), i.getHeight(), i.getScaleX(), i.getScaleY(), i.getAngle()
						);
			}

		}
		
		//batch.draw(img, 0, 0);
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
		
		//	Check Shift, multiply zoomAmount if needed.
		float zoomAmount = .01f;
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) zoomAmount *= 10f;
		
		//	Check 'R', zoom in if needed.
		if(Gdx.input.isKeyPressed(Keys.R)) cam.zoom -= zoomAmount;
		
		//	Check 'F', zoom out if needed.
		if(Gdx.input.isKeyPressed(Keys.F)) cam.zoom += zoomAmount;
		
		//
		//	Rotate (QE)
		//
		
		//	Check Shift, multiply rotateAmount if needed.
		float rotateAmount = .1f;
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) rotateAmount *= 10f;
		
		//	Check 'Q', rotate counter-clockwise if needed.
		if (Gdx.input.isKeyPressed(Keys.Q)) cam.rotate(-1*rotateAmount);
		
		//	Check 'E' rotate clockwise if needed.
		if (Gdx.input.isKeyPressed(Keys.E)) cam.rotate(rotateAmount);
		
		//
		//	Process Movement (WASD, Shift + WASD)
		//
		
		//	Check Shift, multiply movementAmount if needed.
		float movementAmount = 1f;;
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) movementAmount *= 10f;		
		
		//	Check 'W', translate up if needed.
		if(Gdx.input.isKeyPressed(Keys.W)) cam.translate(0, movementAmount);
				
		//	Check 'A', translate left if needed.
		if(Gdx.input.isKeyPressed(Keys.A)) cam.translate(-1*movementAmount, 0);
				
		//	Check 'S', translate down if needed.
		if(Gdx.input.isKeyPressed(Keys.S)) cam.translate(0, -1*movementAmount);
				
		//	Check 'D', translate right if needed.
		if(Gdx.input.isKeyPressed(Keys.D)) cam.translate(movementAmount, 0);
	
	}
	
	private void updateCam() {
		cam.update();
		batch.setProjectionMatrix(cam.combined);
	}
	
}
