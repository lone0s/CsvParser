package CsvParser;

import java.io.*;
import java.util.LinkedList;

public class CsvParser implements AutoCloseable{
    public final String SEPARATING_CHAR = ","; //Eventuellement prompt l'utilisateur sur le charactère

    //Gestion de l'input et suivi de l'état
    private File inputCsv;
    private boolean iCsvInitialized = false;
    private int nbLines = 0;
    private int nbCols = 0;

    //Gestion de la lecture de l'input data + état
    private BufferedReader inputCsvReader;
    private boolean readerIsAtEOF = true;

    //Gestion de l'input en mémoire centrale et de l'état
    public LinkedList<String[]> importedInput;
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
        iCsvInitialized = true;
        inputCsvReader = new BufferedReader(new FileReader(inputCsv));
        readerIsAtEOF = false;
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
    //Lecture d'une ligne du CSV dans la version en mémoire
    public String[] extractLine(int line) {
        String[] res = {"-1"};
        if (listHasData){
            if (line < importedInput.size()){
                res = importedInput.get(line);
            }
        }
        return res;
    }

    public String[] extractColumn(int col) {
        String[] res = new String[nbLines];
        int cpt = 0 ;
        if (listHasData){
            if (col < nbCols){
                for (String[] line: importedInput) {
                    res[cpt] = line[col];
                    cpt++;
                }
            }
        }
        System.out.println("Extracted Column !");
        return res;
    }
    //On considère la première ligne comme un entete sans contenu
    //pb si jamais la colonne extraite contient des données autres que numériques :(

    public double maxValueInColumn(int col){
        double max = 0;
        if (listHasData){
            if(col < nbCols){
                String[] extractedColumn = extractColumn(col);
                for (int i = 1; i < extractedColumn.length ; i++){
                    double extractedValue = Double.parseDouble(extractedColumn[i]);
                    if (extractedValue > max)
                        max = extractedValue;
                }
            }
        }
        return max;
    }

    public double minValueInColumn(int col) {
        double min = 0;
        if (listHasData){
            if(col < nbCols){
                String[] extractedColumn = extractColumn(col);
                for (int i = 1; i < extractedColumn.length ; i++){
                    double extractedValue = Double.parseDouble(extractedColumn[i]);
                    if (extractedValue < min)
                        System.out.println("Here"); // ???????????????????? <== En enlevant ca ne marche plus ???????
                        min = extractedValue;
                }
            }
        }
        return min;
    }

    public double avgValueInColumn(int col) {
        double avg = 0;
        double cpt = 0;
        if (listHasData) {
            if (col < nbCols) {
                String[] extractedColumn = extractColumn(col);
                for (int i = 1 ; i < extractedColumn.length ; i++){
                    avg += Double.parseDouble(extractedColumn[i]);
                    cpt ++;
                }
            }
        }
        if (cpt == 0)
            return 0;
        else return (avg/cpt);
    }

//    public String[] extractArrayOfData(int startLine, int startCol, int endLine, int endCol){
//        LinkedList<String[]> auxData = new LinkedList<>();
//        if (startLine > 0 && startCol > 0 && endLine < nbLines && endCol < nbCols){
//
//        }
//    }

    //Auxilliaire
    private boolean listHasData() {
        return (importedInput.size() != 0);
    }
    private void updateNbLines(){
        nbLines = importedInput.size();
    }
    private void updateNbCols() {
        nbCols = importedInput.getFirst().length;
    }
    @Override
    public void close() throws Exception {
    // Fermeture du feed de communication
    }
}
