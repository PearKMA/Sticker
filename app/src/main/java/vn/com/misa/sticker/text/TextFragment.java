package vn.com.misa.sticker.text;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import vn.com.misa.sticker.R;

public class TextFragment extends Fragment {
    //UI
    ImageView btnOk;
    FrameLayout flContainerSticker;
    LinearLayout llListSticker;
    private VideoView videoView;
    private int position = 0;
    private MediaController mediaController;
    private float lastFrom = 0, lastTo = 10;
    private String path,output="/storage/emulated/0/CPR/Video/test1.mp4";
    final static String FOLDER = "/storage/emulated/0/CPR/Video/";

    public static TextFragment newInstance(String path) {
        Bundle args = new Bundle();
        args.putString("Path",path);
        TextFragment fragment = new TextFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_sticker,container,false);
    }

    /**
     * Hide layout show list sticker because use same layout parent
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        btnOk = view.findViewById(R.id.btnOk);
        flContainerSticker = view.findViewById(R.id.containerSticker);
        llListSticker = view.findViewById(R.id.llListSticker);
        videoView = (VideoView) view.findViewById(R.id.videoView);
        //get path
        if (getArguments() != null)
            path = getArguments().getString("Path");
        setUpVideoView();
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
}
