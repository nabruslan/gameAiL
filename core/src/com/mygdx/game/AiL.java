package com.mygdx.game;

import java.util.ArrayList;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.mygdx.game.screens.DownloadMenu;
import com.mygdx.game.screens.MainMenuScreen;
import com.mygdx.game.screens.PauseMenu;
import com.mygdx.game.screens.SaveMenu;
import com.mygdx.game.screens.Screens;
import com.mygdx.game.screens.screenChairs1;
import com.mygdx.game.screens.screenChairs2;
import com.mygdx.game.screens.screenHall;
import com.mygdx.game.screens.screenHallTo3;
import com.mygdx.game.screens.screenKanz;
import com.mygdx.game.screens.screenLadder;
import com.mygdx.game.screens.screenPad1;
import com.mygdx.game.screens.screenPad2;
import com.mygdx.game.screens.screenPad3;
import com.mygdx.game.screens.screenRoomWithNotebook;

import items.Back;
import items.Items;
import player.Player;

/*Автор: Набиуллин Руслан, группа 3308
 * Основной класс игры*/

public class AiL extends Game {
	
	public SpriteBatch batch; //на чем рисуем
	public BitmapFont font; //шрифт
	public ArrayList<Screens> screenList; //список сцен
	public String State; //состояние игры
	public int currentScreen; //текущая сцена из списка
	public Player player; //игрок
	public ArrayList<Items> itemList;
	public boolean isSaved;
	Preferences preferences;
	public Back back;
	public String pauseScreen;
	public String downloadMenu;
	public Music backgroundMusic;
	public Music guitarMusic;
	
	private static final String FONT_CHARACTERS = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя"
			+ "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ"
			+ "abcdefghijklmnopqrstuvwxyz"
			+ "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
			+ "0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<>"; //русские буквы
	
