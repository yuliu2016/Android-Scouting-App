package ca.warp7.android.scouting.v4.components;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import ca.warp7.android.scouting.R;
import ca.warp7.android.scouting.v4.abstraction.ScoutingScheduleItem;
import ca.warp7.android.scouting.v4.model.ButtonItem;
import ca.warp7.android.scouting.v4.model.MatchWithAllianceItem;
import ca.warp7.android.scouting.v4.widgets.AllianceView;

import java.util.List;

public class ScoutingScheduleAdapter extends ArrayAdapter<ScoutingScheduleItem> {

    private final LayoutInflater mInflater;

    public ScoutingScheduleAdapter(@NonNull Context context,
                                   List<ScoutingScheduleItem> scheduleItems) {
        super(context, 0, scheduleItems);
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ScoutingScheduleItem scoutingScheduleItem = getItem(position);
        if (scoutingScheduleItem instanceof MatchWithAllianceItem) {

            View itemView;
            if (convertView instanceof LinearLayout) {
                itemView = convertView;
            } else {
                itemView = mInflater.inflate(R.layout.list_item_schedule_match, parent, false);
            }

            MatchWithAllianceItem matchItem = (MatchWithAllianceItem) scoutingScheduleItem;
            AllianceView allianceView = itemView.findViewById(R.id.alliance_view);
            allianceView.setDataFromScheduledMatchItem(matchItem);
            allianceView.invalidate(); // Fix cached image in convert view

            TextView matchNumberView = itemView.findViewById(R.id.match_number);
            matchNumberView.setText(String.valueOf(matchItem.getMatchNumber()));

            return itemView;
        } else if (scoutingScheduleItem instanceof ButtonItem) {
            View itemView;
            if (convertView instanceof LinearLayout) {
                itemView = convertView;
            } else {
                itemView = mInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            }
            ButtonItem buttonItem = (ButtonItem) scoutingScheduleItem;

            if (itemView instanceof TextView) {
                TextView textView = (TextView) itemView;
                textView.invalidate();
                textView.setText(buttonItem.getText());
            }

            return itemView;
        }
        return new View(getContext());
    }
}