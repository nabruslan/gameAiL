package npc;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.AiL;

/*јвтор: Ќабиуллин –услан, группа 3308
 *  ласс игры, описывающий диалоговые окна
 * с неигровыми персонажами*/

public class DialogWithNPC {
	final AiL game; //объект класса игры
	Texture backGround; //фон диалога
	public Rectangle dialogRectangle; //представление диалогового окна
	public ArrayList<String> answers; //ответы
	public ArrayList<String> questions; //вопросы
	public int currentText; //состо€ние
	
	public DialogWithNPC(final AiL game) {//конструктор
		this.game = game; //инициализаци€ объекта класса игры
		//загрузка изображений
		backGround = new Texture(Gdx.files.internal("dialogBackground.png"));
		//инициализаци€ представлени€
		dialogRectangle = new Rectangle();
		dialogRectangle.width = 800;
		dialogRectangle.height = 150;
		dialogRectangle.x = 0;
		dialogRectangle.y = 0;
		//инициализаци€ текстов
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
						dialogRectangle.width, dialogRectangle.height); //отрисовка фона диалога
		if(currentText == 0) { //если начальное состо€ние, то
			game.font.draw(game.batch, answers.get(0), 
							dialogRectangle.x + 20, dialogRectangle.y + dialogRectangle.height - 10); // приветственный текст
			game.font.draw(game.batch, questions.get(0), 
					dialogRectangle.x + 20, dialogRectangle.y + dialogRectangle.height - 40); //вариант 1
			game.font.draw(game.batch, questions.get(1), 
					dialogRectangle.x + 20, dialogRectangle.y + dialogRectangle.height - 80); //вариант 2
			game.font.draw(game.batch, questions.get(2), 
					dialogRectangle.x + 20, dialogRectangle.y + dialogRectangle.height - 120); //вариант 3
		}
		else { //иначе
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
	
	public boolean placeToTouch(Vector3 touchPos) {//проверка области
		if(touchPos.x >= dialogRectangle.x && 
				touchPos.x <= dialogRectangle.x + dialogRectangle.width &&
				touchPos.y >= dialogRectangle.y &&
				touchPos.y <= dialogRectangle.y + dialogRectangle.height)
				return true;
		return false;
	}
	
	public boolean placeToTouchVar1(Vector3 touchPos) {//проверка области
		if(touchPos.x >= dialogRectangle.x + 20 && 
				touchPos.x <= dialogRectangle.x + dialogRectangle.width &&
				touchPos.y >= dialogRectangle.y + dialogRectangle.height - 70 &&
				touchPos.y <= dialogRectangle.y + dialogRectangle.height - 40)
				return true;
		return false;
	}
	
	public boolean placeToTouchVar2(Vector3 touchPos) {//проверка области
		if(touchPos.x >= dialogRectangle.x + 20 && 
				touchPos.x <= dialogRectangle.x + dialogRectangle.width &&
				touchPos.y >= dialogRectangle.y + dialogRectangle.height - 110 &&
				touchPos.y <= dialogRectangle.y + dialogRectangle.height - 80)
				return true;
		return false;
	}
	
	public boolean placeToTouchVar3(Vector3 touchPos) {//проверка области
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
