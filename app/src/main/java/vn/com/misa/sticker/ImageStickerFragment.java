package vn.com.misa.sticker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;

import vn.com.misa.sticker.child_fragment.ListFragmentPagerAdapter;
import vn.com.misa.sticker.custom.ImageSticker;
import vn.com.misa.sticker.duration.ChildFragmentDuration;
import vn.com.misa.sticker.model.ImageStickerModel;

public class ImageStickerFragment extends Fragment implements ImageStickerListener {
    //UI
    ImageView btnOk;
    ViewPager viewPager;
    TabLayout tabLayout;
    FrameLayout flContainerSticker;
    LinearLayout llListSticker;
    private VideoView videoView;
    private int position = 0;
    private MediaController mediaController;
    //common
    private ArrayList<ImageStickerModel> listSticker;
    private float lastFrom = 0, lastTo = 0;
    private String path,output="/storage/emulated/0/CPR/Video/test1.mp4";

    public static ImageStickerFragment newInstance(String path) {
        Bundle args = new Bundle();
        args.putString("Path", path);
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
        listSticker = new ArrayList<>();
        flContainerSticker = view.findViewById(R.id.containerSticker);
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        llListSticker = view.findViewById(R.id.llListSticker);
        videoView = (VideoView) view.findViewById(R.id.videoView);
        //get path
        if (getArguments() != null)
            path = getArguments().getString("Path");
        //setup viewpager
        setupViewpager();
        setUpVideoView();
        addEvents();
    }

    private void setupViewpager() {
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

    private void setUpVideoView() {
        videoView.post(new Runnable() {
            @Override
            public void run() {
                flContainerSticker.setLayoutParams(
                        new FrameLayout.LayoutParams(videoView.getWidth(), videoView.getHeight(),
                                Gravity.CENTER));
            }
        });
        // Set the media controller buttons
        if (mediaController == null) {
            mediaController = new MediaController(getActivity());
            // Set the videoView that acts as the anchor for the MediaController.
            mediaController.setAnchorView(videoView);
            // Set MediaController for VideoView
            videoView.setMediaController(null);
        }
        if (path != null && !path.isEmpty()) {
            try {
                // ID of video file.

                videoView.setVideoPath(path);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
        }

        videoView.requestFocus();
        // When the video file ready for playback.
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mediaPlayer) {
                videoView.seekTo(position);
                if (position == 0) {
                    videoView.start();
                }
                // When video Screen change size.
                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        // Re-Set the videoView that acts as the anchor for the MediaController
                        mediaController.setAnchorView(videoView);
                    }
                });
            }
        });

    }

    private void addEvents() {
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llListSticker != null && llListSticker.getVisibility() == View.VISIBLE) {
                    llListSticker.animate()
                            .translationY(llListSticker.getHeight())
                            .alpha(0.0f)
                            .setDuration(300)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    llListSticker.clearAnimation();
                                    llListSticker.setVisibility(View.GONE);
                                    getChildFragmentManager().beginTransaction().add(R.id.containerChildFrag,
                                            ChildFragmentDuration.newInstance(), "duration").commit();
                                }
                            });
                } else if (getChildFragmentManager().findFragmentByTag("duration") != null &&
                        getChildFragmentManager().findFragmentByTag("duration").isVisible()) {
                    createCommand();
                }
            }
        });
    }

    private void createCommand() {
        if (path != null && !path.isEmpty()) {
            StringBuilder cmd = new StringBuilder("-y -i " + path);
            for (ImageStickerModel item : listSticker) {
                cmd.append(" -i " + item.getPath());
            }
            cmd.append(" -filter_complex ");
            for (int i = 1; i <= listSticker.size(); i++) {
                int size = (int) listSticker.get(i).getmImage().getSizeSticker();
                float angle = listSticker.get(i).getmImage().getAngle();
                cmd.append("[" + i + ":v]scale=" + size + ":" + size + ",pad=iw+4:ih+4:black@0[s" + i + "];");
                cmd.append("[s" + i + "]rotate=" + angle + "*PI/180:c=none::ow='rotw(" + angle + "*PI/180)':oh='roth(" + angle + "*PI/180)'[r" + i + "];");
            }
            float x=listSticker.get(0).getmImage().getX()+listSticker.get(0).getmImage().getXMain();
            float y=listSticker.get(0).getmImage().getY()+listSticker.get(0).getmImage().getYMain();
            cmd.append("[0:v][r1]overlay="+x+":"+y+":enable='between(t,"+lastFrom+","+lastTo+")'[v1];");
            if (listSticker.size()>1) {
                for (int i = 1; i <= listSticker.size(); i++) {
                    float x1 = listSticker.get(i).getmImage().getX() + listSticker.get(i).getmImage().getXMain();
                    float y1 = listSticker.get(i).getmImage().getY() + listSticker.get(i).getmImage().getYMain();
                    cmd.append("[v" + i + "][r"+(i+1)+"]overlay=" + x1 + ":" + y1 + ":enable='between(t,"+lastFrom+","+lastTo+")'[v"+(i+1)+"];");
                }
                cmd.append(" -map [v"+listSticker.size()+"] -codec:a copy "+output);
            }else {
                cmd.append(" -map [v1] -codec:a copy "+output);
            }
        }
    }


    public void OnImageSelected(String s) {
        Log.e("TAG", "OnImageSelected: " + s);
        final ImageSticker view = new ImageSticker(getActivity());
        Glide.with(this).asBitmap().load(s)
                .apply(new RequestOptions().override(200, 200))
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
        flContainerSticker.addView(view, new FrameLayout.LayoutParams(200, 200, Gravity.CENTER));
        view.setCallback(this);
        view.bringToFront();
        listSticker.add(new ImageStickerModel(view, s));
    }

    @Override
    public void onRemove(ImageSticker sticker) {
        for (ImageStickerModel item : listSticker) {
            if (item.getmImage() == sticker)
                listSticker.remove(item);
            break;
        }
    }

    public void onRangeTimeSelected(Float minValue, Float maxValue) {
        if (minValue != lastFrom || lastTo < maxValue) {
            videoView.seekTo((int) Math.floor(minValue));
            videoView.start();
        }
    }


}
