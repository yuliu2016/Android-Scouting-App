package ca.warp7.android.scouting;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.List;

import ca.warp7.android.scouting.model.Specs;


public class ScheduleActivity extends AppCompatActivity {

    static class ScoutingScheduleAdapter extends ArrayAdapter<ManagedData.ScoutingScheduleItem> {

        LayoutInflater mInflater;

        ScoutingScheduleAdapter(@NonNull Context context,
                                List<ManagedData.ScoutingScheduleItem> scheduleItems) {
            super(context, 0, scheduleItems);
            mInflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View itemView;
            if (convertView != null && convertView instanceof LinearLayout) {
                itemView = convertView;
            } else {
                itemView = mInflater.inflate(R.layout.entry_list_item, parent, false);
            }

            ManagedData.ScoutingScheduleItem scoutingScheduleItem = getItem(position);
            if (scoutingScheduleItem != null &&
                    scoutingScheduleItem instanceof ManagedData.MatchWithAllianceItem) {

                ManagedData.MatchWithAllianceItem matchItem =
                        (ManagedData.MatchWithAllianceItem) scoutingScheduleItem;
                AllianceView allianceView = itemView.findViewById(R.id.alliance_view);
                allianceView.setAllianceFromScheduledMatchItem(matchItem);
                allianceView.setNoRobotFocused();
                TextView matchNumberView = itemView.findViewById(R.id.match_number);
                matchNumberView.setText(String.valueOf(matchItem.getMatchNumber()));
            }

            return itemView;
        }
    }


    ManagedData.ScoutingSchedule mScoutingSchedule;
    ListView mScheduleListView;

