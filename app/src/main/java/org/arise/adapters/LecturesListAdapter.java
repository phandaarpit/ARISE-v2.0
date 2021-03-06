package org.arise.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.arise.textToSpeech.TTSInitListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import arise.arise.org.arise.R;

/**
 * Created by arpit on 15/3/15.
 */
public class LecturesListAdapter extends BaseAdapter{

    private JSONArray lectures;
    private Context context;
    private boolean completed;
    private boolean current;
    public static int height;
    public static int width;
    private TTSInitListener tts;
    private String text;
    private final String tag = "LectureList";

    public LecturesListAdapter(JSONArray lectureArray, Context context, Boolean completed, Boolean current) {
        this.lectures = lectureArray;
        this.context = context;
        this.completed = completed;
        this.current = current;
        tts = TTSInitListener.getInstance();
    }

    @Override
    public int getCount() {
        return lectures.length();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        tts.snooze();
        View row;
        JSONObject lecture = null;
        String lectureName = "";
        boolean lectureComplete = false;

        try {
            lecture = lectures.getJSONObject(i);
            lectureName = lecture.getString("name");

            if(current)
            {
                lectureComplete = lecture.getBoolean("completed");
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.lectures_list_item_indicator, null);
            row.setLayoutParams(new ViewGroup.LayoutParams(this.width,this.height));
        }
        else {
            row = view;
        }

        TextView title = (TextView) row.findViewById(R.id.lecture_title);
        title.setText(lectureName);
        TextView toFill = (TextView) row.findViewById(R.id.lecture_status);

        if(current)
        {
            if(lectureComplete)
            {
                toFill.setText("COMPLETE");
                toFill.setBackgroundColor(Color.parseColor("#b2ff59"));
            }
            else if(!lectureComplete)
            {
                toFill.setText("IN PROGRESS");
                toFill.setBackgroundColor(Color.parseColor("#ff5252"));
            }
        }
        else if(completed)
        {
            toFill.setText("COMPLETE");
            toFill.setBackgroundColor(Color.parseColor("#b2ff59"));
        }

        text = "You are on Lecture   "+lectureName;
        tts.setText(text);
        tts.speakOut();

        return row;
    }

}
