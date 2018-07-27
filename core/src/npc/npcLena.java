package npc;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.AiL;
import com.mygdx.game.Animator;
import com.mygdx.game.screens.Screens;

import items.Items;

public class npcLena implements NPC {
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
	Animator talkAnimation;
	String name;
	
	public npcLena(final AiL game, final Screens screen) {//конструктор	
		this.game = game;//инициализация объекта класса игры	
		this.screen = screen;//инициализация сцены
		
		npcImage = new Texture(Gdx.files.internal("Lena.png"));	//загрузка изображений	
		talkAnimation = new Animator(2, 2, "lenaAnimation.png", game);
		
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
		
		name = "npcLena";
	}	
	
	public void draw() {//отрисовка НПЦ
		if(!talking && !showMessage) {
			game.batch.draw(npcImage, npcRectangle.x, npcRectangle.y,
							npcRectangle.width, npcRectangle.height); //отрисвка персонажа
		}
		else talkAnimation.draw(npcRectangle.x, npcRectangle.y,
								npcRectangle.width + 10, npcRectangle.height + 10, false);
	}
		
	public void update(Items item) {//действия НПЦ с предметом
		getItem(item);//если получен предмет, то обновляем состояние
	}
	
	public void update() {//действия НПЦ без предмета
		if(!talking && !getItem) {//если не говорим и не получен предмет
			talkAnimation.reset();
			talking = true;//говорим
			game.player.screen.getDialogWindow().setTexts(answers, questions);
			screen.setDialog(true);//показываем диалог
		}
		else if(!showMessage && getItem) {//если получен предмет и нет сообщения
			talkAnimation.reset();
			showMessage = true;//показываем сообщение
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
		answers.add("Лена: Джеронимо!");
		answers.add("Лена: Газету \"Электрик\". Там про тебя написали.");
		answers.add("Лена: Пишу, пишу. Сегодня осознала, что мне нужны кое-какие формулы по физике. "
				+ "Интернет не работает, к сожалению...");
		answers.add("Лена: Там пишут про вашу победу на фестивале искусств. Поздравляю, это было классно!");
		questions.add("1. Что читаешь?");
		questions.add("2. Как диплом?");
		questions.add("3. Что в газете пишут про меня?");
		message.setMessage("Лена: Оооо, это та самая книжка. Спасибо! Держи газету, мне она не нужна больше.");
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
		if(item.getName() == "itemPhysicsBook") {//если нужный предмет
			talkAnimation.reset();
			getItem = true;
			game.player.unuseItem();//снять использование предмета с игрока
			item.setIsAvailable();//сделать предмет недоступным
			game.player.bag.itemList.remove(item);//удалить из сумки
			showMessage = true;//показать сообщение
			message.setMessage("Лена: Оооо, это та самая книжка. Спасибо! Держи газету, мне она не нужна больше.");
			ArrayList<Items> itemList = screen.getItems();
			for(int i = 0; i < itemList.size(); i++)
				if(itemList.get(i).getName() == "itemPaper") {
					itemList.get(i).update();
				}
		}
		else {//иначе
			talkAnimation.reset();
			message.setMessage("Лена: Это мне не нужно...");//установить сообщение
			showMessage = true;//показать сообщение
			game.player.unuseItem();//снять использование
			getItem = false;//предмет не получен
		}
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
		talkAnimation.dispose();
	}
}
