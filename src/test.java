import Database.Database;
import Exceptions.BPlusTreeException;
import Table.Table;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class test {
    public static void main(String[] args) throws BPlusTreeException,IOException {

        String path = "./dat/";
        Database db = new Database("test", 0);
        int[] a = new int[2];
        a[0] = -1;
        a[1] = 8;
        String[] s = new String[2];
        s[0] = "m_id";
        s[1] = "name";
        ArrayList ss = new ArrayList();
        ss.add(s[0]);
        ss.add(s[1]);
        String[] p = new String[1];
        p[0] = "m_id";
        db.createTable(s, a, p, "test");

        db.useDB("test");
        Table table = db.tables.get(0);

        ArrayList arr = new ArrayList<>();
        arr.add(1);
        arr.add("testa");
        table.InsertRow(arr);
        table.index_forest.get(0).printBPlusTree();
        System.out.println();

        ArrayList arr1 = new ArrayList<>();
        arr1.add(2);
        arr1.add("twew1");
        table.InsertRow(arr1);
        table.index_forest.get(0).printBPlusTree();
        System.out.println();

        ArrayList arr2 = new ArrayList<>();
        arr2.add(3);
        arr2.add("twew2");
        table.InsertRow(arr2);
        table.index_forest.get(0).printBPlusTree();
        System.out.println();

        ArrayList arr3 = new ArrayList<>();
        arr3.add(4);
        arr3.add("twew3");
        table.InsertRow(arr3);
        table.index_forest.get(0).printBPlusTree();
        System.out.println();

        ArrayList arr4 = new ArrayList<>();
        arr4.add(5);
        arr4.add("twew4");
        table.InsertRow(arr4);
        table.index_forest.get(0).printBPlusTree();
        System.out.println();

        ArrayList arr5 = new ArrayList<>();
        arr5.add(6);
        arr5.add("twew5");
        table.InsertRow(arr5);
        table.index_forest.get(0).printBPlusTree();
        System.out.println();

        table = db.tables.get(0);
        ArrayList test = new ArrayList();
        ArrayList test_test = new ArrayList();
        ArrayList test_test_test = new ArrayList();
        test_test_test.add("m_id");
        test_test_test.add(1);
        test_test_test.add(5);
        test_test_test.add(true);
        test_test.add(test_test_test);
        ArrayList test_test_test1 = new ArrayList();
        test_test_test1.add("m_id");
        test_test_test1.add(2);
        test_test_test1.add(1);
        test_test_test1.add(true);
        test_test.add(test_test_test1);
        test.add(test_test);
        System.out.println(table.SelectRows(test, ss));
        db.dropTable("test");
        db.dropDB("test");
//        File db = new File(path);
//        if(db.exists()){
//            File[] tmplist = db.listFiles();
//            for(File f: tmplist){
//                System.out.print(f.getName().substring(0,f.getName().lastIndexOf(".")));
//                System.out.print("\n");
//            }
//        }

    }
}