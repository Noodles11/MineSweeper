package saper;

import javax.swing.JFrame;

import javax.swing.JLabel;

import javax.swing.JCheckBox;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;

import java.awt.GridLayout;

import java.awt.*;
import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.border.EmptyBorder;


public class OkienkoMenu extends JFrame{

	JButton btn_start;
	JCheckBox check_autoShow;
	JLabel labelPoleIleBomb;
	// JTextField poleIleBomb;
	JLabel labelPoleWymiary;
	// JTextField poleWymiary;
    JSlider suwakWielkosc, suwakIleMin;

    Kontroler kontroler = new Kontroler();

    Color mainColor = new Color(247,237,212);
    Color secondColor = new Color(18,91,68);
    Color firstColor = new Color(68,141,118);

	public OkienkoMenu(){
		super("MENU");

        getContentPane().setBackground(firstColor);
        // setSize(350, 450);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(7,1,0,0));

        this.addComponents();

        setLocationRelativeTo(null);
        setVisible(true);
	}

	public void addComponents(){

        JLabel title = new JLabel("Jackowy Saper 2017");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setVisible(true);
        title.setForeground(mainColor);
        title.setBorder(new EmptyBorder(15, 20, 0, 20));
        title.setFont(new java.awt.Font("Arial", Font.BOLD, 22));

		check_autoShow = new JCheckBox("Autoodkrywanie",true);
		check_autoShow.setBackground(firstColor);
		// check_autoShow.setPreferredSize(new Dimension(this.getWidth(),30));
        check_autoShow.setVisible(false);

        btn_start = new JButton("Nowa gra");
        btn_start.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
          	if( sendValuesToKontroler() ){
                makeNewGame();
                setMenuFieldsEditable(false);
            } else {

            }
          }
        });
        btn_start.setVisible(true);

        labelPoleWymiary = new JLabel("Wielkosc planszy: 15");
        labelPoleWymiary.setHorizontalAlignment(SwingConstants.CENTER);
        labelPoleWymiary.setVisible(true);
        labelPoleWymiary.setForeground(mainColor);
        labelPoleWymiary.setBorder(new EmptyBorder(20, 20, 0, 20));
        
        // poleWymiary = new JTextField();
        // poleWymiary.setText("15");
        // poleWymiary.setHorizontalAlignment(JTextField.CENTER);
        // poleWymiary.setVisible(true);
        // poleWymiary.setEditable(false);

        suwakWielkosc = new JSlider(10,30,15);
        suwakWielkosc.setMinorTickSpacing(1);
        suwakWielkosc.setMajorTickSpacing(5);
        suwakWielkosc.setPaintTicks(true);
        suwakWielkosc.setSnapToTicks(true);
        suwakWielkosc.setPaintLabels(true);
        suwakWielkosc.setBackground(firstColor);
        suwakWielkosc.setForeground(mainColor);
        suwakWielkosc.setBorder(new EmptyBorder(0, 10, 0, 10));
        // suwakWielkosc.setPaintTrack(false);
        suwakWielkosc.addChangeListener(new ChangeListener()
        {
            public void stateChanged(ChangeEvent e)
            {
                updateLabelWielkoscPlanszy(suwakWielkosc.getValue());
            }
        });
        
        labelPoleIleBomb = new JLabel("Liczba min: 10");
        labelPoleIleBomb.setHorizontalAlignment(SwingConstants.CENTER);
        labelPoleIleBomb.setVisible(true);
        labelPoleIleBomb.setForeground(mainColor);
        labelPoleIleBomb.setBorder(new EmptyBorder(20, 20, 0, 20));

        // poleIleBomb = new JTextField();
        // poleIleBomb.setText("10");
        // poleIleBomb.setHorizontalAlignment(JTextField.CENTER);
        // poleIleBomb.setVisible(true);
        // poleIleBomb.setEditable(false);
    
        suwakIleMin = new JSlider(0,400,10);
        suwakIleMin.setMinorTickSpacing(25);
        suwakIleMin.setMajorTickSpacing(100);
        suwakIleMin.setPaintTicks(true);
        suwakIleMin.setSnapToTicks(false);
        suwakIleMin.setPaintLabels(true);
        suwakIleMin.setBackground(firstColor);
        suwakIleMin.setForeground(mainColor);
        suwakIleMin.setBorder(new EmptyBorder(0, 10, 0, 10));
        // suwakIleMin.setPaintTrack(false);
        suwakIleMin.addChangeListener(new ChangeListener()
        {
            public void stateChanged(ChangeEvent e)
            {
                updateLabelIleBomb(suwakIleMin.getValue());
            }
        });


        add(title);
        add(labelPoleWymiary);
        // add(poleWymiary);
        add(suwakWielkosc);
        add(labelPoleIleBomb);
        // add(poleIleBomb);
        add(suwakIleMin);
        add(check_autoShow);
        add(btn_start);

        pack();
	}

    public void updateLabelWielkoscPlanszy(int w){
        labelPoleWymiary.setText("Wielkosc planszy: "+w);
    }
    public void updateLabelIleBomb(int n){
        labelPoleIleBomb.setText("Ilosc min: "+n);
    }

    public boolean sendValuesToKontroler(){
        int wielkosc, liczbaMin;

        try {
            wielkosc = suwakWielkosc.getValue();
            if( wielkosc<10 || wielkosc>30 )
                throw new MojeBledy("Bledna wielkosc planszy!");
        } catch(MojeBledy e) {
            System.out.println(e.getMessage());
            wielkosc = 15;
            suwakWielkosc.setValue(wielkosc);

            return false;
        }

        try {
            liczbaMin = suwakIleMin.getValue();
            if( liczbaMin<10 || liczbaMin>400 )
                throw new MojeBledy("Bledna ilosc min!");
        } catch(MojeBledy e) {
            System.out.println(e.getMessage());
            liczbaMin = 10;
            suwakIleMin.setValue(liczbaMin);

            return false;
        }

        this.kontroler.setWymiary(wielkosc,wielkosc,25);
        this.kontroler.setLiczbaBomb(liczbaMin);
        this.kontroler.setAutoOdkr(this.check_autoShow.isSelected());

        return true;
    }

    public void setMenuFieldsEditable(boolean flag){
        this.labelPoleWymiary.setEnabled(flag);
        this.suwakWielkosc.setEnabled(flag);
        // this.poleWymiary.setEnabled(flag);
        this.labelPoleIleBomb.setEnabled(flag);
        this.suwakIleMin.setEnabled(flag);
        // this.poleIleBomb.setEnabled(flag);
        this.check_autoShow.setEnabled(flag);
        this.btn_start.setEnabled(flag);
    }

    public void makeNewGame(){
        int[] wymiary = this.kontroler.getWymiary();
        int x = wymiary[0];
        int y = wymiary[1];
        int wlk = wymiary[2];

        PanelGry pg = new PanelGry(x,y,wlk,this.kontroler.getLiczbaBomb(),this.kontroler.getAutoOdkr(),this.kontroler.getMaxTime());
        pg.addWindowListener( new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                setMenuFieldsEditable(true);
            }
        });
    }
}