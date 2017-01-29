package com.example.varun.expensemanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class ExpenseActivity extends AppCompatActivity {

    private Spinner mSpinnerIncomeExpense;
    private Button mCalciButton;
    private EditText mAmount;
    private Spinner mSpinnerPaymentMethod;

    private static final int CALCULATOR=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        mSpinnerIncomeExpense = (Spinner) findViewById(R.id.expense_income_spinner);
        mAmount = (EditText) findViewById(R.id.amount_text);
        mSpinnerPaymentMethod = (Spinner) findViewById(R.id.payment_method_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.income_expense, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerIncomeExpense.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.payment_methods, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerPaymentMethod.setAdapter(adapter1);

        mCalciButton = (Button) findViewById(R.id.calci_button);
        mCalciButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExpenseActivity.this, CalculatorActivity.class);
                startActivityForResult(intent, CALCULATOR);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CALCULATOR)
        {
            if(resultCode == RESULT_OK)
            {
                double amount = data.getDoubleExtra("amount", 0);
                mAmount.setText(Double.toString(amount));
            }
            else
            {
                mAmount.setText(mAmount.getText());
            }
        }
    }
}