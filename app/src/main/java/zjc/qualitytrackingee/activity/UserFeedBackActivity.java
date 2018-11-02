package zjc.qualitytrackingee.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zjc.qualitytrackingee.MyApplication;
import zjc.qualitytrackingee.R;
import zjc.qualitytrackingee.adapter.UserFeedBackAdapter;
import zjc.qualitytrackingee.internet.Net;
import zjc.qualitytrackingee.utils.VolleyInterface;
import zjc.qualitytrackingee.utils.VolleyRequest;

import static zjc.qualitytrackingee.MyApplication.getContext;

public class UserFeedBackActivity extends AppCompatActivity {
    @BindView(R.id.user_feedback_srl)
    SwipeRefreshLayout user_feedback_srl;
    @BindView(R.id.user_feedback_rv)
    RecyclerView user_feedback_rv;
  @BindView(R.id.user_feedback_ll) LinearLayout user_feedback_ll;
    public List<String> ImageList;//反馈图片
    private List<String> UsernameList;//用户名
    public List<String> ConameList;//商品名
    public List<String> MessageList;//反馈信息
    private List<String> ScoreList;//打分
    private List<String> TimeList;//时间
    private List<String> UserImageList;//用户头像


    private UserFeedBackAdapter userFeedBackAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed_back);
       // getSupportActionBar().hide();
        ButterKnife.bind(this);
        user_feedback_rv.setLayoutManager(new LinearLayoutManager(this));
        initView();
        datas();
        updateInterface();
    }
    @OnClick(R.id.user_feedback_ll)
    public void user_feedback_ll_Onclick(){
        finish();
    }
    public void initView(){
        user_feedback_srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                datas();
                user_feedback_srl.setRefreshing(false);
            }
        });
    }
    public void updateInterface(){
        //实例化 RecyclerViewAdapter 并设置数据
        userFeedBackAdapter= new UserFeedBackAdapter(this,
                UserImageList, UsernameList,ScoreList,TimeList,this);
        //将适配的内容放入 mRecyclerView
        user_feedback_rv.setAdapter(userFeedBackAdapter);


    }

    public void datas(){
        ImageList = new ArrayList<String>();//头像（谁的头像）
        UsernameList = new ArrayList<String>();     //姓名（谁的消息）
        MessageList = new ArrayList<String>();  //职务（消息内容）
        ScoreList=new ArrayList<>();//打分
        ConameList=new ArrayList<>();
        TimeList=new ArrayList<>();
        UserImageList=new ArrayList<>();
        final String url= Net.GetAllUserFeedBack+"?c_id="+ MyApplication.getC_id();
        VolleyRequest.RequestGet(getContext(),url, "getAllUserFeedBack",
                new VolleyInterface(getContext(), VolleyInterface.mListener,VolleyInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(String result) {
                        Log.d("zjc",url);
                        try {
                            JSONObject jsonObject=new JSONObject(result);
                            JSONArray jsonArray=jsonObject.getJSONArray("getAllFeedbackBycoid");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String u_name=jsonObject1.getString("u_name");
                                String f_score=jsonObject1.getString("f_score");
                                String co_name=jsonObject1.getString("co_name");
                                String time=jsonObject1.getString("f_time");
                                String f_describe=jsonObject1.getString("f_describe");
                                //String image=jsonObject1.getString("f_img");
                                //String userimage=jsonObject1.getString("f_img");

                                //ImageList.add(image.replaceAll("\\\\","") );
                                UsernameList.add(u_name);
                                MessageList.add(f_describe);
                                ScoreList.add(f_score);
                                TimeList.add(time);
                                ConameList.add(co_name);
                                UserImageList.add(String.valueOf(R.drawable.head0));
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
    }
}
