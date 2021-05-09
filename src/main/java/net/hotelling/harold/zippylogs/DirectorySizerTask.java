package net.hotelling.harold.zippylogs;

import java.io.File;
import java.util.function.Consumer;
import javafx.application.Platform;
import javafx.concurrent.Task;


/**
 * Find the size of a directory asynchronously and optionally report the
 * results to the UI thread for updating the UI.
 */
public class DirectorySizerTask extends Task<Long> {
  private final File directory;
  private final Consumer<Long> uiCallback;

  public static void launch(File directory, Consumer<Long> uiCallback) {
    Thread thread = new Thread(new DirectorySizerTask(directory, uiCallback));
    thread.setDaemon(true);
    thread.start();
  }

  /**
   *
   * @param directory is the directory to compute the size of.
   * @param uiCallback if not null, then this callback is invoked with the size in bytes of the directory.
   */
  public DirectorySizerTask(File directory, Consumer<Long> uiCallback) {
    super();
    this.directory = directory;
    this.uiCallback = uiCallback;
  }

  @Override
  protected Long call() {
    long sizeInBytes = DirectorySizer.getSizeInBytes(directory);
    if (uiCallback != null) {
      Platform.runLater(() -> uiCallback.accept(sizeInBytes));
    }
    return sizeInBytes;
  }
}
