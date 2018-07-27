package com.mygdx.game.screens;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.AiL;

import items.Door;
import items.Items;
import npc.DialogWithNPC;
import npc.NPC;
import npc.npcYanina;
import player.Player;

public class screenHallTo3 implements Screens {
	final AiL game; //объект класса игры
	public Player player;//игрок		
	Texture background; //фон сцены
	public OrthographicCamera camera; //камера сцены
	public ArrayList<Items> itemList;//предметы сцены
	public ArrayList<NPC> npcList;
	public changeScreen changeRight;//переход на новую сцену
	DialogWithNPC dialogWindow;//диалоговое окно
	public boolean dialog; //идёт ли диалог
	public Door door;
	
	public screenHallTo3(final AiL game) {//конструктор сцены
		
		this.game = game;//инициализация объекта класса игры		
		
		background = new Texture(Gdx.files.internal("HallTo3.png"));//загрузка изображений
		//инициализация камеры
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 400);	
		
		player = game.player;//инициализация игрока
		//инициализация списка предметов
		itemList = new ArrayList<Items>();//список предметов
		changeRight = new changeScreen(game, 1);//переход
		dialog = false; //по умолчанию никто не говорит
		dialogWindow = new DialogWithNPC(game);//диалоговое окно
		
		npcList = new ArrayList<NPC>();
		npcList.add(new npcYanina(game, this));
		npcList.get(0).setCoords(250, 0, 80, 300);
		
		door = new Door(game);
		door.setCoords(800/2, 55);
	}
	
	
	public void render(float delta) {//обновление экрана
		//подготовка экрана и камеры
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();	
		game.batch.setProjectionMatrix(camera.combined);
		//отрисовка сцены
		draw();
		update();
	}
	
	//отрисовка сцены
	public void draw() {
		game.batch.begin();
		game.batch.draw(background, 0, 0, 800, 400);//отрисовка фона
		changeRight.draw();//отрисовка перехода
		if(npcList.get(0).getItemState())
			door.draw();
		for(int i = 0; i < itemList.size(); i++)//отрисовка предметов
			itemList.get(i).draw();
		for(int i = 0; i < npcList.size(); i++)
			npcList.get(i).draw();//отрисовка неигрового персонажа
		player.draw();//отрисовка игрока
		player.bag.draw();//отрисовка сумки
		if(dialog) dialogWindow.draw(); //диалоговое окно
		//отрисовка сообщений
		for(int i = 0; i < npcList.size(); i++)
			npcList.get(i).showMessage();
		player.showMessage();//отрисовка сообщения
		game.batch.end();
	}
	
	//обновление сцены
	public void update() {
		//обновление игрока
		player.update();
		/*if(npcList.get(0).getItemState() && Gdx.input.justTouched()) {
			Vector3 touchPosition = new Vector3();
			touchPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0); //получаем координаты нажатия
			camera.unproject(touchPosition);
			if(door.placeToTouch(touchPosition))
				intoDoor();
		}*/
		if(npcList.get(0).getItemState() && door.placeToTouch(game.player.touchPosition) && !game.player.move 
			&& !game.player.showMessage) {
			Vector3 touchPosition = new Vector3();
			touchPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0); //получаем координаты нажатия
			camera.unproject(touchPosition);
			if(door.placeToTouch(touchPosition))
				intoDoor();
		}
	}
	
	//получить объекты сцены===========================================
	public ArrayList<NPC> getNPC() {
		return npcList;
	}
	
	public ArrayList<Items> getItems() {
		return itemList;
	}
	
	public Camera getCamera() {
		return camera;
	}
	
	public changeScreen getChangeLeft() {
		return null;
	}
	
	public changeScreen getChangeRight() {
		return changeRight;
	}
	
	public void intoDoor() {
		game.State = "RoomWithNotebook";
		game.changeScreen();
		player.setScreen(player.playerRectangle.width/2);
	}
	
	//переход
	public void changeScreen(boolean change) {
		if(change) {
			game.State = "Chairs2";
			game.changeScreen();
			player.setScreen(player.playerRectangle.width/2);
		}
	}
	//действия при показе сцены
	public void show() {}
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
		changeRight.dispose();
		dialogWindow.dispose();
		door.dispose();
	}


	@Override
	public void setDialog(boolean state) {// меняем состояние диалога
		dialog = state;
	}

	//получение параметров===========================================================================
	public DialogWithNPC getDialogWindow() {
		return dialogWindow;
	}


	@Override
	public boolean getDialog() {
		return dialog;
	}

	//сохранение/загрузка/ресет======================================================================
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
		dialog = false; //по умолчанию никто не говорит
	}
	
}
