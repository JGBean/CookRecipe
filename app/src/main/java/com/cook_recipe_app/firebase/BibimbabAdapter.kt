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
    private val imageUrl: String, // 요리 이미지 URL
    private val items: List<IngredientItem> // 재료 리스트
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_IMAGE = 0
        private const val VIEW_TYPE_INGREDIENT = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_IMAGE else VIEW_TYPE_INGREDIENT
    }

    override fun getItemCount(): Int {
        return items.size + 1 // 이미지 1개 + 재료 리스트 크기
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
            holder.bind(imageUrl) // 첫 번째 아이템에 이미지 URL 바인딩
        } else if (position > 0 && holder is BibimbabViewHolder) {
            val item = items[position - 1].name // 첫 번째 아이템이 이미지이므로 인덱스 보정
            holder.bind(item)
        }
    }

    // 요리 이미지 ViewHolder
    inner class ImageViewHolder(private val binding: MenuImageItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imageUrl: String) {
            Glide.with(binding.root.context)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder) // 기본 이미지
                .error(R.drawable.ic_user) // 에러 시 대체 이미지
                .into(binding.menuImageView)
        }
    }

    // 재료 리스트 ViewHolder
    inner class BibimbabViewHolder(private val binding: IngredientListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            // 강조 처리: 시간 관련 패턴 감지
            val spannable = SpannableString(item)
            val timePattern = Regex("(\\d+시간|\\d+분)")

            timePattern.findAll(item).forEach { matchResult ->
                val start = matchResult.range.first
                val end = matchResult.range.last + 1

                // 클릭 이벤트 추가
                spannable.setSpan(object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        navigateToTimerFragment() // 타이머 프래그먼트로 이동
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.isUnderlineText = false // 밑줄 제거
                        ds.color = Color.BLUE // 텍스트 색상 변경 (선택 사항)
                    }
                }, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

                // 스타일 강조 (예: 굵게 및 색상 변경)
                spannable.setSpan(
                    StyleSpan(Typeface.BOLD), // 굵게
                    start,
                    end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannable.setSpan(
                    ForegroundColorSpan(Color.BLUE), // 빨간색
                    start,
                    end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            // 텍스트 설정
            binding.itemTextView.text = spannable

            // 클릭 이벤트 활성화
            binding.itemTextView.movementMethod = LinkMovementMethod.getInstance()

            // Bold 처리 조건 (제목과 관련된 항목에 대해)
            if (item == "재료" || item == "양념장" || item == "요리 방법") {
                binding.itemTextView.setTypeface(null, android.graphics.Typeface.BOLD)
            } else {
                binding.itemTextView.setTypeface(null, android.graphics.Typeface.NORMAL)
            }
        }

        private fun navigateToTimerFragment() {
            val timerFragment = TimerFragment()
            (binding.root.context as? FragmentActivity)?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_container, timerFragment)
                ?.addToBackStack(null)
                ?.commit()
        }
    }
}