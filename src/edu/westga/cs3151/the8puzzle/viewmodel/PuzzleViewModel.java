package edu.westga.cs3151.the8puzzle.viewmodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import edu.westga.cs3151.the8puzzle.model.Board;
import edu.westga.cs3151.the8puzzle.model.Move;
import edu.westga.cs3151.the8puzzle.model.Position;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Duration;

/**
 * The Class PuzzleViewModel
 * 
 * @author CS3151
 * @version Spring 2021
 */
public class PuzzleViewModel {

	private final StringProperty[][] tileNumberProperty;
	private final BooleanProperty solvedBoardProperty;

	private Board board;

	private Stack<Move> undoMoveStack;

	/**
	 * Instantiates a new student info view model.
	 * 
	 * @pre none
	 * @post a new view model representing a random 8-puzzle
	 */
	public PuzzleViewModel() {
		this.board = new Board();
		this.undoMoveStack = new Stack<Move>();
		this.board.shuffle();
		this.tileNumberProperty = new StringProperty[Position.MAX_ROWS][Position.MAX_COLS];
		for (Position pos : Position.values()) {
			this.tileNumberProperty[pos.getRow()][pos.getCol()] = new SimpleStringProperty();
		}
		this.setTilesForView();
		this.solvedBoardProperty = new SimpleBooleanProperty(false);
	}

	/**
	 * Gets the tile number property.
	 *
	 * @pre none
	 * @post none
	 * @param row the row number of the tile
	 * @param col the column number of the tile
	 * @return the tile number property
	 */
	public StringProperty tileNumberProperty(int row, int col) {
		return this.tileNumberProperty[row][col];
	}

	/**
	 * Gets the solved board property.
	 *
	 * @pre none
	 * @post none
	 * @return the solved board property
	 */
	public BooleanProperty solvedBoardProperty() {
		return this.solvedBoardProperty;
	}

	/**
	 * Moves the tile selected by the user
	 * 
	 * @pre pos != null
	 * @post the tile at the specified position is moved if possible
	 * @param pos the position of the tile to be moved
	 */
	public void moveTile(Position pos) {
		Position destinationPos = this.board.getEmptyTilePosition();
		
		if (this.board.moveTile(pos, destinationPos)) {
			this.undoMoveStack.push(new Move(destinationPos, pos));
			
			this.setTilesForView();
			
			if (this.board.isSorted()) {
				this.solvedBoardProperty.set(true);
			}
		}
	}

	/**
	 * Undoes the most recent move
	 * 
	 * @pre none
	 * @post the most recent move of a puzzle board tile is undone
	 */
	public void undo() {
		if (this.undoMoveStack.size() > 0) {
			this.board.moveTile(this.undoMoveStack.pop());
			this.setTilesForView();
		}
	}

	/**
	 * Moves the next tile that is not in sequence to its correct position. First,
	 * the moves to place the next tile of the puzzle that is not in the correct
	 * order are determined without modifying the board. Then the moves are passed
	 * to the method call to traceMoves in a queue. Method traceMoves causes the
	 * moves in the queue to be executed on the board and to be displayed
	 * one-by-one.
	 * 
	 * @pre none
	 * @post the next tile that is moved to its correct position
	 */
	public void help() {
		int numberCorrectPositions = this.board.getNumberSortedTiles();
		int nextNumberToSolve = numberCorrectPositions + 1;

		Queue<Move> correctMoves = new LinkedList<Move>();
		Queue<Node> moves = new LinkedList<Node>();
		ArrayList<Node> visitedBoards = new ArrayList<Node>();

		Board sourceBoard = new Board(this.board);
		Node sourceNode = new Node(sourceBoard);
		visitedBoards.add(sourceNode);
		moves.add(sourceNode);
		
		ArrayList<Board> sourceBoardNeighbors = new ArrayList<Board>();
		
		for (Board currentBoard : sourceBoardNeighbors) {
			Node neighborNode = new Node(currentBoard);
			neighborNode.previous = sourceNode;
			moves.add(neighborNode);
			visitedBoards.add(neighborNode);
			
		}

		while (!moves.isEmpty()) {
			Node currentNode = moves.remove();

			ArrayList<Board> neighborBoards = (ArrayList<Board>) currentNode.getNodeNeighbors();

			for (Board currentBoard : neighborBoards) {

				Node neighborNode = new Node(currentBoard);
				neighborNode.previous = currentNode;

				if (!visitedBoards.contains(neighborNode)) {
					moves.add(neighborNode);
					System.out.println(neighborNode.hashCode());
				}	
				visitedBoards.add(currentNode);
			}
			
			if (currentNode.value.getNumberSortedTiles() == nextNumberToSolve) {
				this.board = currentNode.value;
				this.setTilesForView();
			}
		}
	}

