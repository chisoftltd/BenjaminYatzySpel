/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.benjamin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author Benjamin Chinwe
 */
class AliveScoreArea extends ScoreResult implements ActionListener {

    private final JButton resultButton;
    private boolean engaged;
    private int temporaryResult;    

    @SuppressWarnings("LeakingThisInConstructor")
    public AliveScoreArea(String name, int area, JPanel groundPanel ) {
        /*
         Ring ScoreResult konstruktören och ställ in knappen uppåt
         */
        super(groundPanel, name, area);
        //for (int i = 0; i < numberOfPlayers; i++) {
            resultButton = new JButton();
            resultButton.setEnabled(false);
            resultButton.addActionListener(this);
            resultComponent.add(resultButton);
        //}
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        throwButtonClicked();
    }

    public JButton getButton() {
        return resultButton;
    }

    private void throwButtonClicked() {
        /*
         Spara temp värde bestående värde. Inaktivera knappen för resten av matchen
         */
        result = temporaryResult;
        resultLabel.setText("" + result);
        activeButton(false);
        engaged = true;
    }

    protected void activeButton(boolean enable) {
        /*
         Avsluta metod om denna värdering komponent har redan använts och 
         om handikappande knapp, ta bort sin text först
         */
        if (engaged) {
            return;
        }
        if (!enable) {
            resultButton.setText("");
        }
        resultButton.setEnabled(enable);
    }

    void resetTempValue() {
        temporaryResult = 0;
        updateValueButton();
    }

    void addTempValue(int result) {
        temporaryResult += result;
        updateValueButton();
    }

    int getTempValue() {
        return temporaryResult;
    }

    private void updateValueButton() {
        // Avsluta metod om poäng komponent har redan använts
        if (engaged) {
            return;
        }
        resultButton.setText("" + temporaryResult);

    }

    void scoreTempValue(int indexValue, int[] dice, int size) {
        /*
         Ställ temp Värde är lika med poäng Värde så länge punkt 
         Värde inte är noll. Annars är poängvärdet med summan av tärningarna. 
         Ställ in temp Värde. Summera tärningarna värden
         */
        //System.out.println("PointValue " + indexValue + " Dice " + Arrays.toString(dice));
        if (indexValue != 0) {
            temporaryResult = indexValue;
        } else {
            temporaryResult = 0;
            for (int index = 0; index < size; index++) {
                
                temporaryResult += dice[size-1];
                //System.out.println("tempValue " + temporaryResult);
            }
        }
        updateValueButton();
    }
}
