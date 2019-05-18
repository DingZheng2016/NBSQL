package Parser;
import Database.Database;
import Exceptions.BPlusTreeException;
import Exceptions.TableException;
import Table.Table;

import java.io.IOException;
import java.util.ArrayList;

public class SQLVisitorStmt extends SQLBaseVisitor<Void>{
    Database db = null;
    StringBuffer dbName;
    StringBuffer output;
    public SQLVisitorStmt(StringBuffer dbName, StringBuffer output) throws IOException
    {
        this.dbName = dbName;
        this.output = output;
        this.db = new Database(this.dbName.toString(),1);
    }

    @Override
    public Void visitCreate_table_stmt(SQLParser.Create_table_stmtContext ctx) {
        if(db != null)
        {
            String tableName = ctx.table_name().getText().toUpperCase();
            ArrayList<String> names = new ArrayList<>();
            int[] types = new int[ctx.column_def().size()];
            ArrayList<String> primary_key = new ArrayList<>();
            boolean[] not_null = new boolean[ctx.column_def().size()];
            for(int i = 0; i < ctx.column_def().size(); i++)
            {
                ArrayList def = ctx.column_def(i).accept(new SQLVisitorColumnDef());
                names.add((String)def.get(0));
                types[i] = ((Integer)def.get(1)).intValue();
                if(def.get(2) == null)
                {
                    not_null[i] = false;
                }
                else if((Integer)def.get(2) == 0)
                {
                    primary_key.add((String)def.get(0));
                    not_null[i] = false;
                }
                else if((Integer)def.get(2) == 1)
                {
                    not_null[i] = true;
                }
            }
            for(int i = 0; i < ctx.table_constraint().size(); i++)
            {
                ArrayList<String> columns = ctx.table_constraint(i).accept(new SQLVisitorColumnDef());
                if(ctx.table_constraint(i).K_PRIMARY() != null && ctx.table_constraint(i).K_KEY() != null)
                {
                    primary_key.addAll(columns);
                }
                else if(ctx.table_constraint(i).K_NOT() != null && ctx.table_constraint(i).K_NULL() != null)
                {
                    for(String s:columns)
                    {
                        not_null[names.indexOf(s)] = true;
                    }
                }
            }
            try {
                this.db.createTable(names.toArray(new String[names.size()]), types, primary_key.toArray(new String[primary_key.size()]), tableName, not_null);
            }
            catch (IOException e)
            {
                output.append("create table fail");
            }
        }
        else
        {
            output.append("create table fail");
        }
        return null;
    }

    @Override
    public Void visitDrop_table_stmt(SQLParser.Drop_table_stmtContext ctx) {
        if(this.db != null)
        {
            String tableName = ctx.table_name().getText();
        }
        return null;
    }

    @Override
    public Void visitCreate_database_stmt(SQLParser.Create_database_stmtContext ctx) {
        String name = ctx.getChild(2).accept(new SQLVisitorNames());
        try {
            Database temp = new Database(name,0);
            this.output.append("create database success");
        }
        catch (IOException e)
        {
            System.out.println(e.toString());
            this.output.append("create database fail");
        }
        return null;
    }

    @Override
    public Void visitShow_database_stmt(SQLParser.Show_database_stmtContext ctx) {
        String name = ctx.getChild(2).accept(new SQLVisitorNames());
        Database temp;
        try {
            if(name == this.dbName.toString())
                temp = this.db;
            else
                temp = new Database(name,0);
            for(int i = 0; i < temp.tables.size(); i++)
            {
                output.append(temp.tables.get(i).table_name);
                output.append(" ");
            }
        }
        catch (IOException e)
        {
            System.out.println(e.toString());
            this.output.append("show database fail");
        }
        return null;
    }

    @Override
    public Void visitInsert_stmt(SQLParser.Insert_stmtContext ctx){
        String tableName = ctx.table_name().getText().toUpperCase();
        Table t = this.db.getTable(tableName);
        ArrayList<String> colomns= new ArrayList<String>();
        for(int i = 0; i < ctx.column_name().size(); i++)
        {
            colomns.add(ctx.column_name(i).getText().toUpperCase());
        }

        ArrayList data = new ArrayList();
        for(int i = 0; i < ctx.expr().size(); i++)
        {
            DataTypes dataTmp = ctx.expr(i).literal_value().accept(new SQLVisitorLiteralValue());
            switch (dataTmp.type){
                case 0:
                    data.add(dataTmp.int_data);
                    break;
                case 1:
                    data.add(dataTmp.long_data);
                    break;
                case 2:
                    data.add(dataTmp.float_data);
                    break;
                case 3:
                    data.add(dataTmp.double_data);
                    break;
                case 4:
                    data.add(dataTmp.string_data);
                    break;
                default:
                    break;
            }
        }
        ArrayList<String> col_name = t.getColumnName();
        ArrayList row = new ArrayList();
        if(colomns.size() == 0){
            int k = 0;
            for(int i = 0; i<col_name.size(); ++i){
                if(col_name.get(i).toString().compareTo("id")==0){
                    continue;
                }
                if(k < data.size()){
                    row.add(data.get(k));
                    k++;
                    continue;
                }
                row.add(null);
            }
        }
        else{
            for(int i = 0; i<col_name.size(); ++i){
                if(col_name.get(i).toString().compareTo("id")==0){
                    continue;
                }
                for(int j = 0; j<colomns.size(); ++j){
                    if(col_name.get(i) == colomns.get(j)){
                        row.add(data.get(j));
                        continue;
                    }
                }
                row.add(null);
            }
        }
        try{
            t.InsertRow(row);
        }
        catch (Exception a){

        }
        return null;
    }

    @Override
    public Void visitSelect_stmt(SQLParser.Select_stmtContext ctx) {

        if(ctx.join_clause() == null)
        {
            simple_Select(ctx);
        }
        return null;
    }

    private ArrayList<String> simple_Select(SQLParser.Select_stmtContext ctx) {
        String tableName = ctx.table_name().getText().toUpperCase();
        ArrayList<String> columns = new ArrayList<String>();
        for(int i = 0; i < ctx.result_column().size(); i++)
        {
            columns.add(ctx.result_column(i).getText().toUpperCase());
        }
        ArrayList<ArrayList<ArrayList>> conditions = new ArrayList<ArrayList<ArrayList>>();
        conditions.add(new ArrayList<ArrayList>());
        ctx.expr().accept(new SQLVisitorWhereClause(conditions,0));
        try {
            this.db.getTable(tableName).SelectRows(conditions, columns);
        } catch (Exception e)
        {

        }
        return null;
    }
}
