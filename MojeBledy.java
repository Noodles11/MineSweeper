package saper;

import javax.swing.JOptionPane;

public class MojeBledy extends Exception{
    
    public MojeBledy() {}

    public MojeBledy(String message)
    {
    	super(message);
    	pokazOkienko(message);
    }

    public void pokazOkienko(String m){
     	JOptionPane.showMessageDialog(null, m, "Error", JOptionPane.ERROR_MESSAGE);
    }
}