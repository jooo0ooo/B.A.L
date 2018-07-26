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

public class manual_page_1 extends android.support.v4.app.Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LinearLayout linearLayout=(LinearLayout)inflater.inflate(R.layout.page,container,false);

        LinearLayout background=(LinearLayout)linearLayout.findViewById(R.id.background);

        ImageView imageView=(ImageView) linearLayout.findViewById(R.id.manual_image);
        imageView.setBackgroundResource(R.drawable.test1);

        TextView page_num=(TextView)linearLayout.findViewById(R.id.page_num);
        page_num.setMovementMethod(new ScrollingMovementMethod());
        page_num.setText("1. 자전거 핸들 45도 고정\n\n bal은 핸들을 45도 회전시킨 상태에서 움직이지 못하게 고정합니다.\n\n" +
                "실시간 위치확인이 가능하고 누군가가 자전거에 손을 댄다면 즉시 알람이 울리며 앱으로 팝업 메세지를 전송 해줍니다." +
                "\n\n자전거 도난 염려 말고 안심하고 가벼운 발걸음으로 다녀오세요\n\n주의사항 : 잠금/풀림 설정 시 블루투스와 BAL을 연결해주세요");

        background.setBackground(new ColorDrawable(0xff6dc6d2));

        return linearLayout;
    }
}
