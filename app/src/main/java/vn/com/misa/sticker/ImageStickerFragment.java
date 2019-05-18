package vn.com.misa.sticker;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import vn.com.misa.sticker.child_fragment.ListFragmentPagerAdapter;
import vn.com.misa.sticker.custom.CustomView;

public class ImageStickerFragment extends Fragment {
    //UI
    ImageView btnOk;
    ViewPager viewPager;
    TabLayout tabLayout;
    FrameLayout flContainerSticker;

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
        flContainerSticker=view.findViewById(R.id.containerSticker);
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
        final CustomView view =new CustomView(getActivity());
        Glide.with(this).asBitmap().load(s)
                .apply(new RequestOptions().override(200,200))
                .into(new Target<Bitmap>() {
                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {

                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {

                    }

                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        view.setBitmap(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }

                    @Override
                    public void getSize(@NonNull SizeReadyCallback cb) {

                    }

                    @Override
                    public void removeCallback(@NonNull SizeReadyCallback cb) {

                    }

                    @Override
                    public void setRequest(@Nullable Request request) {

                    }

                    @Nullable
                    @Override
                    public Request getRequest() {
                        return null;
                    }

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onStop() {

                    }

                    @Override
                    public void onDestroy() {

                    }
                });
        flContainerSticker.addView(view,new FrameLayout.LayoutParams(200,200));
    }
}
