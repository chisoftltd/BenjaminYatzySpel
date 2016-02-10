/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.benjamin;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Benjamin Chinwe
 */
public class Dice implements ActionListener {

    private final JPanel diceComponent;
    private boolean pause;
    private int result;
    private final JLabel image;
    private final JButton pauseButton;

    // Standard tärnings bilder
    private static final ImageIcon[] DICE_IMAGE
            = {
                loadImage("image\\d1.GIF"),
                loadImage("image\\d2.GIF"),
                loadImage("image\\d3.GIF"),
                loadImage("image\\d4.GIF"),
                loadImage("image\\d5.GIF"),
                loadImage("image\\d6.GIF")
            };

    // Paus tärnings bilder
    private static final ImageIcon[] DICE_IMAGE_PAUSE
            = {
                loadImage("image\\d1Held.GIF"),
                loadImage("image\\d2Held.GIF"),
                loadImage("image\\d3Held.GIF"),
                loadImage("image\\d4Held.GIF"),
                loadImage("image\\d5Held.GIF"),
                loadImage("image\\d6Held.GIF")
            };

    Dice(JPanel dicePanel) {
        /*
         Ställ upp denna tärning komponenter JPanel. Sedan Initiera data 
         medlemmar. Ställ in tärningarna bilden. Ställ in hold knappen. 
         Inaktivera håll knapparna när spelet startar. Lägg hela tärningar 
         komponenten till baspanelen.
         */
        diceComponent = new JPanel();
        diceComponent.setLayout(new BoxLayout(diceComponent, BoxLayout.Y_AXIS));

        pause = false;
        result = 1;

        image = new JLabel(DICE_IMAGE[result - 1]);
        image.setAlignmentX(Component.LEFT_ALIGNMENT);
        diceComponent.add(image);

        pauseButton = new JButton("Pause");
        pauseButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        pauseButton.addActionListener(this);
        diceComponent.add(pauseButton);

        pauseButton.setEnabled(false);
        dicePanel.add(diceComponent);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        /*
         Hold knappen klickade eller en poäng knapp klickade
         */
        if (event.getSource() == pauseButton) {
            pause();
        } else {
            pauseButton.setEnabled(false);
        }

    }

    private static ImageIcon loadImage(String bild) {
        /*
         Konvertera sökvägen till en URL och återställa bilden
         */
        URL resource = Dice.class.getResource(bild);
        return new ImageIcon(resource);
    }

    private void pause() {
        pause = !pause;
        updateImage();
    }

    private void updateImage() {
        /*
        Om tärningarna hålls eller om tärningarna inte hålls
        */
        if (pause) {
            image.setIcon(DICE_IMAGE_PAUSE[result - 1]);
        } 
        else {
            image.setIcon(DICE_IMAGE[result - 1]);
        }
    }

    void roll(int numberOfThrows, int i) {
        /*
        Om detta är det första kastet, återställningstärningarna så ingen hålls. 
        Bara rulla tärningarna om de inte hålls. Generera ett slumpvärde för 
        munstycket. Inaktivera håll knappen om på sista rulle runda, annars aktivera.
        */
        if (numberOfThrows == 1) {
            pause = false;
        }
        if (!pause) {
            result = (int) (Math.floor(Math.random() * (6)) + 1);
            updateImage();
        }
        boolean enable = (numberOfThrows < 3);
            pauseButton.setEnabled(enable);
        
    }

    int getResult() {
        return result;
    }
}
