/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.benjamin;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Benjamin Chinwe
 */
class PassiveScoreArea extends ScoreResult {

    private final JLabel padding;

    public PassiveScoreArea(String name, int area, JPanel groundPanel) {
        /*
         Ring Score konstruktören. Ställ sedan in utfyllnadspanelen
         */
        super(groundPanel, name, area);
        //for (int i = 0; i < numberOfPlayers; i++) {
            padding = new JLabel();
            resultComponent.add(padding);
        //}
    }

}
