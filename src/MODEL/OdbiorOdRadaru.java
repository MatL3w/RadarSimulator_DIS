package MODEL;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.List;

import SHARED.InformacjeOdradaru;
import SHARED.Plot;
import edu.nps.moves.dis.AcknowledgePdu;
import edu.nps.moves.dis.CollisionPdu;
import edu.nps.moves.dis.ElectronicEmissionsPdu;
import edu.nps.moves.dis.EntityID;
import edu.nps.moves.dis.EntityStatePdu;
import edu.nps.moves.dis.EventID;
import javafx.application.Platform;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import SHARED.InformacjeOdradaru;
public class OdbiorOdRadaru extends Thread{
	
	 Pane Pane_podk³ad;
	
	 List<ObiektNaRadarze> ListaObiektów;
	
	 ImageView Image_Linia;
	 
	DatagramSocket GniazdoRadarOdbior;
		
	MulticastSocket GniazdoKomunikacyjne;
	
	TextField OkienkoTekstoweWyjsciowe;
	 
	 
	public OdbiorOdRadaru(Pane Pane_podk³ad,List<ObiektNaRadarze> ListaObiektów,ImageView Image_Linia,DatagramSocket GniazdoRadarOdbior,MulticastSocket GniazdoKomunikacyjne,TextField OkienkoTekstoweWyjsciowe) throws IOException {
		this.Pane_podk³ad=Pane_podk³ad;
		
		this.ListaObiektów=ListaObiektów;
		
		this.Image_Linia=Image_Linia;
		
		this.GniazdoRadarOdbior=GniazdoRadarOdbior;
		
		this.GniazdoKomunikacyjne= new MulticastSocket();
		
		this.OkienkoTekstoweWyjsciowe = OkienkoTekstoweWyjsciowe;
		
	}
	@Override
	public void run() {
		

		
		while(true) {
			
			byte[] buffer = new byte[2000];
			try {
				
				GniazdoRadarOdbior.receive(new DatagramPacket(buffer, 2000));
			
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			  ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
	            ObjectInputStream ois=null;
				try {
					ois = new ObjectInputStream(bais);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	            try {
	                Object readObject = ois.readObject();
	                if(readObject instanceof InformacjeOdradaru) {
	                	
	                	InformacjeOdradaru i=(InformacjeOdradaru)readObject;
	                	
	                	Platform.runLater(new Runnable() {
							
							@Override
							public void run() {
								Image_Linia.setRotate(i.polozenie);
								
							}
						});
	                	
	                	
	                	for(ObiektNaRadarze x:ListaObiektów)
	    				{
	    					
	    					if(x.stopnie>i.polozenie-0.04 && x.stopnie<i.polozenie+0.04) {
	    						
	    						if( ((x.xxradar*x.xxradar)+(x.yyradar*x.yyradar))<250*250  ){
	    						
	    						
	    						Platform.runLater(new Runnable() {
	    							
	    							@Override
	    							public void run() {
	    								Rectangle r = new Rectangle(5, 5);
	    										r.setLayoutX(x.xx);
	    								r.setLayoutY(x.yy);
	    								r.setFill(Color.GREEN);
	    								r.setVisible(true);
	    								Pane_podk³ad.getChildren().add(r);
	    								
	    							
	    								
	    							}
	    						});
	    						
	    						Plot plot = new Plot();
	    						plot.x=x.xx;
	    						plot.y=x.yy;
	    						plot.odleglosc=x.odleglosc;
	    						plot.stopnie=x.stopnie;
	    						
	    						OkienkoTekstoweWyjsciowe.setText("wys³ano plot "+"X:"+(float)plot.x+" Y:"+(float)plot.y+" k¹t:"+(int)plot.stopnie+" odleglosc:"+(float)plot.odleglosc);
								
								 ByteArrayOutputStream baos = new ByteArrayOutputStream();
						    	 ObjectOutputStream oos = new ObjectOutputStream(baos);
						    	  
						    	 oos.writeObject(plot);
	    						
						    	 byte[] data = baos.toByteArray();
						    	 
						    	 GniazdoRadarOdbior.send(new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 60112));
						    	 
						    	 ElectronicEmissionsPdu lol = new ElectronicEmissionsPdu();
						    	 
						    	 AcknowledgePdu ack = new AcknowledgePdu();
						    	ack.setAcknowledgeFlag(x.ID);


						    		byte[] buf =ack.marshal();
						    		
									DatagramPacket d = new DatagramPacket(buf, buf.length, InetAddress.getByName("224.0.0.1"), 62040);
						    	 
						    	 GniazdoKomunikacyjne.send(d);
	    					}
	    					}
	    					
	    				}
	
	                }
	                
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
			
			
			
			
		}

	}

}
