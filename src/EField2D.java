import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class EField2D implements Serializable{

	private static final long serialVersionUID = 1L;
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
	
	public MagnitudeCharge[][] getNormalizedField() throws InterruptedException
	{
		MagnitudeCharge[][] result=new MagnitudeCharge[_width][_height];
		int processors = Runtime.getRuntime().availableProcessors()*2;
		NormalizerThread[] threads=new NormalizerThread[processors];
		for(int y=0; y < processors; y++) {
		  threads[y] = new NormalizerThread(_field,result,y, processors,_charges,_scale, _maxPotential, _minPotential, max);
		  threads[y].start();
		}
		for (int i=0;i<processors;i++)
		{
			threads[i].join();
		}
		return result;
	}
	
	public Charge2D[] getCharges()
	{
		return _charges;
	}
	
	public void save(File f) throws IOException
	{
		// Write to disk with FileOutputStream
		FileOutputStream f_out = new FileOutputStream(f);

		// Write object with ObjectOutputStream
		ObjectOutputStream obj_out = new ObjectOutputStream(f_out);

		// Write object out to disk
		obj_out.writeObject(this);
		obj_out.close();
	}
	
	public int getWidth()
	{
		return _width;
	}
	
	public int getHeight()
	{
		return _height;
	}
}
