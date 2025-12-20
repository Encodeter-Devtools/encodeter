package io.github.haohao123coding.encodeter;

import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class EditorFile{
    private File currentFile;

    public void newFile(TextArea textArea, Stage primaryStage){
        textArea.clear();
        currentFile = null;
        primaryStage.setTitle("EnCodeter IDE - New File");
    }

    public String openFile(TextArea textArea, Stage primaryStage){
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
                return "";
            }catch(IOException e){
                return String.format("Error: cannot open file: %s", e.getMessage());
            }
        }
        return "";
    }

    public String saveFile(TextArea textArea, Stage primaryStage){
        String alertMessage;
        if(currentFile == null){
            alertMessage = saveAsFile(textArea, primaryStage);
        }else{
            alertMessage = save(currentFile, textArea, primaryStage);
        }
        return alertMessage;
    }

    public String saveAsFile(TextArea textArea, Stage primaryStage){
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
        return save(file, textArea, primaryStage);
    }

    private String save(File file, TextArea textArea, Stage primaryStage){
        if(file != null){
            try{
                Files.write(Paths.get(file.getAbsolutePath()), textArea.getText().getBytes());
                currentFile = file;
                primaryStage.setTitle("EnCodeter IDE - " + file.getName());
                return "";
            }catch(IOException e){
                return String.format("Error: cannot save file: %s", e.getMessage());
            }
        }
        return "";
    }
}
