package com.futchampionsstats.ui.wl.new_game;

import com.futchampionsstats.BasePresenter;
import com.futchampionsstats.BaseView;
import com.futchampionsstats.models.Game;
import com.futchampionsstats.models.Squad;

import java.util.ArrayList;

/**
 * Created by yiannitzan on 3/17/17.
 */

public interface NewGameContract {

    interface View extends BaseView<Presenter> {

        //initiate view
        void setNewGameToView(Game game);

        //disconnect
        void showDisconnectWarning();

        //squads related
        void showNewSquadDialog(android.view.View view);
        void setUserSquads(ArrayList<Squad> squads);
        void setFormationSpinner();

        //saving game related
        void saveGameSuccess();
        void showError(String error);

        boolean isActive();

    }

    interface Presenter extends BasePresenter {

        //game related
        void setNewGame();
        void saveNewGame(android.view.View view);

        //squads related
        void getUserSquads();
        void createNewSquad(android.view.View view);
        void saveNewSquad(Squad squad);
        void setUserTeamUsing(int position);
        void setOpponentFormation(String formation);

        //disconnect
        void setDisconnected();

    }

}
