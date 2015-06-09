package com.example.anna.shedule.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.anna.shedule.R;
import com.example.anna.shedule.application.login.model.LoginError;
import com.example.anna.shedule.application.login.model.LoginProgress;
import com.example.anna.shedule.application.login.service.LoginService;
import com.example.anna.shedule.application.schedule.model.Group;
import com.example.anna.shedule.application.schedule.service.GroupService;
import com.example.anna.shedule.application.services.Services;
import com.example.anna.shedule.application.user.model.User;
import com.example.anna.shedule.utils.ContextUtils;

import java.util.ArrayList;
import java.util.List;


public class GroupSelectionActivity extends ActionBarActivity {

    private GroupService groupService = null;
    private String START_ITEM_SPINNER = "Не выбрано";
    private ProgressDialog prog1 = null;
    private List<String> faculties = null;
    private List<String> specialty = null;
    private List<String> course = null;
    private List<String> groups = null;

    private LinearLayout btnEntry = null;

    private Spinner spinnFac = null;
    private Spinner spinnSpec = null;
    private Spinner spinnCour = null;
    private Spinner spinnGroup = null;

    private ArrayAdapter<String> adapterFac = null;
    private ArrayAdapter<String> adapterSpec = null;
    private ArrayAdapter<String> adapterCour = null;
    private ArrayAdapter<String> adapterGroup = null;

    private final LoginService loginService = Services.getService(LoginService.class);

    private List<Group> groupsList = null;
    private String groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_selection);

        spinnCour = (Spinner) findViewById(R.id.courseSpiner);
        spinnFac = (Spinner) findViewById(R.id.facultySpiner);
        spinnSpec = (Spinner) findViewById(R.id.specialtySpiner);
        spinnGroup = (Spinner) findViewById(R.id.groupSpiner);

        progressBarWaiting();
        ContextUtils.setContext(getApplicationContext());
        groupService = Services.getService(GroupService.class);

        specialty = new ArrayList<String>();
        groups = new ArrayList<String>();
        faculties = new ArrayList<String>();
        course = new ArrayList<String>();
        faculties.add(START_ITEM_SPINNER);
        faculties.addAll(groupService.getFaculties());
        prog1.cancel();


        adapterFac = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, faculties);
        adapterFac.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnFac.setAdapter(adapterFac);

        adapterCour = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, course);
        adapterCour.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adapterGroup = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, groups);
        adapterGroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adapterSpec = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, specialty);
        adapterSpec.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnFac.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String fac = spinnFac.getSelectedItem().toString();
                specialty.clear();
                specialty.add(START_ITEM_SPINNER);
                progressBarWaiting();
                specialty.addAll(groupService.getSpecialities(fac));
                spinnSpec.setAdapter(adapterSpec);
                adapterSpec.notifyDataSetChanged();
                prog1.cancel();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnSpec.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String fac = spinnFac.getSelectedItem().toString();
                String sp = spinnSpec.getSelectedItem().toString();
                course.clear();
                course.add(START_ITEM_SPINNER);
                progressBarWaiting();
                course.addAll(groupService.getCourses(fac, sp));
                spinnCour.setAdapter(adapterCour);
                adapterCour.notifyDataSetChanged();
                prog1.cancel();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spinnCour.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String fac = spinnFac.getSelectedItem().toString();
                String sp = spinnSpec.getSelectedItem().toString();
                String cr = spinnCour.getSelectedItem().toString();
                groups.clear();
                groups.add(START_ITEM_SPINNER);
                progressBarWaiting();
                List grs = groupService.getGroups(fac, sp, cr);
                groupsList = grs;
                groups.addAll(grs.subList(0, grs.size()));
                spinnGroup.setAdapter(adapterGroup);
                adapterGroup.notifyDataSetChanged();
                prog1.cancel();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spinnGroup.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int posSelected = spinnGroup.getSelectedItemPosition();
                if (posSelected != 0)
                    groupId = groupsList.get(posSelected - 1).getGroupId();
                else
                    groupId = null;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                groupId = null;
            }
        });

        btnEntry = (LinearLayout) findViewById(R.id.btnEntry);
        btnEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Переход по нажатию на кнопку!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                /*final Intent intent = new Intent(LoginActivity.this,
                        com.example.anna.shedule.login.GroupSelectionActivity.class);
                intent.putExtra("user", user.toString());
                startActivity(intent);*/
//                Toast.makeText(getApplicationContext(), "Переход на следующий экран!", Toast.LENGTH_SHORT).show();
                if (groupId != null)
                    loginService.loginAsStudent(groupId, new LoginService.LoginListener() {
                        @Override
                        public void onSuccess(User user) {
                            final Intent intent = new Intent(GroupSelectionActivity.this,
                                    com.example.anna.shedule.MainActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onError(LoginError loginError) {
                            int messageRes = R.string.no_internet_connection;
                            if (loginError == LoginError.INVALID_USERNAME_OR_PASSWORD) {
                                messageRes = R.string.invalid_password_or_login;
                            }
                            prog1.cancel();
                            Toast.makeText(getApplicationContext(), "Авторизация не удалась! Проверьте логин и пароль!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onProgress(LoginProgress progress) {

                        }
                    });
            }
        });

    }


    private void progressBarWaiting() {
        prog1 = new ProgressDialog(GroupSelectionActivity.this);
        prog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prog1.setMessage("wait please");
        prog1.setIndeterminate(true); // выдать значек ожидания
        prog1.setCancelable(true);
        prog1.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_group_selection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
