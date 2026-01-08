package com.example.pulsepost.presentation.validations;

import jakarta.validation.groups.Default;

public interface GroupValidation {
    interface Create extends Default {
    }

    interface Login extends Default {
    }
}
