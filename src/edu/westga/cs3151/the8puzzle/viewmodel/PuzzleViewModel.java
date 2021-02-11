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

	private Stack<Board> undoBoardStack;

	/**
	 * Instantiates a new student info view model.
	 * 
	 * @pre none
	 * @post a new view model representing a random 8-puzzle
	 */
	public PuzzleViewModel() {
		this.board = new Board();
		this.undoBoardStack = new Stack<Board>();
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
		if (pos == null) {
			return;
		}

		Position destinationPos = this.board.getEmptyTilePosition();

		if (this.undoBoardStack.size() == 0) {
			this.undoBoardStack.add(new Board(this.board));
		}
		if (this.undoBoardStack.size() == 1) {
			this.undoBoardStack.clear();
			this.undoBoardStack.add(new Board(this.board));
		}
		if (this.board.moveTile(pos, destinationPos)) {
			Board newBoard = new Board(this.board);
			this.undoBoardStack.add(newBoard);
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
		System.out.println("Replace me by instructions to undo the most recent move");
		if (this.undoBoardStack.size() == 1) {
			return;
		}
		if (this.undoBoardStack.size() > 1) {
			this.undoBoardStack.pop();
			this.board = this.undoBoardStack.lastElement();
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
		System.out.println("Replace me by instructions to set the next tile at the correct position.");

		int numberCorrectPositions = this.board.getNumberSortedTiles();
		int nextNumberToSolve = numberCorrectPositions + 1;

		Queue<Move> correctMoves = new LinkedList<Move>();
		Queue<Node> moves = new LinkedList<Node>();
		
		Board sourceBoard = new Board(this.board);
		Node sourceNode = new Node(sourceBoard);
		moves.add(sourceNode);
		
		while (!moves.isEmpty()) {
			Node currentNode = moves.remove();
			Position emptyTilePosition = currentNode.value.getEmptyTilePosition();
			ArrayList<Position> emptyTileNeighbors = (ArrayList<Position>) emptyTilePosition.getNeighbors();
			
			for (Position currentPosition : emptyTileNeighbors) {
				
				Board alteredBoard = new Board(currentNode.value);
				alteredBoard.moveTile(currentPosition, emptyTilePosition);
				
				Node neighborNode = new Node(alteredBoard);
				neighborNode.previous = currentNode;
				
				moves.add(neighborNode);	
				
				if (neighborNode.value.getNumberSortedTiles() == nextNumberToSolve) {
					//use neighborNode to execute final method to return queue of moves
					this.board = neighborNode.value;
					moves.clear();
					this.setTilesForView();
				}
			}	
		}
	}
	
	private ArrayList<Position> getUnmoveablePositions(int staticPositions, Position position) {
		ArrayList<Position> currentPositions = (ArrayList<Position>) Position.values();
		for (int i = 0; i < staticPositions; i++) {
			currentPositions.remove(i);
		}
		ArrayList<Position> unmoveablePositions = new ArrayList<Position>(currentPositions);

		return unmoveablePositions;
	}

	private boolean checkIfPositionIsMoveable(ArrayList<Position> unmoveablePositions, Position position) {
		boolean isMoveable = true;

		for (Position currentPosition : unmoveablePositions) {
			if (position.getRow() == currentPosition.getRow() && 
					position.getCol() == currentPosition.getCol()) {
				isMoveable = false;
			}
		}

		return isMoveable;
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
		this.undoBoardStack.clear();

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
		private Node next;
		private Node previous;
		private boolean visited;

		private Node(Board board) {
			this.value = board;
			this.next = null;
			this.previous = null;
			this.visited = false;
		}
	}
}
