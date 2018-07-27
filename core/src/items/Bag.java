package items;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import player.Player;

/*�����: ��������� ������, ������ 3308
 * ����� ����, ����������� ����� (���������) ������*/

//����� ���������
public class Bag {
	final Player player; //������ ������ ������
	Texture bagIcon;//������ �������� ����� ��� ����������
	Texture bagInside;//����������� �������� �����
	public Rectangle bagRectangle;//�������������
	public boolean opened;//���� ��������
	Vector3 touchPosition;//���������� �������
	public Back back;//������ ��������
	public ArrayList<Items> itemList;//������ ���������
	public boolean useItem;//������������ �� �������
	public UseItem itemDialog;//������ ��������
	public int currentItem;//������� �������
	
	public Bag(final Player player) {//�����������
		this.player = player;//������������� ������
		
		//�������� �����������
		bagIcon = new Texture(Gdx.files.internal("bagIcon.png"));
		bagInside = new Texture(Gdx.files.internal("bagInside.png"));	
		opened = false;//�� ��������� �������
		
		//������������� �������������
		bagRectangle = new Rectangle();
		bagRectangle.width = 60;
		bagRectangle.height = 60;	
		bagRectangle.x = 800 - bagRectangle.width;
		bagRectangle.y = 0;
		touchPosition = new Vector3();//���������� �������	
		back = new Back(player.game);//������������� ��������	
		itemList = new ArrayList<Items>();//������������� ������ ���������
		useItem = false;//�� ��������� ������ �� ������������
		itemDialog = new UseItem(player.game);//������������� ������� ���������
		currentItem = 0;//������� �������
	}
	
	public void draw() {//���������
		if(!opened) //���� ������
			//������ ������ ����������
			player.game.batch.draw(bagIcon, bagRectangle.x, bagRectangle.y, bagRectangle.width, bagRectangle.height);
		else { //���� ������
			//������ �������� �����
			player.game.batch.draw(bagInside, 0, 0, 800, 400);
			//������ ������ "�������"
			back.draw();
			//���� ���� ��������, �� ������ ��
			if(!itemList.isEmpty()) {
				int j = 1;
				for(int i = 1; i <= itemList.size(); i++) {
					if(i % 5 == 0) j++;
					itemList.get(i - 1).setCoords(100*i, 400 - 100*j, 50, 50);
					itemList.get(i - 1).drawInBag();
				}
			}
			if(useItem)
				itemDialog.draw();
		}
	}
	
	public void update(Vector3 touchPos) {//���������� �����
		if(!opened) //���� �������
			opened = true; //���������
		else {
			if(back.placeToTouch(touchPos))//���� ������ "�����"
				close();//���������
			if(!useItem) {//���� ��� ������� � ���������
				if(!itemList.isEmpty())//���� ���� ��������
					for(int i = 0; i < itemList.size(); i++)//������� ��� ��������
						if(itemList.get(i).placeToTouch(touchPos)) {//���� �� �����-�� ������
							itemDialog.setParametres(itemList.get(i).getInfo(), itemList.get(i).getRectangle());//������������� ��������� �������
							useItem = true;//"��������" � ���������
							currentItem = i;//������� �������
						}
			}
			else itemDialog.update(itemList.get(currentItem), touchPos);//��������� ������ � ����������
		}
	}
	
	public boolean placeToTouch(Vector3 touchPos) {//�������� �������
		if(touchPos.x >= bagRectangle.x && 
				touchPos.x <= bagRectangle.x + bagRectangle.width &&
				touchPos.y >= bagRectangle.y &&
				touchPos.y <= bagRectangle.y + bagRectangle.height)
				return true;
		return false;
	}
	
	public void close() {//������� �����
		opened = false;
		useItem = false;
	}
	
	public void dispose() {
		bagIcon.dispose();
		bagInside.dispose();
		itemDialog.dispose();
	}
}
