package com.pingrae.bal;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class manual_page_3 extends android.support.v4.app.Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LinearLayout linearLayout=(LinearLayout)inflater.inflate(R.layout.page,container,false);

        LinearLayout background=(LinearLayout)linearLayout.findViewById(R.id.background);

        ImageView imageView=(ImageView) linearLayout.findViewById(R.id.manual_image);
        imageView.setBackgroundResource(R.drawable.test3);

        TextView page_num=(TextView)linearLayout.findViewById(R.id.page_num);
        page_num.setMovementMethod(new ScrollingMovementMethod());
        page_num.setText("3. GPS\n\n자물쇠 위치정보를 지도에서 확인합니다\n\n" +
                "실시간으로 자전거 위치를 확인 할 수 있습니다.");

        background.setBackground(new ColorDrawable(0xff6dc6d2));

        return linearLayout;
    }
}
