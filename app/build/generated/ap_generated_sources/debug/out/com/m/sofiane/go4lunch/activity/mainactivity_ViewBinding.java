// Generated code from Butter Knife. Do not modify!
package com.m.sofiane.go4lunch.activity;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.m.sofiane.go4lunch.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class mainactivity_ViewBinding implements Unbinder {
  private mainactivity target;

  @UiThread
  public mainactivity_ViewBinding(mainactivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public mainactivity_ViewBinding(mainactivity target, View source) {
    this.target = target;

    target.mDrawerLayout = Utils.findRequiredViewAsType(source, R.id.drawer_layout, "field 'mDrawerLayout'", DrawerLayout.class);
    target.mToolbar = Utils.findRequiredViewAsType(source, R.id.activity_main_toolbar, "field 'mToolbar'", Toolbar.class);
    target.mBottomNavigationView = Utils.findRequiredViewAsType(source, R.id.activity_main_bottom_navigation, "field 'mBottomNavigationView'", BottomNavigationView.class);
    target.mNavigationView = Utils.findRequiredViewAsType(source, R.id.activity_main_drawer_isOpen, "field 'mNavigationView'", NavigationView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    mainactivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mDrawerLayout = null;
    target.mToolbar = null;
    target.mBottomNavigationView = null;
    target.mNavigationView = null;
  }
}
