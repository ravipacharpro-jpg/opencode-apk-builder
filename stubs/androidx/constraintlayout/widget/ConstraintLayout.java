package androidx.constraintlayout.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class ConstraintLayout extends ViewGroup {
    public ConstraintLayout(Context context) { super(context); }
    public static class LayoutParams extends ViewGroup.LayoutParams {
        public LayoutParams(int width, int height) { super(width, height); }
        public LayoutParams setMarginStart(int margin) { return this; }
        public LayoutParams setMarginEnd(int margin) { return this; }
        public int topMargin = 0;
        public int bottomMargin = 0;
        public int leftMargin = 0;
        public int rightMargin = 0;
    }
}
