package com.cook_recipe_app.firebase

import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cook_recipe_app.firebase.databinding.IngredientListBinding
import com.cook_recipe_app.firebase.databinding.MenuImageItemBinding

class BibimbabAdapter(
    private val imageUrl: String,
    private val items: List<IngredientItem>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_IMAGE = 0
        private const val VIEW_TYPE_INGREDIENT = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_IMAGE else VIEW_TYPE_INGREDIENT
    }

    override fun getItemCount(): Int {
        return items.size + 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_IMAGE -> {
                val binding = MenuImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ImageViewHolder(binding)
            }
            VIEW_TYPE_INGREDIENT -> {
                val binding = IngredientListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                BibimbabViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == 0 && holder is ImageViewHolder) {
            holder.bind(imageUrl)
        } else if (position > 0 && holder is BibimbabViewHolder) {
            val item = items[position - 1].name
            holder.bind(item)
        }
    }

    inner class ImageViewHolder(private val binding: MenuImageItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imageUrl: String) {
            Glide.with(binding.root.context)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.ic_user)
                .into(binding.menuImageView)
        }
    }

    inner class BibimbabViewHolder(private val binding: IngredientListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            val spannable = SpannableString(item)
            val timePattern = Regex("(\\d+시간)?(\\d+분)?(\\d+초)?")

            timePattern.findAll(item).forEach { matchResult ->
                val start = matchResult.range.first
                val end = matchResult.range.last + 1
                val matchedText = matchResult.value

                spannable.setSpan(object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        navigateToTimerFragment(matchedText)
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.isUnderlineText = false
                        ds.color = Color.BLUE
                    }
                }, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

                spannable.setSpan(
                    StyleSpan(Typeface.BOLD),
                    start,
                    end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannable.setSpan(
                    ForegroundColorSpan(Color.BLUE),
                    start,
                    end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            binding.itemTextView.text = spannable
            binding.itemTextView.movementMethod = LinkMovementMethod.getInstance()

            if (item == "재료" || item == "양념장" || item == "요리 방법") {
                binding.itemTextView.setTypeface(null, Typeface.BOLD)
            } else {
                binding.itemTextView.setTypeface(null, Typeface.NORMAL)
            }
        }

        private fun navigateToTimerFragment(timeString: String) {
            val timerFragment = TimerFragment.newInstance(timeString)
            (binding.root.context as? FragmentActivity)?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_container, timerFragment)
                ?.addToBackStack(null)
                ?.commit()
        }
    }
}
