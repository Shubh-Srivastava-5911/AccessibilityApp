package com.example.messagerecorder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter <MyAdapter.MyHolder> {
    List<String> al = new ArrayList<>();
    public void setList(List<String> list) {
        if(list!=null) {
            al = list;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent,false);
        return new MyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        String str = al.get(position);
        holder.itv.setText(str);
    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    class MyHolder extends RecyclerView.ViewHolder
    {
        TextView itv;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            itv = itemView.findViewById(R.id.itemTextView);
            // click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener!=null && position!=RecyclerView.NO_POSITION) {
                        listener.myOnItemClick(al.get(position)); // pass the item's text to the implementation of this method in MainActivity
                    }
                }
            });
        }
    }

    private MyOnItemClickListener listener;
    public interface MyOnItemClickListener {
        void myOnItemClick(String str);
    }
    public void setMyOnItemClickListener(MyOnItemClickListener listener) {
        this.listener = listener;
    }

}
