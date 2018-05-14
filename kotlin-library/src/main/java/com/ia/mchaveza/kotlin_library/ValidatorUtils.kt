package com.ia.mchaveza.kotlin_library

import android.support.design.widget.TextInputLayout
import android.widget.CheckBox
import android.widget.EditText

class ValidatorUtils {

    companion object {
        /**
         * Checks if field is not empty and if the field has an email valid format
         * @param container to display error
         * @param field to validate
         * @param wrongFormat message to display if is not email format
         * @param emptyField message to display if field is empty
         * @return TRUE -> Valid email format
         * @return FALSE -> Invalid email format
         */
        fun isEmailValid(container: TextInputLayout, field: EditText, wrongFormat: String, emptyField: String): Boolean {
            var isValid = isEmptyField(container, field, emptyField)
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(field.text.toString().trim()).matches()) {
                isValid = false
                container.error = wrongFormat
                return isValid
            }
            container.error = null
            container.isErrorEnabled = false
            return isValid
        }

        /**
         * Checks if field is or not empty
         * @param container to display error
         * @param field to validate
         * @param emptyField message to display if field is empty
         * @return TRUE -> Field is not empty
         * @return FALSE -> Field is empty
         */
        fun isEmptyField(container: TextInputLayout, field: EditText, emptyField: String): Boolean {
            var isValid = true
            if (field.text.toString().trim().isEmpty()) {
                isValid = false
                container.error = emptyField
                return isValid
            }
            container.error = null
            container.isErrorEnabled = false
            return isValid
        }

        /**
         * Checks if field is long enough
         * @param container to display error
         * @param field to validate
         * @param long to determine how long the field should be
         * @param fieldMessage to display if field fulfill condition
         * @return TRUE -> Field is not empty
         * @return FALSE -> Field is empty
         */
        fun isFieldLongEnough(container: TextInputLayout, field: EditText, long: Int, fieldMessage: String): Boolean {
            var isValid = true
            if (field.text.toString().trim().length < long) {
                isValid = false
                container.error = fieldMessage
                return isValid
            }
            container.error = null
            container.isErrorEnabled = false
            return isValid
        }

        /**
         * Checks if checkbox provided is checked.
         * @param checkbox to validate
         * @return TRUE if is checked
         * @return FALSE if unchecked
         */
        fun isCheckboxChecked(checkbox: CheckBox): Boolean =
                checkbox.isChecked

        fun areFieldsTheSame(originalField: EditText, containerToDisplayError: TextInputLayout, fieldInsideContainer: EditText, errorMessage: String): Boolean {
            var isValid = true
            if (originalField.text.toString().trim() != fieldInsideContainer.text.toString().trim()) {
                isValid = false
                containerToDisplayError.error = errorMessage
                return isValid
            }
            containerToDisplayError.error = null
            containerToDisplayError.isErrorEnabled = false
            return isValid
        }
    }

}