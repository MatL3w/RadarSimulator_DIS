package MODEL;

import java.net.MulticastSocket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import edu.nps.moves.dis.EntityStatePdu;
import edu.nps.moves.dis.Pdu;
import javafx.application.Platform;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;


public class Zarz¹dzaniePDU extends Thread{
	
	LinkedBlockingQueue<Pdu> KolejkaPDU;
	
	MulticastSocket Gniazdo;
	
	Connection Polaczenie_z_Baz¹Danych;
	
	String sciezka_do_folderu;
	
	boolean odbieramy_wszystko;
	
	TextField okienko_wyjsciowe;
	
	List<ObiektNaRadarze> ListaObiektów;
	
	ListView<ObiektNaRadarze> Listawykrytych;
	
	Pane Pane;
	
	public Zarz¹dzaniePDU(LinkedBlockingQueue<Pdu> KolejkaPDU,MulticastSocket Gniazdo, String sciezka,boolean odbieramy_wszystko,TextField Oknowyjsciowe,List<ObiektNaRadarze> ListaObiektów,ListView<ObiektNaRadarze> Listawykrytych) {
		
		this.Gniazdo=Gniazdo;
		
		this.KolejkaPDU=KolejkaPDU;
		
		this.sciezka_do_folderu=sciezka;
		
		this.odbieramy_wszystko=odbieramy_wszystko;
		
		this.okienko_wyjsciowe=Oknowyjsciowe;
		
		this.ListaObiektów = ListaObiektów;
		
		this.Listawykrytych=Listawykrytych;
		
	}

	@Override
	public void run() {
		
		boolean czy_zapisac=false;
		
		Statement stmt;
		
		try {
			
			String url = "jdbc:sqlite:"+sciezka_do_folderu+ "\\" +LocalDate.now()+" "+LocalTime.now().getHour()+" "+LocalTime.now().getMinute()+".db";
			
			Polaczenie_z_Baz¹Danych = DriverManager.getConnection(url);
			
			stmt = Polaczenie_z_Baz¹Danych.createStatement();
			
			 String tworzenie_tabeli = "CREATE TABLE IF NOT EXISTS PDUs (\n"
		                + "	id integer PRIMARY KEY AUTOINCREMENT,\n"
		                + "	Time text NOT NULL,\n"
		                + "	Status text,\n"
		                + "	PDU blob\n"
		                + ");";
			 stmt.execute(tworzenie_tabeli);
			
		}catch (SQLException e) {
			
			okienko_wyjsciowe.setText("nie mozna zapisac do bazydanych!!");
			e.printStackTrace();
			
			Thread.interrupted();
		}
		
		String sql = "INSERT INTO PDUs(Time,Status,PDU) VALUES(?,?,?)";
		Pdu pdu=null;
		while(true) {
			
			pdu=KolejkaPDU.poll();
			
			czy_zapisac=true;
			
			if(pdu!=null){
				
				if(odbieramy_wszystko) {
					
					try {
						
					 PreparedStatement pstmt = Polaczenie_z_Baz¹Danych.prepareStatement(sql);
					 
					 byte[] dane = pdu.marshal();					 
						 
				     pstmt.setString(1, LocalTime.now().toString());
				        
				     pstmt.setString(2,"Received");
				     
				     pstmt.setBytes(3,dane);
				     		     
				     pstmt.executeUpdate();

					czy_zapisac=false;
					
					
					}
					catch (SQLException e) {
						
						e.printStackTrace();
						
					}
					
					
				}
				
				if(pdu instanceof EntityStatePdu) {
					
					EntityStatePdu pduu =(EntityStatePdu)pdu;
					
				
					
					if(ListaObiektów.isEmpty() || Listawykrytych.getItems().isEmpty())
					{
						ObiektNaRadarze o = new ObiektNaRadarze(pduu.getEntityLocation().getX(), pduu.getEntityLocation().getY(), pduu.getEntityID().getEntity());
						ListaObiektów.add(o);
						
						Platform.runLater(new Runnable() {
							
							@Override
							public void run() {
								Listawykrytych.refresh();
								Listawykrytych.getItems().add(o);
							}
						});
					}
					else
					{
						boolean niebylo=true;
						
						for(ObiektNaRadarze x:ListaObiektów ){
							
							if(x.ID==pduu.getEntityID().getEntity()) {
								
								niebylo=false;
								
								x.korekta_polozenia(pduu.getEntityLocation().getX(), pduu.getEntityLocation().getY());
							}
						}
						
						for(ObiektNaRadarze x:Listawykrytych.getItems())
						{
								if(x.ID==pduu.getEntityID().getEntity()) {
								
								niebylo=false;
								
								x.korekta_polozenia(pduu.getEntityLocation().getX(), pduu.getEntityLocation().getY());
								Platform.runLater(new Runnable() {
									
									@Override
									public void run() {
										Listawykrytych.refresh();
										
									}
								});
							}
							
						}
						
						
						
						if(niebylo) {
							ObiektNaRadarze o = new ObiektNaRadarze(pduu.getEntityLocation().getX(), pduu.getEntityLocation().getY(), pduu.getEntityID().getEntity());
							ListaObiektów.add(o);
							
							
							
							Platform.runLater(new Runnable() {
								
								@Override
								public void run() {
									Listawykrytych.getItems().add(o);
									Listawykrytych.refresh();
									
								}
							});
						}
					}
					
					
					
				}
				
				
				
				
				
			}
			
		}
		
		

	}



}
