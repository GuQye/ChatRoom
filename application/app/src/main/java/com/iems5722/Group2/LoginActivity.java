package com.iems5722.Group2;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.Bind;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "guquanye";
    private static final int REQUEST_SIGNUP = 0;
    private int user_id;
    private boolean flag = true;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String account;
    private String password;
    private String user_name;
    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_login) Button _loginButton;
    @Bind(R.id.link_signup) TextView _signupLink;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        String acnt = pref.getString("account","");
        String pwd = pref.getString("password","");
        _emailText.setText(acnt);
        _passwordText.setText(pwd);
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //String path = "http://54.169.112.52:5000/signin";
                account = _emailText.getText().toString();
                password = _passwordText.getText().toString();
                editor = pref.edit();
                editor.putString("account",account);
                editor.putString("password",password);
                Log.d(TAG, "onClick: " + account);
                editor.commit();
                new BackgroundTask().execute(account,password);

            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        //_loginButton.setEnabled(true);
        //finish();
        Intent intent = new Intent(LoginActivity.this,RoomListActivity.class);
        intent.putExtra("user_name",user_name);
        intent.putExtra("user_id",user_id);
        startActivity(intent);
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }


    class BackgroundTask extends AsyncTask<String,Integer,String> {
        @Override
        protected String doInBackground(String... params) {
            try {

                OkHttpClient client = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("account", params[0])
                        .add("password", params[1])
                        .build();

                Request request = new Request.Builder()
                        .url("http://54.169.112.52:5000/signin")
                        .post(requestBody)
                        .build();

                Response response = client.newCall(request).execute();
                String responseData = response.body().string();
                Log.d(TAG, "doInBackground: "+ responseData);
                return responseData;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String jsonString) {
            super.onPostExecute(jsonString);

            try {
                JSONObject json = new JSONObject(jsonString);
                String status = json.getString("status");
                if ("OK".equals(status)) {
                    int code = json.getInt("data");
                    if(code==200){
                        user_name = json.getString("user_name");
                        user_id = json.getInt("user_id");
                        login();
                        Log.d(TAG, "onPostExecute: "+jsonString) ;
                    }
                    else{
                        Log.d(TAG, "onPostExecute: fail");
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
