package com.nonfollowers.finder;

import android.app.Dialog;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class AboutDialog {

    public static void show(AppCompatActivity activity) {
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_about);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tvTitle = dialog.findViewById(R.id.tv_about_title);
        TextView tvContent = dialog.findViewById(R.id.tv_about_content);

        try {
            AssetManager am = activity.getAssets();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(am.open("about.json"), StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();

            JSONObject json = new JSONObject(sb.toString());
            tvTitle.setText(json.optString("title", "About"));

            StringBuilder content = new StringBuilder();
            if (json.has("description")) {
                content.append(json.getString("description")).append("\n\n");
            }
            if (json.has("version")) {
                content.append("Version: ").append(json.getString("version")).append("\n\n");
            }
            if (json.has("features")) {
                JSONArray features = json.getJSONArray("features");
                for (int i = 0; i < features.length(); i++) {
                    content.append("- ").append(features.getString(i)).append("\n");
                }
                content.append("\n");
            }
            if (json.has("developer")) {
                content.append("Developer: ").append(json.getString("developer")).append("\n");
            }
            if (json.has("contact")) {
                content.append("Contact: ").append(json.getString("contact"));
            }

            tvContent.setText(content.toString().trim());

        } catch (Exception e) {
            tvTitle.setText("About");
            tvContent.setText("Failed to load about information.");
        }

        dialog.findViewById(R.id.btn_about_close).setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}
