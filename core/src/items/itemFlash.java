package items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.screens.Screens;

import player.Player;
//предмет "флешка"
public class itemFlash implements Items{
	Player player;//игрок
	Screens screen; //сцена, на которой предмет
	Texture itemIcon;//изображение предмета
	Rectangle itemRectangle;//представление предмета
	String info; //информация о предмете
	String name; //название предмета
	boolean isAvailable; //доступен ли на сцене
	boolean inBag;//флаг принадлежности сумки
	Preferences preferences; //для сохранений
	
	public itemFlash(Player player, Screens screen) {//конструктор	
		this.player = player;//инициализация игрока
		this.screen = screen;//получаем сцену
		
		itemIcon = new Texture(Gdx.files.internal("flashDrive.png"));//загрузка изображения
		
		//инициализация представлений
		itemRectangle = new Rectangle();
		
		inBag = false;//по умолчанию на сцене, а не в сумке
		isAvailable = true;//по умолчанию доступен
		
		info = "Флешечка для Даны";//инициализация информации
		name = "itemFlash";//инициализация названия
		
		preferences = Gdx.app.getPreferences("Items");//сохранение
	}
	
	public void draw() {//отрисовка на сцене
		if(!inBag && !player.bag.opened)//если на сцене и сумка закрыта
			player.game.batch.draw(itemIcon, itemRectangle.x, 
										itemRectangle.y, itemRectangle.width, itemRectangle.height);
	}
	
	public void drawInBag() {//отрисовка в сумке
		if(inBag)
			player.game.batch.draw(itemIcon, itemRectangle.x, 
					itemRectangle.y, itemRectangle.width, itemRectangle.height);//если в сумке
	}
	
	public void update() {//обновление
			if(!inBag) { //если не в сумке
				inBag = true; //кладём в сумке
				if(!player.bag.itemList.contains(this))
					player.bag.itemList.add(this); //кладём в сумку
				player.itemList.remove(this); //удаляем со сцены
			}
			else inBag = false; //иначе не в сумке
	}
	
	public boolean placeToTouch(Vector3 touchPos) {//проверка области
		if(touchPos.x >= itemRectangle.x && 
				touchPos.x <= itemRectangle.x + itemRectangle.width &&
				touchPos.y >= itemRectangle.y &&
				touchPos.y <= itemRectangle.y + itemRectangle.height)
				return true;
		return false;
	}
	
	public void setCoords(int x, int y) {//меняем координаты
		itemRectangle.x = x;
		itemRectangle.y = y;
	}
	
	public void setCoords(int x, int y, int width, int height) {//меняем координаты
		itemRectangle.x = x;
		itemRectangle.y = y;
		itemRectangle.width = width;
		itemRectangle.height = height;
	}
	
	//установка параметров================================================================
	@Override
	public void setIsAvailable() {//меняем флаг доступности
		isAvailable = !isAvailable;
	}
	
	//получение параметров======================================================
	@Override
	public Rectangle getRectangle() {//получить представление
		return itemRectangle;
	}

	@Override
	public String getInfo() {//получить информацию
		return info;
	}

	@Override
	public String getName() {//получить название
		return name;
	}

	@Override
	public boolean getIsAvailable() {//получить флаг доступности
		return isAvailable;
	}
	
	//сохранить/загрузить============================================================
	@Override
	public void download() {
		inBag = preferences.getBoolean(player.game.downloadMenu + name +"inBag");//в сумке ли
		isAvailable = preferences.getBoolean(player.game.downloadMenu + name +"isAvailable");//доступен ли предмет
		itemRectangle.x = preferences.getFloat(player.game.downloadMenu + name +"X");
		itemRectangle.y = preferences.getFloat(player.game.downloadMenu + name +"Y");
		itemRectangle.width = preferences.getFloat(player.game.downloadMenu + name +"Width");
		itemRectangle.height = preferences.getFloat(player.game.downloadMenu + name +"Height");
		if(isAvailable && inBag) {//если доступен и в сумке
			if(!player.bag.itemList.contains(this))
				player.bag.itemList.add(this);//добавляем в сумку
			if(screen.getItems().contains(this))
				screen.getItems().remove(this);
		}
		else if(isAvailable && !inBag) {
			if(player.bag.itemList.contains(this))
				player.bag.itemList.remove(this);//добавляем в сумку
			if(!screen.getItems().contains(this)) {
				screen.getItems().add(this);
			}
		}
		else if(!isAvailable) {
			if(player.bag.itemList.contains(this))
				player.bag.itemList.remove(this);//добавляем в сумку
			if(screen.getItems().contains(this))
				screen.getItems().remove(this);
		}
	}

	@Override
	public void save() {
		preferences.putBoolean(player.game.downloadMenu + name +"inBag", inBag);//в сумке ли
		preferences.putBoolean(player.game.downloadMenu + name +"isAvailable", isAvailable);//доступно ли
		preferences.putFloat(player.game.downloadMenu + name +"X", itemRectangle.x);
		preferences.putFloat(player.game.downloadMenu + name +"Y", itemRectangle.y);
		preferences.putFloat(player.game.downloadMenu + name +"Width", itemRectangle.width);
		preferences.putFloat(player.game.downloadMenu + name +"Height", itemRectangle.height);
		preferences.flush();//сохраняем
	}
	
	public void dispose() {
		itemIcon.dispose();
	}
}
