package com.tds.rtt;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.math.collision.Sphere;

/**
 * Created by theal on 2/29/2016.
 */
public class GraphicSphere extends Traceable {
    public Sphere sphere;
    public Color color;
    public float reflectivity;
    public float refractivity;
    public float refractiveIndex;

    public GraphicSphere(Sphere sphere, Color color){
        this(sphere, color, 0, 0);
    }

    public GraphicSphere(Vector3 pos, float radius, Color color){
        this(new Sphere(pos,radius), color);
    }

    public GraphicSphere(Sphere sphere, Color color, float reflectionIndex, float refractionIndex){
        this.sphere = sphere;
        this.color = color;
        this.reflectivity = reflectionIndex;
        this.refractivity = refractionIndex;
    }


    public HitInfo getIntersection(Ray ray){
        HitInfo hitInfo;
        Vector3 intersection = new Vector3();
        boolean doesIntersect =  Intersector.intersectRaySphere(ray, sphere.center, sphere.radius, intersection);

        if(!doesIntersect){
            return null;
        }
        else {
            Vector3 normal = intersection.cpy().sub(sphere.center);
            hitInfo = new HitInfo();
            hitInfo.normal = new Ray(intersection,normal);
            hitInfo.color = color;

            if(reflectivity > 0){
                float k = -1 * hitInfo.normal.direction.dot(ray.direction);
                Vector3 reflect = ray.direction.cpy().add(hitInfo.normal.direction.cpy().scl(2 * k));
                hitInfo.reflection = new Ray(intersection, reflect);
                hitInfo.reflectivity = reflectivity;
            }
            if(refractivity > 0){
                //TODO: code refraction
            }

            return hitInfo;
        }
    }
}