    private void onErrorDialog(Exception exception) {
        exception.printStackTrace();
        new AlertDialog.Builder(this)
                .setTitle("An error occurred")
                .setMessage(exception.toString())
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                })
                .create().show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));
        setTitle("Match Schedule");

        Spinner spinner = findViewById(R.id.board_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.board_choices, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        mScheduleListView = findViewById(R.id.entry_list);
        mScoutingSchedule = new ManagedData.ScoutingSchedule();

        try {
            mScoutingSchedule.loadFullScheduleFromCSV(
                    new File(Specs.getSpecsRoot(), "match-table.csv"));
        } catch (IOException exception) {
            onErrorDialog(exception);
        }

        mScoutingSchedule.scheduleForDisplayOnly();

        mScheduleListView.setAdapter(new ScoutingScheduleAdapter(this,
                mScoutingSchedule.getCurrentlyScheduled()));

//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                mScheduleListView.setAdapter(new ArrayAdapter<>(ScheduleActivity.this,
//                        android.R.layout.simple_list_item_1,
//                        mScoutingSchedule.getTeamsArrayForBoard(position)));
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
    }

    static class AllianceView extends View {

        static final float kPad = 24;
        static final int kRadius = 16;

        Paint mAlmostRedPaint = new Paint();
        Paint mAlmostBluePaint = new Paint();
        Paint mAlmostBlackTextPaint = new Paint();
        Paint mGrayTextPaint = new Paint();
        Paint mRedTextPaint = new Paint();
        Paint mBlueTextPaint = new Paint();
        Paint mRedBoldTextPaint = new Paint();
        Paint mBlueBoldTextPaint = new Paint();

        private float mMinimumWidth;

        private String mR1 = "Red 1";
        private String mR2 = "Red 2";
        private String mR3 = "Red 3";
        private String mB1 = "Blue 1";
        private String mB2 = "Blue 2";
        private String mB3 = "Blue 3";

        private ManagedData.RobotPosition mFocusedRobotPosition = ManagedData.RobotPosition.RED1;
        private boolean mShouldFocusARobot = false;


        public AllianceView(Context context) {
            super(context);
            initPaints();
        }

        public AllianceView(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            initPaints();
        }

        private void setTextPaintStyle(Paint paint, boolean bold) {
            paint.setTextSize(36);
            paint.setAntiAlias(true);
            paint.setTypeface(bold ? Typeface.create(Typeface.SANS_SERIF,
                    Typeface.BOLD) : Typeface.SANS_SERIF);
        }

        private void initPaints() {
            mAlmostRedPaint.setColor(getResources().getColor(R.color.colorAlmostRed));
            mAlmostBluePaint.setColor(getResources().getColor(R.color.colorAlmostBlue));

            mAlmostBlackTextPaint.setColor(getResources().getColor(R.color.colorAlmostBlack));
            mGrayTextPaint.setColor(getResources().getColor(R.color.colorGray));
            mRedTextPaint.setColor(getResources().getColor(R.color.colorRed));
            mBlueTextPaint.setColor(getResources().getColor(R.color.colorBlue));
            mRedBoldTextPaint.setColor(getResources().getColor(R.color.colorRed));
            mBlueBoldTextPaint.setColor(getResources().getColor(R.color.colorBlue));

            setTextPaintStyle(mAlmostBlackTextPaint, false);
            setTextPaintStyle(mGrayTextPaint, false);
            setTextPaintStyle(mRedTextPaint, false);
            setTextPaintStyle(mBlueTextPaint, false);
            setTextPaintStyle(mRedBoldTextPaint, true);
            setTextPaintStyle(mBlueBoldTextPaint, true);

            mMinimumWidth = mRedBoldTextPaint.measureText("8888");
        }

        public void setFocusedRobotPosition(ManagedData.RobotPosition position) {
            mFocusedRobotPosition = position;
            mShouldFocusARobot = true;
        }

        public void setNoRobotFocused() {
            mShouldFocusARobot = false;
        }

        public void setAllianceFromScheduledMatchItem(ManagedData.MatchWithAllianceItem matchItem) {
            mR1 = String.valueOf(matchItem.getTeamAt(0));
            mR2 = String.valueOf(matchItem.getTeamAt(1));
            mR3 = String.valueOf(matchItem.getTeamAt(2));
            mB1 = String.valueOf(matchItem.getTeamAt(3));
            mB2 = String.valueOf(matchItem.getTeamAt(4));
            mB3 = String.valueOf(matchItem.getTeamAt(5));
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            final int w = getMeasuredWidth();
            final int h = getMeasuredHeight();
            canvas.drawRect(0, kRadius, w, h / 2, mAlmostRedPaint);
            canvas.drawRect(kRadius, 0, w - kRadius, kRadius, mAlmostRedPaint);
            canvas.drawCircle(kRadius, kRadius, kRadius, mAlmostRedPaint);
            canvas.drawCircle(w - kRadius, kRadius, kRadius, mAlmostRedPaint);
            canvas.drawRect(0, h / 2, w, h - kRadius, mAlmostBluePaint);
            canvas.drawRect(kRadius, h - kRadius, w - kRadius, h, mAlmostBluePaint);
            canvas.drawCircle(kRadius, h - kRadius, kRadius, mAlmostBluePaint);
            canvas.drawCircle(w - kRadius, h - kRadius, kRadius, mAlmostBluePaint);
            canvas.drawLine(w / 3, 0, w / 3, h, mGrayTextPaint);
            canvas.drawLine(w / 3 * 2, 0, w / 3 * 2, h, mGrayTextPaint);
            canvas.drawLine(0, h / 2, w, h / 2, mGrayTextPaint);
            Paint R1Paint = mShouldFocusARobot ? (mFocusedRobotPosition ==
                    ManagedData.RobotPosition.RED1 ?
                    mRedBoldTextPaint : mGrayTextPaint) : mAlmostBlackTextPaint;
            Paint R2Paint = mShouldFocusARobot ? (mFocusedRobotPosition ==
                    ManagedData.RobotPosition.RED2 ?
                    mRedBoldTextPaint : mGrayTextPaint) : mAlmostBlackTextPaint;
            Paint R3Paint = mShouldFocusARobot ? (mFocusedRobotPosition ==
                    ManagedData.RobotPosition.RED3 ?
                    mRedBoldTextPaint : mGrayTextPaint) : mAlmostBlackTextPaint;
            Paint B1Paint = mShouldFocusARobot ? (mFocusedRobotPosition ==
                    ManagedData.RobotPosition.BLUE1 ?
                    mBlueBoldTextPaint : mGrayTextPaint) : mAlmostBlackTextPaint;
            Paint B2Paint = mShouldFocusARobot ? (mFocusedRobotPosition ==
                    ManagedData.RobotPosition.BLUE2 ?
                    mBlueBoldTextPaint : mGrayTextPaint) : mAlmostBlackTextPaint;
            Paint B3Paint = mShouldFocusARobot ? (mFocusedRobotPosition ==
                    ManagedData.RobotPosition.BLUE3 ?
                    mBlueBoldTextPaint : mGrayTextPaint) : mAlmostBlackTextPaint;
            canvas.drawText(mR1, (w / 3 - R1Paint
                    .measureText(mR1)) / 2, h / 2 - 16, R1Paint);
            canvas.drawText(mR2, w / 3 + (w / 3 - R2Paint
                    .measureText(mR2)) / 2, h / 2 - 16, R2Paint);
            canvas.drawText(mR3, w / 3 * 2 + (w / 3 - R3Paint
                    .measureText(mR3)) / 2, h / 2 - 16, R3Paint);
            canvas.drawText(mB1, (w / 3 - B1Paint
                    .measureText(mB1)) / 2, h - 16, B1Paint);
            canvas.drawText(mB2, w / 3 + (w / 3 - B2Paint
                    .measureText(mB2)) / 2, h - 16, B2Paint);
            canvas.drawText(mB3, w / 3 * 2 + (w / 3 - B3Paint
                    .measureText(mB3)) / 2, h - 16, B3Paint);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

            setMeasuredDimension((int) (mMinimumWidth * 3 + kPad * 6), 120);
        }
    }
}