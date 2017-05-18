package com.futchampionsstats.models.leaderboards;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

/**
 * Created by yiannitzan on 5/9/17.
 */

public class SearchSuggestions implements SearchSuggestion {

    private String mUserName;
    private String mConsole;

    public SearchSuggestions(String suggestion, String console) {
        this.mUserName = suggestion;
        this.mConsole = console;
    }

    public SearchSuggestions(Parcel source) {
        this.mUserName = source.readString();
        this.mConsole = source.readString();
    }

    public static final Creator<SearchSuggestions> CREATOR = new Creator<SearchSuggestions>() {
        @Override
        public SearchSuggestions createFromParcel(Parcel in) {
            return new SearchSuggestions(in);
        }

        @Override
        public SearchSuggestions[] newArray(int size) {
            return new SearchSuggestions[size];
        }
    };

    @Override
    public String getBody() {
        return mUserName + " (" + mConsole + ")";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mUserName);
        dest.writeString(mConsole);
    }
}
