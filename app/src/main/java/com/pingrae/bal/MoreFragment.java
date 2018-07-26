package com.pingrae.bal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.androidquery.AQuery;
import com.kakao.auth.Session;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import de.hdodenhof.circleimageview.CircleImageView;
import static android.content.Context.MODE_PRIVATE;

public class MoreFragment extends Fragment {

    CircleImageView main_user_img;
    String user_nickname, user_email, user_picture, user_picture_big;
    AQuery aQuery;

    TextView main_user_nickname;
    TextView main_user_email;

    BackPressClose back_pressed;

    Button logoutButton;


    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        final ViewGroup rootGroup =(ViewGroup)inflater.inflate(R.layout.fragment_more,container,false);

        context = container.getContext();

        SharedPreferences pref = this.getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);

        user_nickname = pref.getString("my_nickname", "nothing");
        user_email = pref.getString("my_email", "nothing");
        user_picture = pref.getString("picture_url", "nothing");
        user_picture_big = pref.getString("picture_url_big", "nothing");

        main_user_img =(CircleImageView) rootGroup.findViewById(R.id.more_image);
        aQuery = new AQuery(context);
        aQuery.id(main_user_img).image(user_picture_big);

        main_user_nickname = (TextView) rootGroup.findViewById(R.id.more_nickname);
        main_user_nickname.setText(user_nickname);


        main_user_email = (TextView) rootGroup.findViewById(R.id.more_email);
        main_user_email.setText(user_email);

        logoutButton = (Button) rootGroup.findViewById(R.id.logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Session.getCurrentSession().isOpened()) {
                    requestLogout();
                }
            }
        });

        back_pressed = new BackPressClose(getActivity());


        return  rootGroup;
    }


    public void requestLogout() {
        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                        Toast.makeText(context, "logout successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


}