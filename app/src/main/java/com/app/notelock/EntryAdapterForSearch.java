package com.app.notelock;
// obsolete
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class EntryAdapterForSearch extends RecyclerView.Adapter<EntryAdapterForSearch.ViewHolder> {

    Context context;
    ArrayList<Item> array;

    public EntryAdapterForSearch(Context context, ArrayList<Item> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.single_row_for_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Item item = array.get(position);
        String _name = item.getName();
        String _username = item.getUsername();

        holder.name.setText(_name);
        holder.username.setText(_username);
        String url = "https://logo.clearbit.com/" + item.getUrl();
        Picasso.get()
                .load(url)
                .placeholder(R.drawable.ic_baseline_web_24)
                .into(holder.imageView);

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewItemActivity.class);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String name = user.getEmail();
                String path = name + '/' + item.getId();
                intent.putExtra("path", path);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, username;
        ImageView imageView;
        ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.single_row_search_name);
            username = itemView.findViewById(R.id.single_row_search_username);
            imageView = itemView.findViewById(R.id.single_row_search_image);
            constraintLayout = itemView.findViewById(R.id.search_single_row_main_layout);

        }
    }
}
