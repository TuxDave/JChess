package com.tuxdave.JChess.core.listener;

public interface ActionNotifier {

    /**
     * called when the king is under attack at the end of turn
     * @param message
     */
    public void notifyKingUnderAttackOnTurnEnds(String message);

    /**
     * active when the king is under attack, and says which one
     * @param playerNick
     */
    public void kingChecked(String playerNick);
}
