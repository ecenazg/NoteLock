package com.app.notelock;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
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

public class EntryAdapterForSearch2 extends RecyclerView.Adapter<EntryAdapterForSearch2.ViewHolder> implements Filterable {

    Context context;
    ArrayList<Item> array;
    ArrayList<Item> arrayFull;

    public EntryAdapterForSearch2(Context context, ArrayList<Item> array) {
        this.context = context;
        this.array = array;
        Log.d("conss", "EntryAdapterForSearch2: " + array.size());
        arrayFull = new ArrayList<Item>(array);
        Log.d("tag3", "EntryAdapterForSearch2: ");
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.search_cardview, parent, false);
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
                .placeholder(R.drawable.web_24)
                .error(R.drawable.web_24)
                .into(holder.imageView);
        Picasso.get().setLoggingEnabled(true);

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
            name = itemView.findViewById(R.id.search_cardview_name);
            username = itemView.findViewById(R.id.search_cardview_username);
            imageView = itemView.findViewById(R.id.search_cardview_image);
            constraintLayout = itemView.findViewById(R.id.search_cardview_constraint);

        }
    }

    @Override
    public Filter getFilter() {
        return itemFilter;
    }

    private Filter itemFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Log.d("tag4", "performFiltering: ");
            ArrayList<Item> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(arrayFull);
                Log.d("iff", "performFiltering: ");
            } else {
                String filePattern = constraint.toString().toLowerCase().trim();
                for (Item item : array) {
                    if (item.getName().toLowerCase().contains(filePattern)
                            || item.getUsername().toLowerCase().contains(filePattern)
                            || filePattern.contains(item.getName().toLowerCase())
                            || (item.getUsername().trim().length() != 0 && filePattern.contains(item.getUsername().toLowerCase()))) {
                        filteredList.add(item);
                    }
                    Log.d("tag2", "performFiltering: ");
                }
                Log.d("outs", "performFiltering: " + array.size());
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            Log.d("size", "performFiltering:" + filteredList.size());
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            array.clear();
            array.addAll((ArrayList) results.values);
            Log.d("final", "publishResults: " + array.size());
            notifyDataSetChanged();
        }
    };
}
