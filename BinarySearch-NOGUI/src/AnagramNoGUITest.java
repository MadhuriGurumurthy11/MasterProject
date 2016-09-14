import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 */

/**
 * @author Madhuri Gurumurthy-Xms40m
 *
 */
public class AnagramNoGUITest {

	private AnagramNoGUI anagram;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		anagram = new AnagramNoGUI();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		anagram = null;
	}

	/**
	 * Test method for
	 * {@link AnagramNoGUI#findAnagramByOptions(java.lang.String)}.
	 */
	@Test
	public void testFindAnagramByOptions() {
		anagram.findAnagramByOptions("anagram1.properties");
	}

}
