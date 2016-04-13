package paditech.com.fifood;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import Fragment.AddFragment;
import Fragment.HomeFragment;
import Fragment.NearFragment;
import Object.Food;

import java.util.ArrayList;

import Adapter.ListFoodAdapter;


/**
 * Created by USER on 13/4/2016.
 */
public class HomeActivity extends FragmentActivity {

    private RadioGroup rgMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();


    }

    private void init() {

        final HomeFragment homeFragment = new HomeFragment();
        final NearFragment nearFragment = new NearFragment();
        final AddFragment addFragment = new AddFragment();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainLayout, homeFragment).commit();

        rgMenu = (RadioGroup) findViewById(R.id.rgMenu);

        rgMenu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {


                switch (checkedId) {

                    case R.id.btnHome: {
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.mainLayout, homeFragment).commit();
                        break;

                    }
                    case R.id.btnAccount: {
                        break;

                    }
                    case R.id.btnAdd: {
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.mainLayout, addFragment).commit();
                        break;

                    }
                    case R.id.btnSearch: {
                        break;

                    }
                    case R.id.btnNear: {
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                        ft.replace(R.id.mainLayout, nearFragment).commit();
                        break;

                    }
                }
            }
        });
    }


}
