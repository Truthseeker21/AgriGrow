// Generated by view binder compiler. Do not edit!
package com.agrigrow.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.agrigrow.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentHomeBinding implements ViewBinding {
  @NonNull
  private final ScrollView rootView;

  @NonNull
  public final CardView cardViewAddPlant;

  @NonNull
  public final CardView cardViewCommunity;

  @NonNull
  public final CardView cardViewIdentifyPlant;

  @NonNull
  public final CardView cardViewTip;

  @NonNull
  public final CardView cardViewViewAllPlants;

  @NonNull
  public final CardView cardViewWeather;

  @NonNull
  public final ImageView imageViewWeatherIcon;

  @NonNull
  public final RecyclerView recyclerViewRecentPlants;

  @NonNull
  public final TextView textViewDate;

  @NonNull
  public final TextView textViewGreeting;

  @NonNull
  public final TextView textViewNoPlants;

  @NonNull
  public final TextView textViewTemperature;

  @NonNull
  public final TextView textViewTodayTip;

  @NonNull
  public final TextView textViewWeatherStatus;

  private FragmentHomeBinding(@NonNull ScrollView rootView, @NonNull CardView cardViewAddPlant,
      @NonNull CardView cardViewCommunity, @NonNull CardView cardViewIdentifyPlant,
      @NonNull CardView cardViewTip, @NonNull CardView cardViewViewAllPlants,
      @NonNull CardView cardViewWeather, @NonNull ImageView imageViewWeatherIcon,
      @NonNull RecyclerView recyclerViewRecentPlants, @NonNull TextView textViewDate,
      @NonNull TextView textViewGreeting, @NonNull TextView textViewNoPlants,
      @NonNull TextView textViewTemperature, @NonNull TextView textViewTodayTip,
      @NonNull TextView textViewWeatherStatus) {
    this.rootView = rootView;
    this.cardViewAddPlant = cardViewAddPlant;
    this.cardViewCommunity = cardViewCommunity;
    this.cardViewIdentifyPlant = cardViewIdentifyPlant;
    this.cardViewTip = cardViewTip;
    this.cardViewViewAllPlants = cardViewViewAllPlants;
    this.cardViewWeather = cardViewWeather;
    this.imageViewWeatherIcon = imageViewWeatherIcon;
    this.recyclerViewRecentPlants = recyclerViewRecentPlants;
    this.textViewDate = textViewDate;
    this.textViewGreeting = textViewGreeting;
    this.textViewNoPlants = textViewNoPlants;
    this.textViewTemperature = textViewTemperature;
    this.textViewTodayTip = textViewTodayTip;
    this.textViewWeatherStatus = textViewWeatherStatus;
  }

  @Override
  @NonNull
  public ScrollView getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentHomeBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentHomeBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_home, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentHomeBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.cardViewAddPlant;
      CardView cardViewAddPlant = ViewBindings.findChildViewById(rootView, id);
      if (cardViewAddPlant == null) {
        break missingId;
      }

      id = R.id.cardViewCommunity;
      CardView cardViewCommunity = ViewBindings.findChildViewById(rootView, id);
      if (cardViewCommunity == null) {
        break missingId;
      }

      id = R.id.cardViewIdentifyPlant;
      CardView cardViewIdentifyPlant = ViewBindings.findChildViewById(rootView, id);
      if (cardViewIdentifyPlant == null) {
        break missingId;
      }

      id = R.id.cardViewTip;
      CardView cardViewTip = ViewBindings.findChildViewById(rootView, id);
      if (cardViewTip == null) {
        break missingId;
      }

      id = R.id.cardViewViewAllPlants;
      CardView cardViewViewAllPlants = ViewBindings.findChildViewById(rootView, id);
      if (cardViewViewAllPlants == null) {
        break missingId;
      }

      id = R.id.cardViewWeather;
      CardView cardViewWeather = ViewBindings.findChildViewById(rootView, id);
      if (cardViewWeather == null) {
        break missingId;
      }

      id = R.id.imageViewWeatherIcon;
      ImageView imageViewWeatherIcon = ViewBindings.findChildViewById(rootView, id);
      if (imageViewWeatherIcon == null) {
        break missingId;
      }

      id = R.id.recyclerViewRecentPlants;
      RecyclerView recyclerViewRecentPlants = ViewBindings.findChildViewById(rootView, id);
      if (recyclerViewRecentPlants == null) {
        break missingId;
      }

      id = R.id.textViewDate;
      TextView textViewDate = ViewBindings.findChildViewById(rootView, id);
      if (textViewDate == null) {
        break missingId;
      }

      id = R.id.textViewGreeting;
      TextView textViewGreeting = ViewBindings.findChildViewById(rootView, id);
      if (textViewGreeting == null) {
        break missingId;
      }

      id = R.id.textViewNoPlants;
      TextView textViewNoPlants = ViewBindings.findChildViewById(rootView, id);
      if (textViewNoPlants == null) {
        break missingId;
      }

      id = R.id.textViewTemperature;
      TextView textViewTemperature = ViewBindings.findChildViewById(rootView, id);
      if (textViewTemperature == null) {
        break missingId;
      }

      id = R.id.textViewTodayTip;
      TextView textViewTodayTip = ViewBindings.findChildViewById(rootView, id);
      if (textViewTodayTip == null) {
        break missingId;
      }

      id = R.id.textViewWeatherStatus;
      TextView textViewWeatherStatus = ViewBindings.findChildViewById(rootView, id);
      if (textViewWeatherStatus == null) {
        break missingId;
      }

      return new FragmentHomeBinding((ScrollView) rootView, cardViewAddPlant, cardViewCommunity,
          cardViewIdentifyPlant, cardViewTip, cardViewViewAllPlants, cardViewWeather,
          imageViewWeatherIcon, recyclerViewRecentPlants, textViewDate, textViewGreeting,
          textViewNoPlants, textViewTemperature, textViewTodayTip, textViewWeatherStatus);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
