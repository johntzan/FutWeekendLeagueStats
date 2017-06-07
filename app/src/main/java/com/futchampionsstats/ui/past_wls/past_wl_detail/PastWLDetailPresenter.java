package com.futchampionsstats.ui.past_wls.past_wl_detail;

import android.support.annotation.NonNull;

import com.futchampionsstats.models.AllWeekendLeagues;
import com.futchampionsstats.models.WeekendLeague;
import com.futchampionsstats.models.WeekendLeagueRepository;
import com.futchampionsstats.models.source.WeekendLeagueDataSource;
import com.futchampionsstats.ui.wl.WeekendLeagueDetailPresenter;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by yiannitzan on 4/12/17.
 */

public class PastWLDetailPresenter implements PastWLDetailContract.Presenter {

    public static final String TAG = WeekendLeagueDetailPresenter.class.getSimpleName();

    private final PastWLDetailContract.View mPastWeekendLeaguesDetailView;
    private WeekendLeagueRepository mWeekendLeagueRepository;
    private AllWeekendLeagues mCurrentAllWeekendLeagues;

    public PastWLDetailPresenter(@NonNull WeekendLeagueRepository weekendLeagueRepository, @NonNull PastWLDetailContract.View view) {
        mPastWeekendLeaguesDetailView = checkNotNull(view);
        mWeekendLeagueRepository = checkNotNull(weekendLeagueRepository);

        mPastWeekendLeaguesDetailView.setPresenter(this);
    }

    @Override
    public void clearAllWeekendLeagues() {
        mWeekendLeagueRepository.clearAllWeekendLeagues();
        getAllWeekendLeagues();
    }

    @Override
    public void start() {
        getAllWeekendLeagues();
    }

    @Override
    public void getAllWeekendLeagues() {
        mWeekendLeagueRepository.getAllWeekendLeagues(new WeekendLeagueDataSource.GetAllWeekendLeaguesCallback() {
            @Override
            public void onAllWeekendLeaguesLoaded(AllWeekendLeagues weekendLeagues) {
                mCurrentAllWeekendLeagues = weekendLeagues;
                if(weekendLeagues!=null && weekendLeagues.getAllWeekendLeagues()!=null){
                    if(mPastWeekendLeaguesDetailView.isActive()) mPastWeekendLeaguesDetailView.showPastWeekendLeagues(mCurrentAllWeekendLeagues);
                }
                else{
                    weekendLeagues = new AllWeekendLeagues();
                    ArrayList<WeekendLeague> new_wls = new ArrayList<>();
                    weekendLeagues.setAllWeekendLeagues(new_wls);

                    mCurrentAllWeekendLeagues = weekendLeagues;

                    if(mPastWeekendLeaguesDetailView.isActive()) mPastWeekendLeaguesDetailView.showEmptyWeekendLeagues(mCurrentAllWeekendLeagues);
                }
            }
        });
    }
}
