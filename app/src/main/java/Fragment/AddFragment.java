package Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Object.ImageUpload;
import Adapter.GridPhotoAdapter;
import Constant.Constant;
import Constant.ExpandableHeightGridView;
import cz.msebera.android.httpclient.Header;
import paditech.com.fifood_android.DetailFoodActivity;
import paditech.com.fifood_android.HomeActivity;
import paditech.com.fifood_android.LoginActivity;
import paditech.com.fifood_android.PickMultiPhotoActivity;
import paditech.com.fifood_android.R;
import Constant.GetImageFile;
import Constant.HideKeyBoard;

/**
 * Created by USER on 13/4/2016.
 */
public class AddFragment extends Fragment implements Constant {

    private static int CAMERA_REQUEST = 999;
    private static int PICK_MULTI_PHOTOS = 888;
    private EditText etName, etAddress, etDesc;
    private RadioGroup rgGoodBad;
    private CheckBox cbReport, cbChooseAvatar;
    private View btnCamera, tvReport, btnCreateShop, mainLayout, btnRefresh;
    private TextView tvGood, tvBad;
    public ExpandableHeightGridView gridPhoto;
    private ImageView imgAvatar;
    public ArrayList<ImageUpload> listBitmap;
    private GridPhotoAdapter adapter;
    private String arrayImageId = "", avatarId = "";
    private ProgressBar progressBar;
    private boolean isLike;
    public boolean isSelector = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        init(view);
        return view;
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.btnAdd){
            btnCreateShop.callOnClick();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init(View view) {

        etAddress = (EditText) view.findViewById(R.id.etAddress);
        etName = (EditText) view.findViewById(R.id.etName);
        etDesc = (EditText) view.findViewById(R.id.etDesc);
        rgGoodBad = (RadioGroup) view.findViewById(R.id.rgBadGood);
        cbReport = (CheckBox) view.findViewById(R.id.cbReport);
        cbChooseAvatar = (CheckBox) view.findViewById(R.id.btnChooseAvatar);
        btnCamera = view.findViewById(R.id.btnCamera);
        btnCreateShop = view.findViewById(R.id.btnCreateShop);
        btnRefresh = view.findViewById(R.id.btnRefresh);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        tvGood = (TextView) view.findViewById(R.id.tvGood);
        tvBad = (TextView) view.findViewById(R.id.tvBad);
        tvReport = view.findViewById(R.id.tvReport);
        imgAvatar = (ImageView) view.findViewById(R.id.imgAvartar);
        gridPhoto = (ExpandableHeightGridView) view.findViewById(R.id.gridPhoto);
        mainLayout = view.findViewById(R.id.mainLayout);
        setRgGoodBadCheckedChange();
        setBtnCameraClicked();
        setBtnCreateShopClicked();
        hideKeyBoardOnOutClicked();

        listBitmap = new ArrayList<>();
        adapter = new GridPhotoAdapter(listBitmap, getActivity(), this);
        gridPhoto.setAdapter(adapter);
        gridPhoto.setExpanded(true);

        setCurrentAddress();
        setBtnRefreshClicked();
        setCbChooseAvatarClicked();
    }

    private void setCbChooseAvatarClicked() {
        cbChooseAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbChooseAvatar.isChecked()) isSelector = true;
                else isSelector = false;
            }
        });
    }

    public void setImageAvatarID(int position) {
        if (!listBitmap.get(position).getImgID().equals("")) {
            avatarId = listBitmap.get(position).getImgID();
            imgAvatar.setImageBitmap(listBitmap.get(position).getBitmap());
        }
    }

    private void setBtnRefreshClicked() {
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRefresh.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                setCurrentAddress();
            }
        });
    }

    public void setCurrentAddress() {
        GetCurrentAddress getCurrentAddress = new GetCurrentAddress();
        getCurrentAddress.execute();
    }

    private void hideKeyBoardOnOutClicked() {
        mainLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                HideKeyBoard.hideSoftKeyboard(getActivity());
                return false;
            }
        });

        btnCreateShop.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                HideKeyBoard.hideSoftKeyboard(getActivity());
                return false;
            }
        });

        gridPhoto.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                HideKeyBoard.hideSoftKeyboard(getActivity());
                return false;
            }
        });
        btnCamera.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                HideKeyBoard.hideSoftKeyboard(getActivity());
                return false;
            }
        });
    }

    private void setBtnCreateShopClicked() {
        btnCreateShop.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 if (LoginActivity.user == null) {
                                                     final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                     builder.setMessage("Bạn cần phải đăng nhập để tạo quán ăn!");
                                                     builder.setPositiveButton("Đăng nhập", new DialogInterface.OnClickListener() {
                                                         @Override
                                                         public void onClick(DialogInterface dialog, int which) {
                                                             startActivity(new Intent(getActivity(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                                             getActivity().finish();
                                                         }
                                                     });
                                                     builder.setNegativeButton("Bỏ qua", new DialogInterface.OnClickListener() {
                                                         @Override
                                                         public void onClick(DialogInterface dialog, int which) {
                                                         }
                                                     });
                                                     builder.show();
                                                 } else if (etAddress.getText().toString().trim().equals("") || etName.getText().toString().trim().equals("") || etDesc.getText().toString().trim().equals("")) {
                                                     Toast.makeText(getActivity(), "Hãy điền đầy đủ thông tin trước nhé!", Toast.LENGTH_SHORT).show();
                                                 } else if (rgGoodBad.getCheckedRadioButtonId() < 0) {
                                                     Toast.makeText(getActivity(), "Bạn chưa đánh giá quán ăn!", Toast.LENGTH_SHORT).show();
                                                 } else if (listBitmap.size() == 0) {
                                                     Toast.makeText(getActivity(), "Bạn chưa chọn ảnh cho quán ăn!", Toast.LENGTH_SHORT).show();
                                                 } else if (avatarId.equals("")) {
                                                     Toast.makeText(getActivity(), "Bạn chưa chọn Avatar cho quán ăn!", Toast.LENGTH_SHORT).show();
                                                 } else if (!checkLoadCompleted(listBitmap)) {
                                                     Toast.makeText(getActivity(), "Có ảnh chưa tải xong!", Toast.LENGTH_SHORT).show();
                                                 } else {
                                                     getActivity().setProgressBarIndeterminateVisibility(true);
                                                     createShop(LoginActivity.user.getUserID(), LoginActivity.user.getToken(), LoginActivity.lang);
                                                 }
                                             }
                                         }

        );
    }

    private boolean checkLoadCompleted(ArrayList<ImageUpload> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getImgID().equals("")) return false;
        }
        return true;
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

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

                alertDialog.setMessage("Get pictures by?");
                alertDialog.setCancelable(true);
                alertDialog.setNegativeButton("With camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }
                });

                alertDialog.setPositiveButton("Choose from library", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), PickMultiPhotoActivity.class);
                        startActivityForResult(intent, PICK_MULTI_PHOTOS);
                    }
                });

                alertDialog.show();

            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == getActivity().RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            File finalFile = GetImageFile.getImageFile(getActivity(), photo);
            Bitmap bitmap = Bitmap.createScaledBitmap(photo, 100, 100, false);
            listBitmap.add(new ImageUpload(bitmap, 0, finalFile));
            uploadImage(finalFile, listBitmap.size() - 1, LoginActivity.user.getUserID(), LoginActivity.user.getToken(), LoginActivity.lang);
            adapter.notifyDataSetChanged();
        }
        if (requestCode == PICK_MULTI_PHOTOS && resultCode == getActivity().RESULT_OK) {


            ArrayList<String> imagePath = data.getExtras().getStringArrayList("data");
            try {
                for (int i = 0; i < imagePath.size(); i++) {
                    File file = new File(imagePath.get(i));
                    Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(imagePath.get(i)), 50, 50, false);
                    listBitmap.add(new ImageUpload(bitmap, 0, file));
                    uploadImage(file, listBitmap.size() - 1, LoginActivity.user.getUserID(), LoginActivity.user.getToken(), LoginActivity.lang);
                }
            } catch (OutOfMemoryError error) {
                Toast.makeText(getActivity(), "Lỗi tràn bộ nhớ, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }

            adapter.notifyDataSetChanged();
        }
    }

    public void uploadImage(final File file, final int index, final String userID, final String token, final String lang) {

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
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Có ảnh upload không thành công! Bạn có muốn thử lại?");
                builder.setPositiveButton("Thử lại", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        uploadImage(file, index, userID, token, lang);
                    }
                });
                builder.setNegativeButton("Bỏ qua", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listBitmap.remove(index);
                        adapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    JSONObject response = jsonObject.getJSONObject(RESPONSE);

                    if (listBitmap.size() > index) {
                        listBitmap.get(index).setProgress(100);
                        listBitmap.get(index).setImgID(response.getString(ID));
                    }
                    adapter.notifyDataSetChanged();

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
        if (listBitmap.size() > 0) {
            for (int i = 0; i < listBitmap.size(); i++) {
                arrayImageId += listBitmap.get(i).getImgID() + ",";
            }
        }
        params.put(FILES, arrayImageId);
        if (isLike) params.put(IS_LIKE, 1);
        if (cbReport.isChecked()) params.put(IS_REPORT, 1);
        if (!isLike) params.put(IS_MAIN, 1);
        params.put(MAIN_FILE_ID, avatarId);


        aClient.post(BASE_URL + SHOP_CREATE, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("JSON", "POST FAIL");
                Toast.makeText(getActivity(), "Lỗi! Vui lòng thử lại!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);

                    final JSONObject response = jsonObject.getJSONObject(RESPONSE);
                    int result = response.getInt(RESULT);
                    Log.e("FILE ID", arrayImageId);
                    final String name = etName.getText().toString().trim();
                    if (result == 1) {
                        getActivity().setProgressBarIndeterminateVisibility(false);

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Tạo thành công! Bạn có muốn xem quán ăn đã tạo không?");
                        builder.setNegativeButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getActivity(), DetailFoodActivity.class);
                                try {
                                    intent.putExtra(ID, response.getString(ID));
                                    intent.putExtra(NAME, name);
                                    startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        builder.setPositiveButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        builder.show();
                        etDesc.setText("");
                        etName.setText("");
                        listBitmap = new ArrayList<>();
                        listBitmap.clear();
                        adapter = new GridPhotoAdapter(listBitmap, getActivity(), AddFragment.this);
                        gridPhoto.setAdapter(adapter);
                        avatarId = "";
                        arrayImageId = "";
                        cbChooseAvatar.setChecked(false);
                        imgAvatar.setImageBitmap(null);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void deleteImage(int position) {
        listBitmap.remove(position);
        adapter.notifyDataSetChanged();

    }

    private class GetCurrentAddress extends AsyncTask {
        private String address;

        @Override
        protected Object doInBackground(Object[] params) {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(getActivity(), Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(HomeActivity.currLat, HomeActivity.currLongth, 1);
                if (addresses.size() > 0) {
                    address = addresses.get(0).getAddressLine(0) + ", " + addresses.get(0).getLocality();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            etAddress.setText(address);
            btnRefresh.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }
}
