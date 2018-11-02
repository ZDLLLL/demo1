package zjc.qualitytrackingee.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import zjc.qualitytrackingee.R;

public class AnalysisActivity extends AppCompatActivity {
    private String content;
    @BindView(R.id.analysis_tv)
    TextView analysis_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);
        ButterKnife.bind(this);
        content=getIntent().getStringExtra("content");
        analysis_tv.setText(content);
    }
}
