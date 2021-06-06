package com.superduperdrive.cloudstorage.services;

import com.superduperdrive.cloudstorage.mapper.NotesMapper;
import com.superduperdrive.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotesService {
    private NotesMapper notesMapper;

    public NotesService(NotesMapper notesMapper) {
        this.notesMapper = notesMapper;
    }

    public int addOrUpdateNote(Note note) {
        if(note.getNoteId()==null) {
            return notesMapper.addNote(note);
        } else {
            Note toUpdate = notesMapper.getNoteById(note.getNoteId());
            if(toUpdate.getUserId() == note.getUserId()) {
                return notesMapper.updateNoteById(note);
            } else {
                return -1;
            }
        }
    }

    public int deleteNote(Integer nodeId) {
        return notesMapper.deleteNoteById(nodeId);
    }

    public List<Note> getNotesByUserId(int userId) {
        return notesMapper.getNotesByUserId(userId);
    }

    public Note getNoteById(int noteId) {
        return notesMapper.getNoteById(noteId);
    }
}