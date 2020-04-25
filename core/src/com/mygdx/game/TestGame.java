package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2D;


public class TestGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img, ball2;
	AndroidHelper helper;
	public static float bottomY, topY;


	public static int turn=1;
	int k=0;
	long lastTime;
	float aimX,aimY, limitAimY;

	public boolean can;
	boolean canLaunch, launching, finish=true, isBottomTouch;
	TextureRegion aimTexture, gameBarTexture;
	AllBalls allBalls;
	Ball ball;
	BitmapFont font;
	Rectangle rectangle, bounds, bottomBar, topBar;
	Vector3 touchPos;
	OrthographicCamera camera;
	AllShapes allShapes;

	public TestGame(AndroidHelper helper) {
		this.helper = helper;
	}

	@Override
	public void create () {
		bottomY = Gdx.graphics.getHeight()/8f;
		topY = Gdx.graphics.getHeight()*.93f;
		camera = new OrthographicCamera();
		camera.setToOrtho(false);
		touchPos = new Vector3();
		ball = new Ball();
		batch = new SpriteBatch();
		ball2 = new Texture("oldball.png");
		allBalls =new AllBalls();
		allBalls.createBall(AllBalls.startX,AllBalls.startY);
		MyInput.field = this;
		Gdx.input.setInputProcessor(new MyInput());


		font = new BitmapFont(Gdx.files.internal("myFont.fnt"));
		limitAimY =  Gdx.graphics.getHeight()*.8f;
		allShapes = new AllShapes();
		allShapes.generateNewLine();
		aimTexture = AllShapes.textureAtlas.findRegion("ball");

		//bounds = new Rectangle();
		img = new Texture("badlogic.jpg");
		rectangle = new Rectangle(100,200, 300,300);

		gameBarTexture = AllShapes.textureAtlas.findRegion("game_bar");
		bottomBar = new Rectangle(0,0,Gdx.graphics.getWidth(),bottomY);
		topBar = new Rectangle(0, topY, Gdx.graphics.getWidth(),Gdx.graphics.getHeight()-topY);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(.17f, .17f, .17f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		for (Ball b:allBalls.getArrayBalls())allShapes.checkCollision(b);

		batch.begin();

		batch.draw(gameBarTexture, bottomBar.x, bottomBar.y, bottomBar.width, bottomBar.height);
		batch.draw(gameBarTexture,topBar.x,topBar.y,topBar.width,topBar.height);
		//batch.draw(img, rectangle.x, rectangle.y,rectangle.width,rectangle.height);
		font.draw(batch,"Шариков: "+allBalls.getArrayBalls().size,Gdx.graphics.getWidth()/8f,Gdx.graphics.getHeight()*.97f);
		font.draw(batch,"Ход: "+turn,Gdx.graphics.getWidth()/2f,Gdx.graphics.getHeight()*.97f);

		allBalls.drawBalls(batch);
		allShapes.draw(batch);

		if (canLaunch)drawAim();
		batch.end();

		if (launching)launching();
		if (allBalls.inMotion())allBalls.moveAll();

		else if (!finish ){
			//System.out.println("check");
			if (!allBalls.allIn(AllBalls.startX,AllBalls.startY))allBalls.moveWithAnimationTo(AllBalls.startX,AllBalls.startY);
			else nextTurn();
		}

		/*if (Gdx.input.isTouched()){
			touchPos.set(Gdx.input.getX(),Gdx.input.getY(),0);
			camera.unproject(touchPos);
			ball.setX(touchPos.x);
			ball.setY(touchPos.y);
			ball.checkCollision(rectangle);
		}*/

	}

	void topAim(float x, float y){

//		ball.moveTo(touchPos.x,touchPos.y);
//		ball.setY(touchPos.y);
//		System.out.println("x:"+touchPos.x+" y:"+touchPos.y);
		if (finish && y < limitAimY &&  Math.toDegrees(Math.atan((y - AllBalls.startY) / Math.abs((AllBalls.startX - x))))> 10) {
			aimX = x;
			aimY = y;
			canLaunch=true;
		} else {
			canLaunch = false;
		}
	}

	void bottomAim(float x, float y) {
		float factor = (limitAimY - AllBalls.startY) / AllBalls.startY;
		float cathetusX = AllBalls.startX - x;
		float cathetusY = AllBalls.startY - y;
		topAim(AllBalls.startX + cathetusX * factor, AllBalls.startY + cathetusY * factor);
	}

	public void touchUp(float x, float y){
		if (canLaunch) {
			canLaunch = false;
			launching = true;
			finish=false;
		}
	}

	public void touchDown(float x, float y, boolean start){
		touchPos.set(x,y,0);
		camera.unproject(touchPos);
		/*if (start){
			if (touchPos.y<0)isBottomTouch=true;
			else isBottomTouch=false;
		}
		if (isBottomTouch)bottomAim(touchPos.x,touchPos.y);
		else*/ topAim(touchPos.x,touchPos.y);
	}

	void drawAim(){
		int amount = 8;
		float stepX = (AllBalls.startX - aimX)/amount;
		float stepY = (aimY - AllBalls.startY)/amount;
		float stepDiameter = (AllBalls.radius*.4f)/amount;
		for (int i = 1; i <= amount; i++) {
			float diameter = AllBalls.radius*.4f + stepDiameter * i *2;
			batch.draw(aimTexture,AllBalls.startX-stepX*i - diameter/2,AllBalls.startY+stepY*i- diameter/2,diameter,diameter);
		}
	}

	void launching() {
		if (System.currentTimeMillis() - lastTime > 200) {
			lastTime = System.currentTimeMillis();
			allBalls.getArrayBalls().get(k).launch(aimX, aimY);
			k++;
			if (k > allBalls.getArrayBalls().size - 1) {
				launching = false;
				k = 0;
			}
		}
	}

	void nextTurn(){
		finish = true;
		turn++;
		//	allBalls.addBall();
		allShapes.generateNewLine();
	}
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		ball.dispose();
		ball2.dispose();
	}
}
