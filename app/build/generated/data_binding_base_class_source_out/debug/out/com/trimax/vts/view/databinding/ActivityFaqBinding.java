// Generated by data binding compiler. Do not edit!
package com.trimax.vts.view.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import com.trimax.vts.view.R;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class ActivityFaqBinding extends ViewDataBinding {
  @NonNull
  public final RecyclerView rvFaqs;

  protected ActivityFaqBinding(Object _bindingComponent, View _root, int _localFieldCount,
      RecyclerView rvFaqs) {
    super(_bindingComponent, _root, _localFieldCount);
    this.rvFaqs = rvFaqs;
  }

  @NonNull
  public static ActivityFaqBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_faq, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static ActivityFaqBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<ActivityFaqBinding>inflateInternal(inflater, R.layout.activity_faq, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityFaqBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_faq, null, false, component)
   */
  @NonNull
  @Deprecated
  public static ActivityFaqBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<ActivityFaqBinding>inflateInternal(inflater, R.layout.activity_faq, null, false, component);
  }

  public static ActivityFaqBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.bind(view, component)
   */
  @Deprecated
  public static ActivityFaqBinding bind(@NonNull View view, @Nullable Object component) {
    return (ActivityFaqBinding)bind(component, view, R.layout.activity_faq);
  }
}