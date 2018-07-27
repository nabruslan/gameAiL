package items;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import player.Player;

/*Автор: Набиуллин Руслан, группа 3308
 * Класс игры, описывающий сумку (инвентарь) игрока*/

//сумка персонажа
public class Bag {
	final Player player; //объект класса Игрока
	Texture bagIcon;//иконка закрытой сумки для интерфейса
	Texture bagInside;//изображение открытой сумки
	public Rectangle bagRectangle;//представление
	public boolean opened;//флаг открытия
	Vector3 touchPosition;//координаты касания
	public Back back;//объект закрытия
	public ArrayList<Items> itemList;//список предметов
	public boolean useItem;//используется ли предмет
	public UseItem itemDialog;//диалог предмета
	public int currentItem;//текущий предмет
	
	public Bag(final Player player) {//конструктор
		this.player = player;//инициализация игрока
		
		//загрузка изображений
		bagIcon = new Texture(Gdx.files.internal("bagIcon.png"));
		bagInside = new Texture(Gdx.files.internal("bagInside.png"));	
		opened = false;//по умолчанию закрыта
		
		//инициализация представления
		bagRectangle = new Rectangle();
		bagRectangle.width = 60;
		bagRectangle.height = 60;	
		bagRectangle.x = 800 - bagRectangle.width;
		bagRectangle.y = 0;
		touchPosition = new Vector3();//координаты касания	
		back = new Back(player.game);//инициализация закрытия	
		itemList = new ArrayList<Items>();//инициализация списка предметов
		useItem = false;//по умолчанию ничего не используется
		itemDialog = new UseItem(player.game);//инициализация диалога предметов
		currentItem = 0;//текущий предмет
	}
	
	public void draw() {//отрисовка
		if(!opened) //если закрыт
			//рисуем иконку интерфейса
			player.game.batch.draw(bagIcon, bagRectangle.x, bagRectangle.y, bagRectangle.width, bagRectangle.height);
		else { //если закрыт
			//рисуем открытую сумку
			player.game.batch.draw(bagInside, 0, 0, 800, 400);
			//рисуем значок "закрыть"
			back.draw();
			//если есть предметы, то рисуем их
			if(!itemList.isEmpty()) {
				int j = 1;
				for(int i = 1; i <= itemList.size(); i++) {
					if(i % 5 == 0) j++;
					itemList.get(i - 1).setCoords(100*i, 400 - 100*j, 50, 50);
					itemList.get(i - 1).drawInBag();
				}
			}
			if(useItem)
				itemDialog.draw();
		}
	}
	
	public void update(Vector3 touchPos) {//обновление сумки
		if(!opened) //если закрыта
			opened = true; //открываем
		else {
			if(back.placeToTouch(touchPos))//если нажали "назад"
				close();//закрываем
			if(!useItem) {//если нет диалога с предметом
				if(!itemList.isEmpty())//если есть предметы
					for(int i = 0; i < itemList.size(); i++)//смотрим все предметы
						if(itemList.get(i).placeToTouch(touchPos)) {//если на какой-то нажали
							itemDialog.setParametres(itemList.get(i).getInfo(), itemList.get(i).getRectangle());//устанавливаем параметры диалога
							useItem = true;//"общаемся" с предметом
							currentItem = i;//текущий предмет
						}
			}
			else itemDialog.update(itemList.get(currentItem), touchPos);//обновляем диалог с предметами
		}
	}
	
	public boolean placeToTouch(Vector3 touchPos) {//проверка области
		if(touchPos.x >= bagRectangle.x && 
				touchPos.x <= bagRectangle.x + bagRectangle.width &&
				touchPos.y >= bagRectangle.y &&
				touchPos.y <= bagRectangle.y + bagRectangle.height)
				return true;
		return false;
	}
	
	public void close() {//закрыть сумку
		opened = false;
		useItem = false;
	}
	
	public void dispose() {
		bagIcon.dispose();
		bagInside.dispose();
		itemDialog.dispose();
	}
}
