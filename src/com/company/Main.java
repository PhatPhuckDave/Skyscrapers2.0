package com.company;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

// TODO: Maybe try deep copy using toArray

public class Main {
	static final Thread             mainThread = Thread.currentThread();
	static       ThreadPoolExecutor executor   = (ThreadPoolExecutor) Executors.newFixedThreadPool(6);
	static       int                numBoards  = 1;
	static       long               startingTime;
	static       Board              solvedBoard;
	static       Benchmark          benchmark  = new Benchmark();
	static       ArrayList<Board>   boards     = new ArrayList<>();

	public static void stuffsDone(PuzzleSolver currentSolver) {
		if (currentSolver.board.isValid() && currentSolver.board.isAlive()) {
			solvedBoard = currentSolver.board;
			mainThread.interrupt();
//			executor.shutdownNow();
		} else {
			if (currentSolver.board.isAlive()) {
				String guess = currentSolver.board.bestGuess();
				if (!guess.equals(""))
					for (int i = 0; i < currentSolver.board.field.get(guess).size(); i++) {
						Board newBoard = new Board(currentSolver.board);
						executor.execute(new Thread(new PuzzleSolver(newBoard, guess, currentSolver.board.field.get(guess).get(i))));
						++numBoards;
						boards.add(newBoard);
					}
			}
		}
	}

	public static void main(String[] args) throws IOException, SQLException {
		benchmark.start(new Board(new PuzzleGenerator(9)), 5000);
		// This breaks the program
//		executor.execute(new PuzzleSolver(new Board(new PuzzleGenerator(8))));

		do {
			try {Thread.sleep((long) 1e9);}
			catch (InterruptedException e) {
				if (!benchmark.isRunning) {
					System.out.printf("%.3fms\n", (System.nanoTime() - startingTime) / 1e6);
					System.out.println(numBoards);
					System.out.println(solvedBoard.printBoard());
					try {
						executor.shutdown();
						System.exit(0);
					}
					catch (Exception ignored) {}
				} else {
//					System.out.println(benchmark.iterationsRemaining);
					benchmark.benchmarkTimes.add((System.nanoTime() - benchmark.lastStart));
					if (benchmark.iterationsRemaining > 0)
						benchmark.startNewBoard();
					else {
						benchmark.benchmarkTimes.add((System.nanoTime() - benchmark.lastStart));
						DrawGraph.createAndShowGui(benchmark.benchmarkTimes, Collections.max(benchmark.benchmarkTimes));
//						Collections.sort(benchmark.benchmarkTimes);
						System.out.printf("Average: %.3fms Min: %.3fms Max: %.3fms%n", benchmark.getAverage() / 1e6,
						                  Collections.min(benchmark.benchmarkTimes) / 1e6,
						                  Collections.max(benchmark.benchmarkTimes) / 1e6);
						System.in.read();
						System.exit(0);
					}
				}
			}
		} while (benchmark.isRunning);
	}
}
