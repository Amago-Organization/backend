package com.example.amago.core.utils.validations;

import jakarta.validation.groups.Default;

public interface GroupValidation {
    interface Create extends Default {
    }

    interface Login extends Default {
    }
}
