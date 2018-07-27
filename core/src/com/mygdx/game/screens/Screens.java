package com.mygdx.game.screens;

import java.util.ArrayList;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import items.Items;
import npc.DialogWithNPC;
import npc.NPC;

/*�����: ��������� ������, ������ 3308
 * ��������� ������� �������*/

public interface Screens extends Screen {
	public ArrayList<NPC> getNPC();//�������� ��� �����
	public ArrayList<Items> getItems();//�������� ������ ��������� �����
	public Camera getCamera();//�������� ������ �����
	public changeScreen getChangeLeft();//�������� ����� �������
	public changeScreen getChangeRight();//�������� ������ �������
	public void changeScreen(boolean change);//�������
	public void setDialog(boolean state);//�������� ��������� ���������
	public DialogWithNPC getDialogWindow();//�������� ���������� ����
	public boolean getDialog();//�������� ��������� �������
	public void save();
	public void download();
	public void reset();
}
