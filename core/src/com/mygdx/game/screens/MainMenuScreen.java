package com.mygdx.game.screens;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.AiL;
import items.Items;
import npc.DialogWithNPC;
import npc.NPC;

public class MainMenuScreen implements Screens {
	final AiL game;//объект класса игры
	OrthographicCamera camera;//камеры
	button START;
	button DOWNLOAD;
	button EXIT;
	Vector3 touchPosition;
	Texture background;

	public MainMenuScreen(final AiL game) {//конструктор
		this.game = game;//инициализация объекта класса игры
		
		background = new Texture(Gdx.files.internal("LETI.png"));
		//инициализация камеры
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 400);
		int y = (int) (400/2 + (50*3 + 10*2)/2);
		START = new button(game);
		START.setText("Начать");
		START.setCoords(y);
		y = y - 60;
		DOWNLOAD = new button(game);
		DOWNLOAD.setText("Загрузить");
		DOWNLOAD.setCoords(y);
		y = y - 60;
		EXIT = new button(game);
		EXIT.setText("Выход");
		EXIT.setCoords(y);
		touchPosition = new Vector3();
	}
	
	public void render(float delta) {
		//подготовка экрана и камеры
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);	
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
		game.batch.draw(background, 0, 0, 800, 400);
		//отрисовка меню
		START.draw();
		DOWNLOAD.draw();
		EXIT.draw();
		game.batch.end();
		if(Gdx.input.justTouched()) {//обновление меню
			touchPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0); //получаем координаты нажатия
			camera.unproject(touchPosition);
				if(START.placeToTouch(touchPosition))
					game.reset();
				else if(DOWNLOAD.placeToTouch(touchPosition)) {
					game.State = "DOWNLOAD";
					game.changeScreen();
				}
				else if(EXIT.placeToTouch(touchPosition))
					game.exit();
		}
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		background.dispose();
		START.dispose();
		DOWNLOAD.dispose();
		EXIT.dispose();
	}

	@Override
	public ArrayList<NPC> getNPC() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Items> getItems() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Camera getCamera() {
		return camera;
	}

	@Override
	public changeScreen getChangeLeft() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public changeScreen getChangeRight() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void changeScreen(boolean change) {
		// TODO Auto-generated method stub
		
	}

	public void setDialog(boolean state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DialogWithNPC getDialogWindow() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getDialog() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void download() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}
}
