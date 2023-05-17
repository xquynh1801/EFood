package com.example.efood.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.efood.model.Comment;
import com.example.efood.model.CommentDTO;
import com.example.efood.model.ImgComment;

import java.util.ArrayList;
import java.util.List;

public class CommentDB extends DBConnection{
    public CommentDB(Context context) {
        super(context);
    }

    public long addComment(Comment c) {
        ContentValues values = new ContentValues();
        values.put("rating", c.getRating());
        values.put("content", c.getContent());
        values.put("userID", c.getUser().getId());
        values.put("foodID", c.getFood().getId());
        values.put("img", c.getImg());
        SQLiteDatabase db = getWritableDatabase();
        return db.insert("comments", null, values);
    }

    public List<CommentDTO> getCommentsByFood(int foodID){
        List<CommentDTO> ls = new ArrayList<>();

        SQLiteDatabase db=getReadableDatabase();

        //tạo con trỏ đọc bảng dữ liệu sản phẩm
        String sql = "Select * from comments where foodID = ?";
        String[] agrs={""+foodID+""};
        Cursor rs=db.rawQuery(sql,agrs);
        while(rs!=null && rs.moveToNext()){
            ls.add(new CommentDTO(rs.getInt(0),rs.getInt(1),rs.getString(2),
                    rs.getInt(3),rs.getInt(4), rs.getBlob(5)));
        }
        rs.close();
        return ls;
    }

    public long addImgComment(ImgComment i) {
        ContentValues values = new ContentValues();
        values.put("imgPath", i.getImgPath());
        values.put("commentID", i.getComment().getId());
        SQLiteDatabase db = getWritableDatabase();
        return db.insert("imgs_comment", null, values);
    }
}
