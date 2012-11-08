
public class Charge2D {
	private Vector2D _vector;
	private double _val;
	
	public Charge2D(double x, double y, double val)
	{
		_vector=new Vector2D(x,y);
		_val=val;
	}
	
	public Charge2D(Vector2D v, double val)
	{
		_vector=v.clone();
		_val=val;
	}
	
	public double getVal()
	{
		return _val;
	}
	
	public Vector2D getVector()
	{
		return _vector;
	}
}
