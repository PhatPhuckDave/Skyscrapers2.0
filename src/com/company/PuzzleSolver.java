package com.company;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.company.Main.stuffsDone;

public class PuzzleSolver implements Runnable {
	Board board;

	public PuzzleSolver(Board board) {
		this.board = board;
	}

	private void checkThings() {
		for (int i = 0; i < PuzzleGenerator.boardSize; i++) {
			String   pos                = (char) (65 + i) + Integer.toString(i);
			String[] favorablePositions = new String[PuzzleGenerator.boardSize];
			int[]    combinations       = new int[PuzzleGenerator.boardSize];

			if (board.field.get(pos).size() > 1) {
				for (Integer I : board.field.get(pos)) {
					++combinations[I - 1];
					favorablePositions[I - 1] = pos;
				}
			}

			for (String peer : PuzzleGenerator.peers.get(pos)) {
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

	private void checkMoreThings() {
		// Go through half the puzzle and check every combination of every row against both sides of the task
		// Maybe use task and then add one size to it (top + size = bot and left + size = right)
		System.out.println(PuzzleGenerator.taskRows);
		for (int i = 0; i < PuzzleGenerator.boardSize; i++) {
//			System.out.println(PuzzleGenerator.taskRows.get(i));
//			System.out.println(PuzzleGenerator.taskRows.get(i + 2 * PuzzleGenerator.boardSize));
			// TODO: Generate all possible combinations of rows in taskRows(i) and the other one (per row)

			System.out.println(PuzzleGenerator.tasks);

//			Function<Integer, ArrayList<ArrayList<Integer>>> test = (combinations) -> {
//				ArrayList<ArrayList<Integer>> temp = new ArrayList<>();
//				ArrayList<Integer> temp2 = new ArrayList<>();
//				temp2.add(combinations);
//				temp.add(temp2);
//				return temp;
//			};
//
//			var test2 = test.apply(5);
//			System.out.println(test2);


			/*
			const memo = {};
			const makeAllUniqueSequences = (rowOrColumn, state) => {
			  const args = JSON.stringify(rowOrColumn);
			  if (memo[args]) return memo[args];

			  let results = [];

			  function recursiveHelper(arr, i) {
			    for (let value of rowOrColumn[i]) {
			      let copy = arr.slice();
			      if (arr.includes(value)) continue;
			      copy.push(value);

			      if (i === rowOrColumn.length - 1) {
			        results.push(copy);
			      } else {
			        recursiveHelper(copy, i + 1);
			      }
			    }
			  }
			  recursiveHelper([], 0);

			  state.totalCombinations += results.length;
			  memo[args] = results;
			  return results;
			};
			*/
		}
	}

	@Override
	public void run() {
		for (int i = 0; i < PuzzleGenerator.task.size(); i++) {
			if (PuzzleGenerator.task.get(i) == 1) {
				board.assign(PuzzleGenerator.taskRows.get(i).get(0), PuzzleGenerator.boardSize);
			} else if (PuzzleGenerator.task.get(i) == PuzzleGenerator.boardSize) {
				for (int j = 0; j < PuzzleGenerator.boardSize; j++) {
					board.assign(PuzzleGenerator.taskRows.get(i).get(j), j + 1);
				}
			} else {
				int x     = PuzzleGenerator.task.get(i);
				int count = 0;
				for (String mem : PuzzleGenerator.taskRows.get(i)) {
					for (int k = 0; k < x - 1 - count; k++)
					     board.eliminate(mem, PuzzleGenerator.boardSize - k);
					++count;
					if (count >= x - 1)
						break;
				}
			}
		}

		checkThings();
		checkMoreThings();
		stuffsDone(this);
	}
}
