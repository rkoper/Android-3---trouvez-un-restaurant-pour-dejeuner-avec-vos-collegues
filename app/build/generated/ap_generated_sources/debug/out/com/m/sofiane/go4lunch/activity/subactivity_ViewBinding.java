// Generated code from Butter Knife. Do not modify!
package com.m.sofiane.go4lunch.activity;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.m.sofiane.go4lunch.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class subactivity_ViewBinding implements Unbinder {
  private subactivity target;

  @UiThread
  public subactivity_ViewBinding(subactivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public subactivity_ViewBinding(subactivity target, View source) {
    this.target = target;

    target.mNameSub = Utils.findRequiredViewAsType(source, R.id.subName, "field 'mNameSub'", TextView.class);
    target.mPhotoSub = Utils.findRequiredViewAsType(source, R.id.SubPhoto, "field 'mPhotoSub'", ImageView.class);
    target.mAdressSub = Utils.findRequiredViewAsType(source, R.id.subAdress, "field 'mAdressSub'", TextView.class);
    target.mCallButton = Utils.findRequiredViewAsType(source, R.id.buttonCall, "field 'mCallButton'", ImageButton.class);
    target.mSiteUrl = Utils.findRequiredViewAsType(source, R.id.buttonworld, "field 'mSiteUrl'", ImageButton.class);
    target.mSiteView = Utils.findRequiredViewAsType(source, R.id.activity_restaurant_web_site_text, "field 'mSiteView'", TextView.class);
    target.mCallView = Utils.findRequiredViewAsType(source, R.id.activity_restaurant_call_text, "field 'mCallView'", TextView.class);
    target.mLike = Utils.findRequiredViewAsType(source, R.id.buttonLike, "field 'mLike'", ImageButton.class);
    target.mRate1 = Utils.findRequiredViewAsType(source, R.id.placeSub_rating_icon1, "field 'mRate1'", ImageView.class);
    target.mRate2 = Utils.findRequiredViewAsType(source, R.id.placeSub_rating_icon2, "field 'mRate2'", ImageView.class);
    target.mRate3 = Utils.findRequiredViewAsType(source, R.id.placeSub_rating_icon3, "field 'mRate3'", ImageView.class);
    target.mActionButton = Utils.findRequiredViewAsType(source, R.id.floatingActionButton, "field 'mActionButton'", FloatingActionButton.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    subactivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mNameSub = null;
    target.mPhotoSub = null;
    target.mAdressSub = null;
    target.mCallButton = null;
    target.mSiteUrl = null;
    target.mSiteView = null;
    target.mCallView = null;
    target.mLike = null;
    target.mRate1 = null;
    target.mRate2 = null;
    target.mRate3 = null;
    target.mActionButton = null;
  }
}
