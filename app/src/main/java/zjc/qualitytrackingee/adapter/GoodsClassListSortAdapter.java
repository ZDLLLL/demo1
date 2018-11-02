package zjc.qualitytrackingee.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import zjc.qualitytrackingee.R;
import zjc.qualitytrackingee.activity.GoodsClassListActivity;
import zjc.qualitytrackingee.activity.MainActivity;
import zjc.qualitytrackingee.beans.GoodsClassBean;

public class GoodsClassListSortAdapter extends BaseAdapter{

    private List<GoodsClassBean> list = null;
    private Context mContext;
    private GoodsClassListActivity goodsClassListActivity;

    public GoodsClassListSortAdapter(Context mContext, List<GoodsClassBean> list, GoodsClassListActivity goodsClassListActivity) {
        this.mContext = mContext;
        this.list = list;
        this.goodsClassListActivity = goodsClassListActivity;
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        final ViewHolder viewHolder;
        final GoodsClassBean goodsClassBean = list.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.goodsclasslist_item, null);
            viewHolder.goodsclass_item_name =  view.findViewById(R.id.goodsclass_item_name);
            viewHolder.goodsclass_item_catalog =  view.findViewById(R.id.goodsclass_item_catalog);
            view.setTag(viewHolder);
            viewHolder.goodsclass_item_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String coclass=viewHolder.goodsclass_item_name.getText().toString();
                    goodsClassListActivity.coc_classid=goodsClassListActivity.list1.get(position).getCo_classid();
                    Intent intent=new Intent(goodsClassListActivity, MainActivity.class);
                    intent.putExtra("statuCar", 1);
                    intent.putExtra("co_class",coclass);
                    intent.putExtra("co_name", goodsClassListActivity.co_name);
                    intent.putExtra("co_price", goodsClassListActivity.co_price);
                    goodsClassListActivity.startActivity(intent);
                    goodsClassListActivity.finish();
                }
            });
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        //根据position获取首字母作为目录catalog
        String catalog = list.get(position).getFirstLetter();

        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if(position == getPositionForSection(catalog)){
            viewHolder.goodsclass_item_catalog.setVisibility(View.VISIBLE);
            viewHolder.goodsclass_item_catalog.setText(goodsClassBean.getFirstLetter().toUpperCase());
        }else{
            viewHolder.goodsclass_item_catalog.setVisibility(View.GONE);
        }

        viewHolder.goodsclass_item_name.setText(this.list.get(position).getCo_class());

        return view;

    }

    final static class ViewHolder {
        TextView goodsclass_item_catalog;
        TextView goodsclass_item_name;

    }

    /**
     * 获取catalog首次出现位置
     */
    public int getPositionForSection(String catalog) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getFirstLetter();
            if (catalog.equalsIgnoreCase(sortStr)) {
                return i;
            }
        }
        return -1;
    }

}