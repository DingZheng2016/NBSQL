import Database.Database;
import Exceptions.BPlusTreeException;
import Exceptions.TableException;
import Table.Table;
import com.sun.javafx.collections.MappingChange;
import javafx.scene.control.Tab;

import java.io.IOException;
import java.security.cert.TrustAnchor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class test {
    public static void main(String[] args) throws BPlusTreeException,IOException, TableException {

        String path = "./dat/";
        Database db = new Database("test", 0);
        int[] a = new int[2];
        a[0] = -1;
        a[1] = 8;
        String[] s = new String[2];
        s[0] = "m_id";
        s[1] = "1_name";
        ArrayList ss = new ArrayList();
        ss.add(s[0]);
        ss.add(s[1]);
        String[] p = new String[1];
        p[0] = "m_id";
        boolean[] isNotNull = new boolean[2];
        isNotNull[0] = true;
        isNotNull[1] = false;
        db.createTable(s, a, p, "test", isNotNull);

        s[1] = "2_name";
        db.createTable(s, a, p, "test1", isNotNull);
        s[1] = "3_name";
        db.createTable(s, a, p, "test2", isNotNull);

        db.useDB("test");
        Table table = db.tables.get(0);
        Table table1 = db.tables.get(1);
        Table table2 = db.tables.get(2);

        ArrayList arr = new ArrayList<>();
        arr.add(1);
        arr.add("twew0");
        table.InsertRow(arr);
        table1.InsertRow(arr);
        table2.InsertRow(arr);

        ArrayList arr1 = new ArrayList<>();
        arr1.add(2);
        arr1.add("twew1");
        table.InsertRow(arr1);
        table1.InsertRow(arr1);
        table2.InsertRow(arr1);

        ArrayList arr2 = new ArrayList<>();
        arr2.add(3);
        arr2.add("twew2");
        table.InsertRow(arr2);
        table1.InsertRow(arr2);
        table2.InsertRow(arr2);

        ArrayList arr3 = new ArrayList<>();
        arr3.add(4);
        arr3.add("twew3");
        table.InsertRow(arr3);
        table1.InsertRow(arr3);
        table2.InsertRow(arr3);

        ArrayList arr4 = new ArrayList<>();
        arr4.add(5);
        arr4.add("twew4");
        table.InsertRow(arr4);
        table1.InsertRow(arr4);
        table2.InsertRow(arr4);

        ArrayList arr5 = new ArrayList<>();
        arr5.add(6);
        arr5.add("twew5");
        table.InsertRow(arr5);
        table1.InsertRow(arr5);
        table2.InsertRow(arr5);


        db.useDB("test");

        table = db.tables.get(0);
        table1 = db.tables.get(1);
        table2 = db.tables.get(2);

//        table.index_forest.get(0).printBPlusTree();
//        System.out.println();
//        table.DeleteRow(arr3);
//        table.index_forest.get(0).printBPlusTree();
//        System.out.println();
//        ArrayList test = new ArrayList();
//        ArrayList test_test = new ArrayList();
//        ArrayList test_test_test = new ArrayList();
//        test_test_test.add("m_id");
//        test_test_test.add(1);
//        test_test_test.add(5);
//        test_test_test.add(true);
//        test_test.add(test_test_test);
//        ArrayList test_test_test1 = new ArrayList();
//        test_test_test1.add("m_id");
//        test_test_test1.add(2);
//        test_test_test1.add(1);
//        test_test_test1.add(true);
//        test_test.add(test_test_test1);
//        test.add(test_test);
//        System.out.println(table.SelectRows(test, ss));
//        ArrayList sss = new ArrayList();
//        sss.add("name");
//        ArrayList ssss = new ArrayList();
//        ssss.add("test");
//        table.UpdateRow(test, sss, ssss);
//        System.out.println(table.SelectRows(test, ss));
        ArrayList test = new ArrayList();
        ArrayList test1 = new ArrayList();
        ArrayList test_test = new ArrayList();
        ArrayList test_test_test = new ArrayList();
        ArrayList test_test_test_test = new ArrayList();
        test.add("test");
        test.add("m_id");
        test.add(0);
        test.add("test1");
        test.add("m_id");
        test.add(false);
        test1.add("test");
        test1.add("m_id");
        test1.add(0);
        test1.add(2);
        test1.add(null);
        test1.add(true);
        test_test.add(test);
        test_test.add(test1);

        test_test_test.add(test_test);
        test_test_test_test.add(test_test_test);
        test_test_test_test.add(null);
        ArrayList<Table> tmp = new ArrayList<Table>();
        tmp.add(table);
        tmp.add(table1);
        tmp.add(table2);
        Set<ArrayList> res = db.joinTables(tmp, test_test_test_test);
        System.out.print(res);
        System.out.println();
        for (ArrayList tmpres : res) {
            System.out.print(table.file.readData((int)tmpres.get(0)));
            System.out.print(table1.file.readData((int)tmpres.get(1)));
            System.out.print(table2.file.readData((int)tmpres.get(2)));
            System.out.println();
        }
        System.out.print("test1".hashCode());
        db.dropTable("test2");
        db.dropTable("test1");
        db.dropTable("test");
        db.dropDB("test");
    }
}