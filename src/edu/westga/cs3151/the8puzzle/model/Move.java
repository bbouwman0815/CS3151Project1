package edu.westga.cs3151.the8puzzle.model;

/**
 * The Class Move
 *
 * @author CS3151
 * @version Spring 2021
 */
public class Move {
	
	private Position source;
	private Position destination;
	
	/**
	 * Instantiates a new move.
	 *
	 * @pre none
	 * @post getSource().equals(src) && getDestination().equals(dest)
	 * @param src the source position
	 * @param dest the destination position
	 */
	public Move(Position src, Position dest) {
		if (src == null) {
			throw new IllegalArgumentException("source position cannot be null");
		}
		if (dest == null) {
			throw new IllegalArgumentException("destination position cannot be null");
		}
		this.source = src;
		this.destination = dest;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this.hashCode() == obj.hashCode()) {
			return true;
		}
		return false;
	}
	@Override
	public int hashCode() {
		String hash = "" + this.source.getCol() + this.source.getRow() + this.destination.getCol()
				+ this.destination.getRow();
		int Code = Integer.parseInt(hash);
		return Code;
	}
	
	/**
	 * Gets the source.
	 *
	 * @pre none
	 * @post none
	 * @return the source
	 */
	public Position getSource() {
		return this.source;
	}
	
	/**
	 * Gets the destination.
	 *
	 * @pre none
	 * @post none
	 * @return the destination
	 */
	public Position getDestination() {
		return this.destination;
	}
}
