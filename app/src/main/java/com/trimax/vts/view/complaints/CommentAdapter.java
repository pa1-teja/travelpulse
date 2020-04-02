package com.trimax.vts.view.complaints;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.trimax.vts.utils.Utils;
import com.trimax.vts.view.R;
import com.trimax.vts.view.complaints.models.CommentModel;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {

    private List<CommentModel> comments;

    public CommentAdapter(List<CommentModel> comments) {
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
        CardView.LayoutParams params = new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT);
        CommentModel model = comments.get(position);
        holder.tv_date.setText(Utils.getDateTime(model.getCommentDate()));
        holder.tv_comment.setText(model.getComment());
        if (model.getCommentUserType().equalsIgnoreCase("Customer")) {
            holder.tv_name.setText("Me");
            params.setMargins(3, 5, 30, 5);
            holder.rl_container.setLayoutParams(params);
            holder.rl_container.setBackgroundColor(Color.WHITE);
        }
        else {
            params.setMargins(30, 5, 3, 5);
            holder.rl_container.setLayoutParams(params);
            holder.tv_name.setText("TP Support");
            holder.rl_container.setBackgroundColor(Color.parseColor("#FAF8F8"));
        }
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    void addComments(List<CommentModel> comments) {
        this.comments = comments;
        notifyDataSetChanged();
    }

    class CommentHolder extends RecyclerView.ViewHolder{
        CardView rl_container;
        TextView tv_name,tv_date,tv_comment;
        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            rl_container = itemView.findViewById(R.id.rl_container);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_comment = itemView.findViewById(R.id.tv_comment);
        }
    }
}
