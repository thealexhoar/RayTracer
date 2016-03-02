package com.tds.rtt;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.math.collision.Sphere;

/**
 * Created by theal on 2/28/2016.
 */
public class Tracer {
    Traceable[] _objects;
    Vector3[] _lights;

    final int MAX_DEPTH = 2;
    final float AMBIENT_COEFFICIENT = 0.2f;
    final float DIFFUSE_COEFFICIENT = 1 - AMBIENT_COEFFICIENT;

    public Tracer(){
        _objects = new Traceable[]{
                new GraphicSphere(new Sphere(new Vector3(3,0,10), 1), Color.GREEN, 0.3f, 0),
                new GraphicSphere(new Sphere(new Vector3(-3,0,10), 1), Color.RED, 0.3f, 0),
                new GraphicSphere(new Sphere(new Vector3(0,3,10), 1), Color.BLUE, 0.3f, 0),
                new GraphicSphere(new Sphere(new Vector3(0,-3,10), 1), Color.YELLOW, 0.3f, 0),
                new GraphicSphere(new Sphere(new Vector3(0,0,11), 1), Color.WHITE, 0.9f, 0),

        };
        _lights = new Vector3[] {
                //new Vector3(8, 1,0),
                //new Vector3(0,20,7f),
                new Vector3(4,8,2f),
        };
    }

    public Color trace(Ray ray){
        return trace(ray, 0);
    }

    public Color trace(Ray ray, int depth){
        depth = Math.min(depth, MAX_DEPTH);
        HitInfo info = firstIntersectionInfo(ray, _objects, 0.01f);
        if(info.color == null) {
            return getAmbient(ray);
        }
        Color reflectedColor = Color.BLACK;
        Color refractedColor = Color.BLACK;
        if(info.reflection != null && info.reflectivity != 0 && depth > 0){
            reflectedColor = trace(info.reflection, depth-1);
        }
        if(info.refraction != null && info.refractivity != 0 && depth > 0){
            refractedColor = trace(info.refraction, depth-1);
        }

        Color out = blendColors(info.color, reflectedColor, info.reflectivity, refractedColor, info.refractivity);
        info.color = out;
        return getShaded(info, _objects, _lights);

    }

    public Color getShaded(HitInfo info, Traceable[] traceables, Vector3[] lights){
        float finalShade = AMBIENT_COEFFICIENT;
        for(Vector3 light : lights){
            Ray delta = new Ray(info.normal.origin.cpy(), light.cpy().sub(info.normal.origin));
            HitInfo blockInfo = firstIntersectionInfo(delta,traceables,0.01f);
            float shade;
            if(blockInfo.color != null){
                shade = 0;
            }
            else {
                shade = info.normal.direction.dot(delta.direction);
                shade = Math.max(0,shade);
            }
            finalShade = finalShade + shade;
        }
        finalShade = Math.min(finalShade, 1);

        Color outColor = new Color(
            info.color.r * finalShade,
            info.color.g * finalShade,
            info.color.b * finalShade,
            info.color.a
        );
        return outColor;
    }

    public HitInfo firstIntersectionInfo(Ray ray, Traceable[] traceables){
        return this.firstIntersectionInfo(ray,traceables,0);
    }

    public HitInfo firstIntersectionInfo(Ray ray, Traceable[] traceables, float minSeparation){
        float minDistance  = Float.MAX_VALUE;
        HitInfo outInfo = new HitInfo();
        for(Traceable traceable : traceables){
            HitInfo info = null;
            info = traceable.getIntersection(ray);
            if(info == null) {
                //no collision
                continue;
            }
            float distance = info.normal.origin.dst(ray.origin);
            if(distance < minDistance && minDistance > minSeparation){
                outInfo = info;
                minDistance = distance;
            }
        }

        return outInfo;
    }


    private Color getAmbient(Ray ray){
        return Color.SKY;
    }

    private Color blendColors(Color base, Color reflect, float reflectivity, Color refract, float refractivity){
        float rSum = base.r * Float.max(1 - reflectivity - refractivity, 0) + reflect.r * reflectivity + refract.r * refractivity;
        float gSum = base.g * Float.max(1 - reflectivity - refractivity, 0) + reflect.g * reflectivity + refract.g * refractivity;
        float bSum = base.b * Float.max(1 - reflectivity - refractivity, 0) + reflect.b * reflectivity + refract.b * refractivity;
        float rAvg = rSum / (Float.max(reflectivity + refractivity, 1));
        float gAvg = gSum / (Float.max(reflectivity + refractivity, 1));
        float bAvg = bSum / (Float.max(reflectivity + refractivity, 1));
        return new Color(rAvg, gAvg, bAvg, base.a);
    }
}

class HitInfo{
    public Ray normal;
    public Color color;
    public Ray reflection;
    public float reflectivity = 0;
    public Ray refraction;
    public float refractivity = 0;
}
