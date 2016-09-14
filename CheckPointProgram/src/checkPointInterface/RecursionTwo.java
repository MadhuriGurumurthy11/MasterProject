package checkPointInterface;

import unitTest.CheckPointTest;

/**
 * 
 */

/**
 * @author Madhuri
 *
 */
public interface RecursionTwo
{
	public static int sum(int[] vals, int n)
	{
		return 0;
	}

	public static float pow(float num, int exp)
	{
		return 0;
	}

	public static boolean ordered(int[] vals, int n)
	{
		return false;
	}

	public static boolean charPairTest(String s)
	{
		return false;
	}

	public static String exchangePairs(String s)
	{
		return null;
	}

	public void setCheckPointTestObject(CheckPointTest check);

	public void setCodeId(String string);

	public void setSolutionLock(Integer solutionLock2);

	public void setCheckPointTestLock(Integer testLockSolution);

	public void execute();

	public void setStudentLock(Integer studentLock2);

}
