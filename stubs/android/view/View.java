package android.view;

public class View {
    public View(android.content.Context context) {}
    public void setVisibility(int visibility) {}
    public void setOnClickListener(android.view.View.OnClickListener l) {}
    public void setBackgroundColor(int color) {}
    public interface OnClickListener {
        void onClick(View v);
    }
}
