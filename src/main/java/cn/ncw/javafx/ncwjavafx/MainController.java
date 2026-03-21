package cn.ncw.javafx.ncwjavafx;

import cn.ncw.javafx.ncwjavafx.base.ExpressionCalculator;
import cn.ncw.javafx.ncwjavafx.base.PyRandom;
import cn.ncw.javafx.ncwjavafx.core.RationalExpressionEvaluator;
import cn.ncw.javafx.ncwjavafx.pyex.os;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static cn.ncw.javafx.ncwjavafx.base.CommonVariable.*;

public class MainController {

    public static boolean PLAYING = true;

    public static String expression = "";

    @FXML
    public StackPane MainStagePane;

    @FXML
    public ImageView MainImageView;

    @FXML
    public Pane MainPane;

    @FXML
    public JFXTextArea MainJFXTextArea;

    @FXML
    public JFXButton ButtonClear;

    @FXML
    public JFXButton ButtonNum0;

    @FXML
    public JFXButton ButtonNum1;

    @FXML
    public JFXButton ButtonNum2;

    @FXML
    public JFXButton ButtonNum3;

    @FXML
    public JFXButton ButtonNum4;

    @FXML
    public JFXButton ButtonNum5;

    @FXML
    public JFXButton ButtonNum6;

    @FXML
    public JFXButton ButtonNum7;

    @FXML
    public JFXButton ButtonNum8;

    @FXML
    public JFXButton ButtonNum9;

    @FXML
    public JFXButton ButtonNumDot;

    @FXML
    public JFXButton ButtonNumAdd;

    @FXML
    public JFXButton ButtonNumSub;

    @FXML
    public JFXButton ButtonNumMul;

    @FXML
    public JFXButton ButtonNumDiv;

    @FXML
    public JFXButton ButtonLeftBracket;

    @FXML
    public JFXButton ButtonRightBracket;

    @FXML
    public JFXButton ButtonNumEqu;

    @FXML
    public JFXButton ButtonPlay;

    @FXML
    public void initialize() {

        if (EASTER_EGG | !(MUSIC_IS_PLAYING)) {
            ButtonPlay.setOpacity(0);
            ButtonPlay.setDisable(true);
        }


            // 原有的背景初始化逻辑
        if (Objects.equals(BACKGROUND_MODE, "CUSTOM")) {
            List<String> list_images = os.listdir(os.getcwd() + "\\resources\\images\\custom");
            IMAGE = PyRandom.choose(list_images);
            logger.info("Image Choice: " + IMAGE, "controller");
            MainImageView.setImage(new Image(new File(os.getcwd() + "\\resources\\images\\custom\\" + IMAGE).toURI().toString()));
            MainImageView.setVisible(true);

            } else if (Objects.equals(BACKGROUND_MODE, "TIME")) {
            if (Long.parseLong(CURRENT_TIME) >= 180000000000L || Long.parseLong(CURRENT_TIME) < 60000000000L) {
                IMAGE = PyRandom.choose(LIST_NIGHT);
                logger.info("Image Choice: " + IMAGE, "controller");
                MainImageView.setImage(new Image(new File(os.getcwd() + "\\resources\\images\\time\\night\\" + IMAGE).toURI().toString()));
            } else {
                IMAGE = PyRandom.choose(LIST_DAY);
                logger.info("Image Choice: " + IMAGE, "controller");
                MainImageView.setImage(new Image(new File(os.getcwd() + "\\resources\\images\\time\\day\\" + IMAGE).toURI().toString()));
            }
            MainImageView.setVisible(true);

        }
    }

    @FXML
    private void ButtonNum0Click() {

        STRING_WAIT+="0";
        STRING_SHOW+="0";

        logger.info("Button0 was clicked. STRING_WAIT: " + STRING_WAIT, "Num0Click");

        MainJFXTextArea.setText(STRING_SHOW);

    }

    @FXML
    private void ButtonNum1Click() {

        STRING_WAIT+="1";
        STRING_SHOW+="1";

        logger.info("Button1 was clicked. STRING_WAIT: " + STRING_WAIT, "Num1Click");

        MainJFXTextArea.setText(STRING_SHOW);

    }

    @FXML
    private void ButtonNum2Click() {

        STRING_WAIT+="2";
        STRING_SHOW+="2";

        logger.info("Button2 was clicked. STRING_WAIT: " + STRING_WAIT, "Num2Click");

        MainJFXTextArea.setText(STRING_SHOW);

    }

    @FXML
    private void ButtonNum3Click() {

        STRING_WAIT+="3";
        STRING_SHOW+="3";

        logger.info("Button3 was clicked. STRING_WAIT: " + STRING_WAIT, "Num3Click");

        MainJFXTextArea.setText(STRING_SHOW);

    }

