import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

//import ShortestPath.Cell;


public class MyAgent extends za.ac.wits.snake.MyAgent {

    public static void main(String args[]) throws IOException {
        MyAgent agent = new MyAgent();
        MyAgent.start(agent, args);
    }

    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            String initString = br.readLine();
            String[] temp = initString.split(" ");
            int nSnakes = Integer.parseInt(temp[0]);
            
            //Apple decay
            int appleCurrX = -1, appleCurrY = -1;
            int snakeHealth = 50;
            
            while (true) {
                String line = br.readLine();
                if (line.contains("Game Over")) {
                    break;
                }

                String apple1 = line;
                //do stuff with apples
                String[] apple_coord = apple1.split(" ");
                
                int appleX = Integer.parseInt(apple_coord[0]);
                int appleY = Integer.parseInt(apple_coord[1]);
                
                //System.err.println("apple: " + appleX + "," + appleY);

                //initialize snake board and set elements to zero
                int[][] play_area = new int[50][50];
        		
        		for (int i = 0; i < 50; i++) {
        			for (int j = 0; j < 50; j++) {
        				play_area[i][j] = 0;
        			}
        		}
        		
                // read in obstacles and do something with them!
                int nObstacles = 3;
                
                for (int obstacle = 0; obstacle < nObstacles; obstacle++) {
                    String obs = br.readLine();
                    // do something with obs
                    drawSnake(obs, 4, play_area);
                    
                }
                

                int mySnakeNum = Integer.parseInt(br.readLine());
                int move = 5;
        		
                int[] start = new int[2];
                int[] end = {appleX, appleY};
                String mySnake = "";
               
                
                for (int i = 0; i < nSnakes; i++) {
                    String snakeLine = br.readLine();
                    if (i == mySnakeNum) {
                        //hey! That's me :                    	
                    	move = move(snakeLine, appleX, appleY);
                    	start[0] = GetHead(snakeLine)[0]; start[1] = GetHead(snakeLine)[1];
                    	mySnake = snakeLine;
                    	drawSnake(getBody(snakeLine), 7, play_area);
                    }
                    //do stuff with other snakes
                    else if (i != mySnakeNum) {
	                    drawSnake(getBody(snakeLine), 5, play_area);
	                    headCollision(snakeLine, play_area);
                    }
                }
       
    			int headX = GetHead(mySnake)[0];
    			int headY = GetHead(mySnake)[1];
//    			System.err.println(appleX + ", " + appleY );
    			 
    			
    			if (appleCurrX == appleX && appleCurrY == appleY) {
    				snakeHealth--;
    				if (snakeHealth < 0) {
    					play_area[appleX][appleY] = 5;
    					end = chaseTail(mySnake, appleX, appleY); 
    				}
//    				System.err.println("snake health: " + snakeHealth);
    			}
    			else if (appleCurrX != appleX && appleCurrY != appleY) {
    				appleCurrX = appleX; 
    				appleCurrY = appleY;
    				snakeHealth = 50;
    			}
    			
    			//get my snake length
    			String[] snakeLength = mySnake.split(" ");
    			
    			int myLength = Integer.parseInt(snakeLength[1]);
    			
    			if (myLength > 30) {
                //BFS
    				BFS bfs = new BFS();
    				bfs.shortestPath(play_area, start, end);
//                    System.err.println(bfs.getQueue());
    				
    				if (!bfs.getQueue().isEmpty()) {
                    
    					bfs.getQueue().removeFirst();
    				}
                    
                    if (!bfs.getQueue().isEmpty()) {
                		
                    	move = bfs.moves(headX, headY, bfs.getQueue(), mySnake);
//                    	System.err.println(bfs.getQueue());
                    	System.out.println(move);	
 	
                    }
    			}
    			
    			else {
    			
    			//Astar
                Astar a_star = new Astar(headX, headY, end[0], end[1], play_area);
                
                a_star.process();
    			a_star.astar();
              
    			
                if (a_star.path != null) {
                	 a_star.path.pop();
                }
                 
                  if (!a_star.path.empty()) {
                	  move = a_star.moves(headX, headY, a_star.path, mySnake);
	                  	
                	  System.out.println(move);
//	                  a_star.Board(a_star.grid); 
                  }
    				
    			}
//    			
//    			 Board(play_area);        		
                //finished reading, calculate move:
                //int move = new Random().nextInt(4);
                
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public int[] GetHead(String snakeLine) {
    	String[] mySnake_coord = snakeLine.split(" ");
    	
    	String[] head_coord = mySnake_coord[3].split(",");
    	
    	int headX = Integer.parseInt(head_coord[0]);
    	int headY = Integer.parseInt(head_coord[1]);
    	
    	int[] coord = {headX, headY};
    	
    	return coord;
    }
    
    public int[] GetTail(String snakeLine) {
    	String[] mySnake_coord = snakeLine.split(" ");
    	
    	String[] tail_coord = mySnake_coord[mySnake_coord.length-1].split(",");
    	
    	int tailX = Integer.parseInt(tail_coord[0]);
    	int tailY = Integer.parseInt(tail_coord[1]);
    	
    	int[] coord = {tailX, tailY};
    	
    	return coord;
    }
    
    public int[] GetNeck(String snakeLine) {
    	String[] mySnake_coord = snakeLine.split(" ");
    	
    	String[] neck_coord = mySnake_coord[4].split(",");
    	
    	int neckX = Integer.parseInt(neck_coord[0]);
    	int neckY = Integer.parseInt(neck_coord[1]);
    	
    	int[] coord = {neckX, neckY};
    	
    	return coord;
    }
    
    //remove everything from the given string and only get the snake body coordinates
    public String getBody(String snakeLine) {
    	
    	if (snakeLine.length() > 0) {

        	String[] mySnake_coord = snakeLine.split(" ");
        	String[] arr = new String[mySnake_coord.length];
    		arr = Arrays.copyOfRange(mySnake_coord, 3, mySnake_coord.length);
    		String body = String.join(" ", arr);
        	
        	return body;
    	}
    	else {
    		return "hello, world!";
    	}
    }
    
    int[] chaseTail(String snakeLine, int appleX, int appleY) {
    	int tailX = GetTail(snakeLine)[0];
    	int tailY = GetTail(snakeLine)[1];
    	
    	int newAppleX = appleX;
    	int newAppleY = appleY;
    	
    	if (tailX + 20 < 50) newAppleX = tailX+20;
    	else if (tailX-20 > -1) newAppleX = tailX-20;
    	
    	if (tailY + 20 < 50) newAppleY = tailY+20;
    	else if (tailY-20 > -1) newAppleY = tailY-20;
    	
    	int[] newCoord = {newAppleX, newAppleY};
    	
    	return newCoord;
    }
     
    public String myDirection(String snakeLine) {
    	int headX = GetHead(snakeLine)[0];
    	int headY = GetHead(snakeLine)[1];
    	
    	int neckX = GetNeck(snakeLine)[0];
    	int neckY = GetNeck(snakeLine)[1];
    	
    	String direction = "";
    	
    	if (headX == neckX) {						
    		if (headY < neckY) {
    			direction = "up";
    		}
    		else {
    			direction = "down";
    		}
    	}
    	else if (headY == neckY) {						
    		if (headX < neckX) {
    			direction = "left";
    		}
    		else {
    			direction = "right";
    		}
    	}
    	
    	return direction;
    }
  
    public int move(String snakeLine, int appleX, int appleY) {
    	
    	int headX = GetHead(snakeLine)[0];
    	int headY = GetHead(snakeLine)[1];

    	int move = 5;
    	
    	if (appleX == headX) {
    		if (appleY > headY) {
    			move = 1;
    		}
    		else if (appleY < headY) {
    			move = 0;
    		}
    	}
    	
    	else if (appleY == headY) {
    		if (appleX > headX) {
    			move = 3;
    		}
    		else if (appleX < headX) {
    			move = 2;
    		}
    	}
    	
    	else if (myDirection(snakeLine) == "up") {						//if the snake is going upwards
    			if (headY < appleY) {
    				if (headX > appleX) {					//go right if apple is the right and left if apple is on the left
        				move = 2;
        			}
        			else {
        				move = 3;
        			}
    			}    			
    		}
    		
    	else if (myDirection(snakeLine) == "down") {						//if the snake is going downwards
    		if (headY > appleY) {
    			if (headX > appleX) {					//go right if apple is the right and left if apple is on the left
        			move = 2;
        		}
        		else {
        			move = 3;
        		}
    		}    			
    	}
    	   	
    	else if (myDirection(snakeLine) == "left") {						//if the snake is going left
    			if (headX < appleX) {
    				if (headY > appleY) {					//go up if apple is upwards and down if the apple is downwards
        				move = 0;
        			}
        			else {
        				move = 1;
        			}
    			}    			
    		}
    		
    	else if (myDirection(snakeLine) == "right") {						//if the snake is going right
    		if (headX > appleX) {
    			if (headY > appleY) {					//go up if apple is upwards and down if the apple is downwards
        			move = 0;
        		}
        		else {
        			move = 1;
        			}
    		}		
    	}
    		    	    	
    	else if (appleX > headX) {
        	move = 3;
        }
        
    	else if (appleX < headX) {
        	move = 2;
        }
        
    	else if (appleY > headY) {
        	move = 1;
        }
    	else if (appleY < headY) {
        	move = 0;
        }
    	     
        return move;
    }
    
    public void drawSnake(String snake_input, int snake_num, int[][] play_area) {
		String[] points = snake_input.split(" ");
		
		for (int j = 0; j < points.length-1; j++) {
			drawLine(play_area, points[j], points[j+1], snake_num);
		}
		
	}
	
	public void drawLine(int[][] play_area, String first_point, String sec_point, int snake_num) {
		String[] point_one = first_point.split(",");
		String[] point_two = sec_point.split(",");
		
		int x_one = Integer.parseInt(point_one[0]);
		int y_one = Integer.parseInt(point_one[1]);
		
		int x_two = Integer.parseInt(point_two[0]);
		int y_two = Integer.parseInt(point_two[1]);
		
		if (x_one > x_two && y_one == y_two) {
			for (int i = x_two; i <= x_one; i++) {
				play_area[i][y_one] = snake_num;
			}
		}
		else if (x_two > x_one && y_one == y_two) {
			for (int i = x_one; i <= x_two; i++) {
				play_area[i][y_one] = snake_num;
			}
		}
		
		if (y_one > y_two && x_one == x_two) {
			for (int i = y_two; i <= y_one; i++) {
				play_area[x_one][i] = snake_num;
			}
		}
		else if (y_two > y_one && x_one == x_two) {
			for (int i = y_one; i <= y_two; i++) {
				play_area[x_one][i] = snake_num;
			}
		}
	}
	
	  
    public void headCollision(String snakeLine, int[][] play_area) {
    
    	String[] status = snakeLine.split(" ");
    	
    	if (status.length > 3) {
//    		if (snakeLine != null && snakeLine != "") {
        		
            	int x = GetHead(snakeLine)[0];
            	int y = GetHead(snakeLine)[1];
            	
            	if (x+1 < 50) play_area[x+1][y] = 5;
            	
            	if (x-1 > -1) play_area[x-1][y] = 5; 
            	
            	if (y+1 < 50) play_area[x][y+1] = 5; 
            	
            	if (y-1 > -1) play_area[x][y-1] = 5;	
            	
//        	}
    	}
    	
    	
    }
	
	//get the game board as a matrix
	public int[][] Board(int[][] play_area) {
		
		int[][] board = new int[50][50];
		
		for (int i = 0; i < play_area.length; i++) {
			if (i > 9) {
				System.err.print(i + " ");
			}
			else {
				System.err.print(i + "  ");
			}
			
			board[i][0] = play_area[0][i];
			System.err.print(board[i][0]);
			
			for (int j = 1; j < play_area[i].length; j++) {
				board[i][j] = play_area[j][i];
				System.err.print("" + board[i][j]);
			}
			System.err.println();
		}
		
		return board;
		
	}   
    
}