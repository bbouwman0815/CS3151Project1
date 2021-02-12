package edu.westga.cs3151.the8puzzle.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

// TODO: Auto-generated Javadoc
/**
 * The Class PuzzleSolver.
 */
public class PuzzleSolver {

	/** The board. */
	private Board board;
	
	/** The solved board. */
	private Board solvedBoardTemplate;
	
	/** The correct moves. */
	private Queue<Move> correctMoves;

	/**
	 * Instantiates a new puzzle solver.
	 *
	 * @param defaultBoard the default board
	 */
	public PuzzleSolver(Board defaultBoard) {
		this.board = defaultBoard;
		this.solvedBoardTemplate = new Board();
		this.correctMoves = new LinkedList<Move>();
		this.solvedBoardTemplate.setTile(new Position(0, 0), 1);
		this.solvedBoardTemplate.setTile(new Position(0, 1), 2);
		this.solvedBoardTemplate.setTile(new Position(0, 2), 3);
		this.solvedBoardTemplate.setTile(new Position(1, 0), 4);
		this.solvedBoardTemplate.setTile(new Position(1, 1), 5);
		this.solvedBoardTemplate.setTile(new Position(1, 2), 6);
		this.solvedBoardTemplate.setTile(new Position(2, 0), 7);
		this.solvedBoardTemplate.setTile(new Position(2, 1), 8);
		this.solvedBoardTemplate.setTile(new Position(2, 2), 0);
	}

	/**
	 * Gets the correct moves.
	 *
	 * @return the correct moves
	 */
	public Queue<Move> getCorrectMoves() {
		return this.correctMoves;
	}

	/**
	 * Solve.
	 */
	public void solve() {
		//TODO
	}

	/**
	 * The Help function
	 * 
	 * Helps the players by solving the next puzzle move.
	 *
	 * @param tileNeededToSolveFor the tile needed to solve for
	 */
	public void help(int tileNeededToSolveFor) {
		Node start = new Node(this.board, null);
		Queue<Node> nodes = new LinkedList<Node>();
		Stack<Move> backwardsList = new Stack<Move>();
		ArrayList<Node> usedNodes = new ArrayList<Node>();
		nodes.add(start);

		while (!nodes.isEmpty()) {
			Node move = nodes.poll();
			usedNodes.add(move);
			
			for (Board current : move.getNeighbors()) {
				Node tryNode = new Node(current, move);
				if (!usedNodes.contains(tryNode)) {
					nodes.add(tryNode);
				}
			}

			if (move.value.getNumberSortedTiles() >= tileNeededToSolveFor) {
				while (move.previous != null) {
					backwardsList.add(new Move(move.value.getEmptyTilePosition(),
							move.previous.getValue().getEmptyTilePosition()));
					move = move.previous;
				}
				this.reverseQueue(backwardsList);
				return;
			}
		}
	}
	
	/**
	 * Reverse queue.
	 *
	 * @param moves the moves to be reversed
	 */
	private void reverseQueue(Stack<Move> moves) {
		while (!moves.isEmpty()) {
			this.correctMoves.add(moves.pop());
		}
	}

	/**
	 * The Class Node.
	 */
	private class Node {

		/** The previous. */
		private Node previous;
		
		/** The value. */
		private Board value;

		/**
		 * Instantiates a new node.
		 *
		 * @param board the board
		 * @param value the value
		 */
		Node(Board board, Node value) {
			this.previous = value;
			this.value = board;
		}

		/**
		 * Gets the value.
		 *
		 * @return the value
		 */
		public Board getValue() {
			return this.value;
		}

		/**
		 * Gets the neighbors.
		 *
		 * @return the neighbors
		 */
		public Collection<Board> getNeighbors() {
			Collection<Position> emptyTileNeighbors = this.value.getEmptyTilePosition().getNeighbors();
			ArrayList<Move> moves = new ArrayList<Move>();
			for (Position currentPosition : emptyTileNeighbors) {
				moves.add(new Move(currentPosition, this.value.getEmptyTilePosition()));
			}
			
			Collection<Board> boards = new ArrayList<Board>();
			for (Move currentMove : moves) {
				Board newBoard = new Board(this.value);
				newBoard.moveTile(currentMove);
				boards.add(newBoard);
			}
			
			return boards;
		}

		/**
		 * Equals.
		 *
		 * @param obj the obj
		 * @return true, if successful
		 */
		@Override
		public boolean equals(Object obj) {
			if (Node.this.value.hashCode() == ((Node) obj).getValue().hashCode()) {
				return true;
			}
			return false;
		}

		/**
		 * Hash code.
		 *
		 * @return the int
		 */
		@Override
		public int hashCode() {
			return this.value.hashCode();
		}

	}
}
