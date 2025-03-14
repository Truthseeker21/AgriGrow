// Generated by view binder compiler. Do not edit!
package com.agrigrow.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.agrigrow.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class DialogSoilAnalysisBinding implements ViewBinding {
  @NonNull
  private final NestedScrollView rootView;

  @NonNull
  public final Button btnClose;

  @NonNull
  public final Button btnExportData;

  @NonNull
  public final CardView cardNutrientChart;

  @NonNull
  public final CardView cardPhChart;

  @NonNull
  public final CardView cardSoilHealthSummary;

  @NonNull
  public final ImageView ivNutrientChart;

  @NonNull
  public final ImageView ivPhChart;

  @NonNull
  public final ProgressBar progressOverallHealth;

  @NonNull
  public final TextView tvAnalysisSubtitle;

  @NonNull
  public final TextView tvDialogTitle;

  @NonNull
  public final TextView tvHealthSummary;

  @NonNull
  public final TextView tvHealthSummaryTitle;

  @NonNull
  public final TextView tvNpkRecommendation;

  @NonNull
  public final TextView tvNutrientChartTitle;

  @NonNull
  public final TextView tvOverallHealthLabel;

  @NonNull
  public final TextView tvPhChartTitle;

  @NonNull
  public final TextView tvPhRecommendation;

  @NonNull
  public final TextView tvTestsAnalyzed;

  @NonNull
  public final TextView tvTestsAnalyzedLabel;

  private DialogSoilAnalysisBinding(@NonNull NestedScrollView rootView, @NonNull Button btnClose,
      @NonNull Button btnExportData, @NonNull CardView cardNutrientChart,
      @NonNull CardView cardPhChart, @NonNull CardView cardSoilHealthSummary,
      @NonNull ImageView ivNutrientChart, @NonNull ImageView ivPhChart,
      @NonNull ProgressBar progressOverallHealth, @NonNull TextView tvAnalysisSubtitle,
      @NonNull TextView tvDialogTitle, @NonNull TextView tvHealthSummary,
      @NonNull TextView tvHealthSummaryTitle, @NonNull TextView tvNpkRecommendation,
      @NonNull TextView tvNutrientChartTitle, @NonNull TextView tvOverallHealthLabel,
      @NonNull TextView tvPhChartTitle, @NonNull TextView tvPhRecommendation,
      @NonNull TextView tvTestsAnalyzed, @NonNull TextView tvTestsAnalyzedLabel) {
    this.rootView = rootView;
    this.btnClose = btnClose;
    this.btnExportData = btnExportData;
    this.cardNutrientChart = cardNutrientChart;
    this.cardPhChart = cardPhChart;
    this.cardSoilHealthSummary = cardSoilHealthSummary;
    this.ivNutrientChart = ivNutrientChart;
    this.ivPhChart = ivPhChart;
    this.progressOverallHealth = progressOverallHealth;
    this.tvAnalysisSubtitle = tvAnalysisSubtitle;
    this.tvDialogTitle = tvDialogTitle;
    this.tvHealthSummary = tvHealthSummary;
    this.tvHealthSummaryTitle = tvHealthSummaryTitle;
    this.tvNpkRecommendation = tvNpkRecommendation;
    this.tvNutrientChartTitle = tvNutrientChartTitle;
    this.tvOverallHealthLabel = tvOverallHealthLabel;
    this.tvPhChartTitle = tvPhChartTitle;
    this.tvPhRecommendation = tvPhRecommendation;
    this.tvTestsAnalyzed = tvTestsAnalyzed;
    this.tvTestsAnalyzedLabel = tvTestsAnalyzedLabel;
  }

  @Override
  @NonNull
  public NestedScrollView getRoot() {
    return rootView;
  }

  @NonNull
  public static DialogSoilAnalysisBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static DialogSoilAnalysisBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.dialog_soil_analysis, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static DialogSoilAnalysisBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btn_close;
      Button btnClose = ViewBindings.findChildViewById(rootView, id);
      if (btnClose == null) {
        break missingId;
      }

      id = R.id.btn_export_data;
      Button btnExportData = ViewBindings.findChildViewById(rootView, id);
      if (btnExportData == null) {
        break missingId;
      }

      id = R.id.card_nutrient_chart;
      CardView cardNutrientChart = ViewBindings.findChildViewById(rootView, id);
      if (cardNutrientChart == null) {
        break missingId;
      }

      id = R.id.card_ph_chart;
      CardView cardPhChart = ViewBindings.findChildViewById(rootView, id);
      if (cardPhChart == null) {
        break missingId;
      }

      id = R.id.card_soil_health_summary;
      CardView cardSoilHealthSummary = ViewBindings.findChildViewById(rootView, id);
      if (cardSoilHealthSummary == null) {
        break missingId;
      }

      id = R.id.iv_nutrient_chart;
      ImageView ivNutrientChart = ViewBindings.findChildViewById(rootView, id);
      if (ivNutrientChart == null) {
        break missingId;
      }

      id = R.id.iv_ph_chart;
      ImageView ivPhChart = ViewBindings.findChildViewById(rootView, id);
      if (ivPhChart == null) {
        break missingId;
      }

      id = R.id.progress_overall_health;
      ProgressBar progressOverallHealth = ViewBindings.findChildViewById(rootView, id);
      if (progressOverallHealth == null) {
        break missingId;
      }

      id = R.id.tv_analysis_subtitle;
      TextView tvAnalysisSubtitle = ViewBindings.findChildViewById(rootView, id);
      if (tvAnalysisSubtitle == null) {
        break missingId;
      }

      id = R.id.tv_dialog_title;
      TextView tvDialogTitle = ViewBindings.findChildViewById(rootView, id);
      if (tvDialogTitle == null) {
        break missingId;
      }

      id = R.id.tv_health_summary;
      TextView tvHealthSummary = ViewBindings.findChildViewById(rootView, id);
      if (tvHealthSummary == null) {
        break missingId;
      }

      id = R.id.tv_health_summary_title;
      TextView tvHealthSummaryTitle = ViewBindings.findChildViewById(rootView, id);
      if (tvHealthSummaryTitle == null) {
        break missingId;
      }

      id = R.id.tv_npk_recommendation;
      TextView tvNpkRecommendation = ViewBindings.findChildViewById(rootView, id);
      if (tvNpkRecommendation == null) {
        break missingId;
      }

      id = R.id.tv_nutrient_chart_title;
      TextView tvNutrientChartTitle = ViewBindings.findChildViewById(rootView, id);
      if (tvNutrientChartTitle == null) {
        break missingId;
      }

      id = R.id.tv_overall_health_label;
      TextView tvOverallHealthLabel = ViewBindings.findChildViewById(rootView, id);
      if (tvOverallHealthLabel == null) {
        break missingId;
      }

      id = R.id.tv_ph_chart_title;
      TextView tvPhChartTitle = ViewBindings.findChildViewById(rootView, id);
      if (tvPhChartTitle == null) {
        break missingId;
      }

      id = R.id.tv_ph_recommendation;
      TextView tvPhRecommendation = ViewBindings.findChildViewById(rootView, id);
      if (tvPhRecommendation == null) {
        break missingId;
      }

      id = R.id.tv_tests_analyzed;
      TextView tvTestsAnalyzed = ViewBindings.findChildViewById(rootView, id);
      if (tvTestsAnalyzed == null) {
        break missingId;
      }

      id = R.id.tv_tests_analyzed_label;
      TextView tvTestsAnalyzedLabel = ViewBindings.findChildViewById(rootView, id);
      if (tvTestsAnalyzedLabel == null) {
        break missingId;
      }

      return new DialogSoilAnalysisBinding((NestedScrollView) rootView, btnClose, btnExportData,
          cardNutrientChart, cardPhChart, cardSoilHealthSummary, ivNutrientChart, ivPhChart,
          progressOverallHealth, tvAnalysisSubtitle, tvDialogTitle, tvHealthSummary,
          tvHealthSummaryTitle, tvNpkRecommendation, tvNutrientChartTitle, tvOverallHealthLabel,
          tvPhChartTitle, tvPhRecommendation, tvTestsAnalyzed, tvTestsAnalyzedLabel);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
