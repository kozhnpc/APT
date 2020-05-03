package work.kozh.runtime;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import work.kozh.runtime.fragment.FragmentActivity;

public class MainActivity extends AppCompatActivity {


    @InjectView(R.id.tv_test)
    private TextView mTvTest;
    @InjectView(R.id.button)
    private Button mButton;
   /*@BindView(R.id.tv_test)
   private TextView mTvTest;
    @BindView(R.id.button)
    private Button mButton;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        mTvTest = findViewById(R.id.tv_test);
        ViewInject.inject(this);
        mTvTest.setText("注解生效啦");
//        mTvTest.setOnClickListener(this);

    }

    /*@ViewClick({R.id.tv_test, R.id.button})
    public void onClick(View v) {
        Log.i("TAG", "响应点击事件");
        Toast.makeText(this, "注解成功，id:" + v.getId(), Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, FragmentActivity.class));
    }*/


    @ViewClick(R.id.tv_test)
    private void onTvTestClick(TextView tvTest) {
        Toast.makeText(this, "注解成功，TextView", Toast.LENGTH_SHORT).show();
    }

    @ViewClick(R.id.button)
    private void onButtonClick(Button button) {
        Toast.makeText(this, "注解成功，Button", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, FragmentActivity.class));
    }
}
