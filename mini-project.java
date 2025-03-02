/* ***************************************
  @author    Blessing Amike
  @SID       210373644
  @date      10 December 2024
  @version   1

    Miniproject Level 8
    A word association quiz. The user now has the option to create a new file use an existing one or quit the game. 
    If they choose to create a new file they can add a question set to that file consisting of 3 questions along with their answers
    and 4 cluewords each. This is then saved and can be used by players later if they choose. Or they can use an existing file
    containing a question set and play the quiz that way. Their points are added up to give a total score.
   ****************************************/
import java.util.Scanner;// Needed to make Scanner available
import java.util.Random; 
import java.io.*; // File handling
class Question{ // ADT Questions with appropriate fields
    String questionWords;
    String[] clueWords;
    String correctAnswer;
    boolean asked;}
    

class WordQuiz // Word quiz main class
{
    //main
    public static void main (String[] args)throws IOException{
        printRules();
        mainQuiz();
        return;}//END main

    
   // main method that asks the user for their choice and calls all other methods
    public static void mainQuiz() throws IOException{ 
        boolean continueQuiz = true; // boolean variable used to keep track of if the user wants to continue playing or not
        while(continueQuiz){
            String choice = validatedChoice();
            if(choice.equalsIgnoreCase("new")){
                String filename = validFileNameAndFormat("new");
                inputAndWriteQuestions(filename);}
                // validation methods are called throughout
            else if(choice.equalsIgnoreCase("load")){
                boolean loadAnother = true;
                while(loadAnother){
                    String filename = validFileNameAndFormat("load");
                    Question [] questionset = readAndPrintQuestions(filename);
                    playQuiz(questionset);
                    String newQuestionSet = inputString("Do you want to use another question set?\nEnter (yes/no):");
                    if(!newQuestionSet.equalsIgnoreCase("yes")){//user asked if they want to create new question set
                        loadAnother = false;}
                }
            }
            else if(choice.equalsIgnoreCase("quit")){
                print("Exiting game... Goodbye!");
                continueQuiz = false;}// boolean set to false to end loop
        }
        return;
                
    } // END mainQuiz


    
    // Print rules of the quiz
    public static void printRules()
    {
        print("Welcome to the Word Connection quiz!");
        print("You will be given a series of words and you must guess the link between them.");
        print("The fewer guesses you use to get the correct answer, the more points you will get.");
        print("Good Luck!");
        return;
    }
    // END printRules

    
    // method that asks the user the quesions and adds up their total score
    public static void playQuiz(Question [] questions){ 
        Random random = new Random();
        int totalScore = 0;

        for(int i =0; i<3; i++){
            int index = random.nextInt(questions.length);
            while(isAsked(questions[index])){
                index = random.nextInt(questions.length);}
            int score = askQuestions(questions[index]);
            totalScore+= score;
            setAsked(questions[index],true);}
        print("Well done! You total score is: " + totalScore);
        return;} 
    // END playQuiz

    
    // Allows the user to write to a new file and create a question set in that file
    public static void inputAndWriteQuestions(String fileName) throws IOException // allows the reader to create a file of a new set of questions
    {
        final int NUMQUESTIONS = 3;
        PrintWriter question_file = new PrintWriter(new FileWriter(fileName));
            
        for (int i=0 ; i<NUMQUESTIONS ; i++){
          
            String questionWords = inputString("Enter the two connection words in format (word1 and word2):");
            String correctAnswer = inputString("Enter the answer:");
            String clues = inputString("Enter the cluewords seperated by spaces:");
            question_file.println(questionWords+","+correctAnswer+","+clues);}
        
        print("New set of questions has been added to file: " +fileName);
        question_file.close(); // close file
    } // END inputAndWriteQuestions

    
    // Allows user to read from exisiting file and use question set in the file they choose
    public static Question[]readAndPrintQuestions(String filename) throws IOException 
    {
        BufferedReader question_file = new BufferedReader(new FileReader(filename));
        final int NUMQUESTIONS = 3; // final variable used for literal constant, number of questions player can add
        Question [] questionset = new Question[NUMQUESTIONS];
        int index = 0;
        String questions;

        while ((questions=question_file.readLine())!=null && index < questionset.length)
        {
            String []questionComponents = questions.split(",");
            String questionWords= questionComponents[0];
            String correctAnswer = questionComponents[1];
            String [] clueWords = questionComponents[2].split(" ");

            Question quest= new Question();
            setQuestionWords(quest,questionWords); // accessor methods used to set the fields of the ADT Questions
            setCorrectAnswer(quest,correctAnswer);
            setClueWords(quest,clueWords);
            
            questionset[index] = quest;
            index++;
        
        }
        question_file.close();
        return questionset;
    }//END readAndPrintQuestions

