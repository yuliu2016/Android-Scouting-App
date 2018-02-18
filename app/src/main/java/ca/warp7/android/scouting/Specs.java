package ca.warp7.android.scouting;

import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Data Model for reading files with constants
 * Can be used as a singleton
 */

class Specs {

    class Index {

        private ArrayList<String> files = new ArrayList<>();

        ArrayList<String> names = new ArrayList<>();

        ArrayList<String> identifiers = new ArrayList<>();


        Index(File file){
            try {
                JSONObject index = new JSONObject(readFile(file));

                JSONArray files = index.getJSONArray("files");
                JSONArray names = index.getJSONArray("names");
                JSONArray ids = index.getJSONArray("identifiers");

                for(int i = 0; i < ids.length(); i++){
                    this.files.add(files.getString(i));
                    this.names.add(names.getString(i));
                    this.identifiers.add(ids.getString(i));
                }

            } catch (IOException | JSONException e){
                e.printStackTrace();
            }
        }

        public ArrayList<String> getNames() {
            return names;
        }

        public String getFileByName(String name) {
            return files.get(names.indexOf(name));
        }
    }


    class DataConstant {

        private static final String C_ID = "id";

        private static final String C_LOG = "log";

        private static final String C_LABEL = "display";

        private static final String C_TYPE = "type";

        private static final String C_MIN = "min";

        private static final String C_MAX = "max";

        private static final String C_CHOICES = "choices";


        private static final String T_TIME = "time";

        private static final String T_CHOICE = "choice";

        private static final String T_RATING = "rating";


        static final int TIME = 0;

        static final int CHOICE = 1;

        static final int RATING = 2;



        final int index;

        final String id;

        final String logTitle;

        final String label;

        final int type;

        final int min;

        final int max;

        final String[] choices;

        DataConstant(int index, JSONObject data) throws JSONException {

            this.index = index;
            this.id = data.getString(C_ID);

            logTitle = data.has(C_LOG) ? data.getString(C_LOG) : "$" + id;

            label = data.has(C_LABEL) ? data.getString(C_LABEL) : "$" + id;

            type = data.has(C_TYPE) ? toIntegerType(data.getString(C_TYPE)) : -1;

            min = data.has(C_MIN) ? data.getInt(C_MIN) : -1;

            max = data.has(C_MAX) ? data.getInt(C_MAX) : -1;

            if (data.has(C_CHOICES)){
                JSONArray ca = data.getJSONArray(C_CHOICES);
                choices = new String[ca.length()];

                for (int i = 0; i < choices.length; i++){
                    choices[i] = ca.getString(i);
                }

            } else {
                choices = new String[]{"None"};
            }
        }

        public int getIndex() {
            return index;
        }

        public String getId() {
            return id;
        }

        public String getLogTitle() {
            return logTitle;
        }

        public String getLabel() {
            return label;
        }

        public int getType() {
            return type;
        }

        public int getMin() {
            return min;
        }

        public int getMax() {
            return max;
        }

        public String[] getChoices() {
            return choices;
        }

    }

    private static int parseHex32(String h) {
        return Integer.parseInt(h.substring(0, 4), 16) << 16
                + Integer.parseInt(h.substring(4, 8), 16);
    }

    private static int toIntegerType(String type){

        switch (type) {
            case DataConstant.T_TIME:
                return DataConstant.TIME;

            case DataConstant.T_CHOICE:
                return DataConstant.CHOICE;

            case DataConstant.T_RATING:
                return DataConstant.RATING;
        }
        return -1;
    }

    private static String readFile(File f) throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(f));
        StringBuilder sb = new StringBuilder();

        String line = br.readLine();

        while (line != null) {
            sb.append(line);
            line = br.readLine();
        }

        br.close();
        return sb.toString();
    }


    private static final String SPECS_ROOT = "Warp7/specs/";

    private static final String ID = "board_id";

    private static final String BOARD_NAME = "board_name";

    private static final String MATCH_SCHEDULE = "board_matches";

    private static final String CONSTANTS = "board_constants";

    private static Specs activeSpecs;


    static File getSpecsRoot(){
        File r = new File(Environment.getExternalStorageDirectory(), SPECS_ROOT);
        r.mkdirs();
        return r;
    }

    static Specs getActiveSpecs(){
        return activeSpecs;
    }

    static Specs createActiveSpecs(String filename){
        try{

           activeSpecs = new Specs(readFile(new File(getSpecsRoot(), filename)));

        } catch (IOException | JSONException e){

           e.printStackTrace();
           activeSpecs = null;

        }

        return activeSpecs;
    }


    private int specsId;
    private String boardName;

    private ArrayList<Integer> matchSchedule = new ArrayList<>();
    private ArrayList<DataConstant> dataConstants = new ArrayList<>();


    Specs(String json) throws JSONException{

        JSONObject specsObject = new JSONObject(json);

        specsId = parseHex32(specsObject.getString(ID));
        boardName = specsObject.getString(BOARD_NAME);

        if(specsObject.has(MATCH_SCHEDULE)){
            JSONArray schedule = specsObject.getJSONArray(MATCH_SCHEDULE);

            matchSchedule.clear();

            for(int i = 0; i < schedule.length(); i++) {
                matchSchedule.add(schedule.getInt(i));
            }
        }

        if(specsObject.has(CONSTANTS)){
            JSONArray constants = specsObject.getJSONArray(CONSTANTS);

            dataConstants.clear();

            for (int i = 0; i < constants.length(); i++){
                dataConstants.add(new DataConstant(i, constants.getJSONObject(i)));
            }
        }

    }

    boolean hasMatchSchedule(){
        return !matchSchedule.isEmpty();
    }

    boolean matchExistsInSchedule(int m, int t){
        return hasMatchSchedule() &&
                t == (m < matchSchedule.size() && m >= 0 ? matchSchedule.get(m) : -1);
    }

}