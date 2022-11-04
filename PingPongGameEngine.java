/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.acohen_pong;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.KeyListener; 
import java.awt.event.KeyEvent; 

import java.util.logging.Level; 
import java.util.logging.Logger; 
import java.util.Random; 

import java.awt.Graphics; 
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintWriter;
import static java.lang.Math.abs;
import java.util.Scanner;

/**
 *
 * @author abigailcohen
 */
public class PingPongGameEngine implements KeyListener, Runnable, MouseListener, MouseMotionListener, GameConstants{

    final private PingPongTable table; 
    public int kidRacket_Y=KID_RACKET_Y_START; 
    private int computerRacket_Y=GameConstants.COMPUTER_RACKET_Y_START; 
    Thread worker; 
    //for ball movement 
    private int ballX=BALL_START_X; 
    private int ballY=BALL_START_Y; 
    
    //min and max + R(andom) + B(all) for y axis (and at serve, x-axis) 
    private int minRB=1; 
    private int maxRB=2; 
    private int moveYNum; 
    //min and max + R(andom) + C(omputer racket)  for whether it will follow ball or not  
    private int minRC=1; 
    private int maxRC=3; 
    private int followBall;  
    
    Random randomGenerator = new Random(); 
    
    private boolean ballServed=false; 
    private boolean ballMissed=false; 
    private boolean movingLeft; 
    
    private int kidScore=0; 
    private int computerScore=0; 
    
    private boolean gameEnded=false; 
    
    private int tableIncNum=0; 
    
    File score = new File("score.txt");
    
    private String name;
    File savedName = new File("savedName.txt");
    
    PingPongGameEngine(PingPongTable t){
        table = t; 
        worker=new Thread(this); 
        worker.start(); 
        table.addKeyListener(this); 
    }

