package com.example.githubusersearch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView)findViewById(R.id.textView);
    }

    public void onClick(View view) {
        GitHubService gitHubService = GitHubService.retrofit.create(GitHubService.class);

        final Call<GitResult> call =
                gitHubService.getUsers("alexanderklim");

        call.enqueue(new Callback<GitResult>() {
            @Override
            public void onResponse(Call<GitResult> call, Response<GitResult> response) {
                if (response.isSuccessful()) {
                    GitResult result = response.body();

                    String user = "Аккаунт GitHub: " + result.getItems().get(0).getLogin();
                    mTextView.setText(user);
                    Log.i("Git", String.valueOf(result.getItems().size()));
                } else {
                    int statusCode = response.code();

                    ResponseBody errorBody = response.errorBody();
                    try {
                        mTextView.setText(errorBody.string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<GitResult> call, Throwable t) {
                mTextView.setText("Что-то пошло не так: " + t.getMessage());
            }
        });
    }
}