package com.futchampionsstats.ui.wl.new_game;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.futchampionsstats.models.Game;
import com.futchampionsstats.models.Squad;
import com.futchampionsstats.models.SquadRepository;
import com.futchampionsstats.models.WeekendLeague;
import com.futchampionsstats.models.WeekendLeagueRepository;
import com.futchampionsstats.models.source.WeekendLeagueDataSource;
import com.futchampionsstats.models.source.squads.SquadsDataSource;
import com.futchampionsstats.ui.wl.WeekendLeagueDetailPresenter;
import com.futchampionsstats.utils.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by yiannitzan on 3/17/17.
 */

public class NewGamePresenter implements NewGameContract.Presenter {

    public static final String TAG = WeekendLeagueDetailPresenter.class.getSimpleName();

    private final NewGameContract.View mNewGameView;
    private SquadRepository mSquadRepository;
    private WeekendLeagueRepository mWeekendLeagueRepository;

    private Game mNewGame;
    private ArrayList<Squad> user_squads;

    public NewGamePresenter(@NonNull SquadRepository squadRepository, @NonNull WeekendLeagueRepository weekendLeagueRepository, @NonNull NewGameContract.View view){
        mNewGameView = view;
        mSquadRepository = squadRepository;
        mWeekendLeagueRepository = weekendLeagueRepository;
        Log.d(TAG, "NewGamePresenter: setting new presenter");
        mNewGameView.setPresenter(this);
    }

    @Override
    public void start() {
        setNewGame();
        getUserSquads();
        mNewGameView.setFormationSpinner();
    }

    @Override
    public void setNewGame() {
        mNewGame = new Game();
        mNewGameView.setNewGameToView(mNewGame);
    }

    @Override
    public void getUserSquads() {
        mSquadRepository.getSquads(new SquadsDataSource.GetSquadsCallback() {
            @Override
            public void onSquadsLoaded(ArrayList<Squad> squads) {
                if(!mNewGameView.isActive()){
                    return;
                }
                if(squads!=null && squads.size()>0){
                    Log.d(TAG, "onSquadsLoaded: squads not null");
                    user_squads = squads;
                    mNewGameView.setUserSquads(user_squads);
                    setUserTeamUsing(0);
                }
                else{
                    squads = new ArrayList<>();
                    user_squads = squads;
                    mNewGameView.setUserSquads(user_squads);
                }
            }
        });
    }

    @Override
    public void setDisconnected() {
        //set game to disconnected here if not using two way databinding
        mNewGameView.showDisconnectWarning();
    }

    @Override
    public void setUserTeamUsing(int pos) {
        mNewGame.setUser_team(user_squads.get(pos).getName());
        mNewGame.setUser_team_rating(user_squads.get(pos).getTeam_rating());
        mNewGame.setUser_formation(user_squads.get(pos).getFormation());
    }

    @Override
    public void setOpponentFormation(String formation) {
        mNewGame.setOpp_formation(formation);
    }

    @Override
    public void saveNewGame(View view) {

        if (mNewGame != null) {
            Log.d(TAG, "onClick: " + new Gson().toJson(mNewGame));
            String gameDataCheck = Game.checkIfGameDataCorrect(mNewGame);
//                        String gameDataCheck = "finished";
            mNewGame.setGame_id("wl_game");
            if (gameDataCheck.equals("finished")) {
                if(Integer.parseInt(mNewGame.getUser_goals()) > Integer.parseInt(mNewGame.getOpp_goals())){
                    mNewGame.setUser_won(true);
                }
                else{
                    mNewGame.setUser_won(false);
                }
                Log.d(TAG, "FinishedOnClick: " + new Gson().toJson(mNewGame));
                Utils.hideKeyboard(view.getContext(), view.getWindowToken());
                mWeekendLeagueRepository.saveNewGame(mNewGame, new WeekendLeagueDataSource.OnGameSavedCallback() {
                    @Override
                    public void onGameSaved(WeekendLeague weekendLeague) {
                        mNewGameView.saveGameSuccess();
                    }

                    @Override
                    public void onGameSaveError(WeekendLeague weekendLeague) {
                        mNewGameView.showError("Error occurred saving your game, please try again!");
                    }
                });

            } else if(gameDataCheck.equals("finished in pens")){
                if(Integer.parseInt(mNewGame.getUser_pen_score()) > Integer.parseInt(mNewGame.getOpp_pen_score())){
                    mNewGame.setUser_won(true);
                }
                else{
                    mNewGame.setUser_won(false);
                }
                Log.d(TAG, "FinishedOnClick: " + new Gson().toJson(mNewGame));
                mWeekendLeagueRepository.saveNewGame(mNewGame, new WeekendLeagueDataSource.OnGameSavedCallback() {
                    @Override
                    public void onGameSaved(WeekendLeague weekendLeague) {
                        mNewGameView.saveGameSuccess();
                    }

                    @Override
                    public void onGameSaveError(WeekendLeague weekendLeague) {
                        mNewGameView.showError("Error occurred saving your game, please try again!");
                    }
                });
            }
            else if(gameDataCheck.equals("Disconnect")){
                mNewGame.setUser_won(false);
                mWeekendLeagueRepository.saveNewGame(mNewGame, new WeekendLeagueDataSource.OnGameSavedCallback() {
                    @Override
                    public void onGameSaved(WeekendLeague weekendLeague) {
                        mNewGameView.saveGameSuccess();
                    }

                    @Override
                    public void onGameSaveError(WeekendLeague weekendLeague) {
                        mNewGameView.showError("Error occurred saving your game, please try again!");
                    }
                });
            }
            else{
                mNewGameView.showError(gameDataCheck);
            }
        }
    }

    @Override
    public void createNewSquad(View view) {
            mNewGameView.showNewSquadDialog(view);
    }

    @Override
    public void saveNewSquad(Squad squad) {
        mSquadRepository.saveNewSquad(squad);
        getUserSquads();
    }

}