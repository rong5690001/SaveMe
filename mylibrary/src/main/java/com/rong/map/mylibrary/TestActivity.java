package com.rong.map.mylibrary;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import com.rong.map.mylibrary.databinding.ActivityTestBinding;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    private UserBean userBean = new UserBean("test");
    ActivityTestBinding binding;
    Animation likeImgAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil
                .setContentView(this
                        , R.layout.activity_test);
        binding.setUser(userBean);
        binding.setVariable(com.rong.map.mylibrary.BR.onClickListener, this);
//        binding.edt.setText("fkdlsajlfkdsa");
        binding.likeView.setText("339");
        likeImgAnimation = AnimationUtils.loadAnimation(this, R.anim.like_img);
//        likeImgAnimation.setRepeatCount(1);
//        likeImgAnimation.setRepeatMode(Animation.REVERSE);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.button) {
            Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
            userBean.setName("changed");
            userBean.setProgress(binding.seekBar.getProgress() / 2);

            binding.likeView.likeOrUnLike(!binding.likeView.isLike());
            userBean.setIsLike(!userBean.getIsLike().get());
            binding.img.clearAnimation();
            binding.img.startAnimation(likeImgAnimation);
            binding.img2.startAnimation(likeImgAnimation);
        }
    }
}
