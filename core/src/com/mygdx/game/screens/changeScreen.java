package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.AiL;
//������� ���-����

/*�����: ��������� ������
 * ����� ����, ����������� ������ ��������
 * ����� ���������*/

public class changeScreen {
	
	final AiL game;//������ ����
	Texture changeIcon;//������
	public Rectangle changeRectangle;//�������������
	
	public changeScreen(final AiL game, int state) {
		this.game = game;//������������� ������� ����
		//�������� �����������
		if(state == 0)
			changeIcon = new Texture(Gdx.files.internal("changeLeft.png"));
		else changeIcon = new Texture(Gdx.files.internal("changeRight.png"));
		//������������� �������������
		changeRectangle = new Rectangle();
		changeRectangle.width = 50;
		changeRectangle.height = 50;
		if(state != 0) {
			changeRectangle.x = 800 - changeRectangle.width;
			changeRectangle.y = 200 + changeRectangle.height/2;
		}
		else {
			changeRectangle.x = 0;
			changeRectangle.y = 200 + changeRectangle.height/2;
		}
			
	}
	
	public void draw() {//���������
		game.batch.draw(changeIcon, changeRectangle.x, changeRectangle.y, changeRectangle.width, changeRectangle.height);
	}
	
	public void update() {}//����������
	
	public boolean placeToTouch(Vector3 touchPos) {//�������� �������
		if(touchPos.x >= changeRectangle.x && 
				touchPos.x <= changeRectangle.x + changeRectangle.width &&
				touchPos.y >= changeRectangle.y &&
				touchPos.y <= changeRectangle.y + changeRectangle.height)
				return true;
		return false;
	}
	
	public void dispose() {
		changeIcon.dispose();
	}
}
