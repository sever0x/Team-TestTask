@file:OptIn(ExperimentalMaterial3Api::class)

package com.shdwraze.team_testtask

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.util.Calendar

@ExperimentalMaterial3Api
@Composable
fun NewPlayerScreen(
    modifier: Modifier = Modifier,
    onCreateNewPlayerClicked: (String, String, Int) -> Unit,
    navController: NavController
) {
    var name by remember {
        mutableStateOf("")
    }
    var surname by remember {
        mutableStateOf("")
    }
    var yearOfBirth by remember {
        mutableStateOf("")
    }

    val paddingMedium = dimensionResource(id = R.dimen.padding_m)
    val paddingLarge = dimensionResource(id = R.dimen.padding_l)

    val isButtonEnabled = name.isNotBlank() && surname.isNotBlank() && yearOfBirth.isNotBlank()
    val isYearOfBirthValid = yearOfBirth.isBlank() || (yearOfBirth.toIntOrNull() != null &&
            yearOfBirth.toInt() >= 1900 && yearOfBirth.toInt() <= Calendar.getInstance()
        .get(Calendar.YEAR))

    Column(
        modifier = modifier
            .padding(
                top = paddingLarge, start = paddingMedium,
                end = paddingMedium, bottom = paddingLarge
            )
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CustomTextField(
            value = surname,
            onValueChange = { surname = it },
            label = stringResource(R.string.enter_surname),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )
        CustomTextField(
            value = name, onValueChange = { name = it },
            label = stringResource(R.string.enter_name),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )
        CustomTextField(
            value = yearOfBirth,
            onValueChange = { yearOfBirth = it },
            label = stringResource(R.string.enter_year_of_birth),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            isError = !isYearOfBirthValid,
            errorMessage = stringResource(
                R.string.incorrect_year_of_birth,
                Calendar.getInstance().get(Calendar.YEAR)
            )
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                onCreateNewPlayerClicked(name, surname, yearOfBirth.toInt())
                navController.navigateUp()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.hsl(
                    131f, 0.71f, 0.39f, 1f
                )
            ),
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.button_corner_shape_small)),
            enabled = isButtonEnabled
        ) {
            Text(text = stringResource(R.string.save))
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardOptions: KeyboardOptions,
    isError: Boolean = false,
    errorMessage: String = ""
) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        label = {
            Text(text = if (isError) errorMessage else label)
        },
        keyboardOptions = keyboardOptions,
        modifier = modifier.fillMaxWidth(),
        isError = isError,
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = if (isError) Color.Red else MaterialTheme.colorScheme.primary
        ),
        trailingIcon = {
            if (isError) {
                Icon(imageVector = Icons.Default.Info, contentDescription = null)
            }
        }
    )
}