package paditech.com.fifood_android;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import Constant.ImageLoaderConfig;
import Object.Comment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import Adapter.ListCommentAdapter;
import Adapter.ViewPagerAdapter;
import Constant.Constant;
import cz.msebera.android.httpclient.Header;
import Constant.ExpandableHeightListView;
import Constant.GetImageFile;
import Constant.HideKeyBoard;

/**
 * Created by USER on 14/4/2016.
 */
public class DetailFoodActivity extends Activity implements Constant {
    private static int CAMERA_REQUEST = 999;
    private static int PICK_IMAGE_REQUEST = 777;
    private String shopID;
    private View mainLayout;
    private View btnShowMap, btnCamera, btnPost;
    private RadioGroup rgBadGood;
    private ImageView imgMain;
    private TextView tvName, tvAddress, tvDistance, tvImgCount, tvGoodNumb, tvBadNumb;
    private CheckBox cbReport;
    private RatingBar ratingBar;
    private ViewPager pager;
    private ExpandableHeightListView lvComment;
    private EditText etComment;

    private String detailResponse;
    private int imagesResponseID = -1;
    private Bitmap commentBitmap;

    private ArrayList<Comment> listComment;
    private ListCommentAdapter adapter;
    private View scrollView;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setProgressBarIndeterminateVisibility(true);
        setContentView(R.layout.activity_detail_food);

        Bundle bundle = getIntent().getExtras();
        shopID = bundle.getString(ID);
        String name = bundle.getString(NAME);

