package com.example.chatagent

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@BindingAdapter("formattedDate")
fun TextView.setFormattedDate(date: Long){
    val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val dateString = formatter.format(Date(date))
    this.text = dateString
}