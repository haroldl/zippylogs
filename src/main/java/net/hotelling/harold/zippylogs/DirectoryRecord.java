package net.hotelling.harold.zippylogs;

import java.io.File;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class DirectoryRecord {
  private StringProperty directoryName;
  private StringProperty directorySize;
  private File directory;

  public DirectoryRecord(String directoryName, String directorySize, File directory) {
    this.directoryName = new SimpleStringProperty(this, "directoryName");
    this.directoryName.set(directoryName);

    this.directorySize = new SimpleStringProperty(this, "directorySize");
    this.directorySize.set(directorySize);

    this.directory = directory;
  }

  public File getDirectory() {
    return this.directory;
  }

  public void setDirectoryName(String directoryName) {
    this.directoryName.set(directoryName);
  }

  public String getDirectoryName() {
    return this.directoryName.get();
  }

  // Exposing the property using this magic naming convention means that
  // changing the value will trigger the TableView to update the visible
  // data. If we leave out this method, then calling setDirectoryName will
  // not update the UI but everything else will work.
  public StringProperty directoryNameProperty() {
    return directoryName;
  }

  public void setDirectorySize(String directorySize) {
    this.directorySize.set(directorySize);
  }

  public String getDirectorySize() {
    return this.directorySize.get();
  }

  public StringProperty directorySizeProperty() {
    return directorySize;
  }
}
