/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.benjamin;

import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author Benjamin Chinwe
 */
class ScoreResult {
    
    protected final JPanel resultComponent;
    private final JLabel name;
    protected int result;
    protected final JLabel resultLabel;
    private final int area;
    private final int playerNumber = 0;
    private final int gridSize = 3;
    
    public ScoreResult(JPanel groundPanel, String resultName, int area) {
        /*
        Ställ in denna poäng komponenter JPanel. Sedan initiera alla data 
        medlemmar. Tilldela komponenter till platser i behållare och 
        spara värde som anger vilken del denna värdering är i
         */
        resultComponent = new JPanel();
        resultComponent.setLayout(new GridLayout(1, gridSize));
        name = new JLabel(resultName);
        resultLabel = new JLabel("" + result, SwingConstants.LEADING);
        resultComponent.add(name);
        resultComponent.add(resultLabel);
        groundPanel.add(resultComponent);
        this.area = area;
    }
    
    void addToResult(int index) {
        result += index;
        resultLabel.setText("" + result);
    }
    public int getArea() {
        return area;
    }
    public int getResult(){
        return result;
    }
}
