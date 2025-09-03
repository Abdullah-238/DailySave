package com.techabdullah.myapplication
import DBHelper
import WalletDto
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.ComponentActivity


class MainActivity : ComponentActivity()
{

    private lateinit var dbHelper: DBHelper
    private  val currenciesType: List<String>  = listOf("USD", "RS")
    private val type: List<String> = listOf("Buy", "Sell")

    private var spinnerType: Spinner? = null
    private var spinnerCurrencyType: Spinner? = null
    private var editAmount: EditText? = null
    private var editNote: EditText? = null
    private var datePicker: DatePicker? = null
    private var amountInUSD: TextView? = null
    private var amountInRS: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main)

        _load()
    }

    private  fun _load()
    {
        spinnerType = findViewById(R.id.spinnerType)
        spinnerCurrencyType = findViewById(R.id.spinnerCurrencyType)
        editAmount = findViewById(R.id.editAmount)
        editNote = findViewById(R.id.editNote)
        datePicker = findViewById(R.id.datePicker)
        amountInUSD = findViewById(R.id.amountUSD)
        amountInRS = findViewById(R.id.amountRS)

        dbHelper = DBHelper(this)

        FillSpinners()
        loadTotals()
    }


    private fun FillSpinners() {
        val adapterCurrencies = ArrayAdapter(this, android.R.layout.simple_spinner_item, currenciesType)
        adapterCurrencies.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCurrencyType?.adapter = adapterCurrencies

        val adapterType = ArrayAdapter(this, android.R.layout.simple_spinner_item, type)
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerType?.adapter = adapterType
    }

    private fun loadTotals() {
        val  amountUSD: Double = dbHelper.calculateTotalPriceUSD()
        val amount: Double = dbHelper.calculateTotalPriceRS()

        amountInUSD?.text = amountUSD.toString()
        amountInRS?.text = amount.toString()
    }

    private fun setupSaveButton()
    {
        val editAmount: TextView = findViewById(R.id.editAmount)
        val editNote: TextView = findViewById(R.id.editNote)
        val datePicker: DatePicker = findViewById(R.id.datePicker)

        val amount = editAmount.text.toString()
        val note = editNote.text.toString()
        val type = spinnerType?.selectedItem.toString()
        val currency = spinnerCurrencyType?.selectedItem.toString()
        val day = datePicker.dayOfMonth
        val month = datePicker.month + 1  // (months are 0-based)
        val year = datePicker.year
        val date = "$day/$month/$year"

        if ( editAmount.text.isEmpty())
        {
            android.widget.Toast.makeText(this, "Please enter an amount", android.widget.Toast.LENGTH_LONG).show()
            editAmount.requestFocus()
            return
        }

        dbHelper.addWallet(
            WalletDto(
                amount = amount.toDoubleOrNull() ?: 0.0,currency = currency,
                date = date, note = note, type = type,  dateShort = "2025-09-03"
            )
        )


        android.widget.Toast.makeText(this, "Done", android.widget.Toast.LENGTH_LONG).show()

        loadTotals()

    }

    fun btnClick(view: View)
    {
        setupSaveButton()
    }


}
