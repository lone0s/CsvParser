package CsvParser;

import java.awt.desktop.SystemSleepEvent;
import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class CsvParser implements AutoCloseable{
    final String SEPARATING_CHAR = ","; //Eventuellement prompt l'utilisateur sur le charactère de séparation

    //Gestion de l'input et suivi de l'état
    private File inputCsv;
    private boolean iCsvInitialized = false;
    private int nbLines = 0;
    private int nbCols = 0;

    //Gestion de la lecture de l'input data + état
    private BufferedReader inputCsvReader;
    private boolean readerIsAtEOF = true;

    //Gestion de l'input en mémoire centrale et de l'état
    public LinkedList<String[]> importedInput; // <== Pratique pour l'extraction via split, mais source de problèmes sur les dernières fonctionnalités :((
    public LinkedList<String[]> extraBuffer;
    private boolean listHasData = false;

    //Gestion d'un potentiel output
    private File outputCsv;

    public CsvParser(String inputAbsPath) {
        importedInput = new LinkedList<>();
        try {
            inputCsv = new File(inputAbsPath);
            if (inputCsv.isFile()){
                initCsvReader();
            }
        }
        catch (NullPointerException | FileNotFoundException e) {
            System.out.println("Adresse fichier incorrecte");
        }
    }

    //Permet l'initialisation du flux de communication
    private void initCsvReader() throws FileNotFoundException {
        try {
            inputCsvReader = new BufferedReader(new FileReader(inputCsv));
            iCsvInitialized = true;
            readerIsAtEOF = false;
        }
        catch (FileNotFoundException e) {
            System.out.println("File impossible to find ://");
        }
    }

    //Lecture du CSV ligne par ligne
    public void readCsvInput() throws Exception {
        if (iCsvInitialized) {
            while (!readerIsAtEOF){
                String csvLine = inputCsvReader.readLine();
                if (csvLine == null) {
                    readerIsAtEOF = true;
                    close();
                }
                else
                    //Permet de stocker mot par mot le contenu des lignes
                    importedInput.add(csvLine.split(SEPARATING_CHAR));
            }
            this.listHasData = listHasData();
            updateNbCols();
            updateNbLines();
            System.out.println("Properly imported the CSV data");
        }
    }
    //!!!!!! Choix de conception : on débute a 0

    public String[] extractLine(int line) {
        if (listHasData){
            String[] res = {};
            if (line < importedInput.size()){
                res = importedInput.get(line);
            }
            return res;
        }
        return null;
    }

    public String[] extractColumn(int col) {
        if (listHasData){
            if (col < nbCols){
                String[] res = new String[nbLines];
                int cpt = 0 ;
                for (String[] line: importedInput) {
                    res[cpt] = line[col];
                    cpt++;
                }
//                System.out.println("Extracted Column !");
                return res;
            }
        }
        return null;
    }
    //On considère la première ligne comme un entete sans contenu <==
    //pb si jamais la colonne extraite contient des données dont le type n'est pas uniforme (int & string par ex) :((

    public String maxStrValueInColumn(int col){
        if (listHasData){
            if(col < nbCols){
                String[] extractedColumn = extractColumn(col);
                String max = extractedColumn[1];
                for (int i = 1; i < extractedColumn.length ; i++){
                    String extractedValue = extractedColumn[i];
                    if (isSuperiorStrCompareTo(extractedValue.compareTo(max)))
                        max = extractedValue;
                }
                return max;
            }
        }
        return null;
    }

    public String minStrValueInColumn(int col) {
        if (listHasData){
            if(col < nbCols){
                String[] extractedColumn = extractColumn(col);
                String min = (extractedColumn[1]);
                for (int i = 1; i < extractedColumn.length ; i++) {
                    String cell = extractedColumn[i];
                    if (isInferiorStrCompareTo(cell.compareTo(min))) {
                        min = cell;
                    }
                }
                return min;
            }
        }
        return null;
    }

    public Double maxNumValueInColumn(int col){
        if (listHasData){
            if(col < nbCols){
                try{
                    String[] extractedColumn = extractColumn(col);
                    double max = Double.parseDouble(extractedColumn[1]);
                    for (int i = 1; i < extractedColumn.length ; i++){
                        double extractedValue = Double.parseDouble(extractedColumn[i]);
                        if (extractedValue > max)
                            max = extractedValue;
                    }
                    return max;
                }
                catch (NumberFormatException e) {
                    System.out.println("Mixed data, are you sure you didn't want to use maxStrValueInColumn ?");
                }
            }
        }
        return null;
    }

    public Double minNumValueInColumn(int col){
        if (listHasData){
            if(col < nbCols){
                try{
                    String[] extractedColumn = extractColumn(col);
                    double min = Double.parseDouble(extractedColumn[1]);
                    for (int i = 1; i < extractedColumn.length ; i++){
                        double extractedValue = Double.parseDouble(extractedColumn[i]);
                        if (extractedValue < min)
                            min = extractedValue;
                    }
                    return min;
                }
                catch (NumberFormatException e) {
                    System.out.println("Mixed data input, are you sure you didn't want to use maxStrValueInColumn ?");
                }
            }
        }
        return null;
    }
    ///// Uniquement sur valeurs numériques
    public Double avgValueInColumn(int col) {
        if (listHasData) {
            if (col < nbCols) {
                double avg = 0;
                double cpt = 0;
                String[] extractedColumn = extractColumn(col);
                for (int i = 1 ; i < extractedColumn.length ; i++){
                    avg += Double.parseDouble(extractedColumn[i]);
                    cpt ++;
                }
                return (avg/cpt);
            }
        }
        return null;
    }

    //Choix de conception <== extraction ligne par ligne pour cohérence de la data extraite
    //Si besoin, faire de l'extraction par colonne

    //Revoir choix de conception, considérer travailler sur le format liste et exporter comme type objet !!!
    public String[][] extractArrayOfData(int startLine, int endLine){
        if (startLine >= 0 && endLine < nbLines){
            String[][] extractedArray = new String[endLine - startLine + 1][nbCols];
            int cpt = 0;
            for (int i = startLine ; i <= endLine ; i++) {
                extractedArray[cpt] = extractLine(i);
                cpt++;
            }
            return extractedArray;
        }
        return null;
    }

    //Penser a convertir lignes par lignes via toString au lieu de faire de la comparaison mot par mot
    public LinkedList<String[]> removeDuplicates() {
        LinkedList<String[]> removedDuplicates = new LinkedList<>(importedInput);
        for (int i = 0 ; i < removedDuplicates.size() - 1 ; i++) {
            String[] lineToExam = removedDuplicates.get(i);
            for (int j = i+1 ; j < removedDuplicates.size() ; j++) {
                String[] comparedTo = removedDuplicates.get(j);
                int toExamSize = lineToExam.length;
                int comparedSize = comparedTo.length;
                if (toExamSize == comparedSize) {
                    int cpt = 0 ;
                    boolean state = true;
                    for (String cell: lineToExam
                         ) {
                        state = state && cell.equals(comparedTo[cpt]); //Utilisation des propriétés du ET logique pour
                        cpt++;
                    }
                    if (state) {
                        removedDuplicates.remove(comparedTo);
                        j -= 1; // <== Permet de revenir en arrière sur la verification au cas ou on aurait plusieurs fois la meme instance d'un même élément
                                //Solution dégueulasse mais impact sur la compléxité négligeable)
                    }
                }
            }
        }
        return removedDuplicates;
    }

    public LinkedList<String[]> filterLines(Predicate<String> predicate) {
        LinkedList<String[]> dataToFilter = new LinkedList<>(importedInput);
        LinkedList<String[]> filteredData = new LinkedList<>();
        for (String[] line: dataToFilter
             ) {
            Stream<String> streamedData = Arrays.stream(line).filter(predicate);
            if (streamedData.findAny().isPresent()) {
                filteredData.add(line);
            }
        }
        return filteredData;
    }

    public LinkedList<String> filterCells(Predicate<String> predicate) {
        LinkedList<String[]> dataToFilter = new LinkedList<>(importedInput);
        LinkedList<String> filteredData = new LinkedList<>();
        for (String[] line: dataToFilter
        ) {
            Stream<String> streamedData = Arrays.stream(line).filter(predicate);
            streamedData.forEach(filteredData::add); // :: == method reference == methode liée a la classe directement
        }
        return filteredData;
    }

    /*
    Deux cas de figures : soit il faut un objet de type OBJECT dont toutes les classe en Java héritent
                          soit il faut un objet a part entière et donc une classe avec son lot de méthodes
     */

    // Cas 1 <== On retourne un objet de type OBJECT
    public Object line2object (int line) {
        if (inputLineIsValid(line)) {
            Object lineObject = extractLine(line);
            return lineObject;
        }
        return null;
    }

    //Cas 2 <== On retourne un objet de type LineObject locale au package
    public LineObject line2LineObject(int line) {
        if (inputLineIsValid(line)) {
            String[] header = extractLine(0);
            String[] wantedLine = extractLine(line);
            return new LineObject(header,wantedLine);
        }
        return null;
    }


    //POUR PERMETTRE L'UTILISATION D'UN BUFFER ET EVITER LA CREATION REDONDANTE EN MEMOIRE DE STRUCTURES DE DONNEES
    //PENSER A IMPLEMENTER ET UTILISER LE BUFFER
    private void initLocalBuffer() {
        if(listHasData) {
            extraBuffer = new LinkedList<>(importedInput);
        }
    }
    private void emptyBuffer() {
        for (String[] line : extraBuffer
             ) {
            extraBuffer.remove(line);
        }
    }

    private void fillBuffer() {
        for (String[] line: importedInput
             ) {
            extraBuffer.add(line.clone());
        }
    }

    private void resetBuffer() {
        emptyBuffer();
        fillBuffer();
    }


    //Auxilliaire
    private boolean listHasData() {
        return (importedInput.size() != 0);
    }
    private boolean isEqualStrCompareTo(int cmp2output) {
        return cmp2output == 0;
    }
    private boolean isInferiorStrCompareTo(int cmp2output) {
        return (cmp2output < 0);
    }
    private boolean isSuperiorStrCompareTo(int cmp2output) {
        return cmp2output > 0;
    }
    private void updateNbLines(){
        nbLines = importedInput.size();
    }
    private void updateNbCols() {
        nbCols = importedInput.getFirst().length;
    }

    private boolean inputLineIsValid(int line) {
        return (line >= 0 && line < nbLines);
    }

    private boolean inputColIsValid(int col) {
        return (col >= 0 && col < nbCols);
    }
    @Override
    public void close() throws Exception {
    // Fermeture du feed de communication
    }
}
