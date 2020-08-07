package com.tuxdave.JChess.core.listener;

public interface ActionNotifier {

    /**
     * called when the king is under attack
     * @param message
     */
    public void notifyKingUnderAttack(String message);
}
