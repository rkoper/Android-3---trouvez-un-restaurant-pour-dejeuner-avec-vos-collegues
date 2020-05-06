// Generated code from Butter Knife. Do not modify!
package com.m.sofiane.go4lunch.fragment;

import android.view.MenuItem;
import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.m.sofiane.go4lunch.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MapFragment_ViewBinding implements Unbinder {
  private MapFragment target;

  @UiThread
  public MapFragment_ViewBinding(MapFragment target, View source) {
    this.target = target;

    target.mSearch = Utils.findRequiredViewAsType(source, R.id.menu_search, "field 'mSearch'", MenuItem.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MapFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mSearch = null;
  }
}
