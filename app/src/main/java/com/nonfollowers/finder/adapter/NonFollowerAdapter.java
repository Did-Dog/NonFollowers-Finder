package com.nonfollowers.finder.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.nonfollowers.finder.R;
import com.nonfollowers.finder.model.User;
import java.util.List;

public class NonFollowerAdapter extends RecyclerView.Adapter<NonFollowerAdapter.ViewHolder> {

    private final List<User> users;
    private final Context context;

    public NonFollowerAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_non_follower, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = users.get(position);
        holder.usernameText.setText("@" + user.getUsername());

        holder.itemView.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("instagram://user?username=" + user.getUsername()));
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://instagram.com/" + user.getUsername()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView usernameText;

        ViewHolder(View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.tv_username);
        }
    }
}
