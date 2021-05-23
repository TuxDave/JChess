package com.tuxdave.JChess.UI.listener;

public interface ReadyListener {

    /**
     * called on readyButton pressed
     * @param _pNumber 0 or 1
     * @param _nick the nickname of the player
     * @param _imagePath the complete path of profile image
     * @param _state ready or not
     * @return nothing
     */
    public void onReady(int _pNumber, String _nick, String _imagePath, boolean _state);
}
