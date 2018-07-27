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
	final AiL game; //������ ������ ����
	public Player player;//�����		
	Texture background; //��� �����
	public OrthographicCamera camera; //������ �����
	public ArrayList<Items> itemList;//�������� �����
	public ArrayList<NPC> npcList;
	public changeScreen changeRight;//������� �� ����� �����
	DialogWithNPC dialogWindow;//���������� ����
	public boolean dialog; //��� �� ������
	public Door door;
	
	public screenHallTo3(final AiL game) {//����������� �����
		
		this.game = game;//������������� ������� ������ ����		
		
		background = new Texture(Gdx.files.internal("HallTo3.png"));//�������� �����������
		//������������� ������
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 400);	
		
		player = game.player;//������������� ������
		//������������� ������ ���������
		itemList = new ArrayList<Items>();//������ ���������
		changeRight = new changeScreen(game, 1);//�������
		dialog = false; //�� ��������� ����� �� �������
		dialogWindow = new DialogWithNPC(game);//���������� ����
		
		npcList = new ArrayList<NPC>();
		npcList.add(new npcYanina(game, this));
		npcList.get(0).setCoords(250, 0, 80, 300);
		
		door = new Door(game);
		door.setCoords(800/2, 55);
	}
	
	
	public void render(float delta) {//���������� ������
		//���������� ������ � ������
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();	
		game.batch.setProjectionMatrix(camera.combined);
		//��������� �����
		draw();
		update();
	}
	
	//��������� �����
	public void draw() {
		game.batch.begin();
		game.batch.draw(background, 0, 0, 800, 400);//��������� ����
		changeRight.draw();//��������� ��������
		if(npcList.get(0).getItemState())
			door.draw();
		for(int i = 0; i < itemList.size(); i++)//��������� ���������
			itemList.get(i).draw();
		for(int i = 0; i < npcList.size(); i++)
			npcList.get(i).draw();//��������� ���������� ���������
		player.draw();//��������� ������
		player.bag.draw();//��������� �����
		if(dialog) dialogWindow.draw(); //���������� ����
		//��������� ���������
		for(int i = 0; i < npcList.size(); i++)
			npcList.get(i).showMessage();
		player.showMessage();//��������� ���������
		game.batch.end();
	}
	
	//���������� �����
	public void update() {
		//���������� ������
		player.update();
		/*if(npcList.get(0).getItemState() && Gdx.input.justTouched()) {
			Vector3 touchPosition = new Vector3();
			touchPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0); //�������� ���������� �������
			camera.unproject(touchPosition);
			if(door.placeToTouch(touchPosition))
				intoDoor();
		}*/
		if(npcList.get(0).getItemState() && door.placeToTouch(game.player.touchPosition) && !game.player.move 
			&& !game.player.showMessage) {
			Vector3 touchPosition = new Vector3();
			touchPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0); //�������� ���������� �������
			camera.unproject(touchPosition);
			if(door.placeToTouch(touchPosition))
				intoDoor();
		}
	}
	
	//�������� ������� �����===========================================
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
	
	//�������
	public void changeScreen(boolean change) {
		if(change) {
			game.State = "Chairs2";
			game.changeScreen();
			player.setScreen(player.playerRectangle.width/2);
		}
	}
	//�������� ��� ������ �����
	public void show() {}
	//�������� ��� ��������� �������
	public void resize(int width, int height) {}
	//�������� ��� "��������" �����
	public void hide() {}
	//�������� ��� ����� �����
	public void pause() {}	
	//�������� ��� ������������� �����
	public void resume() {}	
	//�������� ��� �������� �����
	public void dispose() {
		background.dispose();
		changeRight.dispose();
		dialogWindow.dispose();
		door.dispose();
	}


	@Override
	public void setDialog(boolean state) {// ������ ��������� �������
		dialog = state;
	}

	//��������� ����������===========================================================================
	public DialogWithNPC getDialogWindow() {
		return dialogWindow;
	}


	@Override
	public boolean getDialog() {
		return dialog;
	}

	//����������/��������/�����======================================================================
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
		//������������� ���������� ���������
		for(int i = 0; i < npcList.size(); i++)
			npcList.get(i).reset();
		//������������� ������
		player.reset();
		//������������� ������ ���������
		itemList.clear();
		dialog = false; //�� ��������� ����� �� �������
	}
	
}
