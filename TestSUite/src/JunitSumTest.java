import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Properties;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class JunitSumTest {

	private int[] first;
	private int expected;
	private int n;

	public JunitSumTest(int[] firstNumber, int n, int expected) {
		first = new int[firstNumber.length];
		first = firstNumber;
		this.expected = expected;
		this.n = n;
	}

	@Parameters
	public static Collection<Object[]> sum() {
		Object[][] i = prepareData();
		return Arrays.asList(i);
	}

	/**
	 * Test method for {@link TestSumArray#runSum()}.
	 * 
	 * Input size is 500 and whole numbers
	 */
	public static Object[][] prepareData() {

		File file = new File(
				"C:/Users/Madhuri/TestCases/Testing/input1.properties");

		FileInputStream fis = null;
		Integer[][] toBeReturned = null;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Properties props = new Properties();
		try {
			props.load(fis);
			fis.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		int answer = 0;
		int[] numbersToBeChecked = null;

		@SuppressWarnings("rawtypes")
		Enumeration ekeys = props.keys();
		Object[][] dataToBeTested = new Object[props.size()][3];
		int j = -1;
		while (ekeys.hasMoreElements()) {
			++j;
			String key = (String) ekeys.nextElement();
			String value = props.getProperty(key);

			String[] values = value.split(":");
			answer = Integer.parseInt(values[values.length - 1]);

			String[] numValues = values[0].split(",");
			int size = numValues.length;
			numbersToBeChecked = new int[size];

			for (int i = 0; i < numValues.length; i++) {

				numbersToBeChecked[i] = Integer.parseInt(numValues[i]);
			}

			dataToBeTested[j][0] = numbersToBeChecked;
			dataToBeTested[j][1] = numbersToBeChecked.length;
			dataToBeTested[j][2] = answer;

		}

		return dataToBeTested;

	}

	// private static Object[][] getData() {
	// return new Object[][] {
	// { new int[] { 1, 3, 2, 4, 5 }, new int[] { 5 },
	// new int[] { 15 } },
	// { new int[] { 0, 0, 0, 0, 0 }, new int[] { 5 },
	// new int[] { 0 } },
	// { new int[] { -1, 2, -5, 8, -9 }, new int[] { 5 },
	// new int[] { -8 } },
	// { new int[] { 1, 1, 1 }, new int[] { 3 },
	// new int[] { 3 } } };
	// }

	@Test
	public void testForArraySum() {
		// Addition add = new Addition();
		Recursion2 recursion = new Recursion2();

		assertEquals(expected, recursion.sum(first, n));
	}
}