package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/*Автор: Набиуллин Руслан
 * Класс игры, описывающий инструмент для воспроизведения
 * анимаций в игре*/

public class Animator {
	public final AiL game;
	
	int FRAME_COLS;
	int FRAME_ROWS;
	
	Animation<TextureRegion> animation;
	Texture spriteSheet;
	TextureRegion[] frames;
	TextureRegion currentFrame;
	
	float stateTime;
	
	public Animator(int fRAME_COLS, int fRAME_ROWS, String path, final AiL game) {
		this.game = game;
		FRAME_COLS = fRAME_COLS;
		FRAME_ROWS = fRAME_ROWS;
		
		spriteSheet = new Texture(Gdx.files.internal(path));
		TextureRegion[][] tmp = TextureRegion.split(spriteSheet, spriteSheet.getWidth()/FRAME_COLS, spriteSheet.getHeight()/FRAME_ROWS);
		frames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
		int index = 0;
		for(int i = 0; i < FRAME_ROWS; i++)
			for(int j = 0; j < FRAME_COLS; j++)
				frames[index++] = tmp[i][j];
		animation = new Animation<TextureRegion>((float)1/(FRAME_COLS*FRAME_ROWS), frames);
		stateTime = 0f;
	}
	
	public void draw(float x, float y, float width, float height, boolean looping) {
		stateTime += Gdx.graphics.getDeltaTime();
		currentFrame = animation.getKeyFrame(stateTime, looping);
		game.batch.draw(currentFrame, x, y, width, height);
	}
	
	public void reset() {
		stateTime = 0f;
	}
	
	public void dispose() {
		spriteSheet.dispose();
	}
}
