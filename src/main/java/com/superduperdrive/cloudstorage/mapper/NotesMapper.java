package com.superduperdrive.cloudstorage.mapper;

import com.superduperdrive.cloudstorage.model.File;
import com.superduperdrive.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NotesMapper {
    @Select("SELECT * FROM NOTES where userId = #{userId}")
    public List<Note> getNotesByUserId(int userId);

    @Select("SELECT * FROM NOTES where noteId = #{noteId}")
    public Note getNoteById(int noteId);

    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid) VALUES " +
            "(#{noteTitle},#{noteDescription},#{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    public int addNote(Note note);

    @Update("UPDATE NOTES SET notetitle = #{noteTitle}," +
            " notedescription = #{noteDescription} WHERE noteid = #{noteId}")
    public int updateNoteById(Note note);

    @Delete("DELETE FROM NOTES where noteid = #{noteId}")
    public int deleteNoteById(Integer noteId);
}