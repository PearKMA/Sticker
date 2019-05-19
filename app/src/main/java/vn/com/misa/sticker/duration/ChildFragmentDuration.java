package vn.com.misa.sticker.duration;

import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.concurrent.TimeUnit;

import vn.com.misa.sticker.ImageStickerFragment;
import vn.com.misa.sticker.R;

public class ChildFragmentDuration extends Fragment {
    private RangeSeekBar<Float> rangeSeekBar;
    private SeekBar progressVideo;
    private TextView txtDuration;
    CountDownTimer count;
    int totalSeconds;
    public static ChildFragmentDuration newInstance() {

        Bundle args = new Bundle();
        ChildFragmentDuration fragment = new ChildFragmentDuration();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.duration_fragment_child,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        progressVideo = (SeekBar) view.findViewById(R.id.progressVid);
        rangeSeekBar = (RangeSeekBar<Float>) view.findViewById(R.id.rangeSBSetTime);
        txtDuration = (TextView) view.findViewById(R.id.txtDuration);
        setRangeSeekBar();
    }
    private long getDurationVideo() {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        //use one of overloaded setDataSource() functions to set your data source
        retriever.setDataSource(getActivity(), Uri.fromFile(new File("/storage/emulated/0/CPR/Video/test.mp4")));
//        AssetFileDescriptor afd = getResources().openRawResourceFd(R.raw.test);
//        retriever.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long timeInMillisec = Long.parseLong(time);
        retriever.release();
        return timeInMillisec;
    }       //get total time of video from path

    private void setRangeSeekBar() {
        progressVideo.setEnabled(false);
        rangeSeekBar.setNotifyWhileDragging(true);
        long time = getDurationVideo();
        totalSeconds = (int) TimeUnit.MILLISECONDS.toSeconds(time);
        progressVideo.setMax(totalSeconds);

        final String str = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(time),
                TimeUnit.MILLISECONDS.toSeconds(time) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time))
        );
        txtDuration.setText(str + "s");

        rangeSeekBar.setRangeValues(0.0f, (float) totalSeconds);
        if (totalSeconds > 10) {
            rangeSeekBar.setSelectedMinValue(0.0f);
            rangeSeekBar.setSelectedMaxValue(10.0f);
        } else {
            rangeSeekBar.setSelectedMinValue(0.0f);
            rangeSeekBar.setSelectedMaxValue((float) totalSeconds);
        }
        count= new CountDownTimer(totalSeconds,
                1000) {

            public void onTick(long millisUntilFinished) {
                progressVideo.setProgress(progressVideo.getProgress()+1);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                cancel();
            }

        }.start();

        rangeSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Float>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Float minValue, Float maxValue) {
                progressVideo.setProgress(Math.round(minValue));
                sendValueToParentFrag(minValue,maxValue);
                if (count!=null){
                    count.cancel();
                }
                count= new CountDownTimer(Math.round(totalSeconds - minValue) * 1000,
                        1000) {

                    public void onTick(long millisUntilFinished) {
                        progressVideo.setProgress(progressVideo.getProgress()+1);
                        //here you can have your logic to set text to edittext
                    }

                    public void onFinish() {
                        cancel();
                    }

                }.start();

                //player.seekTo((long)(minValue*1000));
                //set video view seek to
            }

        });
    }

    private void sendValueToParentFrag(Float minValue, Float maxValue) {
        Fragment frag=(Fragment) getParentFragment();
        if (frag!=null && frag instanceof ImageStickerFragment){
            ((ImageStickerFragment) frag).onRangeTimeSelected(minValue,maxValue);
        }
    }
}
