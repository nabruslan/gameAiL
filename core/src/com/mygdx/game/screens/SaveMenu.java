package com.mygdx.game.screens;

import java.util.ArrayList;
import java.util.Date;

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

public class SaveMenu implements Screens {
	final AiL game;//������ ������ ����
	OrthographicCamera camera;//������
	button DOWNLOAD;
	button BACK;
	Vector3 touchPosition;
	ArrayList <chooseTable> SAVE;
	public boolean choose;
	public int chosen;
	Texture background;
	Preferences preferences;

	public SaveMenu(final AiL game) {//�����������
		this.game = game;//������������� ������� ������ ����
		
		background = new Texture(Gdx.files.internal("LETI.png"));
		//������������� ������
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 400);
		DOWNLOAD = new button(game);
		DOWNLOAD.setText("���������");
		DOWNLOAD.setCoords(800 - DOWNLOAD.dialogRectangle.width, 0);
		BACK = new button(game);
		BACK.setText("�����");
		BACK.setCoords(0, 0);
		SAVE = new ArrayList<chooseTable>();
		for(int i = 0; i < 5; i++) {
			SAVE.add(new chooseTable(game));
			SAVE.get(i).setText("���������� " + Integer.toString(i + 1));
			SAVE.get(i).setCoords(350 - i*60);
		}
		touchPosition = new Vector3();
		
		preferences = Gdx.app.getPreferences("Game");
		
		download();
	}
	
	public void render(float delta) {
		//���������� ������ � ������
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
		if(Gdx.input.justTouched()) {//���������� ����
			touchPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0); //�������� ���������� �������
			camera.unproject(touchPosition);
			if(BACK.placeToTouch(touchPosition)) {
				choose = false;
				for(int i = 0; i < 5; i++)
					if(SAVE.get(i).choose)
						SAVE.get(i).update(touchPosition);
				game.State = "PAUSE";
				game.changeScreen();
			}
			else if(DOWNLOAD.placeToTouch(touchPosition)) {
				if(choose) {
					Date date = new Date();
					SAVE.get(chosen).setText(date.toString());
					game.downloadMenu = SAVE.get(chosen).text;
					choose = false;
					for(int i = 0; i < 5; i++)
						if(SAVE.get(i).choose)
							SAVE.get(i).update(touchPosition);
					save();
					game.screenList.get(2).download();
					game.save();
					game.State = "PAUSE";
					game.changeScreen();
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
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {
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
		return null;
	}

	@Override
	public ArrayList<Items> getItems() {
		return null;
	}

	@Override
	public Camera getCamera() {
		return camera;
	}

	@Override
	public changeScreen getChangeLeft() {
		return null;
	}

	@Override
	public changeScreen getChangeRight() {
		return null;
	}

	@Override
	public void changeScreen(boolean change) {
	}

	public void setDialog(boolean state) {	
	}

	@Override
	public DialogWithNPC getDialogWindow() {
		return null;
	}

	@Override
	public boolean getDialog() {
		return false;
	}

	@Override
	public void save() {
		for(int i = 0; i < SAVE.size(); i++) {
			preferences.putString("SAVE" + i, SAVE.get(i).text);
		}
		preferences.flush();
	}

	@Override
	public void download() {
		for(int i = 0; i < SAVE.size(); i++) {
			SAVE.get(i).setText(preferences.getString("SAVE" + i));
		}
	}

	@Override
	public void reset() {
	}
}
