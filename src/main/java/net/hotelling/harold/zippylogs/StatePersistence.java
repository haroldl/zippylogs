package net.hotelling.harold.zippylogs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;


/**
 * Support for saving and restoring state across executions of the program.
 */
public class StatePersistence {
  private static final Object lock = new Object();

  /**
   * Save the given list of directories to be used later.
   * The I/O work is done in a background thread so it will return immediately.
   * The background thread is not a daemon thread to avoid a source of partially complete write operations.
   */
  public static void saveDirectories(List<File> directories) {
    new SaveDirectoriesThread(directories).start();
  }

  private static class SaveDirectoriesThread extends Thread {
    private final List<File> directories;

    public SaveDirectoriesThread(List<File> directories) {
      this.directories = directories;
    }

    @Override
    public void run() {
      Properties props = new Properties();
      for (int i = 0; i < directories.size(); i++) {
        String prefName = String.format("dir%05d", i);
        props.setProperty(prefName, directories.get(i).toString());
      }

      String propsPath = getPropsFilePath();

      synchronized (lock) {
        new File(propsPath).delete();
        try {
          FileOutputStream fileOutputStream = new FileOutputStream(propsPath);
          props.store(fileOutputStream, "ZippyLogs");
          fileOutputStream.close();
        } catch (IOException e) {
          throw new RuntimeException("Unable to save list of directories to file " + propsPath, e);
        }
      }
    }
  }

  /**
   * Return the list of directories saved previously.
   * I/O work is done in the current thread.
   */
  public static List<File> loadDirectories() {
    Properties props = new Properties();
    String propsPath = getPropsFilePath();

    synchronized (lock) {
      try {
        FileInputStream fileInputStream = new FileInputStream(propsPath);
        props.load(fileInputStream);
      } catch (IOException e) {
        throw new RuntimeException("Unable to save list of directories to file " + propsPath, e);
      }
    }

    Enumeration<String> propertyNames = (Enumeration<String>) props.propertyNames();
    List<String> propNames = new ArrayList<>();
    while (propertyNames.hasMoreElements()) {
      String propName = propertyNames.nextElement();
      propNames.add(propName);
    }
    return propNames.stream()
        .sorted()
        .map(props::getProperty)
        .map(File::new)
        .collect(Collectors.toList());
  }

  private static String getPropsFilePath() {
    String homePath = System.getProperty("user.home");
    return homePath + File.separator + ".zippylogs.properties";
  }

  private static String[] findSavedDirectoryNames(Preferences node) {
    try {
      return node.childrenNames();
    } catch (BackingStoreException e) {
      throw new RuntimeException("Unable to examine the Preferences for existing saved directory names.", e);
    }
  }
}
