package com.futchampionsstats.ui.mysquads;

import com.futchampionsstats.BasePresenter;
import com.futchampionsstats.BaseView;
import com.futchampionsstats.models.Squad;

import java.util.ArrayList;

/**
 * Created by yiannitzan on 4/11/17.
 */

public interface MySquadsContract {

    interface View extends BaseView<Presenter> {

        void setSquads(ArrayList<Squad> squads);
        void showEmptySquads();

        void showEditSquad(Squad squad, int squadIndex);
        void showMySquadsInfoDialog();
        void showAddNewSquadDialog(android.view.View view);


        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void setSquads();
        void addNewSquad(Squad squad);

        void editSquad(Squad squad, int squadIndex);
        void setSquadForEdit(int squadIndex);

        void deleteSquad(int squadIndex);

    }

}
