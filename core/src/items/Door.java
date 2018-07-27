package items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.AiL;
//������� ���-����
public class Door {
	final AiL game;//������ ����
	Texture doorImage;//������
	Rectangle doorRectangle;//�������������
	
	public Door(final AiL game) {	
		this.game = game;//������������� ������� ����
		//�������� �����������
		doorImage = new Texture(Gdx.files.internal("door.png"));
		//������������� �������������
		doorRectangle = new Rectangle();
		doorRectangle.width = 100;
		doorRectangle.height = 280;
	}
	
	public void setCoords(int x, int y) {
		doorRectangle.x = x;
		doorRectangle.y = y;
	}
	
	public void setCoords(int x, int y, int width, int height) {
		doorRectangle.x = x;
		doorRectangle.y = y;
		doorRectangle.width = width;
		doorRectangle.height = height;
	}
	
	public void draw() {//���������
		game.batch.draw(doorImage, doorRectangle.x, doorRectangle.y, doorRectangle.width, doorRectangle.height);
	}
	
	public void update() {}//����������
	
	public boolean placeToTouch(Vector3 touchPos) {//�������� �������
		if(touchPos.x >= doorRectangle.x && 
				touchPos.x <= doorRectangle.x + doorRectangle.width &&
				touchPos.y >= doorRectangle.y &&
				touchPos.y <= doorRectangle.y + doorRectangle.height)
				return true;
		return false;
	}
	
	public void dispose() {
		doorImage.dispose();
	}
}