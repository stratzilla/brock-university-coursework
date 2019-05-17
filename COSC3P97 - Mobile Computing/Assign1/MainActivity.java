package com.example.rs09co.cosc3p97assignment1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import android.view.*;

import java.lang.*;
import java.util.*;

public class MainActivity extends AppCompatActivity {

    final String operators = "+-÷×"; //list of operators I'm using
    String calcMemory = ""; //initial memory is empty
    boolean foundResult = false; //for autoclearing results

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //the textbox which holds expression
        final TextView expressionField = (TextView) findViewById(R.id.expressionBar);
        //numeric buttons
        Button buttonDoubleZero = (Button) findViewById(R.id.buttonDoubleZero);
        Button buttonZero = (Button) findViewById(R.id.buttonZero);
        Button buttonOne = (Button) findViewById(R.id.buttonOne);
        Button buttonTwo = (Button) findViewById(R.id.buttonTwo);
        Button buttonThree = (Button) findViewById(R.id.buttonThree);
        Button buttonFour = (Button) findViewById(R.id.buttonFour);
        Button buttonFive = (Button) findViewById(R.id.buttonFive);
        Button buttonSix = (Button) findViewById(R.id.buttonSix);
        Button buttonSeven = (Button) findViewById(R.id.buttonSeven);
        Button buttonEight = (Button) findViewById(R.id.buttonEight);
        Button buttonNine = (Button) findViewById(R.id.buttonNine);
        //operator+decimal buttons
        Button buttonPlus = (Button) findViewById(R.id.buttonPlus);
        Button buttonMinus = (Button) findViewById(R.id.buttonMinus);
        Button buttonMult = (Button) findViewById(R.id.buttonMult);
        Button buttonObe = (Button) findViewById(R.id.buttonObe);
        Button buttonEqual = (Button) findViewById(R.id.buttonEqual);
        Button buttonDec = (Button) findViewById(R.id.buttonDec);
        //memory/clearing buttons
        Button buttonMemSave = (Button) findViewById(R.id.buttonMemSave);
        Button buttonMemRecall = (Button) findViewById(R.id.buttonMemRecall);
        Button buttonBackspace = (Button) findViewById(R.id.buttonBackspace);
        Button buttonClear = (Button) findViewById(R.id.buttonClear);

        /*
         * a lot of this code is repeated or very similar, so I'll only comment first-time
         * or new code. With these listeners, there is not much that can be done for code re-use
         * as far as I can tell. Some is reused, but you'll see a lot of sameness going forward.
         */

