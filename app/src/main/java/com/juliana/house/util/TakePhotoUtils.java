package com.juliana.house.util;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * 拍照或者从相册获取照片、裁剪等操作工具类
 *
 * @author
 * @version 1.0
 * @date
 */
public class TakePhotoUtils {
    /** 拍照 */
    public static final int TAKE_PHOTO = 100;
    /** 从相册取 */
    public static final int CHOOSE_PICTURE = 101;
    /** 裁剪大图 */
    public static final int CROP_BIG_PICTURE = 102;
    /** 裁剪小图 */
    public static final int CROP_SMALL_PICTURE = 103;

    /** 压缩的原图 */
    public static final int CROP_ORIGAL_PICTURE = 104;

    private static final String FILE_CONTENT_FILEPROVIDER = "com.juliana.house.fileprovider";
    private static TakePhotoUtils mInstance;
    /** 存放照片uri */
    public File mImageFile;

    public TakePhotoUtils() {
        // 设置图片路径
        String _imageDir = "temp.jpg";
        mImageFile = new File(Environment.getExternalStorageDirectory(), _imageDir);
    }

    public static TakePhotoUtils getInstance() {
        if (mInstance == null) {
            mInstance = new TakePhotoUtils();
        }
        return mInstance;
    }

    /**
     * 通过uri生成Bitmap
     *
     * @param context
     * @param uri
     * @return
     */
    public Bitmap decodeUriAsBitmap(Context context, Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    /**
     * 拍照 TAKE_PHOTO
     */
    public void takePhoto(Context context) {
        takePhoto(context,TAKE_PHOTO);
    }

    /**
     * 拍照 TAKE_PHOTO
     */
    public void takePhoto(Context context, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!PermissionsUtils.startRequestPermission((Activity) context, PermissionsUtils.CAMERA_PERMISSIONS, 1)) {
                return;
            }
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  // "android.media.action.IMAGE_CAPTURE"
        intent.addCategory("android.intent.category.DEFAULT");
        //Android7.0以上URI
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //通过FileProvider创建一个content类型的Uri
            Uri uri = FileProvider.getUriForFile(context, FILE_CONTENT_FILEPROVIDER, mImageFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mImageFile));
        }
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    /**
     * 拍照
     */
    public void takePhoto(Fragment fragment) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!PermissionsUtils.startRequestPermission(fragment.getActivity(), PermissionsUtils.CAMERA_PERMISSIONS, 1)) {
                return;
            }
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  // "android.media.action.IMAGE_CAPTURE"
        intent.addCategory("android.intent.category.DEFAULT");
        //Android7.0以上URI
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //通过FileProvider创建一个content类型的Uri
            Uri uri = FileProvider.getUriForFile(fragment.getContext(), FILE_CONTENT_FILEPROVIDER, mImageFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mImageFile));
        }
        fragment.startActivityForResult(intent, TAKE_PHOTO);
    }


    /**
     * 从相册获取
     */
    public void pickPhoto(Context context, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!PermissionsUtils.startRequestPermission((Activity) context, PermissionsUtils.WRITE_EXTERNAL_STORAGE_PERMISSIONS, 1)) {
                return;
            }
        }
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        // 开启一个带有返回值的Activity，请求码为PHOTO_ZOOM
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    /**
     * 从相册获取
     */
    public void pickPhoto(Context context) {
        pickPhoto( context, CHOOSE_PICTURE );
    }

    /**
     * 从相册获取
     */
    public void pickPhoto(Fragment fragment) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!PermissionsUtils.startRequestPermission(fragment.getActivity(),
                    PermissionsUtils.WRITE_EXTERNAL_STORAGE_PERMISSIONS, 1)) {
                return;
            }
        }
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        // 开启一个带有返回值的Activity，请求码为PHOTO_ZOOM
        fragment.startActivityForResult(intent, CHOOSE_PICTURE);
    }

    /**
     * 裁剪图片，使用uri
     * 默认高宽比例1:1，自定义高宽尺寸
     */
    public void cropImageUri(Context context, Fragment fragment, Uri uri, int outputX, int outputY, int requestCode) {
        Intent intent = setCropConfig(context, uri, outputX, outputY);
        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * 设置裁剪配置
     *
     * @param context
     * @param uri
     * @param outputX
     * @param outputY
     * @return
     */
    private Intent setCropConfig(Context context, Uri uri, int outputX, int outputY) {
        Uri imageUri;
        Uri outputUri = null;
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            String url = PhotoUtils.getPath(context, uri);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            {
                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                //通过FileProvider创建一个content类型的Uri
                imageUri = FileProvider.getUriForFile(context, FILE_CONTENT_FILEPROVIDER, new File(url));
                outputUri = Uri.fromFile(mImageFile);
                //TODO:outputUri不需要ContentUri,否则失败
                //outputUri = FileProvider.getUriForFile(activity, "com.solux.furniture.fileprovider", new File(crop_image));
            } else
            {
                imageUri = uri;
                outputUri = Uri.fromFile(mImageFile);
            }
            intent.setDataAndType(imageUri, "image/*");
        } else {
            intent.setDataAndType(uri, "image/*");
            outputUri = Uri.fromFile(mImageFile);
        }
        //        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 4);
        intent.putExtra("aspectY", 5);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);

        intent.putExtra("scale", true);
        // 切图大小不足输出，无黑框
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        return intent;
    }

    /**
     * 裁剪图片，使用uri
     * 默认高宽比例1:1，自定义高宽尺寸
     */
    public void cropImageUri(Context context, Uri uri, int outputX, int outputY, int requestCode) {
        Intent intent = setCropConfig(context, uri, outputX, outputY);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    /**
     * 从相册获取后裁剪小图，不使用uri
     * 默认比例1:1，自定义高宽尺寸
     */
    public void cropSmallPicture(Context context, int outputX, int outputY) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);

        intent.putExtra("scale", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);  // 小图不返回数据
        // 开启一个带有返回值的Activity，请求码为CROP_SMALL_PICTURE
        ((Activity) context).startActivityForResult(intent, CROP_SMALL_PICTURE);
    }
}
