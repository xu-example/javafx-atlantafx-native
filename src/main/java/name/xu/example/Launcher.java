/* SPDX-License-Identifier: MIT */

package name.xu.example;

import atlantafx.base.theme.Dracula;
import atlantafx.base.theme.Styles;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Launcher extends Application {

    static Window window;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Application.setUserAgentStylesheet(new Dracula().getUserAgentStylesheet());

        BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(borderPane);
        window = borderPane.getScene().getWindow();
        VBox content = createView();
        borderPane.setCenter(content);
        borderPane.setPadding(new Insets(100));
        borderPane.getStyleClass().add("start-screen");
        primaryStage.setScene(scene);
        primaryStage.setWidth(800);
        primaryStage.setHeight(400);
        primaryStage.setTitle("功能示例");
        Resources.loadIcons(primaryStage);
        primaryStage.show();

    }

    private VBox createView() {
        DoubleProperty stateProperty = new SimpleDoubleProperty(0.5d);
        // 按钮
        var testButton = new Button();
        testButton.getStyleClass().add(Styles.ACCENT);
        testButton.setPrefWidth(150);
        testButton.textProperty().bind(Bindings.createStringBinding(
                () -> {
                    double state = stateProperty.get();
                    if (state == 0d) {
                        return "测试失败";
                    } else if (state == 1) {
                        return "测试成功";
                    } else if (state == -1d) {
                        return "测试中";
                    } else {
                        return "Google网络测试";
                    }
                },
                stateProperty
        ));

        // 状态
        var indicator = new ProgressIndicator(0);
        indicator.setMinSize(60, 60);
        indicator.progressProperty().bind(Bindings.createDoubleBinding(
                stateProperty::get,
                stateProperty
        ));


        testButton.setOnAction(event -> {
            testButton.setDisable(true);
            stateProperty.set(-1d);
            Task<Boolean> task = new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    return Utils.checkGoogle();
                }
            };
            task.setOnSucceeded(e -> {
                if (task.getValue()) {
                    stateProperty.set(1d);
                } else {
                    stateProperty.set(0d);
                }
                // 在任务完成后更新
                testButton.setDisable(false);
            });
            // 启动任务
            new Thread(task).start();
        });


        var svg2PngBtn = new Button("SVG 转 PNG");
        svg2PngBtn.getStyleClass().add(Styles.SUCCESS);
        svg2PngBtn.setPrefWidth(150);
        svg2PngBtn.setOnAction(e -> addFile());

        var controls = new VBox(10, indicator, testButton, svg2PngBtn);
        controls.setAlignment(Pos.CENTER);
        controls.setFillWidth(true);
        var content = new VBox(30, controls);
        content.getStyleClass().add("content");
        content.setAlignment(Pos.CENTER);
        content.setFillWidth(true);
        return content;
    }

    private void addFile() {
        List<String> extensions = new ArrayList<>();
        extensions.add("*.svg");
        var fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(
                "MP3 files (" + String.join(", ", extensions) + ")",
                extensions
        ));
        File file = fileChooser.showOpenDialog(window);

        if (file == null) {
            return;
        }
        FileSystemView fileSystemView = FileSystemView.getFileSystemView();
        File outDir = new File(fileSystemView.getHomeDirectory(), "svg-png");
        //桌面路径/生成文件
        Utils.svg2Png(file, outDir);

        // 打开生成结果的路径
        try {
            Desktop.getDesktop().open(outDir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
