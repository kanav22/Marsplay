package www.kanavwadhawan.com.marsplay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;

import java.util.ArrayList;

import www.kanavwadhawan.com.marsplay.fresco.zoomable.ZoomableDraweeView;

public class ImageViewDialogActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view_dialog);
        ViewPager viewPager = findViewById(R.id.pager);
        Fresco.initialize(this);
        Intent i = getIntent();
        ArrayList<String> imageArrayList = i.getStringArrayListExtra("imageArray");
        int position = i.getIntExtra("position", 0);
        FullScreenImageAdapter adapter = new FullScreenImageAdapter(ImageViewDialogActivity.this,
                imageArrayList);
        viewPager.setAdapter(adapter);
        // displaying selected image first
        viewPager.setCurrentItem(position);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    class FullScreenImageAdapter extends PagerAdapter {

        private final Activity _activity;
        private final ArrayList<String> _imagePaths;
        private LayoutInflater inflater;

        // constructor
        FullScreenImageAdapter(Activity activity,
                               ArrayList<String> imagePaths) {
            this._activity = activity;
            this._imagePaths = imagePaths;
        }

        @Override
        public int getCount() {
            return this._imagePaths.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            //PinchToZoomDraweeView imgDisplay;
            Button btnClose;
            ZoomableDraweeView imgDisplay;
            inflater = (LayoutInflater) _activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View viewLayout = inflater.inflate(R.layout.multiimage_view_layout, container,
                    false);
            imgDisplay = viewLayout.findViewById(R.id.imgDisplay);
            btnClose = viewLayout.findViewById(R.id.btnClose);
            DraweeController ctrl = Fresco.newDraweeControllerBuilder().setUri(
                    Uri.parse(_imagePaths.get(position))).setTapToRetryEnabled(true).build();
            GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(getResources())
                    .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                    /*.setProgressBarImage(new ProgressBarDrawable())*/
                    .build();

            imgDisplay.setController(ctrl);
            imgDisplay.setHierarchy(hierarchy);
            // close button click event
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _activity.finish();
                }
            });

            container.addView(viewLayout);

            return viewLayout;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((RelativeLayout) object);

        }
    }
}
