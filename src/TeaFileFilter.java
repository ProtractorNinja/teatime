import javax.swing.filechooser.FileFilter;
import java.io.File;

public class TeaFileFilter extends FileFilter {

	/*
	 * Decides whether to accept files or not. This one looks for .dat files only.
	 */
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
	    }

		String extension = TeaUtils.getExtension(f);

		if (extension != null) {
			if (extension.equals("dat")) return true;
		} else return false;

		return false;
	}

	public String getDescription() {
		return "DAT files only";
	}
}
