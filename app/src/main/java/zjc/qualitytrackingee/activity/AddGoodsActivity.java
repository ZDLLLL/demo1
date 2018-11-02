package zjc.qualitytrackingee.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.view.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import zjc.qualitytrackingee.MyApplication;
import zjc.qualitytrackingee.R;
import zjc.qualitytrackingee.adapter.ImagePickerAdapter;
import zjc.qualitytrackingee.internet.Net;
import zjc.qualitytrackingee.utils.Common;
import zjc.qualitytrackingee.utils.GlideImageLoader;
import zjc.qualitytrackingee.utils.SelectDialog;
import zjc.qualitytrackingee.utils.VolleyInterface;
import zjc.qualitytrackingee.utils.VolleyRequest;

import static com.mob.tools.utils.DeviceHelper.getApplication;
import static zjc.qualitytrackingee.MyApplication.getContext;

public class AddGoodsActivity extends AppCompatActivity implements ImagePickerAdapter.OnRecyclerViewItemClickListener{
    private EditText goods_name_et;
    private EditText goods_price_et;
    private EditText goods_class_et;
    private EditText goods_introduction_et;
    private TextView goods_confirm_tv;
    private static Bitmap bmp;
    private static Bitmap bm;
    private SharedPreferences pref;
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    private ImagePickerAdapter adapter;
    private GoodsClassListActivity goodsClassListActivity;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 1;//允许选择图片最大数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goods);
        pref= PreferenceManager.getDefaultSharedPreferences(getApplication());
        initView();
        initImagePicker();
        initWidget();
    }
    public void initView(){
        goods_name_et=findViewById(R.id.goods_name_et);
        goods_price_et=findViewById(R.id.goods_price_et);
        goods_class_et=findViewById(R.id.goods_class_et);
        goods_introduction_et=findViewById(R.id.goods_introduction_et);
        goods_confirm_tv=findViewById(R.id.goods_confirm_tv);

        goods_confirm_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String image=selImageList.get(0).path;
                File file=new File(image);
                if(file.exists()) {
                    bmp = BitmapFactory.decodeFile(image);
                    Log.v("图片的字节流",bmp.toString());
                    Toast.makeText(AddGoodsActivity.this,"图片路径"+bmp,Toast.LENGTH_LONG).show();

                }
                Matrix matrix = new Matrix();
                matrix.setScale(0.5f, 0.5f);
                bm = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
                        bmp.getHeight(), matrix, true);
                Toast.makeText(AddGoodsActivity.this,"图片路径"+bm.toString(),Toast.LENGTH_LONG).show();
                uploaddata();


            }
        });
        goods_class_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AddGoodsActivity.this,GoodsClassListActivity.class);
                intent.putExtra("co_name",goods_name_et.getText().toString());
                intent.putExtra("co_price",goods_price_et.getText().toString());
//                intent.putExtra("co_describe",goods_introduction_et.getText().toString());
//                intent.putExtra("co_")
//                goods_name_et.setText("");
//                goods_price_et.setText("");
                startActivity(intent);
                //getActivity().finish();
            }
        });
        goods_name_et.setText(getIntent().getStringExtra("co_name"));
        goods_class_et.setText(getIntent().getStringExtra("co_class"));
        goods_price_et.setText(getIntent().getStringExtra("co_price"));

    }
    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(true);                            //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(maxImgCount);              //选中数量限制
        imagePicker.setMultiMode(false);                      //多选
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
//        imagePicker.setOutPutX(10);                         //保存文件的宽度。单位像素
//        imagePicker.setOutPutY(10);                         //保存文件的高度。单位像素
        imagePicker.setOutPutX(200);
        imagePicker.setOutPutY(200);

    }
    private SelectDialog showDialog(SelectDialog.SelectDialogListener listener, List<String> names) {
        SelectDialog dialog = new SelectDialog(this, R.style.transparentFrameWindowStyle, listener, names);
        if (!this.isFinishing()) {
            dialog.show();
        }
        return dialog;
    }
    private void initWidget() {
        RecyclerView goods_rv =findViewById(R.id.goods_rv);
        selImageList = new ArrayList<>();
        adapter = new ImagePickerAdapter(this, selImageList, maxImgCount);
        adapter.setOnItemClickListener(this);

        goods_rv.setLayoutManager(new GridLayoutManager(this, 4));
        goods_rv.setHasFixedSize(true);
        goods_rv.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            case IMAGE_ITEM_ADD:
                List<String> names = new ArrayList<>();
                names.add("拍照");
                names.add("相册");
                showDialog(new SelectDialog.SelectDialogListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0: // 直接调起相机
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent = new Intent(AddGoodsActivity.this, ImageGridActivity.class);
                                intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS,true); // 是否是直接打开相机
                                startActivityForResult(intent, REQUEST_CODE_SELECT);
                                break;
                            case 1:
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent1 = new Intent(AddGoodsActivity.this, ImageGridActivity.class);
                                startActivityForResult(intent1, REQUEST_CODE_SELECT);
                                break;
                            default:
                                break;
                        }
                    }
                }, names);
                break;
            default:
                //打开预览
                Intent intentPreview = new Intent(AddGoodsActivity.this, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS,true);
                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                break;
        }
    }
    public void uploaddata(){
        String name=goods_name_et.getText().toString();
        String price=goods_price_et.getText().toString();
        String introduction=goods_introduction_et.getText().toString();
        String coclassid=goodsClassListActivity.coc_classid;
        //String phone= MyApplication.getE_phone();
        String pic=bitmapToString(bm);
        String pic1=bitmapToString(bmp);
        String phone=pref.getString("e_phone","");
        String c_id= MyApplication.getC_id();
        String url= Net.UploadGoods+"?c_id="+c_id+"&co_name="+name+"&co_price="+price+"&co_describe="+introduction+"&co_classid="+coclassid+"&co_img="+ Common.Bitmap2String(bmp);
        VolleyRequest.RequestGet(getContext(),url,"uploadgoods",
                new VolleyInterface(getContext(), VolleyInterface.mListener,VolleyInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(String result) {
                        // getActivity().finish();
                        Toast.makeText(AddGoodsActivity.this,"商品上传成功",Toast.LENGTH_LONG).show();


                    }

                    @Override
                    public void onMyError(VolleyError error) {

                    }
                });

    }
    public String bitmapToString(Bitmap bitmap){
        //将Bitmap转换成字符串
        String string=null;
        ByteArrayOutputStream bStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,bStream);
        byte[]bytes=bStream.toByteArray();
        string= Base64.encodeToString(bytes, Base64.DEFAULT);
        return string;
    }
}
