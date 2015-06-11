package com.example.anna.shedule.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anna.shedule.R;
import com.example.anna.shedule.application.schedule.model.Change;
import com.example.anna.shedule.application.schedule.model.ChangeAction;
import com.example.anna.shedule.application.schedule.model.Lesson;
import com.example.anna.shedule.application.schedule.model.Teacher;
import com.example.anna.shedule.application.schedule.model.helper.LessonStatus;
import com.example.anna.shedule.application.schedule.model.helper.LessonTime;
import com.example.anna.shedule.application.schedule.model.helper.LessonType;
import com.example.anna.shedule.application.schedule.service.LessonsChangesService;
import com.example.anna.shedule.application.services.Services;
import com.example.anna.shedule.application.user.model.User;
import com.example.anna.shedule.application.user.model.UserType;
import com.example.anna.shedule.application.user.service.UserService;
import com.example.anna.shedule.utils.ContextUtils;

import java.util.Calendar;


public class LessonDetailsActivity extends AppCompatActivity {

    TextView teacherName;
    Spinner hullSpinner;
    Spinner timeSpinner;
    Spinner typeSpinner;
    Spinner statusSpinner;

    EditText titleEdit;
    EditText auditoryEdit;

    MenuItem save_button;

    Button addNoteButton;

    View[] views;

    Teacher teacher;
    Drawable spinnerDefaultBackground;

    Lesson lesson;
    long startOfDay;
    boolean isEditMode;

