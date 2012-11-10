import javax.swing.UIManager;



public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        	System.err.println("Look and feel not set.");
        }
		int count=10;
		if (args.length>0)
		{
			count=Integer.parseInt(args[0]);
		}
		new MainGUI(count);
	}
}