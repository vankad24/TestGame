package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;


import java.util.Random;

public class AllShapes {
    public static TextureAtlas textureAtlas;
    TextureManager manager;
    static BitmapFont font;
    static Shape[][] shapes;
    int[] chances={0,40,0,0};
    public static float width;
    float space, margin;
    int shapesInWidth = 1, shapesInHeight = 5;

    public AllShapes(){
        manager = new TextureManager();
        textureAtlas = manager.getManager().get("myAtlas.atlas");
        font = new BitmapFont(Gdx.files.internal("myFont.fnt"));
        font.setColor(Color.WHITE);
        margin = Ball.radius*3;
        space = Ball.radius;
        width = 90;//(Gdx.graphics.getWidth() - margin*2 - space*shapesInWidth)/ shapesInWidth;

        shapes = new Shape[shapesInHeight][shapesInWidth];
    }

    static void delete(Shape shape){
        for (int i = 0; i < shapes.length; i++) {
            for (int j = 0; j < shapes[0].length; j++) {
                if (shape == shapes[i][j]){
                    shapes[i][j]=null;
                    return;
                }
            }
        }
    }

    private int dropWithChance(int[] arrChances){
        Random rand = new Random();
        int chance =0,sum=0;
        for (int x:arrChances)sum+=x;
        int num = rand.nextInt(sum);
        for (int i = 0; i < arrChances.length-1; i++) {
            chance += arrChances[i];
            if (num<chance)return i;
        }
        return arrChances.length-1;
    }

    public void generateNewLine(){
        for (int i = shapes.length-1; i >0 ; i--)
            for (int j = 0; j < shapes[0].length; j++) {
                shapes[i][j] = shapes[i - 1][j];
                if (shapes[i][j]!=null)shapes[i][j].addY(-width-space);
            }
        boolean isAir=true;
        while (isAir){
            for (int i = 0; i <shapes[0].length ; i++) {
                int drop = dropWithChance(chances);
                if (drop!=0)isAir=false;
                switch (drop){
                    case 0:
                        shapes[0][i]=null;
                        break;
                    case 1:
                        shapes[0][i]=new Square(margin+(width+space)*i,TestGame.topY-margin-width,6);
                        break;
                    case 2:
                        break;
                }
            }
        }
    }

    public void draw(SpriteBatch batch){
        for (Shape[] s: shapes){
            for (Shape shape: s){
                if (shape!=null)shape.drawShape(batch);
            }
        }
    }

    public void checkCollision(Ball ball) {
        if (ball.inMotion()) {
            for (Shape[] s : shapes) {
                for (Shape shape : s) {
                    if (shape != null && !shape.destroying) shape.checkCollision(ball);
                }
            }
        }
    }
}
