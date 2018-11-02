package saper;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.List;

import java.util.*;

public class ArtificialPlayer{

	private int speed;
	private Timer timer;
	private boolean gameGoing, ruchWykonany;

	private int wiersze, kolumny, wlk, iloscBomb;
	private int ktoryRuch;

	private Plansza plansza;

	private List<KrokAI> listaKrokow;
	private List<KrokAI> listaCzyszczacych;
	private List<Pole> listaPolOflagowanych;
	private List<Pole> listaPolOdklikane;

	public ArtificialPlayer(Plansza p, int speed){
		this.plansza = p;

		this.speed = speed;
		this.gameGoing = false;	
		this.ruchWykonany = false;	
		this.ktoryRuch = 0;	
		
		this.czyscListeKrokow();
		this.czyscListeCzyszczacych();
		this.czyscListePol();

		this.setTimer();

		configurePlanszaSpecs();
	}

	public void configurePlanszaSpecs(){
		int[] specs = this.plansza.getPlanszaSpecs();

		this.wiersze = specs[0];
		this.kolumny = specs[1];
		this.wlk = specs[2];
		this.iloscBomb = specs[3];
	}

	public void setRuchWykonany(boolean w){
		this.ruchWykonany = w;
	}
	public boolean isRuchWykonany(){
		return this.ruchWykonany;
	}

	public void signalStop(){
		this.timer.stop();
		this.gameGoing = false;
	}
	public void signalStart(){
		this.timer.start();
		this.gameGoing = true;
	}
	public boolean getSignal(){
		return this.gameGoing;
	}

