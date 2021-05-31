package com.example.mediolab;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class VideoFragment extends Fragment {

    private static int VIDEO_REQUEST = 102;
    private Uri videoUri = null;
    Button playVideo, captureVideo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);

        playVideo = (Button) view.findViewById(R.id.playVideo);
        playVideo.setOnClickListener(this::playVideo);
        captureVideo = (Button) view.findViewById(R.id.captureVideo);
        captureVideo.setOnClickListener(this::captureVideo);

        return view;
    }


    public void captureVideo(View v){
        Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        if (videoIntent.resolveActivity(getActivity().getPackageManager())!= null){

            startActivityForResult(videoIntent, VIDEO_REQUEST);

        }

    }

    public void playVideo(View v){
        Intent playIntent = new Intent(getActivity(), VideoPlayActivity.class);
        playIntent.putExtra("videoUri", videoUri.toString());
        startActivity(playIntent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VIDEO_REQUEST && resultCode == Activity.RESULT_OK)
        {
            videoUri = data.getData();
        }
    }
}