import java.util.*;

public class Astar {
	
	//vertical and horizontal cost
	static final int vhCost = 10;
	
	//cells for our grid
	Cell[][] grid = new Cell[50][50];
	
	//unvisited cells still to be traversed
	PriorityQueue<Cell> unvisitedCells = new PriorityQueue<Cell>();
	
	//queue for the path
	Stack<Cell> path = new Stack<Cell>();
	
	//cells already evaluated
	boolean[][] visited;
	
	int startX, startY;
	
	int endX, endY;
	
	
	Astar(int sX, int sY, int eX, int eY, int[][] board){
//		grid = new Cell[50][50];
		visited = new boolean[50][50];
		unvisitedCells = new PriorityQueue<Cell> ((Cell c1, Cell c2) -> {
			return c1.finalCost < c2.finalCost ? -1 : c1.finalCost > c2.finalCost ? 1 : 0;
		});
		
		startCell(sX, sY);
		endCell(eX, eY);
		
		
//		grid[sX][sY] = start;
		Cell end = new Cell(eX, eY);
		path.push(end);
		
		//initialize heuristic and cells
//		for (int i = 0; i < grid.length; i++) {
//			for (int j = 0; j < grid[i].length; j++) {
//				if (grid[i][j] != null) {
//					grid[sX][sY] = new Cell(sX, sY);
//					grid[i][j] = new Cell(i, j);
//					grid[i][j].h_cost = Math.abs(i - endX) + Math.abs(j - endY);
//					grid[i][j].isSolution = false;
//				}
//			}
//		}
		
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if (board[i][j] == 0) {
					grid[i][j] = new Cell(i, j);
					grid[i][j].h_cost = Math.abs(i - endX) + Math.abs(j - endY);
					grid[i][j].isSolution = false;
				}
				else {
					grid[i][j] = null;
				}
			}
		}
		

		grid[sX][sY] = new Cell(sX, sY);
//		grid[eX][eY] = new Cell(eX, eY);
		grid[startX][startY].finalCost = 0;
		
//		System.err.println("startX: " + startX + " startY: " + startY);
//		System.err.println("endX: " + endX + " endY: " + endY);
//		System.err.println("sX: " + sX + " sY: " + sY);
//		System.err.println("eX: " + eX + " eY: " + eY);
		//place blocks in the grid
