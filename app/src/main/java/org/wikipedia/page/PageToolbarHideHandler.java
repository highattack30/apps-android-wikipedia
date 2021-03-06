package org.wikipedia.page;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;

import org.wikipedia.R;
import org.wikipedia.util.DimenUtil;

public class PageToolbarHideHandler extends ViewHideHandler {
    private static final int FULL_OPACITY = 255;

    private boolean fadeEnabled;
    private boolean forceNoFade;
    @NonNull private final Drawable toolbarBackground;
    @NonNull private final Drawable statusBar;

    public PageToolbarHideHandler(@NonNull View hideableView) {
        super(hideableView, Gravity.TOP);
        toolbarBackground = hideableView.getBackground().mutate();
        statusBar = hideableView.findViewById(R.id.empty_status_bar).getBackground().mutate();
    }

    /**
     * Whether to enable fading in/out of the search bar when near the top of the article.
     * @param enabled True to enable fading, false otherwise.
     */
    public void setFadeEnabled(boolean enabled) {
        fadeEnabled = enabled;
        update();
    }

    /**
     * Whether to temporarily disable fading of the search bar, even if fading is enabled otherwise.
     * May be used when displaying a temporary UI element that requires the search bar to be shown
     * fully, e.g. when the ToC is pulled out.
     * @param force True to temporarily disable fading, false otherwise.
     */
    public void setForceNoFade(boolean force) {
        forceNoFade = force;
        update();
    }

    @Override
    protected void onScrolled(int oldScrollY, int scrollY) {
        int opacity = calculateScrollOpacity(scrollY);
        toolbarBackground.setAlpha(opacity);
        statusBar.setAlpha(opacity);
    }

    /** @return Alpha value between 0 and 0xff. */
    private int calculateScrollOpacity(int scrollY) {
        final int fadeHeight = 200;
        int opacity = FULL_OPACITY;
        if (fadeEnabled && !forceNoFade) {
            opacity = scrollY * FULL_OPACITY / (int) (fadeHeight * DimenUtil.getDensityScalar());
        }
        opacity = Math.max(0, opacity);
        opacity = Math.min(FULL_OPACITY, opacity);
        return opacity;
    }
}
