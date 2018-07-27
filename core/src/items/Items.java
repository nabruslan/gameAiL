package items;


import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
//����� ����� ���������

/*�����: ��������� ������, ������ 3308
 * ��������� ���������*/

public interface Items {
	//��������� �� �����
	public void draw();
	//��������� � �����
	public void drawInBag();
	//����������
	public void update();
	//�������� �������
	public boolean placeToTouch(Vector3 touchPos);
	//��������� ���������
	public void setCoords(int x, int y);
	public void setCoords(int x, int y, int width, int height);
	//�������� �����������
	public Rectangle getRectangle();
	//�������� ����������
	public String getInfo();
	public String getName();
	public boolean getIsAvailable();
	public void setIsAvailable();
	public void download();
	public void save();
	public void dispose();
}
