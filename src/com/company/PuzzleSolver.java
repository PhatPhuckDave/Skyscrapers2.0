package com.company;

import static com.company.Main.stuffsDone;
import static com.company.PuzzleGenerator.*;

public class PuzzleSolver implements Runnable {
	Board board;
	Permutator permutator;

	public PuzzleSolver(Board board) {
		this.board = board;
		this.permutator = new Permutator();
	}

	public PuzzleSolver(Board board, String guessPos, int guess) {
		this.board = board;
		this.permutator = new Permutator();
		board.assign(guessPos, guess);
	}

	private void resolveRemainingUniques() {
		for (int i = 0; i < boardSize; i++) {
			String   pos                = (char) (65 + i) + Integer.toString(i);
			String[] favorablePositions = new String[boardSize];
			int[]    combinations       = new int[boardSize];

			if (board.field.get(pos).size() > 1) {
				for (Integer I : board.field.get(pos)) {
					++combinations[I - 1];
					favorablePositions[I - 1] = pos;
				}
			}

			for (String peer : peers.get(pos)) {
				if (board.field.get(peer).size() > 1) {
					for (Integer I : board.field.get(peer)) {
						if (combinations[I - 1] > 1)
							continue;
						++combinations[I - 1];
						favorablePositions[I - 1] = peer;
					}
				}
			}

			for (int j = 0; j < combinations.length; j++) {
				if (combinations[j] == 1) {
//					System.out.println("Assign " + (j + 1) + " to " + favorablePositions[j]);
					board.assign(favorablePositions[j], j + 1);
				}
			}
		}
	}

	@Override
	public void run() {
		if (board.isAlive()) {
			for (int i = 0; i < task.size(); i++) {
				if (task.get(i) == 1) {
					board.assign(taskRows.get(i).get(0), boardSize);
				} else if (task.get(i) == boardSize) {
					for (int j = 0; j < boardSize; j++) {
						board.assign(taskRows.get(i).get(j), j + 1);
					}
				} else {
					int x     = task.get(i);
					int count = 0;
					for (String mem : taskRows.get(i)) {
						for (int k = 0; k < x - 1 - count; k++)
						     board.eliminate(mem, boardSize - k);
						++count;
						if (count >= x - 1)
							break;
					}
				}
			}
			for (int i = 0; i < task.size() / 2; i++) {
				if (i < task.size() / 4) this.permutator.cleanUpRow(i, board);
				else this.permutator.cleanUpRow(boardSize + i, board);
			}
		}

		// resolve unique doesn't seem to work right
//		resolveRemainingUniques();
		stuffsDone(this);
	}
}
