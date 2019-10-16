package mazeGenerator;

import maze.*;
import java.util.Random;
import java.util.*;

public class HuntAndKillGenerator implements MazeGenerator {
	private Random rand = new Random();
	private int randC;
	private int randR;
	private Integer[] array = new Integer[] { 0, 2, 3, 5 };
	private List<Integer> direction = Arrays.asList(array);

	@Override
	public void generateMaze(Maze maze) {
		randR = rand.nextInt(maze.sizeR);
		randC = rand.nextInt(maze.sizeC);
		if(maze.type == 0) normalMazeGenerator(maze);
		else tunnelMazeGenerator(maze);
	} // end of generateMaze()

	private void normalMazeGenerator(Maze maze) {
		boolean[][] visited = new boolean[maze.sizeR][maze.sizeC];
		int         r = randR;
		int         c = randC;//random starting cell required
		boolean     hunt = true;

		while(hunt) {
			boolean kill = true;
			do {
				Collections.shuffle(direction);
				kill = false;
				visited[r][c] = true;
				int dir = 0;
				for(int k = 0; k < 4; ++k) {
					int row = r + Maze.deltaR[direction.get(k)];
					int col = c + Maze.deltaC[direction.get(k)];
					if(checkBound(row, col, maze)) {
						if(!visited[row][col]) {
							dir = direction.get(k);
							kill = true;
							k = 4;
						}
					}
				}
				if(kill) {
					carve(maze.map[r][c], dir);
					r += Maze.deltaR[dir];
					c += Maze.deltaC[dir];
				}
			} while(kill);

			hunt = false;
			for(int i = 0; i < maze.sizeR; ++i) {
				for(int j = 0; j < maze.sizeC; ++j) {
					if(!visited[i][j]) {
						hunt = true;
						for(int k = 0; k < 4; ++k) {
							int row = i + Maze.deltaR[direction.get(k)];
							int col = j + Maze.deltaC[direction.get(k)];
							if(checkBound(row, col, maze)) {
								if(visited[row][col]) {
									r = i;
									c = j;
									carve(maze.map[r][c], direction.get(k));
									i = maze.sizeR;
									j = maze.sizeC;
								}
							}
						}
					}
				}
			}
		}
	}

	private boolean checkBound(int r, int c, Maze maze) {
		return r >= 0 && r < maze.sizeR && c >= 0 && c < maze.sizeC;
	}

	private void carve(Cell cell, int direction) {
		if(cell.wall[direction] == null) {
			System.out.println("cell.wall[direction] : " + cell.r + " : " + cell.c + " : " + direction);
		} else if(cell.neigh[direction] == null) {
			System.out.println("cell.neigh[direction] : " + cell.r + " : " + cell.c + " : " + direction);
		} else if(cell.neigh[direction].wall[Maze.oppoDir[direction]] == null) {
			System.out.println("cell.neigh[direction].wall[Maze.oppoDir[direction]] : " + cell.r + " : " + cell.c + " : " + direction);
		}else{
			cell.wall[direction].present = false;
			cell.neigh[direction].wall[Maze.oppoDir[direction]].present = false;
		}
	}

	private void tunnelMazeGenerator(Maze maze) {
	}
} // end of class HuntAndKillGenerator
