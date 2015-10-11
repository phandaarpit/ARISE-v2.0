package org.arise.tabs.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.arise.adapters.CoursesListAdapter;
import org.arise.enums.CourseRequestType;
import org.arise.enums.CourseStatus;
import org.arise.enums.Options;
import org.arise.gesture.CustomGestureDetector;
import org.arise.interfaces.IAsyncInterface;
import org.arise.listeners.CourseListListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import arise.arise.org.arise.AsyncTaskManager;
import arise.arise.org.arise.R;

/**
 * Created by Arpit Phanda on 3/5/2015.
 */
public class AllCoursesDetail extends Fragment implements IAsyncInterface{
    private final String url ="http://ariseimpactapps.in/audiolearningapp/course_details.php";
    JSONArray courses;
    private View layout;
    private ListView list;

    private GestureDetector gestureDetector;
    private View.OnTouchListener gestureListener;

    public AllCoursesDetail()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment,container,false);
        list = (ListView) layout.findViewById(R.id.list_course_lectures);
        return layout;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair(CourseRequestType.TYPE.toString(), CourseRequestType.ALL.toString()));
        nameValuePairs.add(new BasicNameValuePair("url", url));
        new AsyncTaskManager(Options.ALL_COURSES,this, getActivity()).execute(nameValuePairs);

        gestureDetector = new GestureDetector(new CustomGestureDetector());
        gestureListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };

        super.onCreate(savedInstanceState);

    }

    @Override
    public void parseJSON(String jsonResponse) {
        JSONObject completeResponse;
        JSONArray courses;
        boolean status;

        try {
            completeResponse = new JSONObject(jsonResponse);
            status = completeResponse.getBoolean("success");
            if(status)
            {
                courses = completeResponse.getJSONArray("courses");
                this.courses = courses;

                //added a new parameter to keep a check on status of the course
                list.setAdapter(new CoursesListAdapter(getActivity(),this.courses, CourseStatus.ALL, layout.getHeight(), layout.getWidth()));
                list.setOnItemClickListener(new CourseListListener(getActivity(), courses, CourseStatus.ALL));
                list.setOnTouchListener(gestureListener);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
