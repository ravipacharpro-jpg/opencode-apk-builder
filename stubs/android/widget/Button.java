package android.widget;

import android.content.Context;
import android.view.View;

public class Button extends View {
    public Button(Context context) { super(context); }
    public void setText(CharSequence text) {}
    public void setText(int resId) {}
    public void setTextColor(int color) {}
    public void setTextSize(float size) {}
    public void setBackgroundResource(int resId) {}
    public void setAllCaps(boolean allCaps) {}
    public void setPadding(int left, int top, int right, int bottom) {}
    public void setEnabled(boolean enabled) {}
    public void setOnClickListener(OnClickListener l) {}
    public interface OnClickListener {
        void onClick(Button v);
    }
}
