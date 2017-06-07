package com.futchampionsstats.ui.wl.edit_game;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.futchampionsstats.models.Game;
import com.futchampionsstats.models.WeekendLeague;
import com.futchampionsstats.models.WeekendLeagueRepository;
import com.futchampionsstats.models.source.WeekendLeagueDataSource;
import com.google.gson.Gson;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by yiannitzan on 4/5/17.
 */

public class EditGamePresenter implements EditGameContract.Presenter {

    public static final String TAG = EditGamePresenter.class.getSimpleName();
    private Game mGame;
    private WeekendLeagueRepository mWeekendLeagueRepository;
    private EditGameContract.View mEditGameView;
    private int mGamePosition;

    public EditGamePresenter(@NonNull WeekendLeagueRepository weekendLeagueRepository, @NonNull Game game, @NonNull EditGameContract.View view, int gamePosition) {
        mWeekendLeagueRepository = checkNotNull(weekendLeagueRepository);
        mGame = new Game();
        mGame = checkNotNull(game);
        mGamePosition = gamePosition;

        mEditGameView = checkNotNull(view);
        Log.d(TAG, "EditGamePresenter: NEW");
        mEditGameView.setPresenter(this);
    }

    @Override
    public void start() {
        setGameForEdit();
    }

    @Override
    public void setGameForEdit() {
        if(mEditGameView.isActive()){
            mEditGameView.setGameForEdit(mGame);
            mEditGameView.getOppFormationIndex(mGame);
        }
    }

    @Override
    public void saveGame(View view) {

        if (mGame != null) {
            String gameDataCheck = Game.checkIfGameDataCorrect(mGame);
//            String gameDataCheck = "finished";
            mGame.setGame_id("wl_game");
            try{
                if (gameDataCheck.equals("finished")) {
                    if(Integer.parseInt(mGame.getUser_goals()) > Integer.parseInt(mGame.getOpp_goals())){
                        mGame.setUser_won(true);
                    }
                    else{
                        mGame.setUser_won(false);
                    }
                    Log.d(TAG, "FinishedOnClick: " + new Gson().toJson(mGame));
                    saveGameToRepository(mGame, mGamePosition);

                } else if(gameDataCheck.equals("finished in pens")){
                    if(Integer.parseInt(mGame.getUser_pen_score()) > Integer.parseInt(mGame.getOpp_pen_score())){
                        mGame.setUser_won(true);
                    }
                    else{
                        mGame.setUser_won(false);
                    }
                    Log.d(TAG, "FinishedOnClick: " + new Gson().toJson(mGame));
                    saveGameToRepository(mGame, mGamePosition);
                }
                else if(gameDataCheck.equals("Disconnect")){
                    mGame.setUser_won(false);
                    Log.d(TAG, "FinishedOnClick: " + new Gson().toJson(mGame));

                    saveGameToRepository(mGame, mGamePosition);
                }
                else{
                    if(mEditGameView.isActive()) mEditGameView.showError(gameDataCheck);
                }
            }
            catch (NumberFormatException e){
                if(mEditGameView.isActive()) mEditGameView.showError(gameDataCheck);
            }
        }
    }

    @Override
    public void setOpponentFormation(String formation) {
        mGame.setOpp_formation(formation);
    }

    @Override
    public void setDisconnected() {
        if(mEditGameView.isActive()) mEditGameView.showDisconnectWarning();
    }

    private void saveGameToRepository(Game game, int gamePosition){
        mWeekendLeagueRepository.saveEditGame(game, gamePosition, new WeekendLeagueDataSource.OnGameSavedCallback() {
            @Override
            public void onGameSaved(WeekendLeague weekendLeague) {
                if(mEditGameView.isActive()) mEditGameView.saveGameSuccess();
            }

            @Override
            public void onGameSaveError(WeekendLeague weekendLeague) {
                if(mEditGameView.isActive()) mEditGameView.showError("Error occurred saving your game, please try again!");
            }
        });
    }

}
