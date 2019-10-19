package mazeGenerator;

import maze.*;
import java.util.Random;
import java.util.*;

public class HuntAndKillGenerator implements MazeGenerator {
	private Random rand = new Random();
	private Integer[] array = new Integer[] { Maze.EAST, Maze.NORTH, Maze.WEST, Maze.SOUTH };
	private List<Integer> direction = Arrays.asList(array);

	@Override
	public void generateMaze(Maze maze) {
		mazeGenerator(maze);
	} // end of generateMaze()

	private void mazeGenerator(Maze maze) {
		boolean[][] visited = new boolean[maze.sizeR][maze.sizeC];
		Cell        currentCell = maze.map[rand.nextInt(maze.sizeR)][rand.nextInt(maze.sizeC)];
		while(currentCell != null) {
			kill(maze, visited, currentCell);
			currentCell = hunt(maze, visited, currentCell);
		}
	}

	//find unvisited cell
	private Cell hunt(Maze maze, boolean[][] visited, Cell currentCell) {
		for(int i = 0; i < maze.sizeR; ++i) {
			for(int j = 0; j < maze.sizeC; ++j) {
				if(!visited[i][j]) {
					Cell neighbor = maze.map[i][j].tunnelTo;
					if(neighbor != null && visited[neighbor.r][neighbor.c]) {
						currentCell = maze.map[i][j];
						return currentCell;
					} else {
						for(int k = 0; k < 4; ++k) {
							neighbor = maze.map[i][j].neigh[direction.get(k)];
							if(neighbor != null && visited[neighbor.r][neighbor.c]) {
								currentCell = maze.map[i][j];
								carve(maze.map[i][j], direction.get(k));
								return currentCell;
							}
						}
					}
				}
			}
		}
		return null;
	}

	//start at an unvisited cell and start carving
	private void kill(Maze maze, boolean[][] visited, Cell currentCell) {
		boolean kill = true;
		do {
			Collections.shuffle(direction);
			kill = false;
			visited[currentCell.r][currentCell.c] = true;
			Cell neighbor = currentCell.tunnelTo;
			if(neighbor != null && !visited[neighbor.r][neighbor.c]) {
				currentCell = neighbor;
				kill = true;
			} else {
				for(int k = 0; k < 4; ++k) {
					neighbor = currentCell.neigh[direction.get(k)];
					if(neighbor != null && !visited[neighbor.r][neighbor.c]) {
						carve(currentCell, direction.get(k));
						currentCell = neighbor;
						kill = true;
						break;
					}
				}
			}
		} while(kill);
	}

	//remove wall between cells
	private void carve(Cell cell, int direction) {
		cell.wall[direction].present = false;
		cell.neigh[direction].wall[Maze.oppoDir[direction]].present = false;
	}
} // end of class HuntAndKillGenerator
