package com.tds.rtt;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.collision.Ray;

/**
 * Created by theal on 2/29/2016.
 */
public abstract class Traceable {
    public HitInfo getIntersection(Ray r){ return null; };
    //TODO: add more info here in the future
}
