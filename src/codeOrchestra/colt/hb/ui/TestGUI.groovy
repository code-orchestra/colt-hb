package codeOrchestra.colt.hb.ui

import codeOrchestra.colt.core.ui.ColtApplication
import com.sun.javafx.css.StyleManager
import javafx.application.Application
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

/**
 * @author Dima Kruk
 */
class TestGUI extends Application {
    @Override
    void start(Stage primaryStage) throws Exception {
        StyleManager.getInstance().addUserAgentStylesheet(ColtApplication.getResource("style/main.css").toString());
        Parent root = new HBApplicationGUI();
        primaryStage.setTitle("COLT — HabraHabr editor");
        Scene scene = new Scene(root, 1038, 768)
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(TestGUI, args);
    }
}
