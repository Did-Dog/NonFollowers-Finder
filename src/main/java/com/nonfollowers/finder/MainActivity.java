package com.nonfollowers.finder;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.nonfollowers.finder.adapter.NonFollowerAdapter;
import com.nonfollowers.finder.model.User;
import com.nonfollowers.finder.utils.HtmlParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_FOLLOWERS = 1;
    private static final int REQUEST_FOLLOWING = 2;

    private TextView tvFollowersFile, tvFollowingFile;
    private CardView cardFollowers, cardFollowing;
    private Button btnCompare;
    private RecyclerView recyclerView;
    private TextView tvResultCount;

    private Uri followersUri;
    private Uri followingUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvFollowersFile = findViewById(R.id.tv_followers_file);
        tvFollowingFile = findViewById(R.id.tv_following_file);
        cardFollowers = findViewById(R.id.card_followers);
        cardFollowing = findViewById(R.id.card_following);
        btnCompare = findViewById(R.id.btn_compare);
        recyclerView = findViewById(R.id.recycler_results);
        tvResultCount = findViewById(R.id.tv_result_count);

        ImageButton btnAbout = findViewById(R.id.btn_about);
        btnAbout.setOnClickListener(v -> AboutDialog.show(this));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cardFollowers.setOnClickListener(v -> pickFile(REQUEST_FOLLOWERS));
        cardFollowing.setOnClickListener(v -> pickFile(REQUEST_FOLLOWING));
        btnCompare.setOnClickListener(v -> compareFiles());
    }

    private void pickFile(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/html");
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK || data == null) return;

        Uri uri = data.getData();
        if (uri == null) return;

        final int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
        getContentResolver().takePersistableUriPermission(uri, takeFlags);

        if (requestCode == REQUEST_FOLLOWERS) {
            followersUri = uri;
            tvFollowersFile.setText(getFileName(uri));
            cardFollowers.setCardBackgroundColor(ContextCompat.getColor(this, R.color.oneui_success));
        } else if (requestCode == REQUEST_FOLLOWING) {
            followingUri = uri;
            tvFollowingFile.setText(getFileName(uri));
            cardFollowing.setCardBackgroundColor(ContextCompat.getColor(this, R.color.oneui_success));
        }

        updateCompareButton();
    }

    private String getFileName(Uri uri) {
        String path = uri.getPath();
        if (path != null && path.contains("/")) {
            return path.substring(path.lastIndexOf("/") + 1);
        }
        return uri.getLastPathSegment();
    }

    private void updateCompareButton() {
        btnCompare.setEnabled(followersUri != null && followingUri != null);
        if (btnCompare.isEnabled()) {
            btnCompare.setAlpha(1.0f);
        } else {
            btnCompare.setAlpha(0.5f);
        }
    }

    private void compareFiles() {
        if (followersUri == null || followingUri == null) return;

        Dialog loadingDialog = new Dialog(this);
        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadingDialog.setContentView(R.layout.dialog_loading);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loadingDialog.setCancelable(false);
        loadingDialog.show();

        new Thread(() -> {
            try {
                Set<String> followers;
                Set<String> following;

                try (InputStream is = getContentResolver().openInputStream(followersUri)) {
                    followers = HtmlParser.extractUsernames(is);
                }
                try (InputStream is = getContentResolver().openInputStream(followingUri)) {
                    following = HtmlParser.extractUsernames(is);
                }

                Set<String> nonFollowers = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
                for (String user : following) {
                    if (!followers.contains(user)) {
                        nonFollowers.add(user);
                    }
                }

                List<User> userList = new ArrayList<>();
                for (String username : nonFollowers) {
                    userList.add(new User(username));
                }

                runOnUiThread(() -> {
                    loadingDialog.dismiss();
                    showResults(userList);
                });

            } catch (Exception e) {
                runOnUiThread(() -> {
                    loadingDialog.dismiss();
                    Toast.makeText(this,
                            getString(R.string.error_parse) + ": " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                });
            }
        }).start();
    }

    private void showResults(List<User> userList) {
        recyclerView.setAdapter(new NonFollowerAdapter(this, userList));

        String countText = getString(R.string.result_count,
                userList.size());
        tvResultCount.setText(countText);
        tvResultCount.setVisibility(View.VISIBLE);

        if (userList.isEmpty()) {
            Toast.makeText(this, R.string.all_follow_back, Toast.LENGTH_LONG).show();
        }
    }
}
