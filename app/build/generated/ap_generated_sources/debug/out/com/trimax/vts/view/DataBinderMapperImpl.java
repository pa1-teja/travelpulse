package com.trimax.vts.view;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import androidx.databinding.DataBinderMapper;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import com.trimax.vts.view.databinding.ActivityAddUserBindingImpl;
import com.trimax.vts.view.databinding.ActivityFaqBindingImpl;
import com.trimax.vts.view.databinding.ActivityLoginBindingImpl;
import com.trimax.vts.view.databinding.ActivityUserListBindingImpl;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.RuntimeException;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBinderMapperImpl extends DataBinderMapper {
  private static final int LAYOUT_ACTIVITYADDUSER = 1;

  private static final int LAYOUT_ACTIVITYFAQ = 2;

  private static final int LAYOUT_ACTIVITYLOGIN = 3;

  private static final int LAYOUT_ACTIVITYUSERLIST = 4;

  private static final SparseIntArray INTERNAL_LAYOUT_ID_LOOKUP = new SparseIntArray(4);

  static {
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.trimax.vts.view.R.layout.activity_add_user, LAYOUT_ACTIVITYADDUSER);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.trimax.vts.view.R.layout.activity_faq, LAYOUT_ACTIVITYFAQ);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.trimax.vts.view.R.layout.activity_login, LAYOUT_ACTIVITYLOGIN);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.trimax.vts.view.R.layout.activity_user_list, LAYOUT_ACTIVITYUSERLIST);
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View view, int layoutId) {
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = view.getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
        case  LAYOUT_ACTIVITYADDUSER: {
          if ("layout/activity_add_user_0".equals(tag)) {
            return new ActivityAddUserBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_add_user is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYFAQ: {
          if ("layout/activity_faq_0".equals(tag)) {
            return new ActivityFaqBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_faq is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYLOGIN: {
          if ("layout/activity_login_0".equals(tag)) {
            return new ActivityLoginBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_login is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYUSERLIST: {
          if ("layout/activity_user_list_0".equals(tag)) {
            return new ActivityUserListBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_user_list is invalid. Received: " + tag);
        }
      }
    }
    return null;
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View[] views, int layoutId) {
    if(views == null || views.length == 0) {
      return null;
    }
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = views[0].getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
      }
    }
    return null;
  }

  @Override
  public int getLayoutId(String tag) {
    if (tag == null) {
      return 0;
    }
    Integer tmpVal = InnerLayoutIdLookup.sKeys.get(tag);
    return tmpVal == null ? 0 : tmpVal;
  }

  @Override
  public String convertBrIdToString(int localId) {
    String tmpVal = InnerBrLookup.sKeys.get(localId);
    return tmpVal;
  }

  @Override
  public List<DataBinderMapper> collectDependencies() {
    ArrayList<DataBinderMapper> result = new ArrayList<DataBinderMapper>(1);
    result.add(new androidx.databinding.library.baseAdapters.DataBinderMapperImpl());
    return result;
  }

  private static class InnerBrLookup {
    static final SparseArray<String> sKeys = new SparseArray<String>(1);

    static {
      sKeys.put(0, "_all");
    }
  }

  private static class InnerLayoutIdLookup {
    static final HashMap<String, Integer> sKeys = new HashMap<String, Integer>(4);

    static {
      sKeys.put("layout/activity_add_user_0", com.trimax.vts.view.R.layout.activity_add_user);
      sKeys.put("layout/activity_faq_0", com.trimax.vts.view.R.layout.activity_faq);
      sKeys.put("layout/activity_login_0", com.trimax.vts.view.R.layout.activity_login);
      sKeys.put("layout/activity_user_list_0", com.trimax.vts.view.R.layout.activity_user_list);
    }
  }
}
