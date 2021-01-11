import java.util.*;

public class BFS {
	
	 Deque<Cell> q = new LinkedList<Cell>();

    private static class Cell  {
        int x;
        int y;
        int dist;  	//distance
        Cell prev;  //parent cell in the path

        Cell(int x, int y, int dist, Cell prev) {
            this.x = x;
            this.y = y;
            this.dist = dist;
            this.prev = prev;
        }
        
        @Override
        public String toString(){
        	return x + "," + y;
        }
    }
    
   
    void setQueue(Deque<Cell> arr) {
    	this.q = arr;
    }
    
    Deque<Cell> getQueue(){
    	return this.q;
    }
    
    //BFS, Time O(n^2), Space O(n^2)
    public void shortestPath(int[][] matrix, int[] start, int[] end) {
		int sx = start[0], sy = start[1];
		int dx = end[0], dy = end[1];
	   
	    int m = matrix.length;
	    int n = matrix[0].length;	    
        Cell[][] cells = new Cell[m][n];
        
        //create cells for all valid values in the matrix (excluding obstacles)
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == 0) {
                    cells[i][j] = new Cell(i, j, Integer.MAX_VALUE, null);
                }
            }
        }
        
        cells[sx][sy] = new Cell(sx, sy, Integer.MAX_VALUE, null);
        
        LinkedList<Cell> queue = new LinkedList<>();       
        
        //start cell
        Cell src = cells[sx][sy];
        
        //initialize distance
        src.dist = 0;
        
        //enqueue start cell
        queue.add(src);
        
        //destination cell
        Cell dest = null;
        
        //pointer cell (current cell)
        Cell p;
        
        
        while ((p = queue.poll()) != null) {
        	
        	//if the current cell is the destination then set destination cell and exit loop 
            if (p.x == dx && p.y == dy) { 
                dest = p;
                break;
            }
            
            // moving up
            visit(cells, queue, p.x - 1, p.y, p);
            
            // moving down
            visit(cells, queue, p.x + 1, p.y, p);
            
            // moving left
            visit(cells, queue, p.x, p.y - 1, p);
            
            //moving right
            visit(cells, queue, p.x, p.y + 1, p);
        }

        LinkedList<Cell> path = new LinkedList<>();
        
        if (dest == null) {
            return;
        } else {
            
            p = dest;
            do {
                path.addFirst(p);
            } while ((p = p.prev) != null);
        }
        
        setQueue(path);
    }

  //function to update cell visiting status
    void visit(Cell[][] cells, LinkedList<Cell> queue, int x, int y, Cell parent) { 
        
    	//check if the neighbor we want to traverse is valid
    	if (x < 0 || x >= cells.length || y < 0 || y >= cells[0].length || cells[x][y] == null) {
            return;
        }
        
    	//update parent cell distance
        int dist = parent.dist + 1;
        
        //
        Cell p = cells[x][y];
        
        //
        if (dist < p.dist) {
            p.dist = dist;
            p.prev = parent;
            queue.add(p);
        }
    }
    
    public int moves(int headX, int headY , Deque<Cell> deque, String snakeLine) {
//    	int headX = GetHead(snakeLine)[0];
//    	int headY = GetHead(snakeLine)[1];
    	
    	//path.remove();
    	
    	int nextX = Integer.parseInt(deque.peek().toString().split(",")[0]);
    	int nextY = Integer.parseInt(deque.peek().toString().split(",")[1]);
    	
//    	System.err.println("headX = " + headX + " nodeX = " + nextX);
//    	System.err.println("headY = " + headY + " nodeY = " + nextY);
    	
    	int move = 5;

    	if (nextX == headX) {
    		if (nextY > headY) {
    			move = 1;
    		}
    		else if (nextY < headY) {
    			move = 0;
    		}
    	}
    	
    	else if (nextY == headY) {
    		if (nextX > headX) {
    			move = 3;
    		}
    		else if (nextX < headX) {
    			move = 2;
    		}
    	}
    	
    	else if (myDirection(snakeLine) == "up") {						//if the snake is going upwards
    			if (headY < nextY) {
    				if (headX > nextX) {					//go right if apple is the right and left if apple is on the left
        				move = 2;
        			}
        			else {
        				move = 3;
        			}
    			}    			
    		}
    		
    	else if (myDirection(snakeLine) == "down") {						//if the snake is going downwards
    		if (headY > nextY) {
    			if (headX > nextX) {					//go right if apple is the right and left if apple is on the left
        			move = 2;
        		}
        		else {
        			move = 3;
        		}
    		}    			
    	}
    	   	
    	else if (myDirection(snakeLine) == "left") {						//if the snake is going left
    			if (headX < nextX) {
    				if (headY > nextY) {					//go up if apple is upwards and down if the apple is downwards
        				move = 0;
        			}
        			else {
        				move = 1;
        			}
    			}    			
    		}
    		
    	else if (myDirection(snakeLine) == "right") {						//if the snake is going right
    		if (headX > nextX) {
    			if (headY > nextY) {					//go up if apple is upwards and down if the apple is downwards
        			move = 0;
        		}
        		else {
        			move = 1;
        			}
    		}		
    	}
    		    	    	
    	else if (nextX > headX) {
        	move = 3;
        }
        
    	else if (nextX < headX) {
        	move = 2;
        }
        
    	else if (nextY > headY) {
        	move = 1;
        }
    	else if (nextY < headY) {
        	move = 0;
        }
    	     
        return move;
    }
    
    public String getBody(String snakeLine) {
    	
    	
    	if (snakeLine.length() > 0) {

        	String[] mySnake_coord = snakeLine.split(" ");
        	String[] arr = new String[mySnake_coord.length];
    		arr = Arrays.copyOfRange(mySnake_coord, 3, mySnake_coord.length);
    		String body = String.join(" ", arr);
        	
        	return body;
    	}
    	else {
    		return "hello";
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
    
    public int[] GetNeck(String snakeLine) {
    	String[] mySnake_coord = snakeLine.split(" ");
    	
    	String[] neck_coord = mySnake_coord[4].split(",");
    	
    	int neckX = Integer.parseInt(neck_coord[0]);
    	int neckY = Integer.parseInt(neck_coord[1]);
    	
    	int[] coord = {neckX, neckY};
    	
    	return coord;
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

//	public static void main(String[] args) { 
//	   int[][] matrix = {
//           {1, 1, 1, 1, 1, 1},
//           {1, 1, 1, 1, 0, 1},
//           {1, 0, 0, 0, 1, 1},
//           {1, 1, 1, 1, 1, 1},
//           {0, 0, 0, 0, 0, 0}
//	   };
//	   int[] start = {2, 4};
//	   int[] end = {3, 2};
//	   shortestPath(matrix, start, end);	   
//	} 
}