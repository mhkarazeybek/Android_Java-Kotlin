package com.mhkarazeybek.uubmb;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class SectionsPagerAdapter extends FragmentPagerAdapter {
    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();
                return changePasswordFragment;

            case 1:
                BlockPersonFragment blockPersonFragment = new BlockPersonFragment();
                return blockPersonFragment;

            case 2:
                BlockedContactsFragment blockedContactsFragment = new BlockedContactsFragment();
                return blockedContactsFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
    public CharSequence getPageTitle(int position){

        switch (position){
            case 0:
                return "Şifre Değiştir";

            case 1:
                return  "Kişi Engelle";

            case 2:
                return  "Eng.'ler Listesi";

            default:
                return null;
        }
    }
}
