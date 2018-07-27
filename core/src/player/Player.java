package player;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.AiL;
import com.mygdx.game.Animator;
import com.mygdx.game.screens.Screens;
import com.mygdx.game.screens.changeScreen;
import items.Bag;
import items.Items;
import npc.DialogWithNPC;
import npc.NPC;
import npc.ShowMessage;

public class Player {//игрок

	public final AiL game;//объект класса игры
	public Screens screen;//текущая сцена
	public Camera camera;//камера
	public ArrayList<Items> itemList; //список предметов сцены
	public int currentItem;//предмет, который надо взять или использовать
	public changeScreen changeLeft, changeRight;//кнопки изменения экрана
	public ArrayList<NPC> npcList;//неигровой персонаж сцены
	public int currentNPC;
	
	public Bag bag;//сумка	
	Texture playerImage; //изображение игрока
	public Rectangle playerRectangle;//для представления игрока
	int speed; //скорость передвижения
	public boolean move;//флаг ходьбы	
	public boolean moveToNPC;//идти к персонажу	
	public boolean moveToItem;//идти к предмету
	public boolean moveToChange;//идти к переходу
	public boolean talking;//разговаривает ли в данный момент
	public boolean usingItem;
	public boolean showMessage;
	public Vector3 touchPosition;//координаты касания
	public DialogWithNPC dialogWindow;//диалоговое окно сцены
	ShowMessage message;//окно сообщения
	Preferences preferences;//сохранение
	Animator walkRight;
	Animator walkLeft;
	Animator messageAnimation;
	
	/*Автор: Набиуллин Руслан, группа 3308
	 * Класс, описывающий игрока*/
	
	public Player(final AiL game) {//конструктор		
		this.game = game;//инициализация объекта класса игры
		
		playerImage = new Texture(Gdx.files.internal("Ruslan.png"));//загрузка изображений
		walkRight = new Animator(4, 2, "RuslanWalkAnimationRight.png", game);
		walkLeft = new Animator(4, 2, "RuslanWalkAnimationLeft.png", game);
		messageAnimation = new Animator(4, 1, "ruslanFacepalm.png", game);
		
		//представление игрока
		playerRectangle = new Rectangle();
		playerRectangle.width = 100;
		playerRectangle.height = 320;
		playerRectangle.x = 800/2 - playerRectangle.width*2;
		playerRectangle.y = 0;	
		
		speed = 200;//инициализация скорости передвижения	
		bag = new Bag(this);//инициализация объекта класса сумки	
		move = false;//по умолчанию стоит
		moveToNPC = false;
		moveToItem = false;
		moveToChange = false;	
		talking = false; //по умолчанию ни с кем не говорим
		usingItem = false;//не использует предмет
		showMessage = false;//не показывает сообщение
		
		touchPosition = new Vector3();//инициализация координат касания		
		itemList = new ArrayList<Items>(); //инициализация предметов сцены
		npcList = new ArrayList<NPC>();
		currentItem = 0;//предмет, который надо взять
		currentNPC = 0;

		message = new ShowMessage(game);//инициализация сообщения
		preferences = Gdx.app.getPreferences("Player");//получение сохранени
	}
	
	public void setScreen(float x) {//меняем состояние при изменении сцены
		//меняем координаты
		playerRectangle.width = 100;
		playerRectangle.height = 320;
		playerRectangle.x = x;
		playerRectangle.y = 0;	
		
		screen = game.screenList.get(game.currentScreen);//инициализация текущей сцены	
		camera = screen.getCamera();//заново получаем камеру	
		npcList = screen.getNPC();//инициализация неигрового персонажа сцены
		changeLeft = screen.getChangeLeft();
		changeRight = screen.getChangeRight();
		itemList = screen.getItems(); //получаем список предметов
		
		move = false;//по умолчанию стоит
		moveToNPC = false;
		moveToItem = false;
		moveToChange = false;
		talking = false; //по умолчанию не говорим
		
		dialogWindow = screen.getDialogWindow();
	}
	
