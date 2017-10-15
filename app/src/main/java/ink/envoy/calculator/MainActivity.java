package ink.envoy.calculator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView inputTextView;
    private TextView resultTextView;

    private Button number0Button;
    private Button number1Button;
    private Button number2Button;
    private Button number3Button;
    private Button number4Button;
    private Button number5Button;
    private Button number6Button;
    private Button number7Button;
    private Button number8Button;
    private Button number9Button;
    private Button numberDotButton;

    private Button constantPIButton;

    private Button operatorAddButton;
    private Button operatorSubtractButton;
    private Button operatorMultiplyButton;
    private Button operatorDivideButton;
    private Button operatorLeftBracketButton;
    private Button operatorRightBracketButton;
    private Button operatorFactorialButton;
    private Button operatorPowerButton;
    private Button operatorSqrtButton;
    private Button operatorPercentButton;

    private Button commandEqualButton;
    private Button commandBackspaceButton;
    private Button commandClearButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindViews();
        setOnClickListeners();

        inputTextView.setText("0");
        resultTextView.setText("");
    }

    private void bindViews() {
        inputTextView = (TextView) findViewById(R.id.inputTextView);
        resultTextView = (TextView) findViewById(R.id.resultTextView);
        
        number0Button = (Button) findViewById(R.id.number0Button);
        number1Button = (Button) findViewById(R.id.number1Button);
        number2Button = (Button) findViewById(R.id.number2Button);
        number3Button = (Button) findViewById(R.id.number3Button);
        number4Button = (Button) findViewById(R.id.number4Button);
        number5Button = (Button) findViewById(R.id.number5Button);
        number6Button = (Button) findViewById(R.id.number6Button);
        number7Button = (Button) findViewById(R.id.number7Button);
        number8Button = (Button) findViewById(R.id.number8Button);
        number9Button = (Button) findViewById(R.id.number9Button);
        numberDotButton = (Button) findViewById(R.id.numberDotButton);

        operatorAddButton = (Button) findViewById(R.id.operatorAddButton);
        operatorSubtractButton = (Button) findViewById(R.id.operatorSubtractButton);
        operatorMultiplyButton = (Button) findViewById(R.id.operatorMultiplyButton);
        operatorDivideButton = (Button) findViewById(R.id.operatorDivideButton);
        operatorLeftBracketButton = (Button) findViewById(R.id.operatorLeftBracketButton);
        operatorRightBracketButton = (Button) findViewById(R.id.operatorRightBracketButton);
        operatorFactorialButton = (Button) findViewById(R.id.operatorFactorialButton);
        operatorPowerButton = (Button) findViewById(R.id.operatorPowerButton);
        operatorSqrtButton = (Button) findViewById(R.id.operatorSqrtButton);
        operatorPercentButton = (Button) findViewById(R.id.operatorPercentButton);
        constantPIButton = (Button) findViewById(R.id.constantPIButton);

        commandEqualButton = (Button) findViewById(R.id.commandEqualButton);
        commandBackspaceButton = (Button) findViewById(R.id.commandBackspaceButton);
        commandClearButton = (Button) findViewById(R.id.commandClearButton);
    }
    
    private void setOnClickListeners() {
        number0Button.setOnClickListener(this);
        number1Button.setOnClickListener(this);
        number2Button.setOnClickListener(this);
        number3Button.setOnClickListener(this);
        number4Button.setOnClickListener(this);
        number5Button.setOnClickListener(this);
        number6Button.setOnClickListener(this);
        number7Button.setOnClickListener(this);
        number8Button.setOnClickListener(this);
        number9Button.setOnClickListener(this);
        numberDotButton.setOnClickListener(this);

        operatorAddButton.setOnClickListener(this);
        operatorSubtractButton.setOnClickListener(this);
        operatorMultiplyButton.setOnClickListener(this);
        operatorDivideButton.setOnClickListener(this);
        operatorLeftBracketButton.setOnClickListener(this);
        operatorRightBracketButton.setOnClickListener(this);
        operatorFactorialButton.setOnClickListener(this);
        operatorPowerButton.setOnClickListener(this);
        operatorSqrtButton.setOnClickListener(this);
        operatorPercentButton.setOnClickListener(this);
        constantPIButton.setOnClickListener(this);

        commandEqualButton.setOnClickListener(this);
        commandBackspaceButton.setOnClickListener(this);
        commandClearButton.setOnClickListener(this);
    }

    private void activateEqualView() {
        inputTextView.setTextSize(20);
        resultTextView.setTextSize(36);
    }

    private void deactivateEqualView() {
        inputTextView.setTextSize(36);
        resultTextView.setTextSize(20);
    }

    @SuppressLint("SetTextI18n")
    private void invalidateResult(boolean isInEqualView) {
        String expr = inputTextView.getText().toString();
        if (expr.equals("0")) return;
        try {
            String result = ExpressionUtils.compute(expr).toPlainString();
            if (result.length() > 25) {
                result = result.substring(0, 25) + "...";
            }
            resultTextView.setText("= " + result);
        } catch (EmptyExpressionException | IllegalExpressionException
                | InvalidComputationException e) {
            if (isInEqualView) {
                resultTextView.setText("非法表达式");
            }
        } catch (NumberFormatException e) {
            resultTextView.setText("数字格式错误");
        } catch (ArithmeticException e) {
            resultTextView.setText("不能除以零");
        } catch (CalculationOverflowException e) {
            resultTextView.setText("溢出");
        } catch (NonIntegerFactorialException e) {
            resultTextView.setText("不能对非整数求阶乘");
        } catch (NegativeFactorialException e) {
            resultTextView.setText("不能对负数求阶乘");
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        boolean isInEqualView = false;
        switch (v.getId()) {
            case R.id.operatorAddButton:
            case R.id.operatorSubtractButton:
            case R.id.operatorMultiplyButton:
            case R.id.operatorDivideButton:

            case R.id.operatorPowerButton:
            case R.id.operatorSqrtButton:
            case R.id.operatorPercentButton:
            case R.id.operatorFactorialButton:

            case R.id.constantPIButton:

            case R.id.operatorLeftBracketButton:
            case R.id.operatorRightBracketButton:

            case R.id.number0Button:
            case R.id.number1Button:
            case R.id.number2Button:
            case R.id.number3Button:
            case R.id.number4Button:
            case R.id.number5Button:
            case R.id.number6Button:
            case R.id.number7Button:
            case R.id.number8Button:
            case R.id.number9Button:
            case R.id.numberDotButton:
                deactivateEqualView();
                if (inputTextView.getText().toString().equals("0")) {
                    inputTextView.setText(((Button)v).getText());
                } else {
                    inputTextView.append(((Button) v).getText());
                }
                break;

            case R.id.commandBackspaceButton:
                deactivateEqualView();
                String text = inputTextView.getText().toString();
                int length = text.length();
                if (length > 0) {
                    inputTextView.setText(text.subSequence(0, length - 1));
                }
                break;

            case R.id.commandClearButton:
                deactivateEqualView();
                inputTextView.setText("0");
                resultTextView.setText("");
                break;

            case R.id.commandEqualButton:
                activateEqualView();
                isInEqualView = true;
                break;

            default:
                break;
        }
        invalidateResult(isInEqualView);
    }
}

