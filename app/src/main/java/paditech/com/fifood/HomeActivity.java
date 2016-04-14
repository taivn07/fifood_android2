package paditech.com.fifood;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import Fragment.AccountFragment;
import Fragment.AddFragment;
import Fragment.HomeFragment;
import Fragment.NearFragment;
import Fragment.SearchFragment;
import Object.Food;

import java.util.ArrayList;

import Adapter.ListFoodAdapter;


/**
 * Created by USER on 13/4/2016.
 */
public class HomeActivity extends FragmentActivity {

    public ArrayList<Food> listFood;

    private RadioGroup rgMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ActionBar actionBar = getActionBar();

        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorBgMenu)));
        actionBar.setTitle(Html.fromHtml("<b>Home</b>"));

        init();


    }

    private void init() {

        listFood = new ArrayList<>();

        final HomeFragment homeFragment = new HomeFragment();
        final NearFragment nearFragment = new NearFragment();
        final AddFragment addFragment = new AddFragment();
        final SearchFragment searchFragment = new SearchFragment();
        final AccountFragment accountFragment = new AccountFragment();

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
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.mainLayout, accountFragment).commit();
                        break;

                    }
                    case R.id.btnAdd: {
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.mainLayout, addFragment).commit();
                        break;

                    }
                    case R.id.btnSearch: {
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.mainLayout, searchFragment).commit();
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


    private void getListFoodNear(String lang, long lat, long longth) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
