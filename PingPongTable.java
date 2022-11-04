/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.acohen_pong;

/**
 *
 * @author abigailcohen
 */

import javax.swing.JPanel; 
import javax.swing.JFrame; 
//BoxLayout puts components in a single row/column, 
//respects the maximum sizes set, and allows for alignment 
import javax.swing.BoxLayout; 
import javax.swing.JLabel; 
import javax.swing.WindowConstants; //control the closing of the window 

import java.awt.Point; //represents a location in (x,y) coordinate space 
import java.awt.Dimension; //indicates width & height of object 
import java.awt.Container; 
import java.awt.Graphics; 
import java.awt.Color;

public class PingPongTable extends JPanel implements GameConstants{
    JLabel label; 
    public Point point= new Point(0,0); 
    
    public int ComputerRacket_X=50; 
    private int kidRacket_Y= KID_RACKET_Y_START; 
    private int computerRacket_Y=GameConstants.COMPUTER_RACKET_Y_START; 
     
    //starting coordinates of the ball 
    private int ballX=GameConstants.BALL_START_X; 
    private int ballY=GameConstants.BALL_START_Y; 
    
    private int colorScheme=1; 
    
    private int tableIncrement=3; 
    
    Dimension preferredSize=new Dimension(TABLE_WIDTH, TABLE_HEIGHT); 
    
    @Override
    public Dimension getPreferredSize(){
        return preferredSize; 
    }
    
    PingPongTable(){
        PingPongGameEngine gameEngine=new PingPongGameEngine(this); 
        //Listen to mouse clicks to show its coordinates 
        addMouseListener(gameEngine); 
        //Listen to mouse movements to move the rackets 
        addMouseMotionListener(gameEngine); 
    }
    
    public static void main(String[] args){
        //Create an instance of the frame 
        JFrame f = new JFrame("Ping Pong Table"); 
        //Ensure that the window can be closed 
        //by pressing a little cross in the corner 
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); 
        PingPongTable table=new PingPongTable(); 
        table.addPaneltoFrame(f.getContentPane()); 
        //set the size and make the frame visible 
        f.pack(); 
        f.setVisible(true); 
    }
    
    void addPaneltoFrame(Container container){
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS)); 
        container.add(this); 
        label=new JLabel("Click anywhere on the board to switch colors. "
                + "Press S to Serve, N to start a new game, or Q to quit a game."); 
        container.add(label); 
        //adds a field with the score at the bottom of the frame 
        label = new JLabel("SCORE             COMPUTER: 0                 YOU: 0"); 
        container.add(label); 
    }
    
    public void setMessageText(String text){
        label.setText(text); 
    }
    
    public void paintComponent(Graphics g){
        //if colorScheme == 1
        //had to initialize variables 
        String tableColor = "#93B7BE"; 
        String innerTableColor="#2D3047"; 
        String playerColor="#A799B7"; 
        String computerColor="#048A81"; 
        String ballColor="#E0CA3C"; 
        switch (colorScheme) {
            case 2:
                tableColor = "#1C0B19";
                innerTableColor="#140D4F";
                playerColor="#4EA699";
                computerColor="#2DD881";
                ballColor="#6FEDB7";
                break;
            case 3:
                tableColor = "#3A4F41";
                innerTableColor="#B9314F";
                playerColor="#D5A18E";
                computerColor="#DEC3BE";
                ballColor="#E1DEE3";
                break;
            case 4:
                tableColor = "#2D7DD2";
                innerTableColor="#97CC04";
                playerColor="#EEB902";
                computerColor="#F45D01";
                ballColor="#474647";
                break;
            case 5:
                tableColor = "#EDF67D";
                innerTableColor="#F896D8";
                playerColor="#CA7DF9";
                computerColor="#724CF9"; 
                ballColor="#564592";
                break;
            default:
                break;
        }
        
        super.paintComponent(g); 
        
        //color table 
        g.setColor(Color.decode(tableColor)); 
        g.fillRect(0, 0, TABLE_WIDTH, TABLE_HEIGHT);
        
        //color inner table
        g.setColor(Color.decode(innerTableColor)); 
        g.fillRect(30, 230-tableIncrement, 690, 140+(tableIncrement*2));
        
        //color right racket (player's) 
        g.setColor(Color.decode(playerColor)); 
        g.fillRect(KID_RACKET_X_START, kidRacket_Y, 10, 80); 
        
        //color left racket (computer's) 
        g.setColor(Color.decode(computerColor)); 
        g.fillRect(ComputerRacket_X, computerRacket_Y, 10, 80); 
        
        //color ball 
        g.setColor(Color.decode(ballColor)); 
        g.fillOval(ballX, ballY, 28, 28); 
    }
    
    public int getTableIncrement(){
        return tableIncrement; 
    }
    
    public void setTableIncrement(int n){
        tableIncrement=GameConstants.TABLE_INCREMENT*n; 
    }
    
    public void setColorScheme(int n){
        colorScheme=n; 
    }
    
    public int getColorScheme(){
        return colorScheme; 
    }
    
    public void setBallPosition(int xPos, int yPos){
        ballX=xPos; 
        ballY=yPos; 
        repaint(); 
    }
    
    //set the current position of the kid's racket 
    public void setKidRacket_Y(int xCoordinate){
        this.kidRacket_Y=xCoordinate; 
    }
    
    public int getKidRacket_Y(int xCoordinate){
        return kidRacket_Y; 
    }
    
    public void setComputerRacket_Y(int yCoordinate){
        this.computerRacket_Y=yCoordinate; 
        repaint(); 
    }
    
    public int getComputerRacket_Y(int yCoordinate){
        return computerRacket_Y; 
    }  
} 