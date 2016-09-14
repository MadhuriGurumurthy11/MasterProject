import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class JunitPowTest {

	private float num;
	private int exp;
	private float expected;

	public JunitPowTest(float num, int exp, float expected) {
		this.num = num;
		this.expected = expected;
		this.exp = exp;
	}

	@Parameters
	public static Collection<Object[]> pow() {
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
				"C:/Users/Madhuri/TestCases/Testing/pow.properties");

		FileInputStream fis = null;
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
		float answer = 0;

		@SuppressWarnings("rawtypes")
		Enumeration ekeys = props.keys();
		Object[][] dataToBeTested = new Object[props.size()][3];
		int j = -1;
		while (ekeys.hasMoreElements()) {
			++j;
			String key = (String) ekeys.nextElement();
			String value = props.getProperty(key);

			String[] values = value.split(":");

			String[] numValues = values[0].split(",");
			// int size = numValues.length;

			float pow = Float.parseFloat(numValues[0]);
			int exp = Integer.parseInt(numValues[1]);
			answer = Float.parseFloat(values[1]);

			dataToBeTested[j][0] = pow;
			dataToBeTested[j][1] = exp;
			dataToBeTested[j][2] = answer;

		}

		return dataToBeTested;

	}

	@Test
	public void testForPow() {
		// Addition add = new Addition();
		Recursion2 recursion = new Recursion2();
		Assert.assertEquals(expected, recursion.pow(num, exp), 0.01);
	}
}