/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.benjamin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 *
 * @author BeCh1130
 */
public class YatzySpel extends JFrame implements ActionListener {

    ArrayList<Integer> onesDice = new ArrayList<>();
    ArrayList<Integer> twosDice = new ArrayList<>();
    ArrayList<Integer> threesDice = new ArrayList<>();
    ArrayList<Integer> foursDice = new ArrayList<>();
    ArrayList<Integer> fivesDice = new ArrayList<>();

    ArrayList<ArrayList<Integer>> diceSize = new ArrayList<>();

    private final Container displayContainer;
    private JPanel resultPanel;
    private JPanel abovePanel;
    private JPanel belowPanel;
    protected static BufferedImage img;

    private static final ArrayList<String> playerName = new ArrayList<>();

    private static final String[] ABOVE_NAMES = {"Ones:", "Twos:",
        "Threes:", "Fours:",
        "Fives:", "Sixes:",
        "Upper Score:", "Bonus:",
        "Upper Total:", " "};

    private static final String[] BELOW_NAMES = {"One Pair", "Two Pairs",
        "3 of a kind", "4 of a kind",
        "Full House", "S. Straight",
        "L. Straight", "Maxi Yatzy",
        "Chance", "Lower Total",
        "Grand Total:"};

    private static final int ABOVE_AREA = 0;
    private static final int BELOW_AREA = 1;
    private ScoreResult[] above;
    private ScoreResult[] below;
    private final DiceDriver diceDriver;
    private final int CHANCE = 8;
    private static final int[] SCORE_CALC_ORDER = {7, 0, 1, 3, 2, 4, 5, 6, 8};
    private static final int[] LOWER_SCORES = {0, 0, 0, 0, 0, 15, 20, 50, 0};
    //private static int numberOfPlayers;

    /**
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public YatzySpel() {
        /*
         Ställ upp spelet fönster genom att inrätta behållaren för att sätta 
         komponenterna på. Därefter size ramen och sedan vertikalt och 
         horisontellt centrera spelfönstret på skärmen. Slutligen, visa fönstret.
         */

        setFont(new Font("Font.COURIER", Font.BOLD, 19));
        setTitle("Yatzy Spel Project");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        displayContainer = getContentPane();
        displayContainer.setLayout(new BorderLayout(10, 10));

        resultDisplay(displayContainer); // Create Score Sheet

