package items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.AiL;
//закрыть что-либо
/*Автор: Набиуллин Руслан
 * Класс игры, описывающий кнопку "закрыть"*/

public class Back {
	final AiL game;//объект игры
	Texture backIcon;//иконка
	Rectangle backRectangle;//представление
	
	public Back(final AiL game) {	
		this.game = game;//инициализация объекта игры
		//загрузка изображений
		backIcon = new Texture(Gdx.files.internal("back.png"));
		//инициализация представления
		backRectangle = new Rectangle();
		backRectangle.width = 30;
		backRectangle.height = 30;
		backRectangle.x = 800 - backRectangle.width;
		backRectangle.y = 400 - backRectangle.height;
	}
	
	public void draw() {//отрисовка
		game.batch.draw(backIcon, backRectangle.x, backRectangle.y, backRectangle.width, backRectangle.height);
	}
	
	public void update() {}//обновление
	
	public boolean placeToTouch(Vector3 touchPos) {//проверка области
		if(touchPos.x >= backRectangle.x && 
				touchPos.x <= backRectangle.x + backRectangle.width &&
				touchPos.y >= backRectangle.y &&
				touchPos.y <= backRectangle.y + backRectangle.height)
				return true;
		return false;
	}
	
	public void dispose() {
		backIcon.dispose();
	}
}
