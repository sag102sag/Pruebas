package com.example.pruebas.ui.pantallas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.pruebas.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Ajustes(
    modifier: Modifier = Modifier
)
{
    var fechaElegida: Long? by remember { mutableStateOf(null) }
    var horaElegida: TimePickerState? by remember { mutableStateOf(null) }

    var botonFechaPulsado by remember { mutableStateOf(false) }
    var botonHoraPulsado by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ){
        Button(onClick = { botonFechaPulsado = true }) {
            Text(text = "Mostrar fecha")
        }

        Button(onClick = { botonHoraPulsado = true }) {
            Text(text = "Mostrar hora")
        }

        if(botonFechaPulsado){
            DatePickerMostrado(
                onConfirm = { fecha ->
                    fechaElegida = fecha
                    botonFechaPulsado = false
                },
                onDismiss = { botonFechaPulsado = false })
        }

        if(botonHoraPulsado){
            TimePickerMostrado(
                onConfirm = { hora ->
                    horaElegida = hora
                    botonHoraPulsado = false
                },
                onDismiss = { botonHoraPulsado = false }
            )
        }

        if (fechaElegida != null) {
            val date = Date(fechaElegida!!)
            val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)

            Text(text = stringResource(R.string.fecha_seleccionada, formattedDate)+": "+ formattedDate)
        }
        else {
            Text(text = "Ninguna fecha seleccionada")
        }

        if (horaElegida != null) {
            val cal = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY, horaElegida!!.hour)
            cal.set(Calendar.MINUTE, horaElegida!!.minute)

            Text(text = "Hora seleccionada" + cal.get(Calendar.HOUR_OF_DAY) +":"+ cal.get(Calendar.MINUTE))
        }
        else {
            Text(text = "Ninguna hora seleccionada")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerMostrado(
    onConfirm: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onConfirm(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("Aceptar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerMostrado(
    onConfirm: (TimePickerState) -> Unit,
    onDismiss: () -> Unit
) {
    val horaActual = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = horaActual.get(Calendar.HOUR_OF_DAY),
        initialMinute = horaActual.get(Calendar.MINUTE),
        is24Hour = true
    )
    var mostrarDialogo by remember { mutableStateOf(true) }

    if (mostrarDialogo) {
        AlertDialog(
            text = {
                Column {
                    TimeInput(state = timePickerState)
                    //TimePicker(state = timePickerState)
                }
            },
            onDismissRequest = {
                onDismiss()
                mostrarDialogo = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirm(timePickerState)
                        mostrarDialogo = false
                    }
                ) {
                    Text(text = "Aceptar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onDismiss()
                        mostrarDialogo = false
                    }
                ) {
                    Text(text = "Cancelar")
                }
            }
        )
    }
}