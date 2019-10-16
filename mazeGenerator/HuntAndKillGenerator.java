package mazeGenerator;

import maze.Maze;
import java.util.Random;
public class HuntAndKillGenerator implements MazeGenerator {
	private Random rand = new Random();
	private int randC;
	private int randR;
	private final Integer[] array = new Integer[] { 0, 2, 3, 5 };
	private final List<Integer> direction = Arrays.asList(array);

	@Override
	public void generateMaze(Maze maze) {
		randR = rand.nextInt(maze.sizeR);
		randC = rand.nextInt(maze.sizeC);
		if(maze.type == 0) normalMazeGenerator(maze);
		else tunnelMazeGenerator(maze);
	} // end of generateMaze()

	private void normalMazeGenerator(Maze maze) {
		boolean[][] visited = new boolean[maze.sizeR][maze.sizeC];
		r = randR;
		c = randC;//random starting cell required
		boolean hunt = true;

		while(hunt) {
			boolean kill = true;
			do {
				int i = 0;
				Collections.shuffle(direction);
				visited[r][c] = true;
				while(visited[r + Maze.deltaR[direction[i]]][c + Maze.deltaC[direction[i]]]) {
					++i;
				}
				carve(maze.map[r][c], direction[i]);
				r += Maze.deltaR[direction[i]];
				c += Maze.deltaC[direction[i]];
				kill = false;

				for(int j = 0; j != 4; ++j) {
					if(visited[r + Maze.deltaR[direction[i]]][c + Maze.deltaC[direction[i]]] == false) {
						kill = true;
					}
				}
			} while(kill);

			hunt = false;
			for(int i = 0; i < maze.sizeR; ++i) {
				for(int j = 0; j < maze.sizeC; ++j) {
					if(visited[i][j] == false) {
						hunt = true;
						for(int k = 0; k != 4; ++k) {
							if(visited[i + Maze.deltaR[direction[k]]][j + Maze.deltaC[direction[k]]]) {
								r = i;
								c = j;
								carve(maze.map[i][j], direction[k]);
								i = maze.sizeR;
								j = maze.sizeC;
							}
						}
					}
				}
			}
		}
	}

	private void carve(Cell cell, int direction) {
		cell.wall[direction].present = false;
		cell.neigh[Maze.oppoDir[direction]].wall.present = false;
	}

	private void tunnelMazeGenerator(Maze maze) {
	}
} // end of class HuntAndKillGenerator
