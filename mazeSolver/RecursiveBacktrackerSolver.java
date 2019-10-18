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
	private int cellsExplored = 0;

	@Override
	public void solveMaze(Maze maze) {
		visited = new boolean[maze.sizeR][maze.sizeC];
		Collections.shuffle(direction);
		Stack<Integer> step = new Stack<>();
		int            r = maze.entrance.r;
		int            c = maze.entrance.c;

		while(!isOver(maze)) {
			boolean deadEnd = false;
			do {
				Collections.shuffle(direction);
				visited[r][c] = true;
				deadEnd = true;

				for(int k = 0; k < 4; ++k) {
					int row = r + Maze.deltaR[direction.get(k)];
					int col = c + Maze.deltaC[direction.get(k)];
					if(checkBound(row, col, maze)) {
						if(!visited[row][col]) {
							if(!maze.map[r][c].wall[direction.get(k)].present) {
								r += Maze.deltaR[direction.get(k)];
								c += Maze.deltaC[direction.get(k)];
								System.out.println("go to " + r + " : " + c);
								step.push(direction.get(k));
								k = 4;
								deadEnd = false;
								++cellsExplored;
							}
						}
					}
				}
			} while(!deadEnd);

			if(!isOver(maze)) {
				Cell curCel = backTracking(maze.map[r][c], maze, step);
				r = curCel.r;
				c = curCel.c;
			}
		}
	} // end of solveMaze()

	private Cell backTracking(Cell deadEnd, Maze maze, Stack<Integer> stack) {
		boolean stop = false;
		int     r = deadEnd.r;
		int     c = deadEnd.c;

		while(!stop && !stack.isEmpty()) {
			System.out.println("backTracking");

			if(moveable(r, c, maze))
				stop = true;
			else {
				int dir = stack.pop();
				r += Maze.deltaR[Maze.oppoDir[dir]];
				c += Maze.deltaC[Maze.oppoDir[dir]];
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
		return !curr.wall[dir].present && !visited[des.r][des.c];
	}

	private boolean traceable(Cell curr, Cell des, int dir) {
		return !curr.wall[dir].present && visited[des.r][des.c];
	}

	private boolean isOver(Maze maze) {
		return solved = visited[maze.exit.r][maze.exit.c];
	}

	private boolean checkBound(int r, int c, Maze maze) {
		return r >= 0 && r < maze.sizeR && c >= 0 && c < maze.sizeC;
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
