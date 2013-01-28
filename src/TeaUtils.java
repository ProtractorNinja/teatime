import java.util.ArrayList;
import java.io.File;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.swing.JOptionPane;
import java.io.FileOutputStream;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.ImageIcon;
import java.util.Arrays;
import javax.swing.JOptionPane;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

public class TeaUtils {

	/* Current world information */
	public static Tag currentWorld;
	public static String currentWorldPath;

	/* What OS is this? */
	private static String os = System.getProperty("os.name").toLowerCase();
	private static boolean isUnix = os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0;
	private static boolean isWindows = os.indexOf("win") >= 0;
	private static boolean isMac = os.indexOf("mac") >= 0;

	public static final String PATHSEP = File.separator;

	public static final File SCHEME_DIR = new File(System.getProperty("user.dir") + PATHSEP + "TeaSchemes");

	/*
	 * Is this unix?
	 */
	public static boolean isUnix() {
		return isUnix;
	}

	/*
	 * Is this windows?
	 */
	public static boolean isWindows() {
		return isWindows;
	}

	/*
	 * Is this mac?
	 */
	public static boolean isMac() {
		return isMac;
	}

	/*
	 * Returns the world currently being edited.
	 */
 	public static Tag getCurrentWorld() {
 		return currentWorld;
 	}

	/*
	 * Sets the world currently being edited.
	 */
 	public static void setCurrentWorld(Tag tag) {
 		currentWorld = tag;
 	}

 	public static void removeCurrentWorld() {
 		currentWorld = null;
 		currentWorldPath = "";
 	}

 	/*
	 * Returns the location of the world currently being edited.
	 */
 	public static String getCurrentWorldPath() {
 		return currentWorldPath;
 	}

	/*
	 * Sets the location of the world currently being edited.
	 */
 	public static void setCurrentWorldPath(String path) {
 		currentWorldPath = path;
 	}

	public static String getMCPath() {
		if(isWindows())
 			return System.getenv("APPDATA") + PATHSEP + ".minecraft";
 		else if(isMac())
 			return System.getProperty("user.home") + PATHSEP + "Library" + PATHSEP + "Application Support" + PATHSEP + "minecraft";
 		else if(isUnix())
 			return System.getProperty("user.home") + PATHSEP + ".minecraft";
 		else {
 			System.exit(-1);
 			return "0";
 		}
	}

	/*
	 * Returns the path to the minecraft worlds directory. Needs to be made cross-platform.
	 */
	public static String getMCWorldsPath() {
		return getMCPath() + PATHSEP + "saves";
 	}

	/*
	 * Returns the path to the Minecraft.jar file. Needs to be made cross-platform.
	 */
 	public static String getMCJarPath() {
 		return getMCPath() + PATHSEP + "bin" + PATHSEP + "minecraft.jar";
 	}

	/*
	 * Gets a list of the map data files
	 */
 	public static File[] getMapDataList(String w) {
 		ArrayList<File> maps = new ArrayList<File>();
 		File f = new File(getMCWorldsPath() + PATHSEP + w + PATHSEP + "data");
 		File[] m = f.listFiles();
 		for (File tf : m) {
 			if(tf.getName().contains("map_")) maps.add(tf);
 		}
 		File[] e = new File[0];
 		return maps.toArray(e);
 	}

 	public static boolean mapDirExists(String w) {
 		File f = new File(getMCWorldsPath() + PATHSEP + w + PATHSEP + "data");
 		if(f.exists()) return true;
 		else return false;
 	}


	/*
	 * Returns a list of available minecraft worlds from the saves directory.
	 * Only folders in the format "<String>/level.dat will be accepted.
	 * Kind of needs to be sped up
	 */
 	public static String[] getMCWorldsList() {
 		String[] levels = new String[0];
 		ArrayList<String> files = new ArrayList<String>();
 		File[] worlds;
 		String tPath;
 		try {
			File f = new File(getMCWorldsPath());
			worlds = f.listFiles();
			int limit = 15;
			if(worlds.length < limit) limit = worlds.length;
			for(int i=0; i<limit; i++) {
				tPath = "" + worlds[i] + PATHSEP + "level.dat";
				if(new File(tPath).exists()) files.add(tPath);
			}
			levels = files.toArray(levels);
 		} catch(Exception e) {
 			levels = new String[0];
 		}
		return levels;
 	}

 	public static Tag[] getInvSchemes() {
 		Tag[] schemes;
 		File[] schemeFiles;
 		try {
 			schemeFiles = getSchemeFiles();
 			schemes = new Tag[schemeFiles.length];
 			for(int i=0; i<schemes.length; i++) {
 				schemes[i] = Tag.readFrom(new BufferedInputStream(new FileInputStream(schemeFiles[i])));
 			}
 			return schemes;
 		} catch (Exception e) {
 			return new Tag[0];
 		}
 	}

 	public static File[] getSchemeFiles() {
 		if(!SCHEME_DIR.exists()) return new File[0];
 		ArrayList<File> alfSchemes = new ArrayList<File>();
 		File[] afSchemes = new File[0];
 		Tag t;
 		try {
 			afSchemes = SCHEME_DIR.listFiles();
 			for(File af : afSchemes) {
 				try {
 					t = Tag.readFrom(new BufferedInputStream(new FileInputStream(af)));
 					alfSchemes.add(af);
 				} catch (Exception e) {

 				}
 			}
 			afSchemes = new File[0];
 			return alfSchemes.toArray(afSchemes);
 		} catch (Exception e) {
 			TeaUtils.buildExceptionDialog(e);
 			return new File[0];
 		}
 	}

