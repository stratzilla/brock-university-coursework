package Lab_x;

import BasicIO.*;
import Media.*;
import static java.lang.Math.*;



public class NestedSquares {
  
    private TurtleDisplayer display; 
    private Turtle robot;          
	
	public NestedSquares ( ) {
		display = new TurtleDisplayer();
		robot = new Turtle();
		display.placeTurtle(robot);

		
		int i,j;
		int size = 30;
		robot.penDown();
		for (j=0;j<5;j++){
			for (i=0; i<4; i++){			
				robot.forward(size);
				robot.left(Math.PI/2);	
			}
			size+=20;
		}	
	
		robot.penUp();
		display.close();
		
	};	
	
	
	public static void main ( String[] args ) { new NestedSquares(); };
	
}	