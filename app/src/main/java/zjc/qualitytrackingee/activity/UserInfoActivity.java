package zjc.qualitytrackingee.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zjc.qualitytrackingee.BuildConfig;
import zjc.qualitytrackingee.MyApplication;
import zjc.qualitytrackingee.R;
import zjc.qualitytrackingee.internet.Net;
import zjc.qualitytrackingee.utils.FileUtil;
import zjc.qualitytrackingee.utils.VolleyInterface;
import zjc.qualitytrackingee.utils.VolleyRequest;

import static zjc.qualitytrackingee.MyApplication.getContext;

public class UserInfoActivity extends AppCompatActivity {
    public static String image;
    @BindView(R.id.user_info_back_ll)
    LinearLayout user_info_back_ll;
    @BindView(R.id.user_info_company_tv)
    TextView user_info_company_tv;
    @BindView(R.id.user_info_job_tv)
    TextView user_info_job_tv;
    @BindView(R.id.user_info_name_tv)
    TextView user_info_name_tv;
    @BindView(R.id.user_info_phone_tv)
    TextView user_info_phone_tv;
    @BindView(R.id.user_info_image_iv)
    ImageView user_info_image_iv;
    @BindView(R.id.user_info_save_tv)
    TextView user_info_save_tv;
    @BindView(R.id.userPhoneLayout)RelativeLayout userPhoneLayout;
    //相册请求码
    private static final int ALBUM_REQUEST_CODE = 1;
    //相机请求码
    private static final int CAMERA_REQUEST_CODE = 2;
    //剪裁请求码
    private static final int CROP_REQUEST_CODE = 3;
    private File tempFile;
    //public ImageView headImage1;
    private RelativeLayout userImageLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        image = getIntent().getStringExtra("image");
        MyApplication.addDestoryActivity(UserInfoActivity.this,"UserInfoActivity");
        user_info_image_iv = findViewById(R.id.user_info_image_iv);
        userImageLayout = findViewById(R.id.userImageLayout);
        userImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadHeadImage();
            }
        });
        getUser();

    }

    @OnClick(R.id.user_info_back_ll)
    public void user_info_back_ll_Onclick() {
        finish();
    }

    @OnClick(R.id.userPhoneLayout)
    public void userPhoneLayout_Onclick(){
            Intent intent=new Intent(UserInfoActivity.this,ChangePhoneActivity.class);
            startActivity(intent);

    }
    public void getUser() {
        String url = Net.GetUser + "?e_phone=" + MyApplication.getE_phone();
        VolleyRequest.RequestGet(getContext(), url, "getuser",
                new VolleyInterface(getContext(), VolleyInterface.mListener, VolleyInterface.mErrorListener) {
                    @Override
                    public void onMySuccess(String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            JSONObject jsonObject1 = jsonObject.getJSONObject("personInfo");
                            String e_name = jsonObject1.getString("e_name");
                            String e_img = jsonObject1.getString("e_img");
                            String e_job = jsonObject1.getString("j_name");
                            String e_conpamy = jsonObject1.getString("c_name");
                            image = e_img.replaceAll("\\\\", "");
                            user_info_phone_tv.setText(MyApplication.getE_phone());
                            user_info_job_tv.setText(e_job);
                            user_info_company_tv.setText(e_conpamy);
                            Glide.with(getContext())
                                    .load(image)
                                    .asBitmap()
                                    .error(R.drawable.head1)
                                    .into(user_info_image_iv);
                            user_info_name_tv.setText(e_name);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onMyError(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                        Toast.makeText(getContext(), "╮(╯▽╰)╭连接不上了", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @OnClick(R.id.user_info_save_tv)
    public void user_info_save_tv_Onclick() {
        //uploadMessage();
        finish();
    }

    public void uploadMessage() {
        String url = Net.UploadMessage + "?e_phone=" + MyApplication.getE_phone();
        VolleyRequest.RequestGet(getContext(), url, "uploadmessage",
                new VolleyInterface(getContext(), VolleyInterface.mListener, VolleyInterface.mErrorListener) {
                    @Override
                    public void onMySuccess(String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            JSONObject jsonObject1 = jsonObject.getJSONObject("personInfo");
                            String e_name = jsonObject1.getString("e_name");
                            String e_img = jsonObject1.getString("e_img");
                            String e_job = jsonObject1.getString("e_job");
                            String e_conpamy = jsonObject1.getString("e_conpamy");
                            image = e_img.replaceAll("\\\\", "");
                            user_info_phone_tv.setText(MyApplication.getE_phone());
                            user_info_job_tv.setText(e_job);
                            user_info_company_tv.setText(e_conpamy);
                            Glide.with(getContext())
                                    .load(image)
                                    .asBitmap()
                                    .error(R.drawable.head1)
                                    .into(user_info_image_iv);
                            user_info_name_tv.setText(e_name);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onMyError(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                        Toast.makeText(getContext(), "╮(╯▽╰)╭连接不上了", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadHeadImage() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_popupwindow, null);
        TextView btnCarema = view.findViewById(R.id.btn_camera);
        TextView btnPhoto = view.findViewById(R.id.btn_photo);
        TextView btnCancel = view.findViewById(R.id.btn_cancel);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
        popupWindow.setOutsideTouchable(true);
        View parent = LayoutInflater.from(this).inflate(R.layout.activity_user_info, null);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        //popupWindow在弹窗的时候背景半透明
        final WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.5f;
        getWindow().setAttributes(params);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params.alpha = 1.0f;
                getWindow().setAttributes(params);
            }
        });
        btnCarema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //权限判断

                //跳转到调用系统相机
                getPicFromCamera();

                popupWindow.dismiss();
            }

        });
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //权限判断
                getPicFromAlbm();
                popupWindow.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }


    /**
     * 从相机获取图片
     */
    private void getPicFromCamera() {
        //用于保存调用相机拍照后所生成的文件
        tempFile = new File(Environment.getExternalStorageDirectory().getPath(), System.currentTimeMillis() + ".jpg");
        //跳转到调用系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {   //如果在Android7.0以上,使用FileProvider获取Uri
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(UserInfoActivity.this, "zjc.qualitytrackingee", tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
            Log.e("dasd", contentUri.toString());
        } else {    //否则使用Uri.fromFile(file)方法获取Uri
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        }
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    /**
     * 从相册获取图片
     */
    private void getPicFromAlbm() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, ALBUM_REQUEST_CODE);
    }


    /**
     * 裁剪图片
     */
    private void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        //打开image类型的文件
        intent.setDataAndType(uri, "image/*");
        //下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        //是否将数据保存在Bitmap中返回
        intent.putExtra("return-data", true);
        //    imageUri=uri.toString();
        startActivityForResult(intent, CROP_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:   //调用相机后返回
                if (resultCode == RESULT_OK) {
                    //用相机返回的照片去调用剪裁也需要对Uri进行处理
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Uri contentUri = FileProvider.getUriForFile(UserInfoActivity.this, "zjc.qualitytrackingee", tempFile);
                        cropPhoto(contentUri);
                    } else {
                        cropPhoto(Uri.fromFile(tempFile));
                    }
                }
                break;
            case ALBUM_REQUEST_CODE:    //调用相册后返回
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    cropPhoto(uri);
                }
                break;
            case CROP_REQUEST_CODE:     //调用剪裁后返回
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    //在这里获得了剪裁后的Bitmap对象，可以用于上传
                    Bitmap image = bundle.getParcelable("data");
                    //设置到ImageView上
                    user_info_image_iv.setImageBitmap(image);
                    //也可以进行一些保存、压缩等操作后上传
//                    String path = saveImage("crop", image);
                }
                break;
        }
    }
}



