package hr.algebra.weatherapp.dao

import android.content.Context

fun getRepository(context: Context?) = DBRepository(context)