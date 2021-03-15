package com.company;

import java.util.ArrayList;

public class Benchmark {
	boolean         isRunning           = false;
	int             iterationsRemaining = 0;
	Board           startingBoard;
	long            lastStart;
	ArrayList<Long> benchmarkTimes;

	public Benchmark() {}

	public void start(Board board, int iterationsRemaining) {
		this.iterationsRemaining = iterationsRemaining;
		benchmarkTimes           = new ArrayList<Long>();
		isRunning                = true;
		startingBoard            = board;
		startNewBoard();
	}

	protected void startNewBoard() {
		Main.executor.execute(new PuzzleSolver(new Board(startingBoard)));
		iterationsRemaining--;
		lastStart = System.nanoTime();
	}

	protected double getAverage() {
		double sum = 0;
		for (double time : benchmarkTimes) sum += time;
		return sum / benchmarkTimes.size();
	}
}
