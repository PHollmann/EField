import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class MainGUI extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6978594654303089785L;
	private EField2D _field;
	private int _width;
	private int _height;
	private int _numCharges;
	private boolean _coloured=false;
	private static Checkbox _cbColour;
	private boolean _drawVectors=false;
	private static Checkbox _cbVectors;
	private boolean _drawField=true;
	private static Checkbox _cbField;
	private Drawing _drawing;

	public MainGUI(int numCharges)
	{
		super("E-Field");
		initBase(numCharges);
		initInteractive();
		this.pack();
		this.setVisible(true);
		generateField();
		render();
	}
	
	private void initInteractive()
	{
		JPanel display=new JPanel();
		display.setLayout(new GridLayout(3,1));
		
		_cbColour = new Checkbox("Draw coloured", _coloured);
		_cbColour.addItemListener(new CheckboxListener());
		
		_cbVectors = new Checkbox("Draw vectors", _drawVectors);
		_cbVectors.addItemListener(new CheckboxListener());
		
		_cbField = new Checkbox("Draw field", _drawField);
		_cbField.addItemListener(new CheckboxListener());
		
		display.add(_cbColour);
		display.add(_cbVectors);
		display.add(_cbField);
		
		this.getContentPane().add(display, BorderLayout.EAST);
		
		final JButton generate=new JButton("Generate");
		generate.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				generate.setText("Generating...");
				generate.setEnabled(false);
				Thread t = new Thread()
				{
					public void run()
					{
						generateField();
						render();
						generate.setText("Generate");
						generate.setEnabled(true);
					}
				};
				t.start();
			}
			
		});
		this.getContentPane().add(generate, BorderLayout.SOUTH);
	}
	
	private void initBase(int numCharges)
	{
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension dim = new Dimension(1000,650);
		this.setMinimumSize(dim);
		this.setVisible(true);
		_numCharges=numCharges;
		_width=800;
		_height=601;
		_drawing=new Drawing();
		_drawing.setSize(new Dimension(_width, _height));
		this.getContentPane().add(_drawing, BorderLayout.CENTER);
	}
	
	private void generateField()
	{
		Random r = new Random();
		double scale=100000;
		int cnt=_numCharges;
		boolean test=false;
		Charge2D[] charges;
		if (!test){
			charges=new Charge2D[cnt];
			for (int i=0; i<charges.length/2;i++)
			{
				charges[i]=new Charge2D(r.nextFloat()*_width/scale,r.nextFloat()*_height/scale,(r.nextFloat()*10)*10e-2);
			}
			for (int i=charges.length/2; i<charges.length;i++)
			{
				charges[i]=new Charge2D(r.nextFloat()*_width/scale,r.nextFloat()*_height/scale,-(r.nextFloat()*10)*10e-2);
			}
		}
		else
		{
			charges=new Charge2D[]{new Charge2D(100/scale, 100/scale, 1),new Charge2D(200/scale, 200/scale, -1)};
		}
		long start=System.nanoTime();
			_field=new EField2D(_height,_width,charges,scale);
		System.out.println((System.nanoTime()-start)/1000000+" Milliseconds for "+cnt+" charges");
	}
	
	private void render()
	{
		_drawing.drawColored(_coloured);
		_drawing.drawVectors(_drawVectors);
		_drawing.drawField(_drawField);
		_drawing.setField(_field);
	}
	
	class CheckboxListener implements ItemListener
	{

		@Override
		public void itemStateChanged(ItemEvent e) {
			_coloured = _cbColour.getState();
			_drawVectors = _cbVectors.getState();
			_drawField = _cbField.getState();
			render();
		}
		
	}
}
