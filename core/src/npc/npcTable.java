package npc;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.AiL;
import com.mygdx.game.screens.Screens;

import items.Items;

public class npcTable implements NPC {
	final AiL game; //объект класса игры
	final Screens screen;//объект сцены
	Texture npcImage; //изображение персонажа
	Rectangle npcRectangle; //для представления персонажа
	ArrayList<String> answers; //ответы персонажа
	ArrayList<String> questions; //вопросы персонажу
	boolean getItem;//получен ли предмет
	boolean showMessage;//флаг сообщения
	boolean talking; //флаг беседы
	ShowMessage message;//окно сообщения
	Preferences preferences;//сохранения
	String name;
	
	public npcTable(final AiL game, final Screens screen) {//конструктор	
		this.game = game;//инициализация объекта класса игры	
		this.screen = screen;//инициализация сцены
		
		npcImage = new Texture(Gdx.files.internal("table.png"));	//загрузка изображений	
		
		talking = false;//инициализация флага беседы
		getItem = false;//предмет не получен
		showMessage = false;//сообщение не показывается
		
		//представление персонажа
		npcRectangle = new Rectangle();
		
		answers = new ArrayList<String>(); //инициализация текста
		questions = new ArrayList<String>(); //инициализация текста
		
		message = new ShowMessage(game);//инициализация окна сообщения
		setText();//устанавливаем тексты
		
		preferences = Gdx.app.getPreferences("NPC");//сохранения
		
		name = "npcTable";
	}	
	
	public void draw() {//отрисовка НПЦ
		game.batch.draw(npcImage, npcRectangle.x, npcRectangle.y,
					npcRectangle.width, npcRectangle.height); //отрисвка персонажа
	}
		
	public void update(Items item) {//действия НПЦ с предметом
		getItem(item);//если получен предмет, то обновляем состояние
	}
	
	public void update() {//действия НПЦ без предмета
		if(!showMessage) {//если получен предмет и нет сообщения
			showMessage = true;//показываем сообщение
			if(!getItem) {
				ArrayList<Items> itemList = screen.getItems();
				for(int i = 0; i < itemList.size(); i++)
					if(itemList.get(i).getName() == "itemNotes") {
						itemList.get(i).update();
						getItem = true;
						message.setMessage("Стол: <там лежали ноты>");
					}
			}
		}
	}
	
	public boolean placeToTouch(Vector3 touchPos) {//проверка области
		if(touchPos.x >= npcRectangle.x && 
				touchPos.x <= npcRectangle.x + npcRectangle.width &&
				touchPos.y >= npcRectangle.y &&
				touchPos.y <= npcRectangle.y + npcRectangle.height)
				return true;
		return false;
	}
	
	//получение параметров===================================================
	@Override
	public Rectangle getRectangle() {
		return npcRectangle;
	}

	@Override
	public ArrayList<String> getAnswers() {
		return answers;
	}

	@Override
	public ArrayList<String> getQuestions() {
		// TODO Auto-generated method stub
		return questions;
	}
	
	public boolean getMessageState() {
		return showMessage;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean getItemState() {
		return getItem;
	}

	//установка параметров=========================================================
	@Override
	public void setTalking() {
		talking = !talking;
	}
	
	@Override
	public void setText() {
		answers.add("Автомат: <бззз>");
		answers.add("Автомат: <эх, ничего не выпало>");
		answers.add("Автомат: <там много еды и напитков>");
		answers.add("Автомат: <бззз...>");
		questions.add("1. <постучать>");
		questions.add("2. <осмотреть>");
		questions.add("3. Как дела, автомат?");
		message.setMessage("Стол: <там лежали ноты>");
	}
	
	public void setGetItem() {
		getItem = !getItem;
	}
	
	public void setMessage() {
		showMessage = !showMessage;
	}
	
	public void setCoords(float x, float y) {
		npcRectangle.x = x;
		npcRectangle.y = y;
	}
	
	public void setCoords(float x, float y, float width, float height) {
		npcRectangle.x = x;
		npcRectangle.y = y;
		npcRectangle.width = width;
		npcRectangle.height = height;
	}

	@Override
	public void getItem(Items item) {//получение предмета
		message.setMessage("Я: зачем это делать?");//установить сообщение
		showMessage = true;//показать сообщение
		game.player.unuseItem();//снять использование
		getItem = false;//предмет не получен
	}
	
	public void showMessage() {//отрисовать сообщение
		if(showMessage)
			message.draw();
	}
	
	//сохранение/загрузка/ресет====================================================
	@Override
	public void save() {
		preferences.putBoolean(game.downloadMenu + name + "getItem", getItem);
		preferences.flush();
	}

	@Override
	public void download() {
		getItem = preferences.getBoolean(game.downloadMenu + name + "getItem");
	}

	@Override
	public void reset() {
		talking = false;//инициализация флага беседы
		getItem = false;
		showMessage = false;
	}
	
	public void dispose() {
		npcImage.dispose();
	}
}
