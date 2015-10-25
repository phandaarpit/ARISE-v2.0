package arise.arise.org.arise;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ListView;
import android.widget.TextView;

import org.arise.CustomView.SingleScrollListView;
import org.arise.adapters.LecturesListAdapter;
import org.arise.fragments.NavigationDrawer;
import org.arise.listeners.LectureListListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/***
 * This class views the description of the course.
 * Also holds a singleScrollList which views only one listItem at a time on the screen
 *
 * Issue being faced: onItemClick calls onWindowFocusChanged which was initializing the whole
 * views again and therefore when user clicked on the listItem, for a short time, item 0 used to be
 * displayed. Short time but very visible.
 *
 * Resolution:
 * ViewTreeObserver
 */
public class CourseDetailsActivity extends BaseActivity {

    private Context context;

    private SingleScrollListView listLecture;
    private TextView courseDescription;
    private View main_view;
    private Toolbar toolbar;
    private ViewTreeObserver vto;

    private JSONObject courseFromJSON;
    private JSONArray lectureArray = null;

    private String courseName = "";
    private String courseDesc = "";
    private boolean completed;
    private boolean current;
    private int courseID = 0;

    //variables for the hack
    private static boolean initialized = false;
    public static boolean backPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);
        context = this;

        main_view = findViewById(R.id.course_details_id);

        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        Bundle bundle = getIntent().getExtras();
        String course = bundle.getString("Course");
        completed = bundle.getBoolean("completed");
        current = bundle.getBoolean("current");

        try {
            courseFromJSON = new JSONObject(course);
            courseName = courseFromJSON.getString("course_name");
            courseID = courseFromJSON.getInt("courseID");
            courseDesc = courseFromJSON.getString("course_description");
            lectureArray = courseFromJSON.getJSONArray("lectures");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(courseName);

        courseDescription  = (TextView) findViewById(R.id.course_description_complete);

        listLecture = (SingleScrollListView) findViewById(R.id.lecture_list);
        vto = listLecture.getViewTreeObserver();

        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                listLecture.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                LecturesListAdapter.height = listLecture.getMeasuredHeight();
                Log.d("Dimensions","Height : " +LecturesListAdapter.height);
                LecturesListAdapter.width = listLecture.getMeasuredWidth();
                Log.d("Dimensions","Width : " +LecturesListAdapter.width);
                listLecture.setAdapter(new LecturesListAdapter(lectureArray,context, completed, current));
                listLecture.setOnItemClickListener(new LectureListListener(context, lectureArray, completed, current, courseID));
                listLecture.setSingleScroll(true);
            }
        });



        NavigationDrawer drawer = (NavigationDrawer)getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawer.setUpDrawer(drawerLayout,toolbar, R.id.fragment_navigation_drawer, main_view);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_course_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        courseDescription.setText(courseDesc);
    }

}
