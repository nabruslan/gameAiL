package com.mygdx.game.screens;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.AiL;
import items.Items;
import items.itemApple;
import items.itemPassword;
import npc.DialogWithNPC;
import npc.NPC;
import npc.npcDana;
import player.Player;

public class screenChairs2 implements Screens {	
	final AiL game; //объект класса игры	
	public Player player;//игрок		
	public ArrayList<NPC> npcList;//неигровой персонаж Дана	
	Texture background; //фон сцены		
	Music backgroundMusic; //фоновая музыка сцены	
	public OrthographicCamera camera; //камера сцены	
	public ArrayList<Items> itemList;//предметы сцены
	public changeScreen changeLeft;//переход
	public changeScreen changeRight;//переход
	public boolean dialog; //идёт ли диалог
	DialogWithNPC dialogWindow;//диалоговое окно
	Preferences preferences;//сохранения
	
	//конструктор сцены
	public screenChairs2(final AiL game) {
		this.game = game;//инициализация объекта класса игры
		//загрузка изображений
		background = new Texture(Gdx.files.internal("Chairs2.png"));	
		//загрузка музыкального сопровождения
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Screen1.mp3"));
		//инициализация неигрового персонажа
		npcList = new ArrayList<NPC>();
		npcList.add(new npcDana(game, this));
		npcList.get(0).setCoords(800/2 - npcList.get(0).getRectangle().width/2, 0, 80, 300);
		//инициализация камеры
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 400);	
		//инициализация игрока
		player = game.player;
		//инициализация списка предметов
		itemList = new ArrayList<Items>();
		itemList.add(new itemApple(player, this));
		itemList.get(0).setCoords(500, 10, 40, 40);
		itemList.add(new itemPassword(player, this));
		
		changeLeft = new changeScreen(game, 0);//переход
		changeRight = new changeScreen(game, 1);//переход
		dialog = false; //по умолчанию никто не говорит
		dialogWindow = new DialogWithNPC(game);//диалоговое окно
		
		preferences = Gdx.app.getPreferences("Screens");//сохранение
	}
	
	//обновление экрана
	public void render(float delta) {
		//подготовка экрана и камеры
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();	
		game.batch.setProjectionMatrix(camera.combined);
		//отрисовка сцены
		draw();
		//обновление сцены
		update();
	}
	
	//отрисовка сцены
	public void draw() {
		game.batch.begin();
		game.batch.draw(background, 0, 0, 800, 400);//отрисовка фона
		for(int i = 0; i < itemList.size(); i++)//отрисовка предметов
			itemList.get(i).draw();
		changeRight.draw();//отрисовка перехода
		changeLeft.draw();//отрисовка перехода
		for(int i = 0; i < npcList.size(); i++)
			npcList.get(i).draw();//отрисовка неигрового персонажа
		player.draw();//отрисовка игрока
		player.bag.draw();//отрисовка сумки
		if(dialog) dialogWindow.draw(); //диалоговое окно
		//отрисовка сообщений
		for(int i = 0; i < npcList.size(); i++)
			npcList.get(i).showMessage();
		player.showMessage();
		game.batch.end();
	}
	
	//обновление сцены
	public void update() {
		player.update();//обновление игрока
	}
	
	//получить данные из сцены
	public ArrayList<NPC> getNPC() {return npcList;}
	public ArrayList<Items> getItems() {return itemList;}
	public Camera getCamera() {return camera;}
	public changeScreen getChangeLeft() {return changeLeft;}
	public changeScreen getChangeRight() {return changeRight;}

	//переход
	public void changeScreen(boolean change) { //переход
		if(!change) {
			game.State = "HallTo3";
			game.changeScreen();
			player.setScreen(800 - player.playerRectangle.width*3/2);
		}
		else {
			game.State = "Hall";
			game.changeScreen();
			player.setScreen(player.playerRectangle.width/2);
		}
	}
	
	//действия при показе сцены
	public void show() {		
		/*backgroundMusic.setLooping(true);//зациливание музыку	
		backgroundMusic.play();//запуск музыки*/
	}
	//действия при изменении размера
	public void resize(int width, int height) {}
	//действия при "прятании" сцены
	public void hide() {}	
	//действия при паузе сцены
	public void pause() {}	
	//действия при возобновлении сцены
	public void resume() {}
	//действия при удалении сцены
	public void dispose() {
		background.dispose();
		backgroundMusic.dispose();
		for(int i = 0; i < npcList.size(); i++)
			npcList.get(i).dispose();
		changeRight.dispose();
		changeLeft.dispose();
		dialogWindow.dispose();
	}

	@Override
	public void setDialog(boolean state) {//установка диалога
		dialog = state;
	}

	//получение параметров=========================================================================
	@Override
	public DialogWithNPC getDialogWindow() {
		return dialogWindow;
	}

	@Override
	public boolean getDialog() {
		return dialog;
	}

	//сохранение/загрузка/перезагрузка=========================================================
	@Override
	public void save() {
		for(int i = 0; i < npcList.size(); i++)
			npcList.get(i).save();
	}

	@Override
	public void download() {
		itemList.clear();
		for(int i = 0; i < npcList.size(); i++)
			npcList.get(i).download();
	}

	@Override
	public void reset() {	
		//инициализация неигрового персонажа
		for(int i = 0; i < npcList.size(); i++)
			npcList.get(i).reset();
		//инициализация игрока
		player.reset();
		//инициализация списка предметов
		itemList.clear();
		itemList.add(new itemApple(player, this));
		itemList.get(0).setCoords(500, 10, 20,20);
		itemList.add(new itemPassword(player, this));
		dialog = false; //по умолчанию никто не говорит
	}	
}
