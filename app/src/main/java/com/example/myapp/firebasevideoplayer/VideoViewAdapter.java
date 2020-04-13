package com.example.myapp.firebasevideoplayer;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

class VideoViewAdapter extends RecyclerView.Adapter<VideoViewAdapter.MyViewHolder>{

    private List<VideoDetailModel> videoListItem;
    private RecyclerViewClickListener listener;

    interface RecyclerViewClickListener {
        void onClick(VideoDetailModel videoDetailModel);

    }

    public VideoViewAdapter(List<VideoDetailModel> videoList, RecyclerViewClickListener listener) {
        this.videoListItem=videoList;
        this.listener=listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.video_list_layout, viewGroup, false);
        return  new MyViewHolder(itemView) ;
    }


    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int i) {
        viewHolder.bind(videoListItem.get(i),listener);

    }

    @Override
    public int getItemCount() {
        return videoListItem.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.textview_video_title);
        }

        public void bind(final VideoDetailModel videoDetailModelitem,final RecyclerViewClickListener listener){
            title.setText(videoDetailModelitem.getTitle());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(videoDetailModelitem);
                }
            });
        }
    }



}
