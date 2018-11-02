package zjc.qualitytrackingee.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import zjc.qualitytrackingee.MyApplication;
import zjc.qualitytrackingee.R;
import zjc.qualitytrackingee.adapter.AllGoodsRecyclerViewAdapter;
import zjc.qualitytrackingee.internet.Net;
import zjc.qualitytrackingee.utils.VolleyInterface;
import zjc.qualitytrackingee.utils.VolleyRequest;

import static zjc.qualitytrackingee.MyApplication.getContext;

public class ShowGoodsActivity extends AppCompatActivity implements AllGoodsRecyclerViewAdapter.onSlidingViewClickListener{
    @BindView(R.id.allgoods_list_sp)
    Spinner allgoods_list_sp;
    @BindView(R.id.allgoods_rv)
    RecyclerView allgoods_rv;

    private List<String> goodsList=new ArrayList<>();
    private List<String> goodsidList=new ArrayList<>();
    private AllGoodsRecyclerViewAdapter rvAdapter;
    private View view;
    private ArrayAdapter<String> post_adapter;
    private static List<String> goodsclassnameList=new ArrayList<>();//商品类列表
    private static List<String> goodsclassidList=new ArrayList<>();//商品编号
    private List<String> dataImage;     //商品图片
    private List<String> dataName;     //商品名
    private List<String> dataPrice;     //商品名
    private List<String> dataDescribe;//商品描述
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_goods);
        ButterKnife.bind(this);
       loadSpinner();
        allgoods_rv.setLayoutManager(new LinearLayoutManager(this));
        initView();

        //    loadAllGoods();
//        goods_srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//               // initView();
//                goods_srl.setRefreshing(false);
//            }
//        });
    }
    public void updateInterface(){
        //实例化 RecyclerViewAdapter 并设置数据
        rvAdapter = new AllGoodsRecyclerViewAdapter(this,
                dataImage,dataName,dataDescribe,dataPrice);
        //将适配的内容放入 mRecyclerView
        allgoods_rv.setAdapter(rvAdapter);
        //控制Item增删的动画，需要通过ItemAnimator  DefaultItemAnimator -- 实现自定义动画
        allgoods_rv.setItemAnimator(new DefaultItemAnimator());
        //设置滑动监听器 （侧滑）
        rvAdapter.setOnSlidListener(this);
    }
    public  void initView(){


        post_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, goodsclassnameList);
        post_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        allgoods_list_sp.setAdapter(post_adapter);
        allgoods_list_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //teacher_college=position+1;

               loadList();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void loadSpinner(){
        goodsclassnameList = new ArrayList<String>();     //商品类名（谁的消息）
        goodsclassidList = new ArrayList<String>();     //商品类名（谁的消息）
        goodsclassnameList.add("所有商品");
        goodsclassidList.add("-1");
        String url= Net.GetAllGoodsClass+"?c_id="+ MyApplication.getC_id();
        VolleyRequest.RequestGet(getContext(),url, "getallGoodsclass",
                new VolleyInterface(getContext(), VolleyInterface.mListener,VolleyInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(String result) {

                        try {
                            JSONObject jsonObject=new JSONObject(result);
                            JSONArray jsonArray=jsonObject.getJSONArray("getAllCommodityClassByCid");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                goodsclassnameList.add(jsonObject1.getString("coc_name"));
                                goodsclassidList.add(jsonObject1.getString("coc_id"));

                            }
                            // updateInterface();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onMyError(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                        Toast.makeText(ShowGoodsActivity.this,"╮(╯▽╰)╭连接不上了",Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void loadList(){
        dataImage=new ArrayList<>();
        dataName=new ArrayList<>();
        dataDescribe=new ArrayList<>();
        dataPrice=new ArrayList<>();
        String url=Net.GetAllGoodsbyGoodsClassName+"?c_id="+MyApplication.getC_id()+"&coc_name="+allgoods_list_sp.getSelectedItem().toString();
        VolleyRequest.RequestGet(getContext(),url, "GetAllGoodsbyGoodsName",
                new VolleyInterface(getContext(), VolleyInterface.mListener,VolleyInterface.mErrorListener) {
                    @Override
                    public void onMySuccess(String result) {
                        try {
                            JSONObject jsonObject=new JSONObject(result);
                            JSONArray jsonArray=jsonObject.getJSONArray("getCommodityByClass");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                dataName.add(jsonObject1.getString("co_name"));
                                //dataImage.add(jsonObject1.getString("co_img"));
                                dataImage.add(String.valueOf(R.drawable.head0));
                                dataDescribe.add(jsonObject1.getString("co_describe"));
                                dataPrice.add(jsonObject1.getString("co_price"));
                            }
                            updateInterface();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onMyError(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                        Toast.makeText(getContext(),"╮(╯▽╰)╭连接不上了",Toast.LENGTH_SHORT).show();
                    }
                });



        //updateInterface();
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onDeleteBtnCilck(View view, int position) {
        rvAdapter.removeData(position);
    }
    //    public void loadAllGoods(){
//        String url=Net.GetAllGoods+"?c_id="+MyApplication.getC_id();
//        goodsList=new ArrayList<>();
//        goodsidList=new ArrayList<>();
//        VolleyRequest.RequestGet(getContext(),url, "getallGoodsclass",
//                new VolleyInterface(getContext(), VolleyInterface.mListener,VolleyInterface.mErrorListener) {
//
//                    @Override
//                    public void onMySuccess(String result) {
//                        try {
//                            JSONObject jsonObject=new JSONObject(result);
//                            JSONArray jsonArray=jsonObject.getJSONArray("getAllCommodityByCid");
//                            for(int i=0;i<jsonArray.length();i++){
//                                JSONObject jsonObject1=jsonArray.getJSONObject(i);
//                                goodsList.add(jsonObject1.getString("co_name"));
//                                goodsidList.add(jsonObject1.getString("co_id"));
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//
//                    @Override
//                    public void onMyError(VolleyError error) {
//
//                    }
//                });
//    }
}