 	public static void saveInvScheme(Tag invTag, String name) {
 		try {
 			if(!SCHEME_DIR.exists()) SCHEME_DIR.mkdir();
 			File oFile = new File(SCHEME_DIR, name + ".dat");
 			invTag.writeTo(new BufferedOutputStream(new FileOutputStream(oFile)));
 		} catch (Exception e) {
 			TeaUtils.buildExceptionDialog(e);
 		}
 	}

 	public static void delInvScheme(String name) {
 		try {
 			File f = new File(SCHEME_DIR, name + ".dat");
 			f.delete();
 			if(SCHEME_DIR.listFiles().length == 0) SCHEME_DIR.delete();
 		} catch (Exception e) {
 			TeaUtils.buildExceptionDialog(e);
 		}
 	}

 	public static Tag loadInvScheme(String name) {
 		Tag t;
 		try {
 			t = Tag.readFrom(new BufferedInputStream(new FileInputStream(new File(SCHEME_DIR, name + ".dat"))));
 			return t;
 		} catch (Exception e) {
 			TeaUtils.buildExceptionDialog(e);
 			return null;
 		}
 	}

	/*
	 * Builds a Dialog window displaying the obtained exception and asks if you want to save it.
	 */
	public static void buildExceptionDialog(Exception e) {
		Object[] options = { "Exit TeaTime", "Save Error and Exit", "Continue"};

		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String exceptionStack = sw.toString();

		int i = JOptionPane.showOptionDialog(null, "Errors? In MY tea time?\nIt's more likely than you think.\n" + e.getMessage() +
													"\nIt is greatly advised that you save the error log with the button below, quit, and" +
													" notify Protractor Ninja. He'll do his best to figure out what's going wrong. " +
													"If you choose to continue, something may go terribly wrong." ,
													 "Tea Time Error!", JOptionPane.DEFAULT_OPTION,
											 		JOptionPane.ERROR_MESSAGE, null, options, options[1]);

		if(i == 1) { //Close and save an error message
			String errFile = System.getProperty("user.dir") + PATHSEP + "Tea Time Error " + getDateTime() + ".txt";
			writeStringToFile(exceptionStack, errFile);
			JOptionPane.showMessageDialog(null, "Error log file saved to:\n" + errFile +"\nIf seeking help, please include it in your request.", 										  "Error saved!", JOptionPane.INFORMATION_MESSAGE);
			System.exit(-1);
		} else if(i == 2) { //Continue?!

		} else System.exit(-1);
	}

 	/*
 	 * Creates a new level.dat NBT structure based on the current information.
 	 * Currently takes an inventory Tag as an argument.
 	 */
 	public static Tag buildLevelData(TabLevelData dataTab) {
 		Tag levelData = dataTab.buildLevelData();
		return levelData;
 	}

 	/*
 	 * Returns an ArrayList from a string, separated by a certain string.
 	 * Rendered pointless by string.split().
 	 */
 	public static ArrayList<String> explode(String source, String separator) {
 	 	ArrayList<String> list = new ArrayList<String>();
 	 	while(source.indexOf(separator) > 0) {
 	 		String toAdd = source.substring(0, source.indexOf(separator));
 	 		source = source.replace(toAdd + separator, "");
 	 		list.add(toAdd);
 	 	}
 	 	list.add(source);
 	 	return list;
 	}

 	/*
     * Get the extension of a file.
     */
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }


	/*
	 * Prints out an arraylist
	 */
	public static void printArrayList(ArrayList r) {
		for(int i=0; i<r.size(); i++) {
			System.out.println(r.get(i));
		}
	}

	/*
	 * Writes an arraylist to a file. Not much point to existing.
	 */
	public static void writeArrayListToFile(ArrayList<String> a, String file) {
		try {
			File f = new File(file);
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			for(int i=0; i<a.size(); i++) {
				writer.write(a.get(i));
				writer.newLine();
			}
			writer.close();
		} catch (IOException ioe) {
			System.err.println(ioe);
		}
	}

	/*
	 * Writes a string to a file.
	 */
	public static void writeStringToFile(String string, String file){
		try {
			File f = new File(file);
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(string.getBytes());
			fos.flush();
			fos.close();
		} catch (Exception e) {
			TeaUtils.buildExceptionDialog(e);
		}
	}

	/*
	 * Prints an array.
	 * Only works with high-level object arrays, not things like int[] or byte[].
	 */
	public static void printArray(Object[] array) {
		System.out.print("{ ");
		for(int i=0; i<array.length-1; i++) {
			System.out.print(array[i] + ", ");
		}
		System.out.println(array[array.length-1] + " }");
	}

	/*
	 * Gets a date and time string.
	 * http://www.java-tips.org/java-se-tips/java.util/how-to-get-current-date-time.html
	 */
    public static String buildDate(String format) {
    	DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getDateTime() {
    	return buildDate("yyyy-MM-dd [hh.mm.ss]");
    }

    public static String getDate() {
    	return buildDate("yyyy-MM-dd");
    }

    public static String getTime() {
    	return buildDate("hh.mm.ss");
    }

}
