package com.example.poojapatel.galleryart;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class Utility {

    public static String bitMapToBase64String(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); // Could be Bitmap.CompressFormat.PNG or Bitmap.CompressFormat.WEBP
        byte[] bai = baos.toByteArray();

        String base64Image = Base64.encodeToString(bai, Base64.DEFAULT);
        return base64Image;
    }

    public static Bitmap base64StringToBitMap(String base64Image){
        byte[] data = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap bm;
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inMutable = true;
        bm = BitmapFactory.decodeByteArray(data, 0, data.length, opt);

        return bm;
    }
}
