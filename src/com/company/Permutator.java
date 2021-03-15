package com.company;

import java.util.ArrayList;
import java.util.Collections;

public class Permutator {
	ArrayList<ArrayList<Integer>> set          = new ArrayList<>();
	ArrayList<ArrayList<Integer>> combinations = new ArrayList<>();

	public Permutator() {}

	public void cleanUpRow(int clueIndex, Board board) {
		ArrayList<ArrayList<Integer>> currentRow          = new ArrayList<>();
		int                           clue                = PuzzleGenerator.task.get(clueIndex);
		int                           mirrorClue          = PuzzleGenerator.task.get(PuzzleGenerator.boardSize + clueIndex);
		ArrayList<String>             clueRow             = PuzzleGenerator.taskRows.get(clueIndex);
		ArrayList<ArrayList<Integer>> illegalCombinations = new ArrayList<>();
		if (clue + mirrorClue == 0) return;
		for (String pos : clueRow) {
			currentRow.add(board.field.get(pos));
		}
		set = currentRow;
		daiPlease(new ArrayList<>(), 0);

		for (ArrayList<Integer> combination : combinations) {
			if (clue > 0) if (!isValid(clue, combination)) illegalCombinations.add(combination);
			if (mirrorClue > 0) {
				ArrayList<Integer> reversedCombination = new ArrayList<>(combination);
				Collections.reverse(reversedCombination);
				if (!isValid(mirrorClue, reversedCombination)) illegalCombinations.add(combination);
			}
		}
		combinations.removeAll(illegalCombinations);

		for (int i = 0; i < PuzzleGenerator.boardSize; i++) {
			ArrayList<Integer> tempList = new ArrayList<>();
			for (ArrayList<Integer> combination : combinations) {
				try {if (!tempList.contains(combination.get(i))) tempList.add(combination.get(i));}
				catch (Exception ignored) {}
			}
//			for (Integer j : board.field.get(clueRow.get(i))) if (!tempList.contains(j)) board.eliminate(clueRow.get(i), j);
			if (board.field.get(clueRow.get(i)).size() > 1) board.field.get(clueRow.get(i)).removeIf(e -> !tempList.contains(e));
		}

		set          = new ArrayList<>();
		combinations = new ArrayList<>();
	}

	private boolean isValid(int clue, ArrayList<Integer> combination) {
		if (clue == 0)
			return false;
		int top     = 0;
		int visible = 0;
		for (Integer i : combination) {
			if (i > top) {
				visible += 1;
				top = i;
				if (visible > clue)
					return false;
				if (top == PuzzleGenerator.boardSize) return visible == clue;
			}
		}
		return visible == clue;
	}

	private void daiPlease(ArrayList<Integer> arr, int i) {
		for (Integer j : set.get(i)) {
			ArrayList<Integer> arrCopy = new ArrayList<>(arr);
			if (arrCopy.contains(j)) continue;
			arrCopy.add(j);
			if (i == set.size() - 1) combinations.add(arrCopy);
			else daiPlease(arrCopy, i + 1);
		}
	}
}
