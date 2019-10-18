package mazeGenerator;

import maze.*;
import java.util.*;

public class KruskalGenerator implements MazeGenerator {
	ArrayList<Edge> edges = new ArrayList<>();
	private int[][] matrix;
	private Integer[] direction = new Integer[] { 0, 2, 3, 5 };

	@Override
	public void generateMaze(Maze maze) {
		matrix = new int[maze.sizeR][maze.sizeC];
		int count = 0;
		for(int i = 0; i < maze.sizeR; ++i) {
			for(int j = 0; j < maze.sizeC; ++j) {
				matrix[i][j] = ++count;
				if(maze.map[i][j].tunnelTo != null) {
					int tunnelRow = maze.map[i][j].tunnelTo.r;
					int tunnelCol = maze.map[i][j].tunnelTo.c;
					matrix[i][j] = matrix[tunnelRow][tunnelCol] = count;
				}
			}
		}
		mazeGenerator(maze);
	} // end of generateMaze()

	private void printMatrix() {
		for(int i = 0; i < matrix.length; ++i) {
			for(int j = 0; j < matrix[i].length; ++j) {
				System.out.print(matrix[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
		System.out.println();
	}

	private void mazeGenerator(Maze maze) {
		makeSet(maze);
		Collections.shuffle(edges);
		while(!edges.isEmpty()) {
			Edge temp = edges.remove(0);
			Cell neighbor = temp.dir == -1 ? maze.map[temp.row][temp.col].tunnelTo : maze.map[temp.row][temp.col].neigh[temp.dir];
			if(matrix[temp.row][temp.col] != matrix[neighbor.r][neighbor.c]) {
				if(temp.dir != -1) carve(maze.map[temp.row][temp.col], temp.dir);
				changeParent(maze, temp, neighbor.r, neighbor.c);
			}
		}
	}

	private void addEgde(int row, int col, int direction) {
		Edge edge = new Edge(row, col, direction);
		edges.add(edge);
	}

	private void makeSet(Maze maze) {
		for(int i = 0; i < matrix.length; ++i) {
			for(int j = 0; j < matrix[i].length; ++j) {
				for(int d : direction) {
					if(maze.map[i][j].neigh[d] != null) {
						addEgde(i, j, d);
					}
				}
			}
		}
	}

	private void changeParent(Maze maze, Edge edge, int neighbourRow, int neighbourCol) {
		int current = matrix[edge.row][edge.col];
		int neighbor = matrix[neighbourRow][neighbourCol];
		for(int i = 0; i < matrix.length; ++i)
			for(int j = 0; j < matrix[i].length; ++j)
				if(matrix[i][j] == neighbor)
					matrix[i][j] = current;
	}

	private void carve(Cell cell, int direction) {
		cell.wall[direction].present = false;
		cell.neigh[direction].wall[Maze.oppoDir[direction]].present = false;
	}
} // end of class KruskalGenerator

class Edge {
	int row, col, dir;
	public Edge(int row, int col, int dir) {
		this.row = row;
		this.col = col;
		this.dir = dir;
	}
}
