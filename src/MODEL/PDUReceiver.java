package MODEL;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.util.concurrent.LinkedBlockingQueue;

import edu.nps.moves.dis.Pdu;
import edu.nps.moves.disutil.PduFactory;

public class PDUReceiver extends Thread{

//***ZMIENNE
	private
	LinkedBlockingQueue<Pdu> OdebranePDU;
	
	MulticastSocket Gniazdo;
	
	byte[] buffor;
	
	DatagramPacket packet;
	
	PduFactory pdufactory;
	
	int portNumber;
	
	String Ipnumber;
	
//*****
	
//***LOGIKA
	public PDUReceiver(LinkedBlockingQueue<Pdu> OdebranePDU, MulticastSocket gniazdo) throws IOException {
		
		this.OdebranePDU= OdebranePDU;
		
		this.Gniazdo=gniazdo;
		
		buffor= new byte[10000];
		
		packet = new DatagramPacket(buffor, buffor.length);
		
		pdufactory = new PduFactory();
		

		
	}
	
	
	@Override
	public void run() {
		
		while(true)
		{
			
				try {
					
					Gniazdo.receive(packet);
					
				} catch (IOException e) {
				
					e.printStackTrace();
					
				}
				
				Pdu pdu = pdufactory.createPdu(packet.getData());
							
				try {
					OdebranePDU.add(pdu);	
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
						
						
		}
		
	}
	

	public void close() {
		this.Gniazdo.close();
	}
}
//*****