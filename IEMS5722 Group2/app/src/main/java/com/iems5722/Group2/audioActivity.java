package com.iems5722.Group2;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioFormat;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.media.AudioTrack;
import android.media.AudioManager;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import io.kvh.media.amr.AmrDecoder;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


import java.util.ArrayList;
import java.util.List;

public class audioActivity extends Activity {
    private AudioTrack mAudioTrack;
    private ListView mListView;
    private String TAG = "guquanye";
    private ArrayAdapter<Recorder> mAdapter;
    private List<Recorder> mDatas = new ArrayList<Recorder>();
    private AudioRecorderButton mAudioRecorderButton;
    private View animView;
    private String post_url = "http://54.169.112.52:5000/UploadFile";
    private static final MediaType MEDIA_TYPE_AMR = MediaType.parse("record/amr");
    private String Filepath;
    static final int SAMPLE_RATE = 8000;
    // 20 ms second
    // 0.02 x 8000 x 2 = 320;160 short
    static final int PCM_FRAME_SIZE = 160;
    static final int AMR_FRAME_SIZE = 32;
    int playerBufferSize = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record);
        mListView = (ListView) findViewById(R.id.id_listview);
        final Button record = (Button) findViewById(R.id.record);
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = "http://54.169.112.52:5000/downloadFile";
                new BackgroundTask().execute("",path);
            }
        });
        playerBufferSize = AudioTrack.getMinBufferSize(SAMPLE_RATE,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
        mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
                playerBufferSize, AudioTrack.MODE_STREAM);
        mAudioRecorderButton = (AudioRecorderButton) findViewById(R.id.id_recorder_button);
        mAudioRecorderButton.setFinishRecorderCallBack(new AudioRecorderButton.AudioFinishRecorderCallBack() {

            public void onFinish(float seconds, String filePath) {
                Recorder recorder = new Recorder(seconds, filePath);
                Log.d(TAG, "onFinish: "+ recorder.toString());
                new BackgroundTask().execute(filePath);

                mDatas.add(recorder);
                //更新数据
                mAdapter.notifyDataSetChanged();
                //设置位置
                mListView.setSelection(mDatas.size() - 1);
            }
        });

        mAdapter = new RecoderAdapter(this, mDatas);
        mListView.setAdapter(mAdapter);

        //listView的item点击事件
        mListView.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                // 声音播放动画
                if (animView != null) {
                    animView.setBackgroundResource(R.drawable.adj);
                    animView = null;
                }
                animView = view.findViewById(R.id.id_recoder_anim);
                animView.setBackgroundResource(R.drawable.play_anim);
                AnimationDrawable animation = (AnimationDrawable) animView.getBackground();
                animation.start();
                // 播放录音
                MediaPlayerManager.playSound(mDatas.get(position).filePath, new MediaPlayer.OnCompletionListener() {

                    public void onCompletion(MediaPlayer mp) {
                        //播放完成后修改图片
                        animView.setBackgroundResource(R.drawable.adj);
                    }
                });
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        MediaPlayerManager.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MediaPlayerManager.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaPlayerManager.release();
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chatroom_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:

                break;
            default:

        }
        return true;
    }


    public class BackgroundTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... url) {
            if (!"".equals(url[0])) {
                String h = null;
                try {
                    h = sendRecord(post_url, url[0]);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return h;
            } else {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("filename", "43d24862-197b-4a8d-b23c-0b79454bc552.amr")
                            .build();
                    Request request = new Request.Builder()
                            .url(url[1])
                            .post(requestBody)
                            .build();
                    Response response = null;

                    response = client.newCall(request).execute();

                    String responseData = response.body().string();
                    long state = AmrDecoder.init();

                    byte[] amrframe = new byte[32];//amr frame 32 bytes
                    short[] pcmframs = new short[160];//pcm frame 160 shorts
                    AmrDecoder.decode(state, amrframe, pcmframs);
                    mAudioTrack.write(pcmframs, 0, pcmframs.length);
                    AmrDecoder.exit(state);
                    Log.d(TAG, "doInBackground: " + responseData);
                    return responseData;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        protected void onPostExecute(String result){
            if (result.equals("OK")){
                Log.d("1","nice");

            }
        }

    }
    public String sendRecord(final String url,final String filePath) throws JSONException, IOException {
        OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        Log.d("path",filePath);
        File f = new File(filePath);
        Log.d("name",f.getName());
        Filepath = f.getName();
        final String record_path = f.getPath();

        builder.addFormDataPart("file", f.getName(), RequestBody.create(MEDIA_TYPE_AMR, f));
        builder.addFormDataPart("room_id","1");
        builder.addFormDataPart("user_id","1");

        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        String responseData = response.body().string();
        JSONObject json = new JSONObject(responseData);
        String status = json.getString("status");
        Log.d("status",status);

        return status;
    }








}