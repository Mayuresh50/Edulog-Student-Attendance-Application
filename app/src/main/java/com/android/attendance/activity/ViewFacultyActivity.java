package com.android.attendance.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.attendance.bean.FacultyBean;
import com.android.attendance.db.DBAdapter;
import com.example.androidattendancesystem.R;

public class ViewFacultyActivity extends Activity {

	ArrayList<FacultyBean> facultyBeanList;
	private ListView listView;
	private ArrayAdapter<String> listAdapter;

	DBAdapter dbAdapter = new DBAdapter(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.__listview_main);

		listView = (ListView) findViewById(R.id.listview);
		final ArrayList<String> facultyList = new ArrayList<String>();

		facultyBeanList = dbAdapter.getAllFaculty();

		// Adding faculty details (name and address) to the list
		for (FacultyBean facultyBean : facultyBeanList) {
			String facultyDetails = "FirstName: " + facultyBean.getFaculty_firstname() +
					"\nLastName: " + facultyBean.getFaculty_lastname() +
					"\nAddress: " + facultyBean.getFaculty_address() +
					"\nUsername: "+facultyBean.getFaculty_username(); // Assuming getFaculty_address() exists

			facultyList.add(facultyDetails);
			Log.d("facultyDetails: ", facultyDetails);
		}

		listAdapter = new ArrayAdapter<String>(this, R.layout.view_faculty_list, R.id.labelF, facultyList);
		listView.setAdapter(listAdapter);

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ViewFacultyActivity.this);
				alertDialogBuilder.setTitle(getTitle() + " Are you sure?");
				alertDialogBuilder.setMessage("Delete the Faculty data ?");

				alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						// Remove faculty from the list
						facultyList.remove(position);
						listAdapter.notifyDataSetChanged();
						listAdapter.notifyDataSetInvalidated();

						// Delete faculty from the database
						dbAdapter.deleteFaculty(facultyBeanList.get(position).getFaculty_id());
						facultyBeanList = dbAdapter.getAllFaculty();

						// Refresh the faculty list after deletion
						facultyList.clear();
						for (FacultyBean facultyBean : facultyBeanList) {
							String facultyDetails = "FirstName: " + facultyBean.getFaculty_firstname() +
									"\nLastName: " + facultyBean.getFaculty_lastname() +
									"\nAddress: " + facultyBean.getFaculty_address()+
									"\nUsername: "+facultyBean.getFaculty_username();
							facultyList.add(facultyDetails);
							Log.d("facultyDetails: ", facultyDetails);
						}
					}
				});

				alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						Toast.makeText(getApplicationContext(), "You chose cancel", Toast.LENGTH_LONG).show();
					}
				});

				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();

				return false;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
