package net.hotelling.harold.zippylogs;

import java.io.File;


public class DirectorySizer {
  /**
   * Slow, recursive walk of a directory to add up the size of all files under that directory.
   * Also works on a single file.
   */
  public static long getSizeInBytes(File directory) {
    if (directory == null) {
      return 0L;
    } else if (directory.isDirectory()) {
      File[] files = directory.listFiles();
      if (files == null) {
        return 0L;
      }

      long total = 0L;
      for (File file : files) {
        total += getSizeInBytes(file);
      }
      return total;
    } else {
      return directory.length();
    }
  }
}