    // Handles a single question and calculates score
    public static int askQuestions(Question question)
    {
        final int MAX_ATTEMPTS = getClueWords(question).length; //final variables for literal constants
        final int SCORE_DECREMENT = 2;
        int score = 10;
        boolean isCorrect = false;
        
        String [] clueWords = getClueWords(question);
        print("Question:" + getQuestionWords(question));
        
        for (int attempts = 0; attempts < MAX_ATTEMPTS && !isCorrect; attempts++){ // for loop used to asked the player questions until they run out of thir max attempts
            if(attempts>0){
                int clueIndex = 0;
                while(clueIndex< attempts && clueIndex < clueWords.length){// clues give on each attempt if attempts is greater than 0
                    printClue(clueWords, clueIndex);
                    clueIndex++;}
                    }

            String userAnswer = inputString("Enter your answer:");
            if (userAnswer.equalsIgnoreCase(getCorrectAnswer(question))) {
                print("Correct! You scored " + score + " points.");
                isCorrect = true;}
                
            else {
                print("Incorrect answer. Try Again!");
                score -= SCORE_DECREMENT;} // decrements score by 2 if player gets the question wrong
                }
        if(!isCorrect){
            print("Oops!You've run out of attempts.The correct answer was: " + getCorrectAnswer(question));} // tells player the corrcet answer once they have run out of attempts

        return score; // returns their final score
    } //END askQuestion

    
//***************************************************************
// INPUT VALIDATION METHODS : use of while loops and if statements to continue asking user for valid input and returns their input
    
    //Ensures the user enters a valid choice
    public static String validatedChoice(){ 
        String choice = inputString("Type 'New' to create a question\n'Load' to use an existing set\nor'Quit' to exit the game");
        while(!choice.equalsIgnoreCase("new") && !choice.equalsIgnoreCase("load") && !choice.equalsIgnoreCase("quit")){
            print("Invalid choice. Please enter one of the three options: 'New' 'Load' or 'Quit'.");
            choice = inputString("Type 'New' to create a question\n'Load' to use an existing set\nor'Quit' to exit the game");}
        return choice;}
    // END validatedChoice

    // Ensure the user enters a valid filename with the correct format
    //Ensures filename doesn't already exist
    public static String validFileNameAndFormat(String type){
        String fileName = inputString("Enter the name of the file:");
        boolean isValidInput = false;
        while(!isValidInput){
            if(!isValidFilename(fileName)){
                print("Invalid format. Please end your filename name with (.csv)");} 
            else if("new".equalsIgnoreCase(type) && doesFileExist(fileName)){
                print("A file with this name already exists. Please enter a different name:");}
            else{
                isValidInput = true;} // break loop if the  user enters a valid input
            if(!isValidInput){
                fileName = inputString("Please enter the  name of the file you wish to create or load(.csv):");}
        }
        return fileName;}
    // END validFileNameAndFormat
    
    // checks if filename has .csv
    public static boolean isValidFilename(String fileName){
        int length = fileName.length();
        boolean isCsv = false;
        // filename must have at least 4 characters and .csv
        if (length>=4){
            String suffix = fileName.substring(length-4);
            isCsv = suffix.equalsIgnoreCase(".csv");}
        return isCsv;}
    // END isValidFilename
    
    // checks if a file with the name user created already exists
    public static boolean doesFileExist(String fileName){
        File file = new File(fileName);
        return file.exists() && !file.isDirectory();}
    //END doesFileExist

    
//********************************************************************
// INPUT HELPERS
    // prints out a string
    public static void print(String message){ 
        System.out.println(message);}
    // END print

    // prints the clues from array using the index of the array 
    public static void printClue(String[] clueWords, int clueIndex){
        print("Clue:" + clueWords[clueIndex]);}
    // END printClue
    
    // asks user to input string user message
    public static String inputString(String message){ 
        Scanner scanner = new Scanner(System.in);
        print(message);
        String userInput = scanner.nextLine();
        return userInput;}
    // END inputString
    
    // takes user input which should be a positive integer and returns the value
    public static int inputInt(String message)
    {
        int userInputInt = -1;
        Scanner scanner = new Scanner(System.in);
        // while loop used to continously ask the user for an integer until they input a positive integer
        while (userInputInt < 0){
            System.out.println(message);
            userInputInt = Integer.parseInt(scanner.nextLine());
            if (userInputInt<0){
                System.out.println("Please enter a non negative number:");
            }
        }
        return userInputInt;
        }// END InputInt

    
//***************************************************************
//ACCESSOR METHODS USED TO ACCESS FIELDS OF ADT Question
    // get and set Question words
    public static String getQuestionWords(Question q){
        return q.questionWords;}
    public static Question setQuestionWords(Question q, String questionWords){
        q.questionWords = questionWords;
        return q;}
    
    // get and set correct answer
    public static String getCorrectAnswer(Question q){
        return q.correctAnswer;}
    public static Question setCorrectAnswer(Question q, String correctAnswer){
        q.correctAnswer = correctAnswer;
        return q;}
    
    // get and set clue words
    public static String [] getClueWords(Question q){
        return q.clueWords;}
    public static Question setClueWords(Question q, String [] clueWords){
        q.clueWords= clueWords;
        return q;}
    
    // boolean determining if asked and then sets asked boolean
    public static boolean isAsked(Question q){
        return q.asked;}
    public static Question setAsked(Question q, boolean asked){
        q.asked = asked;
        return q;}

//END Accessor Methods

} // END class WordQuiz
