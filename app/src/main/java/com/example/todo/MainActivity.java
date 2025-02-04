package com.example.todo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.todo.Adapters.NotesListAdapter;
import com.example.todo.Database.RoomDB;
import com.example.todo.Models.Notes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;




import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

    RecyclerView recyclerView;
    NotesListAdapter notesListAdapter;
    List<Notes> notes=new ArrayList<>();
    RoomDB database;
    FloatingActionButton fab_add;
    SearchView searchView_home;
    Notes selectedNotes;
    //MenuItem pinned;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_home);
        fab_add=findViewById(R.id.fab_add);
        searchView_home=findViewById(R.id.searchView_home);
        //pinned=findViewById(R.id.pinned);

        database=RoomDB.getInstance(this);
        notes=database.mainDAO().getAll();

        updateRecycler(notes);

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,NotesTakerActivity.class);
                startActivityForResult(intent,101);
            }
        });

        searchView_home.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }

    private void filter(String newText){
        List<Notes> filterdList = new ArrayList<>();
        for (Notes singleNotes :  notes){
            if(singleNotes.getTitle().toLowerCase().contains(newText.toLowerCase())
            ||singleNotes.getNotes().toLowerCase().contains(newText.toLowerCase())){
                filterdList.add(singleNotes);
            }

        }
        notesListAdapter.filterList(filterdList);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101){
            if(resultCode== Activity.RESULT_OK){
                Notes new_notes= (Notes) data.getSerializableExtra("note");
                database.mainDAO().Insert(new_notes);
                notes.clear();
                notes.addAll(database.mainDAO().getAll());
                notesListAdapter.notifyDataSetChanged();
            }

        }
        else if(requestCode==102){
            if(resultCode== Activity.RESULT_OK){
                Notes new_notes= (Notes) data.getSerializableExtra("note");
                database.mainDAO().update(new_notes.getID(), new_notes.getTitle(), new_notes.getNotes());
                notes.clear();
                notes.addAll(database.mainDAO().getAll());
                notesListAdapter.notifyDataSetChanged();
            }

        }
    }

    private void updateRecycler(List<Notes> notes) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        notesListAdapter=new NotesListAdapter(MainActivity.this,notes,notesClickListener);
        recyclerView.setAdapter(notesListAdapter);
    }

    private final NotesClickListener notesClickListener=new NotesClickListener() {
        @Override
        public void onClick(Notes notes) {
            Intent intent=new Intent(MainActivity.this,NotesTakerActivity.class);
            intent.putExtra("old_note",notes);
            startActivityForResult(intent,102);
        }

        @Override
        public void onLongClick(Notes notes, CardView cardView) {
            selectedNotes= new Notes();
            selectedNotes=notes;
            showPopup(cardView);
        }
    };

    private void showPopup(CardView cardView){
        PopupMenu popupMenu=new PopupMenu(this,cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int itemId=item.getItemId();
        /*if(itemId==R.id.pin){
            if(selectedNote.isPinned()){
                database.mAinDAO().pin(selectedNote.getID(),false);
                Toast.makeText(this, "Unpinned", Toast.LENGTH_SHORT).show();
            }
            else{

                database.mAinDAO().pin(selectedNote.getID(),true);
                Toast.makeText(this, "Pinned", Toast.LENGTH_SHORT).show();
            }
            notes.clear();
            notes.addAll(database.mAinDAO().getAll());
            notesAdapter.notifyDataSetChanged();

        } else if (itemId==R.id.delete) {
            database.mAinDAO().delete(selectedNote);
            notes.remove(selectedNote);
            notesAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Notes Deleted", Toast.LENGTH_SHORT).show();
        }*/

        return true;

    }

    }
