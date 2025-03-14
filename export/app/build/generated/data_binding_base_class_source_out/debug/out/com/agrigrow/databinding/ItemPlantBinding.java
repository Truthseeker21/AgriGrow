// Generated by view binder compiler. Do not edit!
package com.agrigrow.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.agrigrow.R;
import com.google.android.material.card.MaterialCardView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ItemPlantBinding implements ViewBinding {
  @NonNull
  private final MaterialCardView rootView;

  @NonNull
  public final View divider;

  @NonNull
  public final ImageView imageViewBookmark;

  @NonNull
  public final ImageView imageViewPlant;

  @NonNull
  public final TextView textViewDifficultyLevel;

  @NonNull
  public final TextView textViewPlantDescription;

  @NonNull
  public final TextView textViewPlantName;

  @NonNull
  public final TextView textViewSunlight;

  @NonNull
  public final TextView textViewWater;

  private ItemPlantBinding(@NonNull MaterialCardView rootView, @NonNull View divider,
      @NonNull ImageView imageViewBookmark, @NonNull ImageView imageViewPlant,
      @NonNull TextView textViewDifficultyLevel, @NonNull TextView textViewPlantDescription,
      @NonNull TextView textViewPlantName, @NonNull TextView textViewSunlight,
      @NonNull TextView textViewWater) {
    this.rootView = rootView;
    this.divider = divider;
    this.imageViewBookmark = imageViewBookmark;
    this.imageViewPlant = imageViewPlant;
    this.textViewDifficultyLevel = textViewDifficultyLevel;
    this.textViewPlantDescription = textViewPlantDescription;
    this.textViewPlantName = textViewPlantName;
    this.textViewSunlight = textViewSunlight;
    this.textViewWater = textViewWater;
  }

  @Override
  @NonNull
  public MaterialCardView getRoot() {
    return rootView;
  }

  @NonNull
  public static ItemPlantBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ItemPlantBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.item_plant, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ItemPlantBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.divider;
      View divider = ViewBindings.findChildViewById(rootView, id);
      if (divider == null) {
        break missingId;
      }

      id = R.id.imageViewBookmark;
      ImageView imageViewBookmark = ViewBindings.findChildViewById(rootView, id);
      if (imageViewBookmark == null) {
        break missingId;
      }

      id = R.id.imageViewPlant;
      ImageView imageViewPlant = ViewBindings.findChildViewById(rootView, id);
      if (imageViewPlant == null) {
        break missingId;
      }

      id = R.id.textViewDifficultyLevel;
      TextView textViewDifficultyLevel = ViewBindings.findChildViewById(rootView, id);
      if (textViewDifficultyLevel == null) {
        break missingId;
      }

      id = R.id.textViewPlantDescription;
      TextView textViewPlantDescription = ViewBindings.findChildViewById(rootView, id);
      if (textViewPlantDescription == null) {
        break missingId;
      }

      id = R.id.textViewPlantName;
      TextView textViewPlantName = ViewBindings.findChildViewById(rootView, id);
      if (textViewPlantName == null) {
        break missingId;
      }

      id = R.id.textViewSunlight;
      TextView textViewSunlight = ViewBindings.findChildViewById(rootView, id);
      if (textViewSunlight == null) {
        break missingId;
      }

      id = R.id.textViewWater;
      TextView textViewWater = ViewBindings.findChildViewById(rootView, id);
      if (textViewWater == null) {
        break missingId;
      }

      return new ItemPlantBinding((MaterialCardView) rootView, divider, imageViewBookmark,
          imageViewPlant, textViewDifficultyLevel, textViewPlantDescription, textViewPlantName,
          textViewSunlight, textViewWater);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
