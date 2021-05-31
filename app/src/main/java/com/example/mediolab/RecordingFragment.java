package com.example.mediolab;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class RecordingFragment extends Fragment {

    Button btnRecord, btnStopRecord, btnPlay, btnStop;

    String pathSave=" ";
    MediaRecorder mediaRecorder;
    MediaPlayer mediaplayer;

    final int REQUEST_PERMISSION_CODE=1000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_recording, container, false);

        if(!CheckPermissionFromDevice())
            requestPermissions();

        btnPlay = (Button) view.findViewById(R.id.btnPlay);
        btnRecord = (Button) view.findViewById(R.id.btnStartRecord);
        btnStop = (Button) view.findViewById(R.id.btnStop);
        btnStopRecord = (Button) view.findViewById(R.id.btnStopRecord);

        btnPlay.setOnClickListener(this::onClick);
        btnRecord.setOnClickListener(this::onClick);
        btnStop.setOnClickListener(this::onClick);
        btnStopRecord.setOnClickListener(this::onClick);

        return view;
    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnStartRecord:
                if(CheckPermissionFromDevice())
                {
                    pathSave = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+ UUID.randomUUID().toString()+"_audio_record.3gp";
                    setupMediaRecorder();
                    try{
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                        btnStopRecord.setEnabled(true);
                        btnPlay.setEnabled(false);
                        btnRecord.setEnabled(false);
                        btnStop.setEnabled(false);

                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    btnPlay.setEnabled(false);
                    btnStop.setEnabled(false);
                    Toast.makeText(getContext(), "Recording...", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    requestPermissions();
                }

                break;

            case R.id.btnStopRecord:

                mediaRecorder.stop();
                btnStopRecord.setEnabled(false);
                btnPlay.setEnabled(true);
                btnRecord.setEnabled(true);
                btnStop.setEnabled(false);

                break;

            case R.id.btnPlay:
                btnStop.setEnabled(true);
                btnStopRecord.setEnabled(false);
                btnRecord.setEnabled(false);

                mediaplayer = new MediaPlayer();
                try{
                    mediaplayer.setDataSource(pathSave);
                    mediaplayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaplayer.start();
                Toast.makeText(getActivity(), "Playing...", Toast.LENGTH_SHORT).show();

                break;

            case R.id.btnStop:

                btnStopRecord.setEnabled(false);
                btnRecord.setEnabled(true);
                btnStop.setEnabled(false);
                btnPlay.setEnabled(true);

                if(mediaplayer !=null)
                {
                    mediaplayer.stop();
                    mediaplayer.release();
                    setupMediaRecorder();
                }

                break;

        }

    }


    private void setupMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(pathSave);


    }

    private void requestPermissions()
    {
        ActivityCompat.requestPermissions(getActivity(),new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,

        },REQUEST_PERMISSION_CODE );
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case REQUEST_PERMISSION_CODE:
            {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(getActivity(),"Permission Granted",Toast.LENGTH_SHORT).show();
                else
                {
                    Toast.makeText(getActivity() ,"Permission Denied",Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
    }


    private boolean CheckPermissionFromDevice()
    {
        int write_external_storage_result = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.RECORD_AUDIO);
        return write_external_storage_result == PackageManager.PERMISSION_GRANTED && record_audio_result == PackageManager.PERMISSION_GRANTED;

    }
}