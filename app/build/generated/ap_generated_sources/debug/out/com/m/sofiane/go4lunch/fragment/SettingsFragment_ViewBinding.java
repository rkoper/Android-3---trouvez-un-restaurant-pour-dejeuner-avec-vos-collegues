// Generated code from Butter Knife. Do not modify!
package com.m.sofiane.go4lunch.fragment;

import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TimePicker;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.m.sofiane.go4lunch.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SettingsFragment_ViewBinding implements Unbinder {
  private SettingsFragment target;

  @UiThread
  public SettingsFragment_ViewBinding(SettingsFragment target, View source) {
    this.target = target;

    target.mSwitch = Utils.findRequiredViewAsType(source, R.id.switchNotif, "field 'mSwitch'", Switch.class);
    target.picker = Utils.findRequiredViewAsType(source, R.id.ClockNotif, "field 'picker'", TimePicker.class);
    target.mButtonTime = Utils.findRequiredViewAsType(source, R.id.buttonTime, "field 'mButtonTime'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SettingsFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mSwitch = null;
    target.picker = null;
    target.mButtonTime = null;
  }
}
