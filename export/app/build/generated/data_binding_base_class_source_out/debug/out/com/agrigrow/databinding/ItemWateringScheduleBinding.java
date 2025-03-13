// Generated by view binder compiler. Do not edit!
package com.agrigrow.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.agrigrow.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ItemWateringScheduleBinding implements ViewBinding {
  @NonNull
  private final CardView rootView;

  @NonNull
  public final ImageView imageViewDroplet;

  @NonNull
  public final ImageView imageViewPlantIcon;

  @NonNull
  public final TextView textViewPlantType;

  @NonNull
  public final TextView textViewWateringDay;

  @NonNull
  public final View viewIndicator;

  private ItemWateringScheduleBinding(@NonNull CardView rootView,
      @NonNull ImageView imageViewDroplet, @NonNull ImageView imageViewPlantIcon,
      @NonNull TextView textViewPlantType, @NonNull TextView textViewWateringDay,
      @NonNull View viewIndicator) {
    this.rootView = rootView;
    this.imageViewDroplet = imageViewDroplet;
    this.imageViewPlantIcon = imageViewPlantIcon;
    this.textViewPlantType = textViewPlantType;
    this.textViewWateringDay = textViewWateringDay;
    this.viewIndicator = viewIndicator;
  }

  @Override
  @NonNull
  public CardView getRoot() {
    return rootView;
  }

  @NonNull
  public static ItemWateringScheduleBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ItemWateringScheduleBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.item_watering_schedule, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ItemWateringScheduleBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.imageViewDroplet;
      ImageView imageViewDroplet = ViewBindings.findChildViewById(rootView, id);
      if (imageViewDroplet == null) {
        break missingId;
      }

      id = R.id.imageViewPlantIcon;
      ImageView imageViewPlantIcon = ViewBindings.findChildViewById(rootView, id);
      if (imageViewPlantIcon == null) {
        break missingId;
      }

      id = R.id.textViewPlantType;
      TextView textViewPlantType = ViewBindings.findChildViewById(rootView, id);
      if (textViewPlantType == null) {
        break missingId;
      }

      id = R.id.textViewWateringDay;
      TextView textViewWateringDay = ViewBindings.findChildViewById(rootView, id);
      if (textViewWateringDay == null) {
        break missingId;
      }

      id = R.id.viewIndicator;
      View viewIndicator = ViewBindings.findChildViewById(rootView, id);
      if (viewIndicator == null) {
        break missingId;
      }

      return new ItemWateringScheduleBinding((CardView) rootView, imageViewDroplet,
          imageViewPlantIcon, textViewPlantType, textViewWateringDay, viewIndicator);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
