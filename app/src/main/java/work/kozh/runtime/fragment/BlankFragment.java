package work.kozh.runtime.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import work.kozh.apt.TestActivity;
import work.kozh.runtime.InjectView;
import work.kozh.runtime.R;
import work.kozh.runtime.ViewClick;
import work.kozh.runtime.ViewInject;

public class BlankFragment extends Fragment {

    @InjectView(R.id.tv_test2)
    private TextView mTvTest2;


    public BlankFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static BlankFragment newInstance() {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        ViewInject.inject(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTvTest2.setText("Fragment中的注解也生效啦");

    }

    @ViewClick(R.id.tv_test2)
    public void onClick(View v) {
        Log.i("TAG", "Fragment响应点击事件");
        Toast.makeText(getContext(), "Fragment注解成功，id:" + v.getId(), Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getContext(), TestActivity.class));
    }


}
