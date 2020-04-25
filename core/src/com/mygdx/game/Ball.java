package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;


public class Ball {
    Texture texture;
    float velocityX,velocityY;
    public float maxVelocity = 30, old_x, old_y;
    boolean motion;

    public Sprite spriteBall;
    public Circle ballCircle;
    int diameter = 30;
    static float radius;
    public Ball() {this(0,0);}
    public Ball(float x, float y) {

        radius = diameter/2f;
        texture = new Texture("ball.png");
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        spriteBall = new Sprite(texture);
        spriteBall.setSize(diameter,diameter);
        ballCircle= new Circle(x, y, diameter/2f);
    }

    public Texture getTexture() {
        return texture;
    }

    public void draw(SpriteBatch batch){
        spriteBall.setX(getBottomX());
        spriteBall.setY(getBottomY());
        spriteBall.draw(batch);
    }

    public float getBottomX(){return ballCircle.x-ballCircle.radius;}//Только для рисования
    public float getBottomY(){return ballCircle.y-ballCircle.radius;}//Только для рисования
    public float getX(){return ballCircle.x;}
    public float getY(){return ballCircle.y;}
    public void setX(float x){ballCircle.x=x;}
    public void setY(float y){ballCircle.y=y;}
    public void reflectionWall(){ velocityX*=-1;}
    public void reflectionCeiling(){velocityY*=-1;}

    public boolean inMotion(){
        return motion;
    }

    public void move(){        //System.out.println("x:"+velocityX+" y:"+velocityY);

        old_x=getX();
        old_y=getY();
        ballCircle.x+=velocityX;
        ballCircle.y+=velocityY;
        if (getBottomX()+ diameter >= Gdx.graphics.getWidth()|| getBottomX()<=0){
            reflectionWall();
            setX(getX()<=Gdx.graphics.getWidth()/2f? diameter /2f:Gdx.graphics.getWidth()- diameter /2f);
        }

        if ( getBottomY()+ diameter >= TestGame.topY){
            reflectionCeiling();
            setY(TestGame.topY- diameter /2f);
        }
        if (getBottomY()<=TestGame.bottomY) {
            AllBalls.startX = getX();
            setY(AllBalls.startY = TestGame.bottomY+radius);
            motion = false;
        }


    }
    public void testDraw(SpriteBatch batch){
        spriteBall.setX(getBottomX());
        spriteBall.setY(getBottomY());
        spriteBall.draw(batch);
    }

    public void checkCollision(Rectangle rectangle){
        if (Intersector.overlaps(ballCircle, rectangle)){
            spriteBall.setColor(Color.RED);
            System.out.println("Collision!!!");
        }else spriteBall.setColor(Color.GREEN);
    }

    public void normalizeVelocity(float x,float y){
        float width = x-ballCircle.x;
        float height = y-ballCircle.y;
        float index =(float)(maxVelocity/Math.hypot(width,height));
        velocityX = width*index;
        velocityY = height*index;
    }

    public void launch(float x,float y){
        normalizeVelocity(x, y);
        motion=true;
        //Log.d("myteg","x:"+velocityX+" y:"+velocityY);

    }
    public void moveTo(float x, float y) {
        if (getX() != x || getY()!= y) {
            if (Math.hypot(getX()-x,getY()-y) < Math.hypot(maxVelocity,maxVelocity)) {
                setX(x);
                setY(y);
            } else {
                normalizeVelocity(x, y);
                ballCircle.x += velocityX;
                ballCircle.y += velocityY;
            }
        }

    }

    public void dispose () {
        texture.dispose();

    }
}
