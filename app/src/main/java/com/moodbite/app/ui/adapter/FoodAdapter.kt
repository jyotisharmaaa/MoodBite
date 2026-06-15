package com.moodbite.app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.moodbite.app.R
import com.moodbite.app.databinding.ItemFoodCardBinding
import com.moodbite.app.model.FoodItem

class FoodAdapter(private val foods: List<FoodItem>) :
    RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val binding = ItemFoodCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        holder.bind(foods[position], position)
    }

    override fun getItemCount() = foods.size

    inner class FoodViewHolder(private val binding: ItemFoodCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(food: FoodItem, position: Int) {
            binding.tvFoodEmoji.text = food.emoji
            binding.tvFoodName.text = food.name
            binding.tvBenefit.text = food.benefit
            binding.tvCategory.text = food.category.label
            binding.tvCalories.text = "${food.calories} cal"
            binding.tvIngredients.text = food.ingredients.joinToString(" · ")

            val slideIn = AnimationUtils.loadAnimation(binding.root.context, R.anim.slide_in_right)
            slideIn.startOffset = (position * 80).toLong()
            binding.root.startAnimation(slideIn)
        }
    }
}
