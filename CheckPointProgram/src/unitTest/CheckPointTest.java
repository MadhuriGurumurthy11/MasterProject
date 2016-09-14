package unitTest;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Hashtable;

/**
 * 
 */

/**
 * @author Madhuri Gurumurthy
 *
 */
public class CheckPointTest
{
	// ---------------------------------------------------------------------
	// Arguments
	static String assnId;
	static String solutionLoc;
	static String studentLoc;
	static String testClass;
	static String solutionClassName;
	static String studentClassName;

	// ---------------------------------------------------------------------

	static String studentStr;
	static String solutionStr;
	static Hashtable<String, Object[]> studentObj;
	static Hashtable<String, Object[]> solutionObj;
	public static String[] checkPointIds;// setter
	private static Hashtable<String, Boolean> output = new Hashtable<String, Boolean>();
	// LOCKS
	static Integer studentLock = new Integer(100);
	static Integer solutionLock = new Integer(101);
	static Integer testLockStudent = new Integer(102);
	static Integer testLockSolution = new Integer(103);

	public void checkPoint(String str, Hashtable<String, Object[]> hash)
	{
		// TODO:
		System.out.println("String = " + str);

		if (str.equals("student"))
		{
			studentStr = str;
			studentObj = hash;
			synchronized (studentLock)
			{
				studentLock.notify();
			}
			//
			try
			{
				synchronized (testLockStudent)
				{
					testLockStudent.wait(3000);
				}
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}

		} else if (str.equals("solution"))
		{
			solutionStr = str;
			solutionObj = hash;

			synchronized (solutionLock)
			{
				solutionLock.notify();
			}
			//
			try
			{
				synchronized (testLockSolution)
				{
					testLockSolution.wait(3000);
				}
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}

		}

	}

	static int x = 0;

	private static boolean compareOutputs(String s)
	{
		Object[] solutionValue = solutionObj.get(s);
		Object[] studentValue = studentObj.get(s);

		// CASES
		// Case 1: when both the objects have equal number of objects

		// Case 2: When they have unequal number of objects.
		boolean returnValue = true;
		if (solutionValue != null && studentValue != null)
		{

			for (int i = 0; i < studentValue.length; i++)
			{
				if (!(studentValue[i].equals(solutionValue[i])))
				{
					returnValue = false;
				}
			}
			output.put(s, returnValue);
			return returnValue;
		} else
		{
			return false;
		}

	}

	public static void threadProcess(Class<?> studentRecursionClass,
			Object studentRecursionObj, Class<?> solutionClass,
			Object solutionRecursionObj)
	{
		CheckPointTest recursion = new CheckPointTest();

		Thread studentThread = createStudentThread(recursion,
				studentRecursionClass, studentRecursionObj);
		String[] checkPointIds = { "ordered", "charPair", "sum", "power",
				"exchange" };

		// SolutionThreadCreate two threads:
		Thread solutionThread = createSolutionThread(recursion, solutionClass,
				solutionRecursionObj);

		for (String s : checkPointIds)
		{
			execute(studentThread, testLockStudent, studentLock); // execute
			execute(solutionThread, testLockSolution, solutionLock);
			doCompare(s);
		}
		// TODO:
		System.out.println("Output Size= " + output.size());
		if (output.size() != checkPointIds.length)
		{
			for (int i = 0; i < checkPointIds.length; i++)
			{
				String id = checkPointIds[i];
				if (output.get(id) == null)
				{

					Object[] solutionValue = solutionObj.get(id);
					Object[] studentValue = studentObj.get(id);

					// CASES
					// Case 1: when both the objects have equal number of
					// objects

					// Case 2: When they have unequal number of objects.
					boolean returnValue = true;
					if (solutionValue != null && studentValue != null)
					{

						for (int j = 0; j < studentValue.length; j++)
						{
							if (!(studentValue[j].equals(solutionValue[j])))
							{
								returnValue = false;
							}
						}
						output.put(id, returnValue);
					}
				}
			}
		}
		// TODO:
		System.out.println("Final Output Size= " + output.size());
		studentThread = null;
		solutionThread = null;
	}

	/**
	 * 
	 * @param studentThread
	 * @param testLock
	 * @param objectLock
	 */
	public static void execute(Thread sThread, Integer testLock,
			Integer objectLock)
	{

		if (!sThread.isAlive())
		{
			sThread.start();
		}
		synchronized (testLock)
		{
			testLock.notify();
		}
		try
		{
			synchronized (objectLock)
			{
				objectLock.wait(2000);
			}
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @param check
	 * @param solution
	 * @return
	 */
	public static Thread createSolutionThread(CheckPointTest recursion,
			Class<?> solutionClass, Object solutionObj)
	{

		Thread solutionThread = new Thread()
		{
			public void run()
			{

				Method exe;
				try
				{
					exe = solutionClass.getMethod("execute");
					exe.invoke(solutionObj);
				} catch (NoSuchMethodException e)
				{
					e.printStackTrace();
				} catch (SecurityException e)
				{
					e.printStackTrace();
				} catch (IllegalAccessException e)
				{
					e.printStackTrace();
				} catch (IllegalArgumentException e)
				{
					e.printStackTrace();
				} catch (InvocationTargetException e)
				{
					e.printStackTrace();
				}

			}
		};

		try
		{
			Method setRecursionMethod = solutionClass.getMethod(
					"setCheckPointTestObject", CheckPointTest.class);
			setRecursionMethod.invoke(solutionObj, recursion);

			Method setStudentLock = solutionClass.getMethod("setSolutionLock",
					Integer.class);
			setStudentLock.invoke(solutionObj, solutionLock);

			Method setCodeId = solutionClass.getMethod("setCodeId",
					String.class);
			setCodeId.invoke(solutionObj, "solution");

			Method setRecursionLock = solutionClass.getMethod(
					"setCheckPointTestLock", Integer.class);
			setRecursionLock.invoke(solutionObj, testLockSolution);
		} catch (NoSuchMethodException | SecurityException e)
		{
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			e.printStackTrace();
		} catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		} catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}

		return solutionThread;
	}

