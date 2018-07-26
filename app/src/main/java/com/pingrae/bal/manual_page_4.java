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


public class manual_page_4 extends android.support.v4.app.Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LinearLayout linearLayout=(LinearLayout)inflater.inflate(R.layout.page,container,false);

        LinearLayout background=(LinearLayout)linearLayout.findViewById(R.id.background);

        ImageView imageView=(ImageView) linearLayout.findViewById(R.id.manual_image);
        imageView.setBackgroundResource(R.drawable.test4);

        TextView page_num=(TextView)linearLayout.findViewById(R.id.page_num);
        page_num.setMovementMethod(new ScrollingMovementMethod());
        page_num.setText("4. 문의사항\n\nBAL을 사용할 때 궁금한 점이나 개선사항이 있다면\n아래 연락처로 연락주세요\n\n" +
                "Phone. 010-1234-1234\ne-mail. asdf1234@mail.com");

        background.setBackground(new ColorDrawable(0xff6dc6d2));

        return linearLayout;
    }
}
