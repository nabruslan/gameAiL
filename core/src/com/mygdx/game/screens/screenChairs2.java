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
	final AiL game; //������ ������ ����	
	public Player player;//�����		
	public ArrayList<NPC> npcList;//��������� �������� ����	
	Texture background; //��� �����		
	Music backgroundMusic; //������� ������ �����	
	public OrthographicCamera camera; //������ �����	
	public ArrayList<Items> itemList;//�������� �����
	public changeScreen changeLeft;//�������
	public changeScreen changeRight;//�������
	public boolean dialog; //��� �� ������
	DialogWithNPC dialogWindow;//���������� ����
	Preferences preferences;//����������
	
	//����������� �����
	public screenChairs2(final AiL game) {
		this.game = game;//������������� ������� ������ ����
		//�������� �����������
		background = new Texture(Gdx.files.internal("Chairs2.png"));	
		//�������� ������������ �������������
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Screen1.mp3"));
		//������������� ���������� ���������
		npcList = new ArrayList<NPC>();
		npcList.add(new npcDana(game, this));
		npcList.get(0).setCoords(800/2 - npcList.get(0).getRectangle().width/2, 0, 80, 300);
		//������������� ������
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 400);	
		//������������� ������
		player = game.player;
		//������������� ������ ���������
		itemList = new ArrayList<Items>();
		itemList.add(new itemApple(player, this));
		itemList.get(0).setCoords(500, 10, 40, 40);
		itemList.add(new itemPassword(player, this));
		
		changeLeft = new changeScreen(game, 0);//�������
		changeRight = new changeScreen(game, 1);//�������
		dialog = false; //�� ��������� ����� �� �������
		dialogWindow = new DialogWithNPC(game);//���������� ����
		
		preferences = Gdx.app.getPreferences("Screens");//����������
	}
	
	//���������� ������
	public void render(float delta) {
		//���������� ������ � ������
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();	
		game.batch.setProjectionMatrix(camera.combined);
		//��������� �����
		draw();
		//���������� �����
		update();
	}
	
	//��������� �����
	public void draw() {
		game.batch.begin();
		game.batch.draw(background, 0, 0, 800, 400);//��������� ����
		for(int i = 0; i < itemList.size(); i++)//��������� ���������
			itemList.get(i).draw();
		changeRight.draw();//��������� ��������
		changeLeft.draw();//��������� ��������
		for(int i = 0; i < npcList.size(); i++)
			npcList.get(i).draw();//��������� ���������� ���������
		player.draw();//��������� ������
		player.bag.draw();//��������� �����
		if(dialog) dialogWindow.draw(); //���������� ����
		//��������� ���������
		for(int i = 0; i < npcList.size(); i++)
			npcList.get(i).showMessage();
		player.showMessage();
		game.batch.end();
	}
	
	//���������� �����
	public void update() {
		player.update();//���������� ������
	}
	
	//�������� ������ �� �����
	public ArrayList<NPC> getNPC() {return npcList;}
	public ArrayList<Items> getItems() {return itemList;}
	public Camera getCamera() {return camera;}
	public changeScreen getChangeLeft() {return changeLeft;}
	public changeScreen getChangeRight() {return changeRight;}

	//�������
	public void changeScreen(boolean change) { //�������
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
	
	//�������� ��� ������ �����
	public void show() {		
		/*backgroundMusic.setLooping(true);//����������� ������	
		backgroundMusic.play();//������ ������*/
	}
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
		backgroundMusic.dispose();
		for(int i = 0; i < npcList.size(); i++)
			npcList.get(i).dispose();
		changeRight.dispose();
		changeLeft.dispose();
		dialogWindow.dispose();
	}

	@Override
	public void setDialog(boolean state) {//��������� �������
		dialog = state;
	}

	//��������� ����������=========================================================================
	@Override
	public DialogWithNPC getDialogWindow() {
		return dialogWindow;
	}

	@Override
	public boolean getDialog() {
		return dialog;
	}

	//����������/��������/������������=========================================================
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
		itemList.add(new itemApple(player, this));
		itemList.get(0).setCoords(500, 10, 20,20);
		itemList.add(new itemPassword(player, this));
		dialog = false; //�� ��������� ����� �� �������
	}	
}
