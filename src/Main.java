

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int count=100;
		if (args.length>0)
		{
			count=Integer.parseInt(args[0]);
		}
		new MainGUI(count);
	}
}