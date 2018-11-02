package zjc.qualitytrackingee.EEfragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zjc.qualitytrackingee.EEactivity.EEAnalysisActivity;
import zjc.qualitytrackingee.R;
import zjc.qualitytrackingee.activity.AnalysisActivity;
import zjc.qualitytrackingee.activity.ScanActivity;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class EEHomeFragment extends Fragment {
    @BindView(R.id.eescan_ib)
    ImageButton eescan_ib;

    private int REQUEST_CODE_SCAN = 111;
    private View view;
    public EEHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_eehome, container, false);
        ButterKnife.bind(this,view);
        return  view;
    }

    @OnClick(R.id.eescan_ib)
    public void  eescan_ib_Onclick(){
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        ZxingConfig config = new ZxingConfig();
        config.setPlayBeep(true);//是否播放扫描声音 默认为true
        config.setShake(true);//是否震动  默认为true
        config.setDecodeBarCode(true);//是否扫描条形码 默认为true
        config.setReactColor(R.color.colorAccent);//设置扫描框四个角的颜色 默认为白色
        config.setFrameLineColor(R.color.colorAccent);//设置扫描框边框颜色 默认无色
        config.setScanLineColor(R.color.colorAccent);//设置扫描线的颜色 默认白色
        config.setFullScreenScan(false);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描
        intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                Intent intent=new Intent(getActivity(),EEAnalysisActivity.class);
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                intent.putExtra("content",content);
                startActivity(intent);

                Toast.makeText(getActivity(),"扫描结果为：" + content,Toast.LENGTH_LONG).show();
                //result.setText("扫描结果为：" + content);
            }
        }
    }

}
