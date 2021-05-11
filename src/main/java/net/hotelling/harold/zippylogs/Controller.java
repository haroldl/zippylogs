package net.hotelling.harold.zippylogs;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;


public class Controller {
  @FXML TableView<DirectoryRecord> dataTable;

  @FXML public void initialize() {
    for (File directory : StatePersistence.loadDirectories()) {
      addDirectoryToDataTable(directory);
    }
  }

  public void pickADirectoryToWatch(ActionEvent actionEvent) {
    DirectoryChooser chooser = new DirectoryChooser();
    chooser.setTitle("Select directory to watch");
    chooser.setInitialDirectory(new File(System.getProperty("user.home")));

    // Pass the current Stage (the window works) in order to make the chooser dialog modal:
    Button buttonClicked = (Button) actionEvent.getTarget();
    Window window = buttonClicked.getScene().getWindow();

    File directory = chooser.showDialog(window);

    // If a directory was chosen, versus the user clicking to cancel the selection process:
    if (directory != null) {
      addDirectoryToDataTable(directory);
      saveState();
    }
  }

  public void removeSelectedRow(ActionEvent actionEvent) {
    ObservableList<DirectoryRecord> selectedItems = dataTable.getSelectionModel().getSelectedItems();
    dataTable.getItems().removeAll(selectedItems);
    saveState();
  }

  public void updateDirectorySizes(ActionEvent actionEvent) {
    for (DirectoryRecord record : dataTable.getItems()) {
      record.setDirectorySize("Pending");
      DirectorySizerTask.launch(record.getDirectory(), (sizeInBytes) -> {
        record.setDirectorySize(formatSizeForDisplay(sizeInBytes));
      });
    }
  }

  private void saveState() {
    List<File> currentDirectories = dataTable.getItems().stream()
        .map(DirectoryRecord::getDirectory)
        .collect(Collectors.toList());
    StatePersistence.saveDirectories(currentDirectories);
  }

  private void addDirectoryToDataTable(File directory) {
    DirectoryRecord record = new DirectoryRecord(directory.toString(), "Pending", directory);
    dataTable.getItems().add(record);
    DirectorySizerTask.launch(directory, (sizeInBytes) -> {
      record.setDirectorySize(formatSizeForDisplay(sizeInBytes));
    });
  }

  private String formatSizeForDisplay(long sizeInBytes) {
    double sizeInGB = sizeInBytes / (1024.0 * 1024 * 1024);
    return String.format("%.1f", sizeInGB);
  }
}