	/**
	 * @param recursion2
	 * @param recursion
	 * @param object
	 * @param args
	 * @param recursion
	 * @param recursionObj
	 * @param recursionClass
	 * @param recursion
	 * @param check
	 * @param student
	 * @return
	 */
	public static Thread createStudentThread(CheckPointTest recursion,
			Class<?> recursionClass, Object recursionObj)
	{

		Thread studentThread = new Thread()
		{
			public void run()
			{

				Method exe;
				try
				{
					exe = recursionClass.getMethod("execute");
					exe.invoke(recursionObj);
				} catch (NoSuchMethodException e)
				{
					e.printStackTrace();
				} catch (SecurityException e)
				{
					e.printStackTrace();
				} catch (IllegalAccessException e)
				{
					e.printStackTrace();
				} catch (IllegalArgumentException e)
				{
					e.printStackTrace();
				} catch (InvocationTargetException e)
				{
					e.printStackTrace();
				}

			}
		};

		try
		{
			Method setRecursionMethod = recursionClass.getMethod(
					"setCheckPointTestObject", CheckPointTest.class);
			setRecursionMethod.invoke(recursionObj, recursion);

			Method setStudentLock = recursionClass.getMethod("setStudentLock",
					Integer.class);
			setStudentLock.invoke(recursionObj, studentLock);

			Method setCodeId = recursionClass.getMethod("setCodeId",
					String.class);
			setCodeId.invoke(recursionObj, "student");

			Method setRecursionLock = recursionClass.getMethod(
					"setCheckPointTestLock", Integer.class);
			setRecursionLock.invoke(recursionObj, testLockStudent);
		} catch (NoSuchMethodException | SecurityException e)
		{
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			e.printStackTrace();
		} catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		} catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}

		return studentThread;
	}

	public static void doCompare(String s)
	{
		// TODO:
		System.out.println("To compare = " + s);
		if (solutionStr != null && studentStr != null)
		{
			boolean result = false;
			result = compareOutputs(s);
			// TODO:
			System.out.println("Result= " + result);

			solutionStr = null;
			studentStr = null;
		}
	}

	public static void main(String[] args)
	{
		setTheArguments(args);
		getFilesAndStartThreads();

	}

	public static void setTheArguments(String[] args)
	{
		// This method is used to set the required arguments passed through
		// command prompt.
		if (args.length > 0)
		{
			assnId = args[0];

			solutionLoc = args[1] + "/example/cs416-" + assnId + "-dir";
			studentLoc = args[1] + "/run";
			if (args[2].contains("@generic"))
			{
				solutionClassName = args[2].replace("@generic", "recursionPkg");
				studentClassName = args[2].replace("@generic", "recursionPkg");

			} else if (args[2].contains("@student"))
			{
				solutionClassName = args[2].replace("@student", "solution");
				testClass = args[2];

			} else
			{
				solutionClassName = args[2];
				studentClassName = args[2];
			}

		}

	}

	private static void getFilesAndStartThreads()
	{
		// ----------------------TO FETCH SOLUTION CLASS----------------

		// Changing working directory
		System.setProperty("user.dir", solutionLoc);

		URL solUrl;
		Class<?> solution = null;

		Object solutionObj = null;
		File solFile = new File(solutionLoc);

		try
		{
			solUrl = solFile.toURL();
			URL[] solUrls = new URL[] { solUrl };
			ClassLoader solClazz = new URLClassLoader(solUrls);
			solution = solClazz.loadClass(solutionClassName);
			solutionObj = solution.newInstance();
		} catch (ClassNotFoundException e1)
		{
			e1.printStackTrace();
		} catch (MalformedURLException e)
		{
			e.printStackTrace();
		} catch (InstantiationException e)
		{
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		// --------------------------------Fetching Student's code--------------

		// Changing working directory
		System.setProperty("user.dir", studentLoc);

		File dir = new File(studentLoc);
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null)
		{
			for (File child : directoryListing)
			{
				// TODO:
				System.out.println(child.getName());
				String[] childNameSplit = child.getName().split("-");

				try
				{

					File file = new File(studentLoc + "/" + child.getName());

					URL url;
					Class<?> recursion = null;

					Object recursionObj = null;

					// ---------------------------------------------------------

					if (testClass.contains("@student"))
					{
						studentClassName = testClass;
						studentClassName = studentClassName.replace("@student",
								childNameSplit[0] + "_" + childNameSplit[1]);
					}

					// ---------------------------------------------------------

					try
					{
						url = file.toURL();
						URL[] urls = new URL[] { url };
						ClassLoader clazz = new URLClassLoader(urls);

						recursion = clazz.loadClass(studentClassName);
						recursionObj = recursion.newInstance();

					} catch (MalformedURLException e)
					{
						e.printStackTrace();
					}

					threadProcess(recursion, recursionObj, solution,
							solutionObj);

					// TODO:
					System.out
							.println("-------------------------------------------");

				} catch (ClassNotFoundException e)
				{
					e.printStackTrace();
				} catch (SecurityException e)
				{

					e.printStackTrace();
				} catch (IllegalAccessException e)
				{

					e.printStackTrace();
				} catch (IllegalArgumentException e)
				{

					e.printStackTrace();
				} catch (InstantiationException e)
				{

					e.printStackTrace();
				}

			}
		}

	}
}
