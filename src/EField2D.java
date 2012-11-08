public class EField2D {
	private Charge2D[] _charges;
	private int _width, _height;
	public static final double EPSILON0=8.854*10e-12;
	private Charge2D[][] _field;
	private double max, min;
	private double _scale;
	private double _maxPotential, _minPotential;
	
	
	public EField2D(int height, int width, Charge2D[] charges, double scale)
	{
		_width=width;
		_height=height;
		_charges=charges;
		_field=new Charge2D[_width][_height];
		max=0;
		min=0;
		_maxPotential=0;
		_minPotential=0;
		_scale=scale;
		try {
			calcField();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void calcField() throws InterruptedException
	{
		int processors = Runtime.getRuntime().availableProcessors()*2;
		FieldCalculatorThread[] threads=new FieldCalculatorThread[processors];
		for(int y=0; y < processors; y++) {
		  threads[y] = new FieldCalculatorThread(_field,y, processors,_charges,_scale);
		  threads[y].start();
		}
		for (int i=0;i<processors;i++)
		{
			threads[i].join();
		}
		max=0;
		min=0;
		_maxPotential=0;
		_minPotential=0;
		for (int i=0;i<processors;i++)
		{
			if (max<threads[i].getMax())
			{
				max=threads[i].getMax();
			}
			if (min>threads[i].getMin())
			{
				min=threads[i].getMin();
			}
			if (_maxPotential<threads[i].getMaxPotential())
			{
				_maxPotential=threads[i].getMaxPotential();
			}
			if (_minPotential>threads[i].getMinPotential())
			{
				_minPotential=threads[i].getMinPotential();
			}
		}
		System.out.println("Threads finished");
	}
	
	public Charge2D[][] getField()
	{
		return _field;
	}
	
	public MagnitudeCharge[][] getNormalizedField()
	{
		MagnitudeCharge[][] result=new MagnitudeCharge[_width][_height];
		for (int x=0;x<_width;x++)
		{
			for (int y=0;y<_height;y++)
			{
				double normalizedCharge=0;
				if (_field[x][y].getVal()>0)
				{
					normalizedCharge=_field[x][y].getVal()/_maxPotential;
				}
				else
				{
					normalizedCharge=_field[x][y].getVal()/Math.abs(_minPotential);
				}
				double magnitude=_field[x][y].getVector().getAbs();
				if (magnitude>Float.NEGATIVE_INFINITY&&magnitude<Float.POSITIVE_INFINITY)
				{
					result[x][y]=new MagnitudeCharge(magnitude/(max/_scale)>1 ? 1 : magnitude/(max/_scale), normalizedCharge);
				}
				else
				{
					if (magnitude<=Float.NEGATIVE_INFINITY)
					{
						result[x][y]=new MagnitudeCharge( -1 , normalizedCharge);
					}
					else
					{
						result[x][y]=new MagnitudeCharge( 1 , normalizedCharge);
					}
				}
			}
		}
		return result;
	}
	
	public Charge2D[] getCharges()
	{
		return _charges;
	}
}
