import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Area;
import java.util.Random;
 
import javax.swing.JApplet;

 
public class SnakeGame extends JApplet implements Runnable
{
	//�������
	private static final int wallStep = 10;
 
	//������ ����� �������������� 3 ���� ��� �������� ����������
	private Shape walls1 = new Polygon();
 	private Shape walls2 = new Polygon();
 	private Shape walls3 = new Polygon();
 
	//������
 	private Snake snake = new Snake(2, 2, wallStep);
 
	private Point food = new Point();
 
	//����� ���-�� ������ (���������� �� ������ ��� ������ �� ���������)
	private int points = 0;
 
	private static final Random rand = new Random();
 
	//��� ��������� ������� �����������
	private Image offScreenImage;
	private Graphics2D offScreenGraphics;
	private Thread animationThread = null;
	
	public void init() 
	{
		//������� ����������� ����� � ������������� ������ �� ��� Graphics
		offScreenImage = createImage(getWidth(), getHeight());
		offScreenGraphics = (Graphics2D)offScreenImage.getGraphics();
		
		addKeyListener(new KeyAdapter() 
		 {
			 public void keyPressed(KeyEvent ev) 
			 {
				 processKey(ev);
			 }
		 });
		
		//����� ���������� ������������ ������ 
		animationThread = new Thread(this);
		animationThread.start();
	}
	
	//����� ���������� Runnable
	public void run()
	{
		Thread th = Thread.currentThread();
				
		while(animationThread == th)
		{
			//���������� ��������� �������� ����
			gameCycle();
			
			//���������� ����������� �� ������
			repaint();
			
			//�������� (������� �� �������� �������� ������)
			try 
			{
				Thread.sleep(500 - snake.getSpeed());
			} 
			catch (InterruptedException e) 
			{}
		}
	
	}
	
	//�������������
	public void update(Graphics g)
	{
		paint(g);
	}
	
	public SnakeGame() 
	{
		Dimension d = getLevel();
		putFood();
 
		setPreferredSize(d);
		setMinimumSize(d);
		setMaximumSize(d);
		setSize(d);
 
	}
 
	//��������� "������"
	private void putFood() 
	{
		int x = 5;
		int y = 5;
 
		while (walls2.contains(x, y)) 
		{
			x = wallStep * rand.nextInt(40) + 2;
			y = wallStep * rand.nextInt(40) + 2;
		}
		food.setLocation(x, y);
	}
 
	public Snake getSnake() 
	{
		return snake;
	}
 
	//����� �������� ���� (�������� �������, 1 - �����������)
	private Dimension getLevel() 
	{
				
		final String levelSubStrings[] = {
				"1111111111111111111111111111111111111111",
				"1000000000000000000000000000000000000001",
				"1000000000000000000000000000000000000001",
				"1000000000000000000000001111111111100001",
				"1000000000000000000000000000000000100001",
				"1000000000000010000000000000000000100001",
				"1000000000000010000000000000000000100001",
				"1000000000000011111000000000000000100001",
				"1000000000000000001000000000000000100001",
				"1000111110000000001000001111111111100001",
				"1000100010000000001000000000000000000001",
				"1000100010000000001000000000000000000001",
				"1000000000000000001000000000000000000001",
				"1000000000000000001000000000000000000001",
				"1000000011111111111000000000000000111111",
				"1000000000000000000000000000000000000001",
				"1000000000000000000000000000000000000001",
				"1000000000000000000000000000000000000001",
				"1000000000000000000000000000000000000001",
				"1000000000000000001000000001000000000001",
				"1000011111000000001000000111000000000001",
				"1000010000000000001000000001000000000001",
				"1000010000000000001000000001000000000001",
				"1000010000000000001000000001000000000001",
				"1000010000000000001000000001000000000001",
				"1000010000000000001000000001000000000001",
				"1000010000000000001000000001000000000001",
				"1000010000000000001000000001111110000001",
				"1000010000000000000000000000000010000001",
				"1000010000000000000000000000000010000001",
				"1000010000001111111000000000000010000001",
				"1000010000000000000000000000000010000001",
				"1000010000000000000000000000000010000001",
				"1000011110011111111111111100011110000001",
				"1000000000000000000000000000000000000001",
				"1000000000000000000000000000000000000001",
				"1000000000000000000000000000000000000001",
				"1000000000000000000000000000000000000001",
				"1000000000000000000000000000000000000001",
				"1111111111111111111111111111111111111111"};
		
		int minX = 500, maxX = 0, minY = 500, maxY = 0;
		Area w1 = new Area();
		Area w2 = new Area();
		Area w3 = new Area();
 
		String line = null; //not declared within while loop
		 
		for (int y = 0; y<levelSubStrings.length; y++) 
		{
			line = levelSubStrings[y];
			
			for (int x = 0; x < line.length(); x++) 
			{
				if (line.charAt(x) == '1') 
				{
					minX = Math.min(minX, x * wallStep);
					maxX = Math.max(maxX, (x + 1) * wallStep + 4);
					minY = Math.min(minY, y * wallStep);
					maxY = Math.max(maxY, (y + 1) * wallStep + 4);

					w1.add(new Area(new Rectangle(x * wallStep, y * wallStep, wallStep, wallStep)));
					w2.add(new Area(new Rectangle(x * wallStep + 2, y * wallStep + 2, wallStep, wallStep)));
					w3.add(new Area(new Rectangle(x * wallStep + 4, y * wallStep + 4, wallStep, wallStep)));
				}
			}
		}
		
		walls1 = w1;
		walls2 = w2;
		walls3 = w3;
 
		Dimension d = new Dimension(maxX - minX, maxY - minY);
		return d;
	}
 
