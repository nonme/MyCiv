package com.nations.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.nations.game.util.PointF;
import com.nations.game.util.ScrollInputProcessor;

public class TheNationsGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private OrthographicCamera mCamera;

	private World mWorld;
	private AssetsManager mAssetsManager;
	private ExtendViewport mViewport;
	private BitmapFont mFont;
	private static final float GAME_WIDTH = 600;
	private static final float GAME_HEIGHT = 360;
	private boolean mIsMousePressed;
	private float mCameraZoom;
	private PointF mCameraOrigin;
	private ScrollInputProcessor mScrollInputProcessor;
	@Override
	public void create () {
		batch = new SpriteBatch();
		mViewport = new ExtendViewport(GAME_WIDTH, GAME_HEIGHT);
		mWorld = new World();
		mAssetsManager = AssetsManager.get();
		mFont = new BitmapFont();
		mFont.getData().setScale(0.4f);
		mIsMousePressed = false;
		mCameraZoom = 1;
		mCameraOrigin = new PointF(0,0);
		mCamera = (OrthographicCamera) mViewport.getCamera();
		mScrollInputProcessor = new ScrollInputProcessor(mCamera);
		Gdx.input.setInputProcessor(mScrollInputProcessor);
	}

	@Override
	public void resize(int width, int height) {
		mViewport.update(width, height, false);

		mCamera.position.x = GAME_WIDTH/2;
		mCamera.position.y = GAME_HEIGHT/2;
		mCamera.update();
	}
	@Override
	public void render () {
		mCamera.update();
		//handleInput();
		batch.setProjectionMatrix(mCamera.combined);

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		drawFirstLayer();
		drawSecondLayer();
		batch.end();
	}
	public void handleInput() {
		if(!mIsMousePressed && Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
			mCameraOrigin.x = mCamera.position.x;
			mCameraOrigin.y = mCamera.position.y;
			mIsMousePressed = true;
		}
		else if(mIsMousePressed && Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
			double x = Gdx.input.getX();
			double y = Gdx.input.getY();
			//mCamera.position.x = mCameraOrigin.x + (float) (mOriginX-x);
			//mCamera.position.y = mCameraOrigin.y + (float) (y-mOriginY);
		}
		else mIsMousePressed = false;
		if(Gdx.input.isButtonPressed(Input.Buttons.FORWARD))
			mCamera.zoom+=0.02;
		if(Gdx.input.isButtonPressed(Input.Buttons.BACK))
			mCamera.zoom-=0.02;

		mCamera.update();
	}
	private void drawFirstLayer() {
		for(int i = 0; i < mWorld.getHeight(); ++i) {
			for(int j = 0; j < mWorld.getWidth(); j+=2) {
				float x = j/2*24+(j+1)/2*23;
				float y = 480-(j%2 == 0 ?
						i*mAssetsManager.getTileHeight() :
						i*mAssetsManager.getTileHeight()+mAssetsManager.getTileHeight()/2);

				Sprite sprite = mAssetsManager.getSprite(mWorld.getTile(i, j));
				sprite.setX(x);
				sprite.setY(y);
				sprite.draw(batch);
			}
			for(int j = 1; j < mWorld.getWidth(); j+=2) {
				float x = j/2*24+(j+1)/2*23;
				float y = 480-(j%2 == 0 ?
						i*mAssetsManager.getTileHeight() :
						i*mAssetsManager.getTileHeight()+mAssetsManager.getTileHeight()/2);

				Sprite sprite = mAssetsManager.getSprite(mWorld.getTile(i, j));
				sprite.setX(x);
				sprite.setY(y);
				sprite.draw(batch);
			}
		}
	}
	private void drawSecondLayer() {
		for(int i = 0; i < mWorld.getHeight(); ++i) {
			for(int j = 0; j < mWorld.getWidth(); ++j) {
				if(!mWorld.getTile(i,j).isHasRiver())
					continue;
				float x = j/2*24+(j+1)/2*23;
				float y = 480-(j%2 == 0 ?
						i*mAssetsManager.getTileHeight() :
						i*mAssetsManager.getTileHeight()+mAssetsManager.getTileHeight()/2);

				String spriteType = "";
				if(mWorld.getTile(i,j,World.Direction.NORTH) != null &&
						mWorld.getTile(i,j,World.Direction.NORTH).isHasRiver())
					spriteType+="0"; //TODO
				else
					spriteType+="0";
				if(mWorld.getTile(i,j,World.Direction.NORTH_EAST) != null &&
						mWorld.getTile(i,j,World.Direction.NORTH_EAST).isHasRiver())
					spriteType+="1";
				else
					spriteType+="0";
				if(mWorld.getTile(i,j,World.Direction.SOUTH_EAST) != null &&
						mWorld.getTile(i,j,World.Direction.SOUTH_EAST).isHasRiver())
					spriteType+="1";
				else
					spriteType+="0";
				if(mWorld.getTile(i,j,World.Direction.SOUTH) != null &&
						mWorld.getTile(i,j,World.Direction.SOUTH).isHasRiver())
					spriteType+="0"; //TODO
				else
					spriteType+="0";
				if(mWorld.getTile(i,j,World.Direction.SOUTH_WEST) != null &&
						mWorld.getTile(i,j,World.Direction.SOUTH_WEST).isHasRiver())
					spriteType+="1";
				else
					spriteType+="0";
				if(mWorld.getTile(i,j,World.Direction.NORTH_WEST) != null &&
						mWorld.getTile(i,j,World.Direction.NORTH_WEST).isHasRiver())
					spriteType+="1";
				else
					spriteType+="0";
				Sprite sprite = mAssetsManager.getRiver(spriteType);
				if(sprite == null)
					continue;
				sprite.setX(x);
				sprite.setY(y);
				sprite.draw(batch);
				mFont.draw(batch, spriteType, x+5, y+5);
			}

		}
	}
	@Override
	public void dispose () {
		batch.dispose();
		mAssetsManager.dispose();
	}
}
