package MODEL;

import java.io.File;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

import edu.nps.moves.dis.Pdu;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class InterfejsAplikacji {
	
//***ZMIENNE
	private 
			Thread[] W¹tki;
			
	LinkedBlockingQueue<Pdu> OdebranePDU;
	
	List<ObiektNaRadarze> ListaObiektów;
	
	
			
			MulticastSocket GniazdoKomunikacyjne;
			
			String NumerIp;
			
			int NumerPortu;
			
			String Sciezka_Do_BazyDanych;			
			 
			Timer  timer;
			
			Timer timer2;
			
			double obrot_lini;
			
			DatagramSocket GniazdoRadarOdbior;
			
			
			

			
//*****

			
//***LOGIKA			
	public InterfejsAplikacji() throws SocketException {
		
		W¹tki= new Thread[3];
		
		OdebranePDU = new LinkedBlockingQueue<>();
		
		ListaObiektów = Collections.synchronizedList(new ArrayList<ObiektNaRadarze>());
		
		obrot_lini=0;
		
		timer= new Timer();
		
		timer2 = new Timer();
		
		GniazdoRadarOdbior= new DatagramSocket(60111);
		
		
		
	}
	
	
	
	public void UtworzGniazdoKomunikacyjne(String a ,String b, String c, String d,String port,TextField OkienkoTekstoweWyjsciowe) {
		
		StringBuilder sb= new StringBuilder();
		
    	try {
    		
    		sb.append(Integer.toString(Integer.parseInt(a)));
    		sb.append(".");
    		sb.append(Integer.toString(Integer.parseInt(b)));
    		sb.append(".");
    		sb.append(Integer.toString(Integer.parseInt(c)));
    		sb.append(".");
    		sb.append(Integer.toString(Integer.parseInt(d)));
    		
    		this.NumerIp=sb.toString();
    		
    		
    		
    		this.NumerPortu=Integer.parseInt(port);
    		
    		try {
    			
    			GniazdoKomunikacyjne= new MulticastSocket(NumerPortu);
    			
    			GniazdoKomunikacyjne.joinGroup(InetAddress.getByName(NumerIp));
    			
    			OkienkoTekstoweWyjsciowe.setText("Ustawiono po³¹czenie, Adres: "+NumerIp+" port: "+NumerPortu);
    			
    		} catch (IOException e) {
    			
    			OkienkoTekstoweWyjsciowe.setText("Cos sie popsu³o");
    			
    			e.printStackTrace();
    			
    		}
    		
    		
    		
		} catch (NumberFormatException e) {
			
			OkienkoTekstoweWyjsciowe.setText("Z³y format przy zapisie numeru Ip b¹dz numeru portu");
			
			e.printStackTrace();
			
			sb= new StringBuilder();
			
		}
    	
    	
	}

	
	public void odbieraj_i_Zapisuj_PDU(TextField OkienkoTekstoweWyjsciowe) {
		
		try {
			
			W¹tki[0]= new PDUReceiver(OdebranePDU, GniazdoKomunikacyjne);
			
			W¹tki[0].start();
			
		} catch (IOException e) {
			
			OkienkoTekstoweWyjsciowe.setText("ERROR nie mo¿na odbieraæ Pakietów danych");
			
			e.printStackTrace();
			
		}
	}

	public void zarzadzaj_PDu(TextField OkienkoTekstoweWyjsciowe,boolean odbieramy_wszystko,Pane pane,ListView<ObiektNaRadarze> Listawykrytych) {
		
		W¹tki[1]= new Zarz¹dzaniePDU(OdebranePDU, GniazdoKomunikacyjne,Sciezka_Do_BazyDanych,odbieramy_wszystko,OkienkoTekstoweWyjsciowe,ListaObiektów, Listawykrytych);
		
		W¹tki[1].start();
	}
	public void Komunikuj_z_radarem(Pane Pane_podk³ad,ImageView Image_Linia,TextField OkienkoTekstoweWyjsciowe) throws IOException{
		
		W¹tki[2]= new OdbiorOdRadaru(Pane_podk³ad,ListaObiektów,Image_Linia,GniazdoRadarOdbior,GniazdoKomunikacyjne,OkienkoTekstoweWyjsciowe);
		
		W¹tki[2].start();
	}
	
	
	public void Wybierz_folder_do_zapisu_BazyDanych(TextField OkienkoTekstoweWyjsciowe, Label label_sciezka) {
		
		DirectoryChooser WybierzFolder = new DirectoryChooser();
		
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				
				File Sciezka = WybierzFolder.showDialog(new Stage());
				
				Sciezka_Do_BazyDanych=Sciezka.getAbsolutePath();
				
				label_sciezka.setText(Sciezka_Do_BazyDanych);
				
			}
		});
	}
	
	public void Poruszanie_lini(ImageView linia,Pane pane,Circle c) {
			
		TimerTask  zadanie= new TimerTask() {
				
			@Override
			public void run() {
				/*
				for(ObiektNaRadarze x:ListaObiektów)
				{
					
					if(x.stopnie>obrot_lini-0.1 && x.stopnie<obrot_lini+0.1) {
						
						if( ((x.xxradar*x.xxradar)+(x.yyradar*x.yyradar))<250*250  ){
						
						
						Platform.runLater(new Runnable() {
							
							@Override
							public void run() {
								Rectangle r = new Rectangle(5, 5);
										r.setLayoutX(x.xx);
								r.setLayoutY(x.yy);
								r.setFill(Color.GREEN);
								r.setVisible(true);
								pane.getChildren().add(r);
								System.out.println(pane.getChildren().size());
							}
						});
					}
					}
					
				}

				
				Zmien_obrot_lini();
				
				
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						linia.setRotate(obrot_lini);
					}
				});
				*/
			}
		
		
		};
	
		TimerTask zadanie2 = new TimerTask() {
			
			@Override
			public void run() {
				
				
				
				Platform.runLater(new Runnable() {
					ArrayList<Node> lista = new ArrayList<>();
					@Override
					public void run() {
						for(Node x:pane.getChildren()) {
							
							if(x instanceof Rectangle) {
								x.setOpacity(x.getOpacity()-0.1);
								if(x.getOpacity()<=0) {
									lista.add(x);
								}
							}
							
						}
						pane.getChildren().removeAll(lista);
						
					}
				});
				
			}
		};
		
		timer.scheduleAtFixedRate(zadanie, 2,2);
		timer2.scheduleAtFixedRate(zadanie2, 1000, 1000);
	}
	public void Zmien_obrot_lini() {
		
		this.obrot_lini=this.obrot_lini+0.072;
		
		if (this.obrot_lini>=360)this.obrot_lini=0;
		
	}

	
	
//*****
}
