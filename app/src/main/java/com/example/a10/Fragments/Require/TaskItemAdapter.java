package com.example.a10.Fragments.Require;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.a10.R;
import com.example.a10.Utils.Tool;
import com.example.a10.Views.LoadButton;

import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by BieTong on 2018/4/25.
 */

public class TaskItemAdapter extends RecyclerView.Adapter<TaskItemAdapter.ViewHolder> {
    private List<TaskItem> taskItemList;
    private Context context;

    public TaskItemAdapter(Context context, List<TaskItem> taskItemList) {
        this.context = context;
        this.taskItemList = taskItemList;
    }

    public void setTaskItemList(List<TaskItem> taskItemList) {
        this.taskItemList = taskItemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (taskItemList.size() > 0 && position == taskItemList.size() - 1) {
            LinearLayout linearLayout = (LinearLayout) holder.titleView.getParent();
            linearLayout.setMinimumHeight(Tool.getTaskItemHeight(context));
//            linearLayout.removeAllViews();
            linearLayout.findViewById(R.id.showButton).setVisibility(GONE);
            linearLayout.setBackgroundColor(Color.parseColor("#00000000"));
            return;
        }
        final TaskItem item = taskItemList.get(position);
        holder.titleView.setText(item.getTitle());
        holder.dateView.setText(item.getDate());
        holder.senderView.setText(item.getSender());
        holder.introduceView.setText(item.getIntroduce());
        final ScrollView scrollView = holder.scrollView;
        scrollView.setOnTouchListener(childScrollTouchListener);
        holder.showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!item.isShow()) {
                    RecyclerView recyclerView = (RecyclerView) v.getParent().getParent().getParent();
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    linearLayoutManager.scrollToPositionWithOffset(position, 0);
                }
                showIntroduceView(scrollView, v, holder, item);
            }
        });
        holder.hideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIntroduceView(scrollView, v, holder, item);
            }
        });
        holder.complete.setText(item.getLeftButtonText());
        holder.complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemButtonClickListener.OnItemButtonClick(v, position, true);
            }
        });
        holder.refuse.setText(item.getRightButtonText());
        holder.refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemButtonClickListener.OnItemButtonClick(v, position, false);
            }
        });
    }

    private void showIntroduceView(final ScrollView scrollView, View v, TaskItemAdapter.ViewHolder holder, TaskItem item) {
        if (scrollView.getVisibility() == GONE) {
            scrollView.setVisibility(VISIBLE);
            ValueAnimator animator = Tool.createDropAnimator(scrollView, scrollView.getLayoutParams(), 0, Tool.getTaskItemHeight(context));
            animator.start();
            holder.showButton.setBackground(v.getResources().getDrawable(R.drawable.button_hide));
        } else {
            ValueAnimator animator = Tool.createDropAnimator(scrollView, scrollView.getLayoutParams(), Tool.getTaskItemHeight(context), 0);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    scrollView.setVisibility(GONE);
                }
            });
            animator.start();
            holder.showButton.setBackground(v.getResources().getDrawable(R.drawable.button_show));
        }
        item.setShow(!item.isShow());
        return;
    }

    public void removeItem(int position) {
        taskItemList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return taskItemList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleView;
        TextView dateView;
        TextView senderView;
        TextView introduceView;
        Button showButton;
        Button complete;
        Button refuse;
        LoadButton hideButton;
        ScrollView scrollView;

        public ViewHolder(View view) {
            super(view);
            titleView = view.findViewById(R.id.titleView);
            dateView = view.findViewById(R.id.dateView);
            senderView = view.findViewById(R.id.senderView);
            introduceView = view.findViewById(R.id.introduceView);
            showButton = view.findViewById(R.id.showButton);
            complete = view.findViewById(R.id.complete);
            refuse = view.findViewById(R.id.refuse);
            hideButton = view.findViewById(R.id.hideButton);
            scrollView = view.findViewById(R.id.scrollView);
        }
    }

    private OnItemButtonClickListener onItemButtonClickListener = null;

    public interface OnItemButtonClickListener {
        void OnItemButtonClick(View v, int position, boolean isLeft);
    }

    public void setOnItemButtonClickListener(OnItemButtonClickListener onItemButtonClickListener) {
        this.onItemButtonClickListener = onItemButtonClickListener;
    }

    private View.OnTouchListener childScrollTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            return false;
        }
    };
}
