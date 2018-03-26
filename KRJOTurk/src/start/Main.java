package start;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage window) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/resources/view/login.fxml"));
        window.getIcons().addAll(new Image("/images/books.png"));
        window.setTitle("OADTurk");
        window.setScene(new Scene(root, 800, 600));
        window.show();
    }

// <div>Icons made by <a href="http://www.freepik.com" title="Freepik">Freepik</a>
// from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a> is l
// icensed by <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons
// BY 3.0" target="_blank">CC 3.0 BY</a></div>

    @Override
    public void stop() throws Exception {
        System.out.println("Stop");
        //TODO: update all data, that was changed, and save them into DB.
    }

    public static void main(String[] args) {
        launch(args);
    }
}
