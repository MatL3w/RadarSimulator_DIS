package MODEL;

import java.io.IOException;
import Controller.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MAIN extends Application{
	
	private
		InterfejsAplikacji Interfejs;
	
	
	@Override
	public void start(Stage primaryStage) throws IOException {
			
			Interfejs = new InterfejsAplikacji();
			
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/view.fxml"));
			Parent parent = fxmlLoader.load();
			Controller controller = fxmlLoader.getController();
			controller.SetInterfejs(Interfejs);
			
			Scene scene = new Scene(parent);
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setResizable(false);

	}
	
	@Override
	public void stop() throws Exception {

	}
	public static void main(String[] args) {
		launch(args);
	}
	
}
