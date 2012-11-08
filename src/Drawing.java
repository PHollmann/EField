import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


public class Drawing extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7587957638806621652L;
	private EField2D _field;
	private MagnitudeCharge[][] _norm;
	private boolean _showField=true;
	private boolean _colored=true;
	private boolean _showVectors=true;
	
	public Drawing()
	{
	}
	
	public void drawField(boolean b)
	{
		_showField = b;
	}
	
	public void drawColored(boolean b)
	{
		_colored = b;
	}
	
	public void drawVectors(boolean b)
	{
		_showVectors = b;
	}
	
	public void setField(EField2D field)
	{
		_field=field;
		_norm = _field.getNormalizedField();
		this.repaint();
	}
	
	@Override 
	public void paintComponent(Graphics g1) {
        super.paintComponent(g1);
        if (_field==null)
        {
        	return;
        }
        Graphics2D g=(Graphics2D)g1;
        g.setRenderingHint ( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (int x=0;x<_norm.length;x++)
        {
        	for (int y=0;y<_norm[x].length;y++)
        	{
        		drawPoint(x,y,g,_norm);
        	}
        }
        if (_showVectors)
		{
        	Charge2D[][] vectorField = _field.getField();
        	for (int x=10;x<vectorField.length;x+=10)
            {
            	for (int y=10;y<vectorField[x].length;y+=10)
            	{
            		Vector2D unit=vectorField[x-5][y-5].getVector().getUnitVector();
            		Vector2D v=unit.clone();
            		v.scale(20);
            		unit.scale(-10);
            		v.add(unit);
            		g.setColor(Color.WHITE);
            		drawArrow(g,x, y, (int)(x+v.getX()), (int)(y+v.getY()));
            	}
            }
		}
    }
	
	private void drawPoint(int x,int y,Graphics g, MagnitudeCharge[][] f)
	{
		double red,green,blue;
		if (_colored)
		{
    		red=0;
    		blue=0;
    		green=0;
    		if (f[x][y].getCharge()>0)
    		{
    			red=255;
    		}
    		else
    		{
    			blue=255;
    		}
		}
		else
		{
			red=255;
			green=255;
			blue=255;
		}
		if (_showField)
		{
    		red*=f[x][y].getMagnitude();
    		blue*=f[x][y].getMagnitude();
    		green*=f[x][y].getMagnitude();
		}
        g.setColor(
    		new Color((int)red,(int)green,(int)blue)
    	);
		g.drawLine(x, y, x, y);
	}
	
	public void save()
	{
	    Dimension size = this.getSize();
	    BufferedImage image = (BufferedImage)this.createImage(size.width, size.height);
	    Graphics g = image.getGraphics();
	    this.paint(g);
	    g.dispose();
	    try      {
	      ImageIO.write(image, "png", new File("image.png"));
	    }
	    catch (IOException e){
	      e.printStackTrace();
	    }
	}
	
	/**
	   * Draws an arrow on the given Graphics2D context
	   * @param g The Graphics2D context to draw on
	   * @param x The x location of the "tail" of the arrow
	   * @param y The y location of the "tail" of the arrow
	   * @param xx The x location of the "head" of the arrow
	   * @param yy The y location of the "head" of the arrow
	   */
	void drawArrow(Graphics g1, int x1, int y1, int x2, int y2) {
		final int ARR_SIZE = 3;
        Graphics2D g = (Graphics2D) g1.create();

        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx*dx + dy*dy);
        AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
        at.concatenate(AffineTransform.getRotateInstance(angle));
        g.transform(at);

        // Draw horizontal arrow starting in (0, 0)
        g.drawLine(0, 0, len, 0);
        g.fillPolygon(new int[] {len, len-ARR_SIZE, len-ARR_SIZE, len},
                      new int[] {0, -ARR_SIZE, ARR_SIZE, 0}, 4);
    }
}
