package Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import Adapter.GridPhotoAdapter;
import Adapter.ListFoodAdapter;
import paditech.com.fifood.R;
import Object.Food;

/**
 * Created by USER on 13/4/2016.
 */
public class AddFragment extends Fragment {

    private static int CAMERA_REQUEST = 999;
    private EditText etName, etAddress, etDesc;
    private RadioGroup rgGoodBad;
    private CheckBox cbReport;
    private View btnCamera, btnAddImage, tvReport;
    private TextView tvGood, tvBad;
    private GridView gridPhoto;
    private ArrayList<Bitmap> listBitmap;
    private GridPhotoAdapter adapter;

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
        tvReport = view.findViewById(R.id.tvReport);
        gridPhoto = (GridView) view.findViewById(R.id.gridPhoto);
        setRgGoodBadCheckedChange();
        setBtnCameraClicked();

        listBitmap = new ArrayList<>();
        adapter = new GridPhotoAdapter(listBitmap, getActivity(), this);
        gridPhoto.setAdapter(adapter);

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

        tvReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbReport.setChecked(true);
            }
        });
    }

    private void setBtnCameraClicked() {
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == getActivity().RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            listBitmap.add(photo);
            adapter.notifyDataSetChanged();
        }
    }


    private void createShopFood(Food food, String userID, String token) {


    }

    public void deleteImage(int position) {
        listBitmap.remove(position);
        adapter.notifyDataSetChanged();

    }

}
