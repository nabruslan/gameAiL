package items;


import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
//общий класс предметов

/*Автор: Набиуллин Руслан, группа 3308
 * Интерфейс предметов*/

public interface Items {
	//отрисовка на сцене
	public void draw();
	//отрисовка в сумке
	public void drawInBag();
	//обновление
	public void update();
	//проверка области
	public boolean placeToTouch(Vector3 touchPos);
	//изменение координат
	public void setCoords(int x, int y);
	public void setCoords(int x, int y, int width, int height);
	//получить отображение
	public Rectangle getRectangle();
	//получить информацию
	public String getInfo();
	public String getName();
	public boolean getIsAvailable();
	public void setIsAvailable();
	public void download();
	public void save();
	public void dispose();
}
