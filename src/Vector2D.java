import java.io.Serializable;


public class Vector2D implements Serializable{
	private double _x,_y;
	
	public Vector2D(double x,double y)
	{
		_x=x;
		_y=y;
	}
	
	public double getX()
	{
		return _x;
	}
	
	public double getY()
	{
		return _y;
	}
	
	public void add(Vector2D v)
	{
		_x+=v.getX();
		_y+=v.getY();
	}
	
	public Vector2D getSum(Vector2D v)
	{
		return new Vector2D(_x+v.getX(),_y+v.getY());
	}
	
	public double getAbs()
	{
		return Math.sqrt(_x*_x+_y*_y);
	}
	
	public void scale(double s)
	{
		_x*=s;
		_y*=s;
	}
	
	public Vector2D getUnitVector()
	{
		return new Vector2D(_x/getAbs(),_y/getAbs());
	}
	
	public Vector2D getVectorTo(Vector2D v)
	{
		return new Vector2D(v.getX()-_x, v.getY()-_y);
	}
	
	public Vector2D clone()
	{
		return new Vector2D(_x,_y);
	}
}
