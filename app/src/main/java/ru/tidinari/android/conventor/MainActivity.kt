package ru.tidinari.android.conventor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.tidinari.android.conventor.exchange.ExchangeViewModel
import ru.tidinari.android.conventor.ui.theme.ConventorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ConventorTheme {
                var rubles by remember {
                    mutableStateOf(0)
                }
                var exchange by remember {
                    mutableStateOf(0f)
                }
                val exchangeViewModel = viewModel<ExchangeViewModel>()
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background)
                ) {
                    Text(
                        text = stringResource(R.string.header),
                        style = MaterialTheme.typography.h4
                    )
                    Row {
                        TextField(
                            value = rubles.toString(),
                            onValueChange = {
                                if (it.isEmpty()) {
                                    rubles = 0
                                } else if (rubles == 0 && it.isDigitsOnly()) {
                                    it.replace("0", "").let { replacedString ->
                                        if (replacedString.isNotEmpty()) {
                                            rubles = replacedString.toInt()
                                        }
                                    }
                                } else if (it.isDigitsOnly()) {
                                    rubles = it.toInt()
                                }
                                if (exchangeViewModel.selectedCurrency != null) {
                                    exchange = exchangeViewModel.convert(rubles)
                                }
                            },
                            textStyle = MaterialTheme.typography.h4,
                            singleLine = true,
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(3f)
                        )
                        Text(
                            text = "рублей ${ if (exchangeViewModel.selectedCurrency != null) "- это $exchange ${exchangeViewModel.selectedCurrency?.name}" else ""}",
                            style = MaterialTheme.typography.body1,
                            modifier = Modifier.weight(5f)
                        )
                    }
                    LazyColumn {
                        items(exchangeViewModel.getCurrency()) { curr ->
                            Text(
                                text = "${curr.charCode} > ${curr.name}",
                                style = MaterialTheme.typography.body2,
                                modifier = Modifier.clickable {
                                    exchangeViewModel.selectedCurrency = curr
                                    exchange = exchangeViewModel.convert(rubles, curr)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}