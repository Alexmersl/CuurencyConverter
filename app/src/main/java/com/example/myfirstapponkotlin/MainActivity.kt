package com.example.myfirstapponkotlin

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.myfirstapponkotlin.notification.NotificationManager
import com.example.myfirstapponkotlin.ui.CurrencyChart
import com.example.myfirstapponkotlin.ui.CurrencyViewModel
import com.example.myfirstapponkotlin.worker.CurrencyUpdateWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    private lateinit var viewModel: CurrencyViewModel
    private lateinit var notificationManager: NotificationManager
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = CurrencyViewModel()
        viewModel.initialize(this)
        notificationManager = NotificationManager(this)
        sharedPreferences = getSharedPreferences("CurrencyHistoryPrefs", Context.MODE_PRIVATE)

        setupPeriodicUpdates()
        setupComposeUI()
    }

    private fun setupPeriodicUpdates() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val updateRequest = PeriodicWorkRequestBuilder<CurrencyUpdateWorker>(
            24, TimeUnit.HOURS,
            1, TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                CurrencyUpdateWorker.WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                updateRequest
            )
    }

    private fun setupComposeUI() {
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CurrencyConverterScreen(viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyConverterScreen(viewModel: CurrencyViewModel) {
    var fromCurrency by remember { mutableStateOf("") }
    var toCurrency by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    // Get currency list
    val currencyList = remember { mutableStateListOf<String>() }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    // Search states
    var fromSearchText by remember { mutableStateOf("") }
    var toSearchText by remember { mutableStateOf("") }
    var fromExpanded by remember { mutableStateOf(false) }
    var toExpanded by remember { mutableStateOf(false) }

    // Filtered lists based on search
    val filteredFromCurrencies = remember(currencyList, fromSearchText) {
        if (fromSearchText.isEmpty()) {
            currencyList
        } else {
            currencyList.filter { it.contains(fromSearchText, ignoreCase = true) }
        }
    }
    val filteredToCurrencies = remember(currencyList, toSearchText) {
        if (toSearchText.isEmpty()) {
            currencyList
        } else {
            currencyList.filter { it.contains(toSearchText, ignoreCase = true) }
        }
    }

    // Load currencies
    LaunchedEffect(Unit) {
        try {
            withContext(Dispatchers.IO) {
                val currencies = HttpConnectionClass.getCurrencySet()
                println("Retrieved currencies: $currencies")
                if (currencies.isNotEmpty()) {
                    currencyList.clear()
                    currencyList.addAll(currencies.sorted())
                    println("Updated currencyList: $currencyList")
                    // Set default currencies if not set
                    if (fromCurrency.isEmpty() && currencyList.isNotEmpty()) {
                        fromCurrency = currencyList[0]
                        fromSearchText = currencyList[0]
                    }
                    if (toCurrency.isEmpty() && currencyList.size > 1) {
                        toCurrency = currencyList[1]
                        toSearchText = currencyList[1]
                    }
                } else {
                    error = "No currencies available"
                }
            }
            isLoading = false
        } catch (e: Exception) {
            println("Error loading currencies: ${e.message}")
            error = e.message ?: "Failed to load currencies"
            isLoading = false
        }
    }

    // Show result or error in Snackbar
    LaunchedEffect(uiState.conversionResult, uiState.error) {
        when {
            uiState.error != null -> {
                snackbarHostState.showSnackbar(
                    message = uiState.error!!,
                    duration = SnackbarDuration.Short
                )
            }
            uiState.conversionResult != null -> {
                snackbarHostState.showSnackbar(
                    message = "$amount $fromCurrency = ${String.format("%.2f", uiState.conversionResult)} $toCurrency",
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Currency Converter") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            } else if (error != null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = "Error",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Error loading currencies",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = error ?: "Unknown error",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            } else {
                // Amount input
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .testTag("amountInput"),
                    shape = MaterialTheme.shapes.large,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    singleLine = true
                )

                // Currency selection row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // From Currency dropdown
                    ExposedDropdownMenuBox(
                        expanded = fromExpanded,
                        onExpandedChange = { fromExpanded = it },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = fromSearchText,
                            onValueChange = { 
                                fromSearchText = it
                                fromExpanded = true
                            },
                            label = { Text("From") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = fromExpanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .padding(end = 8.dp)
                                .fillMaxWidth()
                                .testTag("fromCurrency"),
                            shape = MaterialTheme.shapes.large,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            ),
                            singleLine = true,
                            readOnly = false,
                            enabled = true
                        )
                        ExposedDropdownMenu(
                            expanded = fromExpanded,
                            onDismissRequest = { fromExpanded = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            filteredFromCurrencies.forEach { currency ->
                                DropdownMenuItem(
                                    text = { Text(currency) },
                                    onClick = {
                                        fromCurrency = currency
                                        fromSearchText = currency
                                        fromExpanded = false
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                    // Swap currencies button
                    IconButton(
                        onClick = {
                            val temp = fromCurrency
                            fromCurrency = toCurrency
                            toCurrency = temp
                            val tempSearch = fromSearchText
                            fromSearchText = toSearchText
                            toSearchText = tempSearch
                        },
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SwapHoriz,
                            contentDescription = "Swap currencies",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    // To Currency dropdown
                    ExposedDropdownMenuBox(
                        expanded = toExpanded,
                        onExpandedChange = { toExpanded = it },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = toSearchText,
                            onValueChange = { 
                                toSearchText = it
                                toExpanded = true
                            },
                            label = { Text("To") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = toExpanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .padding(start = 8.dp)
                                .fillMaxWidth()
                                .testTag("toCurrency"),
                            shape = MaterialTheme.shapes.large,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            ),
                            singleLine = true,
                            readOnly = false,
                            enabled = true
                        )
                        ExposedDropdownMenu(
                            expanded = toExpanded,
                            onDismissRequest = { toExpanded = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            filteredToCurrencies.forEach { currency ->
                                DropdownMenuItem(
                                    text = { Text(currency) },
                                    onClick = {
                                        toCurrency = currency
                                        toSearchText = currency
                                        toExpanded = false
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }

                // Convert button
                Button(
                    onClick = {
                        if (amount.isEmpty()) {
                            viewModel.setError("Please enter an amount")
                        } else if (fromCurrency.isEmpty() || toCurrency.isEmpty()) {
                            viewModel.setError("Please select currencies")
                        } else {
                            amount.toDoubleOrNull()?.let {
                                viewModel.convert(fromCurrency, toCurrency, it)
                            } ?: viewModel.setError("Invalid amount")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .testTag("convertButton"),
                    shape = MaterialTheme.shapes.large,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Convert", style = MaterialTheme.typography.titleMedium)
                }

                // Action buttons row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // History button
                    OutlinedButton(
                        onClick = {
                            val intent = Intent(context, MainActivity3::class.java)
                            context.startActivity(intent)
                        },
                        shape = MaterialTheme.shapes.large,
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "History",
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text("История")
                    }

                    // Offline mode button
                    OutlinedButton(
                        onClick = {
                            val intent = Intent(context, MainActivity2::class.java)
                            context.startActivity(intent)
                        },
                        shape = MaterialTheme.shapes.large,
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudOff,
                            contentDescription = "Offline Mode",
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text("Оффлайн")
                    }
                }

                // Task Planner button at the bottom
                OutlinedButton(
                    onClick = {
                        val intent = Intent(context, TaskPlannerActivity::class.java)
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = MaterialTheme.shapes.large,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Task Planner",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text("Планировщик задач")
                }

                // History section
                Text(
                    text = "Recent Conversions",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )

                LazyColumn {
                    items(uiState.history) { conversion ->
                        HistoryItem(conversion)
                    }
                }

                // Add chart after the conversion result
                if (fromCurrency.isNotEmpty() && toCurrency.isNotEmpty() && uiState.historicalRates.isNotEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = MaterialTheme.shapes.large
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Historical Exchange Rate",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            CurrencyChart(
                                historicalRates = uiState.historicalRates,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }

                // Result text
                Text(
                    text = "$amount $fromCurrency = ${String.format("%.2f", uiState.conversionResult)} $toCurrency",
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("resultText"),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryItem(conversion: com.example.myfirstapponkotlin.data.ConversionHistory) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "${conversion.amount} ${conversion.fromCurrency} → ${conversion.result} ${conversion.toCurrency}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = conversion.timestamp.toString(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }
    }
}
