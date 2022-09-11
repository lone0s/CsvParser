package CsvParser;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class test {
    public static void main(String[] args) throws Exception {
        CsvParser test = new CsvParser("E:\\ohno\\Documents\\Java2223\\TP1\\test.csv");
        test.readCsvInput();
//        for (String str: test.extractColumn(0)
//             ) {
//            System.out.println(str);
//        }
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
/*        LinkedList<String[]> remDup = test.removeDuplicates();
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
        System.out.println(toz);*/
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
        String[] streamTest = {"abc", "bbc", "aef"};
        String[] streamTestBis = {"edf", "enedis","total"};
        LinkedList<String[]> input = new LinkedList<>();
        input.add(streamTest);
        input.add(streamTestBis);
        LinkedList<String> res = new LinkedList<>();
        for (String[] line: input
             ) {
            Stream<String> filtered = Arrays.stream(line).filter(x-> x.contains("enedis"));
            filtered.forEach(res::add);
        }
        for (String cell: res
             ) {
            System.out.println(cell); // <== WORKSSSSS
        }
        double max = test.minNumValueInColumn(0);
        System.out.println(max);
        Predicate<String> predicate = new Predicate<String>() {
            @Override
            public boolean test(String s) {
                return s.contains("a");
            }
        };
//        LinkedList<String[]> filteredData = test.filterLines(predicate);
//        for (String[] line: filteredData
//             ) {
//            for (String cell: line
//                 ) {
//                System.out.print(cell + ";");
//            }
//            System.out.println();
//        }
        LinkedList<String> filteredDataCells = test.filterCells(predicate);
        for (String cell: filteredDataCells
             ) {
            System.out.println(cell);
        }
//        Stream<String> filtered = Arrays.stream(streamTest).filter(x -> x.contains("b"));
//        filtered.forEach(e -> System.out.println(e));
//        LinkedList<String[]> res = filtered.toList();
    }
}
