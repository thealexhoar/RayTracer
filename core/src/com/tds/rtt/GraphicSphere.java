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
    public float refractiveIndex = 1.5f; //common glass refractivity

    public GraphicSphere(Sphere sphere, Color color){
        this(sphere, color, 0, 0);
    }

    public GraphicSphere(Vector3 pos, float radius, Color color){
        this(new Sphere(pos,radius), color);
    }

    public GraphicSphere(Sphere sphere, Color color, float reflectionIndex, float refractivity){
        this.sphere = sphere;
        this.color = color;
        this.reflectivity = reflectionIndex;
        this.refractivity = refractivity;
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

            float k1 = -1 * hitInfo.normal.direction.dot(ray.direction);
            Vector3 reflect = ray.direction.cpy().add(hitInfo.normal.direction.cpy().scl(2 * k1));
            hitInfo.reflection = new Ray(intersection, reflect);
            hitInfo.reflectivity = reflectivity;


            /*Ray farSideFinder, finalRefraction;
            Vector3 internalRefractionDirection, finalRefractionDirection;

            float n1 = 1/refractiveIndex;
            float n2 = 1/n1;

            float k2 = (float)Math.sqrt(1 - Math.pow(n1,2) * (1 - Math.pow(k1, 2)));

            internalRefractionDirection = ray.direction.cpy().scl(n1).add(normal.cpy().scl(n1 * k1 - k2));

            farSideFinder = new Ray(intersection.cpy().add(internalRefractionDirection.cpy().scl(sphere.radius * 3)), internalRefractionDirection.cpy().scl(-1));
            Vector3 farSide = new Vector3();
            Intersector.intersectRaySphere(farSideFinder, sphere.center, sphere.radius, farSide);

            hitInfo.refraction = finalRefraction;
            hitInfo.refractivity = refractivity;*/
            //TODO: implement refraction
            return hitInfo;
        }
    }
}
