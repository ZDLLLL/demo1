package zjc.qualitytrackingee.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zjc.qualitytrackingee.R;

public class StaffManagementInfoActivity extends AppCompatActivity {
    @BindView(R.id.staffinfo_back_ll)
    LinearLayout staffinfo_back_ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_management_info);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.staffinfo_back_ll)
    public void staffinfo_back_ll_Onclick(){
        finish();
    }
}
