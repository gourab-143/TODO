package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.todo.Models.Notes;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotesTakerActivity extends AppCompatActivity {

    EditText editText_title,editText_notes;
    ImageView imageViewSave;
    Notes notes;
    boolean isOldNote=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_taker);

        editText_notes=findViewById(R.id.editText_notes);
        editText_title=findViewById(R.id.editText_title);
        imageViewSave=findViewById(R.id.imageViewSave);

        notes = new Notes();
        try {
            notes= (Notes) getIntent().getSerializableExtra("old_note");
            editText_title.setText(notes.getTitle());
            editText_notes.setText(notes.getNotes());
            isOldNote=true;
        }
        catch (Exception e){
            e.printStackTrace();
        }

        imageViewSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title=editText_title.getText().toString();
                String description=editText_notes.getText().toString();

                if(description.isEmpty()){

                    Toast.makeText(NotesTakerActivity.this, "Add some notes!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                SimpleDateFormat formatter=new SimpleDateFormat("EEE, d MMM yyyy HH:mm a");
                Date date=new Date();

                if(!isOldNote) {
                    notes = new Notes();
                }
                notes.setTitle(title);
                notes.setNotes(description);
                notes.setDate(formatter.format(date));

                Intent intent=new Intent();
                intent.putExtra("note",notes);
                setResult(Activity.RESULT_OK,intent);
                finish();

            }
        });
    }
}