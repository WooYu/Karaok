package com.clicktech.snsktv.module_discover.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.entity.MVConfig;
import com.clicktech.snsktv.module_discover.ui.helper.TemplateAndFilterType;
import com.clicktech.snsktv.module_discover.ui.helper.TemplateFilterTypeHelper;

import java.util.List;


/**
 * Created by why8222 on 2016/3/17.
 * 模板和滤镜的适配器
 */
public class TemplateAndFilterAdapter extends RecyclerView.Adapter<TemplateAndFilterAdapter.FilterHolder> {

    private final int ITEM_TEMPLATE = 0;
    private final int ITEM_FILTER = 1;
    private List<MVConfig> mvConfigList;
    private Context context;
    private int selected = 2;//模板默认选中的是2；滤镜默认选中的是0
    private onFilterChangeListener onFilterChangeListener;

    public TemplateAndFilterAdapter(Context context, TemplateAndFilterType[] filters, int pos) {
        this.mvConfigList = TemplateFilterTypeHelper.TemplateFilterType2MVConfig(filters);
        this.context = context;
        this.selected = pos;
    }

    @Override
    public int getItemViewType(int position) {
        MVConfig mvConfig = mvConfigList.get(position);
        return 0 == mvConfig.getLayouttype() ? ITEM_FILTER : ITEM_TEMPLATE;
    }

    @Override
    public FilterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (ITEM_FILTER == viewType) {
            view = LayoutInflater.from(context).inflate(R.layout.adapter_filter,
                    parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.adapter_template,
                    parent, false);
        }
        FilterHolder viewHolder = new FilterHolder(view);
        viewHolder.thumbImage = (ImageView) view
                .findViewById(R.id.iv_effect);
        viewHolder.filterName = (TextView) view
                .findViewById(R.id.tv_explain);
        viewHolder.filterRoot = (FrameLayout) view
                .findViewById(R.id.fl_root);
        viewHolder.thumbSelected_bg = view.
                findViewById(R.id.coverlayout_bg);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FilterHolder holder, final int position) {

        MVConfig mvConfig = mvConfigList.get(position);
        holder.thumbImage.setImageResource(mvConfig.getEffectpicture());
        holder.filterName.setText(mvConfig.getConfigname());

        if (ITEM_FILTER == getItemViewType(position) && position == selected) {
            holder.thumbSelected_bg.setVisibility(View.VISIBLE);
        } else {
            holder.thumbSelected_bg.setVisibility(View.GONE);
        }

        holder.filterRoot.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (selected == position)
                    return;
                int lastSelected = selected;
                selected = position;
                notifyItemChanged(lastSelected);
                notifyItemChanged(position);
                if (null != onFilterChangeListener)
                    onFilterChangeListener.onFilterChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {

        return mvConfigList == null ? 0 : mvConfigList.size();
    }

    public void setOnFilterChangeListener(onFilterChangeListener onFilterChangeListener) {
        this.onFilterChangeListener = onFilterChangeListener;
    }

    public interface onFilterChangeListener {
        void onFilterChanged(int position);
    }

    class FilterHolder extends RecyclerView.ViewHolder {
        ImageView thumbImage;
        TextView filterName;
        FrameLayout filterRoot;
        View thumbSelected_bg;

        public FilterHolder(View itemView) {
            super(itemView);
        }
    }

}
