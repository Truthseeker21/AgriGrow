// Generated by view binder compiler. Do not edit!
package com.agrigrow.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
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

public final class FragmentWeatherBinding implements ViewBinding {
  @NonNull
  private final CoordinatorLayout rootView;

  @NonNull
  public final AppBarLayout appBarLayout;

  @NonNull
  public final ImageView backgroundImage;

  @NonNull
  public final Button buttonUpdateWateringSchedule;

  @NonNull
  public final CollapsingToolbarLayout collapsingToolbar;

  @NonNull
  public final ImageView imageViewWeatherIcon;

  @NonNull
  public final LinearLayout linearLayoutForecast;

  @NonNull
  public final ProgressBar progressBar;

  @NonNull
  public final RecyclerView recyclerViewRecommendations;

  @NonNull
  public final RecyclerView recyclerViewWateringSchedule;

  @NonNull
  public final TextView textViewHumidity;

  @NonNull
  public final TextView textViewLocation;

  @NonNull
  public final TextView textViewSunrise;

  @NonNull
  public final TextView textViewSunset;

  @NonNull
  public final TextView textViewTemperature;

  @NonNull
  public final TextView textViewWateringInfo;

  @NonNull
  public final TextView textViewWeatherDescription;

  @NonNull
  public final TextView textViewWind;

  @NonNull
  public final Toolbar toolbar;

  private FragmentWeatherBinding(@NonNull CoordinatorLayout rootView,
      @NonNull AppBarLayout appBarLayout, @NonNull ImageView backgroundImage,
      @NonNull Button buttonUpdateWateringSchedule,
      @NonNull CollapsingToolbarLayout collapsingToolbar, @NonNull ImageView imageViewWeatherIcon,
      @NonNull LinearLayout linearLayoutForecast, @NonNull ProgressBar progressBar,
      @NonNull RecyclerView recyclerViewRecommendations,
      @NonNull RecyclerView recyclerViewWateringSchedule, @NonNull TextView textViewHumidity,
      @NonNull TextView textViewLocation, @NonNull TextView textViewSunrise,
      @NonNull TextView textViewSunset, @NonNull TextView textViewTemperature,
      @NonNull TextView textViewWateringInfo, @NonNull TextView textViewWeatherDescription,
      @NonNull TextView textViewWind, @NonNull Toolbar toolbar) {
    this.rootView = rootView;
    this.appBarLayout = appBarLayout;
    this.backgroundImage = backgroundImage;
    this.buttonUpdateWateringSchedule = buttonUpdateWateringSchedule;
    this.collapsingToolbar = collapsingToolbar;
    this.imageViewWeatherIcon = imageViewWeatherIcon;
    this.linearLayoutForecast = linearLayoutForecast;
    this.progressBar = progressBar;
    this.recyclerViewRecommendations = recyclerViewRecommendations;
    this.recyclerViewWateringSchedule = recyclerViewWateringSchedule;
    this.textViewHumidity = textViewHumidity;
    this.textViewLocation = textViewLocation;
    this.textViewSunrise = textViewSunrise;
    this.textViewSunset = textViewSunset;
    this.textViewTemperature = textViewTemperature;
    this.textViewWateringInfo = textViewWateringInfo;
    this.textViewWeatherDescription = textViewWeatherDescription;
    this.textViewWind = textViewWind;
    this.toolbar = toolbar;
  }

  @Override
  @NonNull
  public CoordinatorLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentWeatherBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentWeatherBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_weather, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentWeatherBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.appBarLayout;
      AppBarLayout appBarLayout = ViewBindings.findChildViewById(rootView, id);
      if (appBarLayout == null) {
        break missingId;
      }

      id = R.id.backgroundImage;
      ImageView backgroundImage = ViewBindings.findChildViewById(rootView, id);
      if (backgroundImage == null) {
        break missingId;
      }

      id = R.id.buttonUpdateWateringSchedule;
      Button buttonUpdateWateringSchedule = ViewBindings.findChildViewById(rootView, id);
      if (buttonUpdateWateringSchedule == null) {
        break missingId;
      }

      id = R.id.collapsingToolbar;
      CollapsingToolbarLayout collapsingToolbar = ViewBindings.findChildViewById(rootView, id);
      if (collapsingToolbar == null) {
        break missingId;
      }

      id = R.id.imageViewWeatherIcon;
      ImageView imageViewWeatherIcon = ViewBindings.findChildViewById(rootView, id);
      if (imageViewWeatherIcon == null) {
        break missingId;
      }

      id = R.id.linearLayoutForecast;
      LinearLayout linearLayoutForecast = ViewBindings.findChildViewById(rootView, id);
      if (linearLayoutForecast == null) {
        break missingId;
      }

      id = R.id.progressBar;
      ProgressBar progressBar = ViewBindings.findChildViewById(rootView, id);
      if (progressBar == null) {
        break missingId;
      }

      id = R.id.recyclerViewRecommendations;
      RecyclerView recyclerViewRecommendations = ViewBindings.findChildViewById(rootView, id);
      if (recyclerViewRecommendations == null) {
        break missingId;
      }

      id = R.id.recyclerViewWateringSchedule;
      RecyclerView recyclerViewWateringSchedule = ViewBindings.findChildViewById(rootView, id);
      if (recyclerViewWateringSchedule == null) {
        break missingId;
      }

      id = R.id.textViewHumidity;
      TextView textViewHumidity = ViewBindings.findChildViewById(rootView, id);
      if (textViewHumidity == null) {
        break missingId;
      }

      id = R.id.textViewLocation;
      TextView textViewLocation = ViewBindings.findChildViewById(rootView, id);
      if (textViewLocation == null) {
        break missingId;
      }

      id = R.id.textViewSunrise;
      TextView textViewSunrise = ViewBindings.findChildViewById(rootView, id);
      if (textViewSunrise == null) {
        break missingId;
      }

      id = R.id.textViewSunset;
      TextView textViewSunset = ViewBindings.findChildViewById(rootView, id);
      if (textViewSunset == null) {
        break missingId;
      }

      id = R.id.textViewTemperature;
      TextView textViewTemperature = ViewBindings.findChildViewById(rootView, id);
      if (textViewTemperature == null) {
        break missingId;
      }

      id = R.id.textViewWateringInfo;
      TextView textViewWateringInfo = ViewBindings.findChildViewById(rootView, id);
      if (textViewWateringInfo == null) {
        break missingId;
      }

      id = R.id.textViewWeatherDescription;
      TextView textViewWeatherDescription = ViewBindings.findChildViewById(rootView, id);
      if (textViewWeatherDescription == null) {
        break missingId;
      }

      id = R.id.textViewWind;
      TextView textViewWind = ViewBindings.findChildViewById(rootView, id);
      if (textViewWind == null) {
        break missingId;
      }

      id = R.id.toolbar;
      Toolbar toolbar = ViewBindings.findChildViewById(rootView, id);
      if (toolbar == null) {
        break missingId;
      }

      return new FragmentWeatherBinding((CoordinatorLayout) rootView, appBarLayout, backgroundImage,
          buttonUpdateWateringSchedule, collapsingToolbar, imageViewWeatherIcon,
          linearLayoutForecast, progressBar, recyclerViewRecommendations,
          recyclerViewWateringSchedule, textViewHumidity, textViewLocation, textViewSunrise,
          textViewSunset, textViewTemperature, textViewWateringInfo, textViewWeatherDescription,
          textViewWind, toolbar);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