	public void gameCycle() 
	{
		
		
		if (snake.getDirection() != Snake.DIR_POUSE) 
		{
			setMessage(null);
		}
    
		Point p = snake.move();
		if(p.x == -1000 && p.y == -1000)//���������������
		{
			points -= 10;//������� 10 ������
			snake.setDirection(Snake.DIR_POUSE);
			setMessage("��-���!");
		}
		
		if (p.x == food.x && p.y == food.y) 
		{
			points++;
			snake.expand();
			putFood();
		}
		
		if (walls2.contains(p)/*������������ � ������������*/) 
		{
			if (snake.getDirection() != Snake.DIR_POUSE) 
			{
				points -= 10;//������� 10 ������
			}
			snake.setDirection(Snake.DIR_POUSE);
			setMessage("��-���!");
		}
		
		
		
	}
 
	//���������
	private String message = null;
 	private void setMessage(String msg) 
	{
		message = msg;
	}
 	
 	//��������� �� ����������� ������ �������� ���� � ���������
 	private void setImage()
	{
				
		offScreenGraphics.setColor(Color.white);
		offScreenGraphics.fillRect(0, 0, getWidth(), getHeight());
 
		offScreenGraphics.setColor(Color.black);
		offScreenGraphics.fill(walls3);
		offScreenGraphics.setColor(Color.lightGray);
		offScreenGraphics.fill(walls1);
		offScreenGraphics.setColor(Color.gray);
		offScreenGraphics.fill(walls2);
		
		offScreenGraphics.setColor(Color.orange);
		offScreenGraphics.fillArc(food.x, food.y, wallStep, wallStep, 0, 360);
		offScreenGraphics.setColor(Color.red);
		offScreenGraphics.drawArc(food.x, food.y, wallStep, wallStep, 0, 360);
		
    	offScreenGraphics.setColor(Color.yellow);
		offScreenGraphics.drawString("�����: " + points, 2, 10);

		if (message != null) 
		{
			offScreenGraphics.setColor(Color.red);
			offScreenGraphics.fillRect(150, 100, 100, 30);
			offScreenGraphics.setColor(Color.black);
			offScreenGraphics.drawRect(150, 100, 100, 30);
			offScreenGraphics.drawString(message, 160, 120);
		}
		
	}
	
	//��������� �������
	public void paint(Graphics g) 
	{
		offScreenGraphics.clearRect(0, 0, getWidth(), getHeight());
		
		setImage();
		snake = getSnake();
		
		snake.paint(offScreenGraphics);
		
		g.drawImage(offScreenImage, 0 , 0 , this);
	}
	
	//��������� ������� �������
	public void processKey(KeyEvent ev) 
	{
		snake = getSnake();
    
		switch (ev.getKeyCode()) 
		{
			case KeyEvent.VK_RIGHT:
				snake.setDirection(Snake.DIR_RIGHT);
				break;
			case KeyEvent.VK_LEFT:
				snake.setDirection(Snake.DIR_LEFT);
				break;
			case KeyEvent.VK_DOWN:
				snake.setDirection(Snake.DIR_DOWN);
				break;
			case KeyEvent.VK_UP:
				snake.setDirection(Snake.DIR_UP);
				break;
			case KeyEvent.VK_ESCAPE:
				System.exit(0);
				break;
		}
	}
 
}