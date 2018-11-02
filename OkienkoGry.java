package saper;

import javax.swing.JFrame;

import javax.swing.JLabel;
import javax.swing.ImageIcon;

import javax.swing.JCheckBox;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;

import javax.swing.JButton;

import java.awt.FlowLayout;

import java.awt.*;

import javax.swing.Timer;




public class OkienkoGry extends JFrame{

        private int wiersze;
        private int kolumny;
        private int wlk;
        private int iloscBomb;
        private boolean autoOdkr;
        private int maxTime;
        private int czasGry;

        private int threeBV;

        PanelGry panel;

        Timer timer;
        
        JLabel napisCzas;
        JLabel ikonaCzas;
        JLabel ikonaMina;
        JLabel napisPoleIleBomb;
        JLabel ikonaScore;
        JLabel napisIleMin;
        JLabel napisWynik;

        JLabel ikonaHelp;
        public JButton btn_AI;

    public OkienkoGry(int wiersze, int kolumny, int wlk, int iloscBomb, boolean autoOdkr, int maxTime) {
        super("SAPER");

        this.wiersze = wiersze;
        this.kolumny = kolumny;
        this.wlk = wlk;
        this.iloscBomb = iloscBomb;
        this.autoOdkr =autoOdkr;
        this.maxTime = maxTime;
        this.czasGry = 0;

        this.threeBV = 0;
        
        getContentPane().setBackground(new Color(68,141,118));
        setResizable(false);
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.addComponents();
        this.prepareGame();

        // setVisible(true);
    }

    public void prepareGame(){
        setPreferredSize(new Dimension(this.wiersze*this.wlk+18, this.kolumny*this.wlk+75));        
    }
    public void startGame(){
        timerStop();
        zerujZegar();
        timerStart();
    }

    public void set3BV(int i){
        System.out.println("3BV="+i);
        this.threeBV = i;
        this.odswiezWynik(i);
    }

    public void timerStart(){
        this.timer.start();
    }
    public void timerStop(){
        this.timer.stop();
    }
    public void odswiezZegar(){ //liczy od 0 do 999 i zamyka zegar
        if( this.czasGry<999 ){
            this.czasGry+=1;
            this.napisCzas.setText(""+(this.czasGry<10?("00"+this.czasGry):(this.czasGry<100?("0"+this.czasGry):this.czasGry)));
        }
    }
    public void zerujZegar(){
        this.czasGry = 0;
    }

    public void odswiezIleMin(int n){
        this.napisIleMin.setText(""+n);
    }
    public void odswiezWynik(int n){
        this.napisWynik.setText(""+n);
    }

    public void addComponents(){
        this.timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                    odswiezZegar();
            }    
        });

        java.awt.Font myfont = new java.awt.Font("Helvetica", Font.BOLD, 16);

        ImageIcon czasIcon = new ImageIcon("hourglass.png");
        Image czasObr = czasIcon.getImage();
        Image czasObr2 = czasObr.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
        czasIcon = new ImageIcon(czasObr2);
        ikonaCzas = new JLabel(czasIcon);
        ikonaCzas.setPreferredSize(new Dimension(20,20));
        ikonaCzas.setVisible(true);

        this.napisCzas = new JLabel("000");
        this.napisCzas.setFont(myfont);
        this.napisCzas.setForeground(Color.BLACK);
        this.napisCzas.setVisible(true);

        ImageIcon minaIcon = new ImageIcon("bomb_menu.png");
        Image minaObr = minaIcon.getImage();
        Image minaObr2 = minaObr.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
        minaIcon = new ImageIcon(minaObr2);
        ikonaMina = new JLabel(minaIcon);
        ikonaMina.setPreferredSize(new Dimension(20,20));
        ikonaMina.setVisible(true);
        
        this.napisIleMin = new JLabel(""+this.iloscBomb);
        this.napisIleMin.setFont(myfont);
        this.napisIleMin.setVisible(true);

        ImageIcon scoreIcon = new ImageIcon("trophy.png");
        Image scoreObr = scoreIcon.getImage();
        Image scoreObr2 = scoreObr.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
        scoreIcon = new ImageIcon(scoreObr2);
        ikonaScore = new JLabel(scoreIcon);
        ikonaScore.setPreferredSize(new Dimension(20,20));
        ikonaScore.setVisible(true);

        this.napisWynik = new JLabel("0");
        this.napisWynik.setFont(myfont);
        this.napisWynik.setVisible(true);

        this.btn_AI = new JButton("Hint");


        ImageIcon helpIcon = new ImageIcon("help.png");
        Image helpObr = helpIcon.getImage();
        Image helpObr2 = helpObr.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
        helpIcon = new ImageIcon(helpObr2);
        this.btn_AI.setIcon(helpIcon);
        // this.btn_AI.setBorderPainted(false);
        this.btn_AI.setContentAreaFilled(false);


        add(ikonaCzas);
        add(this.napisCzas);
        add(ikonaMina);
        add(this.napisIleMin);
        add(ikonaScore);
        add(this.napisWynik);
        add(this.btn_AI);

        // pack();
    }
}