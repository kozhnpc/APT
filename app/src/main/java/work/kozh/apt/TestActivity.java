package work.kozh.apt;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import me.brucezz.viewfinder.ViewFinder;
import me.brucezz.viewfinder.annotation.BindView;
import work.kozh.runtime.R;

/**
 * distributionUrl=https\://services.gradle.org/distributions/gradle-4.10.1-all.zip
 * classpath 'com.android.tools.build:gradle:3.3.2'
 * 这两点很关键 否则无法生成代码
 */
public class TestActivity extends AppCompatActivity {

    /*@InjectView2(R.id.tv_test)
    private TextView mTvTest;
    @InjectView2(R.id.button)
    private Button mButton;*/

    @BindView(R.id.tv_test)
    TextView mTvTest;
    @BindView(R.id.button)
    Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ButterKnife.bind(this);
//        ViewInject2.inject(this);
        ViewFinder.inject(this);

    }

 /*   @ViewClick2(R.id.tv_test)
    private void onTvTestClick(TextView tvTest) {
        Toast.makeText(this, "注解成功，TextView", Toast.LENGTH_SHORT).show();
    }

    @ViewClick2(R.id.button)
    private void onButtonClick(Button button) {
        Toast.makeText(this, "注解成功，Button", Toast.LENGTH_SHORT).show();
    }*/

}
