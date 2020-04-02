package com.trimax.vts.view.faq;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.trimax.vts.view.R;

import java.util.List;

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.FaqHolder> {
    private List<FaqModel> faqs;
    private int lastSelection=-1;

    public FaqAdapter( List<FaqModel> faqs) {
        this.faqs = faqs;
    }

    @NonNull
    @Override
    public FaqHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FaqHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_faq_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull FaqHolder holder, int position) {
        FaqModel model = faqs.get(position);
        holder.tv_question.setText(model.getQuestion());
        holder.tv_ans.setText(model.getAns());
        if (model.isExpanded()) {
            hideShowAnimation(holder.tv_ans, 1);
            holder.tv_question.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_add_white,0);
        }
        else {
            hideShowAnimation(holder.tv_ans, 0);
            holder.tv_question.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_add_black,0);
        }
        holder.tv_question.setTag(model);
        holder.tv_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateItem(position);
            }
        });

        holder.tv_question.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                    if(event.getRawX() >= (view.getRight() - ((TextView)view).getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        updateItem(position);
                        return true;
                    }
                return false;
            }
        });
    }


    @Override
    public int getItemCount() {
        return faqs.size();
    }

    public void addFaqs(List<FaqModel> faqs){
        this.faqs=faqs;
        notifyDataSetChanged();
    }

    private void updateItem(int index){
        if (lastSelection!=-1) {
            faqs.get(lastSelection).setExpanded(false);
            notifyItemChanged(lastSelection);
        }
        if (faqs.get(index).isExpanded())
            faqs.get(index).setExpanded(false);
        else {
            faqs.get(index).setExpanded(true);
            lastSelection=index;
        }
        notifyItemChanged(index);
    }

    class FaqHolder extends RecyclerView.ViewHolder{
        TextView tv_question,tv_ans;
        FaqHolder(@NonNull View itemView) {
            super(itemView);
            tv_question = itemView.findViewById(R.id.tv_question);
            tv_ans = itemView.findViewById(R.id.tv_ans);
        }
    }

    private void hideShowAnimation(View view,float value){
        view.animate()
                .alpha(value)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (value==0)
                            view.setVisibility(View.GONE);
                        else
                            view.setVisibility(View.VISIBLE);
                    }
                });
    }
}
