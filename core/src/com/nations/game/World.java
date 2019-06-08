package com.nations.game;

import com.nations.game.util.OpenSimplexNoise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;

public class World {
    private List<ArrayList<Terrain>> mTiles = new ArrayList<ArrayList<Terrain> >();
    private Terrain mGrassTerrain;
    private Terrain mGrassForestTerrain;
    private Terrain mGrassHillsTerrain;
    private Terrain mGrassForestHillsTerrain;
    private Terrain mGrassRiverTerrain;
    private Terrain mGrassMountainTerrain;
    private Terrain mGrassLakeTerrain;

    private Terrain mDesertTerrain;
    private Terrain mDesertHillsTerrain;
    private Terrain mDesertOasisTerrain;
    private Terrain mDesertRiverTerrain;
    private Terrain mDesertMountainTerrain;

    private Terrain mSwampTerrain;
    private Terrain mSwampForestTerrain;
    private Terrain mSwampHillsTerrain;
    private Terrain mSwampRiverTerrain;
    private Terrain mSwampLakeTerrain;

    private Terrain mSnowTerrain;
    private Terrain mSnowForestTerrain; //Tundra
    private Terrain mSnowHillsTerrain;
    private Terrain mSnowForestHillsTerrain;

    private Terrain mFoodTerrain;
    private Terrain mSeaTerrain;
    private Terrain mOceanTerrain;

    private int mWidth;
    private int mHeight;

    public enum Direction {
        NORTH(0), NORTH_EAST(60), SOUTH_EAST(120), SOUTH(180),
        SOUTH_WEST(240), NORTH_WEST(300);
        public final int value;
        private static Map map = new HashMap<Integer,Direction>();
        static {
            for (Direction direction : Direction.values()) {
                map.put(direction.value, direction);
            }
        }
        private Direction(int value) {
            this.value = value;
        }
        public static Direction getEnum(int val) {
            return (Direction) map.get(val);
        }
        public Direction getNearestEnum() {
            return getEnum(getNearest());
        }
        public int getValue() {
            return value;
        }
        public int getNearest() {
            return new Random().nextInt(2) == 0 ?
                    (value == 0 ? 300 : value-60) : (value == 300 ? 0 : value+60);
        }
    }

