package com.tealium.kitchensink.fragment;


import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.VideoView;

import com.tealium.kitchensink.R;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 */
public class WidgetVideoViewFragment extends StyleableFragment {


    public WidgetVideoViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup viewGroup = (ViewGroup) inflater.inflate(
                R.layout.fragment_widget_videoview,
                container,
                false);

        final VideoView videoView = (VideoView) viewGroup.findViewById(R.id.videolayout_videoview);
        final ImageButton imageButton = (ImageButton) viewGroup.findViewById(R.id.videolayout_toggle);
        final SeekBar seekBar = (SeekBar) viewGroup.findViewById(R.id.videolayout_seekbar);

        this.setupVideoView(viewGroup, videoView, seekBar, imageButton);
        imageButton.setOnClickListener(createToggleButtonClickListener(videoView));
        seekBar.setOnSeekBarChangeListener(this.createOnSeekBarChangeListener(viewGroup));

        return viewGroup;
    }

    private void setupVideoView(ViewGroup parent,
                                VideoView videoView,
                                SeekBar seekBar,
                                ImageButton imageButton) {

        MediaController mediaController = new MediaController(this.getActivity());
        mediaController.setVisibility(View.GONE);

        String path = "android.resource://" + getActivity().getPackageName() + File.separator + R.raw.tmu;

        videoView.setOnErrorListener(this.createErrorListener(parent));
        videoView.setOnPreparedListener(this.createOnPreparedListener(videoView, seekBar, imageButton));
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(Uri.parse(path));
    }

    private SeekBar.OnSeekBarChangeListener createOnSeekBarChangeListener(View parent) {

        final VideoView videoView = (VideoView) parent.findViewById(R.id.videolayout_videoview);

        return new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    videoView.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };
    }

    private MediaPlayer.OnPreparedListener createOnPreparedListener(
            final VideoView videoView,
            final SeekBar seekBar,
            final ImageButton imageButton) {

        return new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                seekBar.setMax(videoView.getDuration());
                seekBar.postDelayed(createControlUpdater(videoView, seekBar, imageButton), 1000);
            }
        };
    }

    private Runnable createControlUpdater(final VideoView videoView, final SeekBar seekBar, final ImageButton imageButton) {

        return new Runnable() {
            @Override
            public void run() {

                seekBar.setProgress(videoView.getCurrentPosition());

                if (videoView.isPlaying()) {
                    imageButton.setImageResource(android.R.drawable.ic_media_pause);
                } else {
                    imageButton.setImageResource(android.R.drawable.ic_media_play);
                }

                imageButton.postDelayed(this, 1000);
            }
        };
    }

    private MediaPlayer.OnErrorListener createErrorListener(final ViewGroup viewGroup) {
        return new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {

                View child;

                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    if ((child = viewGroup.getChildAt(i)).getId() == R.id.videolayout_errorlabel) {
                        child.setVisibility(View.VISIBLE);
                    } else {
                        child.setVisibility(View.GONE);
                    }
                }

                return true;
            }
        };
    }

    private View.OnClickListener createToggleButtonClickListener(final VideoView videoView) {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()) {
                    videoView.pause();
                } else {
                    videoView.start();
                }
            }
        };
    }
}
