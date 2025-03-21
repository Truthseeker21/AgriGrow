// Generated by view binder compiler. Do not edit!
package com.agrigrow.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.agrigrow.R;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class DialogAddSoilTestBinding implements ViewBinding {
  @NonNull
  private final NestedScrollView rootView;

  @NonNull
  public final Button btnCancel;

  @NonNull
  public final Button btnGetLocation;

  @NonNull
  public final Button btnSave;

  @NonNull
  public final View divider;

  @NonNull
  public final View divider2;

  @NonNull
  public final AutoCompleteTextView dropdownSoilType;

  @NonNull
  public final TextInputEditText etLocation;

  @NonNull
  public final TextInputEditText etNitrogen;

  @NonNull
  public final TextInputEditText etNotes;

  @NonNull
  public final TextInputEditText etOrganicMatter;

  @NonNull
  public final TextInputEditText etPhosphorus;

  @NonNull
  public final TextInputEditText etPotassium;

  @NonNull
  public final TextInputEditText etTestDate;

  @NonNull
  public final TextInputEditText etTestName;

  @NonNull
  public final ImageView ivSoilPhoto;

  @NonNull
  public final LinearLayout layoutAddPhoto;

  @NonNull
  public final Slider sliderPh;

  @NonNull
  public final TextInputLayout tilLocation;

  @NonNull
  public final TextInputLayout tilNitrogen;

  @NonNull
  public final TextInputLayout tilNotes;

  @NonNull
  public final TextInputLayout tilOrganicMatter;

  @NonNull
  public final TextInputLayout tilPhosphorus;

  @NonNull
  public final TextInputLayout tilPotassium;

  @NonNull
  public final TextInputLayout tilSoilType;

  @NonNull
  public final TextInputLayout tilTestDate;

  @NonNull
  public final TextInputLayout tilTestName;

  @NonNull
  public final TextView tvAddPhoto;

  @NonNull
  public final TextView tvAdditionalInfoLabel;

  @NonNull
  public final TextView tvDialogSubtitle;

  @NonNull
  public final TextView tvDialogTitle;

  @NonNull
  public final TextView tvPhLabel;

  @NonNull
  public final TextView tvSoilCompositionLabel;

  private DialogAddSoilTestBinding(@NonNull NestedScrollView rootView, @NonNull Button btnCancel,
      @NonNull Button btnGetLocation, @NonNull Button btnSave, @NonNull View divider,
      @NonNull View divider2, @NonNull AutoCompleteTextView dropdownSoilType,
      @NonNull TextInputEditText etLocation, @NonNull TextInputEditText etNitrogen,
      @NonNull TextInputEditText etNotes, @NonNull TextInputEditText etOrganicMatter,
      @NonNull TextInputEditText etPhosphorus, @NonNull TextInputEditText etPotassium,
      @NonNull TextInputEditText etTestDate, @NonNull TextInputEditText etTestName,
      @NonNull ImageView ivSoilPhoto, @NonNull LinearLayout layoutAddPhoto,
      @NonNull Slider sliderPh, @NonNull TextInputLayout tilLocation,
      @NonNull TextInputLayout tilNitrogen, @NonNull TextInputLayout tilNotes,
      @NonNull TextInputLayout tilOrganicMatter, @NonNull TextInputLayout tilPhosphorus,
      @NonNull TextInputLayout tilPotassium, @NonNull TextInputLayout tilSoilType,
      @NonNull TextInputLayout tilTestDate, @NonNull TextInputLayout tilTestName,
      @NonNull TextView tvAddPhoto, @NonNull TextView tvAdditionalInfoLabel,
      @NonNull TextView tvDialogSubtitle, @NonNull TextView tvDialogTitle,
      @NonNull TextView tvPhLabel, @NonNull TextView tvSoilCompositionLabel) {
    this.rootView = rootView;
    this.btnCancel = btnCancel;
    this.btnGetLocation = btnGetLocation;
    this.btnSave = btnSave;
    this.divider = divider;
    this.divider2 = divider2;
    this.dropdownSoilType = dropdownSoilType;
    this.etLocation = etLocation;
    this.etNitrogen = etNitrogen;
    this.etNotes = etNotes;
    this.etOrganicMatter = etOrganicMatter;
    this.etPhosphorus = etPhosphorus;
    this.etPotassium = etPotassium;
    this.etTestDate = etTestDate;
    this.etTestName = etTestName;
    this.ivSoilPhoto = ivSoilPhoto;
    this.layoutAddPhoto = layoutAddPhoto;
    this.sliderPh = sliderPh;
    this.tilLocation = tilLocation;
    this.tilNitrogen = tilNitrogen;
    this.tilNotes = tilNotes;
    this.tilOrganicMatter = tilOrganicMatter;
    this.tilPhosphorus = tilPhosphorus;
    this.tilPotassium = tilPotassium;
    this.tilSoilType = tilSoilType;
    this.tilTestDate = tilTestDate;
    this.tilTestName = tilTestName;
    this.tvAddPhoto = tvAddPhoto;
    this.tvAdditionalInfoLabel = tvAdditionalInfoLabel;
    this.tvDialogSubtitle = tvDialogSubtitle;
    this.tvDialogTitle = tvDialogTitle;
    this.tvPhLabel = tvPhLabel;
    this.tvSoilCompositionLabel = tvSoilCompositionLabel;
  }

  @Override
  @NonNull
  public NestedScrollView getRoot() {
    return rootView;
  }

  @NonNull
  public static DialogAddSoilTestBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static DialogAddSoilTestBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.dialog_add_soil_test, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static DialogAddSoilTestBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btn_cancel;
      Button btnCancel = ViewBindings.findChildViewById(rootView, id);
      if (btnCancel == null) {
        break missingId;
      }

      id = R.id.btn_get_location;
      Button btnGetLocation = ViewBindings.findChildViewById(rootView, id);
      if (btnGetLocation == null) {
        break missingId;
      }

      id = R.id.btn_save;
      Button btnSave = ViewBindings.findChildViewById(rootView, id);
      if (btnSave == null) {
        break missingId;
      }

      id = R.id.divider;
      View divider = ViewBindings.findChildViewById(rootView, id);
      if (divider == null) {
        break missingId;
      }

      id = R.id.divider2;
      View divider2 = ViewBindings.findChildViewById(rootView, id);
      if (divider2 == null) {
        break missingId;
      }

      id = R.id.dropdown_soil_type;
      AutoCompleteTextView dropdownSoilType = ViewBindings.findChildViewById(rootView, id);
      if (dropdownSoilType == null) {
        break missingId;
      }

      id = R.id.et_location;
      TextInputEditText etLocation = ViewBindings.findChildViewById(rootView, id);
      if (etLocation == null) {
        break missingId;
      }

      id = R.id.et_nitrogen;
      TextInputEditText etNitrogen = ViewBindings.findChildViewById(rootView, id);
      if (etNitrogen == null) {
        break missingId;
      }

      id = R.id.et_notes;
      TextInputEditText etNotes = ViewBindings.findChildViewById(rootView, id);
      if (etNotes == null) {
        break missingId;
      }

      id = R.id.et_organic_matter;
      TextInputEditText etOrganicMatter = ViewBindings.findChildViewById(rootView, id);
      if (etOrganicMatter == null) {
        break missingId;
      }

      id = R.id.et_phosphorus;
      TextInputEditText etPhosphorus = ViewBindings.findChildViewById(rootView, id);
      if (etPhosphorus == null) {
        break missingId;
      }

      id = R.id.et_potassium;
      TextInputEditText etPotassium = ViewBindings.findChildViewById(rootView, id);
      if (etPotassium == null) {
        break missingId;
      }

      id = R.id.et_test_date;
      TextInputEditText etTestDate = ViewBindings.findChildViewById(rootView, id);
      if (etTestDate == null) {
        break missingId;
      }

      id = R.id.et_test_name;
      TextInputEditText etTestName = ViewBindings.findChildViewById(rootView, id);
      if (etTestName == null) {
        break missingId;
      }

      id = R.id.iv_soil_photo;
      ImageView ivSoilPhoto = ViewBindings.findChildViewById(rootView, id);
      if (ivSoilPhoto == null) {
        break missingId;
      }

      id = R.id.layout_add_photo;
      LinearLayout layoutAddPhoto = ViewBindings.findChildViewById(rootView, id);
      if (layoutAddPhoto == null) {
        break missingId;
      }

      id = R.id.slider_ph;
      Slider sliderPh = ViewBindings.findChildViewById(rootView, id);
      if (sliderPh == null) {
        break missingId;
      }

      id = R.id.til_location;
      TextInputLayout tilLocation = ViewBindings.findChildViewById(rootView, id);
      if (tilLocation == null) {
        break missingId;
      }

      id = R.id.til_nitrogen;
      TextInputLayout tilNitrogen = ViewBindings.findChildViewById(rootView, id);
      if (tilNitrogen == null) {
        break missingId;
      }

      id = R.id.til_notes;
      TextInputLayout tilNotes = ViewBindings.findChildViewById(rootView, id);
      if (tilNotes == null) {
        break missingId;
      }

      id = R.id.til_organic_matter;
      TextInputLayout tilOrganicMatter = ViewBindings.findChildViewById(rootView, id);
      if (tilOrganicMatter == null) {
        break missingId;
      }

      id = R.id.til_phosphorus;
      TextInputLayout tilPhosphorus = ViewBindings.findChildViewById(rootView, id);
      if (tilPhosphorus == null) {
        break missingId;
      }

      id = R.id.til_potassium;
      TextInputLayout tilPotassium = ViewBindings.findChildViewById(rootView, id);
      if (tilPotassium == null) {
        break missingId;
      }

      id = R.id.til_soil_type;
      TextInputLayout tilSoilType = ViewBindings.findChildViewById(rootView, id);
      if (tilSoilType == null) {
        break missingId;
      }

      id = R.id.til_test_date;
      TextInputLayout tilTestDate = ViewBindings.findChildViewById(rootView, id);
      if (tilTestDate == null) {
        break missingId;
      }

      id = R.id.til_test_name;
      TextInputLayout tilTestName = ViewBindings.findChildViewById(rootView, id);
      if (tilTestName == null) {
        break missingId;
      }

      id = R.id.tv_add_photo;
      TextView tvAddPhoto = ViewBindings.findChildViewById(rootView, id);
      if (tvAddPhoto == null) {
        break missingId;
      }

      id = R.id.tv_additional_info_label;
      TextView tvAdditionalInfoLabel = ViewBindings.findChildViewById(rootView, id);
      if (tvAdditionalInfoLabel == null) {
        break missingId;
      }

      id = R.id.tv_dialog_subtitle;
      TextView tvDialogSubtitle = ViewBindings.findChildViewById(rootView, id);
      if (tvDialogSubtitle == null) {
        break missingId;
      }

      id = R.id.tv_dialog_title;
      TextView tvDialogTitle = ViewBindings.findChildViewById(rootView, id);
      if (tvDialogTitle == null) {
        break missingId;
      }

      id = R.id.tv_ph_label;
      TextView tvPhLabel = ViewBindings.findChildViewById(rootView, id);
      if (tvPhLabel == null) {
        break missingId;
      }

      id = R.id.tv_soil_composition_label;
      TextView tvSoilCompositionLabel = ViewBindings.findChildViewById(rootView, id);
      if (tvSoilCompositionLabel == null) {
        break missingId;
      }

      return new DialogAddSoilTestBinding((NestedScrollView) rootView, btnCancel, btnGetLocation,
          btnSave, divider, divider2, dropdownSoilType, etLocation, etNitrogen, etNotes,
          etOrganicMatter, etPhosphorus, etPotassium, etTestDate, etTestName, ivSoilPhoto,
          layoutAddPhoto, sliderPh, tilLocation, tilNitrogen, tilNotes, tilOrganicMatter,
          tilPhosphorus, tilPotassium, tilSoilType, tilTestDate, tilTestName, tvAddPhoto,
          tvAdditionalInfoLabel, tvDialogSubtitle, tvDialogTitle, tvPhLabel,
          tvSoilCompositionLabel);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