        ActionBar actionBar = getActionBar();

        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorBgMenu)));
        actionBar.setTitle(Html.fromHtml("<b>" + name + "</b>"));

        init();

    }

    private void init() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getShopDetail(LoginActivity.lang);
        getListImageFood(LoginActivity.lang);
        mainLayout = findViewById(R.id.mainLayout);
        imgMain = (ImageView) findViewById(R.id.imgMain);
        tvName = (TextView) findViewById(R.id.tvName);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvDistance = (TextView) findViewById(R.id.tvDistance);
        tvImgCount = (TextView) findViewById(R.id.tvShopID);
        tvGoodNumb = (TextView) findViewById(R.id.tvGoodCount);
        tvBadNumb = (TextView) findViewById(R.id.tvBadCount);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        pager = (ViewPager) findViewById(R.id.pager);
        lvComment = (ExpandableHeightListView) findViewById(R.id.lvComment);
        btnShowMap = findViewById(R.id.btnShowMap);
        btnCamera = findViewById(R.id.btnCamera);
        btnPost = findViewById(R.id.btnPost);
        etComment = (EditText) findViewById(R.id.etComment);
        rgBadGood = (RadioGroup) findViewById(R.id.rgBadGood);
        cbReport = (CheckBox) findViewById(R.id.cbReport);
        scrollView = findViewById(R.id.scrollView);

        setBtnShowMapClicked();
        btnCameraClicked();
        setBtnPostClicked();

        hideKeyBoard();
        adapter = new ListCommentAdapter(listComment, DetailFoodActivity.this);
        lvComment.setAdapter(adapter);
        lvComment.setExpanded(true);
    }

    private void hideKeyBoard() {
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                HideKeyBoard.hideSoftKeyboard(DetailFoodActivity.this);
                return false;
            }
        });

        lvComment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                HideKeyBoard.hideSoftKeyboard(DetailFoodActivity.this);
                return false;
            }
        });

        pager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                HideKeyBoard.hideSoftKeyboard(DetailFoodActivity.this);
                return false;
            }
        });
    }


    private void setBtnPostClicked() {
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideKeyBoard.hideSoftKeyboard(DetailFoodActivity.this);
                if(LoginActivity.user==null){
                    final AlertDialog.Builder builder=new AlertDialog.Builder(DetailFoodActivity.this);
                    builder.setMessage("Bạn cần phải đăng nhập để bình luận!");
                    builder.setPositiveButton("Đăng nhập", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(DetailFoodActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finish();
                        }
                    });
                    builder.setNegativeButton("Bỏ qua", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.show();
                }else
                    if(etComment.getText().toString().trim().equals("")){
                        Toast.makeText(DetailFoodActivity.this, "Bạn chưa nhập bình luận!", Toast.LENGTH_SHORT).show();
                    }else {
                        createComment(LoginActivity.lang);
                    }

            }
        });
    }

    private void btnCameraClicked() {
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(DetailFoodActivity.this);

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
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                    }
                });

                alertDialog.show();



            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                commentBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                File file = GetImageFile.getImageFile(this, commentBitmap);

                setProgressBarIndeterminateVisibility(true);

                uploadImage(file, LoginActivity.user.getUserID(), LoginActivity.user.getToken(), LoginActivity.lang);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            File finalFile = GetImageFile.getImageFile(this ,photo);

            uploadImage(finalFile, LoginActivity.user.getUserID(), LoginActivity.user.getToken(), LoginActivity.lang);
        }


    }


    private void uploadImage(File file, final String userID, final String token, final String lang) {

        final ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Loading");
        progress.show();
        AsyncHttpClient aClient = new AsyncHttpClient();
        aClient.addHeader(KEY_X_API_TOKEN, API_TOCKEN);
        aClient.addHeader(KEY_X_APP_PLATFORM, PLATFORM);
        aClient.addHeader(KEY_X_APP_VERSION, VERSION);
        aClient.addHeader(KEY_X_LOCATE, LOCATE);
        aClient.addHeader(KEY_X_UID, UID);

        final RequestParams params = new RequestParams();
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

                    Log.e("IMAGE UPLOAD", responseString);
                    JSONObject jsonObject = new JSONObject(responseString);

                    JSONObject response = jsonObject.getJSONObject(RESPONSE);

                    imagesResponseID = response.getInt(ID);
                    setProgressBarIndeterminateVisibility(false);

                    progress.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void getShopDetail(String lang) {
        listComment=new ArrayList<>();
        AsyncHttpClient aClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put(LANG, lang);
        params.put(LAT, HomeActivity.currLat);
        params.put(SHOP_ID, shopID);
        params.put(LONGTH, HomeActivity.currLongth);

        aClient.post(BASE_URL + SHOP_DETAIL, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("JSON", "POST FAIL");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                detailResponse = responseString;
                Log.e("JSON", responseString + "");
                try {
                    JSONObject jsonObject = new JSONObject(responseString);

                    JSONObject response = jsonObject.getJSONObject(RESPONSE);

                    tvName.setText(response.getString(NAME));
                    tvAddress.setText(response.getString(ADDRESS));
                    tvDistance.setText((int) response.getDouble(DISTANCE) + " m");
                    tvGoodNumb.setText(response.getString(LIKE_NUMB));
                    tvBadNumb.setText(response.getString(DISLIKE_NUMB));
                    ratingBar.setRating((int) response.getDouble(RATING));

                    ImageLoaderConfig.imageLoader.displayImage(response.getJSONObject(FILE).getString(URL), imgMain, ImageLoaderConfig.options);


                    JSONArray comments = response.getJSONArray(COMMENTS);

                    for (int i = 0; i < comments.length(); i++) {
                        Comment comment = new Comment();
                        comment.setContent(comments.getJSONObject(i).getString(CONTENT));
                        if(comments.getJSONObject(i).getJSONArray(FILES).length()>0)
                            comment.setImgUrl(comments.getJSONObject(i).getJSONArray(FILES).getJSONObject(0).getString(URL));
                        comment.setIsLike(comments.getJSONObject(i).getInt(IS_LIKE));
                        comment.setIsMain(comments.getJSONObject(i).getInt(IS_MAIN));
                        comment.setIsReport(comments.getJSONObject(i).getInt(IS_REPORT));
                        comment.setNickname(comments.getJSONObject(i).getString(NICKNAME));
                        comment.setUserProfifeImage(comments.getJSONObject(i).getString(USER_PROFILE_IMAGE));
                        listComment.add(comment);
                    }
                    adapter=new ListCommentAdapter(listComment,DetailFoodActivity.this);
                    lvComment.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                setProgressBarIndeterminateVisibility(false);
                mainLayout.setVisibility(View.GONE);

            }
        });
    }

    private void setBtnShowMapClicked() {
        btnShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailFoodActivity.this, ShowMapFoodActivity.class);
                intent.putExtra(DETAIL_RESPONSE, detailResponse);
                startActivity(intent);
            }
        });
    }

    private void getListImageFood(String lang) {

        AsyncHttpClient aClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put(LANG, lang);
        params.put(SHOP_ID, shopID);

        aClient.post(BASE_URL + SHOP_IMAGE, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("JSON", "POST FAIL");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.e("JSON", responseString + "");
                try {
                    JSONObject jsonObject = new JSONObject(responseString);

                    JSONObject response = jsonObject.getJSONObject(RESPONSE);

                    JSONArray imgs = response.getJSONArray(IMAGES);

                    setImageViewpager(imgs);

                    tvImgCount.setText(imgs.length() + "");


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }

    private void setImageViewpager(JSONArray a) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(a, this);
        pager.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void createComment(String lang) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        final AsyncHttpClient aClient = new AsyncHttpClient();
        aClient.addHeader(KEY_X_API_TOKEN, API_TOCKEN);
        aClient.addHeader(KEY_X_APP_PLATFORM, PLATFORM);
        aClient.addHeader(KEY_X_APP_VERSION, VERSION);
        aClient.addHeader(KEY_X_LOCATE, LOCATE);
        aClient.addHeader(KEY_X_UID, UID);
        RequestParams params = new RequestParams();
        params.put(LANG, lang);
        params.put(SHOP_ID, shopID);
        params.put(USER_ID, LoginActivity.user.getUserID());
        params.put(TOKEN, LoginActivity.user.getToken());
        params.put(CONTENT, etComment.getText().toString().trim());
        if(imagesResponseID>0) params.put(FILES, imagesResponseID);


        if (rgBadGood.getCheckedRadioButtonId() == R.id.rbGood) params.put(IS_LIKE, 1);
        if(rgBadGood.getCheckedRadioButtonId() == R.id.rbBad) params.put(IS_MAIN, 1);
        if (cbReport.isChecked()) params.put(IS_REPORT, 1);


        aClient.post(BASE_URL + COMMENT, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("JSON", "POST FAIL");
                progressDialog.dismiss();
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.e("JSON", responseString + "");
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    JSONObject response = jsonObject.getJSONObject(RESPONSE);
                    if (response.getString(RATING) != null) {
                        getShopDetail(LoginActivity.lang);
                        progressDialog.dismiss();
                        imagesResponseID = -1;
                        lvComment.setSelection(listComment.size()-1);
                        etComment.setText("");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }


            }
        });
    }
}
