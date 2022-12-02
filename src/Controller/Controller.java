package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import MODEL.InterfejsAplikacji;
import MODEL.ObiektNaRadarze;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;


public class Controller implements Initializable
{
	private 
		InterfejsAplikacji  Interfejs;

	    @FXML
	    private ResourceBundle resources;

	    @FXML
	    private URL location;
	    
	    @FXML
	    private Button Button_PobierzIp_iNumerPortu;
	    
	    @FXML
	    private Button Button_WybierzFolder;
	    
	    @FXML
	    private TextField TextField_NumerIp1;

	    @FXML
	    private TextField TextField_NumerIp2;

	    @FXML
	    private TextField TextField_NumerIp3;

	    @FXML
	    private TextField TextField_NumerIp4;
	    
	    @FXML
	    private ListView<ObiektNaRadarze> List_view_listaWszystkich;

	    @FXML
	    private TextField TextField_WyjscieInformacji;
	    
	    @FXML
	    private Button Button_InicjalizacjaGniazda;
	    
	    @FXML
	    private TextField TextField_NumerPortu;
	    
	    @FXML
	    private Label Label_WybranyFolder;
	    
	    @FXML
	    private ImageView Image_Linia;

	    @FXML
	    private CheckBox ChechBox_zapisujeWys³ane;

	    @FXML
	    private CheckBox CheckBox_zapisujeWszystkieodebrane;
	    
	    @FXML
	    private Pane Pane_podk³ad;
	    
	    @FXML
	    private Circle Circle_kolo;
	    

	    
	    
	    //Konfiguracja po³¹czenie sieciowego
	    @FXML
	    void Button_PobierzIp_iNumerPortu_OnMouseClicked(MouseEvent event) throws IOException {
	 
	    	Interfejs.UtworzGniazdoKomunikacyjne(TextField_NumerIp1.getText(), TextField_NumerIp2.getText(), TextField_NumerIp3.getText(), TextField_NumerIp4.getText(), TextField_NumerPortu.getText(), TextField_WyjscieInformacji);
	    	
	    }

	    @FXML
	    void Button_Start_onMouseClick(MouseEvent event) throws IOException {
	    	
	    	Interfejs.odbieraj_i_Zapisuj_PDU(TextField_WyjscieInformacji);
	    	
	    	Interfejs.zarzadzaj_PDu(TextField_WyjscieInformacji, CheckBox_zapisujeWszystkieodebrane.isSelected(),Pane_podk³ad,List_view_listaWszystkich);
	   	
	    	Interfejs.Poruszanie_lini(Image_Linia,Pane_podk³ad,Circle_kolo);
	    	
	    	Interfejs.Komunikuj_z_radarem(Pane_podk³ad, Image_Linia,TextField_WyjscieInformacji);
	    }

	    @FXML
	    void Button_WybierzFolder_MouseClicked(MouseEvent event) {
	    	
	    	Interfejs.Wybierz_folder_do_zapisu_BazyDanych(TextField_WyjscieInformacji, Label_WybranyFolder);
	    	
	    }
	    
		@Override
		public void initialize(URL arg0, ResourceBundle arg1) {
			
		}
		
		public void SetInterfejs(InterfejsAplikacji Interfejs) {
			this.Interfejs=Interfejs;

		}
		
}






