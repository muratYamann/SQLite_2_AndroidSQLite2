package com.yamankod.sqlite_2_androidsqlite2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private SQLiteDatabase db;
    private TextView textView;
    final static int PERMISSIONS_REQUEST_CODE = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        textView = (TextView) findViewById(R.id.textView2);
        try {
            openDatabase();
            dropTable();
            insertSomeDbData();
            useRawQuery1();
            useRawQuery2();
            useRawQuery3();
            useSimpleQuery1();
            useSimpleQuery2();
            useCursor1();
            updateDB();
            useInsertMethod();
            useUpdateMethod();
            useDeleteMethod();
            db.close();
            Toast.makeText(this,"Bitti!",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void openDatabase() {
        try {
            db = SQLiteDatabase.openDatabase("/mnt/sdcard/myfriendsDB", null,
                    SQLiteDatabase.CREATE_IF_NECESSARY);
            Toast.makeText(getApplicationContext(), "DB acildi!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void insertSomeDbData() {
        db.beginTransaction();
        try {
            db.execSQL("create table tblAMIGO("
                    + "recID integer PRIMARY KEY autoincrement," + "name text,"	+ "phone text);");
            db.setTransactionSuccessful();
            Toast.makeText(getApplicationContext(), "Tablo olusturuldu.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            db.endTransaction();
        }
        db.beginTransaction();
        try {
            db.execSQL("insert into tblAMIGO(name, phone)"	+ " values ('AAA', '555');");
            db.execSQL("insert into tblAMIGO(name, phone)"	+ " values ('BBB', '777');");
            db.execSQL("insert into tblAMIGO(name, phone)"	+ " values ('CCC', '999');");
            db.setTransactionSuccessful();
            Toast.makeText(this, " 3 kayit eklendi", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            db.endTransaction();
        }
    }
    private void useRawQuery1() {
        try {
            String mySQL = "select count(*) as Total from tblAMIGO";
            Cursor c1 = db.rawQuery(mySQL, null);
            int index = c1.getColumnIndex("Total");
            c1.moveToNext();
            int theTotal = c1.getInt(index);
            Toast.makeText(this, "Total1: " + theTotal, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void useRawQuery2() {
        try {
            String mySQL = " select count(*) as Total " + " from tblAMIGO"
                    + " where recID> ? " + " and name = ? ";
            String[] args = { "1", "BBB" };
            Cursor c1 = db.rawQuery(mySQL, args);
            int index = c1.getColumnIndex("Total");
            c1.moveToNext();
            int theTotal = c1.getInt(index);
            Toast.makeText(this, "Total2: " + theTotal, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void useRawQuery3() {
        try {
            String[] args = { "1", "BBB" };
            String mySQL = " select count(*) as Total " + " from tblAMIGO"
                    + " where recID> " + args[0] + " and name = '" + args[1]
                    + "'";
            Cursor c1 = db.rawQuery(mySQL, null);
            int index = c1.getColumnIndex("Total");
            c1.moveToNext();
            int theTotal = c1.getInt(index);
            Toast.makeText(this, "Total3: " + theTotal, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void useSimpleQuery1() {
        try {
            String[] columns = { "recID", "name", "phone" };
            Cursor c1 = db.query("tblAMIGO", columns,
                    "recID> 2 and length(name) >= 3 and name like 'B%' ", null,
                    null, null, "recID");
            int theTotal = c1.getCount();
            Toast.makeText(this, "Total4: " + theTotal, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void useSimpleQuery2() {
        try {
            String[] selectColumns = { "name", "count(*) as TotalSubGroup" };
            String whereCondition = "recID>= ?";
            String[] whereConditionArgs = { "1" };
            String groupBy = "name";
            String having = "count(*) <= 4";
            String orderBy = "name";
            Cursor c = db.query("tblAMIGO", selectColumns, whereCondition,
                    whereConditionArgs, groupBy, having, orderBy);
            int theTotal = c.getCount();
            Toast.makeText(this, "Total5: " + theTotal, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void useCursor1() {
        try {
            textView.append("\n");
            String[] columns = { "recID", "name", "phone" };
            Cursor c = db.query("tblAMIGO", columns, null, null, null, null,
                    "recID");
            int theTotal = c.getCount();
            Toast.makeText(this, "Total6: " + theTotal, Toast.LENGTH_SHORT).show();
            int idCol = c.getColumnIndex("recID");
            int nameCol = c.getColumnIndex("name");
            int phoneCol = c.getColumnIndex("phone");
            while (c.moveToNext()) {
                columns[0] = Integer.toString((c.getInt(idCol)));
                columns[1] = c.getString(nameCol);
                columns[2] = c.getString(phoneCol);
                textView.append(columns[0] + " " + columns[1] + " "
                        + columns[2] + "\n");
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void updateDB() {
        String theValue;
        try {
            theValue = "222";
            db.execSQL("UPDATE tblAMIGO" + " SET NAME = (name ||'XXX') "
                    + " where phone >= '" + theValue + "' ");
            useCursor1();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void dropTable() {
        try {
            db.execSQL(" drop table tblAMIGO; ");
            Toast.makeText(this, "Tablo drop edildi", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "dropTable()\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void useInsertMethod() {
        ContentValues initialValues = new ContentValues();
        initialValues.put("name", "ABC");
        initialValues.put("phone", "101");
        int rowPosition = (int) db.insert("tblAMIGO", null, initialValues);
        textView.append("\n kayit eklendi: " + rowPosition);
        initialValues.put("name", "DEF");
        initialValues.put("phone", "202");
        rowPosition = (int) db.insert("tblAMIGO", null, initialValues);
        textView.append("\n kayit eklendi: " + rowPosition);
//		initialValues.clear();
        rowPosition = (int) db.insert("tblAMIGO", null, initialValues);
        textView.append("\n kayit eklendi: " + rowPosition);
        rowPosition = (int) db.insert("tblAMIGO", "name", initialValues);
        textView.append("\n kayit eklendi: " + rowPosition);
        useCursor1();
    }
    private void useUpdateMethod() {
        String[] whereArgs = { "2", "7" };
        ContentValues updValues = new ContentValues();
        updValues.put("name", "Suhap");
        int recAffected = db.update("tblAMIGO", updValues,
                "recID> ? and recID< ?", whereArgs);
        Toast.makeText(this, "Total7: " + recAffected, Toast.LENGTH_SHORT).show();
        useCursor1();
    }
    private void useDeleteMethod() {
        String[] whereArgs = { "2", "7" };
        int recAffected = db.delete("tblAMIGO", "recID> ? and recID< ?",
                whereArgs);
        Toast.makeText(this, "Total8: " + recAffected, Toast.LENGTH_SHORT).show();
        useCursor1();
    }




    public void getPermissionWriteExternalStorage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {


            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            }
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_CODE);
        }
    }
    public void getPermissionReadExternalStorage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {


            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            }
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_CODE);
        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }






}

