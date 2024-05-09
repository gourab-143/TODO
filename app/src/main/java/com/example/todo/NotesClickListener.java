package com.example.todo;

import androidx.cardview.widget.CardView;

import com.example.todo.Models.Notes;

public interface NotesClickListener {
    void onClick(Notes notes);
    void onLongClick(Notes notes, CardView cardView);
}