	// Look into using a different queue so can just manipulate easier
	private Queue<Move> reverseMoves(Queue<Move> moves) {
		Stack<Move> reverseStack = new Stack<Move>();
		Queue<Move> reverseQueue = new LinkedList<Move>();
		while (!moves.isEmpty()) {
			reverseStack.add(moves.remove());
		}

		while (!reverseStack.isEmpty()) {
			reverseQueue.add(reverseStack.pop());
		}

		return reverseQueue;

	}

	/**
	 * Solves this 8-puzzle. First, the moves to solve the puzzle are determined
	 * without modifying the board. Then the moves are passed to the method call to
	 * traceMoves in a queue. Method traceMoves causes the moves in the queue to be
	 * executed on the board and to be displayed one-by-one.
	 * 
	 * @pre none
	 * @post all tiles of this board are in the correct position
	 */
	public void solve() {
		System.out.println("Replace me by instructions to solve the puzzle.");
	}

	/**
	 * Shuffles the tiles to generate a new 8-puzzle
	 * 
	 * @pre none
	 * @post the application is reset for a new random puzzle
	 */
	public void newPuzzle() {
		this.board.shuffle();
		this.undoMoveStack.clear();

		this.setTilesForView();
	}

	/**
	 * One-by-one removes the Move objects from the specified queue and applies each
	 * move on this board.
	 * 
	 * @param moves a sequence of valid moves
	 */
	private void traceMoves(Queue<Move> moves) {
		if (moves != null && moves.size() > 0) {
			Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), evt -> {
				Move nextMove = moves.remove();
				this.board.moveTile(nextMove);
				this.setTilesForView();
			}));
			timeline.setOnFinished(evt -> {
				if (this.board.isSorted()) {
					this.solvedBoardProperty.set(true);
				}
			});
			timeline.setCycleCount(moves.size());
			timeline.play();
		}
	}

	/**
	 * Needs to be called whenever the tiles of this.board have changed to display
	 * the updated board in the view
	 */
	private void setTilesForView() {
		for (Position pos : Position.values()) {
			String tileNumber = Integer.toString(this.board.getTile(pos));
			this.tileNumberProperty[pos.getRow()][pos.getCol()].set(tileNumber);
		}
	}

	private final class Node {
		private Board value;
		private Node previous;

		private Node(Board board) {
			this.value = board;
			this.previous = null;
		}

		public Board getValue() {
			return this.value;
		}

		@Override
		public boolean equals(Object obj) {
			if (Node.this.value.hashCode() == ((Node) obj).getValue().hashCode()) {
				return true;
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return this.value.hashCode();
		}

		public Collection<Board> getNodeNeighbors() {
			Collection<Position> neighbors = this.value.getEmptyTilePosition().getNeighbors();
			ArrayList<Move> moves = new ArrayList<Move>();
			for (Position currentPosition : neighbors) {
				moves.add(new Move(currentPosition, this.value.getEmptyTilePosition()));
			}

			Collection<Board> neighborBoards = new ArrayList<Board>();
			for (Move currentMove : moves) {
				Board newBoard = new Board(this.value);
				newBoard.moveTile(currentMove);
				neighborBoards.add(newBoard);
			}

			return neighborBoards;
		}
	}
}
