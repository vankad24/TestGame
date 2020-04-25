package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;


public class AllBalls {
    public static float radius,startX, startY;
    Array<Ball> balls;
    public AllBalls() {
        balls = new Array<>();
        radius = 15;
        startX =  Gdx.graphics.getWidth()/2f;
        startY = TestGame.bottomY+radius;
    }


    public Array<Ball> getArrayBalls() {
        return balls;
    }
    public float getRadius(){
        return radius;
    }
    public void moveAll(){
        for (Ball b: balls){
            if (b.inMotion()) b.move();
        }
    }

    public void createBall(float x,float y){ balls.add(new Ball(x,y));}
    public void addBall(){
        System.out.println(startY);
        balls.add(new Ball(startX,startY));}
    public void setX(float x) {
        for (Ball b : balls) {
            b.setX(x);
        }
    }

    public void setY(float y){
        for (Ball b: balls){
            b.setY(y);
        }
    }

    public boolean inMotion(){
        for (Ball b: balls){
            if (b.inMotion())return true;
        }
        return false;
    }
    public void moveWithAnimationTo(float x,float y){
        for (Ball b: balls){
            b.moveTo(x, y);
        }
    }
    public boolean allIn(float x,float y){
        for (Ball b: balls){
            if (x!=b.getX()||y!=b.getY()){
                //System.out.println("x,y"+x+" "+y+" b "+b.getX()+" "+b.getY());
                return false;
            }
        }
        return true;
    }
    public void drawBalls(SpriteBatch batch){

        float lastX = -1, lastY = -1;
        for (Ball ball : balls) {
            if (lastX != ball.getBottomX() || lastY != ball.getBottomY()) {
                lastX = ball.getBottomX();
                lastY = ball.getBottomY();
                ball.spriteBall.setY(ball.getBottomY());
                ball.spriteBall.setX(ball.getBottomX());
                ball.spriteBall.draw(batch);
//                batch.draw(b.spriteBall,lastX, lastY);
            }
        }
    }
    public void dispose(){
        for (Ball ball : balls) {
            ball.dispose();
        }
    }
}
