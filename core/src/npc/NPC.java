package npc;

import java.util.ArrayList;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import items.Items;

/*�����: ��������� ������, ������ 3308
 * ��������� ��������� ����������*/

public interface NPC {
	//��������� �� �����
	public void draw();
	//��������� � �����
	public void update(Items item);
	public void update();
	//�������� �������
	public boolean placeToTouch(Vector3 touchPos);
	public Rectangle getRectangle();
	public void setTalking();
	public ArrayList<String> getAnswers();
	public ArrayList<String> getQuestions();
	public void setText();
	public void getItem(Items item);
	public void setGetItem();
	public void setMessage();
	public void showMessage();
	public void save();
	public void download();
	public void reset();
	public boolean getMessageState();
	public void setCoords(float x, float y);
	public void setCoords(float x, float y, float width, float height);
	public void dispose();
	public String getName();
	public boolean getItemState();
}
