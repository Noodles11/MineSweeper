package saper;

import java.util.*;
import java.awt.Graphics2D;
import java.awt.Graphics;
import javax.swing.*;

import javax.swing.JPanel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Plansza extends JPanel implements MouseListener{

	private int wiersze, kolumny, wlk, ileBomb;
	private boolean autoOdkr;
	private Pole[][] tabelaPol;
	private boolean currentlyPlaying;
	private boolean gameStarted;
	private PanelGry mypanel;
	private ArtificialPlayer ai;
	private int tbv;

	public Plansza(int wiersze, int kolumny, int wielkosc, int ileBomb, boolean autoOdkr){
		this.wiersze = wiersze;
		this.kolumny = kolumny;
		this.wlk = wielkosc;
		this.ileBomb = ileBomb;
		this.autoOdkr = autoOdkr;
		this.gameStarted = false;

		this.tbv = 0;

		addMouseListener(this);

		ustawPola();

		this.ai = new ArtificialPlayer(this,1000);
	}

	public void makeAI(){
		this.ai.signalStart();
	}
	public void killAI(){
		this.ai.signalStop();
	}
	public ArtificialPlayer getAI(){
		return this.ai;
	}
	public boolean isAIplaying(){
		return this.ai.getSignal();
	}

	public Pole[][] getTabelaPol(){
		return this.tabelaPol;
	}
	public int getIloscBomb(){
		return this.ileBomb;
	}
	
	public void calculate3BV(){

		//tylko wyspy
		for( int w=0; w<this.tabelaPol.length; w++){
			for( int k=0; k<this.tabelaPol[0].length; k++){
				if( this.tabelaPol[w][k].getNearBombs()==0 && this.tabelaPol[w][k].getContent()!=1 && !this.tabelaPol[w][k].isCalculated() ){
					this.add3BV();
					// System.out.println("3VB+ "+w+","+k);
					findEmptySpacesAround(w,k);
				}
			}
		}

		//inne wymagajace klikniecia
		for( int w=0; w<this.tabelaPol.length; w++){
			for( int k=0; k<this.tabelaPol[0].length; k++){
				if( this.tabelaPol[w][k].getContent()!=1 && !this.tabelaPol[w][k].isCalculated() ){
					this.add3BV();
					// System.out.println("3VB+ "+w+","+k);
					this.tabelaPol[w][k].setCalculated(2);
				}
			}
		}

		this.mypanel.set3BV(this.get3BV());
	}
	public void findEmptySpacesAround(int row, int col){

		for(int wiersz=row-1;wiersz<=row+1;wiersz++){
			for(int kolumna=col-1;kolumna<=col+1;kolumna++){
				if(wiersz>=0 && wiersz<this.tabelaPol.length && kolumna>=0 && kolumna<this.tabelaPol[0].length && !(wiersz==row && kolumna==col))
				{
					if( !this.tabelaPol[wiersz][kolumna].isCalculated() ){
						this.tabelaPol[wiersz][kolumna].setCalculated(1);
						if( this.tabelaPol[wiersz][kolumna].getNearBombs()==0 ){
							findEmptySpacesAround(wiersz,kolumna);
						}
					}
				}
			}	
		}
	}
	public int get3BV(){

		return this.tbv;
	}
	public void add3BV(){

		this.tbv++;
	}


	public int[] getPlanszaSpecs(){
		int[] specs = new int[4];

		specs[0] = this.wiersze;
		specs[1] = this.kolumny;
		specs[2] = this.wlk;
		specs[3] = this.ileBomb;

		return specs;
	}

	public void startSequence(){
		this.gameStarted = true;
		this.rozstawMiny(this.ileBomb);
		this.uaktualnijPlansze();
		this.setCurrentlyPlaying(true);

		this.calculate3BV();
	}

	public void setPanel(PanelGry p){
		this.mypanel = p;
	}

	public void ustawPola(){

		this.tabelaPol = new Pole[this.wiersze][this.kolumny];

		for( int w=0; w<this.wiersze; w++){
			for( int k=0; k<this.kolumny; k++){
				this.tabelaPol[w][k] = new Pole(w*this.wlk,k*this.wlk,this.wlk);	//ustawianie wspolrzednych i wielkosci
				this.tabelaPol[w][k].setArrayNum(w,k);								//ustawianie numeru pola
			}
		}
	}

	public boolean isCurrentlyPlaying(){
		return this.currentlyPlaying;
	}
	public void setCurrentlyPlaying(boolean flag){
		this.currentlyPlaying = flag;

		if( !flag ){
			this.mypanel.timerStop();
			killAI();
			this.mypanel.btn_AI.setEnabled(false);
		}
		else{
			this.mypanel.startGame();
		}
	}

	public Pole getPoleOnXY(int x, int y){
		for( int w=0; w<this.tabelaPol.length; w++){
			for( int k=0; k<this.tabelaPol[0].length; k++){
				if( ( x >= this.tabelaPol[w][k].getXpos() && x < this.tabelaPol[w][k].getXpos()+this.tabelaPol[w][k].getSize()) && ( y >= this.tabelaPol[w][k].getYpos() && y < this.tabelaPol[w][k].getYpos()+this.tabelaPol[w][k].getSize()) )
					return this.tabelaPol[w][k];
			}
		}

		return new Pole(0,0,0);
	}
	public Pole getPoleByNumber(int w, int k){
		return this.tabelaPol[w][k];
	}

	public void rozstawMiny(int n){
		int count=0;

		do {
			double x = Math.round(Math.random()*(this.tabelaPol.length-1));
			double y = Math.round(Math.random()*(this.tabelaPol[0].length-1));
			if( this.tabelaPol[(int)x][(int)y].getContent()!=1 && !this.tabelaPol[(int)x][(int)y].isPolePressed() && count < n ){
				this.tabelaPol[(int)x][(int)y].setPoleContent(1);
				count++;
			}
		} while( count < n );
	}
	public void uaktualnijPlansze(){

		for( int w=0; w<this.tabelaPol.length; w++){
			for( int k=0; k<this.tabelaPol[0].length; k++){
				this.tabelaPol[w][k].setNearBombs(ileOtaczajacych(w,k,"mine"));
			}
		}
	}

	public int ileOtaczajacych(int row, int col, String type){

		int count=0;
		int pressed=0;
		int unpressed=0;
		int marked=0;
		for(int wiersz=row-1;wiersz<=row+1;wiersz++){
			for(int kolumna=col-1;kolumna<=col+1;kolumna++){
				if(wiersz>=0 && wiersz<tabelaPol.length && kolumna>=0 && kolumna<tabelaPol[0].length && !(wiersz==row && kolumna==col))
				{
						if( this.tabelaPol[wiersz][kolumna].getContent()==1 ) count++;
						if( this.tabelaPol[wiersz][kolumna].isPolePressed() ){
							pressed++;
						} else {
							unpressed++;
						}
						if( this.tabelaPol[wiersz][kolumna].getMarker()==1 ) marked++;
				}
			}	
		}

		this.tabelaPol[row][col].setNearBombs(count);

		if( type=="mine" )
			return count;
		else if( type=="pressed" )
			return pressed;
		else if( type=="unpressed" )
			return unpressed;
		else if( type=="marked" )
			return marked;
		else
			return 0;
	}


	public void mouseEntered(MouseEvent e){
        
    }
    public void mouseExited(MouseEvent e){

    }
    
    public void mouseClicked(MouseEvent e) {


    }
    public void mouseReleased(MouseEvent e){
        
    }
 
    public void mousePressed(MouseEvent e) {

    	if( this.mypanel.isBlinkFinished() ){
	    	int x = e.getX();
	    	int y = e.getY();

	    	boolean isLPM = SwingUtilities.isLeftMouseButton(e);
	    	boolean isRPM = SwingUtilities.isRightMouseButton(e);

	    	int clickCount = e.getClickCount();

	    	klikMyszy(x,y,isLPM,isRPM,clickCount);

	    	this.ai.czyscListeKrokow();
	    	this.ai.czyscListePol();
	    	this.ai.setRuchWykonany(true);
	    }
    }


    public void klikMyszy(int x, int y, boolean isLPM, boolean isRPM, int clickCount){
    	if( !this.gameStarted && !isCurrentlyPlaying() ){
	        Pole wybrane = this.getPoleOnXY(x, y);

	            if( isLPM && x>=0 && y>=0 ){
	            	wybrane.setPolePressed(true);
	            	this.startSequence();
	            }

	        repaint();
    	}

    	if( isCurrentlyPlaying() && this.gameStarted ){ //tylko jesli gra sie toczy
	        Pole wybrane = this.getPoleOnXY(x, y);

	        if( x>=0 && y>=0 ){
	            if( isLPM && wybrane.getMarker()!=1 ){ // lewy klik (nie wolno na oznaczone pole)
	            	wybrane.setPolePressed(true);

	            	if( wybrane.getContent()==1 ){
	                    pokazZawartoscPol();
	                    this.setCurrentlyPlaying(false);
	                } else {

	                	if( this.autoOdkr ){
		            		odkryjPustyObszar(wybrane.getArrayX(),wybrane.getArrayY());
		                    if (clickCount == 2) {
		                        odkryjObszarWokol(wybrane.getArrayX(),wybrane.getArrayY());
		                    }
		            	}
	                }

	            } else if( isRPM ){ //prawy klik
	            	wybrane.setMarker(wybrane.getMarker()==1?0:1);
	            }

	            int[] polaPlanszy = this.policzIleZamarkowanych();
		        int zamarkowane = polaPlanszy[0];
		        int nieodkryte = polaPlanszy[1];

		        if( (zamarkowane==this.ileBomb && nieodkryte==0) || nieodkryte==this.ileBomb ){
		        	pokazZawartoscPol();
                    this.setCurrentlyPlaying(false);
		        }
	        }
	        repaint();
        }
    }


    public int[] policzIleZamarkowanych(){
    	int[] bombCount = new int[4];
		int countMarkers=0;
		int countUnpressed=0;
		int countRevealed=0;
		for( int w=0; w<this.tabelaPol.length; w++){
			for( int k=0; k<this.tabelaPol[0].length; k++){
				if( this.tabelaPol[w][k].getMarker()==1 ) {
					countMarkers++;
				}
				if( !this.tabelaPol[w][k].isPolePressed() ){
					countUnpressed++;
				}
				if( this.tabelaPol[w][k].isRevealed() ){
					countRevealed++;
				}
			}
		}
		bombCount[0]=countMarkers;
		bombCount[1]=countUnpressed;
		bombCount[2]=(this.wiersze*this.kolumny)-countUnpressed; //pressed
		bombCount[3]=countRevealed; //revealed

		if(isCurrentlyPlaying()){
			this.mypanel.odswiezIleMin(this.ileBomb-countMarkers);
			// this.mypanel.odswiezWynik(countMarkers);
		}

		return bombCount;
	}

    public void odkryjPustyObszar(int row, int col){

		if( this.tabelaPol[row][col].isPolePressed() && this.tabelaPol[row][col].getNearBombs()==0 && this.tabelaPol[row][col].getContent()==0 ){
			for(int k=row-1;k<=row+1;k++){
				for(int w=col-1;w<=col+1;w++){
					if(k>=0 && k<this.tabelaPol.length && w>=0 && w<this.tabelaPol[0].length && !(k==row && w==col))
					{
						this.tabelaPol[k][w].setPolePressed(true);
							
						if( this.tabelaPol[k][w].getNearBombs()==0 && !this.tabelaPol[k][w].isRevealed() ){ //odkrywanie rekurencyjne
							this.tabelaPol[k][w].setRevealed(true);
							this.odkryjPustyObszar(k,w);
						}
					}
				}	
			}

		}	
	}
	public void odkryjObszarWokol(int row, int col){

		if( this.tabelaPol[row][col].isPolePressed() && this.tabelaPol[row][col].getContent()==0 ){
			int nearBombsCounter=0;

			for(int k=row-1;k<=row+1;k++){
				for(int w=col-1;w<=col+1;w++){
					if(k>=0 && k<this.tabelaPol.length && w>=0 && w<this.tabelaPol[0].length && !(k==row && w==col))
					{
						if( this.tabelaPol[k][w].getMarker()==1 || (this.tabelaPol[k][w].getContent()==1 && this.tabelaPol[k][w].isPolePressed()))
							nearBombsCounter++;
					}
				}	
			}

			if( this.tabelaPol[row][col].getNearBombs()==nearBombsCounter ){
				for(int k=row-1;k<=row+1;k++){
					for(int w=col-1;w<=col+1;w++){
						if(k>=0 && k<this.tabelaPol.length && w>=0 && w<this.tabelaPol[0].length && !(k==row && w==col))
						{
							if( this.tabelaPol[k][w].getNearBombs()==0 && this.tabelaPol[k][w].getContent()==0 ){ //odkrywanie rekurencyjne
								this.tabelaPol[k][w].setPolePressed(true);
								odkryjPustyObszar(k,w);
							}
							if( this.tabelaPol[k][w].getMarker()!=1 ){
								this.tabelaPol[k][w].setPolePressed(true);
							}
						}
					}	
				}
			}

		}	
	}








    public void pokazZawartoscPol(){
		for( int row=0; row<this.tabelaPol.length; row++){
			for( int col=0; col<this.tabelaPol[row].length; col++){
				this.tabelaPol[row][col].setRevealed(true);

				if( this.tabelaPol[row][col].getMarker()==1 && this.tabelaPol[row][col].getContent()==1 )
					this.tabelaPol[row][col].setMarker(100);
				else if( this.tabelaPol[row][col].isPolePressed() && this.tabelaPol[row][col].getContent()==1 )
					this.tabelaPol[row][col].setMarker(404);
			}
		}
	}


	public void rysujPlansze(Graphics2D g2d){
	
		if( this.tabelaPol != null ){
			for( int row=0; row<this.tabelaPol.length; row++){
				for( int col=0; col<this.tabelaPol[0].length; col++){
					this.tabelaPol[row][col].drawPole(g2d);
					if(this.tabelaPol[row][col].getContent()!=1 && this.tabelaPol[row][col].isPolePressed() && this.tabelaPol[row][col].getNearBombs()!=0)
						this.tabelaPol[row][col].drawNumber(g2d,""+this.tabelaPol[row][col].getNearBombs());
				}
			}
		}
	}

	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
	    Graphics2D g2d = (Graphics2D) g;

        this.rysujPlansze(g2d);
	}
}