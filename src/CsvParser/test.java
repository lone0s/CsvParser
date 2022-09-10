package CsvParser;

import java.io.ObjectStreamClass;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.function.Predicate;
import java.util.random.RandomGenerator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class test {
    public static void main(String[] args) throws Exception {
        CsvParser test = new CsvParser("C:\\Users\\jeune\\Documents\\Fac\\COO\\TPs\\CsvParser\\test.csv");
        test.readCsvInput();
/*        for (String str: test.extractColumn(0)
             ) {
            System.out.println(str);
        }*/
/*        String[] col = test.extractColumn(0);
        for (String str: col
             ) {
            System.out.println(str);
        }*/
        /*System.out.println(test.minValueInColumn(0));*/
/*        System.out.println(test.minValueInColumn(2));*/
//        String[][] extract = test.extractArrayOfData(1,1);
/*        for (String[] arr: extract
             ) {
            for (String str: arr
                 ) {
                System.out.print(str);
            }
            System.out.print("\n");
        }*/
        LinkedList<String[]> remDup = test.removeDuplicates();
//        LinkedList<String[]> filteredData = test.filterData(x -> );
        StringBuilder l1str = new StringBuilder();
        String[] l1 = test.extractLine(0);
        for (String cell :l1
             ) {
            l1str.append(cell);
        }
        StringBuilder l2str = new StringBuilder();
        String[] l2 = test.extractLine(1);
        for (String cell : l2
        ) {
            l2str.append(cell);
        }
        String l1f = l1str.toString();
//        objBuilder.add(l1f);
        String l2f = l2str.toString();
        Stream.Builder<Object> objBuilder = Stream.builder();
//        objBuilder.add(l2f);
        objBuilder.accept(l1f + l2f);
        Stream<Object> toz = objBuilder.build();
        //On manipule comment un objet wshhhhhhh aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaah
//        Object toz = objBuilder.build();
        System.out.println(toz);
//        System.out.println(l1f);
//        System.out.println(l2f);
//        System.out.println(l1f.hashCode() == l2f.hashCode());

//        for (String[] line: remDup
//             ) {
//            for (String cell: line
//                 ) {
//                System.out.print(cell + ";");
//            }
//            System.out.print("\n");
//        }

    }
}
