package com.futchampionsstats;

import android.content.Context;
import android.support.annotation.NonNull;

import com.futchampionsstats.models.SquadRepository;
import com.futchampionsstats.models.WeekendLeagueRepository;
import com.futchampionsstats.models.source.WeekendLeagueLocalDataSource;
import com.futchampionsstats.models.source.squads.SquadsLocalDataSource;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by yiannitzan on 3/15/17.
 */

public class Injection {

    public static WeekendLeagueRepository provideWeekendLeagueRepository(@NonNull Context context) {
        checkNotNull(context);
        return WeekendLeagueRepository.getInstance(WeekendLeagueLocalDataSource.getInstance(context));
    }

    public static SquadRepository provideSquadsRepository(@NonNull Context context){
        checkNotNull(context);
        return SquadRepository.getInstance(SquadsLocalDataSource.getInstance(context));
    }
}
