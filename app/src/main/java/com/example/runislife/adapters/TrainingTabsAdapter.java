package com.example.runislife.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.runislife.TrainingDaysFragment;
import com.example.runislife.TrainingDescriptionFragment;

public class TrainingTabsAdapter extends FragmentPagerAdapter {

    private TrainingDescriptionFragment tab1;
    private TrainingDaysFragment tab2;

    public TrainingTabsAdapter(FragmentManager fm,TrainingDescriptionFragment tab1, TrainingDaysFragment tab2) {
        super(fm);
        this.tab1 = tab1;
        this.tab2 = tab2;
    }

    @Override
    public CharSequence getPageTitle(int position){
        switch(position){
            case 0:
                return "Описание плана";
            case 1:
                return "Дни тренировок";
            default:
                return "";
        }
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return tab1;
            case 1:
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

}
