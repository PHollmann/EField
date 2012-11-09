
public class NormalizerThread extends Thread {
	
	private int _offsetY;
	private int _width,_height;
	private double max;
	private double _scale;
	private int _slice;
	private double _maxPotential, _minPotential;
	private Charge2D[][] _field;
	private MagnitudeCharge[][] _output;
	
	public NormalizerThread(Charge2D[][] field, MagnitudeCharge[][] output, int position, int threadcount, Charge2D[] charges, double scale, double maxPotential, double minPotential, double max)
	{
		_width=field.length;
		_height=field[0].length;
		_field=field;
		_output=output;
		_slice=_height/threadcount;
		this.max=max;
		_maxPotential=maxPotential;
		_minPotential=minPotential;
		_offsetY=_height*position/threadcount;
		_scale=scale;
	}

	public void run()
	{
		for (int y=_offsetY;y<=_offsetY+_slice;y++)
		{
			for (int x=0;x<_width;x++)
			{
				normalizePoint(x,y);
			}
		}
	}
	
	private void normalizePoint(int x, int y)
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
			_output[x][y]=new MagnitudeCharge(magnitude/(max/_scale)>1 ? 1 : magnitude/(max/_scale), normalizedCharge);
		}
		else
		{
			if (magnitude<=Float.NEGATIVE_INFINITY)
			{
				_output[x][y]=new MagnitudeCharge( -1 , normalizedCharge);
			}
			else
			{
				_output[x][y]=new MagnitudeCharge( 1 , normalizedCharge);
			}
		}
	}
}
