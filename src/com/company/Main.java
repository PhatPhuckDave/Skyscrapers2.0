package com.company;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

// TODO: Maybe try deep copy using toArray

public class Main {
	static ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1000);

	static public void stuffsDone(PuzzleSolver currentSolver) {
		System.out.println(currentSolver.board.printBoard());
		System.exit(0);
	}

	public static void main(String[] args) {
//		executor.execute(new PuzzleSolver(
//				new Board(new PuzzleGenerator(0, "https://www.puzzle-skyscrapers.com/?e=MDo0OTMsMjUx"))));
//		executor.execute(new PuzzleSolver(
//				new Board(new PuzzleGenerator(0, "https://www.puzzle-skyscrapers.com/?e=MDoxODEsMTAz"))));
//		executor.execute(new PuzzleSolver(new Board(new PuzzleGenerator(7))));
		executor.execute(new PuzzleSolver(new Board(new PuzzleGenerator(true))));
	}
}
