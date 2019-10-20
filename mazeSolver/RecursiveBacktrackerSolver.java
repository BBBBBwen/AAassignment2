package mazeSolver;

import maze.*;
import java.util.*;

/**
 * Implements the recursive backtracking maze solving algorithm.
 */
public class RecursiveBacktrackerSolver implements MazeSolver {
	private boolean[][] visited;
	private List<Integer> direction = Arrays.asList(Maze.EAST, Maze.NORTHEAST, Maze.NORTH, Maze.WEST, Maze.SOUTHWEST, Maze.SOUTH);
	private boolean solved = false;
	private int cellsExplored = 1;

	@Override
	public void solveMaze(Maze maze) {
		visited = new boolean[maze.sizeR][maze.sizeC];
		Collections.shuffle(direction);
		move(maze.entrance, maze);
	} // end of solveMaze()

	private void move(Cell cell, Maze maze) {
		if(cell == maze.exit) {
			solved = true;
			visited[cell.r][cell.c] = true;
			maze.drawFtPrt(cell);
		} else if(!reachEnd(cell, maze)) {
			++cellsExplored;
			visited[cell.r][cell.c] = true;
			maze.drawFtPrt(cell);
			if(isTunnel(cell))
				move(cell.tunnelTo, maze);
			for(int i = 0; i < Maze.NUM_DIR; ++i)
				if(isValid(cell, direction.get(i)))
					move(cell.neigh[direction.get(i)], maze);
		}
	}

	private boolean isValid(Cell cell, int dir) {
		Cell neigh = cell.neigh[dir];
		return neigh != null && !visited[neigh.r][neigh.c] && !cell.wall[dir].present;
	}

	private boolean isTunnel(Cell cell) {
		Cell tunnel = cell.tunnelTo;
		return tunnel != null && !visited[tunnel.r][tunnel.c];
	}

	private boolean reachEnd(Cell cell, Maze maze) {
		Cell tunnel = cell.tunnelTo;
		if(tunnel != null && visited[tunnel.r][tunnel.c])
			return false;
		for(int i = 0; i < Maze.NUM_DIR; ++i) {
			Cell neigh = cell.neigh[i];
			if(neigh != null && !visited[neigh.r][neigh.c]) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean isSolved() {
		return solved;
	} // end if isSolved()


	@Override
	public int cellsExplored() {
		return cellsExplored;
	} // end of cellsExplored()
}         // end of class RecursiveBackTrackerSolver
