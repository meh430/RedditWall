// Generated by view binder compiler. Do not edit!
package com.mehul.redditwall.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import com.mehul.redditwall.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class NextWallpaperBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final ImageView refreshWall;

  private NextWallpaperBinding(@NonNull RelativeLayout rootView, @NonNull ImageView refreshWall) {
    this.rootView = rootView;
    this.refreshWall = refreshWall;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static NextWallpaperBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static NextWallpaperBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.next_wallpaper, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static NextWallpaperBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.refresh_wall;
      ImageView refreshWall = rootView.findViewById(id);
      if (refreshWall == null) {
        break missingId;
      }

      return new NextWallpaperBinding((RelativeLayout) rootView, refreshWall);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
