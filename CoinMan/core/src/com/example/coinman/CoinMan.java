package com.example.coinman;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.ScreenUtils;

import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;
import java.util.Random;

public class CoinMan extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] man;
	Random random;

	int manstate, pause = 0;
	float gravity = 0.8f;
	float velocity = 0;
	int manY = 0;
	Rectangle manRectangle;
	int score = 0;
	float objHeight;

	BitmapFont font;

	Texture dizzy;

	//THE GAMESTATE OF THE COINMAN
	int gameState = 1;


	// PROPERTIES FOR COINS IN THE GAME.
	Texture coin;
	int coinCount;
	ArrayList<Integer> coinXs = new ArrayList<Integer>();
	ArrayList<Integer> coinYs = new ArrayList<Integer>();
	ArrayList<Rectangle> coinRectangle = new ArrayList<Rectangle>();



	// PROPERTIES FOR BOMBS IN THE GAME.
	Texture bomb;
	int bombCount;

	ArrayList<Integer> bombXs = new ArrayList<Integer>();
	ArrayList<Integer> bombYs = new ArrayList<Integer>();
	ArrayList<Rectangle> bombRectangle = new ArrayList<Rectangle>();
	int bombplace;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		man = new Texture[4];
		man[0] = new Texture("frame-1.png");
		man[1] = new Texture("frame-2.png");
		man[2] = new Texture("frame-3.png");
		man[3] = new Texture("frame-4.png");
		manY = Gdx.graphics.getHeight()/2;

		coin = new Texture("coin.png");
		bomb = new Texture("bomb.png");

		random = new Random();
		bombplace = random.nextInt(50)+100;

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);

		dizzy = new Texture("dizzy-1.png");
	}

	public void makeBomb(){
		objHeight = random.nextFloat();
		if(objHeight < 0.5f){
			objHeight += 0.5;
		}
		float BombHeight = objHeight*Gdx.graphics.getHeight();
		bombYs.add((int)BombHeight);
		bombXs.add(Gdx.graphics.getWidth());
	}

	public void makeCoin(){
		objHeight = random.nextFloat();
		if(objHeight < 0.5f){
			objHeight += 0.5;
		}
		float coinHeight = objHeight*Gdx.graphics.getHeight();
		coinYs.add((int)coinHeight);
		coinXs.add(Gdx.graphics.getWidth());
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background,0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if(gameState == 0){
			//GAME IS LIVE
			if(coinCount < 100){
				coinCount++;
			}else{
				coinCount=0;
				makeCoin();
			}

			coinRectangle.clear();
			for(int i=0; i<coinXs.size(); i++){
				batch.draw(coin, coinXs.get(i), coinYs.get(i));
				coinXs.set(i, coinXs.get(i)-4);
				coinRectangle.add(new Rectangle(coinXs.get(i), coinYs.get(i), coin.getWidth(), coin.getHeight()));
			}

			bombRectangle.clear();
			for(int i=0; i<bombXs.size(); i++){
				batch.draw(bomb, bombXs.get(i), bombYs.get(i));
				bombXs.set(i, bombXs.get(i)-4);
				bombRectangle.add(new Rectangle(bombXs.get(i), bombYs.get(i), bomb.getWidth(), bomb.getHeight()));
			}

			if(bombCount < bombplace ){
				bombCount++;
			}else{
				bombCount=0;
				makeBomb();
			}

			if(Gdx.input.justTouched()){
				velocity = -10;
			}

			if(pause < 8){
				pause++;
			}else {
				pause=0;
				if (manstate < 3) {
					manstate++;
				} else {
					manstate = 0;
				}
			}

			velocity += gravity;
			manY -= velocity;

			if(manY <=0){
				manY = 0;
			}
		} else if (gameState == 1) {
			// WAITING TO START THE GAME
			if(Gdx.input.justTouched()){
				gameState = 0;
			}
		} else if (gameState == 2) {
			//GAME OVER SITUATION
			if(Gdx.input.justTouched()){
				gameState = 0;
				score = 0;
				velocity = 0;
				manY = Gdx.graphics.getHeight()/2;
				coinXs.clear();
				coinYs.clear();
				coinRectangle.clear();
				bombXs.clear();
				bombYs.clear();
				bombRectangle.clear();
			}
		}


		if(gameState == 2){
			batch.draw(dizzy,Gdx.graphics.getWidth() / 2 - man[manstate].getWidth() / 2, manY);
		}else {
			batch.draw(man[manstate], Gdx.graphics.getWidth() / 2 - man[manstate].getWidth() / 2, manY);
		}

		manRectangle = new Rectangle(Gdx.graphics.getWidth()/2 - man[manstate].getWidth()/2, manY, man[manstate].getWidth(), man[manstate].getHeight());


		//CHECKING THE COIN COLLISION WITH THE PLAYER....
		for(int i=0; i<coinRectangle.size(); i++){
			if(Intersector.overlaps(manRectangle,coinRectangle.get(i))){
				score++;

				coinRectangle.remove(i);
				coinXs.remove(i);
				coinYs.remove(i);
				break;
			}
		}

		//CHECKING THE BOMB COLLISION WITH THE PLAYER....
		for(int i=0; i<bombRectangle.size(); i++){
			if(Intersector.overlaps(manRectangle,bombRectangle.get(i))){
				gameState = 2;
			}
		}

		font.draw(batch, String.valueOf(score), 100, 200);

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
