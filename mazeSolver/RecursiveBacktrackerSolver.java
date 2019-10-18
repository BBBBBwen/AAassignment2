package mazeSolver;

import maze.*;
import java.util.*;

/**
 * Implements the recursive backtracking maze solving algorithm.
 */
public class RecursiveBacktrackerSolver implements MazeSolver {
	private boolean[][] visited;
	private Integer[] array = new Integer[] { Maze.EAST, Maze.NORTH, Maze.WEST, Maze.SOUTH };
	private List<Integer> direction = Arrays.asList(array);
	private boolean solved = false;
	private int cellsExplored = 1;

	@Override
	public void solveMaze(Maze maze) {
		visited = new boolean[maze.sizeR][maze.sizeC];
		Collections.shuffle(direction);
		Stack<Integer> step = new Stack<>();
		Cell           currentCell = maze.map[maze.entrance.r][maze.entrance.c];
		visited[maze.entrance.r][maze.entrance.c] = true;

		while(!isOver(currentCell, maze)) {
			boolean deadEnd = false;

			while(!deadEnd) {
				Collections.shuffle(direction);
				deadEnd = true;
				boolean hasTunnel = currentCell.tunnelTo != null;
				if(hasTunnel && !visited[currentCell.tunnelTo.r][currentCell.tunnelTo.c]) {
					currentCell = currentCell.tunnelTo;
					step.push(-1);
					deadEnd = false;
					++cellsExplored;
				} else {
					for(int k = 0; k < 4; ++k) {
						Cell neighbor = currentCell.neigh[direction.get(k)];
						if(isVisitable(currentCell, neighbor, direction.get(k))) {
							currentCell = neighbor;
							step.push(direction.get(k));
							deadEnd = false;
							++cellsExplored;
							break;
						}
					}
				}

				maze.drawFtPrt(currentCell);
				visited[currentCell.r][currentCell.c] = true;
				if(isOver(currentCell, maze)) deadEnd = true;
			}

			if(!isOver(currentCell, maze)) {
				currentCell = backTracking(currentCell, maze, step);
			}
		}
	} // end of solveMaze()

	private Cell backTracking(Cell deadEnd, Maze maze, Stack<Integer> stack) {
		boolean stop = false;
		int     r = deadEnd.r;
		int     c = deadEnd.c;

		while(!stop && !stack.isEmpty()) {
			if(moveable(r, c, maze))
				stop = true;
			else {
				int dir = stack.pop();
				r = dir == -1 ? maze.map[r][c].tunnelTo.r : Maze.deltaR[Maze.oppoDir[dir]] + r;
				c = dir == -1 ? maze.map[r][c].tunnelTo.c :  Maze.deltaC[Maze.oppoDir[dir]] + c;
			}
		}
		return maze.map[r][c];
	}

	private boolean moveable(int r, int c, Maze maze) {
		for(int k = 0; k < 4; ++k) {
			Cell cell = maze.map[r][c].neigh[direction.get(k)];
			if(cell != null && !maze.map[r][c].wall[direction.get(k)].present && !visited[cell.r][cell.c]) {
				return true;
			}
		}
		return false;
	}

	private boolean isVisitable(Cell curr, Cell des, int dir) {
		return des != null && !curr.wall[dir].present && !visited[des.r][des.c];
	}

	private boolean isOver(Cell current, Maze maze) {
		return solved = maze.map[current.r][current.c] == maze.map[maze.exit.r][maze.exit.c];
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