        //listener for numeric button
        buttonDoubleZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View e) { //why not have 00? My numpad has this
                clearResult(expressionField); //see later, but clears if previous result on screen
                //split it up so it's not so ugly a few lines down
                String currentExpression = (String) expressionField.getText();
                //if expression bar is not empty
                if (!currentExpression.equals("")) {
                    //find newest character inputted into expression bar
                    char finalChar = currentExpression.charAt(currentExpression.length() - 1);
                    //if it's not an operator (can't have 5+0, this is obvious arithmetic)
                    if (!operators.contains(String.valueOf(finalChar))) {
                        //if kosher, append to expression bar
                        expressionField.setText(expressionField.getText() + "00");
                    }
                }
            }
        });

        buttonZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View e) {
                clearResult(expressionField);
                String currentExpression = (String) expressionField.getText();
                if (!currentExpression.equals("")) {
                    char finalChar = currentExpression.charAt(currentExpression.length() - 1);
                    if (!operators.contains(String.valueOf(finalChar))) {
                        expressionField.setText(expressionField.getText() + "0");
                    }
                }
            }
        });

        //first nonzero button listener, it's slightly different
        buttonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View e) {
                clearResult(expressionField);
                //append to expression bar
                expressionField.setText(expressionField.getText() + "1");
            }
        });

        buttonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View e) {
                clearResult(expressionField);
                expressionField.setText(expressionField.getText() + "2");
            }
        });

        buttonThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View e) {
                clearResult(expressionField);
                expressionField.setText(expressionField.getText() + "3");
            }
        });

        buttonFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View e) {
                clearResult(expressionField);
                expressionField.setText(expressionField.getText() + "4");
            }
        });

        buttonFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View e) {
                clearResult(expressionField);
                expressionField.setText(expressionField.getText() + "5");
            }
        });

        buttonSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View e) {
                clearResult(expressionField);
                expressionField.setText(expressionField.getText() + "6");
            }
        });

        buttonSeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View e) {
                clearResult(expressionField);
                expressionField.setText(expressionField.getText() + "7");
            }
        });

        buttonEight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View e) {
                clearResult(expressionField);
                expressionField.setText(expressionField.getText() + "8");
            }
        });

        buttonNine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View e) {
                clearResult(expressionField);
                expressionField.setText(expressionField.getText() + "9");
            }
        });

        //first operator button listener
        buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View e) {
                clearResult(expressionField); //clear result if new expression
                String currentExpression = (String) expressionField.getText(); //save space
                //if expression empty
                if (currentExpression.equals("")) {
                    //add 0 then the operator
                    expressionField.setText(expressionField.getText() + "0");
                    expressionField.setText(expressionField.getText() + "+");
                } else { //if expression bar not empty
                    //find newest character inputted in expression bar
                    char finalChar = currentExpression.charAt(currentExpression.length() - 1);
                    //can't chain operators, ignore it
                    if (!operators.contains(String.valueOf(finalChar))) {
                        //if unignored, append operator to expression
                        expressionField.setText(expressionField.getText() + "+");
                    }
                }
            }
        });

        buttonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View e) {
                clearResult(expressionField);
                String currentExpression = (String) expressionField.getText();
                if (currentExpression.equals("")) {
                    expressionField.setText(expressionField.getText() + "0");
                    expressionField.setText(expressionField.getText() + "-");
                } else {
                    char finalChar = currentExpression.charAt(currentExpression.length() - 1);
                    if (!operators.contains(String.valueOf(finalChar))) {
                        expressionField.setText(expressionField.getText() + "-");
                    }
                }
            }
        });

        buttonMult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View e) {
                clearResult(expressionField);
                String currentExpression = (String) expressionField.getText();
                if (currentExpression.equals("")) {
                    expressionField.setText(expressionField.getText() + "0");
                    expressionField.setText(expressionField.getText() + "×");
                } else {
                    char finalChar = currentExpression.charAt(currentExpression.length() - 1);
                    if (!operators.contains(String.valueOf(finalChar))) {
                        expressionField.setText(expressionField.getText() + "×");
                    }
                }
            }
        });

        buttonObe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View e) {
                clearResult(expressionField);
                String currentExpression = (String) expressionField.getText();
                if (currentExpression.equals("")) {
                    expressionField.setText(expressionField.getText() + "0");
                    expressionField.setText(expressionField.getText() + "÷");
                } else {
                    char finalChar = currentExpression.charAt(currentExpression.length() - 1);
                    if (!operators.contains(String.valueOf(finalChar))) {
                        expressionField.setText(expressionField.getText() + "÷");
                    }
                }
            }
        });

        //different operator; equal/evaluate
        buttonEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View e) {
                clearResult(expressionField);
                String currentExpression = (String) expressionField.getText();
                if (!currentExpression.equals("")) {
                    char finalChar = currentExpression.charAt(currentExpression.length() - 1);
                    if (!operators.contains(String.valueOf(finalChar))) {
                        /*
                         *evaluate expression by translating to postfix first
                         * then easily solving the postfix.
                         */
                        expressionField.setText(evaluate(currentExpression));
                        //a result was found, so flag it so next input is start of new exp
                        foundResult = true;
                    }
                }
            }
        });

        //decimal button listener
        buttonDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View e) {
                clearResult(expressionField);
                String currentExpression = (String) expressionField.getText();
                if (!currentExpression.equals("")) {
                    char finalChar = currentExpression.charAt(currentExpression.length() - 1);
                    //can't have decimal after operator
                    if (!operators.contains(String.valueOf(finalChar))) {
                        /*
                         * holy hell I hate regex but it's a miracle sometimes.
                         * adds a decimal to rightmost operator, ignoring it if
                         * a decimal already exists in it.
                         */
                        expressionField.setText(currentExpression.replaceAll("(?<!\\.)\\b\\d+\\b(?!\\.)(?!.*\\d)", "$0."));
                    } else { //actually, you can, but consider start nf new operand as 0
                        expressionField.setText(expressionField.getText() + "0");
                        expressionField.setText(expressionField.getText() + ".");
                    }
                } else { //likewise, start of expression starts with 0
                    expressionField.setText(expressionField.getText() + "0");
                    expressionField.setText(expressionField.getText() + ".");
                }
            }
        });

        //listener for memory saving
        buttonMemSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View e) {
                //store it in string init earlier
                calcMemory = (String) expressionField.getText();
                //clear the expression bar
                expressionField.setText("");
            }
        });

        //listener for retrieving memory
        buttonMemRecall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View e) {
                //set expression field to what stored in memory
                expressionField.setText(calcMemory);
            }
        });

        //listener for backspace/C
        buttonBackspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View e) {
                String currentExpression = (String) expressionField.getText();
                if (!currentExpression.equals("")) {
                    //make substring from 0..length-1, deleting last character
                    expressionField.setText(currentExpression.substring(0, expressionField.length() - 1));
                }
            }
        });

        //listener for clearing expression
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View e) {
                foundResult = true; //treat it as result found
                clearResult(expressionField); //then erase it
            }
        });
    }

    /*
     * method to clear expression bar after a result has been found
     *
     * @param in - the expression bar
     */
    public void clearResult(TextView in) {
        if (foundResult) { //if flag is true
            foundResult = false; //reset it to false
            in.setText(""); //set expression bar to empty
        }
    }

    /*
     * method to evaluate postfix equation
     *
     * @param in - the postfix string to solve
     * @return result concatenated to a string
     */
    public String evaluate(String in) {
        /*
         * use Djikstra's shunting yard algorithm to convert from infix to postfix
         * sometimes I ended up with space reduplication, so trim it just in case
         */
        String postFix = shuntingYard(in).replaceAll("\\s+", " ").trim();
        Stack<Double> theStack = new Stack<Double>(); //make a stack to "recursively" solve from
        //basically split up by operands and use appropriate operator
        Scanner token = new Scanner(postFix); //to sequentially read postfix equation
        while (token.hasNext()) { //while there exists more characters
            if (token.hasNextDouble()) { //if next is an operand
                theStack.push(token.nextDouble()); //push to stack
            } else { //otherwise operator
                double preNum = theStack.pop(); //first number
                double postNum = theStack.pop(); //second number
                String operator = token.next(); //get operator
                if (operator.equals("+")) { //if addition, add two nums
                    theStack.push(postNum + preNum);
                } else if (operator.equals("-")) { //likewise subtraction
                    theStack.push(postNum - preNum);
                } else if (operator.equals("×")) { //likewise multiplication
                    theStack.push(postNum * preNum);
                } else { //lastly division
                    theStack.push(postNum / preNum);
                }
            }
        }
        //normalize output before returning
        return normalize(theStack.pop());
    }

    /*
     * method to normalize output after evaluation
     *
     * @param in - the result to normalize
     * @return a string formatted normalized result
     */
    public String normalize(double in) {
        double result = in;
        //below three lines are just rounding for my sanity
        result *= 1000000;
        result = Math.round(result);
        result /= 1000000;
        /*
         * floating point arithmetic sucks, so catch cases of inaccuracy where you get
         * nonsense like 3.0 - 1.0 = 1.999999999999
         */
        result = (result < 0.000001 && result > 0) ? 0 : result;
        if (result == (int) result) { //just to remove decimal from answer
            return "" + (int) result; //return 4.0 as 4 for example
        }
        return "" + result; //otherwise return the decimal
    }

    /*
     * standard shunting yard algorithm by Djikstra
     * convert infix expression to postfix expression which
     * makes it easier to solve
     *
     * @param in - the infix string to turn into postfix
     * @return the postfix string
     */
    public String shuntingYard(String in) {
        String sBuilder = ""; //for building postfix string
        Stack<Double> theStack = new Stack<Double>(); //a stack for adding to
        //seperate into tokens by operands; keep decimals
        for (String token : in.split("(?<=[-+÷×])|(?=[-+÷×])")) {
            if (!token.equals("")) { //as long as token isnt blank
                char c = token.charAt(0); //find operator
                double d = operators.indexOf(c); //find index of operator
                if (d != -1) { //if not operand
                    if (theStack.isEmpty()) {
                        theStack.push(d); //append to stack
                    } else {
                        while (!theStack.isEmpty()) {
                            double precedenceA = theStack.peek() / 2; //treat + as -, x as /, etc
                            double precedenceB = d / 2; //same weights
                            //for order of operations, BEDMAS, etc
                            if (precedenceA > precedenceB || (precedenceA == precedenceB)) {
                                //append to string
                                sBuilder += (operators.charAt(theStack.pop().intValue())) + (" ");
                            } else {
                                break;
                            }
                        }
                        theStack.push(d);
                    }
                } else { //is operator
                    sBuilder += (token) + (" "); // append operand
                }
            }
        }
        //append it all to the string
        while (!theStack.isEmpty()) {
            sBuilder += (operators.charAt(theStack.pop().intValue())) + (" ");
        }
        return sBuilder.toString(); //return it!
    }
}
