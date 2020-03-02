package zrock.application.scriptme.editor.helper;

import java.io.File;

public class FileHelper {
    /**
     * Get the extension of a file
     * @param f the file
     * @return the extension of a file
     */
    public static String getExtension(File f) {
        return getExtension(f.getAbsolutePath());
    }

    /**
     * Get the extension from a file path
     * @param path the path
     * @return the extension  from a file path
     */
    public static String getExtension(String path) {
        String ext = null;
        String s = path;
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
}
