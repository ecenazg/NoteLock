package com.app.notelock;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class EntryAdapter extends FirestoreRecyclerAdapter<Item, EntryAdapter.EntryHolder> {

    OnItemClickListener listener;
    OnItemLaunchClickListener listenerLaunch;
    OnItemCopyUserClickListener listenerCopyUser;
    OnItemCopyPasswordClickListener listenerCopyPassword;

    public EntryAdapter(@NonNull @NotNull FirestoreRecyclerOptions<Item> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull EntryHolder holder, int position, @NonNull @NotNull Item model) {
        holder.title.setText(model.getName());
        String desc = model.getUsername();
        if (desc.length() == 0) {
            desc = " ";
        }
        holder.description.setText(desc);
        //holder.creation.setText(model.getCreation());
        String url = "https://logo.clearbit.com/" + model.getUrl();
        Picasso.get()
                .load(url)
                .placeholder(R.drawable.ic_baseline_web_24)
                .into(holder.logo);

    }

    @NonNull
    @NotNull
    @Override
    public EntryHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_card,
                parent, false);


        return new EntryHolder(v);
    }

    class EntryHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;
        //TextView creation;
        ImageView logo;
        ImageButton launch, copyUsername, copyPassword;

        public EntryHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.dashboard_single_card_title);
            description = itemView.findViewById(R.id.dashboard_single_card_description);
            logo = itemView.findViewById(R.id.dashboard_single_card_imageview);
            //creation = itemView.findViewById(R.id.dashboard_creation);
            launch = itemView.findViewById(R.id.dashboardCardLaunch);
            copyUsername = itemView.findViewById(R.id.dashboardCardCopyUsername);
            copyPassword = itemView.findViewById(R.id.dashboardCardCopyPassword);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null)
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                }
            });
            launch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listenerLaunch != null)
                        listenerLaunch.onItemClick(getSnapshots().getSnapshot(position), position);
                }
            });

            copyUsername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listenerCopyUser != null)
                        listenerCopyUser.onItemClick(getSnapshots().getSnapshot(position), position);
                }
            });

            copyPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listenerCopyPassword != null)
                        listenerCopyPassword.onItemClick(getSnapshots().getSnapshot(position), position);
                }
            });




        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemLaunchClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemLaunchClickListener(OnItemLaunchClickListener listenerLaunch) {
        this.listenerLaunch = listenerLaunch;
    }

    public interface OnItemCopyUserClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemCopyUserClickListener(OnItemCopyUserClickListener listenerCopyUser) {
        this.listenerCopyUser = listenerCopyUser;
    }

    public interface OnItemCopyPasswordClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemCopyPasswordClickListener(OnItemCopyPasswordClickListener listenerCopyPassword) {
        this.listenerCopyPassword = listenerCopyPassword;
    }


}
