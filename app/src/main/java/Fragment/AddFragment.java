package Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import Adapter.ListFoodAdapter;
import paditech.com.fifood.R;
import Object.Food;

/**
 * Created by USER on 13/4/2016.
 */
public class AddFragment extends Fragment {

    private EditText etName, etAddress, etDesc;
    private RadioGroup rgGoodBad;
    private CheckBox cbReport;
    private View btnCamera, btnAddImage;
    private TextView tvGood, tvBad;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add, container, false);
        init(view);
        return view;
    }

    private void init(View view) {

        etAddress = (EditText) view.findViewById(R.id.etAddress);
        etName = (EditText) view.findViewById(R.id.etName);
        etDesc = (EditText) view.findViewById(R.id.etDesc);
        rgGoodBad = (RadioGroup) view.findViewById(R.id.rgBadGood);
        cbReport = (CheckBox) view.findViewById(R.id.cbReport);
        btnCamera = view.findViewById(R.id.btnCamera);
        btnAddImage = view.findViewById(R.id.btnAddImage);
        tvGood = (TextView) view.findViewById(R.id.tvGood);
        tvBad = (TextView) view.findViewById(R.id.tvBad);

    }

    private void setRgGoodBadCheckedChange() {
        rgGoodBad.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbGood) {
                    tvGood.setTextColor(getResources().getColor(R.color.colorBgMenuClicked));
                    tvBad.setTextColor(Color.parseColor("#000"));
                } else {
                    tvBad.setTextColor(getResources().getColor(R.color.colorBgMenuClicked));
                    tvGood.setTextColor(Color.parseColor("#000"));
                }
            }
        });
    }

}
