package com.tds.rtt;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.math.collision.Sphere;

public class Main extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
    Pixmap p;
    Tracer tracer;
    CameraManager cm;
    FPSLogger logger;

    ShapeRenderer shapeRenderer;
    int rScale = 5;
    float angle = 0;
    float omega = 0.01f;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        p = new Pixmap(1600 / rScale, 900 / rScale, Pixmap.Format.RGBA8888);
        tracer = new Tracer();

        cm = new CameraManager(90, 1600/rScale, 900/rScale);
        logger = new FPSLogger();
	}

	@Override
	public void render () {
        logger.log();

        angle += omega;

        cm.doStuff(new Vector3(0,0,10), new Vector3(10 * (float)Math.sin(angle), 4, 10 - 10 * (float)Math.cos(angle)));

		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Ray[][] rays = cm.getScreenRays();
        float ymin, ymax, xmin, xmax;
        ymin = Float.MAX_VALUE;
        ymax = -Float.MAX_VALUE;
        xmin = Float.MAX_VALUE;
        xmax = -Float.MAX_VALUE;

        batch.begin();
        for(int i = 0; i < rays.length; i++){
            for(int j = 0; j < rays[i].length; j++){
                ymin = Math.min(rays[i][j].origin.y, ymin);
                ymax = Math.max(rays[i][j].origin.y, ymax);
                xmin = Math.min(rays[i][j].origin.x, xmin);
                xmax = Math.max(rays[i][j].origin.x, xmax);
                Color c = tracer.trace(rays[i][j], 2);
                p.setColor(c);
                p.drawPixel(i,j);
            }
        }

        img = new Texture(p);
		batch.draw(img,0,0, 1600, 900);

		batch.end();
	}
}