//		for (int i = 0; i < obs.size(); i++) {
//			int x = Integer.parseInt(obs.get(i).split(",")[0]);
//			int y = Integer.parseInt(obs.get(i).split(",")[1]);
//			addObstacles(x, y);
//			System.err.println("obsX: " + x + " obsY: " + y);
//			
//		}
	}
		
	public class Cell{
		
		//coordinates
		int x, y;
		//parent cell of path
		Cell parent;
		//Heuristic Cost
		int h_cost;
		//final cost
		int finalCost;
		//if cell is part of the solution path
		boolean isSolution; 
		
		
		public Cell(int i, int j) {
			this.x = i;
			this.y = j;
		}
		
		@Override
        public String toString(){
        	return x + "," + y;
        }
	}

		void addObstacles(int i, int j) {
			grid[i][j] = null;
		}
		
		void startCell(int i, int j) {
			startX = i;
			startY = j;
		}
		
		void endCell(int i, int j) {
			endX = i;
			endY = j;
		}
		
		void updateCost(Cell curr, Cell t, int cost) {
			
			//if the neighboring cell is already visited or if it's an obstacle then return (do nothing)
			if (t == null || visited[t.x][t.y]) return;
			
			//the final cost of the neighboring cell
			int tFinalCost = t.h_cost + cost;
			
			//check if the neighboring cell was stored in unvisited cells to avoid invalid and out of bound cells
			boolean isOpen = unvisitedCells.contains(t);
			
			//if the cell is not an obstacle and it's not visited then update the cost and set it's parent and add it to unvisited cells
			//update the cost of the neighboring cell if needed
			if (!isOpen || tFinalCost < t.finalCost) {
				t.finalCost = tFinalCost;
				t.parent = curr;
				
				if (!isOpen) unvisitedCells.add(t);
			}
		}
		
		
		void process() {
			//add start cell to unvisited cells
//			Cell start = new Cell(startX, startY);
//			unvisitedCells.add(start);
			unvisitedCells.add(grid[startX][startY]);
//			System.err.println("StartX: " + start.x + " StartY: " + start.y);
			Cell curr;
			
			while (true) {
				//Cell end = new Cell(endX, endY);
//				System.err.println("X: " + unvisitedCells.peek().x + " Y: " + unvisitedCells.peek().y);
				
				//point current to the first unvisited cell 
				if (unvisitedCells != null) curr = unvisitedCells.poll();
				
				else break;
				
				if (curr != null) {
					//mark the unvisited cell as visited
					visited[curr.x][curr.y] = true;
					
					//if we have reached the apple then return (we done)
					if (curr.equals(grid[endX][endY])) return;
					
					Cell t;
					
					//traverse all neighboring cells (horizontally and vertically)
					//calculate the total cost in distance to travel to each neighboring cell
					if (curr.x - 1 >= 0) {
						t = grid[curr.x -1][curr.y];
						updateCost(curr, t, curr.finalCost + vhCost);
					}
					
					if (curr.y + 1 < grid[0].length) {
						t = grid[curr.x][curr.y + 1];
						updateCost(curr, t, curr.finalCost + vhCost);
					}
					
					if (curr.x + 1 < grid.length) {
						t = grid[curr.x + 1][curr.y];
						updateCost(curr, t, curr.finalCost + vhCost);
					}
					
					if (curr.y - 1 >= 0) {
						t = grid[curr.x][curr.y - 1];
						updateCost(curr, t, curr.finalCost + vhCost);
					}
				}
				else return;
			}
		}
		
		void astar() {
			
			//if we've traversed till the apple
			if (visited[endX][endY]) {
//				Cell end = new Cell(endX, endY);
				//track back the path
				Cell curr = grid[endX][endY];
				
				//mark as part of the solution path
				grid[curr.x][curr.y].isSolution = true;
				
				while (curr.parent != null) {
					
					//add parent cell to path(Stack)
					path.push(curr.parent);
					
//					System.err.println("currX: " + curr.parent.x + " currY: " + curr.parent.y);
					
					grid[curr.parent.x][curr.parent.y] = curr.parent;
					//mark parent as part of the solution path
					grid[curr.parent.x][curr.parent.y].isSolution = true;
					
					//move to the next parent
					curr = curr.parent;
				}
			}
		}
		
		 public static void drawSnake(String snake_input, int snake_num, Cell[][] play_area) {
				String[] points = snake_input.split(" ");
				
				for (int j = 0; j < points.length-1; j++) {
					drawLine(play_area, points[j], points[j+1], snake_num);
				}
				
			}
			
			public static void drawLine(Cell[][] play_area, String first_point, String sec_point, int snake_num) {
				String[] point_one = first_point.split(",");
				String[] point_two = sec_point.split(",");
				
				int x_one = Integer.parseInt(point_one[0]);
				int y_one = Integer.parseInt(point_one[1]);
				
				int x_two = Integer.parseInt(point_two[0]);
				int y_two = Integer.parseInt(point_two[1]);
				
				if (x_one > x_two && y_one == y_two) {
					for (int i = x_two; i <= x_one; i++) {
						play_area[i][y_one] = null;
					}
				}
				else if (x_two > x_one && y_one == y_two) {
					for (int i = x_one; i <= x_two; i++) {
						play_area[i][y_one] = null;
					}
				}
				
				if (y_one > y_two && x_one == x_two) {
					for (int i = y_two; i <= y_one; i++) {
						play_area[x_one][i] = null;
					}
				}
				else if (y_two > y_one && x_one == x_two) {
					for (int i = y_one; i <= y_two; i++) {
						play_area[x_one][i] = null;
					}
				}
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
			
			   public int moves(int headX, int headY , Stack<Cell> deque, String snakeLine) {
//			    	int headX = GetHead(snakeLine)[0];
//			    	int headY = GetHead(snakeLine)[1];
			    	
			    	//path.remove();
			    	
			    	int nextX = Integer.parseInt(deque.peek().toString().split(",")[0]);
			    	int nextY = Integer.parseInt(deque.peek().toString().split(",")[1]);
			    	
//			    	System.err.println("headX = " + headX + " nodeX = " + nextX);
//			    	System.err.println("headY = " + headY + " nodeY = " + nextY);
			    	
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
			 
				public Cell[][] Board(Cell[][] play_area) {
					
					Cell[][] board = new Cell[50][50];
					
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
							System.err.print(" " + board[i][j]);
						}
						System.err.println();
					}
					
					return board;
					
				}
				
//		    	System.err.println("headX = " + headX + " " + "nodeX = " + nextX);
//		    	System.err.println("headY = " + headY + " " + "nodeY = " + nextY);
//		public static void main(String[] args) {
			
//			Scanner input = new Scanner(System.in);
//			
//			int num_snakes = input.nextInt();
//			int width = input.nextInt();
//			int height = input.nextInt();
//			int one = input.nextInt();
//			
//			int[][] play_area = new int[width][height];
//			ArrayList<String> obstacles = new ArrayList<String>();
//			
//			for (int i = 0; i < width; i++) {
//				for (int j = 0; j < height; j++) {
//					play_area[i][j] = 0;
//				}
//			}
//			
//			int[] start = {31,21};
//			int[] end = {25,21};
//			
//			for (int obstacle = 0; obstacle < 4; obstacle++) {
//	            String obs = input.nextLine();
//	            obstacles.addAll(getObstacles(obs));
//	        }
//			
//			for (int i = 0; i <= num_snakes; i++) {
//				String snake_input = input.nextLine();
//				drawSnake(getBody(snake_input), i, play_area);
//			}
//			
//			for (int i = 0; i < 50; i++) {
//				for (int j = 0; j < 50; j++) {
//					if (play_area[i][j] != 0 && i != start[0] && j != start[1]) {
//						obstacles.add(i + "," + j);
//					}
//				}
//			}
//			
//			System.out.println(obstacles);
//			
//			Astar a_star = new Astar(start[0], start[1], end[0], end[1], obstacles);
//			
//			System.out.println(obstacles);
//			
//			a_star.process();
//			a_star.astar();
//			System.out.println(a_star.path);
////			System.out.println(obs);
//			input.close();
//		}
}
//4 50 50 1
//30,21 29,21 28,21 27,21 26,21
//16,32 16,33 16,34 16,35 16,36
//47,26 46,26 45,26 44,26 43,26
//alive 26 2 10,12 15,12 15,7 5,7 5,2
//dead 6 6 14,13 19,13
//alive 2 1 12,13 12,14
//alive 17 1 31,14 21,14 15,14 15,13