    @Override
    public void run() {
        boolean run=true; 
        askName(); 
        //resumes scores from last game 
        try{
            Scanner input = new Scanner(score);
            computerScore = input.nextInt();
            kidScore = input.nextInt();
        } catch(Exception e){
            computerScore = 0;
            kidScore = 0;
        }
        table.setMessageText("SCORE             COMPUTER: "+computerScore+"                 "+name.toUpperCase()+": "+kidScore); 
        
        moveYNum=randomGenerator.nextInt(maxRB - minRB + 1) + minRB; 
        followBall=randomGenerator.nextInt(maxRC - minRC +1)+ minRC; 
        while (run==true){
            if(!gameEnded){
                if (ballServed==true){
                    //moves the ball on x axis
                    if(movingLeft){
                        if(ballX > GameConstants.BALL_MIN_X){
                            ballX -= GameConstants.BALL_INCREMENT;  
                        }
                        else if(ballMissed){
                            ballX-=GameConstants.BALL_INCREMENT; 
                        }
                        else if(ballX <= GameConstants.BALL_MIN_X){
                            if(ballX<-30){
                                ballMissed=true; 
                            }
                            //ball hits computer racket 
                            else if (ballY>=table.getComputerRacket_Y(0)-25 && ballY<=table.getComputerRacket_Y(0)+80){
                                if(!ballMissed){
                                    computerScore+=1;
                                }
                                ballX+=GameConstants.BALL_INCREMENT; 
                                movingLeft=false; 
                                moveYNum=randomGenerator.nextInt(maxRB - minRB + 1) + minRB; 
                                
                                if(GameConstants.TABLE_TOP-(tableIncNum*GameConstants.TABLE_INCREMENT)>GameConstants.TABLE_TOP_MIN){
                                    tableIncNum+=1; 
                                    table.setTableIncrement(tableIncNum);
                                }
                            }
                            //ball misses computer racket 
                            else{
                                if(!ballMissed){
                                    kidScore+=1; 
                                }
                                ballX-=GameConstants.BALL_INCREMENT; 
                                ballMissed=true; 
                            }
                        }
                    }
                    else{
                        if(ballX < GameConstants.BALL_MAX_X){
                            ballX+=GameConstants.BALL_INCREMENT; 
                        }
                        else if(ballX >= GameConstants.BALL_MAX_X){
                            if(ballX>GameConstants.TABLE_WIDTH){
                                ballMissed=true; 
                            }
                            else if(ballMissed){
                                ballX+=GameConstants.BALL_INCREMENT; 
                            }
                            //ball hits kid racket 
                            else if (ballY>=table.getKidRacket_Y(0)-25 && ballY<=table.getKidRacket_Y(0)+80){
                                kidScore+=1;
                                ballX-=GameConstants.BALL_INCREMENT; 
                                movingLeft=true; 
                                //randomizes whether ball will move up or down 
                                moveYNum=randomGenerator.nextInt(maxRB - minRB + 1) + minRB; 
                                
                                followBall=randomGenerator.nextInt(maxRC - minRC +1)+ minRC; 
                                
                                if(GameConstants.TABLE_TOP-(tableIncNum*GameConstants.TABLE_INCREMENT)>GameConstants.TABLE_TOP_MIN){
                                    tableIncNum+=1; 
                                    table.setTableIncrement(tableIncNum);
                                }
                            }
                            //ball misses kid racket 
                            else{
                                if(!ballMissed){
                                    computerScore+=1; 
                                }
                                ballX+=GameConstants.BALL_INCREMENT; 
                                ballMissed=true; 
                                followBall=randomGenerator.nextInt(maxRC - minRC +1)+ minRC; 
                                
                            }
                        }
                    }               

                    //checks if ball is at the top or bottom of the board and needs to turn around 
                    if(ballY>=GameConstants.BALL_MAX_Y+(GameConstants.TABLE_INCREMENT*tableIncNum)){
                        moveYNum=2; 
                    }
                    else if (ballY<=GameConstants.BALL_MIN_Y-(GameConstants.TABLE_INCREMENT*tableIncNum)){
                        moveYNum=1; 
                    }

                    //moves ball on y axis 
                    if(moveYNum==1){ //if 1, moves down 
                        ballY+=GameConstants.BALL_INCREMENT; 
                    }else{ // if (moveYNum==2), moves up 
                        ballY-=GameConstants.BALL_INCREMENT; 
                    }

                    table.setBallPosition(ballX, ballY);
                    table.setMessageText("SCORE             COMPUTER: "+computerScore+"                 "+name.toUpperCase()+": "+kidScore); 
        
                    //moves computer racket 
                    int computerYNum = table.getComputerRacket_Y(0); 
                    if(computerYNum>TABLE_TOP-(GameConstants.TABLE_INCREMENT*tableIncNum)
                            &&computerYNum<TABLE_BOTTOM+(GameConstants.TABLE_INCREMENT*tableIncNum)){
                        if(ballMissed){
                            if(moveYNum==1){
                                computerRacket_Y+= RACKET_INCREMENT; 
                            }else{
                                computerRacket_Y-= RACKET_INCREMENT; 
                            }
                        }
                        else if(followBall==3){ //doesn't follow ball
                            if(moveYNum==1){//ball is moving down 
                                if (ballY<computerRacket_Y){
                                    computerRacket_Y += RACKET_INCREMENT;
                                }
                                else{
                                    computerRacket_Y -= RACKET_INCREMENT;
                                }
                            }
                            else{//ball is moving up 
                                if (ballY>computerRacket_Y){
                                    computerRacket_Y -= RACKET_INCREMENT; 
                                }
                                else{
                                    computerRacket_Y += RACKET_INCREMENT;
                                }
                            }
                        }
                        else{ //follows ball 
                            if(moveYNum==1){ //ball is moving down 
                                if (ballY<computerRacket_Y){
                                    computerRacket_Y -= RACKET_INCREMENT;
                                }
                                else{
                                    computerRacket_Y += RACKET_INCREMENT;
                                }
                            }
                            else{ //ball is moving up 
                                if (ballY>computerRacket_Y){
                                    computerRacket_Y += RACKET_INCREMENT; 
                                }
                                else{
                                    computerRacket_Y -= RACKET_INCREMENT;
                                }
                            }
                        }

                    } else{ moveComputerRacket();}  

                    if(computerRacket_Y>TABLE_TOP-(GameConstants.TABLE_INCREMENT*tableIncNum)
                            &&computerRacket_Y<TABLE_BOTTOM+(GameConstants.TABLE_INCREMENT*tableIncNum)){
                        table.setComputerRacket_Y(computerRacket_Y); 
                    } else{ moveComputerRacket();}  
                    
                    table.repaint(); 
                }
            }
            
            if(ballMissed){
                try{
                    PrintWriter out = new PrintWriter(score);
                    out.println(computerScore);
                    out.println(kidScore);
                    out.close();
                }catch(Exception e){
                     System.out.println("Error encountered " + e.toString());
                }
            }

            //checks score 
            if(computerScore>=11|| kidScore>=11){
                if(abs(computerScore-kidScore)>=2){
                    endGame(); 
                }
            }
            
            table.requestFocus(); 
            
            try{
                Thread.sleep(40); 
            } catch(InterruptedException ex){
                Logger.getLogger(PingPongGameEngine.class.getName()).log(Level.SEVERE, null, ex); 
            }
        }
    }
    public void askName(){
        
        try{
            Scanner inputName = new Scanner(savedName);
            name = inputName.nextLine();           
        } catch(Exception e){
            System.out.println("No name previously saved."); 
        }
        
        Scanner askKidName = new Scanner(System.in); 
        if(name!=null){
            System.out.println("Is "+name+" your name? (yes/no)"); 
            String response=askKidName.nextLine(); 
            if("no".equals(response.toLowerCase())){
                System.out.println("What is your name? "); 
                name=askKidName.nextLine();   
            } else if(!"yes".equals(response.toLowerCase())){ 
                boolean ask=true; 
                while(ask){
                    System.out.println("Is "+name+" your name? (yes/no)"); 
                    response=askKidName.nextLine(); 
                    if("no".equals(response.toLowerCase())){
                        System.out.println("What is your name? "); 
                        name=askKidName.nextLine();
                        ask=false; 
                    } else if("yes".equals(response.toLowerCase())){
                        ask=false; 
                    }
                }
            }
        } else{
            System.out.println("What is your name? "); 
            name=askKidName.nextLine();   
        }
        
        try{
            PrintWriter out = new PrintWriter(savedName);
            out.println(name);
            out.close();
        }catch(Exception e){
             System.out.println("No name to save.");
        }
        
    }
    
