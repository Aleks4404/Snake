import java.awt.*;
import java.util.ArrayList;

public class Snake 
{
	//enum направлений движения
	public static final int DIR_POUSE = 0;
	public static final int DIR_UP = 1;
	public static final int DIR_RIGHT = 2;
	public static final int DIR_DOWN = 3;
	public static final int DIR_LEFT = 4;

	//тело змейки (коллекция точек)
	private ArrayList<Point> body = new ArrayList<Point>();
	public ArrayList<Point> getBody() 
	{
		return body;
	}
	
	
	//размер тела змейки
	private int bodySize;

	//направление движения змейки
	private int direction = DIR_POUSE;
	public int getDirection() 
	{
		return direction;
	}
	public void setDirection(int direction) 
	{
		this.direction = direction;
	}

	
	public Snake(int x0, int y0, int sz) 
	{
		bodySize = sz;
		int x = x0 * sz + 2;
		int y = y0 * sz + 2;
		for (int i = 0; i < 3; i++) 
		{
			body.add(new Point(x, y));
		}
	}

	//отрисовка змейки
	public void paint(Graphics2D g2) 
	{
		for (Point p : body) 
		{
			g2.setColor(Color.green);
			g2.fillArc(p.x, p.y, bodySize, bodySize, 0, 360);
			g2.setColor(Color.magenta);
			g2.drawArc(p.x, p.y, bodySize, bodySize, 0, 360);
		}
    
		Point p = body.get(body.size() - 1);
		g2.setColor(Color.black);
		g2.fillArc(p.x + bodySize / 2 - 2, p.y + bodySize / 2 - 2, 4, 4, 0, 360);
		g2.setColor(Color.white);
		g2.fillArc(p.x + bodySize / 2 - 1, p.y + bodySize / 2 - 1, 2, 2, 0, 360);
	}

	private boolean getSelfIntersection(Point p)
	{
		for(int i=0; i < body.size(); i++)
		{
			if(body.get(i).x == p.x && body.get(i).y == p.y)
				return true;
			
		}
		
		return false;
	}
	
	//передвижение змейки
 	public Point move()
 	{
 		Point last = body.get(body.size() - 1);
 		Point pp = last;
 		
 		switch (direction) 
 		{
 			case DIR_POUSE:
 				break;
 			case DIR_UP:
 				body.remove(0);
 				pp = new Point(last.x, last.y - bodySize);
 				
 				if(getSelfIntersection(pp))
 					return new Point(-1000, -1000);
 				
 				body.add(pp);
 				break;
 			case DIR_RIGHT:
 				body.remove(0);
 				pp = new Point(last.x + bodySize, last.y);
 				
 				if(getSelfIntersection(pp))
 					return new Point(-1000, -1000);
 				
 				body.add(pp);
 				break;
 			case DIR_DOWN:
 				body.remove(0);
 				pp = new Point(last.x, last.y + bodySize);
 				
 				if(getSelfIntersection(pp))
 					return new Point(-1000, -1000);
 				
 				body.add(pp);
 				break;
 			case DIR_LEFT:
 				body.remove(0);
 				pp = new Point(last.x - bodySize, last.y);
 				
 				if(getSelfIntersection(pp))
 					return new Point(-1000, -1000);
 				
 				body.add(pp);
 				break;
 		}
 		return pp;
 	}

 	//скорость движения змейки
 	public int getSpeed() 
 	{
 		return 250;
 	}

 	//увеличение тела змейки на одну единицу
 	public void expand() 
 	{
 		body.add(0, new Point(body.get(0).x, body.get(0).y));
 	}
}