// Generated code from Butter Knife. Do not modify!
package com.m.sofiane.go4lunch.adapter;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.m.sofiane.go4lunch.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ListAdapter$ViewHolder_ViewBinding implements Unbinder {
  private ListAdapter.ViewHolder target;

  @UiThread
  public ListAdapter$ViewHolder_ViewBinding(ListAdapter.ViewHolder target, View source) {
    this.target = target;

    target.R_name = Utils.findRequiredViewAsType(source, R.id.place_name, "field 'R_name'", TextView.class);
    target.R_adress = Utils.findRequiredViewAsType(source, R.id.place_address, "field 'R_adress'", TextView.class);
    target.R_photo = Utils.findRequiredViewAsType(source, R.id.place_photo, "field 'R_photo'", ImageView.class);
    target.R_rate1 = Utils.findRequiredViewAsType(source, R.id.place_rating_icon1, "field 'R_rate1'", ImageView.class);
    target.R_rate2 = Utils.findRequiredViewAsType(source, R.id.place_rating_icon2, "field 'R_rate2'", ImageView.class);
    target.R_rate3 = Utils.findRequiredViewAsType(source, R.id.place_rating_icon3, "field 'R_rate3'", ImageView.class);
    target.R_rateTxt = Utils.findRequiredViewAsType(source, R.id.RateTxt, "field 'R_rateTxt'", TextView.class);
    target.R_prox = Utils.findRequiredViewAsType(source, R.id.place_distance, "field 'R_prox'", TextView.class);
    target.R_openhour = Utils.findRequiredViewAsType(source, R.id.place_open, "field 'R_openhour'", TextView.class);
    target.R_button = Utils.findRequiredViewAsType(source, R.id.buttonForClick, "field 'R_button'", ImageButton.class);
    target.R_place_people_count = Utils.findRequiredViewAsType(source, R.id.place_people_count, "field 'R_place_people_count'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ListAdapter.ViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.R_name = null;
    target.R_adress = null;
    target.R_photo = null;
    target.R_rate1 = null;
    target.R_rate2 = null;
    target.R_rate3 = null;
    target.R_rateTxt = null;
    target.R_prox = null;
    target.R_openhour = null;
    target.R_button = null;
    target.R_place_people_count = null;
  }
}
