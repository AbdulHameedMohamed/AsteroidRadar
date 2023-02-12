package com.udacity.asteroidradar.bindingAdapters

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.ui.main.AsteroidAdapter
import com.udacity.asteroidradar.model.Asteroid

@BindingAdapter("goneIfNotNull")
fun goneIfNotNull(view: View, it: Any?) {
    view.visibility = if (it != null) View.GONE else View.VISIBLE
}

@BindingAdapter("listData")
fun RecyclerView.bindRecyclerView(data: List<Asteroid>?) {
    if (!data.isNullOrEmpty()){
        val adapter = this.adapter as AsteroidAdapter
        adapter.submitList(data)
    }
}

@BindingAdapter("statusIcon")
fun ImageView.bindAsteroidStatusImage(isHazardous: Boolean) {

    if (isHazardous) {
        this.setImageResource(R.drawable.ic_status_potentially_hazardous)

    } else {
        this.setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("descriptionStatusIcon")
fun ImageView.bindDescAsteroidStatusImage(isHazardous: Boolean) {
    if (isHazardous) {
        this.contentDescription= "This Asteroid Is Hazard"

    } else {
        this.contentDescription= "This Asteroid Is Not Hazard"
    }
}

@BindingAdapter("asteroidStatusImage")
fun ImageView.bindDetailsStatusImage(isHazardous: Boolean) {
    if (isHazardous) {
        this.contentDescription = this.context.getString(
            R.string.potentially_hazardous_asteroid_image
        )
        this.setImageResource(R.drawable.asteroid_hazardous)
    } else {
        this.contentDescription = this.context.getString(
            R.string.not_hazardous_asteroid_image
        )
        this.setImageResource(R.drawable.asteroid_safe)
    }
}

@BindingAdapter("astronomicalUnitText")
fun TextView.bindTextViewToAstronomicalUnit(number: Double) {
    val context = this.context
    this.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun TextView.bindTextViewToKmUnit(number: Double) {
    val context = this.context
    this.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun TextView.bindTextViewToDisplayVelocity(number: Double) {
    val context = this.context
    this.text = String.format(context.getString(R.string.km_s_unit_format), number)
}

@BindingAdapter("imageUrl")
fun ImageView.bindImageToUrl(imgUrl: String?) {
    imgUrl?.let {
        Glide.with(this.context)
            .load(imgUrl)
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .error(R.drawable.ic_error)
            .into(this)
    }
}