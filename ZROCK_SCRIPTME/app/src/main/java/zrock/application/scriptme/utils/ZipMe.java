package zrock.application.scriptme.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.*;
import java.util.*;
import java.io.*;

/**
 * This utility extracts files and directories of a standard zip file to
 * a destination directory.
 * @author www.codejava.net
 *
 */
public class ZipMe {
	public static boolean compressFile(File parent, List<File> files) {
        boolean success = false;
        try {
            File dest = new File(parent, getNameFromFilename(files.get(0).getName()) + ".zip");
            ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(dest));
            compressFile("", zout, files.toArray(new File[files.size()]));
            zout.close();
            success = true;
        } catch (Exception e) {

        }
        return success;
    }

    private static void compressFile(String currentDir, ZipOutputStream zout, File[] files) throws Exception {
        byte[] buffer = new byte[1024];
        for (File fi : files) {
            if (fi.isDirectory()) {
                compressFile(currentDir + "/" + fi.getName(), zout, fi.listFiles());
                continue;
            }
            ZipEntry ze = new ZipEntry(currentDir + "/" + fi.getName());
            FileInputStream fin = new FileInputStream(fi.getPath());
            zout.putNextEntry(ze);
            int length;
            while ((length = fin.read(buffer)) > 0) {
                zout.write(buffer, 0, length);
            }
            zout.closeEntry();
            fin.close();
        }
    }

    public static boolean uncompress(File zipFile) {
        boolean success = false;
        try {
            FileInputStream fis = new FileInputStream(zipFile);
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
            ZipEntry entry;
            File destFolder = new File(zipFile.getParent(), getNameFromFilename(zipFile.getName()));
            destFolder.mkdirs();
            while ((entry = zis.getNextEntry()) != null) {
                File dest = new File(destFolder, entry.getName());
                dest.getParentFile().mkdirs();

                if(entry.isDirectory()) {
                    if (!dest.exists()) {
                        dest.mkdirs();
                    }
                } else {
                    int size;
                    byte[] buffer = new byte[2048];
                    FileOutputStream fos = new FileOutputStream(dest);
                    BufferedOutputStream bos = new BufferedOutputStream(fos, buffer.length);
                    while ((size = zis.read(buffer, 0, buffer.length)) != -1) {
                        bos.write(buffer, 0, size);
                    }
                    bos.flush();
                    bos.close();
                    IoUtils.flushQuietly(bos);
                    IoUtils.closeQuietly(bos);
                }
                zis.closeEntry();
            }
            IoUtils.closeQuietly(zis);
            IoUtils.closeQuietly(fis);
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }
	
	public static String getNameFromFilename(String filename) {
        int dotPosition = filename.lastIndexOf('.');
        if (dotPosition != -1) {
            return filename.substring(0, dotPosition);
        }
        return "";
    }
}