    //Generate world
    public World() {
        initTiles();
        generateTerrain();
        generateRivers2();
    }
    private void initTiles() {
        mGrassTerrain = new Terrain(
                Terrain.TYPES.GRASS, 1,false, false,
                false, false, false);
        mGrassHillsTerrain = new Terrain(
                Terrain.TYPES.GRASS_HILLS, 2, false, false,
                false, true, false);
        mGrassMountainTerrain = new Terrain(
                Terrain.TYPES.GRASS_MOUNTAINS, -1, false, true,
                false, false, false);
        mGrassForestTerrain = new Terrain(
                Terrain.TYPES.GRASS_FOREST, 2, false, false,
                true, false, false);
        mGrassForestHillsTerrain = new Terrain(
                Terrain.TYPES.GRASS_FOREST_HILLS,3, false, false,
                true, true, false);
        mSeaTerrain = new Terrain(
                Terrain.TYPES.SEA, 10, true, false,
                false, false, false);

        mWidth = 50;
        mHeight = 50;
    }
    private void generateTerrain() {
        OpenSimplexNoise noise = new OpenSimplexNoise(new Random().nextInt(100));
        for(int y = 0; y < mHeight; ++y) {
            mTiles.add(new ArrayList<Terrain>());
            for(int x = 0; x < mWidth; ++x) {
                double elevation = noise.eval(((double) (x))/32, ((double) (y))/32);
                double features = noise.eval(((double) (x))/4, ((double) (y))/4);
                Terrain terrain;
                if(features > 0.7f)
                    terrain = mGrassMountainTerrain.clone();
                else if(features > 0.5f)
                    terrain = mGrassHillsTerrain.clone();
                else if(features > 0.3f)
                    terrain = mGrassForestHillsTerrain.clone();
                else if(features > -0.2f)
                    terrain = mGrassForestTerrain.clone();
                else
                    terrain = mGrassTerrain.clone();

                if(elevation < -0.2f)
                    terrain = mSeaTerrain.clone();
                terrain.setElevation(elevation);
                terrain.setX(x);
                terrain.setY(y);
                mTiles.get(y).add(terrain);
            }
        }
    }
    private void generateRivers() {
        for(int y = 0; y < mHeight; ++y) {
            for(int x = 0; x < mWidth; ++x) {
                int maxLength = 10, curLength = 0;
                if(getTile(x, y).getElevation() < -0.2f) {
                    Queue<Terrain> bfs = new LinkedList<Terrain>();
                    bfs.add(getTile(y,x));
                    while(!bfs.isEmpty()) {
                        Terrain terrain = bfs.poll();
                        if(!terrain.isWater())
                            terrain.setHasRiver(true);
                        curLength++;
                        double minElevation = Integer.MAX_VALUE;
                        Direction minDirection = null;
                        for(Direction direction : Direction.values()) {
                            Terrain cornerTile = getTile(terrain.getX(), terrain.getY(), direction);
                            if(cornerTile == null)
                                continue;
                            double tileElevation = cornerTile.getElevation();
                            if(tileElevation > minElevation) {
                                minElevation = tileElevation;
                                minDirection = direction;
                            }
                        }
                        if(minDirection == null)
                            break;
                        if(curLength < maxLength)
                            bfs.add(getTile(y,x, minDirection));
                    }
                }
            }
        }
    }
    private void generateRivers2() {
        Random rand = new Random();
        //Ten rivers in each map
        for(int i = 0; i < 10; ++i) {
            int start_x = rand.nextInt(mWidth),
                    start_y = rand.nextInt(mHeight);
            while(getTile(start_y, start_x).isWater()) {
                start_x = new Random().nextInt(mWidth);
                start_y = new Random().nextInt(mHeight);
            }
            Direction direction = Direction.NORTH_EAST;
            int max_length = 20, cur_length = 0;
            int current_x = start_x, current_y = start_y;
            Terrain terrain = getTile(current_y, current_x);
            terrain.setHasRiver(true);
            while(cur_length != max_length) {
                Direction new_direction = direction.getNearestEnum();
                if(new_direction .getValue() == Direction.NORTH.getValue())
                    new_direction = Direction.NORTH_EAST;
                terrain = getTile(current_y, current_x, new_direction);
                if(terrain == null)
                    break;
                current_x = terrain.getX();
                current_y = terrain.getY();
                terrain.setHasRiver(true);
                if(terrain.isWater())
                    break;
                cur_length++;
            }
        }
        for(int y = 0; y < mHeight-1; ++y) {
            for(int x = 0; x < mWidth; ++x) {
                if(getTile(y,x).isHasRiver() && getTile(y, x, Direction.SOUTH).isHasRiver()) {
                    if(getTile(y,x,Direction.SOUTH_WEST) != null
                            && getTile(y,x,Direction.SOUTH_EAST) != null
                            && !getTile(y,x, Direction.SOUTH_WEST).isHasRiver()
                            && !getTile(y, x, Direction.SOUTH_EAST).isHasRiver())
                        getTile(y, x, Direction.SOUTH_WEST).setHasRiver(true);
                    else if(getTile(y,x,Direction.SOUTH_EAST) != null
                            && !getTile(y, x, Direction.SOUTH_EAST).isHasRiver())
                        getTile(y, x, Direction.SOUTH_EAST).setHasRiver(true);
                    else if(getTile(y,x,Direction.SOUTH_WEST) != null
                            && !getTile(y, x, Direction.SOUTH_WEST).isHasRiver())
                        getTile(y, x, Direction.SOUTH_WEST).setHasRiver(true);
                }
            }
        }
    }
    public Terrain findClosestTile(Terrain.TYPES type) {
        return null;
    }
    public Terrain getTile(int y, int x) {
        return mTiles.get(y).get(x);
    }
    public int getWidth() {
        return mWidth;
    }
    public int getHeight() {
        return mHeight;
    }
    public Terrain getTile(int i, int j, Direction direction) {
        switch(direction) {
            case NORTH:
                if(i != 0)
                    return getTile(i-1, j);
                else
                    return null;
            case NORTH_EAST:
                if(i+j%2 != 0 && j != getWidth()-1)
                    return getTile(i-1+j%2, j+1);
                else
                    return null;
            case SOUTH_EAST:
                if(i+j%2 < getHeight() && j != getWidth()-1)
                    return getTile(i+j%2, j+1);
                else
                    return null;
            case SOUTH:
                if(i != getHeight()-1)
                    return getTile(i+1, j);
                else
                    return null;
            case SOUTH_WEST:
                if(j != 0 && i+j%2 < getHeight())
                    return getTile(i+j%2, j-1);
                else
                    return null;
            case NORTH_WEST:
                if(i+j%2 != 0 && j != 0)
                    return getTile(i-1+j%2, j-1);
                else
                    return null;
        }
        return null;
    }
}
