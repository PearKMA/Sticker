package vn.com.misa.sticker.child_fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import vn.com.misa.sticker.child_fragment.AnimalFragment;
import vn.com.misa.sticker.child_fragment.EmotionFragment;
import vn.com.misa.sticker.child_fragment.FishFragment;
import vn.com.misa.sticker.child_fragment.FruitFragment;
import vn.com.misa.sticker.child_fragment.LoveFragment;

/**
 * create by lvhung on 5/18/2019
 * class này dùng để quản lý các tab sticker trong viewpager
 */
public class ListFragmentPagerAdapter extends FragmentPagerAdapter {

    public ListFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return AnimalFragment.newInstance();
        } else if (position == 1) {
            return EmotionFragment.newInstance();
        } else if (position == 2) {
            return FishFragment.newInstance();
        } else if (position == 3) {
            return FruitFragment.newInstance();
        } else {
            return LoveFragment.newInstance();
        }
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 5;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return "";
            case 1:
                return "";
            case 2:
                return "";
            case 3:
                return "";
            default:
                return null;
        }
    }
}
