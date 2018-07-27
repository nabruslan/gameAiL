package npc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.AiL;

/*Автор: Набиуллин Руслан, группа 3308
 * Класс игры, описывающий окно сообщений*/

public class ShowMessage {
	final AiL game; //объект класса игры
	Texture backGround; //фон диалога
	public Rectangle dialogRectangle; //представление диалогового окна
	String message;
	
	public ShowMessage(final AiL game) {//конструктор
		this.game = game; //инициализация объекта класса игры
		//загрузка изображений
		backGround = new Texture(Gdx.files.internal("dialogBackground.png"));
		//инициализация представления
		dialogRectangle = new Rectangle();
		dialogRectangle.width = 800;
		dialogRectangle.height = 150;
		dialogRectangle.x = 0;
		dialogRectangle.y = 0;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public void draw() {
		game.batch.draw(backGround, dialogRectangle.x, dialogRectangle.y, 
						dialogRectangle.width, dialogRectangle.height); //отрисовка фона диалога
		write(message, dialogRectangle.x + 20, dialogRectangle.y + dialogRectangle.height - 10);
	}
	
	public void update(Vector3 touchPos) {
		game.screenList.get(game.currentScreen).setDialog(false);
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
