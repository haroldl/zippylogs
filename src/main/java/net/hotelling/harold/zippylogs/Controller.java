package net.hotelling.harold.zippylogs;

import java.io.File;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;


public class Controller {
  @FXML TableView<DirectoryRecord> dataTable;

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
      DirectoryRecord record = new DirectoryRecord(directory.toString(), "Pending", directory);
      dataTable.getItems().add(record);
      DirectorySizerTask.launch(directory, (sizeInBytes) -> {
        record.setDirectorySize("" + sizeInBytes);
      });
    } else {
      System.out.println("No directory chosen.");
    }
  }

  public void removeSelectedRow(ActionEvent actionEvent) {
    ObservableList<DirectoryRecord> selectedItems = dataTable.getSelectionModel().getSelectedItems();
    dataTable.getItems().removeAll(selectedItems);
  }

  public void updateDirectorySizes(ActionEvent actionEvent) {
    for (DirectoryRecord record : dataTable.getItems()) {
      record.setDirectorySize("Pending");
      DirectorySizerTask.launch(record.getDirectory(), (sizeInBytes) -> {
        record.setDirectorySize("" + sizeInBytes);
      });
    }
  }
}
