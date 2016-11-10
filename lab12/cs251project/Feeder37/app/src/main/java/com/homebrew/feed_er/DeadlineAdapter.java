package com.homebrew.feed_er;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by MEET on 31-Oct-16.
 */

public class DeadlineAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public DeadlineAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                FeedbackList feedbackList = new FeedbackList();
                return feedbackList;
            case 1:
                AssignmentList assignmentList = new AssignmentList();
                return assignmentList;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
