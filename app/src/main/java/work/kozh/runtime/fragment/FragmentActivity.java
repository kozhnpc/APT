package work.kozh.runtime.fragment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import work.kozh.runtime.R;

/**
 * 测试注解在Fragment中的作用
 */
public class FragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        BlankFragment fragment = BlankFragment.newInstance();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();   // 开启一个事务
        transaction.replace(R.id.frame, fragment);
        transaction.commit();

    }
}
