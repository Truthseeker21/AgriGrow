// Generated by view binder compiler. Do not edit!
package com.agrigrow.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.agrigrow.R;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentJournalBinding implements ViewBinding {
  @NonNull
  private final CoordinatorLayout rootView;

  @NonNull
  public final ChipGroup chipGroupEntryTypes;

  @NonNull
  public final FloatingActionButton fabAddEntry;

  @NonNull
  public final RecyclerView recyclerViewJournal;

  @NonNull
  public final TextView textViewJournalSubtitle;

  @NonNull
  public final TextView textViewJournalTitle;

  @NonNull
  public final TextView textViewNoEntries;

  private FragmentJournalBinding(@NonNull CoordinatorLayout rootView,
      @NonNull ChipGroup chipGroupEntryTypes, @NonNull FloatingActionButton fabAddEntry,
      @NonNull RecyclerView recyclerViewJournal, @NonNull TextView textViewJournalSubtitle,
      @NonNull TextView textViewJournalTitle, @NonNull TextView textViewNoEntries) {
    this.rootView = rootView;
    this.chipGroupEntryTypes = chipGroupEntryTypes;
    this.fabAddEntry = fabAddEntry;
    this.recyclerViewJournal = recyclerViewJournal;
    this.textViewJournalSubtitle = textViewJournalSubtitle;
    this.textViewJournalTitle = textViewJournalTitle;
    this.textViewNoEntries = textViewNoEntries;
  }

  @Override
  @NonNull
  public CoordinatorLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentJournalBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentJournalBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_journal, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentJournalBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.chipGroupEntryTypes;
      ChipGroup chipGroupEntryTypes = ViewBindings.findChildViewById(rootView, id);
      if (chipGroupEntryTypes == null) {
        break missingId;
      }

      id = R.id.fabAddEntry;
      FloatingActionButton fabAddEntry = ViewBindings.findChildViewById(rootView, id);
      if (fabAddEntry == null) {
        break missingId;
      }

      id = R.id.recyclerViewJournal;
      RecyclerView recyclerViewJournal = ViewBindings.findChildViewById(rootView, id);
      if (recyclerViewJournal == null) {
        break missingId;
      }

      id = R.id.textViewJournalSubtitle;
      TextView textViewJournalSubtitle = ViewBindings.findChildViewById(rootView, id);
      if (textViewJournalSubtitle == null) {
        break missingId;
      }

      id = R.id.textViewJournalTitle;
      TextView textViewJournalTitle = ViewBindings.findChildViewById(rootView, id);
      if (textViewJournalTitle == null) {
        break missingId;
      }

      id = R.id.textViewNoEntries;
      TextView textViewNoEntries = ViewBindings.findChildViewById(rootView, id);
      if (textViewNoEntries == null) {
        break missingId;
      }

      return new FragmentJournalBinding((CoordinatorLayout) rootView, chipGroupEntryTypes,
          fabAddEntry, recyclerViewJournal, textViewJournalSubtitle, textViewJournalTitle,
          textViewNoEntries);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