        diceDriver = new DiceDriver(displayContainer);
        pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width / 2 - getSize().width / 2, dim.height
                / 2 - getSize().height / 2);
        setVisible(true);
    }

    public static void main(String[] args) {
        /* numberOfPlayers = Integer.parseInt(JOptionPane.showInputDialog
        (null, "How many layers in this game?"));
        for (int i = 0; i < numberOfPlayers; i++) {
            playerName.add(i, JOptionPane.showInputDialog(null, "What is your name?"));
        }*/
        YatzySpel yatzySpel = new YatzySpel();
    }

    private void resultDisplay(Container displayContainer) {
        /*
         Ställ in ytter poäng panelen, sedan övre och below panelerna och 
         placera både i yttre panel. Också stall i POÄNG arrayer
         */
        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setBorder(BorderFactory.createTitledBorder("Result/Score Area :"));

        abovePanel = new JPanel();
        abovePanel.setLayout(new GridLayout(10, 1));
        abovePanel.setBorder(BorderFactory.createLineBorder(Color.BLUE));

        belowPanel = new JPanel();
        belowPanel.setLayout(new GridLayout(13, 1));
        belowPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE));

        above = new ScoreResult[10];
        groundResult(abovePanel, above, ABOVE_NAMES, 6, ABOVE_AREA);

        below = new ScoreResult[11];
        groundResult(belowPanel, below, BELOW_NAMES, 9, BELOW_AREA);

        resultPanel.add(abovePanel);
        resultPanel.add(belowPanel);
        displayContainer.add(resultPanel, BorderLayout.LINE_START);
    }

    /**
     *
     *
     * @param groundPanel
     * @param result
     * @param namn
     * @param size
     * @param area
     */
    private void groundResult(JPanel groundPanel, ScoreResult[] result,
            String[] namn, int size, int area) {
        for (int bound = 0; bound < result.length; bound++) {
            /*
             Om index ligger below gränsvärdet, initiera som ett levande area 
             och lägg poäng controller som en åtgärd lyssnare för varje värde knapp
             */
            if (" ".equals(namn[bound])) {
                return;
            }
            if (bound < size) {
                result[bound] = new AliveScoreArea(namn[bound], area, groundPanel);
                ((AliveScoreArea) result[bound]).getButton().addActionListener(this);
            } else {
                result[bound] = new PassiveScoreArea(namn[bound], area, groundPanel);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        // Få åtgärds lyssnare av poängen knappen bara klickade
        // Få Active Score objekt som klickade poäng knappen tillhör
        // Antal nya punkter som ska läggas till summor
        ActionListener[] listeners = ((JButton) event.getSource())
                .getActionListeners();

        AliveScoreArea clickedScore = ((AliveScoreArea) listeners[listeners.length - 1]);

        int newIndex = clickedScore.getTempValue();

        // Score knappen klickas var i den övre sektionen
        // Uppdatering övre totalt
        // Kontrollera övre bonus
        if (clickedScore.getArea() == 0) {

            above[6].addToResult(newIndex);
            above[8].addToResult(newIndex);

            if (above[7].getResult() == 0
                    && above[6].getResult() >= 63) {
                above[7].addToResult(50);
                above[8].addToResult(50);
                below[9].addToResult(50);
                below[10].addToResult(50);
            }
        } // Resultat knappen klickas var i den nedre sektionen
        else {
            // Uppdatering under totalt
            below[9].addToResult(newIndex);
        }

        // Uppdatera totalsumman
        below[10].addToResult(newIndex);

        // Inaktivera alla poäng knapparna
        enableScoreButtons(false);
    }

    public void calcScores(int[] dice) {
        /*
         Återställ alla temporära värdena för de aktiva poäng, sedan göra det 
         möjligt för alla poäng knappar för de aktiva poängen. Följt genom att 
         beräkna övre och undre aktiva poäng.
         */
        resetTempActiveScores();
        enableScoreButtons(true);
        resultAbove(dice);
        resultBelow(dice);
    }

    private void resetTempActiveScores() {
        //Återställ temp värden för alla övre och undre aktiva poäng
        for (int index = 0; index < 6; index++) {
            ((AliveScoreArea) above[index]).resetTempValue();
        }
        for (int index = 0; index < 9; index++) {
            ((AliveScoreArea) below[index]).resetTempValue();
        }
    }

    private void enableScoreButtons(boolean enable) {
        // Aktivera / inaktivera övre och undre aktiva poäng knappar
        for (int index = 0; index < 6; index++) {
            ((AliveScoreArea) above[index]).activeButton(enable);
        }

        for (int index = 0; index < 9; index++) {
            ((AliveScoreArea) below[index]).activeButton(enable);
        }
    }

    private void resultAbove(int[] dice) {
        /*
         Iterera igenom tärnings arrayen och stämmer upp de övre poäng 
         komponenter och lägga dicena värdet till motsvarande poäng komponenten
         */
        for (int index = 0; index < dice.length; index++) {
            ((AliveScoreArea) above[dice[index] - 1]).addTempValue(dice[index]);
        }
    }

    private void resultBelow(int[] dice) {
        // alltid poäng chans
        scoreComponent(CHANCE, dice, 5);

        // Calculate a pair
        for (int index = 0; index < dice.length; index++) {

            switch (dice[index]) {
                case 1:
                    onesDice.add(1);
                    break;
                case 2:
                    twosDice.add(1);
                    break;
                case 3:
                    threesDice.add(1);
                    break;
                case 4:
                    foursDice.add(1);
                    break;
                case 5:
                    fivesDice.add(1);
                    break;
                    
                default:
                    break;
            }
        }
        if (onesDice.size() == 2) {
            scoreComponent(0, dice, onesDice.size());
        }
        if (twosDice.size() == 2) {
            scoreComponent(0, dice, twosDice.size());
        }
        if (threesDice.size() == 2) {
            scoreComponent(0, dice, threesDice.size());
        }
        if (foursDice.size() == 2) {
            scoreComponent(0, dice, foursDice.size());
        }
        if (fivesDice.size() == 2) {
            scoreComponent(0, dice, fivesDice.size());
        }

        //Create a multidimensional array, calculating two pairs
        // ArrayList<ArrayList<Integer>> diceSize = new ArrayList<>();
        diceSize.add(onesDice);
        diceSize.add(twosDice);
        diceSize.add(threesDice);
        diceSize.add(foursDice);
        diceSize.add(fivesDice);
        int twoSize = 0;
        Iterator<ArrayList<Integer>> itDice = diceSize.iterator();
        for (int i = 0; i < diceSize.size(); i++) {
            for (int j = 0; j < diceSize.size(); j++) {
                if (diceSize.get(i) != diceSize.get(j)) {
                    while (itDice.hasNext()) {
                        ArrayList<Integer> diceList = itDice.next();
                        if (diceList.size() == 2) {
                            twoSize++;
                        }
                    }
                }
            }
        }

        //calculating two pairs
        if ((twoSize % 2) == 0) {
            
            scoreComponent(1, dice, 4);
        }
        int threeSize = 0;
        while (itDice.hasNext()) {
            ArrayList<Integer> diceList = itDice.next();
            if (diceList.size() == 3) {
                threeSize++;
            }
        }
        // att beräkna tre par
        if ((threeSize % 3) == 0) {
            scoreComponent(2, dice, 3);
        }

        int fourSize = 0;
        while (itDice.hasNext()) {
            ArrayList<Integer> diceList = itDice.next();
            if (diceList.size() == 4) {
                fourSize++;
            }
        }
        // att beräkna tre par
        if ((fourSize % 4) == 0) {
            scoreComponent(3, dice, 4);
        }

        if (onesDice.size() == 1 && twosDice.size() == 1 && threesDice.size() == 1
                && foursDice.size() == 1 && fivesDice.size() == 1) {
            scoreComponent(5, dice, 5);
        }
        
        int largeStraight = 1;
        for (int i = 0; i < dice.length - 1; i++) {
            if (dice[i + 1] - dice[i] == 1) {
                largeStraight++;
            }
        }
        
        if (largeStraight == 4) {
            scoreComponent(6, dice, 5);
        }
        
        /*
         Number of matching dice, and the value of matching dice. 
        Use the upper points to count the greatest number of matching dice. 
        Calculate the number of dice with the value "index + 1". 
        Record the largest number of identical dice
         */
        int sameDice = 0;
        int sameDiceVal = 0;
        for (int index = 0; index < 6; index++) {
            int numDice = ((AliveScoreArea) above[index]).getTempValue()
                    / (index + 1);
            if (numDice > sameDice) {
                sameDice = numDice;
                sameDiceVal = index + 1;
            }
        }

        /*
         Beräkna lägre poäng baserat på antalet matchande dice. 
         Sedan gör det första halvan av lägre poäng. Kontrollera också om 
         kriterierna för denna värdering komponent är uppfyllt, och beräkna 
         poängen för denna komponent
         */
        int orderIndex = 0;
        int sameDiceNeeded = 5;
        while (orderIndex < SCORE_CALC_ORDER.length / 2) {
            orderIndex++;
            sameDiceNeeded--;
        }

        /*
         Kontrollera om en kåk om tre av dicena matchen. testvillkor 1: 
         de två första dicena stämmer och är inte sameDiceVal. testvillkor 2: 
         de senaste två dice matcha och är inte sameDiceVal. 
         Om ett av villkoren är uppfyllt, det finns ett fullständigt hus
         */
        if (sameDice == ++sameDiceNeeded) {
            boolean cond1 = (dice[0] == dice[1])
                    && (dice[0] != sameDiceVal);
            boolean cond2 = (dice[dice.length - 1] == dice[dice.length - 2])
                    && (dice[dice.length - 1] != sameDiceVal);
            if (cond1 || cond2) {
                scoreComponent(4, dice, 5);
            }
        }
        orderIndex++;
        sameDiceNeeded--;

        /*
         Kontrollera för små och stora stegar. Räkna antalet löpnummer. 
         Iterera igenom tärningsvärdet array för att räkna # sekventiella # 's. 
         Om angränsande nummer är sekventiell
         */
        if (sameDice <= sameDiceNeeded) {
            int sequentialNums = 1;
            for (int index = 0; index < dice.length - 1; index++) {
                if (dice[index + 1] - dice[index] == 1) {
                    sequentialNums++;
                }

            }

            /*
             Kontrollera om det finns en liten rak och första kontrollera 
             om det finns tillräckligt många punkter. nummer för en liten rak
             Alla små stegar måste ha 3 och 4. De resterade siffrorna kan 
             vara 1 och 2. Eller resterade siffrorna kan vara 2 och 5. 
             Eller resterade siffrorna kan vara 5 och 6
             */
            if ((sequentialNums >= 4)
                    && ((Arrays.binarySearch(dice, 3) >= 0)
                    && (Arrays.binarySearch(dice, 4) >= 0))
                    && (((Arrays.binarySearch(dice, 1) >= 0)
                    && (Arrays.binarySearch(dice, 2) >= 0))
                    || ((Arrays.binarySearch(dice, 2) >= 0)
                    && (Arrays.binarySearch(dice, 5) >= 0))
                    || ((Arrays.binarySearch(dice, 5) >= 0)))) {
            }

            orderIndex++;
        }

    }

    private void scoreComponent(int playIndex, int[] dice, int size) {
        /*
        Bestäm index i lägre poäng array från playIndex. 
        Ställ sedan in det tillfälliga resultatet av poängen komponenten vid index
         */
        int index = SCORE_CALC_ORDER[playIndex];

        ((AliveScoreArea) below[playIndex]).scoreTempValue(LOWER_SCORES[playIndex],
                dice, size);
    }

    /*
    Inner Class to create dice image and panel, throw button and manager dice throw
     */
    protected class DiceDriver implements ActionListener {

        private final JPanel playPanel;
        private final JPanel dicePanel;
        private final JButton throwButton;
        private final JPanel throwPanel;
        private final Dice[] dice;
        private int round = 0;
        private int numberOfThrows = 0;

        @SuppressWarnings("LeakingThisInConstructor")
        public DiceDriver(Container displayContainer) {
            /*
             Ställ in spelpanelen. Sedan ställa in begränsningar för layout och 
             ställa in dicena panelen. Också ställa in layout begränsningar 
             som är specifika för tärningspanelen och gör rullknappen, 
             med valspanelen. Gör dicena och ställa upp dicena och 
             dice controller som handlings lyssnare för värdering knappar
             */
            try {
                img = ImageIO.read(new File("C:\\Users\\Chisoft\\Documents\\"
                        + "NetBeansProjects\\YatzySpel_VersionII\\src\\com\\"
                        + "benjamin\\image\\skyImage.jpg"));
            } catch (IOException e) {
            }
            playPanel = new MyPanel();
            playPanel.setLayout(new GridBagLayout());
            playPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createMatteBorder(2, 2,
                            2, 2, Color.blue), "Dice Board", TitledBorder.RIGHT,
                    TitledBorder.TOP, new Font("Font.COURIER", Font.BOLD, 19),
                    Color.BLUE));
            displayContainer.add(playPanel, BorderLayout.CENTER);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridwidth = 1;
            gbc.gridheight = 2;
            gbc.weighty = 0.5;

            dicePanel = new JPanel();
            dicePanel.setLayout(new GridLayout(1, 5, 5, 0));

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(0, 150, 0, 150);
            playPanel.add(dicePanel, gbc);

            throwButton = new JButton("Throw");
            throwButton.setBackground(new Color(255, 165, 0));
            throwButton.addActionListener(this);

            throwPanel = new JPanel();
            throwPanel.setLayout(new BorderLayout());
            throwPanel.add(throwButton, BorderLayout.CENTER);

            // Ställ in layout begränsningar som är specifika för valspanelen
            gbc.gridy = GridBagConstraints.RELATIVE;
            gbc.ipadx = 10;
            gbc.ipady = 10;
            gbc.insets = new Insets(0, 190, 18,
                    190);
            gbc.weightx = 0.01;
            gbc.weighty = 0.075;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.anchor = GridBagConstraints.PAGE_START;
            playPanel.add(throwPanel, gbc);

            dice = new Dice[5];
            for (int index = 0; index < 5; index++) {
                dice[index] = new Dice(dicePanel);
            }

            setUpActionListener(dice, this);

        }

        @Override
        public void actionPerformed(ActionEvent event) {
            /*
             Vals knappen klickade och göra knappen klickade eller Starta en ny runda
             */
            if (event.getSource() == throwButton) {
                throwButtonClicked();
            } else {
                nextRound();
                throwButton.setEnabled(round < 13);
            }
        }

        private void throwButtonClicked() {
            /*
             Rulla tärningen. Om detta är den sista rullen den här omgången, 
             inaktivera valsknappen, inaktivera valsknappen vid sändning 
             tråden och skicka dice värden till scoringmetoden genom poängen controller
             */
            rollDice();

            if (numberOfThrows == 3) {
                throwButton.setEnabled(false);
            }

            calcScores(getDiceValues());

        }

        private void rollDice() {
            // Rulla varje tärning
            numberOfThrows++;
            for (Dice dice1 : dice) {
                dice1.roll(numberOfThrows, 3);
            }
        }

        public int[] getDiceValues() {
            /*
             Gör en array för att hålla dicena värden, kopiera sedan 
             dicena värden i int array. Efter sortera arrayen dice 
             värden i stigande ordning och tillbaka arrayen
             */
            int[] diceValues = new int[dice.length];
            for (int index = 0; index < dice.length; index++) {
                diceValues[index] = dice[index].getResult();
            }
            Arrays.sort(diceValues);
            return diceValues;
        }

        private void nextRound() {
            numberOfThrows = 0;
            round++;
        }

        private void setUpActionListener(Dice[] dice, DiceDriver diceDriver1) {
            /*
             Inrätta tillfälliga JButton aktiv poäng för att spara.
             Lägg som handling avlyssnare för övre aktiva poäng knappar.
             Få knappen. Lägg till dice kontroll som en åtgärd lyssnare.
             Lägg till alla dice som action lyssnare.
             */
            JButton btnTemp;
            for (int scoreIndex = 0; scoreIndex < 6; scoreIndex++) {
                btnTemp = ((AliveScoreArea) above[scoreIndex]).getButton();
                btnTemp.addActionListener(diceDriver1);
                for (Dice dice1 : dice) {
                    btnTemp.addActionListener(dice1);
                }
            }

            /*
             Lägg som handling avlyssnare för lägre aktiva poäng knappar.
             Få knappen. Lägg till dice kontroll som en åtgärd lyssnare.
             Lägg till alla dice som action lyssnare.
             */
            for (int scoreIndex = 0; scoreIndex < 9; scoreIndex++) {
                btnTemp = ((AliveScoreArea) below[scoreIndex]).getButton();
                btnTemp.addActionListener(diceDriver1);
                for (Dice dice1 : dice) {
                    btnTemp.addActionListener(dice1);
                }
            }
        }
    }
}
