package com.company;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

// TODO: Maybe try deep copy using toArray

public class Main {
	static ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(12);
	static long startingTime;
	static int boards = 1;

	public static void stuffsDone(PuzzleSolver currentSolver) {
		if (currentSolver.board.isValid() && !currentSolver.board.isDead()) {
			System.out.println((System.nanoTime() - startingTime) / 1e9);
			System.out.println(boards);
			System.out.println(currentSolver.board.printBoard());
			System.out.println("yaay!");
			currentSolver.board.isValid();
//			executor.shutdownNow();
			try { executor.awaitTermination(30, TimeUnit.SECONDS); }
			catch (Exception ignored) {}
		} else {
			if (!currentSolver.board.isDead()) {
				String guess = currentSolver.board.bestGuess();
				if (!guess.equals(""))
					for (int i = 0; i < currentSolver.board.field.get(guess).size(); i++) {
						executor.execute(new Thread(new PuzzleSolver(new Board(currentSolver.board), guess, currentSolver.board.field.get(guess).get(i))));
						++boards;
					}
			}
		}
	}

	public static void main(String[] args) {
		// TODO: See why the below places a 1 in 1, 3 without eliminating it from any others
//		executor.execute(new PuzzleSolver(new Board(new PuzzleGenerator(7, "https://www.puzzle-skyscrapers.com/?e=NzoyLDM2OSwzOTY="))));
		startingTime = System.nanoTime();
//		executor.execute(new PuzzleSolver(new Board(new PuzzleGenerator(8, "https://www.puzzle-skyscrapers.com/?e=ODo3LDMwOSw1MTY="))));
		executor.execute(new PuzzleSolver(new Board(new PuzzleGenerator(7, "https://www.puzzle-skyscrapers.com/?e=Nzo1LDYyNyw4MDA="))));
	}
}
