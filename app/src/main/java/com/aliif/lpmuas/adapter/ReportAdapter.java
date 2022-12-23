package com.aliif.lpmuas.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.aliif.lpmuas.R;
import com.aliif.lpmuas.activty.DetailActivity;
import com.aliif.lpmuas.model.Report;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    private List<Report> reportList;
    private Activity activity;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public ReportAdapter(List<Report> reportList, Activity activity){
        this.reportList = reportList;
        this.activity = activity;
    }


    @NonNull
    @Override
    public ReportAdapter.ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item, parent, false);

        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportAdapter.ReportViewHolder holder, int position) {
        final Report report = reportList.get(position);
        holder.title.setText(report.getTitle());
        holder.content.setText(report.getContent());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity.getApplicationContext(), DetailActivity.class);
                intent.putExtra("title", report.getTitle());
                intent.putExtra("content", report.getContent());
                intent.putExtra("date", report.getDate());
                intent.putExtra("location", report.getLocation());
                intent.putExtra("user_id", report.getUser_id());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    public class ReportViewHolder extends RecyclerView.ViewHolder{
        TextView title, content;
        CardView cardView;

        public ReportViewHolder(View itemView){
            super(itemView);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
            cardView = itemView.findViewById(R.id.card);
        }
    }
}