	public void draw() {//отрисовка персонажа	
		if(!move && !moveToNPC && !moveToItem && !moveToChange) {
			if(!showMessage)
				game.batch.draw(playerImage, playerRectangle.x, playerRectangle.y,
								playerRectangle.width, playerRectangle.height); //рисуем игрока
			else
				messageAnimation.draw(playerRectangle.x, playerRectangle.y,
								playerRectangle.width, playerRectangle.height, false);
		}
		else if(playerRectangle.x > touchPosition.x - playerRectangle.width/2 + 10
				&& playerRectangle.x > 10) {
			walkLeft.draw(playerRectangle.x, playerRectangle.y - 10, playerRectangle.width+50, playerRectangle.height + 10, true);
		}
		else if(playerRectangle.x < touchPosition.x - playerRectangle.width/2 - 10
				&& playerRectangle.x < 800 - playerRectangle.width) {
			walkRight.draw(playerRectangle.x, playerRectangle.y - 10, playerRectangle.width+40, playerRectangle.height + 10, true);
		}
	}
	
	
	public void update() {//обновление персонажа
		if(Gdx.input.justTouched()) {
			touchPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0); //получаем координаты нажатия
			camera.unproject(touchPosition);
			if(placeToTouch(touchPosition) && !talking && !move && !moveToItem 
					&& !moveToNPC && !moveToNPC && !usingItem && !bag.opened) {
				showMessage = true;
				messageAnimation.reset();
				message.setMessage("Я: Не надо в меня тыкать, пожалуйста");
			}
			else move = actWithWorld(touchPosition);
		}
		if(move /*&& !moveToNPC && !moveToItem && !moveToChange*/) move(); //идём к точке
		if(moveToNPC) moveToNPC(); //идём к персонажу
		if(moveToItem) moveToItem(); //идём к предмету
		if(moveToChange) moveToChange(); //идём к переходу
	}
	
	
	//ДВИЖЕНИЕ-------------------------------------------------------------------------------------------
	public void move() {//функция движения
		if(move && playerRectangle.x < touchPosition.x - playerRectangle.width/2 - 10
				&& playerRectangle.x < 800 - playerRectangle.width)
			playerRectangle.x += speed*Gdx.graphics.getDeltaTime();
		else if(move && playerRectangle.x > touchPosition.x - playerRectangle.width/2 + 10
				&& playerRectangle.x > 10)
			playerRectangle.x -= speed*Gdx.graphics.getDeltaTime();
		else move = false;
	}
	
	public void moveToNPC() {//функция движения к НПЦ
		if(moveToNPC && playerRectangle.x < touchPosition.x - playerRectangle.width/2 - 10
				&& playerRectangle.x < 800 - playerRectangle.width)
			playerRectangle.x += speed*Gdx.graphics.getDeltaTime();
		else if(moveToNPC && playerRectangle.x > touchPosition.x - playerRectangle.width/2 + 10
				&& playerRectangle.x > 10)
			playerRectangle.x -= speed*Gdx.graphics.getDeltaTime();
		else {
			if(usingItem)
				npcList.get(currentNPC).update(itemList.get(currentItem));
			else 
				npcList.get(currentNPC).update();
			moveToNPC = false;
			talking = true; //говорим с НПЦ
		}
	}
	
	public void moveToItem() {//функция движения к предмету
		if(moveToItem && playerRectangle.x < touchPosition.x - playerRectangle.width/2 - 10
				&& playerRectangle.x < 800 - playerRectangle.width)
			playerRectangle.x += speed*Gdx.graphics.getDeltaTime();
		else if(moveToItem && playerRectangle.x > touchPosition.x - playerRectangle.width/2 + 10
				&& playerRectangle.x > 10)
			playerRectangle.x -= speed*Gdx.graphics.getDeltaTime();
		else {
			if(!itemList.isEmpty())
				itemList.get(currentItem).update();
			currentItem = 0;
			moveToItem = false;
		}
	}
	
	public void moveToChange() {//функция движения к переходу
		if(moveToChange && playerRectangle.x < touchPosition.x - playerRectangle.width/2 - 10
				&& playerRectangle.x < 800 - playerRectangle.width)
			playerRectangle.x += speed*Gdx.graphics.getDeltaTime();
		else if(moveToChange && playerRectangle.x > touchPosition.x - playerRectangle.width/2 + 10
				&& playerRectangle.x > 10)
			playerRectangle.x -= speed*Gdx.graphics.getDeltaTime();
		else {
			if(touchPosition.x < 400)
				screen.changeScreen(false);
			else
				screen.changeScreen(true);
			moveToChange = false;
		}
	}
	
	
	//ВЗАИМОДЕЙСТВИЕ---------------------------------------------------------------------------------------------------
	public boolean actWithWorld(Vector3 touchPos) {//проверка области
		if(bag.opened) {//если сумка открыта
			//то ничего не делает
			moveToItem = false;
			moveToChange = false;
			moveToNPC = false;
			bag.update(touchPos);//проверяем состояние сумки
			return false;
		}
		hideMessages();//прячем все диалоги
		if(dialogWindow != null) {//если есть диалоговое окно
			if(screen.getDialog() && dialogWindow.placeToTouch(touchPos)) {//если активен диалог и на него нажали
				dialogWindow.update(touchPos);//обновляем диалог
				moveToItem = false;
				moveToChange = false;
				moveToNPC = false;
				return false;
			}
		}
		if(game.back.placeToTouch(touchPosition)) {//если нажали назад
			game.pauseScreen = game.State;
			game.State = "PAUSE";//идём в меню
			game.changeScreen();
		}
		if(screen.getDialog()) {//если идёт диалог
			if(!npcList.isEmpty())
				for(int i = 0; i < npcList.size(); i++)
					npcList.get(i).setTalking();
			screen.setDialog(false);
			talking = false;
		}
		if(npcList!= null && !npcList.isEmpty()) {//если есть НПЦ
			for(int i = 0; i < npcList.size(); i++) {
				if(npcList.get(i).placeToTouch(touchPos)) {//если на него нажали
					//идём к нему и взаимодействуем
					touchPosition.set(npcList.get(i).getRectangle().x - playerRectangle.width/2, Gdx.input.getY(), 0);
					currentNPC = i;
					moveToItem = false;
					moveToChange = false;
					moveToNPC = true;
					//if(usingItem) npcList.get(i).setGetItem();//если используется предмет, отдаём НПЦ
					return false;
				}
				else if(usingItem) {//если не на персонажа, но используется предмет
					//снимаем использование и стоим на месте
					moveToItem = false;
					moveToChange = false;
					moveToNPC = false;
					unuseItem();
					return false;
				}
			}
		}
		else if(usingItem) {//если нет персонажа, снимаем использование предмета
			moveToItem = false;
			moveToChange = false;
			moveToNPC = false;
			unuseItem();
			return false;
		}
		if(bag.placeToTouch(touchPos) && !bag.opened) {//если сумка закрыта, нажали на иконку сумки
			moveToItem = false;
			moveToChange = false;
			moveToNPC = false;
			bag.update(touchPos);//обновляем состояние сумки
			return false;
		}
		if(itemList != null && !itemList.isEmpty()) {//если на сцене есть предметы
			for(int i = 0; i < itemList.size(); i++)//проверяем все
				if(itemList.get(i).placeToTouch(touchPos)) {//если нажали на какой-то
					//идём к нему
					touchPosition.set(itemList.get(i).getRectangle().x - playerRectangle.width/2, Gdx.input.getY(), 0);
					currentItem = i;
					moveToItem = true;
					moveToChange = false;
					moveToNPC = false;
					return false;
				}
		}
		if(changeLeft != null) {
			if(changeLeft.placeToTouch(touchPos)) {
				touchPosition.set(0, Gdx.input.getY(), 0);
				moveToItem = false;
				moveToChange = true;
				moveToNPC = false;
				return false;
			}
		}
		if(changeRight != null) {
			if(changeRight.placeToTouch(touchPos)) {
				touchPosition.set(800, Gdx.input.getY(), 0);
				moveToItem = false;
				moveToChange = true;
				moveToNPC = false;
				return false;
			}
		}
		moveToItem = false;
		moveToChange = false;
		moveToNPC = false;
		return true;
	}
	
	public boolean placeToTouch(Vector3 touchPos) {//проверка области персонажа
		if(touchPos.x >= playerRectangle.x + playerRectangle.width/4 && 
				touchPos.x <= playerRectangle.x + playerRectangle.width*3/4 &&
				touchPos.y >= playerRectangle.y + playerRectangle.height*2/3 &&
				touchPos.y <= playerRectangle.y + playerRectangle.height)
				return true;
		return false;
	}
	
	public void useItem(Items item) {//используем предмет
		usingItem = true;
		bag.close();
		item.setCoords(0, 400 - (int)item.getRectangle().height);
		itemList.add(item);
		currentItem =  itemList.indexOf(item);
		itemList.get(currentItem).update();
	}
	
	public void unuseItem() {//снимаем использование
		usingItem = false;
		itemList.get(currentItem).update();
		currentItem = 0;
	}
	
	public void hideMessages() {//прячем сообщения и диалоги
		if(npcList != null && !npcList.isEmpty())
			for(int i = 0; i < npcList.size(); i++)
				if(npcList.get(i).getMessageState())
					npcList.get(i).setMessage();
		showMessage = false;
		talking = false;
	}
	
	public void showMessage() {//показать сообщение
		if(showMessage)
			message.draw();
	}
	
	//сохранение/загрузка/перезагрузка======================================================
	public void save() {
		preferences.putFloat(game.downloadMenu + "x", playerRectangle.x);
		preferences.flush();
	}
	
	public void download() {
		bag.itemList.clear();
		playerRectangle.x = preferences.getFloat(game.downloadMenu + "x");
	}
	
	public void reset() {
		bag.itemList.clear();
	}
	
	public void setCoords(float x, float y) {
		playerRectangle.x = x;
		playerRectangle.y = y;
	}
	
	public void setCoords(float x, float y, float width, float height) {
		playerRectangle.x = x;
		playerRectangle.y = y;
		playerRectangle.width = width;
		playerRectangle.height = height;
	}
	
	public void dispose() {
		playerImage.dispose();
		bag.dispose();
		walkRight.dispose();
		walkLeft.dispose();
	}
}
