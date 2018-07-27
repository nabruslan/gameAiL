package player;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.AiL;
import com.mygdx.game.Animator;
import com.mygdx.game.screens.Screens;
import com.mygdx.game.screens.changeScreen;
import items.Bag;
import items.Items;
import npc.DialogWithNPC;
import npc.NPC;
import npc.ShowMessage;

public class Player {//�����

	public final AiL game;//������ ������ ����
	public Screens screen;//������� �����
	public Camera camera;//������
	public ArrayList<Items> itemList; //������ ��������� �����
	public int currentItem;//�������, ������� ���� ����� ��� ������������
	public changeScreen changeLeft, changeRight;//������ ��������� ������
	public ArrayList<NPC> npcList;//��������� �������� �����
	public int currentNPC;
	
	public Bag bag;//�����	
	Texture playerImage; //����������� ������
	public Rectangle playerRectangle;//��� ������������� ������
	int speed; //�������� ������������
	public boolean move;//���� ������	
	public boolean moveToNPC;//���� � ���������	
	public boolean moveToItem;//���� � ��������
	public boolean moveToChange;//���� � ��������
	public boolean talking;//������������� �� � ������ ������
	public boolean usingItem;
	public boolean showMessage;
	public Vector3 touchPosition;//���������� �������
	public DialogWithNPC dialogWindow;//���������� ���� �����
	ShowMessage message;//���� ���������
	Preferences preferences;//����������
	Animator walkRight;
	Animator walkLeft;
	Animator messageAnimation;
	
	/*�����: ��������� ������, ������ 3308
	 * �����, ����������� ������*/
	
	public Player(final AiL game) {//�����������		
		this.game = game;//������������� ������� ������ ����
		
		playerImage = new Texture(Gdx.files.internal("Ruslan.png"));//�������� �����������
		walkRight = new Animator(4, 2, "RuslanWalkAnimationRight.png", game);
		walkLeft = new Animator(4, 2, "RuslanWalkAnimationLeft.png", game);
		messageAnimation = new Animator(4, 1, "ruslanFacepalm.png", game);
		
		//������������� ������
		playerRectangle = new Rectangle();
		playerRectangle.width = 100;
		playerRectangle.height = 320;
		playerRectangle.x = 800/2 - playerRectangle.width*2;
		playerRectangle.y = 0;	
		
		speed = 200;//������������� �������� ������������	
		bag = new Bag(this);//������������� ������� ������ �����	
		move = false;//�� ��������� �����
		moveToNPC = false;
		moveToItem = false;
		moveToChange = false;	
		talking = false; //�� ��������� �� � ��� �� �������
		usingItem = false;//�� ���������� �������
		showMessage = false;//�� ���������� ���������
		
		touchPosition = new Vector3();//������������� ��������� �������		
		itemList = new ArrayList<Items>(); //������������� ��������� �����
		npcList = new ArrayList<NPC>();
		currentItem = 0;//�������, ������� ���� �����
		currentNPC = 0;

		message = new ShowMessage(game);//������������� ���������
		preferences = Gdx.app.getPreferences("Player");//��������� ���������
	}
	
	public void setScreen(float x) {//������ ��������� ��� ��������� �����
		//������ ����������
		playerRectangle.width = 100;
		playerRectangle.height = 320;
		playerRectangle.x = x;
		playerRectangle.y = 0;	
		
		screen = game.screenList.get(game.currentScreen);//������������� ������� �����	
		camera = screen.getCamera();//������ �������� ������	
		npcList = screen.getNPC();//������������� ���������� ��������� �����
		changeLeft = screen.getChangeLeft();
		changeRight = screen.getChangeRight();
		itemList = screen.getItems(); //�������� ������ ���������
		
		move = false;//�� ��������� �����
		moveToNPC = false;
		moveToItem = false;
		moveToChange = false;
		talking = false; //�� ��������� �� �������
		
		dialogWindow = screen.getDialogWindow();
	}
	
