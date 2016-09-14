/**
 * Tester.java -- A simple test driver program that can execute the main program
 * of another class Primarily used to test a lab or program in which the student
 * submits his or her own version of the main class -- so that we cannot easily
 * modify the main method for doing our tests.
 * 
 * @author rdb 02/20/11 02/13/13 rdb Modified to use new AnagramTest batch mode
 *         constructor
 */
public class Tester {
	// ---------------------- main --------------------------------
	public static void main(String[] args) {
		if (args.length == 0) // execute the batch version
			batch(args);
		else
			interactive(args);
	}

	public static void batch(String[] args) {
		String opted3Test[] = { "mew", "elf", "axe", "new", "yew", "zot",
				"art", "own", "die" };
		String opted3to8Test[] = { "wears", "sparse", "relapse", "qwertyui" };

		// We just want to test the binary search
		AnagramTest test = new AnagramTest();
		test.findAnangram("opted3.txt", "b", opted3Test);
		test.findAnangram("opted3to8.txt", "b", opted3to8Test);
	}

	public static void interactive(String[] args) {
		String[] newArgs = { "opted3.txt" };
		String dashes = " --------------------------------- ";

		if (args.length == 0) {
			System.out.println(dashes + "opted3.txt" + dashes);
			AnagramTest.main(newArgs);

			System.out.println(dashes + "opted3to8.txt" + dashes);
			newArgs[0] = "opted3to8.txt";
			AnagramTest.main(newArgs);
		} else {
			System.out.println(dashes + args[0] + dashes);
			AnagramTest.main(args);
		}
	}
}