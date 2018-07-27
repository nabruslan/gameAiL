package items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.AiL;
//закрыть что-либо
public class Door {
	final AiL game;//объект игры
	Texture doorImage;//иконка
	Rectangle doorRectangle;//представление
	
	public Door(final AiL game) {	
		this.game = game;//инициализация объекта игры
		//загрузка изображений
		doorImage = new Texture(Gdx.files.internal("door.png"));
		//инициализация представления
		doorRectangle = new Rectangle();
		doorRectangle.width = 100;
		doorRectangle.height = 280;
	}
	
	public void setCoords(int x, int y) {
		doorRectangle.x = x;
		doorRectangle.y = y;
	}
	
	public void setCoords(int x, int y, int width, int height) {
		doorRectangle.x = x;
		doorRectangle.y = y;
		doorRectangle.width = width;
		doorRectangle.height = height;
	}
	
	public void draw() {//отрисовка
		game.batch.draw(doorImage, doorRectangle.x, doorRectangle.y, doorRectangle.width, doorRectangle.height);
	}
	
	public void update() {}//обновление
	
	public boolean placeToTouch(Vector3 touchPos) {//проверка области
		if(touchPos.x >= doorRectangle.x && 
				touchPos.x <= doorRectangle.x + doorRectangle.width &&
				touchPos.y >= doorRectangle.y &&
				touchPos.y <= doorRectangle.y + doorRectangle.height)
				return true;
		return false;
	}
	
	public void dispose() {
		doorImage.dispose();
	}
}