    @FXML
    private void ButtonNum4Click() {

        STRING_WAIT+="4";
        STRING_SHOW+="4";

        logger.info("Button4 was clicked. STRING_WAIT: " + STRING_WAIT, "Num4Click");

        MainJFXTextArea.setText(STRING_SHOW);

    }
    @FXML
    private void ButtonNum5Click() {

        STRING_WAIT+="5";
        STRING_SHOW+="5";

        logger.info("Button5 was clicked. STRING_WAIT: " + STRING_WAIT, "Num5Click");

        MainJFXTextArea.setText(STRING_SHOW);

    }

    @FXML
    private void ButtonNum6Click() {

        STRING_WAIT+="6";
        STRING_SHOW+="6";

        logger.info("Button6 was clicked. STRING_WAIT: " + STRING_WAIT, "Num6Click");

        MainJFXTextArea.setText(STRING_SHOW);

    }

    @FXML
    private void ButtonNum7Click() {

        STRING_WAIT+="7";
        STRING_SHOW+="7";

        logger.info("Button7 was clicked. STRING_WAIT: " + STRING_WAIT, "Num7Click");

        MainJFXTextArea.setText(STRING_SHOW);

    }

    @FXML
    private void ButtonNum8Click() {

        STRING_WAIT+="8";
        STRING_SHOW+="8";

        logger.info("Button8 was clicked. STRING_WAIT: " + STRING_WAIT, "Num8Click");

        MainJFXTextArea.setText(STRING_SHOW);

    }

    @FXML
    private void ButtonNum9Click() {

        STRING_WAIT+="9";
        STRING_SHOW+="9";

        logger.info("Button9 was clicked. STRING_WAIT: " + STRING_WAIT, "Num9Click");

        MainJFXTextArea.setText(STRING_SHOW);

    }
    @FXML
    private void ButtonNumDotClick() {

        STRING_WAIT+=".";
        STRING_SHOW+=".";

        logger.info("ButtonDot was clicked. STRING_WAIT: " + STRING_WAIT, "DotClick");

        MainJFXTextArea.setText(STRING_SHOW);

    }

    @FXML
    private void ButtonNumAddClick() {

        if (Objects.equals(STRING_WAIT, "")) {
            LIST_WAIT.addLast("+");
            LIST_FOR_OPERATING.addLast('+');
            expression += " + ";
        } else {
            LIST_WAIT.addLast(STRING_WAIT);
            LIST_WAIT.addLast("+");
            expression += STRING_WAIT;
            expression += " + ";
            LIST_FOR_OPERATING.addLast(STRING_WAIT);
            LIST_FOR_OPERATING.addLast('+');
            STRING_WAIT = "";
        }
        STRING_SHOW+="+";
        logger.info("ButtonAdd was clicked. LIST_WAIT: " + LIST_WAIT, "AddClick");
        MainJFXTextArea.setText(STRING_SHOW);

    }

    @FXML
    private void ButtonNumSubClick() {

        if (Objects.equals(STRING_WAIT, "")) {
            LIST_WAIT.addLast("-");
            LIST_FOR_OPERATING.addLast('-');
            expression += " - ";
        } else {
            LIST_WAIT.addLast(STRING_WAIT);
            LIST_WAIT.addLast("-");
            expression += STRING_WAIT;
            expression += " - ";
            LIST_FOR_OPERATING.addLast(STRING_WAIT);
            LIST_FOR_OPERATING.addLast('-');
            STRING_WAIT = "";
        }
        STRING_SHOW+="-";
        logger.info("ButtonSub was clicked. LIST_WAIT: " + LIST_WAIT, "SubClick");
        MainJFXTextArea.setText(STRING_SHOW);

    }

    @FXML
    private void ButtonNumMulClick() {

        if (Objects.equals(STRING_WAIT, "")) {
            LIST_WAIT.addLast("x");
            LIST_FOR_OPERATING.addLast('*');
            expression += " * ";
        } else {
            LIST_WAIT.addLast(STRING_WAIT);
            LIST_WAIT.addLast("x");
            expression += STRING_WAIT;
            expression += " * ";
            LIST_FOR_OPERATING.addLast(STRING_WAIT);
            LIST_FOR_OPERATING.addLast('*');
            STRING_WAIT = "";
        }
        STRING_SHOW+="x";
        logger.info("ButtonMul was clicked. LIST_WAIT: " + LIST_WAIT, "MulClick");
        MainJFXTextArea.setText(STRING_SHOW);

    }

    @FXML
    private void ButtonNumDivClick() {

        if (Objects.equals(STRING_WAIT, "")) {
            LIST_WAIT.addLast("/");
            LIST_FOR_OPERATING.addLast('/');
            expression += " / ";
        } else {
            LIST_WAIT.addLast(STRING_WAIT);
            LIST_WAIT.addLast("/");
            expression += STRING_WAIT;
            expression += " / ";
            LIST_FOR_OPERATING.addLast(STRING_WAIT);
            LIST_FOR_OPERATING.addLast('/');
            STRING_WAIT = "";
        }
        STRING_SHOW+="/";
        logger.info("ButtonDiv was clicked. LIST_WAIT: " + LIST_WAIT, "DivClick");
        MainJFXTextArea.setText(STRING_SHOW);

    }

