package com.tds.rtt;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

/**
 * Created by theal on 2/29/2016.
 */
public class CameraManager {


    private PerspectiveCamera _camera;
    private int _width, _height;


    public CameraManager(float xFov, int width, int height){
        float aspectRatio = 1f * width/height;
        _camera = new PerspectiveCamera(xFov/aspectRatio, width, height);
        _width = width;
        _height = height;
        _camera.lookAt(0,0,1);
        _camera.translate(_camera.position.cpy().scl(-1));
        _camera.update();
    }

    public  CameraManager(){
        this(90,320,180);
    }

    public Ray[][] getScreenRays(){
        Ray[][] screenRays = new Ray[_width][_height];

        Vector3 position, projection, direction;


        float xMul = Gdx.graphics.getBackBufferWidth() / _width;
        float yMul = Gdx.graphics.getBackBufferHeight() / _height;


        for(int i = 0; i < _width; i++){
            for(int j = 0; j < _height; j++){
                float screenX = (i + 0.5f) * xMul;
                float screenY = (j + 0.5f) * yMul;
                position = new Vector3(screenX, screenY, 0);
                projection =_camera.unproject(position);
                direction = projection.cpy().sub(_camera.position);



                screenRays[i][j] = new Ray(projection, direction);
            }
        }

        return screenRays;
    }

    public void doStuff(Vector3 vLook, Vector3 vMove){
        _camera.position.set(vMove);
        _camera.lookAt(vLook);
        _camera.up.set(0,1,0);
        _camera.update();
    }
}
