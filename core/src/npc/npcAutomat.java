package npc;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.AiL;
import com.mygdx.game.screens.Screens;

import items.Items;

public class npcAutomat implements NPC {
	final AiL game; //������ ������ ����
	final Screens screen;//������ �����
	Texture npcImage; //����������� ���������
	Rectangle npcRectangle; //��� ������������� ���������
	ArrayList<String> answers; //������ ���������
	ArrayList<String> questions; //������� ���������
	boolean getItem;//������� �� �������
	boolean showMessage;//���� ���������
	boolean talking; //���� ������
	ShowMessage message;//���� ���������
	Preferences preferences;//����������
	String name;
	
	public npcAutomat(final AiL game, final Screens screen) {//�����������	
		this.game = game;//������������� ������� ������ ����	
		this.screen = screen;//������������� �����
		
		npcImage = new Texture(Gdx.files.internal("automat.png"));	//�������� �����������	
		
		talking = false;//������������� ����� ������
		getItem = false;//������� �� �������
		showMessage = false;//��������� �� ������������
		
		//������������� ���������
		npcRectangle = new Rectangle();
		
		answers = new ArrayList<String>(); //������������� ������
		questions = new ArrayList<String>(); //������������� ������
		
		message = new ShowMessage(game);//������������� ���� ���������
		setText();//������������� ������
		
		preferences = Gdx.app.getPreferences("NPC");//����������
		
		name = "npcAutomat";
	}	
	
	public void draw() {//��������� ���
		game.batch.draw(npcImage, npcRectangle.x, npcRectangle.y,
					npcRectangle.width, npcRectangle.height); //�������� ���������
	}
		
	public void update(Items item) {//�������� ��� � ���������
		getItem(item);//���� ������� �������, �� ��������� ���������
	}
	
	public void update() {//�������� ��� ��� ��������
		if(!talking && !getItem) {//���� �� ������� � �� ������� �������
			talking = true;//�������
			game.player.screen.getDialogWindow().setTexts(answers, questions);
			screen.setDialog(true);//���������� ������
		}
		else if(!showMessage && getItem) {//���� ������� ������� � ��� ���������
			showMessage = true;//���������� ���������
		}
	}
	
	public boolean placeToTouch(Vector3 touchPos) {//�������� �������
		if(touchPos.x >= npcRectangle.x && 
				touchPos.x <= npcRectangle.x + npcRectangle.width &&
				touchPos.y >= npcRectangle.y &&
				touchPos.y <= npcRectangle.y + npcRectangle.height)
				return true;
		return false;
	}
	
	//��������� ����������===================================================
	@Override
	public Rectangle getRectangle() {
		return npcRectangle;
	}

	@Override
	public ArrayList<String> getAnswers() {
		return answers;
	}

	@Override
	public ArrayList<String> getQuestions() {
		// TODO Auto-generated method stub
		return questions;
	}
	
	public boolean getMessageState() {
		return showMessage;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean getItemState() {
		return getItem;
	}

	//��������� ����������=========================================================
	@Override
	public void setTalking() {
		talking = !talking;
	}
	
	@Override
	public void setText() {
		answers.add("�������: <����>");
		answers.add("�������: <��, ������ �� ������>");
		answers.add("�������: <��� ����� ��� � ��������>");
		answers.add("�������: <����...>");
		questions.add("1. <���������>");
		questions.add("2. <���������>");
		questions.add("3. ��� ����, �������?");
		message.setMessage("�������: <����, ���������!>");
	}
	
	public void setGetItem() {
		getItem = !getItem;
	}
	
	public void setMessage() {
		showMessage = !showMessage;
	}
	
	public void setCoords(float x, float y) {
		npcRectangle.x = x;
		npcRectangle.y = y;
	}
	
	public void setCoords(float x, float y, float width, float height) {
		npcRectangle.x = x;
		npcRectangle.y = y;
		npcRectangle.width = width;
		npcRectangle.height = height;
	}

	@Override
	public void getItem(Items item) {//��������� ��������
		if(item.getName() == "itemCoin") {//���� ������ �������
			getItem = true;
			game.player.unuseItem();//����� ������������� �������� � ������
			item.setIsAvailable();//������� ������� �����������
			game.player.bag.itemList.remove(item);//������� �� �����
			showMessage = true;//�������� ���������
			message.setMessage("�������: <����, ���������!>");
			ArrayList<Items> itemList = screen.getItems();
			for(int i = 0; i < itemList.size(); i++)
				if(itemList.get(i).getName() == "itemChocolate") {
					itemList.get(i).update();
				}
		}
		else {//�����
			message.setMessage("�������: ��� ��� �� �����...");//���������� ���������
			showMessage = true;//�������� ���������
			game.player.unuseItem();//����� �������������
			getItem = false;//������� �� �������
		}
	}
	
	public void showMessage() {//���������� ���������
		if(showMessage)
			message.draw();
	}
	
	//����������/��������/�����====================================================
	@Override
	public void save() {
		preferences.putBoolean(game.downloadMenu + name + "getItem", getItem);
		preferences.flush();
	}

	@Override
	public void download() {
		getItem = preferences.getBoolean(game.downloadMenu + name + "getItem");
	}

	@Override
	public void reset() {
		talking = false;//������������� ����� ������
		getItem = false;
		showMessage = false;
	}
	
	public void dispose() {
		npcImage.dispose();
	}
}
