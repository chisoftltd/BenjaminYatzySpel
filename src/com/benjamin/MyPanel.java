/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.benjamin;

import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author Benjamin Chinwe
 */
class MyPanel extends JPanel {

    @Override
       protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(YatzySpel.img, 0, 0, getWidth(), getHeight(), this);
        }
    
}