    @FXML
    private void ButtonLeftBracketClick() {

        LIST_WAIT.addLast("(");
        LIST_FOR_OPERATING.addLast('(');
        expression += "(";
        STRING_WAIT = "";
        STRING_SHOW+="(";

        logger.info("LBracket was clicked. LIST_WAIT: " + LIST_WAIT, "LBracket");

        MainJFXTextArea.setText(STRING_SHOW);

    }

    @FXML
    private void ButtonRightBracketClick() {

        LIST_WAIT.addLast(STRING_WAIT);
        LIST_WAIT.addLast(")");
        expression += STRING_WAIT;
        expression += ")";
        LIST_FOR_OPERATING.addLast(STRING_WAIT);
        LIST_FOR_OPERATING.addLast(')');
        STRING_WAIT = "";
        STRING_SHOW+=")";

        logger.info("RBracket was clicked. LIST_WAIT: " + LIST_WAIT, "RBracket");

        MainJFXTextArea.setText(STRING_SHOW);

    }

    @FXML
    private void ButtonNumEquClick() {

        if (!Objects.equals(STRING_WAIT, "")) {
            if (!LIST_WAIT.isEmpty()) {
                if (LIST_OPERATE_WORD.contains(LIST_WAIT.getLast())) {
                    LIST_WAIT.addLast(STRING_WAIT);
                    LIST_FOR_OPERATING.addLast(STRING_WAIT);
                    expression += STRING_WAIT;
                } else {
                    String var = LIST_WAIT.getLast();
                    var += STRING_WAIT;
                    LIST_WAIT.removeLast();
                    LIST_WAIT.addLast(var);
                    String var1 = (String) LIST_FOR_OPERATING.getLast();
                    var1 += STRING_WAIT;
                    LIST_WAIT.removeLast();
                    LIST_WAIT.addLast(var1);
                }
            } else {
                LIST_WAIT.addLast(STRING_WAIT);
                LIST_FOR_OPERATING.addLast(STRING_WAIT);
            }
        }
        STRING_WAIT = "";
        logger.info("LIST_WAIT: " + LIST_WAIT, "operate");
        if (LIST_WAIT.isEmpty()) {
            RESULT = "0";
        } else if (LIST_WAIT.size() == 1) {
            RESULT = LIST_WAIT.getFirst();
        } else {

            if (LIST_OPERATE_WORD.contains(LIST_WAIT.getLast())) {
                RESULT = "0";
                STRING_SHOW = "";
            } else {
                try {
                    if (Objects.equals(OPERATE_MODE, "rational")) {
                        RESULT = RationalExpressionEvaluator.evaluateExpression(RationalExpressionEvaluator.parseExpression(expression)).toString();
                    } else {
                        RESULT = ExpressionCalculator.calculate(LIST_FOR_OPERATING);
                    }
                    STRING_SHOW = "";
                } catch (Exception e) {
                    logger.error("Error.", "operate", e);
                    STRING_SHOW = "Err";
                }
            }
        }
        LIST_WAIT = new ArrayList<>();
        LIST_FOR_OPERATING = new ArrayList<>();
        expression = "";
        logger.info("RESULT: " + RESULT, "operate");
        MainJFXTextArea.setText(RESULT);
        if (LIST_WAIT.isEmpty()) {
            if (!Objects.equals(RESULT, "0")) {
                LIST_WAIT.addLast(RESULT);
                LIST_FOR_OPERATING.addLast(RESULT);
                STRING_SHOW = RESULT;
                expression+=RESULT;
            }
        }

    }

    @FXML
    private void ButtonClearClick() {

        STRING_WAIT = "";
        STRING_SHOW = "";
        LIST_WAIT = new ArrayList<>();
        LIST_FOR_OPERATING = new ArrayList<>();
        expression = "";

        logger.info("STRING_WAIT: " + STRING_WAIT, "clear");

        MainJFXTextArea.setText(STRING_SHOW);

    }

    @FXML
    private void ButtonPlayClick() {

        if (PLAYING) {

            ButtonPlay.setText("▶");
            try {
                TimeUnit.MILLISECONDS.sleep(50);
            } catch (InterruptedException ignored) {

            }
            logger.info("Music stop playing...", "pause");
            player.pause();

        } else {

            ButtonPlay.setText("⏸");
            try {
                TimeUnit.MILLISECONDS.sleep(50);
            } catch (InterruptedException ignored) {

            }
            logger.info("Music continue to play...", "resume");
            player.resume();

        }
        PLAYING = !PLAYING;
    }

}