	public void draw() {//��������� ���������	
		if(!move && !moveToNPC && !moveToItem && !moveToChange) {
			if(!showMessage)
				game.batch.draw(playerImage, playerRectangle.x, playerRectangle.y,
								playerRectangle.width, playerRectangle.height); //������ ������
			else
				messageAnimation.draw(playerRectangle.x, playerRectangle.y,
								playerRectangle.width, playerRectangle.height, false);
		}
		else if(playerRectangle.x > touchPosition.x - playerRectangle.width/2 + 10
				&& playerRectangle.x > 10) {
			walkLeft.draw(playerRectangle.x, playerRectangle.y - 10, playerRectangle.width+50, playerRectangle.height + 10, true);
		}
		else if(playerRectangle.x < touchPosition.x - playerRectangle.width/2 - 10
				&& playerRectangle.x < 800 - playerRectangle.width) {
			walkRight.draw(playerRectangle.x, playerRectangle.y - 10, playerRectangle.width+40, playerRectangle.height + 10, true);
		}
	}
	
	
	public void update() {//���������� ���������
		if(Gdx.input.justTouched()) {
			touchPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0); //�������� ���������� �������
			camera.unproject(touchPosition);
			if(placeToTouch(touchPosition) && !talking && !move && !moveToItem 
					&& !moveToNPC && !moveToNPC && !usingItem && !bag.opened) {
				showMessage = true;
				messageAnimation.reset();
				message.setMessage("�: �� ���� � ���� ������, ����������");
			}
			else move = actWithWorld(touchPosition);
		}
		if(move /*&& !moveToNPC && !moveToItem && !moveToChange*/) move(); //��� � �����
		if(moveToNPC) moveToNPC(); //��� � ���������
		if(moveToItem) moveToItem(); //��� � ��������
		if(moveToChange) moveToChange(); //��� � ��������
	}
	
	
	//��������-------------------------------------------------------------------------------------------
	public void move() {//������� ��������
		if(move && playerRectangle.x < touchPosition.x - playerRectangle.width/2 - 10
				&& playerRectangle.x < 800 - playerRectangle.width)
			playerRectangle.x += speed*Gdx.graphics.getDeltaTime();
		else if(move && playerRectangle.x > touchPosition.x - playerRectangle.width/2 + 10
				&& playerRectangle.x > 10)
			playerRectangle.x -= speed*Gdx.graphics.getDeltaTime();
		else move = false;
	}
	
	public void moveToNPC() {//������� �������� � ���
		if(moveToNPC && playerRectangle.x < touchPosition.x - playerRectangle.width/2 - 10
				&& playerRectangle.x < 800 - playerRectangle.width)
			playerRectangle.x += speed*Gdx.graphics.getDeltaTime();
		else if(moveToNPC && playerRectangle.x > touchPosition.x - playerRectangle.width/2 + 10
				&& playerRectangle.x > 10)
			playerRectangle.x -= speed*Gdx.graphics.getDeltaTime();
		else {
			if(usingItem)
				npcList.get(currentNPC).update(itemList.get(currentItem));
			else 
				npcList.get(currentNPC).update();
			moveToNPC = false;
			talking = true; //������� � ���
		}
	}
	
	public void moveToItem() {//������� �������� � ��������
		if(moveToItem && playerRectangle.x < touchPosition.x - playerRectangle.width/2 - 10
				&& playerRectangle.x < 800 - playerRectangle.width)
			playerRectangle.x += speed*Gdx.graphics.getDeltaTime();
		else if(moveToItem && playerRectangle.x > touchPosition.x - playerRectangle.width/2 + 10
				&& playerRectangle.x > 10)
			playerRectangle.x -= speed*Gdx.graphics.getDeltaTime();
		else {
			if(!itemList.isEmpty())
				itemList.get(currentItem).update();
			currentItem = 0;
			moveToItem = false;
		}
	}
	
	public void moveToChange() {//������� �������� � ��������
		if(moveToChange && playerRectangle.x < touchPosition.x - playerRectangle.width/2 - 10
				&& playerRectangle.x < 800 - playerRectangle.width)
			playerRectangle.x += speed*Gdx.graphics.getDeltaTime();
		else if(moveToChange && playerRectangle.x > touchPosition.x - playerRectangle.width/2 + 10
				&& playerRectangle.x > 10)
			playerRectangle.x -= speed*Gdx.graphics.getDeltaTime();
		else {
			if(touchPosition.x < 400)
				screen.changeScreen(false);
			else
				screen.changeScreen(true);
			moveToChange = false;
		}
	}
	
	
	//��������������---------------------------------------------------------------------------------------------------
	public boolean actWithWorld(Vector3 touchPos) {//�������� �������
		if(bag.opened) {//���� ����� �������
			//�� ������ �� ������
			moveToItem = false;
			moveToChange = false;
			moveToNPC = false;
			bag.update(touchPos);//��������� ��������� �����
			return false;
		}
		hideMessages();//������ ��� �������
		if(dialogWindow != null) {//���� ���� ���������� ����
			if(screen.getDialog() && dialogWindow.placeToTouch(touchPos)) {//���� ������� ������ � �� ���� ������
				dialogWindow.update(touchPos);//��������� ������
				moveToItem = false;
				moveToChange = false;
				moveToNPC = false;
				return false;
			}
		}
		if(game.back.placeToTouch(touchPosition)) {//���� ������ �����
			game.pauseScreen = game.State;
			game.State = "PAUSE";//��� � ����
			game.changeScreen();
		}
		if(screen.getDialog()) {//���� ��� ������
			if(!npcList.isEmpty())
				for(int i = 0; i < npcList.size(); i++)
					npcList.get(i).setTalking();
			screen.setDialog(false);
			talking = false;
		}
		if(npcList!= null && !npcList.isEmpty()) {//���� ���� ���
			for(int i = 0; i < npcList.size(); i++) {
				if(npcList.get(i).placeToTouch(touchPos)) {//���� �� ���� ������
					//��� � ���� � ���������������
					touchPosition.set(npcList.get(i).getRectangle().x - playerRectangle.width/2, Gdx.input.getY(), 0);
					currentNPC = i;
					moveToItem = false;
					moveToChange = false;
					moveToNPC = true;
					//if(usingItem) npcList.get(i).setGetItem();//���� ������������ �������, ����� ���
					return false;
				}
				else if(usingItem) {//���� �� �� ���������, �� ������������ �������
					//������� ������������� � ����� �� �����
					moveToItem = false;
					moveToChange = false;
					moveToNPC = false;
					unuseItem();
					return false;
				}
			}
		}
		else if(usingItem) {//���� ��� ���������, ������� ������������� ��������
			moveToItem = false;
			moveToChange = false;
			moveToNPC = false;
			unuseItem();
			return false;
		}
		if(bag.placeToTouch(touchPos) && !bag.opened) {//���� ����� �������, ������ �� ������ �����
			moveToItem = false;
			moveToChange = false;
			moveToNPC = false;
			bag.update(touchPos);//��������� ��������� �����
			return false;
		}
		if(itemList != null && !itemList.isEmpty()) {//���� �� ����� ���� ��������
			for(int i = 0; i < itemList.size(); i++)//��������� ���
				if(itemList.get(i).placeToTouch(touchPos)) {//���� ������ �� �����-��
					//��� � ����
					touchPosition.set(itemList.get(i).getRectangle().x - playerRectangle.width/2, Gdx.input.getY(), 0);
					currentItem = i;
					moveToItem = true;
					moveToChange = false;
					moveToNPC = false;
					return false;
				}
		}
		if(changeLeft != null) {
			if(changeLeft.placeToTouch(touchPos)) {
				touchPosition.set(0, Gdx.input.getY(), 0);
				moveToItem = false;
				moveToChange = true;
				moveToNPC = false;
				return false;
			}
		}
		if(changeRight != null) {
			if(changeRight.placeToTouch(touchPos)) {
				touchPosition.set(800, Gdx.input.getY(), 0);
				moveToItem = false;
				moveToChange = true;
				moveToNPC = false;
				return false;
			}
		}
		moveToItem = false;
		moveToChange = false;
		moveToNPC = false;
		return true;
	}
	
	public boolean placeToTouch(Vector3 touchPos) {//�������� ������� ���������
		if(touchPos.x >= playerRectangle.x + playerRectangle.width/4 && 
				touchPos.x <= playerRectangle.x + playerRectangle.width*3/4 &&
				touchPos.y >= playerRectangle.y + playerRectangle.height*2/3 &&
				touchPos.y <= playerRectangle.y + playerRectangle.height)
				return true;
		return false;
	}
	
	public void useItem(Items item) {//���������� �������
		usingItem = true;
		bag.close();
		item.setCoords(0, 400 - (int)item.getRectangle().height);
		itemList.add(item);
		currentItem =  itemList.indexOf(item);
		itemList.get(currentItem).update();
	}
	
	public void unuseItem() {//������� �������������
		usingItem = false;
		itemList.get(currentItem).update();
		currentItem = 0;
	}
	
	public void hideMessages() {//������ ��������� � �������
		if(npcList != null && !npcList.isEmpty())
			for(int i = 0; i < npcList.size(); i++)
				if(npcList.get(i).getMessageState())
					npcList.get(i).setMessage();
		showMessage = false;
		talking = false;
	}
	
	public void showMessage() {//�������� ���������
		if(showMessage)
			message.draw();
	}
	
	//����������/��������/������������======================================================
	public void save() {
		preferences.putFloat(game.downloadMenu + "x", playerRectangle.x);
		preferences.flush();
	}
	
	public void download() {
		bag.itemList.clear();
		playerRectangle.x = preferences.getFloat(game.downloadMenu + "x");
	}
	
	public void reset() {
		bag.itemList.clear();
	}
	
	public void setCoords(float x, float y) {
		playerRectangle.x = x;
		playerRectangle.y = y;
	}
	
	public void setCoords(float x, float y, float width, float height) {
		playerRectangle.x = x;
		playerRectangle.y = y;
		playerRectangle.width = width;
		playerRectangle.height = height;
	}
	
	public void dispose() {
		playerImage.dispose();
		bag.dispose();
		walkRight.dispose();
		walkLeft.dispose();
	}
}
