package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

public class MyInput implements InputProcessor {
    static TestGame field;


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

       switch (pointer){
           case 0:
               field.touchDown(screenX,screenY,true);
               break;
           case 1:
               field.allBalls.addBall();
               break;
           case 2:
               field.turn++;
               break;
           case 3:
               field.allShapes.generateNewLine();
               break;
           case 4:
               field.allBalls.moveWithAnimationTo(Gdx.input.getX(pointer),Gdx.graphics.getHeight()-Gdx.input.getY(pointer));
               break;
       }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        field.touchUp(screenX,screenY);

        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        field.can=true;
        field.touchDown(screenX,screenY,false);
        return true;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode){
            case 0:
                break;
            case 45:
                field.allBalls.addBall();
                break;
            case 51:
                field.turn++;
                break;
            case 33:
                field.allShapes.generateNewLine();
                break;
            case 46:
                field.allBalls.moveWithAnimationTo(Gdx.input.getX(),Gdx.graphics.getHeight()-Gdx.input.getY());
                break;
            case 32:
                AllBalls.startX = field.allBalls.getArrayBalls().get(0).getX()+30;
                field.allBalls.setX(AllBalls.startX);
                break;
            case 29:
                AllBalls.startX =field.allBalls.getArrayBalls().get(0).getX()-30;
                field.allBalls.setX(AllBalls.startX);
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
