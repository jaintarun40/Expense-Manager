package com.example.varun.expensemanager;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CalculatorActivity extends AppCompatActivity {
    private TextView mExpression;
    private TextView mAfterExpression;
    private ActionBar mActionBar;

    double answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        mExpression = (TextView) findViewById(R.id.calc_expression);
        mAfterExpression = (TextView) findViewById(R.id.after_expression);
        mActionBar = getSupportActionBar();

        mActionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void myClick(View view)
    {
        switch (view.getId())
        {
            case (R.id.zero_button):
            {
                mExpression.setText(mExpression.getText()+"0");
                break;
            }
            case (R.id.one_button):
            {
                mExpression.setText(mExpression.getText()+"1");
                break;
            }
            case (R.id.two_button):
            {
                mExpression.setText(mExpression.getText()+"2");
                break;
            }
            case (R.id.three_button):
            {
                mExpression.setText(mExpression.getText()+"3");
                break;
            }
            case (R.id.four_button):
            {
                mExpression.setText(mExpression.getText()+"4");
                break;
            }
            case (R.id.five_button):
            {
                mExpression.setText(mExpression.getText()+"5");
                break;
            }
            case (R.id.six_button):
            {
                mExpression.setText(mExpression.getText()+"6");
                break;
            }
            case (R.id.seven_button):
            {
                mExpression.setText(mExpression.getText()+"7");
                break;
            }
            case (R.id.eight_button):
            {
                mExpression.setText(mExpression.getText()+"8");
                break;
            }
            case (R.id.nine_button):
            {
                mExpression.setText(mExpression.getText()+"9");
                break;
            }
            case (R.id.deci_button):
            {
                mExpression.setText(mExpression.getText()+".");
                break;
            }
            case (R.id.AC_button):
            {
                mExpression.setText("");
                mAfterExpression.setText("");
                answer = 0;
                break;
            }
            case (R.id.bksp_button):
            {
                if(mExpression.getText().toString().length()>0)
                    mExpression.setText(mExpression.getText().toString().substring(0,mExpression.getText().toString().length()-1));
                break;
            }
            case (R.id.divide_button):
            {
                mExpression.setText(mExpression.getText()+"/");
                break;
            }
            case (R.id.perc_button):
            {
                String expression = mExpression.getText().toString();
                int i=expression.length()-1;
                while((Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.') && i>=0)
                {
                    i--;
                }
                if(i<expression.length()-1)
                {
                    expression=expression.substring(i+1);
                    double x = Double.parseDouble(expression);
                    x = x*0.01;
                    mExpression.setText(mExpression.getText().toString().substring(0,i+1));
                    mExpression.setText(mExpression.getText()+Double.toString(x));
                }
                break;
            }
            case (R.id.multiply_button):
            {
                mExpression.setText(mExpression.getText()+"*");
                break;
            }
            case (R.id.minus_button):
            {
                mExpression.setText(mExpression.getText()+"-");
                break;
            }
            case (R.id.plus_button):
            {
                mExpression.setText(mExpression.getText()+"+");
                break;
            }
            case (R.id.equal_button):{
                String expression = mExpression.getText().toString();

                if(expression.length()>0)
                    answer = eval(expression);

                mExpression.setText(Double.toString(answer));
                mAfterExpression.setText(expression);
                break;
            }
            case (R.id.ok_button):{
                Intent intent = new Intent();
                intent.putExtra("amount", answer);
                setResult(RESULT_OK, intent);
                finish();
                break;
            }
        }
    }
    public double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case (android.R.id.home):{
                this.finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}