    UserType userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_details);
        ContextUtils.setContext(getApplicationContext());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        User user = Services.getService(UserService.class).getCurrentUser();
        if (user != null) {
            userType = user.getType();
        } else {
            userType = UserType.STUDENT;
        }

        Bundle extra = getIntent().getExtras();
        lesson = (Lesson) extra.getSerializable("lesson");
        isEditMode = extra.getBoolean("editMode");
        startOfDay = extra.getLong("startOfDay");

        findDataInputs();

        if (lesson != null) {
            setDataOnView(lesson);
            if (startOfDay <= 0) {
                startOfDay = lesson.getStartOfLessonDay();
            }
        }
    }

    private boolean isNewLesson() {
        return lesson == null;
    }

    private void setDataOnView(Lesson lesson) {
        teacher = new Teacher(lesson.getTeacherId(), lesson.getTeacherName());
        teacherName.setText(teacher.getName());
        titleEdit.setText(lesson.getTitle());
        if (lesson.getAuditory() != null) {
            auditoryEdit.setText(lesson.getAuditory());
        }
        setLessonStatus(lesson.getStatus());
        setLessonType(lesson.getType());
        setLessonTime(lesson.getTime());
        setLessonHull(lesson.getHull());
    }

    private void setLessonHull(String hull) {
        int pos;
        switch (hull) {
            case "I" : pos = 0; break;
            case "II" : pos = 1; break;
            case "III" : pos = 2; break;
            case "IV" : pos = 3; break;
            case "V" : pos = 4; break;
            default: pos = 0;
        }
        hullSpinner.setSelection(pos);
    }

    private void setLessonTime(LessonTime time) {
        int pos = time.getId();
        timeSpinner.setSelection(pos - 1);
    }

    private void setLessonType(LessonType type) {
        int pos = (type == LessonType.LECTURE) ? 1 : 0;
        typeSpinner.setSelection(pos);

    }

    private void setLessonStatus(LessonStatus status) {
        int pos = (status == LessonStatus.CANCELED) ? 1 : 0;
        statusSpinner.setSelection(pos);
    }

    public void findDataInputs() {
        teacherName = (TextView) findViewById(R.id.teacher_input);
        titleEdit = (EditText) findViewById(R.id.title_input);
        hullSpinner = (Spinner) findViewById(R.id.hull_input);
        timeSpinner = (Spinner) findViewById(R.id.time_input);
        typeSpinner = (Spinner) findViewById(R.id.type_input);
        statusSpinner = (Spinner) findViewById(R.id.status_input);
        auditoryEdit = (EditText) findViewById(R.id.auditory_input);
        addNoteButton = (Button) findViewById(R.id.add_mark);

        spinnerDefaultBackground = hullSpinner.getBackground();

        views = new View[] {
                teacherName, hullSpinner,
                timeSpinner, typeSpinner,
                statusSpinner, titleEdit,
                auditoryEdit};

        teacherName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LessonDetailsActivity.this, TeacherSelectorActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateNoteLayout.class);
                intent.putExtra("lessonId", lesson.getLessonId());
                intent.putExtra("changeId", lesson.getChangeId());
                intent.putExtra("startOfDay", lesson.getStartOfLessonDay());
                startActivity(intent);
            }
        });
    }

    public LessonType getLessonType() {
        int pos = typeSpinner.getSelectedItemPosition();
        return (pos == 0) ? LessonType.PRACTICE : LessonType.LECTURE;
    }

    public LessonStatus getLessonStatus() {
        int pos = statusSpinner.getSelectedItemPosition();
        return (pos == 0) ? LessonStatus.NORMAL: LessonStatus.CANCELED;
    }

    public String getHull() {
        int pos = hullSpinner.getSelectedItemPosition();
        switch (pos) {
            case 0: return "I";
            case 1: return "II";
            case 2: return "III";
            case 3: return "IV";
            case 4: return "V";
            default: return null;
        }
    }

    public LessonTime getLessonTime() {
        return LessonTime.getByTypeId(timeSpinner.getSelectedItemPosition() + 1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            teacher = (Teacher) data.getSerializableExtra("teacher");
            teacherName.setText(teacher.getName());
        }
    }

    public void setViewMode() {
        save_button.setVisible(false);
        if (userType == UserType.CLASS_LEADER || userType == UserType.TEACHER) {
            addNoteButton.setVisibility(View.VISIBLE);
        } else {
            addNoteButton.setVisibility(View.GONE);
        }

        teacherName.setTextColor(Color.LTGRAY);

        setTitle(getResources().getString(R.string.lesson_details_page_title));

        for (View view: views) {
            view.setClickable(false);
            view.setEnabled(false);

            if ((view instanceof Spinner)) {
                Spinner sp = (Spinner) view;
                sp.getSelectedView().setEnabled(false);
                sp.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }

    public void setEditMode() {
        save_button.setVisible(true);
        addNoteButton.setVisibility(View.GONE);

        setTitle(getString(R.string.lesson_edit_page_title));

        for (View view: views) {
            view.setClickable(true);
            view.setEnabled(true);
            if ((view instanceof Spinner)) {
                Spinner sp = (Spinner) view;
                sp.setBackgroundDrawable(spinnerDefaultBackground);
                sp.getSelectedView().setEnabled(true);
                sp.setEnabled(true);
            }
        }

        if (!isNewLesson()) {
            teacherName.setClickable(false);
            teacherName.setEnabled(false);

            timeSpinner.getSelectedView().setEnabled(false);
            timeSpinner.setEnabled(false);
            timeSpinner.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    public void saveChanges() {
        if (teacher == null) {
            Toast
                    .makeText(getApplicationContext(), R.string.teacher_not_set, Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        String title = titleEdit.getText().toString();
        if (title.isEmpty()) {
            Toast
                    .makeText(getApplicationContext(), R.string.title_not_set, Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        Change change;
        if (lesson != null && lesson.getChange() != null) {
            change = lesson.getChange();
        } else {
            change = new Change();
        }
        if (lesson != null) {
            change.setLessonId(lesson.getLessonId());
        }

        change.setHull(getHull());
        change.setAuditory(auditoryEdit.getText().toString());
        change.setTitle(title);
        change.setTeacher(teacher);
        change.setStatus(getLessonStatus());
        change.setType(getLessonType());
        change.setTime(getLessonTime());
        saveChange(change);
    }

    private void saveChange(Change change) {
        LessonsChangesService changesService = Services.getService(LessonsChangesService.class);
        ChangeAction action;
        if (change.getStatus() == LessonStatus.CANCELED && isNewLesson()) {
            finish();
            return;
        }

        if (change.getStatus() == LessonStatus.CANCELED && change.getChangeId() != null) {
            action = ChangeAction.DELETE;
        } else if (isNewLesson()) {
            UserService userService = Services.getService(UserService.class);
            change.setGroupIds(userService.getCurrentUser().getGroupId());
            action = ChangeAction.CREATE_NEW;

        } else if (lesson.getChangeId() == null){
            change.setTeacher(null);
            change.setGroupIds(null);
            change.setTime(null);
            action = ChangeAction.CREATE_BY_EXISTS;
        } else {
            action = ChangeAction.UPDATE;
        }


        final ProgressDialog prog = createProgressDialog();
        LessonsChangesService.ChangeListener listener = new LessonsChangesService.ChangeListener() {
            @Override
            public void onSuccess() {
                prog.dismiss();
                finish();
            }

            @Override
            public void onError() {
                LessonDetailsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast
                                .makeText(getApplicationContext(), R.string.error_create_change, Toast.LENGTH_SHORT)
                                .show();
                    }
                });
                prog.dismiss();
            }
        };


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startOfDay);
        changesService.doAction(action, change, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), listener);
    }

    private ProgressDialog createProgressDialog() {
        final ProgressDialog prog = new ProgressDialog(LessonDetailsActivity.this);
        prog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prog.setMessage(getString(R.string.pleas_wait));
        prog.setIndeterminate(true);
        prog.setCancelable(false);
        prog.setCanceledOnTouchOutside(false);
        prog.show();
        return prog;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_lesson_details, menu);

        save_button = menu.findItem(R.id.action_save);

        if (isEditMode || lesson == null) {
            setEditMode();
        } else {
            setViewMode();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_save) {
            saveChanges();
            return true;
        }

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
