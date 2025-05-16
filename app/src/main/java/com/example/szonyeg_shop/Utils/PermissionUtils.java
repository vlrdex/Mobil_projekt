package com.example.szonyeg_shop.Utils;

import android.content.Context;
import android.content.pm.PackageManager;
import androidx.core.content.ContextCompat;
import android.Manifest; // Fontos: a Manifest osztályt is importálni kell az engedély stringekhez

public class PermissionUtils {

    public static boolean isFineLocationPermissionGranted(Context context) {
        // A ContextCompat.checkSelfPermission segít ellenőrizni az engedélyt
        // függetlenül az Android verziótól.
        int permissionState = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION // Az ellenőrizni kívánt engedély stringje
        );

        // Visszatérünk true-val, ha az engedély állapota PERMISSION_GRANTED
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isCoarseLocationPermissionGranted(Context context) {
        int permissionState = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
        );
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isAnyLocationPermissionGranted(Context context) {
        return isFineLocationPermissionGranted(context) || isCoarseLocationPermissionGranted(context);
    }

}