	public void create() {
		player = new Player(this); //инициализация игрока
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("backgroundMusic.mp3"));
		guitarMusic = Gdx.audio.newMusic(Gdx.files.internal("guitar.mp3"));
		currentScreen = 0; //инициализация текущей сцены
		State = "MENU"; //инициализация состояния игры
		currentScreen = 0;
		batch = new SpriteBatch(); //инициализация батча
		font = new BitmapFont(); //шрифт
		Gdx.input.setCatchBackKey(true);
		final String FONT_PATH = "font.ttf";
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(FONT_PATH));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.characters = FONT_CHARACTERS;
		parameter.size = 30;
		parameter.color = Color.WHITE;
		font = generator.generateFont(parameter);
		generator.dispose();
		preferences = Gdx.app.getPreferences("Game");
		isSaved = false;
		back = new Back(this);
		itemList = new ArrayList<Items>();
		screenList = new ArrayList<Screens>(); //инициализация списка сцен
		screenList.add(new MainMenuScreen(this)); //меню
		screenList.add(new PauseMenu(this)); //меню паузы
		screenList.add(new DownloadMenu(this));//меню загрузки
		screenList.add(new SaveMenu(this));//меню сохранения
		screenList.add(new screenPad1(this));
		screenList.add(new screenPad2(this));
		screenList.add(new screenPad3(this));
		screenList.add(new screenKanz(this));
		screenList.add(new screenLadder(this));
		screenList.add(new screenChairs1(this));
		screenList.add(new screenHall(this));
		screenList.add(new screenChairs2(this));
		screenList.add(new screenHallTo3(this));
		screenList.add(new screenRoomWithNotebook(this));
		getItems();
		backgroundMusic.setLooping(true);
		guitarMusic.setLooping(true);
		backgroundMusic.play();
		changeScreen(); //ставим текущую сцену
	}
	//изменение сцены
	public void changeScreen() {
		switch(State) { //в зависимости от состояния
			case "MENU":
				backgroundMusic.stop();
				setScreen(screenList.get(0)); //переходим на другую сцену
				currentScreen = 0; //меняем указатель на текщуюу сцену
				break;
			case "PAUSE":
				backgroundMusic.pause();
				setScreen(screenList.get(1)); //переходим на другую сцену
				currentScreen = 1;
				break;
			case "DOWNLOAD":
				setScreen(screenList.get(2)); //переходим на другую сцену
				currentScreen = 2;
				break;
			case "SAVE":
				setScreen(screenList.get(3)); //переходим на другую сцену
				currentScreen = 3;
				break;
			case "Pad1":
				setScreen(screenList.get(4));
				currentScreen = 4;
				break;
			case "Pad2":
				setScreen(screenList.get(5));
				currentScreen = 5;
				break;
			case "Pad3":
				setScreen(screenList.get(6));
				currentScreen = 6;
				break;
			case "Kanz":
				setScreen(screenList.get(7));
				currentScreen = 7;
				break;
			case "Ladder":
				setScreen(screenList.get(8));
				currentScreen = 8;
				break;
			case "Chairs1":
				setScreen(screenList.get(9));
				currentScreen = 9;
				break;
			case "Hall":
				setScreen(screenList.get(10));
				currentScreen = 10;
				break;
			case "Chairs2":
				setScreen(screenList.get(11));
				currentScreen = 11;
				break;
			case "HallTo3":
				setScreen(screenList.get(12));
				currentScreen = 12;
				break;
			case "RoomWithNotebook":
				setScreen(screenList.get(13));
				currentScreen = 13;
				break;
		}
	}
	
	public void render() {
		super.render();
		if(State != "MENU" && State != "PAUSE" && State != "DOWNLOAD" && State != "SAVE") {
			batch.begin();
			back.draw();
			batch.end();
		}
	}
	
	public void dispose() {
		backgroundMusic.dispose();
		guitarMusic.dispose();
		batch.dispose();
		font.dispose();
		for(int i = 0; i < screenList.size(); i++)
			screenList.get(i).dispose();
		for(int i = 0; i < itemList.size(); i++)
			itemList.get(i).dispose();
		player.dispose();
		back.dispose();
	}
	
	public void getItems() {
		itemList.clear();
		for(int i = 0; i < screenList.size(); i++)
			for(int j = 0; screenList.get(i).getItems() != null && j < screenList.get(i).getItems().size(); j++)
				if(!itemList.contains(screenList.get(i).getItems().get(j)))
					itemList.add(screenList.get(i).getItems().get(j));
	}
	
	public void downloadItems() {
		for(int i = 0; i < itemList.size(); i++)
			itemList.get(i).download();
	}
	
	public void download() {
		backgroundMusic.play();
		isSaved = preferences.getBoolean(downloadMenu + "Saved");
		if(isSaved) {
			for(int i = 0; i < screenList.size(); i++)
				screenList.get(i).download();
			player.download();
			downloadItems();
			State = preferences.getString(downloadMenu + "State");
			changeScreen();
			player.setScreen(player.playerRectangle.x);
		}
	}
	
	public void save() {
		isSaved = true;
		for(int i = 0; i < screenList.size(); i++)
			screenList.get(i).save();
		player.save();
		for(int i = 0; i < itemList.size(); i++)
			itemList.get(i).save();
		preferences.putBoolean(downloadMenu + "Saved", isSaved);
		preferences.putString(downloadMenu + "State", pauseScreen);
		preferences.flush();
	}
	
	public void reset() {
		backgroundMusic.play();
		State = "Ladder";
		changeScreen();
		player.setScreen(800/2 + player.playerRectangle.width/2);
		currentScreen = 1;
		for(int i = 0; i < screenList.size(); i++)
			screenList.get(i).reset();
		player.reset();
		getItems();
	}
	
	@SuppressWarnings("deprecation")
	public void exit() {
		dispose();
		System.runFinalizersOnExit(true);
		System.exit(0);
	}
}
