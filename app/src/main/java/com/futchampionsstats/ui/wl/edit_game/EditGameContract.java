package com.futchampionsstats.ui.wl.edit_game;

import com.futchampionsstats.BasePresenter;
import com.futchampionsstats.BaseView;
import com.futchampionsstats.models.Game;

/**
 * Created by yiannitzan on 4/5/17.
 */

public class EditGameContract {

    interface View extends BaseView<Presenter> {

        //initiate view
        void setGameForEdit(Game game);

        //disconnect
        void showDisconnectWarning();

        void getOppFormationIndex(Game game);
        void setFormationSpinner(int  oppFormationIndex);

        //saving game related
        void saveGameSuccess();
        void showError(String error);

        boolean isActive();

    }

    public interface Presenter extends BasePresenter {

        //game related
        void setGameForEdit();
        void saveGame(android.view.View view);

        //squads related
        void setOpponentFormation(String formation);

        //disconnect
        void setDisconnected();

    }
}
