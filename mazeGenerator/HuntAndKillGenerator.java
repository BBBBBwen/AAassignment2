package mazeGenerator;

import maze.*;
import java.util.Random;
import java.util.*;

public class HuntAndKillGenerator implements MazeGenerator {
	private Random rand = new Random();
	private int randC;
	private int randR;
	private Integer[] array = new Integer[] { Maze.EAST, Maze.NORTH, Maze.WEST, Maze.SOUTH };
	private List<Integer> direction = Arrays.asList(array);

	@Override
	public void generateMaze(Maze maze) {
		randR = rand.nextInt(maze.sizeR);
		randC = rand.nextInt(maze.sizeC);
		mazeGenerator(maze);
	} // end of generateMaze()

	private void mazeGenerator(Maze maze) {
		boolean[][] visited = new boolean[maze.sizeR][maze.sizeC];
		int         r = randR;
		int         c = randC;
		Cell        currentCell = maze.map[r][c];
		while(currentCell != null) {
			kill(maze, visited, currentCell);
			currentCell = hunt(maze, visited, currentCell);
		}
	}

	//find unvisited cell
	private Cell hunt(Maze maze, boolean[][] visited, Cell currentCell) {
		boolean hunt = false;
		boolean found = false;
		for(int i = 0; i < maze.sizeR; ++i) {
			for(int j = 0; j < maze.sizeC; ++j) {
				if(!visited[i][j]) {
					hunt = true;
					if(maze.map[i][j].tunnelTo != null) {
						int row = maze.map[i][j].tunnelTo.r;
						int col = maze.map[i][j].tunnelTo.c;
						if(checkBound(row, col, maze)) {
							if(visited[row][col]) {
								currentCell = maze.map[i][j];
								i = maze.sizeR;
								j = maze.sizeC;
								found = true;
							}
						}
					}
					if(!found) {
						for(int k = 0; k < 4; ++k) {
							int row = i + Maze.deltaR[direction.get(k)];
							int col = j + Maze.deltaC[direction.get(k)];
							if(checkBound(row, col, maze)) {
								if(visited[row][col]) {
									currentCell = maze.map[i][j];
									carve(maze.map[i][j], direction.get(k));
									i = maze.sizeR;
									j = maze.sizeC;
								}
							}
						}
					}
				}
			}
		}
		if(hunt) return currentCell;
		return null;
	}

	//start at an unvisited cell and start carving
	private void kill(Maze maze, boolean[][] visited, Cell currentCell) {
		boolean kill = true;
		int     r = currentCell.r;
		int     c = currentCell.c;
		do {
			Collections.shuffle(direction);
			kill = false;
			boolean found = false;
			visited[r][c] = true;
			if(maze.map[r][c].tunnelTo != null) {
				int row = maze.map[r][c].tunnelTo.r;
				int col = maze.map[r][c].tunnelTo.c;
				if(checkBound(row, col, maze)) {
					if(!visited[row][col]) {
						r = row;
						c = col;
						kill = true;
						found = true;
					}
				}
			}
			if(!found) {
				for(int k = 0; k < 4; ++k) {
					int row = r + Maze.deltaR[direction.get(k)];
					int col = c + Maze.deltaC[direction.get(k)];
					if(checkBound(row, col, maze)) {
						if(!visited[row][col]) {
							carve(maze.map[r][c], direction.get(k));
							r += Maze.deltaR[direction.get(k)];
							c += Maze.deltaC[direction.get(k)];
							kill = true;
							k = 4;
						}
					}
				}
			}
		} while(kill);
	}

	//check if cell is in the maze
	private boolean checkBound(int r, int c, Maze maze) {
		return r >= 0 && r < maze.sizeR && c >= 0 && c < maze.sizeC;
	}

	//remove wall between cells
	private void carve(Cell cell, int direction) {
		cell.wall[direction].present = false;
		cell.neigh[direction].wall[Maze.oppoDir[direction]].present = false;
	}
} // end of class HuntAndKillGenerator
