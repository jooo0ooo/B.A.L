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


public class manual_page_2 extends android.support.v4.app.Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LinearLayout linearLayout=(LinearLayout)inflater.inflate(R.layout.page,container,false);

        LinearLayout background=(LinearLayout)linearLayout.findViewById(R.id.background);

        ImageView imageView=(ImageView) linearLayout.findViewById(R.id.manual_image);
        imageView.setBackgroundResource(R.drawable.test2);

        TextView page_num=(TextView)linearLayout.findViewById(R.id.page_num);
        page_num.setMovementMethod(new ScrollingMovementMethod());
        page_num.setText("2. 잠김 풀림\n\n화면에서는 화면을 터치하면 잠김,\n풀림상태가 변합니다.\n\n" +
                "단 핸들을 45도로 돌리지 않으면 화면을 터치해도 잠기지 않습니다");

        background.setBackground(new ColorDrawable(0xff6dc6d2));

        return linearLayout;
    }
}
