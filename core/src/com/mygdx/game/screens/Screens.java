package com.mygdx.game.screens;

import java.util.ArrayList;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import items.Items;
import npc.DialogWithNPC;
import npc.NPC;

/*Автор: Набиуллин Руслан, группа 3308
 * Интерфейс игровых локаций*/

public interface Screens extends Screen {
	public ArrayList<NPC> getNPC();//получить НПЦ сцены
	public ArrayList<Items> getItems();//получить список предметов сцены
	public Camera getCamera();//получить камеру сцены
	public changeScreen getChangeLeft();//получить левый переход
	public changeScreen getChangeRight();//получить правый переход
	public void changeScreen(boolean change);//переход
	public void setDialog(boolean state);//изменить состояние разговора
	public DialogWithNPC getDialogWindow();//получить диалоговое окно
	public boolean getDialog();//получить состояние диалога
	public void save();
	public void download();
	public void reset();
}
