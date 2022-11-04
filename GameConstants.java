package com.mycompany.acohen_pong;

/**
 *
 * @author abigailcohen
 */


public interface GameConstants {
    public final int TABLE_WIDTH=750; 
    public final int TABLE_HEIGHT=600; 
    //kid means the player 
    public final int KID_RACKET_Y_START=260; 
    public final int KID_RACKET_X_START=685; 
    
    public final int TABLE_TOP=230; 
    public final int TABLE_BOTTOM=290; 
    public final int TABLE_INCREMENT=10; 
    public final int TABLE_TOP_MIN=30; 
    public final int TABLE_BOTTOM_MAX=480;
    
    public final int RACKET_INCREMENT=15; 
    
    public final int COMPUTER_RACKET_X=50; 
    public final int COMPUTER_RACKET_Y_START=260; 
    
    //ball movement increment in pixels 
    public final int BALL_INCREMENT=15; 
    
    //max and min allowed ball coordinates 
    public final int BALL_MIN_X=65; 
     public final int BALL_MIN_Y=240; 
    public final int BALL_MAX_X=TABLE_WIDTH-95; 
    public final int BALL_MAX_Y=TABLE_BOTTOM+50; 
    
    //starting coordinates of the ball 
    public final int BALL_START_X=375; 
    public final int BALL_START_Y=300; 
    public final int BALL_CENTER=5; 

}
