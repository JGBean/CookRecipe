package com.cook_recipe_app.firebase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.cook_recipe_app.firebase.databinding.FragmentBibimbabBinding
import com.cook_recipe_app.firebase.viewmodel.BibimbabViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class BibimbabFragment : Fragment() {
    private var _binding: FragmentBibimbabBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BibimbabViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBibimbabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuId = arguments?.getString("menuId")
        if (menuId == null) {
            Log.e("BibimbabFragment", "menuId is null")
            return
        }

        // 사용자 ID 가져오기
        viewModel.fetchUserId()

        // 좋아요 상태 초기화
        viewModel.userId.observe(viewLifecycleOwner) { userId ->
            if (userId != null) {
                viewModel.checkIsLiked(menuId) // 메뉴 좋아요 상태 확인
            }
        }

        // 좋아요 버튼 상태 반영
        viewModel.isLiked.observe(viewLifecycleOwner) { isLiked ->
            val likeButtonImage = if (isLiked) R.drawable.ic_heart_full else R.drawable.ic_heart_empty
            binding.likeButton.setImageResource(likeButtonImage)
        }

        // 좋아요 버튼 클릭 리스너
        binding.likeButton.setOnClickListener {
            viewModel.toggleLike(menuId)
        }

        // 제목 및 데이터 설정
        viewModel.fetchMenuData(menuId)
        viewModel.menuTitle.observe(viewLifecycleOwner) { title ->
            binding.menuTitle.text = title
        }

        viewModel.menuItems.observe(viewLifecycleOwner) { items ->
            binding.bibimbabRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.bibimbabRecyclerView.adapter = BibimbabAdapter(items)
        }

        // 메뉴 이미지 가져오기
        viewModel.fetchImageUrl(menuId) // 이미지 URL을 가져오기 위해 호출
        viewModel.imageUrl.observe(viewLifecycleOwner) { imageUrl ->
            Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)  // 이미지를 로드하기 전에 보여줄 기본 이미지
                .error(R.drawable.ic_user)  // 에러 발생 시 대체 이미지
                .into(binding.menuImage)  // 이미지를 실제 ImageView에 표시
        }

        viewModel.userId.observe(viewLifecycleOwner) { userId ->
            if (userId != null) {
                viewModel.checkIsLiked(menuId) // 메뉴 좋아요 상태 확인
            }
        }

        // 타이머 버튼 클릭 리스너
        binding.timer.setOnClickListener {
            navigateToTimerFragment()
        }

    }


    private fun navigateToTimerFragment() {
        val timerFragment = TimerFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, timerFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

