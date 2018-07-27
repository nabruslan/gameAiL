package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.AiL;

/*Автор: Набиуллин Руслан, группа 3308
 * Класс игры, описывающий кнопки*/

public class button {
	final AiL game; //объект класса игры
	Texture backGround; //фон диалога
	public Rectangle dialogRectangle; //представление диалогового окна
	String text;
	
	public button(final AiL game) {//конструктор
		this.game = game; //инициализация объекта класса игры
		//загрузка изображений
		backGround = new Texture(Gdx.files.internal("dialogBackground.png"));
		//инициализация представления
		dialogRectangle = new Rectangle();
		dialogRectangle.width = 200;
		dialogRectangle.height = 50;
		dialogRectangle.x = 0;
		dialogRectangle.y = 0;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void setCoords(float x, float y) {
		dialogRectangle.x = x;
		dialogRectangle.y = y;
	}
	
	public void setCoords(float y) {
		dialogRectangle.x = 800/2 - dialogRectangle.width/2;
		dialogRectangle.y = y;
	}
	
	public void draw() {
		game.batch.draw(backGround, dialogRectangle.x, dialogRectangle.y, 
						dialogRectangle.width, dialogRectangle.height); //отрисовка фона диалога
		game.font.draw(game.batch, text, 
						dialogRectangle.x + dialogRectangle.width/2 - text.length()*5, 
						dialogRectangle.y + dialogRectangle.height - 15);
	}
	
	public void update(Vector3 touchPos) {
	}
	
	public boolean placeToTouch(Vector3 touchPos) {//проверка области
		if(touchPos.x >= dialogRectangle.x && 
				touchPos.x <= dialogRectangle.x + dialogRectangle.width &&
				touchPos.y >= dialogRectangle.y &&
				touchPos.y <= dialogRectangle.y + dialogRectangle.height)
				return true;
		return false;
	}
	
	public void dispose() {
		backGround.dispose();
	}
}
