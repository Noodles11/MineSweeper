package saper;

import saper.*;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.GradientPaint;
import java.lang.Math.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Pole{

	private int size;
	private int content;
	private boolean pressedFlag;
	private int pX, pY;
	private int numX, numY;
	private int marker;
	// private int calculatedA;
	private int nearBombs;
	private boolean revealed, calculated;
	private int blink;
	private BufferedImage imgFlaga = null;
	private BufferedImage imgMine = null;
	// private BufferedImage imgDirt = null;
	private GradientPaint gradient;

	public Pole(int pX, int pY, int s){
		this.size = s;
		this.pX = pX;
		this.pY = pY;
		this.pressedFlag = false;
		this.marker = 0;
		this.revealed = false;
		this.calculated = false;
		// this.calculatedA = 0;
		this.blink = 0;

		this.gradient = new GradientPaint(this.pX, this.pY, new Color(247,237,212), this.pX+this.size, this.pY+this.size, new Color(160,160,160));

		try {
	        this.imgFlaga = ImageIO.read(new File("flag.png"));
	        this.imgMine = ImageIO.read(new File("mine.png"));
	        // this.imgDirt = ImageIO.read(new File("space.png"));
	    } catch (IOException e) {
	    	System.out.println("Unable to load images - flag, mine or dirt.");
	    }

	    // this.imgDirt = cropImage(this.imgDirt,(int)Math.round(Math.random()*256),(int)Math.round(Math.random()*256));
		
	}
	
	private BufferedImage cropImage(BufferedImage src, int x, int y) {
		if( x+this.size>255 ) x=255-this.size;
		if( y+this.size>255 ) y=255-this.size;

    	BufferedImage dest = src.getSubimage(x, y, this.size, this.size);

    	return dest; 
   }

   	public void setCalculated(int a){
   		this.calculated = true;
   		// this.calculatedA = a;
   	}
   	public boolean isCalculated(){
   		return this.calculated;
   	}

	public int getBlink(){
		return this.blink;
	}
	public void setBlink(int n){
		this.blink = n;
	}

	public void setArrayNum(int w, int k){
		this.numX = w;
		this.numY = k;
	}
	public int getArrayX(){
		return this.numX;
	}
	public int getArrayY(){
		return this.numY;
	}

	public boolean isRevealed(){
		return this.revealed;
	}
	public void setRevealed(boolean value){
		this.revealed = value;
	}

	public int getContent(){
		return this.content;
	}
	public int setContent(int c){
		this.content = c;
		return this.content;
	}

	public int getMarker(){
		return this.marker;
	}
	public int setMarker(int m){
		if( (m==1 && !this.isPolePressed()) || m!=1 )
			this.marker = m;
		return this.marker;
	}

	public void setNewGradient(Color a, Color b){
		this.gradient = new GradientPaint(this.pX, this.pY, a, this.pX+this.size, this.pY+this.size, b);
	}

	public int setNearBombs(int n){
		this.nearBombs = n;
		return this.nearBombs;
	}
	public int getNearBombs(){
		return this.nearBombs;
	}

	public int getXpos(){
		return this.pX;
	}
	public int getYpos(){
		return this.pY;
	}
	public int getSize(){
		return this.size;
	}

	public void setCoordinates(int x, int y){
		this.pX = x;
		this.pY = y;
	}

	public void setPolePressed(boolean flag){
			this.pressedFlag = flag;
	}
	public boolean isPolePressed(){
		return this.pressedFlag;
	}

	public void setPoleContent(int val){

		this.setContent(val);
	}


	public void setPoleColor(Graphics2D g2d){

		if( this.pressedFlag ){ //kolor wcisnietego - szary

			g2d.setColor(Color.GRAY);

		} else if( this.getBlink()!=0 ) { //kolor nie zainicjowanego - animacja

			switch( this.getBlink() ){
				case 1:
					setNewGradient(new Color(160,160,160), new Color(68,141,118));
					break;
				case 2:
					setNewGradient(new Color(170,170,170), new Color(90,160,130));
					break;
				case 3:
					setNewGradient(new Color(200,200,190), new Color(180,180,160));
					break;
				default:
					setNewGradient(new Color(247,247,232), new Color(200,200,200));
					break;
			}
			g2d.setPaint(this.gradient);
		} else {
			g2d.setColor(new Color(68,141,118));
		}

		if( this.marker==100 ){ //znaleziona bomba

			this.gradient = new GradientPaint(this.pX, this.pY, new Color(70,200,70), this.pX+this.size, this.pY+this.size, new Color(50,150,50));
			g2d.setPaint(this.gradient);
		}
		else if( this.marker==404 ){ //kliknieta bomba

			this.gradient = new GradientPaint(this.pX, this.pY, new Color(200,70,70), this.pX+this.size, this.pY+this.size, new Color(150,50,50));
			g2d.setPaint(this.gradient);
		}
	}

	public void drawPole(Graphics2D g2d){

		this.setPoleColor(g2d); //ustaw kolor
		g2d.fillRect(this.pX,this.pY,this.size,this.size); // wypelnij pole ustawionym kolorem

		// g2d.drawImage(this.imgDirt,this.pX,this.pY,this.size,this.size,null); //obrazek podloga

		if( this.isPolePressed() ){ //jesli pole klikniete, narysuj szara ramke
			g2d.setColor(Color.darkGray);
			g2d.drawRect(this.pX,this.pY,this.size,this.size);
		} else if( this.getBlink()!=0 ) { //jesli pole nie odklikniete i zainicjowane narysuj czarna ramke
			g2d.setColor(Color.lightGray);
			g2d.drawRect(this.pX,this.pY,this.size-1,this.size-1);
			g2d.setColor(Color.WHITE);
			g2d.drawRect(this.pX+1,this.pY+1,this.size,this.size);
			g2d.setColor(Color.BLACK);
			g2d.drawRect(this.pX,this.pY,this.size,this.size);
		}

		if( (this.getContent()==1 && this.isPolePressed()) || (this.isRevealed() && this.getContent()==1) ){ //narysuj mine
			g2d.setColor(new Color(0,0,0));
			g2d.drawImage(this.imgMine,this.pX+4,this.pY+4,this.size-8,this.size-8,null);
		} else if( this.getMarker()==1 ) { //narysuj flage
			g2d.drawImage(this.imgFlaga,this.pX+3,this.pY+3,this.size-6,this.size-6,null);
		}

		// if( this.calculatedA==1 ){
		// 	g2d.setColor(new Color(255,0,0,70));
		// 	g2d.fillRect(this.pX,this.pY,this.size,this.size);
		// } else if( this.calculatedA==2 ){
		// 	g2d.setColor(new Color(0,255,0,70));
		// 	g2d.fillRect(this.pX,this.pY,this.size,this.size);
		// }
	}
	public void drawNumber(Graphics2D g2d, String a){

		switch(a){ //wybierz kolor w zaleznosci od wartosci numerycznej
			case "1":
				g2d.setColor(new Color(162,255,0));
				break;
			case "2":
				g2d.setColor(new Color(255,222,0));
				break;
			case "3":
				g2d.setColor(new Color(255,94,0));
				break;
			case "4":
				g2d.setColor(new Color(255,0,0));
				break;
			case "5":
				g2d.setColor(new Color(0,34,255));
				break;
			default:
				g2d.setColor(new Color(0,0,0));
				break;
		}
		g2d.setFont(new java.awt.Font("Helvetica", java.awt.Font.BOLD, 12)); //ustaw czcionke
		g2d.drawString(a, this.pX+(this.size/2)-3, this.pY+(this.size/2)+5); //rysuj numer
	}

	public boolean equals(Object o)
    {
        boolean identyczny = false;

        if (o != null)
        {
        	if( this.pX==((Pole) o).pX && this.pY==((Pole) o).pY )
        		identyczny = true;
        }

        return identyczny;
    }

}