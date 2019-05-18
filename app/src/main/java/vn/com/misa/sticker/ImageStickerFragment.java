package vn.com.misa.sticker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import vn.com.misa.sticker.child_fragment.ListFragmentPagerAdapter;

public class ImageStickerFragment extends Fragment {
    //UI
    ImageView btnOk;
    ViewPager viewPager;
    TabLayout tabLayout;

    public static ImageStickerFragment newInstance(String path) {
        Bundle args = new Bundle();
        args.putString("Path",path);
        ImageStickerFragment fragment = new ImageStickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_sticker, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        btnOk = view.findViewById(R.id.btnOk);
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        ListFragmentPagerAdapter adapter = new ListFragmentPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_animal);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_emotion);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_fish);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_fruit);
        tabLayout.getTabAt(4).setIcon(R.drawable.ic_love);
        viewPager.setCurrentItem(0);
    }

    public void OnImageSelected(String s) {
        Log.e("TAG", "OnImageSelected: " + s);
    }
}
