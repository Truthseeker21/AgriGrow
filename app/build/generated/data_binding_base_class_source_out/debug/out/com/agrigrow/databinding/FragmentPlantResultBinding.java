// Generated by view binder compiler. Do not edit!
package com.agrigrow.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.agrigrow.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentPlantResultBinding implements ViewBinding {
  @NonNull
  private final CoordinatorLayout rootView;

  @NonNull
  public final AppBarLayout appBarLayout;

  @NonNull
  public final Button buttonAddToGarden;

  @NonNull
  public final Button buttonTryAgain;

  @NonNull
  public final CardView cardViewCareGuide;

  @NonNull
  public final CardView cardViewEdibleParts;

  @NonNull
  public final CollapsingToolbarLayout collapsingToolbar;

  @NonNull
  public final ImageView imageViewPlant;

  @NonNull
  public final RecyclerView recyclerViewAlternatives;

  @NonNull
  public final TextView textViewCareGuide;

  @NonNull
  public final TextView textViewCommonName;

  @NonNull
  public final TextView textViewConfidence;

  @NonNull
  public final TextView textViewDescription;

  @NonNull
  public final TextView textViewEdibleParts;

  @NonNull
  public final TextView textViewFamily;

  @NonNull
  public final TextView textViewScientificName;

  @NonNull
  public final TextView textViewWarning;

  @NonNull
  public final Toolbar toolbar;

  private FragmentPlantResultBinding(@NonNull CoordinatorLayout rootView,
      @NonNull AppBarLayout appBarLayout, @NonNull Button buttonAddToGarden,
      @NonNull Button buttonTryAgain, @NonNull CardView cardViewCareGuide,
      @NonNull CardView cardViewEdibleParts, @NonNull CollapsingToolbarLayout collapsingToolbar,
      @NonNull ImageView imageViewPlant, @NonNull RecyclerView recyclerViewAlternatives,
      @NonNull TextView textViewCareGuide, @NonNull TextView textViewCommonName,
      @NonNull TextView textViewConfidence, @NonNull TextView textViewDescription,
      @NonNull TextView textViewEdibleParts, @NonNull TextView textViewFamily,
      @NonNull TextView textViewScientificName, @NonNull TextView textViewWarning,
      @NonNull Toolbar toolbar) {
    this.rootView = rootView;
    this.appBarLayout = appBarLayout;
    this.buttonAddToGarden = buttonAddToGarden;
    this.buttonTryAgain = buttonTryAgain;
    this.cardViewCareGuide = cardViewCareGuide;
    this.cardViewEdibleParts = cardViewEdibleParts;
    this.collapsingToolbar = collapsingToolbar;
    this.imageViewPlant = imageViewPlant;
    this.recyclerViewAlternatives = recyclerViewAlternatives;
    this.textViewCareGuide = textViewCareGuide;
    this.textViewCommonName = textViewCommonName;
    this.textViewConfidence = textViewConfidence;
    this.textViewDescription = textViewDescription;
    this.textViewEdibleParts = textViewEdibleParts;
    this.textViewFamily = textViewFamily;
    this.textViewScientificName = textViewScientificName;
    this.textViewWarning = textViewWarning;
    this.toolbar = toolbar;
  }

  @Override
  @NonNull
  public CoordinatorLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentPlantResultBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentPlantResultBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_plant_result, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentPlantResultBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.appBarLayout;
      AppBarLayout appBarLayout = ViewBindings.findChildViewById(rootView, id);
      if (appBarLayout == null) {
        break missingId;
      }

      id = R.id.buttonAddToGarden;
      Button buttonAddToGarden = ViewBindings.findChildViewById(rootView, id);
      if (buttonAddToGarden == null) {
        break missingId;
      }

      id = R.id.buttonTryAgain;
      Button buttonTryAgain = ViewBindings.findChildViewById(rootView, id);
      if (buttonTryAgain == null) {
        break missingId;
      }

      id = R.id.cardViewCareGuide;
      CardView cardViewCareGuide = ViewBindings.findChildViewById(rootView, id);
      if (cardViewCareGuide == null) {
        break missingId;
      }

      id = R.id.cardViewEdibleParts;
      CardView cardViewEdibleParts = ViewBindings.findChildViewById(rootView, id);
      if (cardViewEdibleParts == null) {
        break missingId;
      }

      id = R.id.collapsingToolbar;
      CollapsingToolbarLayout collapsingToolbar = ViewBindings.findChildViewById(rootView, id);
      if (collapsingToolbar == null) {
        break missingId;
      }

      id = R.id.imageViewPlant;
      ImageView imageViewPlant = ViewBindings.findChildViewById(rootView, id);
      if (imageViewPlant == null) {
        break missingId;
      }

      id = R.id.recyclerViewAlternatives;
      RecyclerView recyclerViewAlternatives = ViewBindings.findChildViewById(rootView, id);
      if (recyclerViewAlternatives == null) {
        break missingId;
      }

      id = R.id.textViewCareGuide;
      TextView textViewCareGuide = ViewBindings.findChildViewById(rootView, id);
      if (textViewCareGuide == null) {
        break missingId;
      }

      id = R.id.textViewCommonName;
      TextView textViewCommonName = ViewBindings.findChildViewById(rootView, id);
      if (textViewCommonName == null) {
        break missingId;
      }

      id = R.id.textViewConfidence;
      TextView textViewConfidence = ViewBindings.findChildViewById(rootView, id);
      if (textViewConfidence == null) {
        break missingId;
      }

      id = R.id.textViewDescription;
      TextView textViewDescription = ViewBindings.findChildViewById(rootView, id);
      if (textViewDescription == null) {
        break missingId;
      }

      id = R.id.textViewEdibleParts;
      TextView textViewEdibleParts = ViewBindings.findChildViewById(rootView, id);
      if (textViewEdibleParts == null) {
        break missingId;
      }

      id = R.id.textViewFamily;
      TextView textViewFamily = ViewBindings.findChildViewById(rootView, id);
      if (textViewFamily == null) {
        break missingId;
      }

      id = R.id.textViewScientificName;
      TextView textViewScientificName = ViewBindings.findChildViewById(rootView, id);
      if (textViewScientificName == null) {
        break missingId;
      }

      id = R.id.textViewWarning;
      TextView textViewWarning = ViewBindings.findChildViewById(rootView, id);
      if (textViewWarning == null) {
        break missingId;
      }

      id = R.id.toolbar;
      Toolbar toolbar = ViewBindings.findChildViewById(rootView, id);
      if (toolbar == null) {
        break missingId;
      }

      return new FragmentPlantResultBinding((CoordinatorLayout) rootView, appBarLayout,
          buttonAddToGarden, buttonTryAgain, cardViewCareGuide, cardViewEdibleParts,
          collapsingToolbar, imageViewPlant, recyclerViewAlternatives, textViewCareGuide,
          textViewCommonName, textViewConfidence, textViewDescription, textViewEdibleParts,
          textViewFamily, textViewScientificName, textViewWarning, toolbar);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
