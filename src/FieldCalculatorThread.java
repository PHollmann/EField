
public class FieldCalculatorThread extends Thread {
	private Charge2D[] _charges;
	private int _offsetY;
	private int _width,_height;
	private Charge2D[][] _field;
	private double max, min;
	private double _scale;
	private int _slice;
	private double _maxPotential, _minPotential;
	
	public FieldCalculatorThread(Charge2D[][] field, int position, int threadcount, Charge2D[] charges, double scale)
	{
		_width=field.length;
		_height=field[0].length;
		_charges=charges;
		_field=field;
		_slice=_height/threadcount;
		max=0;
		min=0;
		_maxPotential=0;
		_minPotential=0;
		_offsetY=_height*position/threadcount;
		_scale=scale;
	}
	
	public void run()
	{
		System.out.println(_offsetY+" Running!");
		calcField();
	}
	
	private Charge2D calcPoint(double x, double y)
	{
		Vector2D ep=new Vector2D(0,0);
		Vector2D currentPos=new Vector2D(x, y);
		double charge;
		double potentialSum=0;;
		for (int i=0;i<_charges.length;i++)
		{
			Vector2D v=new Vector2D(_charges[i].getVector().getX(),_charges[i].getVector().getY());
			Vector2D trans=v.getVectorTo(currentPos);
			Vector2D er=trans.getUnitVector();
			charge = _charges[i].getVal()/Math.pow(trans.getAbs(), 2);
			potentialSum+=_charges[i].getVal()/trans.getAbs();
			er.scale(charge);
			ep.add(er);
		}
		ep.scale(1/(4*Math.PI*EField2D.EPSILON0));
		if (!new Double(ep.getAbs()).isInfinite())
		{
			if (ep.getAbs()>max)
			{
				max=ep.getAbs();
			}
			else
			{
				if (ep.getAbs()<min)
				{
					min=ep.getAbs();
				}
			}
		}
		if (!new Double(potentialSum).isInfinite())
		{
			if (potentialSum>_maxPotential)
			{
				_maxPotential=potentialSum;
			}
			else
			{
				if (potentialSum<_minPotential)
				{
					_minPotential=potentialSum;
				}
				else
				{
				}
			}
		}
		return new Charge2D(ep, potentialSum);
	}
	
	private void calcField()
	{
		for (int y=_offsetY;y<=_offsetY+_slice;y++)
		{
			for (int x=0;x<_width;x++)
			{
				_field[x][y]=calcPoint(x/_scale,y/_scale);
			}
		}
	}
	
	public Charge2D[][] getField()
	{
		return _field;
	}
	
	public double getMax()
	{
		return max;
	}
	
	public double getMin()
	{
		return min;
	}
	
	public double getMaxPotential()
	{
		return _maxPotential;
	}
	
	public double getMinPotential()
	{
		return _minPotential;
	}
}
