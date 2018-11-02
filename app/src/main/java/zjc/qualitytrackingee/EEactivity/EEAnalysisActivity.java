package zjc.qualitytrackingee.EEactivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import zjc.qualitytrackingee.R;

public class EEAnalysisActivity extends AppCompatActivity {
    private String content;
    @BindView(R.id.eeanalysis_tv)
    TextView eeanalysis_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eeanalysis);
        ButterKnife.bind(this);
        content=getIntent().getStringExtra("content");
        eeanalysis_tv.setText(content);
    }
}
