package com.capstoneproject.pedromarco.eventapp.main.tabs.uicommon;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.capstoneproject.pedromarco.eventapp.R;
import com.capstoneproject.pedromarco.eventapp.entities.Event;
import com.capstoneproject.pedromarco.eventapp.lib.base.ImageLoader;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Adapter class for the recliclerviews containing the event list
 */
public class FragmentListRecyclerviewAdapter extends RecyclerView.Adapter<FragmentListRecyclerviewAdapter.ViewHolder> {

    private List<Event> events;
    private ImageLoader loader;
    private OnEventClickListener listener;

    public FragmentListRecyclerviewAdapter(List<Event> events, ImageLoader imageLoader, OnEventClickListener listener) {
        this.loader = imageLoader;
        this.listener = listener;
        this.events = events;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Event currentEvent = events.get(position);
        holder.setOnItemClickListener(currentEvent, listener);
        holder.tvEventName.setText(currentEvent.getName());
        holder.tvCategory.setText(currentEvent.getCategory());
        holder.tvDate.setText(currentEvent.getDate());
        holder.tvLocation.setText(currentEvent.getLocation());
        holder.tvNumberPeople.setText(currentEvent.getCurrentPeople() + (currentEvent.getMaxPeople() != 0 ? ("/" + currentEvent.getMaxPeople()) : ""));
        loader.load(holder.imgBackground, currentEvent.getPhotoURL());
    }

    public void addEvents(List<Event> newEvents) {
        events.clear();
        events.addAll(newEvents);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.imgBackground)
        ImageView imgBackground;
        @Bind(R.id.relativeLayout)
        RelativeLayout relativeLayout;
        @Bind(R.id.tvDate)
        TextView tvDate;
        @Bind(R.id.tvNumberPeople)
        TextView tvNumberPeople;
        @Bind(R.id.tvCategory)
        TextView tvCategory;
        @Bind(R.id.tvEventName)
        TextView tvEventName;
        @Bind(R.id.tvLocation)
        TextView tvLocation;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }

        public void setOnItemClickListener(final Event event,
                                           final OnEventClickListener listener) {
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onEventClick(event);
                }
            });
        }
    }
}
