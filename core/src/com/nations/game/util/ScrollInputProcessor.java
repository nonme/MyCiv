package com.nations.game.util;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

public class ScrollInputProcessor implements InputProcessor {
    private OrthographicCamera mCamera;
    Vector3 mVector = new Vector3();
    boolean mDragging;
    private int mOriginX;
    private int mOriginY;
    private PointF mCameraOrigin;

    public ScrollInputProcessor(OrthographicCamera camera) {
        mCamera = camera;
        mOriginX = 0;
        mOriginY = 0;
        mCameraOrigin = new PointF(0,0);
    }
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override public boolean touchDown (int screenX, int screenY, int pointer, int button) {
        // ignore if its not left mouse button or first touch pointer
        if (button != Input.Buttons.LEFT || pointer > 0) return false;
        mCamera.unproject(mVector.set(screenX, screenY, 0));
        mDragging = true;
        mOriginX = screenX;
        mOriginY = screenY;
        mCameraOrigin.x = mCamera.position.x;
        mCameraOrigin.y = mCamera.position.y;
        return true;
    }

    @Override public boolean touchDragged (int screenX, int screenY, int pointer) {
        if (!mDragging) return false;
        mCamera.unproject(mVector.set(screenX, screenY, 0));

        mCamera.position.x = mCameraOrigin.x + (float) (mOriginX-screenX);
        mCamera.position.y = mCameraOrigin.y + (float) (screenY-mOriginY);

        return true;
    }

    @Override public boolean touchUp (int screenX, int screenY, int pointer, int button) {
        if (button != Input.Buttons.LEFT || pointer > 0) return false;
        mCamera.unproject(mVector.set(screenX, screenY, 0));
        mDragging = false;
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        mCamera.zoom += 0.05f * amount;
        return false;
    }
}
