package MODEL;



public class ObiektNaRadarze {
	
	double xx;
	double yy;
	
	double odleglosc;
	
	double xxradar;
	double yyradar;
	
	double stopnie=0;
	
	int ID;

	public ObiektNaRadarze(double x, double y,int ID) {

		
		xx=x;
		yy=y;
		
		yyradar=-y +250;
		xxradar=x-250;
		this.ID=ID;

		Obliczanie_kata();

	}
	
	public void korekta_polozenia(double x, double y) {
		xx=x;
		yy=y;
		
		yyradar=-y +250;
		xxradar=x-250;
		Obliczanie_kata();
	}
	
	
	private void Obliczanie_kata() {
	
		if(xxradar>=0 && yyradar>=0) {
			
			stopnie= Math.toDegrees(Math.atan(xxradar/yyradar));
					
			odleglosc =xxradar/Math.sin(Math.toRadians(stopnie));
		}
		else if(xxradar>=0 && yyradar<0) {
			
			stopnie= Math.toDegrees(Math.atan(Math.abs(yyradar)/xxradar))+90;	
			
			odleglosc =Math.abs(yyradar)/Math.sin(Math.toRadians(stopnie-90));
		}
		else if(xxradar<0 && yyradar<0){
			
			stopnie= Math.toDegrees(Math.atan(Math.abs(xxradar)/Math.abs(yyradar)))+180;
			
			odleglosc =Math.abs(xxradar)/Math.sin(Math.toRadians(stopnie-180));
						
		}
		else if(xxradar<0 && yyradar>=0)
		{
			stopnie= Math.toDegrees(Math.atan(yyradar/Math.abs(xxradar)))+270;
			
			odleglosc =(yyradar)/Math.sin(Math.toRadians(stopnie-270));
			
		}
		
	
	}
	
	@Override
	public String toString() {
		
		return "ID: "+ID+" "+"X: "+(float)xx+" "+"Y:"+(float)yy +" ";
	}
}
