package com.ia.mchaveza.kotlin_library

import android.support.design.widget.TextInputLayout
import android.widget.CheckBox
import android.widget.EditText

object ValidatorUtils {

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

    /**
     * Checks if two fields have the same content
     * @param originalField  -> The field we wanna compare
     * @param containerToDisplayError -> Indicates where we will display the error
     * @param fieldInsideContainer -> Another field that we will use to compare
     * @param errorMessage -> Message to display in case of error
     */
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

    /**
     * Checks if rfc provided is valid. It also checks for the verifying digit.
     * @param container -> Where we will display error
     * @param field -> The filed that contains the rfc
     * @param wrongFormatMessage -> The message we will show
     */
    fun isValidRFC(container: TextInputLayout, field: EditText, wrongFormatMessage: String): Boolean {
        var isValid = true
        val input = field.text.toString()
        val pattern = "^([A-ZÑ\\x26]{3,4}([0-9]{2})(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1]))([A-Z\\d]{3})?\$".toRegex()

        if (input.matches(pattern)) {
            val extractedVerifyingDigit = input[input.length - 1].toString()
            val rfcWithoutVerifyingDigit = input.dropLast(1)
            val lengthOfModifiedRFC = rfcWithoutVerifyingDigit.length
            var checkSum = calculateCheckSum(rfcWithoutVerifyingDigit)
            for (i in 0 until lengthOfModifiedRFC) {
                checkSum += getDictionaryValue(rfcWithoutVerifyingDigit[i]) * (input.length - i)
            }
            val module: Int = checkSum % 11
            val verifyingDigit = getVerifyingDigit(module)
            if (verifyingDigit != extractedVerifyingDigit) {
                isValid = false
                container.error = wrongFormatMessage
                return isValid
            }
        } else {
            isValid = false
            container.error = wrongFormatMessage
            return isValid
        }

        container.error = null
        container.isErrorEnabled = false
        return isValid
    }

    fun patternMatches(container: TextInputLayout, field: EditText, pattern: String, errorMessage: String): Boolean {
        var isValid = true
        val regex = pattern.toRegex()
        if (!field.text.toString().matches(regex)) {
            isValid = false
            container.error = errorMessage
            return isValid
        }
        container.error = null
        container.isErrorEnabled = false
        return isValid
    }

    private fun calculateCheckSum(rfc: String): Int =
            if (rfc.length == 12) {
                0
            } else {
                481
            }

    private fun getVerifyingDigit(module: Int) = when {
        module == 0 -> {
            module.toString()
        }
        module > 0 -> {
            (11 - module).toString()
        }
        module == 10 -> {
            "A"
        }
        else -> {
            "0"
        }
    }

    private fun getDictionaryValue(digit: Char)
            : Int = when (digit) {
        '0' -> 0
        '1' -> 1
        '2' -> 2
        '3' -> 3
        '4' -> 4
        '5' -> 5
        '6' -> 6
        '7' -> 7
        '8' -> 8
        '9' -> 9
        'A' -> 10
        'B' -> 11
        'C' -> 12
        'D' -> 13
        'E' -> 14
        'F' -> 15
        'G' -> 16
        'H' -> 17
        'I' -> 18
        'J' -> 19
        'K' -> 20
        'L' -> 21
        'M' -> 22
        'N' -> 23
        'Ñ' -> 38
        'O' -> 25
        'P' -> 26
        'Q' -> 27
        'R' -> 28
        'S' -> 29
        'T' -> 30
        'U' -> 31
        'V' -> 32
        'W' -> 33
        'X' -> 34
        'Y' -> 35
        'Z' -> 36
        '&' -> 24
        else -> {
            0
        }
    }
}