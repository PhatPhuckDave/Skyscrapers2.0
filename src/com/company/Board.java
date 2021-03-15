package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.company.PuzzleGenerator.*;

public class Board {
	Map<String, ArrayList<Integer>> field         = new HashMap<>();
	Map<String, Boolean>            assignedField = new HashMap<>();

	public Board(PuzzleGenerator startingPuzzle) {
		importField(startingPuzzle.field);
		for (String pos : boardMembers) assignedField.put(pos, false);
	}

	public Board(Board input) {
		importField(input.field);
		for (String pos : boardMembers) assignedField.put(pos, false);
	}

	public void importField(Map<String, ArrayList<Integer>> input) {
		for (Map.Entry<String, ArrayList<Integer>> entry : input.entrySet()) {
			field.put(entry.getKey(), new ArrayList<>(entry.getValue()));
		}
	}

	protected boolean checkRow(int task, ArrayList<String> cRow) {
		if (task == 0) return false;
		int top     = 0;
		int visible = 0;
		for (String pos : cRow) {
			if (field.get(pos).size() == 1) {
				int c = field.get(pos).get(0);
				if (c > top) {
					visible += 1;
					top = c;
					if (visible > task)
						return true;
				}
			} else
				return true;
		}
		return visible != task;
	}

	protected boolean isValid() {
		for (int i = 0; i < boardSize; i++) if (checkRow(task.get(i), taskRows.get(i))) return false;
		for (String pos : boardMembers)
			if (field.get(pos).size() > 1)
				return false;
		return true;
	}

	protected boolean isDead() {
		for (String pos : boardMembers)
			if (field.get(pos).size() == 0)
				return true;
		return false;
	}

	protected String bestGuess() {
		String pos = "";
		int    min = boardSize;
		for (String mem : boardMembers) {
			if (field.get(mem).size() > 1 && field.get(mem).size() < min) {
				min = field.get(mem).size();
				pos = mem;
			}
		}
		return pos;
	}

	protected void assign(String pos, int i) {
		if (!assignedField.get(pos)) {
			field.get(pos).removeIf(e -> !e.equals(i));
			assignedField.put(pos, true);
			propagate(pos, i);
		}
	}

	protected void eliminate(String pos, int i) {
		field.get(pos).removeIf(e -> e.equals(i));
		if (field.get(pos).size() == 1 && !assignedField.get(pos))
			assign(pos, field.get(pos).get(0));
	}

	protected void propagate(String pos, int i) {
		for (String peer : peers.get(pos)) {
			eliminate(peer, i);
		}
	}

	protected String printBoard() {
		StringBuilder output      = new StringBuilder();
		StringBuilder outputSmall = new StringBuilder();
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				outputSmall.delete(0, outputSmall.length());
				for (Integer I : field.get((char) (65 + i) + Integer.toString(j))) {
					outputSmall.append(I);
				}
				output.append(String.format("%8s  ", outputSmall));
			}
			output.append("\n");
		}
		return output.toString();
	}
}
