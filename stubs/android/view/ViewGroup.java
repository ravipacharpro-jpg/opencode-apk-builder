package android.view;

public class ViewGroup extends View {
    public ViewGroup(android.content.Context context) { super(context); }
    public void addView(View child) {}
    public void addView(View child, android.view.ViewGroup.LayoutParams params) {}
    public void removeAllViews() {}
    public void removeAllViewsInLayout() {}
    public void removeView(View view) {}
    public static class LayoutParams {
        public int width;
        public int height;
        public int topMargin;
        public int bottomMargin;
        public int leftMargin;
        public int rightMargin;
        public LayoutParams(int width, int height) { this.width = width; this.height = height; }
    }
}