    public void moveComputerRacket(){
        boolean move=false; 
        if ((computerRacket_Y<=TABLE_TOP-(GameConstants.TABLE_INCREMENT*tableIncNum))||(computerRacket_Y>=TABLE_BOTTOM+(GameConstants.TABLE_INCREMENT*tableIncNum))){
            move=true; 
        }
        while (move==true){
            if(computerRacket_Y<=TABLE_TOP-(GameConstants.TABLE_INCREMENT*tableIncNum)){
                computerRacket_Y += RACKET_INCREMENT; 
            }
            else if(computerRacket_Y>=TABLE_BOTTOM+(GameConstants.TABLE_INCREMENT*tableIncNum)){
                computerRacket_Y -= RACKET_INCREMENT; 
            }
            else{
                move=false; 
            }
        }
    }

    public void kidServe(){
        if(!gameEnded){
            if (!ballServed || ballMissed){
                ballMissed=false; 
                ballServed=true; 
                //at serve, random number chooses if ball moves right or left first 
                int startRightOrLeft=randomGenerator.nextInt(maxRB - minRB + 1) + minRB; 
                if(startRightOrLeft==1){
                    movingLeft=true; 
                }
                else{
                    movingLeft=false; 
                }
                //sets ball position 
                ballX=BALL_START_X; 
                ballY=BALL_START_Y; 

                //random num decides whether ball moves up or down first 
                moveYNum=randomGenerator.nextInt(maxRB - minRB + 1) + minRB; 

                //random num decides whether the computer racket will follow the ball or not 
                followBall=randomGenerator.nextInt(maxRC - minRC +1)+ minRC; 
                
                table.setBallPosition(ballX, ballY); 
                table.setKidRacket_Y(kidRacket_Y);

                table.repaint(); 
            }
        }
    }
    
    public void endGame(){
        ballServed=false; 
        gameEnded=true; 
        
        if (computerScore>kidScore){
            table.setMessageText("COMPUTER: "+computerScore+" "+name.toUpperCase()+": "+kidScore+"     Computer won. Exit the window or choose N to start a new game."); 
        } else if(computerScore<kidScore){
            table.setMessageText("COMPUTER: "+computerScore+" "+name.toUpperCase()+": "+kidScore+"     You won. Exit the window or choose N to start a new game."); 
        } else{
            table.setMessageText("COMPUTER: "+computerScore+" "+name.toUpperCase()+": "+kidScore+"     Tie. Exit the window or choose N to start a new game.");  
        }
        
        //score saved 
        try{
            PrintWriter out = new PrintWriter(score);
            out.println(computerScore);
            out.println(kidScore);
            out.close();
        }catch(Exception e){
             System.out.println("Error encountered " + e.toString());
        }
    }
        
    public void startNewGame(){
        gameEnded=false; 
        ballServed=false; 
        ballMissed=false; 
        
        ballX=BALL_START_X; 
        ballY=BALL_START_Y; 
        
        kidScore=0; 
        computerScore=0; 
        computerRacket_Y=GameConstants.COMPUTER_RACKET_Y_START; 
        
        tableIncNum=0; 
        table.setTableIncrement(tableIncNum);
        
        table.setBallPosition(ballX, ballY); 
        table.setKidRacket_Y(GameConstants.KID_RACKET_Y_START);
        table.setComputerRacket_Y(computerRacket_Y);
        table.repaint(); 
        table.setMessageText("SCORE             COMPUTER: "+computerScore+"                 "+name.toUpperCase()+": "+kidScore); 
        
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        char key=e.getKeyChar(); 
        
        if('n'==key || 'N' ==key){
            startNewGame(); 
        }
        else if ('q'==key || 'Q' ==key){
            endGame(); 
        }
        else if ('s'==key || 'S' ==key){
            kidServe();  
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyReleased(KeyEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void mouseClicked(MouseEvent e){
        int colorScheme=table.getColorScheme(); 
        if(colorScheme==5){
            colorScheme=1; 
        } else{
            colorScheme+=1; 
        }
        table.setColorScheme(colorScheme); 
        table.repaint(); 
    }
    
    @Override
    public void mouseMoved(MouseEvent e){
        int mouse_Y=e.getY(); 
        if (ballServed==true){
            if(mouse_Y<kidRacket_Y && kidRacket_Y>TABLE_TOP-(GameConstants.TABLE_INCREMENT*tableIncNum)){
                kidRacket_Y -= RACKET_INCREMENT; 
            } else if (kidRacket_Y < TABLE_BOTTOM+(GameConstants.TABLE_INCREMENT*tableIncNum)){
                kidRacket_Y += RACKET_INCREMENT; 
            } 
            table.setKidRacket_Y(kidRacket_Y); 
            table.repaint(); 
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseDragged(MouseEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
