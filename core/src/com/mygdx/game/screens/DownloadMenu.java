package com.mygdx.game.screens;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.AiL;
import items.Items;
import npc.DialogWithNPC;
import npc.NPC;

public class DownloadMenu implements Screens {
	final AiL game;//объект класса игры
	OrthographicCamera camera;//камеры
	button DOWNLOAD;
	button BACK;
	Vector3 touchPosition;
	ArrayList <chooseTable> SAVE;
	public boolean choose;
	public int chosen;
	Texture background;
	Preferences preferences;

	public DownloadMenu(final AiL game) {//конструктор
		this.game = game;//инициализация объекта класса игры
		
		background = new Texture(Gdx.files.internal("LETI.png"));
		//инициализация камеры
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 400);
		DOWNLOAD = new button(game);
		DOWNLOAD.setText("Загрузить");
		DOWNLOAD.setCoords(800 - DOWNLOAD.dialogRectangle.width, 0);
		BACK = new button(game);
		BACK.setText("Назад");
		BACK.setCoords(0, 0);
		SAVE = new ArrayList<chooseTable>();
		for(int i = 0; i < 5; i++) {
			SAVE.add(new chooseTable(game));
			SAVE.get(i).setText("Сохранение " + Integer.toString(i + 1));
			SAVE.get(i).setCoords(350 - i*60);
		}
		touchPosition = new Vector3();
		
		preferences = Gdx.app.getPreferences("Game");
		download();
	}
	
	public void render(float delta) {
		//подготовка экрана и камеры
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);	
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
		game.batch.draw(background, 0, 0, 800, 400);
		DOWNLOAD.draw();
		BACK.draw();
		for(int i = 0; i < 5; i++)
			SAVE.get(i).draw();
		game.batch.end();
		if(Gdx.input.justTouched()) {//обновление меню
			touchPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0); //получаем координаты нажатия
			camera.unproject(touchPosition);
			if(BACK.placeToTouch(touchPosition)) {
				choose = false;
				for(int i = 0; i < 5; i++)
					if(SAVE.get(i).choose)
						SAVE.get(i).update(touchPosition);
				game.State = "MENU";
				game.changeScreen();
			}
			else if(DOWNLOAD.placeToTouch(touchPosition)) {
				if(choose) {
					game.downloadMenu = SAVE.get(chosen).text;
					choose = false;
					for(int i = 0; i < 5; i++)
						if(SAVE.get(i).choose)
							SAVE.get(i).update(touchPosition);
					game.download();
				}
			}
			else {
				for(int i = 0; i < 5; i++)
					if(SAVE.get(i).placeToTouch(touchPosition)) {
						if(!SAVE.get(i).choose && !choose) {
							choose = true;
							SAVE.get(i).update(touchPosition);
							chosen = i;
						}
						else if(SAVE.get(i).choose) {
							choose = false;
							SAVE.get(i).update(touchPosition);
						}
						else if(!SAVE.get(i).choose && choose) {
							SAVE.get(chosen).update(touchPosition);
							SAVE.get(i).update(touchPosition);
							chosen = i;
						}
					}
			}
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
		DOWNLOAD.dispose();
		BACK.dispose();
		for(int i = 0; i < SAVE.size(); i++)
			SAVE.get(i).dispose();
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
		for(int i = 0; i < SAVE.size(); i++) {
			SAVE.get(i).setText(preferences.getString("SAVE" + i));
		}
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}
}
