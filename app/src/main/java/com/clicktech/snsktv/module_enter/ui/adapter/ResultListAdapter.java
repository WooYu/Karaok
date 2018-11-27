package com.clicktech.snsktv.module_enter.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.entity.CityEntity;

import java.util.List;

/**
 * author zaaach on 2016/1/26.
 */
public class ResultListAdapter extends BaseAdapter {
    private Context mContext;
    private List<CityEntity> mCities;

    public ResultListAdapter(Context mContext, List<CityEntity> mCities) {
        this.mCities = mCities;
        this.mContext = mContext;
    }

    public void changeData(List<CityEntity> list) {
        if (mCities == null) {
            mCities = list;
        } else {
            mCities.clear();
            mCities.addAll(list);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mCities == null ? 0 : mCities.size();
    }

    @Override
    public CityEntity getItem(int position) {
        return mCities == null ? null : mCities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ResultViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.adapter_city_searchresult
                    , parent, false);
            holder = new ResultViewHolder();
            holder.name = (TextView) view.findViewById(R.id.tv_item_result_listview_name);
            view.setTag(holder);
        } else {
            holder = (ResultViewHolder) view.getTag();
        }
        holder.name.setText(mCities.get(position).getName());
        return view;
    }

    public static class ResultViewHolder {
        TextView name;
    }
}
