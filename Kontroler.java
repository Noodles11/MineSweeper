package saper;

import java.awt.Graphics2D;
import java.awt.Color;
import java.lang.Math.*;
import java.awt.Font;
import java.awt.RenderingHints;
import java.awt.*;

import javax.swing.JFrame;

public class Kontroler{

	private boolean automatyczneOdkrywanie; //odkrywanie pol sasiadujacych przy kliknieciu na pole
	private int liczbaBomb;					//calosciowa ilosc min na planszy

	private boolean gameGoing;	//czy gra trwa
	private int score;			//wynik gry

	private int maxTime;		//ile czasu na znalezienie wszystkich min
	private int timeLeft;		//ile czasu zostalo do konca

	private int poleSize;		//wymiary planszy
	private int kolumny;		//
	private int wiersze;		//

	public Kontroler(){
		this.automatyczneOdkrywanie = true;
		this.liczbaBomb = 10;
		this.gameGoing = false;
		this.score = 0;

		this.maxTime = 60; //5 minut
		this.timeLeft = this.maxTime;

		this.poleSize = 25;
		this.kolumny = 25;
		this.wiersze = 25;
	}

	public void setWymiary(int x, int y, int pole){
		this.wiersze = x<=30?(x>=8?x:8):30;
		this.kolumny = y<=24?(y>=8?y:8):24;
		this.poleSize = pole;				
	}
	public int[] getWymiary(){
		
		int[] wymiary = new int[3];
		wymiary[0] = this.wiersze;
		wymiary[1] = this.kolumny;
		wymiary[2] = this.poleSize;

		return wymiary;
	}

	public int przelicznikWyniku(){
		return (getLiczbaBomb()*getTimeLeft())+(getWymiary()[0]*getWymiary()[1]/10);
	}

	public void setTimeLeft(int seconds){
		this.timeLeft = seconds;
	}
	public int getTimeLeft(){
		return this.timeLeft;
	}

	public void addPoints(int points){
		this.score+=points;
	}
	public void removePoints(int points){
		this.score-=points;
	}
	public void setPoints(int score){
		this.score=score;
	}
	public int getScore(){
		return this.score;
	}
	public void resetScore(){
		this.score=0;
	}

	public int getMaxTime(){
		return this.maxTime;
	}
	public void setMaxTime(int maxtime){
		this.maxTime = maxtime;
	}

	public boolean getAutoOdkr(){
		return this.automatyczneOdkrywanie;
	}
	public void setAutoOdkr(boolean flag){
		this.automatyczneOdkrywanie = flag;
	}

	public boolean isGameGoing(){
		return this.gameGoing;
	}
	public void setGameGoing(boolean flag){
		this.gameGoing = flag;		
	}

	public int getLiczbaBomb(){
		return this.liczbaBomb;
	}
	public void setLiczbaBomb(int n){
		if( n < (this.wiersze-1)*(this.kolumny-1) ){
			this.liczbaBomb = n>=10?n:10;
		} else {
			this.liczbaBomb = (this.wiersze-1)*(this.kolumny-1);
		}
	}

	//---------------------------------------------------------------

}