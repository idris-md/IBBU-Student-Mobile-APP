package com.ibbumobile;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class calculator extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private List<Score> scoreList;
    private ScoreListAdapter mAdapter;
    private CoordinatorLayout coordinatorLayout;

    String code;
    String unit = "Credit Load";
    String grade = "Score Grade";

    Spinner spinnerUnit;
    Spinner spinnerGrade;
    TextView resultView;
    int scores;
    Score score;
    int scorePoint;
    String scoreGrade;
    int scoreUnit;

    float totalunit;
    float totalPoint;
    boolean isFomated;
    float gpa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_calculator);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final RecyclerView recyclerView = findViewById(R.id.recycler);
        Button saveBtn = findViewById(R.id.btnSave);
        final EditText course = findViewById(R.id.course);
        course.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString();
                if (text.length() == 3 && !text.contains(" ") && !isFomated) {

                    text = charSequence + " ";
                    course.setText(text.toUpperCase());
                    course.setSelection(4);
                    course.setInputType(InputType.TYPE_CLASS_NUMBER);
                    isFomated = true;

                }else if (text.length() < 3){
                    course.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                    isFomated = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });

        resultView = findViewById(R.id.result);

        spinnerUnit = (Spinner) findViewById(R.id.unit);
        spinnerGrade = findViewById(R.id.grade);

        String unitItem[] = {"Credit Load", "1", "2", "3", "4", "5", "6"};
        String gradeItem[] = {"Score Grade", "A", "B", "C", "D", "F"};

        spinnerGrade.setAdapter(getSpinnerAdapter(gradeItem));
        spinnerUnit.setAdapter(getSpinnerAdapter(unitItem));

        spinnerUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                unit = adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                unit = "Credit Load";
            }
        });
        spinnerGrade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                grade = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                grade = "Score Grade";
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                code = course.getText().toString();
                if (code.length() < 7) {
                    showTast("Please enter valid course code");
                    return;
                }

                if (unit.equalsIgnoreCase("Credit Load")) {
                    showTast("Please select Credit Load");
                    return;
                }

                if (grade.equalsIgnoreCase("Score Grade")) {
                    showTast("Please select Score Grade");
                    return;
                }

                scoreList.add(new Score(code, Integer.parseInt(unit), grade));
                mAdapter = new ScoreListAdapter(getBaseContext(), scoreList);
                recyclerView.setAdapter(mAdapter);

                prepareScores();

                /////////////// Reset Fields

                course.setText(null);
                spinnerGrade.setSelection(0);
                spinnerUnit.setSelection(0);

            }
        });

        getSupportActionBar().setTitle(getString(R.string.calculator));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        coordinatorLayout = findViewById(R.id.coordinator_layout);
        scoreList = new ArrayList<>();
        mAdapter = new ScoreListAdapter(this, scoreList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);


    }


    private void prepareScores() {

         scores = mAdapter.getItemCount();


         totalunit = 0;
         totalPoint = 0;


        for (int i = 0; i < scores; i++) {

            score = mAdapter.getItem(i);
            scoreGrade = score.getGrade();
            scoreUnit = score.getLoad();
            scorePoint = getPoint(scoreGrade, scoreUnit);
            totalunit += scoreUnit;
            totalPoint += scorePoint;

        }

        gpa = totalPoint / totalunit;
        BigDecimal decimal = new BigDecimal(Float.toString(gpa));
        decimal=decimal.setScale(2, BigDecimal.ROUND_HALF_UP );

        resultView.setText(scores + " Course :: " + ((int) totalunit) + " Credit Load :: GPA = " + decimal.doubleValue());

    }

    private int getPoint(String scoreGrade, int scoreUnit) {

        int scorePoint = 0;

        switch (scoreGrade) {

            case "A":
                scorePoint = 5 * scoreUnit;
                break;

            case "B":
                scorePoint = 4 * scoreUnit;
                break;

            case "C":
                scorePoint = 3 * scoreUnit;
                break;

            case "D":
                scorePoint = 2 * scoreUnit;
                break;
            case "F":
                scorePoint = 0;
        }
        return scorePoint;
    }

    /**
     * callback when recycler view is swiped
     * item will be removed on swiped
     * undo option will be provided in snackbar to restore the item
     */

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof ScoreListAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            String cCode = scoreList.get(viewHolder.getAdapterPosition()).getCourse();

            // backup of removed item for undo purpose
            final Score deletedItem = scoreList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, cCode + " removed!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO REMOVE", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // undo is selected, restore the deleted item
                    mAdapter.restoreItem(deletedItem, deletedIndex);
                    prepareScores();
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
            if (!scoreList.isEmpty()) {
                prepareScores();
            } else {
                resultView.setText("0 Course :: 0 Credit Load :: GPA = 0.00");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds cartList to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    private void showTast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private ArrayAdapter<String> getSpinnerAdapter(String[] items) {


        final List<String> plantsList = new ArrayList<>(Arrays.asList(items));

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, plantsList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        return spinnerArrayAdapter;

    }
}