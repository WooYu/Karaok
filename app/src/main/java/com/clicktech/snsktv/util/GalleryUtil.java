package com.clicktech.snsktv.util;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import timber.log.Timber;

/**
 * 对系统图库或相册进行操作
 * Created by cb on 2016/8/24.
 * ref:http://blog.csdn.net/tempersitu/article/details/20557383
 */
public class GalleryUtil {
    /**
     * 4.4以上系统获取相册图片返回路径
     *
     * @param context
     * @param uri
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String selectPathUpKitkat(Context context, Uri uri) {
        Timber.e("----------------- Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT");
        //如果在4.4上面不用"图片"来选,用"图库"来选,就会无法读取到图片路径
        //所以只需要加个判断,如果DocumentsContract.isDocumentUri(context, uri)返回false,用另外方式获取图片路径
        if (DocumentsContract.isDocumentUri(context, uri)) {
            Timber.e("----------------- Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT | 选择的是“图片”进行图片选择");
            if (isExternalStorageDocument(uri)) {
                // ExternalStorageProvider
                Timber.e("------------selectPathUpKitkat | ExternalStorageProvider");
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                // DownloadsProvider
                Timber.e("------------selectPathUpKitkat | DownloadsProvider");
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                // MediaProvider
                Timber.e("------------selectPathUpKitkat | MediaProvider");
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }

            Timber.e("------------selectPathUpKitkat | null");
            return null;
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // MediaStore (and general)
            Timber.e("------------selectPathUpKitkat | MediaStore (and general)");

            if (isGooglePhotosUri(uri)) {
                // Return the remote address
                Timber.e("------------selectPathUpKitkat | Return the remote address");
                return uri.getLastPathSegment();
            }
            Timber.e("------------selectPathUpKitkat | getDataColumn");
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // File
            Timber.e("------------selectPathUpKitkat | MediaStore (File)");
            return uri.getPath();
        } else {
            Timber.e("----------------- Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT | 选择的是“图库”而不是“图片”进行图片选择");
            return selectImageFromGallery(context, uri);
        }
    }

    /**
     * 从“图库”选择的图片路径（如果在4.4上面不用"图片"来选,用"图库"来选,就会无法读取到图片路径）
     *
     * @param context
     * @param selectedImage
     * @return
     */
    public static String selectImageFromGallery(Context context, Uri selectedImage) {
        if (selectedImage != null) {
            String uriStr = selectedImage.toString();
            String path = uriStr.substring(10, uriStr.length());
            if (path.startsWith("com.sec.android.gallery3d")) {
                return null;
            }
        }
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        String picturePath;
        if (cursor == null) {
            picturePath = selectedImage.getPath();
        } else {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
        }
        return picturePath;
    }

    /**
     * 4.4以下系统获取相册图片返回路径
     *
     * @param context
     * @param uri
     * @return
     */
    public static String selectPathDownKitkat(Context context, Uri uri) {
        Timber.e("----------------- Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT");
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            // MediaStore (and general)
            Timber.e("------------selectPathDownKitkat | MediaStore (and general)");

            if (isGooglePhotosUri(uri)) {
                // Return the remote address
                Timber.e("------------selectPathDownKitkat | Return the remote address");
                return uri.getLastPathSegment();
            }
            Timber.e("------------selectPathDownKitkat | getDataColumn");
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // File
            Timber.e("------------selectPathDownKitkat | MediaStore (File)");
            return uri.getPath();
        }
        Timber.e("------------selectPathDownKitkat | null");
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        Timber.e("------------getDataColumn | null");
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
