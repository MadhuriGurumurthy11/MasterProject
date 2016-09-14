import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

import java.awt.Component;
import java.io.File;
import java.util.Collection;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.testng.PowerMockTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ JOptionPane.class, Scanner.class })
public class JunitMockitoAnagramTest extends PowerMockTestCase {

	@SuppressWarnings("static-access")
	@Test
	public void testAnagram() {

		// We just want to test the binary search
		AnagramTest test = new AnagramTest();
		PowerMockito.mockStatic(JOptionPane.class);

		test.getJOptionValue("");

		PowerMockito.verifyStatic();
		JOptionPane.showInputDialog(any(), any(String.class));

		JOptionPane joption = Mockito.mock(JOptionPane.class);

		File file = new File("opted3.txt");
		try {

			Mockito.when(joption.showInputDialog(any(), anyString()))
					.thenReturn("s", "b", "riverriver", "elf", "ram", "set",
							"bus", null);
			PowerMockito.whenNew(File.class).withArguments(String.class)
					.thenReturn(file);

		} catch (Exception e) {
			e.printStackTrace();
		}

		test.findAnagramByOptions();
	}
}