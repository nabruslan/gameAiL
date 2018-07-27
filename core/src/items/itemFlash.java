package items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.screens.Screens;

import player.Player;
//������� "������"
public class itemFlash implements Items{
	Player player;//�����
	Screens screen; //�����, �� ������� �������
	Texture itemIcon;//����������� ��������
	Rectangle itemRectangle;//������������� ��������
	String info; //���������� � ��������
	String name; //�������� ��������
	boolean isAvailable; //�������� �� �� �����
	boolean inBag;//���� �������������� �����
	Preferences preferences; //��� ����������
	
	public itemFlash(Player player, Screens screen) {//�����������	
		this.player = player;//������������� ������
		this.screen = screen;//�������� �����
		
		itemIcon = new Texture(Gdx.files.internal("flashDrive.png"));//�������� �����������
		
		//������������� �������������
		itemRectangle = new Rectangle();
		
		inBag = false;//�� ��������� �� �����, � �� � �����
		isAvailable = true;//�� ��������� ��������
		
		info = "�������� ��� ����";//������������� ����������
		name = "itemFlash";//������������� ��������
		
		preferences = Gdx.app.getPreferences("Items");//����������
	}
	
	public void draw() {//��������� �� �����
		if(!inBag && !player.bag.opened)//���� �� ����� � ����� �������
			player.game.batch.draw(itemIcon, itemRectangle.x, 
										itemRectangle.y, itemRectangle.width, itemRectangle.height);
	}
	
	public void drawInBag() {//��������� � �����
		if(inBag)
			player.game.batch.draw(itemIcon, itemRectangle.x, 
					itemRectangle.y, itemRectangle.width, itemRectangle.height);//���� � �����
	}
	
	public void update() {//����������
			if(!inBag) { //���� �� � �����
				inBag = true; //����� � �����
				if(!player.bag.itemList.contains(this))
					player.bag.itemList.add(this); //����� � �����
				player.itemList.remove(this); //������� �� �����
			}
			else inBag = false; //����� �� � �����
	}
	
	public boolean placeToTouch(Vector3 touchPos) {//�������� �������
		if(touchPos.x >= itemRectangle.x && 
				touchPos.x <= itemRectangle.x + itemRectangle.width &&
				touchPos.y >= itemRectangle.y &&
				touchPos.y <= itemRectangle.y + itemRectangle.height)
				return true;
		return false;
	}
	
	public void setCoords(int x, int y) {//������ ����������
		itemRectangle.x = x;
		itemRectangle.y = y;
	}
	
	public void setCoords(int x, int y, int width, int height) {//������ ����������
		itemRectangle.x = x;
		itemRectangle.y = y;
		itemRectangle.width = width;
		itemRectangle.height = height;
	}
	
	//��������� ����������================================================================
	@Override
	public void setIsAvailable() {//������ ���� �����������
		isAvailable = !isAvailable;
	}
	
	//��������� ����������======================================================
	@Override
	public Rectangle getRectangle() {//�������� �������������
		return itemRectangle;
	}

	@Override
	public String getInfo() {//�������� ����������
		return info;
	}

	@Override
	public String getName() {//�������� ��������
		return name;
	}

	@Override
	public boolean getIsAvailable() {//�������� ���� �����������
		return isAvailable;
	}
	
	//���������/���������============================================================
	@Override
	public void download() {
		inBag = preferences.getBoolean(player.game.downloadMenu + name +"inBag");//� ����� ��
		isAvailable = preferences.getBoolean(player.game.downloadMenu + name +"isAvailable");//�������� �� �������
		itemRectangle.x = preferences.getFloat(player.game.downloadMenu + name +"X");
		itemRectangle.y = preferences.getFloat(player.game.downloadMenu + name +"Y");
		itemRectangle.width = preferences.getFloat(player.game.downloadMenu + name +"Width");
		itemRectangle.height = preferences.getFloat(player.game.downloadMenu + name +"Height");
		if(isAvailable && inBag) {//���� �������� � � �����
			if(!player.bag.itemList.contains(this))
				player.bag.itemList.add(this);//��������� � �����
			if(screen.getItems().contains(this))
				screen.getItems().remove(this);
		}
		else if(isAvailable && !inBag) {
			if(player.bag.itemList.contains(this))
				player.bag.itemList.remove(this);//��������� � �����
			if(!screen.getItems().contains(this)) {
				screen.getItems().add(this);
			}
		}
		else if(!isAvailable) {
			if(player.bag.itemList.contains(this))
				player.bag.itemList.remove(this);//��������� � �����
			if(screen.getItems().contains(this))
				screen.getItems().remove(this);
		}
	}

	@Override
	public void save() {
		preferences.putBoolean(player.game.downloadMenu + name +"inBag", inBag);//� ����� ��
		preferences.putBoolean(player.game.downloadMenu + name +"isAvailable", isAvailable);//�������� ��
		preferences.putFloat(player.game.downloadMenu + name +"X", itemRectangle.x);
		preferences.putFloat(player.game.downloadMenu + name +"Y", itemRectangle.y);
		preferences.putFloat(player.game.downloadMenu + name +"Width", itemRectangle.width);
		preferences.putFloat(player.game.downloadMenu + name +"Height", itemRectangle.height);
		preferences.flush();//���������
	}
	
	public void dispose() {
		itemIcon.dispose();
	}
}
