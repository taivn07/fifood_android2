package Fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import Adapter.GridPhotoAdapter;
import Adapter.ListFoodAdapter;
import Constant.Constant;
import Constant.ExpandableHeightGridView;
import cz.msebera.android.httpclient.Header;
import paditech.com.fifood.HomeActivity;
import paditech.com.fifood.PickMultiPhotoActivity;
import paditech.com.fifood.R;
import Object.Food;

/**
 * Created by USER on 13/4/2016.
 */
public class AddFragment extends Fragment implements Constant {

    private static int CAMERA_REQUEST = 999;
    private static int PICK_MULTI_PHOTOS = 888;
    private EditText etName, etAddress, etDesc;
    private RadioGroup rgGoodBad;
    private CheckBox cbReport;
    private View btnCamera, btnAddImage, tvReport, btnCreateShop;
    private TextView tvGood, tvBad;
    private ExpandableHeightGridView gridPhoto;
    private ArrayList<Bitmap> listBitmap;
    private ArrayList<File> listFile;
    private GridPhotoAdapter adapter;
    private String arrayImageId = "";


    private boolean isLike;

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
        btnCreateShop = view.findViewById(R.id.btnCreateShop);
        tvGood = (TextView) view.findViewById(R.id.tvGood);
        tvBad = (TextView) view.findViewById(R.id.tvBad);
        tvReport = view.findViewById(R.id.tvReport);
        gridPhoto = (ExpandableHeightGridView) view.findViewById(R.id.gridPhoto);
        setRgGoodBadCheckedChange();
        setBtnCameraClicked();
        setBtnCreateShopClicked();

        listBitmap = new ArrayList<>();
        listFile = new ArrayList<>();
        adapter = new GridPhotoAdapter(listBitmap, getActivity(), this);
        gridPhoto.setAdapter(adapter);
        gridPhoto.setExpanded(true);

    }

    private void setBtnCreateShopClicked() {
        btnCreateShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().setProgressBarIndeterminateVisibility(true);

                if (etName.getText().equals("") || etDesc.getText().equals("") || etAddress.getText().equals("") || listFile.size() < 1) {
                    Toast.makeText(getActivity(), "Please fill all infomations!", Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < listFile.size(); i++) {
                        uploadImage(listFile.get(i), "1", "V6VDTERWUONH170817209946291", "vi", i);
                    }
                }
            }
        });
    }

    private void setRgGoodBadCheckedChange() {

        rgGoodBad.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbGood) {
                    tvGood.setTextColor(getResources().getColor(R.color.colorBgMenuClicked));
                    tvBad.setTextColor(Color.BLACK);
                    isLike = true;
                } else {
                    tvBad.setTextColor(getResources().getColor(R.color.colorBgMenuClicked));
                    tvGood.setTextColor(Color.BLACK);
                    isLike = false;
                }
            }
        });

        tvReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cbReport.isChecked())
                    cbReport.setChecked(true);
                else cbReport.setChecked(false);
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


        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PickMultiPhotoActivity.class);
                startActivityForResult(intent, PICK_MULTI_PHOTOS);
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
            Uri tempUri = getImageUri(getActivity(), photo);

            File finalFile = new File(getRealPathFromURI(tempUri));

            listFile.add(finalFile);

        }
        if (requestCode == PICK_MULTI_PHOTOS && resultCode == getActivity().RESULT_OK) {


            ArrayList<String> imagePath = data.getExtras().getStringArrayList("data");

            for (int i = 0; i < imagePath.size(); i++) {
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath.get(i));
                listBitmap.add(bitmap);
                File file = new File(imagePath.get(i));
                listFile.add(file);
            }
            adapter.notifyDataSetChanged();
        }


    }


    private void uploadImage(File file, final String userID, final String token, final String lang, final int index) {

        AsyncHttpClient aClient = new AsyncHttpClient();
        aClient.addHeader(KEY_X_API_TOKEN, API_TOCKEN);
        aClient.addHeader(KEY_X_APP_PLATFORM, PLATFORM);
        aClient.addHeader(KEY_X_APP_VERSION, VERSION);
        aClient.addHeader(KEY_X_LOCATE, LOCATE);
        aClient.addHeader(KEY_X_UID, UID);

        RequestParams params = new RequestParams();
        params.put(USER_ID, userID);
        params.put(TOKEN, token);
        try {
            params.put(FILE, file);
            params.setHttpEntityIsRepeatable(true);
            params.setUseJsonStreamer(false);
            params.setForceMultipartEntityContentType(true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        params.put(LANG, lang);

        aClient.post(BASE_URL + UPLOAD_FILE, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("JSON", "POST FAIL");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);

                    JSONObject response = jsonObject.getJSONObject(RESPONSE);

                    arrayImageId += response.getString(ID) + ",";

                    Log.e("FILE ID", arrayImageId);

                    if (index == (listFile.size() - 1)) {
                        createShop(userID, token, lang);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void createShop(String userID, String token, String lang) {

        AsyncHttpClient aClient = new AsyncHttpClient();
        aClient.addHeader(KEY_X_API_TOKEN, API_TOCKEN);
        aClient.addHeader(KEY_X_APP_PLATFORM, PLATFORM);
        aClient.addHeader(KEY_X_APP_VERSION, VERSION);
        aClient.addHeader(KEY_X_LOCATE, LOCATE);
        aClient.addHeader(KEY_X_UID, UID);

        RequestParams params = new RequestParams();
        params.put(USER_ID, userID);
        params.put(TOKEN, token);
        params.put(LANG, lang);
        params.put(NAME, etName.getText().toString().trim());
        params.put(ADDRESS, etAddress.getText().toString().trim());
        params.put(LAT, HomeActivity.currLat);
        params.put(LONGTH, HomeActivity.currLongth);
        params.put(CONTENT, etDesc.getText().toString().trim());
        params.put(FILES, arrayImageId);
        if (isLike) params.put(IS_LIKE, 1);
        if (cbReport.isChecked()) params.put(IS_REPORT, 1);
        params.put(MAIN_FILE_ID, arrayImageId.split(",")[0]);


        aClient.post(BASE_URL + SHOP_CREATE, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("JSON", "POST FAIL");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);

                    JSONObject response = jsonObject.getJSONObject(RESPONSE);

                    int result = response.getInt(RESULT);

                    if (result == 1) {
                        Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();

                        getActivity().setProgressBarIndeterminateVisibility(false);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void deleteImage(int position) {
        listBitmap.remove(position);
        listFile.remove(position);
        adapter.notifyDataSetChanged();

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

}
