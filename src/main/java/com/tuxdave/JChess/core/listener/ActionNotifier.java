package com.tuxdave.JChess.core.listener;

public interface ActionNotifier {

    /**
     * called when the king is under attack at the end of turn
     * @param message
     */
    public void notifyKingUnderAttack(String message);
}
