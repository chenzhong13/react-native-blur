package com.cmcewen.blurview;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.ArrayList;
import java.util.List;


public class BlurViewManager extends SimpleViewManager<BlurringView> {
    public static final String REACT_CLASS = "BlurView";

    public static final int defaultRadius = 10;
    public static final int defaultSampling = 10;

    private static ThemedReactContext context;

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    public BlurringView createViewInstance(ThemedReactContext ctx) {
        context = ctx;

        BlurringView blurringView = new BlurringView(ctx);
        blurringView.setBlurRadius(defaultRadius);
        blurringView.setDownsampleFactor(defaultSampling);
        return blurringView;
    }

    @ReactProp(name = "blurRadius", defaultInt = defaultRadius)
    public void setRadius(BlurringView view, int radius) {
        view.setBlurRadius(radius);
        view.invalidate();
    }

    @ReactProp(name = "overlayColor", customType = "Color")
    public void setColor(BlurringView view, int color) {
        view.setOverlayColor(color);
        view.invalidate();
    }

    @ReactProp(name = "downsampleFactor", defaultInt = defaultSampling)
    public void setDownsampleFactor(BlurringView view, int factor) {
        view.setDownsampleFactor(factor);
    }

//    @ReactProp(name = "viewRef")
//    public void setViewRef(BlurringView view, int viewRef) {
//        if (context != null && context.getCurrentActivity() != null) {
//          View viewToBlur = context.getCurrentActivity().findViewById(viewRef);
//
//          if (viewToBlur != null) {
//              view.setBlurredView(viewToBlur);
//          }
//        }
//    }

    @ReactProp(name = "viewRef")
    public void setViewRef(BlurringView view, int viewRef) {
        if (context != null && context.getCurrentActivity() != null) {
            List<View> views = new ArrayList<>();
            getAllViews(views, context.getCurrentActivity().getWindow().getDecorView());

            View viewToBlur = null;
            for (View childView : views) {
                if (childView instanceof WebView) {
                    viewToBlur = childView;
                }
            }

            if (viewToBlur != null) {
                view.setBlurredView(viewToBlur);
            }
        }
    }

    private static void getAllViews(@NonNull List<View> resultViews, @NonNull View parentView) {
        resultViews.add(parentView);
        if (parentView instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) parentView).getChildCount(); i++) {
                getAllViews(resultViews, ((ViewGroup) parentView).getChildAt(i));
            }
        }
    }
}
