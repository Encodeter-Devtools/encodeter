package io.github.haohao123coding.encodeter;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main extends Application{
    private Stage primaryStage;
    private TextArea textArea;
    private Label statusLabel;
    private File currentFile;

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        this.primaryStage = primaryStage;
        primaryStage.setTitle("EnCodeter IDE");

        BorderPane root = createRootLayout();

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        updateStatus();
    }

    private BorderPane createRootLayout(){
        BorderPane root = new BorderPane();

        root.setTop(createMenuBar());

        textArea = new TextArea();
        textArea.setWrapText(true);
        textArea.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 16px;");
        textArea.textProperty().addListener((observable, oldValue, newValue) -> updateStatus());
        root.setCenter(textArea);
        
        root.setBottom(createStatusBar());

        return root;
    }

    private MenuBar createMenuBar(){
        MenuBar menuBar = new MenuBar();

        // File Menu
        Menu fileMenu = new Menu("File");
        MenuItem newItem = new MenuItem("New");
        MenuItem openItem = new MenuItem("Open");
        MenuItem saveItem = new MenuItem("Save");
        MenuItem saveAsItem = new MenuItem("Save As...");
        MenuItem exitItem = new MenuItem("Exit");

        newItem.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        openItem.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        saveItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        saveAsItem.setAccelerator(new KeyCodeCombination(KeyCode.S,
            KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));
        exitItem.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN));

        newItem.setOnAction(e -> newFile());
        openItem.setOnAction(e -> openFile());
        saveItem.setOnAction(e -> saveFile());
        saveAsItem.setOnAction(e -> saveAsFile());
        exitItem.setOnAction(e -> exitApplication());

        fileMenu.getItems().addAll(newItem, openItem, saveItem, saveAsItem, new SeparatorMenuItem(), exitItem);

        // Edit Menu
        Menu editMenu = new Menu("Edit");
        MenuItem copyItem = new MenuItem("Copy");
        MenuItem cutItem = new MenuItem("Cut");
        MenuItem pasteItem = new MenuItem("Paste");
        MenuItem selectAllItem = new MenuItem("Choose All");

        copyItem.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN));
        cutItem.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN));
        pasteItem.setAccelerator(new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN));
        selectAllItem.setAccelerator(new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN));

        copyItem.setOnAction(e -> textArea.copy());
        cutItem.setOnAction(e -> textArea.cut());
        pasteItem.setOnAction(e -> textArea.paste());
        selectAllItem.setOnAction(e -> textArea.selectAll());

        editMenu.getItems().addAll(copyItem, cutItem, pasteItem, selectAllItem);

        // Help Menu
        Menu helpMenu = new Menu("Help");
        MenuItem aboutItem = new MenuItem("About");
        aboutItem.setOnAction(e -> showAboutMessage());
        helpMenu.getItems().add(aboutItem);

        menuBar.getMenus().addAll(fileMenu, editMenu, helpMenu);
        return menuBar;
    }

    private void newFile(){
        textArea.clear();
        currentFile = null;
        primaryStage.setTitle("EnCodeter IDE - New File");
        updateStatus();
    }

    private void openFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose file to open");
        fileChooser.getExtensionFilters().setAll(
            new FileChooser.ExtensionFilter("All files", "*.*"),
            new FileChooser.ExtensionFilter("Text", "*.txt"),
            new FileChooser.ExtensionFilter("C++", "*.cpp"),
            new FileChooser.ExtensionFilter("Java", "*.java"),
            new FileChooser.ExtensionFilter("Python", "*.py")
        );

        File file = fileChooser.showOpenDialog(primaryStage);
        if(file != null){
            try{
                String content = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
                textArea.setText(content);
                currentFile = file;
                primaryStage.setTitle("EnCodeter IDE - " + file.getName());
                updateStatus();
            }catch(IOException e){
                showAlert(String.format("Error: cannot open file: %s", e.getMessage()));
            }
        }
    }

    private void saveFile(){
        if(currentFile == null){
            saveAsFile();
        }else{
            save(currentFile);
        }
    }

    private void saveAsFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save file");
        fileChooser.getExtensionFilters().setAll(
            new FileChooser.ExtensionFilter("All files", "*.*"),
            new FileChooser.ExtensionFilter("Text", "*.txt"),
            new FileChooser.ExtensionFilter("C++", "*.cpp"),
            new FileChooser.ExtensionFilter("Java", "*.java"),
            new FileChooser.ExtensionFilter("Python", "*.py")
        );

        File file = fileChooser.showSaveDialog(primaryStage);
        save(file);
    }

    private void save(File file){
        if(file != null){
            try{
                Files.write(Paths.get(file.getAbsolutePath()), textArea.getText().getBytes());
                currentFile = file;
                primaryStage.setTitle("EnCodeter IDE - " + file.getName());
                updateStatus();
            }catch(IOException e){
                showAlert(String.format("Error: cannot save file: %s", e.getMessage()));
            }
        }
    }

    private void showAboutMessage(){
        String aboutMessage = "EnCodeter IDE\nVersion 1.0 Alpha 0\nDeveloped by Encodeter Devtools.\n";
        // textArea.appendText("\n" + aboutMessage);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setContentText(aboutMessage);
        alert.showAndWait();
    }

    private void exitApplication(){
        primaryStage.close();
    }

    private HBox createStatusBar(){
        HBox statusBar = new HBox();
        statusBar.setPadding(new Insets(5, 10, 5, 10));
        statusBar.setStyle("-fx-background-color: #f0f0f0;");

        statusLabel = new Label("Ready");
        statusBar.getChildren().add(statusLabel);

        return statusBar;
    }

    private void updateStatus(){
        int length = textArea.getText().length();
        int lines = textArea.getText().split("\n", -1).length;
        statusLabel.setText(String.format("Length: %d | Lines: %d", length, lines));
    }

    private void showAlert(String errorMessage){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }
}
