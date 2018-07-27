package npc;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.AiL;

/*�����: ��������� ������, ������ 3308
 * ����� ����, ����������� ���������� ����
 * � ���������� �����������*/

public class DialogWithNPC {
	final AiL game; //������ ������ ����
	Texture backGround; //��� �������
	public Rectangle dialogRectangle; //������������� ����������� ����
	public ArrayList<String> answers; //������
	public ArrayList<String> questions; //�������
	public int currentText; //���������
	
	public DialogWithNPC(final AiL game) {//�����������
		this.game = game; //������������� ������� ������ ����
		//�������� �����������
		backGround = new Texture(Gdx.files.internal("dialogBackground.png"));
		//������������� �������������
		dialogRectangle = new Rectangle();
		dialogRectangle.width = 800;
		dialogRectangle.height = 150;
		dialogRectangle.x = 0;
		dialogRectangle.y = 0;
		//������������� �������
		answers = new ArrayList<String>();
		questions = new ArrayList<String>();
		currentText = 0;
	}
	
	public void setTexts(ArrayList<String> answers, ArrayList<String> questions) {
		this.answers = answers;
		this.questions = questions;
	}
	
	public void draw() {
		game.batch.draw(backGround, dialogRectangle.x, dialogRectangle.y, 
						dialogRectangle.width, dialogRectangle.height); //��������� ���� �������
		if(currentText == 0) { //���� ��������� ���������, ��
			game.font.draw(game.batch, answers.get(0), 
							dialogRectangle.x + 20, dialogRectangle.y + dialogRectangle.height - 10); // �������������� �����
			game.font.draw(game.batch, questions.get(0), 
					dialogRectangle.x + 20, dialogRectangle.y + dialogRectangle.height - 40); //������� 1
			game.font.draw(game.batch, questions.get(1), 
					dialogRectangle.x + 20, dialogRectangle.y + dialogRectangle.height - 80); //������� 2
			game.font.draw(game.batch, questions.get(2), 
					dialogRectangle.x + 20, dialogRectangle.y + dialogRectangle.height - 120); //������� 3
		}
		else { //�����
			write(answers.get(currentText), dialogRectangle.x + 20, dialogRectangle.y + dialogRectangle.height);
		}
	}
	
	public void update(Vector3 touchPos) {
		if(placeToTouch(touchPos)) {
			if(currentText == 0) {
				if(placeToTouchVar1(touchPos)) currentText = 1;
				if(placeToTouchVar2(touchPos)) currentText = 2;
				if(placeToTouchVar3(touchPos)) currentText = 3;
			}
			else currentText = 0;
		}
		else {
			currentText = 0;
			game.screenList.get(game.currentScreen).setDialog(false);
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
				touchPos.y >= dialogRectangle.y + dialogRectangle.height - 70 &&
				touchPos.y <= dialogRectangle.y + dialogRectangle.height - 40)
				return true;
		return false;
	}
	
	public boolean placeToTouchVar2(Vector3 touchPos) {//�������� �������
		if(touchPos.x >= dialogRectangle.x + 20 && 
				touchPos.x <= dialogRectangle.x + dialogRectangle.width &&
				touchPos.y >= dialogRectangle.y + dialogRectangle.height - 110 &&
				touchPos.y <= dialogRectangle.y + dialogRectangle.height - 80)
				return true;
		return false;
	}
	
	public boolean placeToTouchVar3(Vector3 touchPos) {//�������� �������
		if(touchPos.x >= dialogRectangle.x + 20 && 
				touchPos.x <= dialogRectangle.x + dialogRectangle.width &&
				touchPos.y >= dialogRectangle.y + dialogRectangle.height - 150 &&
				touchPos.y <= dialogRectangle.y + dialogRectangle.height - 120)
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
