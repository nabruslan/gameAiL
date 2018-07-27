package items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.AiL;

/*�����: ��������� ������, ������ 3308
 * ����� ����, ����������� ���������� ���� � ����������*/

public class UseItem {
	final AiL game; //������ ������ ����
	Texture backGround; //��� �������
	public Rectangle dialogRectangle; //������������� ����������� ����
	String textInformation;//���������� � ��������
	boolean info;//���������� ���������� ��� ��������
	
	public UseItem(final AiL game) {//�����������
		this.game = game; //������������� ������� ������ ����
		
		//�������� �����������
		backGround = new Texture(Gdx.files.internal("dialogBackground.png"));
		
		//������������� �������������
		dialogRectangle = new Rectangle();
		dialogRectangle.width = 170;
		dialogRectangle.height = 100;
		
		info = false;//�� ���������� ����������
	}
	
	void setText(String textInformation) {//������������� �����
		this.textInformation = textInformation;
	}
	
	void setCoords(Rectangle item) {//������ ����������
		dialogRectangle.x = item.x + item.width;
		dialogRectangle.y = item.y - item.height;
	}
	
	public void setParametres(String textInformation, Rectangle item) {//���������� ���������
		setText(textInformation);//���������� �����
		setCoords(item);//���������� ����������
	}
	
	public void draw() {//���������
		game.batch.draw(backGround, dialogRectangle.x, dialogRectangle.y, 
						dialogRectangle.width, dialogRectangle.height); //��������� ���� �������
		if(!info) { //���� ��������� ���������, ��
			game.font.draw(game.batch, "������������", 
							dialogRectangle.x + 20, dialogRectangle.y + dialogRectangle.height - 10); // �������������� �����
			game.font.draw(game.batch, "����������", 
					dialogRectangle.x + 20, dialogRectangle.y + dialogRectangle.height - 50); //������� 1
		}
		else { //�����
			write(textInformation, dialogRectangle.x + 20, dialogRectangle.y + dialogRectangle.height);
		}
	}
	
	public void update(Items item, Vector3 touchPos) {//����������
		if(placeToTouch(touchPos)) {
			if(!info) {
				if(placeToTouchVar1(touchPos)) game.player.useItem(item);
				if(placeToTouchVar2(touchPos)) info = true;
			}
			else info = false;
		}
		else {
			info = false;
			game.player.bag.useItem = false;
			game.player.bag.currentItem = 0;
		}
	}
	
	public boolean placeToTouch(Vector3 touchPos) {//�������� �������
		if(touchPos.x >= dialogRectangle.x && 
				touchPos.x <= dialogRectangle.x + dialogRectangle.width &&
				touchPos.y >= dialogRectangle.y &&
				touchPos.y <= dialogRectangle.y + dialogRectangle.height)
				return true;
		return false;
	}
	
	public boolean placeToTouchVar1(Vector3 touchPos) {//�������� �������
		if(touchPos.x >= dialogRectangle.x + 20 && 
				touchPos.x <= dialogRectangle.x + dialogRectangle.width &&
				touchPos.y >= dialogRectangle.y + dialogRectangle.height - 40 &&
				touchPos.y <= dialogRectangle.y + dialogRectangle.height - 10)
				return true;
		return false;
	}
	
	public boolean placeToTouchVar2(Vector3 touchPos) {//�������� �������
		if(touchPos.x >= dialogRectangle.x + 20 && 
				touchPos.x <= dialogRectangle.x + dialogRectangle.width &&
				touchPos.y >= dialogRectangle.y + dialogRectangle.height - 80 &&
				touchPos.y <= dialogRectangle.y + dialogRectangle.height - 50)
				return true;
		return false;
	}
	
	public void dispose() {
		backGround.dispose();
	}
	
	public void write(String text, float x, float y) {
		float width = 0;
		int i = 0;
		float fontWidth = game.font.getSpaceWidth();
		while(width < text.length()*fontWidth) {
			if(dialogRectangle.width/2 + width < text.length()*fontWidth) {
				game.font.draw(game.batch, text.substring((int)(width/fontWidth), 
						(int) ((dialogRectangle.width/2 + width)/fontWidth)), 
						x, y - 10 - 25*i);
			}
			else {
				game.font.draw(game.batch, text.substring((int)(width/fontWidth), text.length()), 
						x, y - 10 - 25*i);
			}
			i++;
			width = width + dialogRectangle.width/2;
		}
	}
}
