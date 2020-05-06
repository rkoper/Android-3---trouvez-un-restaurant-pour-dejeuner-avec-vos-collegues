// Generated code from Butter Knife. Do not modify!
package com.m.sofiane.go4lunch.fragment;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.m.sofiane.go4lunch.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ListFragment_ViewBinding implements Unbinder {
  private ListFragment target;

  @UiThread
  public ListFragment_ViewBinding(ListFragment target, View source) {
    this.target = target;

    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.List_recyclerView, "field 'recyclerView'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ListFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.recyclerView = null;
  }
}
