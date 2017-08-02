package com.johnpetitto.orachat.ui;

import android.support.design.widget.TextInputLayout;
import android.util.Patterns;
import android.widget.EditText;

import com.johnpetitto.orachat.R;
import com.johnpetitto.orachat.StringUtils;

public abstract class InputValidator {
    private TextInputLayout layout;
    private String error;

    public InputValidator(TextInputLayout layout, String error) {
        this.layout = layout;
        this.error = error;
    }

    public abstract boolean validate(String input);

    public static boolean validateInputs(InputValidator... validators) {
        boolean allInputsValid = true;

        for (InputValidator validator : validators) {
            TextInputLayout layout = validator.layout;

            EditText editText = layout.getEditText();
            if (editText == null) {
                throw new IllegalArgumentException("TextInputLayout must contain EditText");
            }

            if (validator.validate(StringUtils.getTrimmedInput(editText))) {
                layout.setError(null);
            } else {
                layout.setError(validator.error);
                allInputsValid = false;
            }
        }

        return allInputsValid;
    }

    public static InputValidator nameValidator(TextInputLayout layout) {
        return new InputValidator(layout, layout.getContext().getString(R.string.name_error)) {
            @Override
            public boolean validate(String input) {
                return input.length() > 0;
            }
        };
    }

    public static InputValidator emailValidator(TextInputLayout layout) {
        return new InputValidator(layout, layout.getContext().getString(R.string.email_error)) {
            @Override
            public boolean validate(String input) {
                return Patterns.EMAIL_ADDRESS.matcher(input).matches();
            }
        };
    }

    public static InputValidator passwordValidator(TextInputLayout layout) {
        return new InputValidator(layout, layout.getContext().getString(R.string.password_error)) {
            @Override
            public boolean validate(String input) {
                return input.length() >= 6;
            }
        };
    }
}
