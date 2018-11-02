package saper;

import java.awt.*;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.lang.Math.*;

import javax.swing.Timer;


public class PanelGry extends OkienkoGry{

    private int wiersze, kolumny, wlk, ileBomb, maxTime;
    private boolean autoOdkr;

    private Timer timerBlink;
    private int blinkState;

    private Plansza plansza;

    public PanelGry(int w, int k, int wlk, int ileBomb, boolean autoOdkr, int maxTime) {

        super(w,k,wlk,ileBomb,autoOdkr,maxTime);

        this.wiersze = w;
        this.kolumny = k;
        this.wlk = wlk;
        this.autoOdkr = autoOdkr;
        this.ileBomb = ileBomb;
        this.maxTime = maxTime;

        this.blinkState = 0;

        this.initPlansza(w,k,wlk,ileBomb,autoOdkr);
        this.prepareBlink(25);

        super.add(this.plansza);
        

        super.pack();
        super.setLocationRelativeTo(null);
        super.setVisible(true);

        this.blinkStart();
    }

    public void blinkStart(){
        this.timerBlink.start();
    }
    public void blinkStop(){
        this.timerBlink.stop();
    }
    public boolean isBlinkFinished(){
        if( this.blinkState>0 )
            return false;
        else
            return true;
    }

    public void initPlansza(int w, int k, int wlk, int ileBomb, boolean autoOdkr){
        this.plansza = new Plansza(w,k,wlk,ileBomb,autoOdkr);
        this.plansza.setPanel(this);
        this.plansza.setPreferredSize(new Dimension(this.wiersze*this.wlk+1,this.kolumny*this.wlk+1));
        this.plansza.setBackground(new Color(68,141,118));

        this.btn_AI.addActionListener(new ActionListener(){
          public void actionPerformed(ActionEvent e)
          {    
                int[] check = plansza.policzIleZamarkowanych();

                while( (check[0]==plansza.policzIleZamarkowanych()[0] && check[1]==plansza.policzIleZamarkowanych()[1]) || (plansza.policzIleZamarkowanych()[0]==plansza.policzIleZamarkowanych()[1]) ) {
                    check = plansza.policzIleZamarkowanych();
                    plansza.getAI().wykonajKrokAI();

                    if( !plansza.isCurrentlyPlaying() )
                        btn_AI.setEnabled(false);
                }             
          }
        });
        this.btn_AI.setVisible(true);
    }

    public void prepareBlink(int t){
        this.timerBlink = new Timer(t, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Pole[][] tabelaPol = plansza.getTabelaPol();

            //color wave
                for( int w=0; w<wiersze; w++){
                    for( int k=0; k<kolumny; k++){

                        if( (w+k)<=blinkState ){ // po skosie
                        // if( k<=blinkState ){ //z gory na dol
                        // if( w<=blinkState ){ //z lewej do prawej
                        
                            if( tabelaPol[w][k].getBlink()<3 ){
                                int a = tabelaPol[w][k].getBlink() + 1;
                                tabelaPol[w][k].setBlink(a);
                            }
                            else {
                                tabelaPol[w][k].setBlink(100);
                            }
                        
                        }

                    }
                }

                if( tabelaPol[wiersze-1][kolumny-1].getBlink()<100 ){
                    blinkState++;
                }
                else{
                    // for( int w=0; w<wiersze; w++){
                    //     for( int k=0; k<kolumny; k++){
                            
                    //             tabelaPol[w][k].setBlink(100);
                            
                    //     }
                    // }
                    blinkState = 0;
                    blinkStop();
                }


                plansza.repaint();
            }
        });
    }
}