package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Pawn extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATES = { 7, 8, 9, 16 };



    Pawn(int piecePostion, Alliance pieceAlliance) {
        super(piecePostion, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
//TODO pawn: en passant
        final List<Move> legalMoves = new ArrayList<>();

        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) {


            final int candidateDestinationCoordinate = this.piecePostion + (this.getPieceAlliance().getDirection() * currentCandidateOffset);

            if(!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                continue;
            }

            final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

            if(currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                //TODO deal with Pawn promotions!!
                legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
            } else if (currentCandidateOffset == 16 && this.isFirstMove() &&
                    (BoardUtils.SECOND_ROW[this.piecePostion] && this.getPieceAlliance().isBlack()) ||
                    (BoardUtils.SEVENTH_ROW[this.piecePostion] && this.getPieceAlliance().isWhite())) {
                final int behindCandidateDestinationCoordinate = this.piecePostion + (this.pieceAlliance.getDirection() * 8);
                if(!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() &&
                        !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));

                }
            } else if (currentCandidateOffset == 7 && candidateDestinationTile.isTileOccupied() &&
                    ((this.getPieceAlliance().isWhite() && !BoardUtils.EIGHTH_COLUMN[this.piecePostion]) ||
                    (this.getPieceAlliance().isBlack() && !BoardUtils.FIRST_COLUMN[this.piecePostion]))) {
                final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                if (this.getPieceAlliance() != pieceAlliance) {
                    //TODO attacking into pawn promotion
                    legalMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                }
            } else if (currentCandidateOffset == 9 && candidateDestinationTile.isTileOccupied() &&
                    ((this.getPieceAlliance().isBlack() && !BoardUtils.FIRST_COLUMN[this.piecePostion]) ||
                    (this.getPieceAlliance().isWhite() && !BoardUtils.EIGHTH_COLUMN[this.piecePostion]))) {
                final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                if (this.getPieceAlliance() != pieceAlliance) {
                    //TODO attacking into pawn promotion
                    legalMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }
}
