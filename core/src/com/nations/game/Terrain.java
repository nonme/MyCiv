package com.nations.game;

/**
 * This is class is an implementation of the Flyweight programming pattern.
 */
public class Terrain {
    private TYPES mType;
    private int mMovementCost;
    private boolean mHasForest;
    private boolean mHasHills;
    private boolean mIsWater;
    private boolean mIsMountain;
    private boolean mHasRiver;
    private double mElevation;
    private int mX, mY;
    public Terrain(TYPES type, int movementCost, boolean isWater, boolean isMountain,
                   boolean hasForest, boolean hasHills, boolean hasRiver) {
        mType = type;
        mMovementCost = movementCost;
        mIsWater = isWater;
        mIsMountain = isMountain;
        mHasForest = hasForest;
        mHasHills = hasHills;
        mHasRiver = hasRiver;
    }
    public Terrain clone() {
        return new Terrain(mType, mMovementCost, mIsWater, mIsMountain, mHasForest, mHasHills,mHasRiver);
    }

    public int getMovementCost() {
        return mMovementCost;
    }

    public boolean isWater() {
        return mIsWater;
    }

    public boolean isMountain() {
        return mIsMountain;
    }

    public boolean isHasForest() {
        return mHasForest;
    }

    public boolean isHasHills() {
        return mHasHills;
    }

    public boolean isHasRiver() {
        return mHasRiver;
    }

    public void setHasRiver(boolean hasRiver) {
        mHasRiver = hasRiver;
    }
    public TYPES getType() {
        return mType;
    }

    public void setType(TYPES type) {
        mType = type;
    }

    public double getElevation() {
        return mElevation;
    }

    public void setElevation(double elevation) {
        mElevation = elevation;
    }

    public int getX() {
        return mX;
    }

    public void setX(int x) {
        this.mX = x;
    }

    public int getY() {
        return mY;
    }

    public void setY(int y) {
        this.mY = y;
    }

    public enum TYPES {
        GRASS, GRASS_HILLS, GRASS_FOREST, GRASS_FOREST_HILLS, GRASS_MOUNTAINS,
        DESERT, DESERT_HILLS, DESERT_MOUNTAINS, SEA, OCEAN
    };
}
