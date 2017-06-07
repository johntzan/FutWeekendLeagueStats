package com.futchampionsstats.ui.past_wls.past_wl_view_games;

import android.support.annotation.NonNull;

import com.futchampionsstats.models.Game;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by yiannitzan on 4/19/17.
 */

public class PastWLViewGamePresenter implements PastWLViewGameContract.Presenter{


    private Game mGame;
    private PastWLViewGameContract.View mPastWLViewGameView;

    public PastWLViewGamePresenter(@NonNull Game game, @NonNull PastWLViewGameContract.View view) {
        mPastWLViewGameView = checkNotNull(view);
        mGame = checkNotNull(game);

        mPastWLViewGameView.setPresenter(this);
    }

    @Override
    public void start() {
        getGame();
    }

    @Override
    public void getGame() {
        if(mPastWLViewGameView.isActive()) mPastWLViewGameView.showGame(mGame);
    }
}
