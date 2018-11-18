package sample;

import javafx.scene.control.TextField;

public class MyTextField extends TextField {

    public MyTextField() {
        setMinWidth(25);
        setMaxWidth(100);
    }

    public void replaceText(int start, int end, String text) {
        String oldValue = getText();
        if (!text.matches("[A-Za-z]") && !text.matches("[\\\\!\"#$%&() '*+,./:;<=->?@\\[\\]^_{|}~]+")
                && !text.matches("[А-Яа-я]")) {
            super.replaceText(start, end, text);
        }
    }

    public void replaceSelection(String text) {
        String oldValue = getText();
        if (!text.matches("[A-Za-z]") && !text.matches("[\\\\!\"#$%&() '*+,./:;<-=>?@\\[\\]^_{|}~]+")
                && !text.matches("[А-Яа-я]")) {
            super.replaceSelection(text);
        }
    }

}
