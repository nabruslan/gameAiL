package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.AiL;
//закрыть что-либо

/*Автор: Набиуллин Руслан
 * Класс игры, описывающий кнопки перехода
 * между локациями*/

public class changeScreen {
	
	final AiL game;//объект игры
	Texture changeIcon;//иконка
	public Rectangle changeRectangle;//представление
	
	public changeScreen(final AiL game, int state) {
		this.game = game;//инициализация объекта игры
		//загрузка изображений
		if(state == 0)
			changeIcon = new Texture(Gdx.files.internal("changeLeft.png"));
		else changeIcon = new Texture(Gdx.files.internal("changeRight.png"));
		//инициализация представления
		changeRectangle = new Rectangle();
		changeRectangle.width = 50;
		changeRectangle.height = 50;
		if(state != 0) {
			changeRectangle.x = 800 - changeRectangle.width;
			changeRectangle.y = 200 + changeRectangle.height/2;
		}
		else {
			changeRectangle.x = 0;
			changeRectangle.y = 200 + changeRectangle.height/2;
		}
			
	}
	
	public void draw() {//отрисовка
		game.batch.draw(changeIcon, changeRectangle.x, changeRectangle.y, changeRectangle.width, changeRectangle.height);
	}
	
	public void update() {}//обновление
	
	public boolean placeToTouch(Vector3 touchPos) {//проверка области
		if(touchPos.x >= changeRectangle.x && 
				touchPos.x <= changeRectangle.x + changeRectangle.width &&
				touchPos.y >= changeRectangle.y &&
				touchPos.y <= changeRectangle.y + changeRectangle.height)
				return true;
		return false;
	}
	
	public void dispose() {
		changeIcon.dispose();
	}
}