	public void setTimer(){
		this.timer = new Timer(this.speed, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {

                	makeMove();
            }    
        });
	}

	public void wyczyscBledneFlagi(){

		Pole wybranePole;
		Pole[][] tabelaPol = this.plansza.getTabelaPol();

		this.czyscListePol();
		this.czyscListeKrokow();

		for( int w=0; w<tabelaPol.length; w++ ){
			for( int k=0; k<tabelaPol[w].length; k++ ){
				wybranePole = this.plansza.getPoleByNumber(w,k);
				if( wybranePole.isPolePressed() && wybranePole.getNearBombs()<this.plansza.ileOtaczajacych(w,k,"marked") ){
					this.flagujOtaczajace(w,k,0);
				}
			}
		}

	}
	public boolean czySaBledy(){

		// System.out.print("sprawdzam czy sa bledy ---> ");

		Pole wybranePole;
		Pole[][] tabelaPol = this.plansza.getTabelaPol();

		for( int w=0; w<tabelaPol.length; w++ ){
			for( int k=0; k<tabelaPol[w].length; k++ ){
				wybranePole = this.plansza.getPoleByNumber(w,k);

				if( wybranePole.isPolePressed() && wybranePole.getNearBombs()<this.plansza.ileOtaczajacych(w,k,"marked") ){
					// System.out.println("TAK: --->"+w+","+k);
					return true;
				}
			}
		}

		// System.out.println("NIE");
		return false;

	}

	public void czyscListeKrokow(){
		this.listaKrokow = new ArrayList<KrokAI>();
	}
	public void czyscListeCzyszczacych(){
		this.listaCzyszczacych = new ArrayList<KrokAI>();
	}
	public void czyscListePol(){
		this.listaPolOflagowanych = new ArrayList<Pole>();
		this.listaPolOdklikane = new ArrayList<Pole>();
	}

	public void makeMove(){

		if( czySaBledy() ){
			if( this.listaCzyszczacych.size()==0 ){
				// System.out.println("generuje liste naprawcza");
				this.wyczyscBledneFlagi();
				this.setRuchWykonany(true);
			}
			else {
				KrokAI krok = this.listaCzyszczacych.get(0);
				this.plansza.klikMyszy(krok.x,krok.y,krok.lpm,krok.ppm,krok.clicks);
				this.listaCzyszczacych.remove(0);
				// System.out.println("pozostalo zadan naprawczych: "+this.listaCzyszczacych.size());
			}
		} else {
			this.czyscListeCzyszczacych();

			// System.out.print("zwykly krok ---> ");
		
			if( this.listaKrokow.size()>0 ){
				KrokAI krok = this.listaKrokow.get(0);
				this.plansza.klikMyszy(krok.x,krok.y,krok.lpm,krok.ppm,krok.clicks);
				this.listaKrokow.remove(0);
				// System.out.println("pozostalo zadan: "+this.listaKrokow.size());
			}
		}
	}

	public void wykonajKrokAI(){

		if( this.plansza.isCurrentlyPlaying() || this.ktoryRuch==0 ){

			if( this.listaKrokow.size()==0 && this.listaCzyszczacych.size()==0 ){

				Pole wybranePole;
				Pole[][] tabelaPol = this.plansza.getTabelaPol();

				if( this.isRuchWykonany() ){
					this.setRuchWykonany(false);
							//przeszukuje cala tablice i znajduje pola otoczone minami
							//jesli ilosc nieodkrytych pol naokolo odpowiada numerze na polu to oznaczam nieklikniete pola flaga
								for( int w=0; w<tabelaPol.length; w++ ){
									for( int k=0; k<tabelaPol[w].length; k++ ){
										wybranePole = this.plansza.getPoleByNumber(w,k);
										if( wybranePole.getNearBombs()>0 ){
											if( wybranePole.getNearBombs()==this.plansza.ileOtaczajacych(w,k,"unpressed") ){
												flagujOtaczajace(w,k,1);
											} else if( wybranePole.isPolePressed() && wybranePole.getNearBombs()==this.plansza.ileOtaczajacych(w,k,"marked") && this.plansza.ileOtaczajacych(w,k,"unpressed")>this.plansza.ileOtaczajacych(w,k,"marked") ){
												this.listaKrokow.add(new KrokAI(1,wybranePole.getXpos(),wybranePole.getYpos(),true,false,2,"dwuklik pola ("+wybranePole.getArrayX()+","+wybranePole.getArrayY()+")"));
												this.listaPolOdklikane.add(wybranePole);
												this.setRuchWykonany(true);
											}
										}
									}
								}
				} 
				else {
										//wybieram losowe nie klikniete i nie oznaczone pole i klikam je
						do {
							double x = Math.round(Math.random()*(this.wiersze-1));
							double y = Math.round(Math.random()*(this.kolumny-1));

							wybranePole = this.plansza.getPoleByNumber((int)x,(int)y);
						} while( wybranePole.isPolePressed() || wybranePole.getMarker()!=0 );

						this.listaKrokow.add(new KrokAI(1,wybranePole.getXpos(),wybranePole.getYpos(),true,false,1,"klikniecie w losowym miejscu ("+wybranePole.getArrayX()+","+wybranePole.getArrayY()+")"));
						this.setRuchWykonany(true);
					
				}


			}

			makeMove();

		}
		
	}
	public void flagujOtaczajace(int row, int col, int mark){

		Pole[][] tabelaPol = this.plansza.getTabelaPol();


		for(int wiersz=row-1;wiersz<=row+1;wiersz++){
			for(int kolumna=col-1;kolumna<=col+1;kolumna++){
				if(wiersz>=0 && wiersz<tabelaPol.length && kolumna>=0 && kolumna<tabelaPol[0].length && !(wiersz==row && kolumna==col))
				{
					if( mark==0 && tabelaPol[wiersz][kolumna].getMarker()==1 ){
						KrokAI krok = new KrokAI(0,tabelaPol[wiersz][kolumna].getXpos(),tabelaPol[wiersz][kolumna].getYpos(),false,true,1,"usuniecie blednej flagi z pola ("+tabelaPol[wiersz][kolumna].getArrayX()+","+tabelaPol[wiersz][kolumna].getArrayY()+")");
						if( !this.listaCzyszczacych.contains(krok) ){
							// System.out.println("+ odflagowanie "+wiersz+","+kolumna);
							this.listaCzyszczacych.add(krok);
							this.setRuchWykonany(true);
						}
					} else if( mark!=0 && !tabelaPol[wiersz][kolumna].isPolePressed() && tabelaPol[wiersz][kolumna].getMarker()!=1 && !this.listaPolOflagowanych.contains(tabelaPol[wiersz][kolumna]) ){
						this.listaKrokow.add(new KrokAI(1,tabelaPol[wiersz][kolumna].getXpos(),tabelaPol[wiersz][kolumna].getYpos(),false,true,1,"oznaczenie flaga pola ("+tabelaPol[wiersz][kolumna].getArrayX()+","+tabelaPol[wiersz][kolumna].getArrayY()+")"));
						this.listaPolOflagowanych.add(tabelaPol[wiersz][kolumna]);
						this.dwuklikObokZamarkowanego(wiersz,kolumna);
						this.setRuchWykonany(true);
					}
						
				}
			}
		}

	}
	public void dwuklikObokZamarkowanego(int row, int col){

		Pole[][] tabelaPol = this.plansza.getTabelaPol();
		Pole wybranePole;

		for(int wiersz=row-1;wiersz<=row+1;wiersz++){
			for(int kolumna=col-1;kolumna<=col+1;kolumna++){
				if(wiersz>=0 && wiersz<tabelaPol.length && kolumna>=0 && kolumna<tabelaPol[0].length && !(wiersz==row && kolumna==col))
				{
					wybranePole = this.plansza.getPoleByNumber(wiersz,kolumna);

		// System.out.println("------:"+wiersz+","+kolumna+"----[klik:"+(wybranePole.isPolePressed()?"tak":"nie")+"][bomb:"+wybranePole.getNearBombs()+"][mark-obok:"+this.plansza.ileOtaczajacych(wiersz,kolumna,"marked")+"][nieklik-obok:"+this.plansza.ileOtaczajacych(wiersz,kolumna,"unpressed")+"][naliscie:"+(this.listaPolOdklikane.contains(wybranePole)?"tak":"nie")+"]");

					if( wybranePole.isPolePressed() && wybranePole.getNearBombs()>0 && !this.listaPolOdklikane.contains(wybranePole) ){
						this.listaKrokow.add(new KrokAI(1,tabelaPol[wiersz][kolumna].getXpos(),tabelaPol[wiersz][kolumna].getYpos(),true,false,2,"dwuklik pola ("+tabelaPol[wiersz][kolumna].getArrayX()+","+tabelaPol[wiersz][kolumna].getArrayY()+")"));
						this.listaPolOdklikane.add(wybranePole);
						this.setRuchWykonany(true);
					}
				}
			}
		}

	}	


}